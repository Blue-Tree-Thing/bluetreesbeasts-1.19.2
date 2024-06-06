package net.fabricmc.bluetreebeasts.entities.custom;

import net.fabricmc.bluetreebeasts.entities.custom.ai.CustomLandMobWanderGoal;
import net.fabricmc.bluetreebeasts.entities.custom.ai.GrapplerDefendWreathGoal;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import static software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes.LOOP;

public class GreaterGrapplerEntity extends HostileEntity implements IAnimatable {
    @Nullable
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    private static final AnimationBuilder run_animation = new AnimationBuilder().addAnimation("animation.greater_grappler.walk", LOOP);
    private static final AnimationBuilder idle_animation = new AnimationBuilder().addAnimation("animation.greater_grappler.idle", LOOP);
    private static final AnimationBuilder attack_animation = new AnimationBuilder().addAnimation("animation.greater_grappler.attack", LOOP);


    public GreaterGrapplerEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
        this.setPersistent();
        this.checkDespawn();
        this.navigation = new MobNavigation(this, world);
        this.moveControl = getMoveControl();
        this.experiencePoints = 50;
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return AnimalEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 40)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 1f)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 12f)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 5f)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, .6f)
                .add(EntityAttributes.GENERIC_ARMOR, 4f);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(2, new LookAtEntityGoal(this, LivingEntity.class, 20.0f));

        this.goalSelector.add(3, new MeleeAttackGoal(this, .5, true));

        this.goalSelector.add(5, new GrapplerDefendWreathGoal(this, 10));

        this.goalSelector.add(2, new SwimGoal(this));
        this.goalSelector.add(3, new LookAroundGoal(this));
        this.goalSelector.add(5, new CustomLandMobWanderGoal(this, .6, 80, 3));


        this.targetSelector.add(2, new ResettableActiveTargetGoal<>(this, PassiveEntity.class, true, false, 6000));
        if (!isPacified()) { // Only target players if not pacified
            this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
            this.targetSelector.add(1, new RevengeGoal(this));
        }
        if(isPacified){
            this.targetSelector.add(2, new ActiveTargetGoal<>(this, ZombieEntity.class, true));
            this.targetSelector.add(2, new ActiveTargetGoal<>(this, SpiderEntity.class, true));
            this.targetSelector.add(2, new ActiveTargetGoal<>(this, SkeletonEntity.class, true));

        }
    }

    public boolean isPacified = false;

    public void setPacified(boolean pacified) {
        if (this.isPacified != pacified) {
            this.isPacified = pacified;
            resetGoals(); // Method to reinitialize goals based on pacification state
        }
    }

    private void resetGoals() {
        this.goalSelector.clear();
        this.targetSelector.clear();
        initGoals(); // Reinitialize goals, which will now respect the new pacification state
    }

    public boolean isPacified() {
        return isPacified;
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("IsPacified", isPacified);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        isPacified = nbt.getBoolean("IsPacified");
        resetGoals(); // Make sure to apply pacification state to behavior after loading
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);

        if (itemStack.getItem() == Items.COOKED_BEEF) {
            if (!player.world.isClient) {
                itemStack.decrement(1); // Decrease the item count by one
                setPacified(true); // Set the entity as pacified
                this.world.sendEntityStatus(this, (byte)7); // Show happy particle effect
            }

            return ActionResult.SUCCESS; // Return that the interaction was successful
        }

        return super.interactMob(player, hand); // Proceed with normal interaction if not the specified item
    }





    static class ResettableActiveTargetGoal<T extends LivingEntity> extends ActiveTargetGoal<T> {
        private long lastTargetTime;
        private final long cooldown;

        public ResettableActiveTargetGoal(PathAwareEntity mob, Class<T> targetClass, boolean checkVisibility, boolean checkCanNavigate, long cooldownTicks) {
            super(mob, targetClass, checkVisibility, checkCanNavigate);
            this.cooldown = cooldownTicks;
        }

        @Override
        public boolean canStart() {
            // Only start if the cooldown has elapsed
            if (System.currentTimeMillis() - lastTargetTime < cooldown) {
                return false;
            }
            return super.canStart();
        }

        @Override
        public void start() {
            super.start();
            lastTargetTime = System.currentTimeMillis(); // Reset cooldown timer on start
        }
    }


    @Override
    public boolean disablesShield() {
        return true;
    }



    private void setAnimationWithTransition(AnimationController<GreaterGrapplerEntity> controller, AnimationBuilder animation, int transitionTicks) {
        controller.transitionLengthTicks = transitionTicks;
        controller.setAnimation(animation);
    }

    private PlayState predicate(AnimationEvent<GreaterGrapplerEntity> event) {
        AnimationController<GreaterGrapplerEntity> controller = event.getController();
        boolean isMoving = event.isMoving();
        boolean isAttacking = this.isAttacking();  // This should ideally check if the attack method was invoked

        // Default to idle
        String targetAnimation = "animation.greater_grappler.idle";
        int transitionTicks = 2;  // Standard transition time

        // Calculate if the target is within attack range
        double attackRange = 2.0; // Assuming attack range is 2 blocks
        boolean withinAttackRange = this.getTarget() != null && this.squaredDistanceTo(this.getTarget()) <= attackRange * attackRange;

        if (isAttacking && withinAttackRange) {
            targetAnimation = "animation.greater_grappler.attack";
        } else if (isMoving) {
            targetAnimation = "animation.greater_grappler.walk";
        }

        // Check current animation and update if necessary
        String currentAnimationName = controller.getCurrentAnimation() != null ? controller.getCurrentAnimation().animationName : "";
        if (!currentAnimationName.equals(targetAnimation)) {
            AnimationBuilder animation = switch (targetAnimation) {
                case "animation.greater_grappler.attack" -> attack_animation;
                case "animation.greater_grappler.walk" -> run_animation;
                default -> idle_animation;
            };
            setAnimationWithTransition(controller, animation, transitionTicks);
        }

        return PlayState.CONTINUE;
    }



    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this,"controller", 2, this::predicate));
    }

    @Override
    public @Nullable AnimationFactory getFactory() {
        return factory;
    }
}