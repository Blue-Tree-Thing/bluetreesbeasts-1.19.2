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
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
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

import java.util.Collections;
import java.util.EnumSet;

import static software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes.HOLD_ON_LAST_FRAME;
import static software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes.LOOP;

public class HorraneEntity extends AnimalEntity implements IAnimatable {
    @Nullable
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    private static final AnimationBuilder run_animation = new AnimationBuilder().addAnimation("animation.horrane.walk", LOOP);
    private static final AnimationBuilder idle_animation = new AnimationBuilder().addAnimation("animation.horrane.idle", LOOP);
    private static final AnimationBuilder attack_animation = new AnimationBuilder().addAnimation("animation.horrane.pounce", HOLD_ON_LAST_FRAME);
    private boolean isPouncing = false;


    public HorraneEntity(EntityType<? extends AnimalEntity> entityType, World world) {
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
        this.goalSelector.add(2, new SwimGoal(this));
        this.goalSelector.add(2, new PounceGoal(this, 5));
        this.goalSelector.add(3, new LookAroundGoal(this));
        this.goalSelector.add(5, new CustomLandMobWanderGoal(this, 1, 80, 3));

        // Setup target goals with exclusion of HorraneEntity
        this.targetSelector.add(2, new ResettableActiveTargetGoal<>(this, PassiveEntity.class, true, false, 6000));

        if (!isPacified()) {
            this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
            this.targetSelector.add(1, new RevengeGoal(this));
        }

        if (isPacified) {
            this.targetSelector.add(2, new ActiveTargetGoal<>(this, ZombieEntity.class, true));
            this.targetSelector.add(2, new ActiveTargetGoal<>(this, SpiderEntity.class, true));
            this.targetSelector.add(2, new ActiveTargetGoal<>(this, SkeletonEntity.class, true));
        }

        // Exclude other HorraneEntity instances from being targeted
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, AnimalEntity.class, 10, true, false, entity -> !(entity instanceof HorraneEntity)));
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

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }

    public void setPouncing(boolean isPouncing) {
        this.isPouncing = isPouncing;
    }

    public boolean isPouncing() {
        return this.isPouncing;
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

        if (itemStack.getItem() == Items.CHICKEN) {
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

    public static class PounceGoal extends Goal {
        private final HorraneEntity entity;  // Change to HorraneEntity to use specific methods like setPouncing
        private final double pounceDistance;
        private LivingEntity target;
        private long lastPounceTime;
        private static final long POUNCE_COOLDOWN = 1000; // Cooldown time in milliseconds

        public PounceGoal(HorraneEntity entity, double pounceDistance) {
            this.entity = entity;
            this.pounceDistance = pounceDistance;
            this.setControls(EnumSet.of(Control.MOVE));
            this.lastPounceTime = 0;
        }

        @Override
        public boolean canStart() {
            long timeSinceLastPounce = System.currentTimeMillis() - lastPounceTime;
            if (this.entity.isPouncing() || timeSinceLastPounce < POUNCE_COOLDOWN) {
                return false;
            }

            this.target = this.entity.getTarget();
            if (this.target == null) {
                return false;
            }

            double distance = this.entity.squaredDistanceTo(this.target.getX(), this.target.getY(), this.target.getZ());
            return distance < pounceDistance * pounceDistance;
        }

        @Override
        public void start() {
            Vec3d direction = new Vec3d(target.getX() - entity.getX(), 0, target.getZ() - entity.getZ()).normalize();
            entity.setVelocity(direction.x, 0.4, direction.z); // Adjust the y velocity to control the jump height
            entity.velocityDirty = true;
            entity.setPouncing(true);  // Set the pouncing state
            this.lastPounceTime = System.currentTimeMillis();
        }

        @Override
        public boolean shouldContinue() {
            boolean onGround = entity.isOnGround();
            if (onGround && entity.isPouncing()) {
                entity.setPouncing(false); // Reset pouncing state once it lands
            }
            return !onGround; // Continue this goal until the entity lands
        }

        @Override
        public void stop() {
            entity.setPouncing(false); // Ensure the pouncing flag is reset when the goal stops
        }
    }










    private PlayState predicate(AnimationEvent<HorraneEntity> event) {
        AnimationController<HorraneEntity> controller = event.getController();
        // Select animation based on movement and environment
        boolean isMoving = event.isMoving();
        if (isMoving) {
            controller.setAnimation(run_animation);
        } else {
            controller.setAnimation(idle_animation);
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