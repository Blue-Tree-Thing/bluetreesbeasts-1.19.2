package net.fabricmc.bluetreebeasts.entities.custom;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import net.fabricmc.bluetreebeasts.effect.ModEffects;
import software.bernie.geckolib3.util.GeckoLibUtil;


public class HomingFlishEntity extends PathAwareEntity implements IAnimatable {
    private LivingEntity target;
    @Nullable
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    private State currentState = State.SPAWNING;
    private int timer = 20;
    private Vec3d chargeDirection;
    private int lifeSpan = 200;

    private enum State {
        SPAWNING,
        LOCKING_ON,
        CHARGING,
    }

    public HomingFlishEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
        this.noClip = true; // Prevents collision with blocks
        this.setNoGravity(true); // Prevents the entity from being affected by gravity
    }

    public void setTarget(LivingEntity target) {
        this.target = target;
    }

    public static DefaultAttributeContainer.Builder createHomingFlishAttributes() {
        return PathAwareEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 1);
    }

    @Override
    protected void initGoals() {
        // No goals are necessary for this entity.
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.world.isClient) {
            if (this.lifeSpan-- <= 0) {
                this.remove(RemovalReason.DISCARDED);
                return;
            }

            if (this.currentState == State.SPAWNING) {
                if (--this.timer <= 0) {
                    this.currentState = State.LOCKING_ON;
                    this.timer = 20; // Time before locking on
                }
            } else if (this.currentState == State.LOCKING_ON) {
                if (this.target != null) {
                    Vec3d direction = this.target.getPos().subtract(this.getPos()).normalize();
                    this.updateRotationTowards(direction);
                    if (--this.timer <= 0) {
                        this.currentState = State.CHARGING;
                        // Finalize direction when charging starts
                        this.setChargeDirection(direction);
                    }
                }
            } else if (this.currentState == State.CHARGING) {
                double chargeSpeed = 1.0;
                this.setVelocity(this.chargeDirection.multiply(chargeSpeed));
                this.move(MovementType.SELF, this.getVelocity());
                this.checkCollision();
            }

            emitParticles();
        }
    }

    private void updateRotationTowards(Vec3d direction) {
        float targetYaw = (float) MathHelper.atan2(direction.z, direction.x) * (180F / (float) Math.PI) - 90F;
        float targetPitch = (float) -(MathHelper.atan2(direction.y, direction.horizontalLength()) * (180F / (float) Math.PI));

        // Smoothly interpolate yaw and pitch
        this.setYaw(interpolateRotation(this.getYaw(), targetYaw)); // Adjust 2.0F for speed of yaw change
        this.setPitch(interpolateRotation(this.getPitch(), targetPitch)); // Adjust 2.0F for speed of pitch change
    }


    private float interpolateRotation(float currentAngle, float targetAngle) {
        float angleDifference = MathHelper.wrapDegrees(targetAngle - currentAngle);
        return currentAngle + MathHelper.clamp(angleDifference, -(float) 2.0, (float) 2.0);
    }

    private void setChargeDirection(Vec3d direction) {
        // Calculate yaw and pitch to face the target
        float yaw = (float) MathHelper.atan2(direction.z, direction.x) * (180F / (float) Math.PI) - 90F;
        float pitch = (float) -(MathHelper.atan2(direction.y, direction.horizontalLength()) * (180F / (float) Math.PI));
        this.setYaw(yaw);
        this.setPitch(pitch);
        this.chargeDirection = direction;
    }

    private void checkCollision() {
        if (this.world instanceof ServerWorld && this.target != null && this.squaredDistanceTo(this.target) < 1.0) { // Reduced effective radius check
            // Apply any effects to the target here
            if (this.target != null) {
                LivingEntity livingTarget = this.target;
                livingTarget.addStatusEffect(new StatusEffectInstance(ModEffects.SPECTRALPOISON, 100, 0));
            }
            // Simulate an explosion visually without causing damage
            simulateExplosionVisuals();
            this.remove(RemovalReason.KILLED);
        }
    }

    private void simulateExplosionVisuals() {
        if (this.world instanceof ServerWorld serverWorld) {
            // Play explosion sound
            serverWorld.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_GENERIC_EXPLODE, this.getSoundCategory(), 1.0F, 1.0F);
            // Create explosion particles
            serverWorld.spawnParticles(ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
        }
    }

    protected void emitParticles() {
        if (this.world instanceof ServerWorld serverWorld) {
            Vec3d particlePos = this.getPos().add(this.getVelocity().multiply(-0.5));
            serverWorld.spawnParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, particlePos.x, particlePos.y, particlePos.z, 1, 0, 0, 0, 0.03);
        }
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        // Implement animation controllers if necessary
    }

    @Override
    public @Nullable AnimationFactory getFactory() {
        return this.factory;
    }
}