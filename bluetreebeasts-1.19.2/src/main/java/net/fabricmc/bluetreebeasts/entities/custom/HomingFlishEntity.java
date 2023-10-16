package net.fabricmc.bluetreebeasts.entities.custom;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import net.fabricmc.bluetreebeasts.effect.ModEffects; // Make sure to import your ModEffects class

import java.util.List;
import java.util.UUID;

public class HomingFlishEntity extends PathAwareEntity implements IAnimatable {
    private LivingEntity target;
    private final AnimationFactory factory = new AnimationFactory(this);
    private State currentState = State.SPAWNING;
    private int timer = 20; // Initial pause for 1 second (20 ticks)
    private final double chargeSpeed = 1.0; // Speed at which the entity charges
    private Vec3d chargeDirection; // Direction in which to charge
    private int lifeSpan = 200; // Entity lifespan in ticks
    private UUID ownerUuid; // UUID of the owner

    public HomingFlishEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
        this.noClip = false;
    }

    public void setOwnerUuid(UUID ownerUuid) {
        this.ownerUuid = ownerUuid;
    }

    public static DefaultAttributeContainer.Builder createHomingFlishAttributes() {
        return PathAwareEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 1.5);
    }

    @Override
    protected void initGoals() {
        // No goals are necessary for this entity.
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.world.isClient) {
            if (--lifeSpan <= 0) {
                this.remove(RemovalReason.DISCARDED);
                return;
            }

            switch (currentState) {
                case SPAWNING:
                    if (--timer <= 0) {
                        currentState = State.LOCKING_ON;
                        timer = 10; // Half-second pause
                    }
                    break;
                case LOCKING_ON:
                    if (target != null && --timer <= 0) {
                        Vec3d direction = target.getPos().subtract(this.getPos()).normalize();
                        float yaw = (float) (MathHelper.atan2(direction.z, direction.x) * (180 / Math.PI) - 90);
                        this.setYaw(yaw);
                        this.bodyYaw = yaw;
                        this.chargeDirection = direction;
                        currentState = State.CHARGING;
                    }
                    break;
                case CHARGING:
                    this.setVelocity(chargeDirection.multiply(chargeSpeed));
                    this.move(MovementType.SELF, this.getVelocity());
                    if (this.squaredDistanceTo(target.getPos()) < 2.0 || !this.isAlive()) {
                        this.remove(RemovalReason.KILLED);
                    }
                    applyEffects();
                    break;
                case DESTROY:
                    this.remove(RemovalReason.KILLED);
                    break;
            }
        }
    }

    private void applyEffects() {
        List<Entity> entities = this.world.getOtherEntities(this, this.getBoundingBox().expand(0.3));
        for (Entity entity : entities) {
            if (entity instanceof LivingEntity livingEntity && !entity.getUuid().equals(this.ownerUuid)) {
                // Here you apply your custom effect, for example:
                livingEntity.addStatusEffect(new StatusEffectInstance(ModEffects.SPECTRALPOISON, 200, 0)); // Duration and amplifier are examples
            }
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
    public AnimationFactory getFactory() {
        return this.factory;
    }

    private enum State {
        SPAWNING, LOCKING_ON, CHARGING, DESTROY
    }
}
