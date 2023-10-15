package net.fabricmc.bluetreebeasts.entities.custom;

import net.fabricmc.bluetreebeasts.sounds.ModSounds;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;




public class HellBenderEntity extends HostileEntity implements IAnimatable {
    private final AnimationFactory factory = new AnimationFactory(this);
    private static final AnimationBuilder chase_animation = new AnimationBuilder().addAnimation("animation.hell_bender.chase", true);
    private static final AnimationBuilder sleep_animation = new AnimationBuilder().addAnimation("animation.hell_bender.sleep", true);
    private PlayerEntity player;
    private final ServerBossBar bossBar = (ServerBossBar)new ServerBossBar(this.getDisplayName(), BossBar.Color.GREEN, BossBar.Style.PROGRESS).setDarkenSky(false);


    public HellBenderEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
        this.setHealth(this.getMaxHealth());
        this.setPersistent();
        this.checkDespawn();
        this.navigation = new HellBenderNavigation(this, world);
        this.setPathfindingPenalty(PathNodeType.LAVA, 0f);
        this.moveControl = getMoveControl();
        this.experiencePoints = 100;
    }
    @Override
    protected EntityNavigation createNavigation(World world) {
        return new HellBenderNavigation(this, world);
    }


    public static DefaultAttributeContainer.Builder setAttributes() {
        return AnimalEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 300)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 1.5f)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 12f)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 6f)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 1f)
                .add(EntityAttributes.GENERIC_ARMOR, 4f);
    }
    @Override
    protected void initGoals() {

            this.goalSelector.add(1, new HalfHealthGoal(this));
            this.goalSelector.add(2, new PlaceFireTrailGoal(this));
            this.goalSelector.add(2, new SwimGoal(this));
            this.goalSelector.add(1, new RevengeGoal(this));
            this.goalSelector.add(3, new MeleeAttackGoal(this, .5, true));
            this.goalSelector.add(3, new PounceAtTargetGoal(this, 0.6f));


        this.targetSelector.add(2, new ActiveTargetGoal<>( this, PlayerEntity.class, true));
        this.targetSelector.add(2, new ActiveTargetGoal<>( this, WornGloveEntity.class, true));




    }
    @Override
    public boolean isOnFire() {
        return false;
    }


    @Override
    public boolean disablesShield() {
        return true;
    }

    static class PlaceFireTrailGoal extends Goal {
        private final HellBenderEntity hellbender;
        PlaceFireTrailGoal(HellBenderEntity hellbender) {
            this.hellbender = hellbender;
        }
        @Override
        public boolean canStart() {
            return this.hellbender.world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING) || this.hellbender.onGround;
        }
        public void tick() {
            int i = MathHelper.floor(this.hellbender.getX());
            int j = MathHelper.floor(this.hellbender.getY());
            int k = MathHelper.floor(this.hellbender.getZ());
            BlockPos blockPos = new BlockPos(i, j, k);
            if(this.hellbender.world.isAir(blockPos)) {
                this.hellbender.world.setBlockState(blockPos, AbstractFireBlock.getState(this.hellbender.world, blockPos));
            }
        }
    }

    static class HalfHealthGoal extends Goal{
        private final HellBenderEntity hellbender;


        HalfHealthGoal(HellBenderEntity hellbender) {
            this.hellbender = hellbender;
        }

        @Override
        public boolean canStart() {
            return true;
        }

        public void tick(){
            if(hellbender.getHealth() <= 200){

                hellbender.bossBar.setColor(BossBar.Color.YELLOW);



                if(hellbender.getHealth() <= 100){

                    hellbender.bossBar.setColor(BossBar.Color.RED);


                }
            }
        }
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (this.hasCustomName()) {
            this.bossBar.setName(this.getDisplayName());
        }
    }
    @Override
    public void setCustomName(@Nullable Text name) {
        super.setCustomName(name);
        this.bossBar.setName(this.getDisplayName());
    }

    @Override
    public void onStartedTrackingBy(ServerPlayerEntity player) {
        super.onStartedTrackingBy(player);
        this.bossBar.addPlayer(player);
    }

    @Override
    public void onStoppedTrackingBy(ServerPlayerEntity player) {
        super.onStoppedTrackingBy(player);
        this.bossBar.removePlayer(player);
    }

    public static class HellBenderNavigation
            extends MobNavigation {
        @Nullable
        private BlockPos targetPos;


        public HellBenderNavigation(MobEntity mobEntity, World world) {
            super(mobEntity, world);
        }
        @Override
        protected PathNodeNavigator createPathNodeNavigator(int range) {
            this.nodeMaker = new LandPathNodeMaker();
            this.nodeMaker.setCanEnterOpenDoors(false);
            return new PathNodeNavigator(this.nodeMaker, 50){

                @Override
                protected float getDistance(PathNode a, PathNode b) {
                    return a.getSquaredDistance(b);
                }
            };
        }

        @Override
        public Path findPathTo(BlockPos target, int distance) {
            this.targetPos = target;
            return super.findPathTo(target, distance);
        }

        @Override
        public Path findPathTo(Entity entity, int distance) {
            this.targetPos = entity.getBlockPos();
            return super.findPathTo(entity, distance);
        }

        @Override
        public boolean startMovingTo(Entity entity, double speed) {
            Path path = this.findPathTo(entity, 0);
            if (path != null) {
                return this.startMovingAlong(path, speed);
            }
            this.targetPos = entity.getBlockPos();
            this.speed = speed;
            return true;
        }

        @Override
        public void tick() {
                if(this.isIdle()) {
                    if (this.targetPos != null) {

                        if (this.targetPos.isWithinDistance(this.entity.getPos(), this.entity.getWidth()) || this.entity.getY() >= (double) this.targetPos.getY() && new BlockPos(this.targetPos.getX(), this.entity.getY(), this.targetPos.getZ()).isWithinDistance(this.entity.getPos(), this.entity.getWidth())) {
                            this.targetPos = null;
                            recalculatePath();
                        } else {
                            this.entity.getMoveControl().moveTo(this.targetPos.getX(), this.targetPos.getY(), this.targetPos.getZ(), this.speed);
                            isFollowingPath();
                        }

                    }
                return;
            }

            super.tick();
        }
    }
     @Override
    protected void mobTick(){

         this.bossBar.setPercent(this.getHealth() / this.getMaxHealth());
    }



    private PlayState predicate (AnimationEvent event) {
        if (!event.isMoving() && this.player == null) {
            event.getController().setAnimation(sleep_animation);
            return PlayState.CONTINUE;
        } else if(event.isMoving()){
            event.getController().setAnimation(chase_animation);}
        return PlayState.CONTINUE;
    }

    private PlayState standPredicate (AnimationEvent event) {
        if (!event.isMoving() && this.isAttacking()) {
            event.getController().setAnimation(null);
            return PlayState.CONTINUE;
        }
        return PlayState.CONTINUE;
    }


    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this,"controller", 0, this::predicate));
        animationData.addAnimationController(new AnimationController(this,"nullController", 0, this::standPredicate));
    }
    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
    @Override
    protected SoundEvent getAmbientSound(){return ModSounds.HELLBENDER_AMBIENT;}
    @Override
    protected SoundEvent getHurtSound(DamageSource source){return ModSounds.HELLBENDER_HURT;}
    @Override
    protected SoundEvent getDeathSound(){return ModSounds.HELLBENDER_DEATH;}
    @Override protected void playStepSound(BlockPos pos, BlockState state ) {
        this.playSound(SoundEvents.ENTITY_COW_STEP ,1f, .8f);
    }

}
