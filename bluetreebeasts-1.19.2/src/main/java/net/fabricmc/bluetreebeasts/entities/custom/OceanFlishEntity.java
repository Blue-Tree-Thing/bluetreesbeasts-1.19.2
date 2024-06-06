package net.fabricmc.bluetreebeasts.entities.custom;


import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
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

import static software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes.LOOP;

public class OceanFlishEntity extends AnimalEntity implements IAnimatable {

    @Nullable
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    private Vec3d movementDirection = Vec3d.ZERO;
    private final double movementSpeed = 0.1;
    private final double maxTurnSpeedRadians = Math.toRadians(2);
    private double targetAltitude;
    private Vec3d previousPosition = Vec3d.ZERO;
    private int ticksSinceLastMove = 0;


    public OceanFlishEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
        this.noClip = false;
        this.setNoGravity(true);
        this.targetAltitude = this.getY();

    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return AnimalEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 7);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.world.isClient()) {
            if (this.getPos().squaredDistanceTo(previousPosition) < 0.01) {
                ticksSinceLastMove++;
            } else {
                ticksSinceLastMove = 0;
                previousPosition = this.getPos();
            }

            // Adjust direction more proactively if an obstacle is within a 5-block radius
            if (this.random.nextInt(100) == 0 || ticksSinceLastMove > 5 || isPathBlocked()) {
                chooseNewDirection();
                ticksSinceLastMove = 0;
            }

            adjustAltitude();
            moveInDirection();
            faceMovementDirection();
        }
    }

    private void chooseNewDirection() {
        // Forward direction based on current yaw and pitch
        float yawRadians = (float)Math.toRadians(this.getYaw());
        float pitchRadians = (float)Math.toRadians(this.getPitch()); // Existing pitch

        // Adjust yaw within a 180-degree forward arc
        float angleOffsetYaw = (this.random.nextFloat() * (float)Math.toRadians(120)) - (float)Math.toRadians(60);
        float newDirectionYaw = yawRadians + angleOffsetYaw;

        // Slightly adjust pitch to encourage smoother vertical transitions
        // Ensure the pitch adjustment is modest to avoid drastic altitude changes
        float pitchAdjustmentRange = (float)Math.toRadians(30); // Example range for pitch adjustment
        float angleOffsetPitch = (this.random.nextFloat() * pitchAdjustmentRange) - (pitchAdjustmentRange / 2);
        float newDirectionPitch = MathHelper.clamp(pitchRadians + angleOffsetPitch, (float)Math.toRadians(-45), (float)Math.toRadians(45)); // Clamp pitch

        // Convert updated yaw and pitch back to a direction vector
        this.movementDirection = Vec3d.fromPolar((float)Math.toDegrees(newDirectionPitch), (float)Math.toDegrees(newDirectionYaw));

        // Update target altitude based on the new pitch
        this.targetAltitude = this.getY() + (Math.sin(newDirectionPitch) * 10); // Example to link altitude change with pitch
    }

    private void adjustAltitude() {
        // This method might now ensure compliance with minimum and maximum altitude limits
        if (this.getY() < this.world.getSeaLevel() + 5) {
            this.targetAltitude = this.world.getSeaLevel() + 5; // Avoid going too low
        } else if (this.getY() > this.world.getSeaLevel() + 15) {
            this.targetAltitude = this.world.getSeaLevel() + 15; // Avoid going too high
        }
        // The actual altitude adjustment is now more influenced by chooseNewDirection's pitch adjustment
    }

    private void moveInDirection() {
        Vec3d currentVelocity = this.getVelocity();
        Vec3d desiredHeading = this.movementDirection.normalize();

        // Determine the desired velocity based on the desired heading and movement speed.
        Vec3d desiredVelocity = desiredHeading.multiply(movementSpeed);

        // Interpolate between the current velocity and the desired velocity for smoother transitions.
        double interpolationFactor = 0.1; // Control how quickly the entity adjusts its course. Adjust as needed.
        Vec3d newVelocity = currentVelocity.add(
                desiredVelocity.subtract(currentVelocity).multiply(interpolationFactor)
        );

        // Adjust vertical speed separately if needed
        double verticalSpeed = (this.targetAltitude - this.getY()) * 0.05; // Smoothing the altitude adjustment
        newVelocity = new Vec3d(newVelocity.x, verticalSpeed, newVelocity.z);

        this.setVelocity(newVelocity);
        this.move(MovementType.SELF, newVelocity);
    }

    private boolean isPathBlocked() {
        Vec3d startPosition = this.getPos();
        boolean isBlocked = false;

        for (int i = 1; i <= 5; i++) { // Check 5 blocks ahead for an obstacle
            Vec3d checkPosition = startPosition.add(this.movementDirection.normalize().multiply(i));
            BlockPos blockPos = new BlockPos(checkPosition);

            // Check a broader area to maintain distance from obstacles
            for (int xOffset = -1; xOffset <= 1; xOffset++) {
                for (int zOffset = -1; zOffset <= 1; zOffset++) {
                    BlockPos posToCheck = blockPos.add(xOffset, 0, zOffset);
                    if (!this.world.isAir(posToCheck)) {
                        isBlocked = true;
                        break;
                    }
                }
                if (isBlocked) break;
            }
            if (isBlocked) break;
        }
        return isBlocked;
    }

    private void faceMovementDirection() {
        Vec3d direction = this.getVelocity().normalize();
        float targetYaw = (float)Math.toDegrees(Math.atan2(-direction.x, direction.z));
        float targetPitch = (float)-Math.toDegrees(Math.atan2(direction.y, Math.sqrt(direction.x * direction.x + direction.z * direction.z)));

        this.setYaw(interpolateRotation(this.getYaw(), targetYaw, maxTurnSpeedRadians));
        this.setPitch(interpolateRotation(this.getPitch(), targetPitch, maxTurnSpeedRadians));
    }

    private float interpolateRotation(float current, float target, double maxTurnSpeedRadians) {
        double angleDifference = Math.toRadians(MathHelper.wrapDegrees(target - current));
        double turnSpeed = Math.signum(angleDifference) * Math.min(maxTurnSpeedRadians, Math.abs(angleDifference));
        return current + (float)Math.toDegrees(turnSpeed);
    }

    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }

    // Implement IAnimatable methods
    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.ocean_flish.flying", LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public @Nullable AnimationFactory getFactory() {
        return this.factory;
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }
}
