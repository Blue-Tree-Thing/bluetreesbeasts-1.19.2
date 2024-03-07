package net.fabricmc.bluetreebeasts.entities.custom;

import net.fabricmc.bluetreebeasts.entities.ModEntities;
import net.fabricmc.bluetreebeasts.items.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.predicate.block.BlockStatePredicate;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import java.util.EnumSet;
import java.util.function.Predicate;

public class WoollyGigantelopeEntity extends AnimalEntity implements IAnimatable {

    @Nullable
    private final AnimationFactory factory = new AnimationFactory(this);
    private static final AnimationBuilder walking_animation = new AnimationBuilder().addAnimation("animation.woolly_gigantelope.walk", true);
    private static final AnimationBuilder foraging_animation = new AnimationBuilder().addAnimation("animation.woolly_gigantelope.feed", true);
    private static final AnimationBuilder idle_animation = new AnimationBuilder().addAnimation("animation.woolly_gigantelope.idle", true);
    private static final AnimationBuilder baby_idle_animation = new AnimationBuilder().addAnimation("animation.baby_gigantelope.idle", true);
    private static final AnimationBuilder baby_walking_animation = new AnimationBuilder().addAnimation("animation.baby_gigantelope.walk", true);
    private int eatGrassTimer;
    public static boolean isDigging;

    public WoollyGigantelopeEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return AnimalEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 40)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, .4f)
                .add(EntityAttributes.GENERIC_ARMOR, 4f);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new LookAtEntityGoal(this, LivingEntity.class, 20.0f));
        this.goalSelector.add(1, new WanderAroundFarGoal(this, .4f));
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(1, new EscapeDangerGoal(this, .6));
        this.goalSelector.add(3, new LookAroundGoal(this));
        this.goalSelector.add(2, new GigantelopeForageGoal(this));

        this.targetSelector.add(1, new AnimalMateGoal(this,1));

    }

    @Override
    protected void mobTick() {
        this.eatGrassTimer = GigantelopeForageGoal.getTimer();
        super.mobTick();
    }

    @Override
    public void tickMovement() {
        if (this.world.isClient) {
            this.eatGrassTimer = Math.max(0, this.eatGrassTimer - 1);
        }
        super.tickMovement();
    }

    static class GigantelopeForageGoal extends Goal{
        private static int timer;
        private static final Predicate<BlockState> SNOW_LAYER_PREDICATE = BlockStatePredicate.forBlock(Blocks.SNOW);
        private final WoollyGigantelopeEntity woollyGigantelope;

        public GigantelopeForageGoal(WoollyGigantelopeEntity mob) {
            this.woollyGigantelope = mob;
            woollyGigantelope.world = mob.world;
            this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK, Goal.Control.JUMP));
        }
        @Override
        public boolean canStart() {
            if (this.woollyGigantelope.getRandom().nextInt(this.woollyGigantelope.isBaby() ? 25 : 500) != 0) {
                return false;
            }
            BlockPos blockPos = this.woollyGigantelope.getBlockPos();
            if (SNOW_LAYER_PREDICATE.test(this.woollyGigantelope.world.getBlockState(blockPos))) {
                return true;
            }
            return this.woollyGigantelope.world.getBlockState(blockPos.down()).isOf(Blocks.SNOW);
        }

        @Override
        public void start() {
            timer = this.getTickCount(40);

            this.woollyGigantelope.world.sendEntityStatus(this.woollyGigantelope, EntityStatuses.SET_SHEEP_EAT_GRASS_TIMER_OR_PRIME_TNT_MINECART);
            this.woollyGigantelope.getNavigation().stop();
        }

        @Override
        public void stop() {
            timer = 0;

        }

        @Override
        public boolean shouldContinue() {
            return timer > 0;
        }

        public static int getTimer() {
            return timer;
        }

        @Override
        public void tick() {
            timer = Math.max(0, timer - 1);
            if (timer != this.getTickCount(4)) {
                return;
            }
            BlockPos blockPos = this.woollyGigantelope.getBlockPos();
            if (SNOW_LAYER_PREDICATE.test(this.woollyGigantelope.world.getBlockState(blockPos))) {
                if (this.woollyGigantelope.world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) {
                    this.woollyGigantelope.world.breakBlock(blockPos, false);
                    isDigging = true;
                }
                this.woollyGigantelope.onEatingGrass();
            } else {
                BlockPos blockPos2 = blockPos.down();
                if (this.woollyGigantelope.world.getBlockState(blockPos2).isOf(Blocks.SNOW)) {
                    if (this.woollyGigantelope.world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) {
                        isDigging = true;
                        this.woollyGigantelope.world.syncWorldEvent(WorldEvents.BLOCK_BROKEN, blockPos2, Block.getRawIdFromState(Blocks.SNOW.getDefaultState()));
                        this.woollyGigantelope.world.setBlockState(blockPos2, Blocks.AIR.getDefaultState(), Block.NOTIFY_LISTENERS);

                    }
                    this.woollyGigantelope.onEatingGrass();
                }
            }
            isDigging = false;
        }
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.getItem() == Items.WHEAT;
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
            return super.interactMob(player, hand);

    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return ModEntities.WOOLLYGIGANTELOPE.create(world);
    }



    private PlayState predicate (AnimationEvent event) {
        if (!event.isMoving()) {
            event.getController().setAnimation(idle_animation);
            return PlayState.CONTINUE;
        } else if(event.isMoving()){
            event.getController().setAnimation(walking_animation);}
        return PlayState.CONTINUE;
    }
    private PlayState babyPredicate (AnimationEvent event) {
        if (this.isBaby() && !event.isMoving()) {
            event.getController().setAnimation(baby_idle_animation);
            return PlayState.CONTINUE;
        } else if(this.isBaby() &&event.isMoving()){
            event.getController().setAnimation(baby_walking_animation);}
        return PlayState.CONTINUE;
    }

    private PlayState feedPredicate (AnimationEvent event) {
        if (isDigging) {
            event.getController().setAnimation(foraging_animation);
            return PlayState.CONTINUE;
        }
        return PlayState.CONTINUE;
    }


    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this,"controller", 0, this::predicate));
        animationData.addAnimationController(new AnimationController(this,"baby_controller", 0, this::babyPredicate));
        animationData.addAnimationController(new AnimationController(this,"feed_controller", 0, this::feedPredicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
