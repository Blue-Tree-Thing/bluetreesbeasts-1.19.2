package net.fabricmc.bluetreebeasts.entities.custom.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class GreaterGrapplerNavigation extends MobNavigation {
    @Nullable
    private BlockPos targetPos;
    private Vec3d movementDirection = Vec3d.ZERO;
    private final double wanderSpeed = 0.5;
    private final double chargeSpeed = 1;
    private long lastWanderUpdateTime = 0;
    private static final long WANDER_UPDATE_COOLDOWN = 10000; // Adjust if needed for wandering updates
    private boolean obstacleDetected = false;

    public GreaterGrapplerNavigation(MobEntity mobEntity, World world) {
        super(mobEntity, world);
    }

    @Override
    public void tick() {
        super.tick();
        if (!world.isClient) {
            LivingEntity target = entity.getTarget();
            if (target != null) {
                targetPos = target.getBlockPos();
                updateChargingTowardsTarget(target);
            } else {
                if (shouldUpdateWandering()) {
                    updateWandering();
                }
            }
            if (isPathBlocked()) {
                obstacleDetected = true;
                handleObstacleAvoidance();
            }
            smoothRotationAndMovement();
        }
    }

    private boolean shouldUpdateWandering() {
        long currentTime = System.currentTimeMillis();
        return currentTime - lastWanderUpdateTime > WANDER_UPDATE_COOLDOWN && targetPos == null;
    }

    private void updateWandering() {
        if (!obstacleDetected) {
            lastWanderUpdateTime = System.currentTimeMillis();
            chooseNewDirectionWithinArc();
            Vec3d newPosition = entity.getPos().add(movementDirection.multiply(5)); // Adjust the multiplier as necessary
            this.startMovingTo(newPosition.x, newPosition.y, newPosition.z, wanderSpeed);
        }
    }

    private void updateChargingTowardsTarget(Entity target) {
        Vec3d targetDirection = new Vec3d(target.getX() - this.entity.getX(), target.getY() - this.entity.getY(), target.getZ() - this.entity.getZ()).normalize();
        this.movementDirection = targetDirection;
        double distanceToTarget = this.entity.squaredDistanceTo(target.getX(), target.getY(), target.getZ());
        double adjustedSpeed = calculateSpeedModifier(Math.sqrt(distanceToTarget));
        this.startMovingTo(target.getX(), target.getY(), target.getZ(), adjustedSpeed);
    }

    private void chooseNewDirectionWithinArc() {
        float currentYaw = entity.getYaw();
        float angleOffset = entity.getRandom().nextFloat() * 90 - 45; // Choose a random angle within +/- 45 degrees of current yaw
        double radianOffset = Math.toRadians(angleOffset);
        double newDirectionYaw = Math.toRadians(currentYaw) + radianOffset;

        movementDirection = new Vec3d(Math.cos(newDirectionYaw), 0, Math.sin(newDirectionYaw));
    }

    private void handleObstacleAvoidance() {
        // Simplify direction change to avoid sharp turns
        double angle = Math.toRadians(this.entity.getRandom().nextInt(180) - 90); // Limit angle change
        movementDirection = new Vec3d(Math.cos(angle), 0, Math.sin(angle));
        Vec3d newPosition = entity.getPos().add(movementDirection.multiply(5)); // Move 5 blocks in the new direction
        this.startMovingTo(newPosition.x, newPosition.y, newPosition.z, wanderSpeed);
        obstacleDetected = false;
    }



    private void smoothRotationAndMovement() {
        double targetYaw;
        if (targetPos != null) {
            double deltaX = targetPos.getX() - entity.getX();
            double deltaZ = targetPos.getZ() - entity.getZ();
            targetYaw = Math.toDegrees(Math.atan2(deltaZ, deltaX)) - 90;
        } else {
            // Use current movement direction to determine yaw when wandering
            targetYaw = Math.toDegrees(Math.atan2(movementDirection.z, movementDirection.x)) - 90;
        }
        // Smoothly update yaw towards the target direction
        entity.setYaw(lerpRotation(entity.getYaw(), (float) targetYaw, 0.1F)); // Lower delta value for smoother rotation

        // Additional logic to adjust velocity or stop movement near targets can remain unchanged
    }

    private double calculateSpeedModifier(double distance) {
        // Reduce speed as the entity gets closer to the target
        if (distance < 10) {
            return wanderSpeed * (distance / 10);
        } else {
            return entity.getTarget() == null ? wanderSpeed : this.chargeSpeed;
        }
    }

    private float lerpRotation(float current, float target, float delta) {
        float angleDifference = MathHelper.wrapDegrees(target - current);
        return current + angleDifference * delta;
    }

    // Detects if there is an obstacle in the immediate path
    private boolean isPathBlocked() {
        // Check if the path ahead is blocked
        Vec3d lookVec = entity.getRotationVec(1.0F);
        Vec3d aheadPos = entity.getPos().add(lookVec.x * 2, lookVec.y * 2, lookVec.z * 2); // Look ahead 2 blocks
        return !entity.world.isAir(new BlockPos(aheadPos));
    }
}