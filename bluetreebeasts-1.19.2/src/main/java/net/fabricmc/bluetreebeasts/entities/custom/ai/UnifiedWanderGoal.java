package net.fabricmc.bluetreebeasts.entities.custom.ai;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.PathAwareEntity;

import net.minecraft.util.math.Vec3d;

import java.util.EnumSet;

public class UnifiedWanderGoal extends Goal {
    private final PathAwareEntity entity;
    private final double landSpeed;
    private final double waterSpeed;
    private final int landExecutionChance;  // High value to make land movement rare
    private final int waterExecutionChance; // Low value for frequent water movement
    private long lastCheckTime;
    private final long cooldown;
    private Vec3d targetPosition;

    public UnifiedWanderGoal(PathAwareEntity entity, double landSpeed, double waterSpeed, int landExecutionChance, int waterExecutionChance, long cooldown) {
        this.entity = entity;
        this.landSpeed = landSpeed;
        this.waterSpeed = waterSpeed;
        this.landExecutionChance = landExecutionChance;
        this.waterExecutionChance = waterExecutionChance;
        this.cooldown = cooldown;
        this.lastCheckTime = 0;
        this.setControls(EnumSet.of(Control.MOVE));
    }

    @Override
    public boolean canStart() {
        if (System.currentTimeMillis() - lastCheckTime < cooldown) {
            return false;
        }
        lastCheckTime = System.currentTimeMillis();
        return entity.getNavigation().isIdle() && entity.getRandom().nextInt(getExecutionChance()) == 0;
    }

    @Override
    public void start() {
        updateTargetPosition(true);
    }

    @Override
    public void tick() {
        boolean inWater = entity.isInsideWaterOrBubbleColumn();
        entity.setNoGravity(inWater);
        if (inWater) {
            if (targetPosition == null || entity.squaredDistanceTo(targetPosition.x, targetPosition.y, targetPosition.z) > 100) { // Large distance check to allow longer uninterrupted travel
                updateTargetPosition(false);
            }
        } else {
            if (targetPosition == null || entity.getNavigation().isIdle()) {
                updateTargetPosition(true);
            }
        }
        if (targetPosition != null) {
            entity.getNavigation().startMovingTo(targetPosition.x, targetPosition.y, targetPosition.z, getCurrentSpeed());
        }
    }

    private void updateTargetPosition(boolean forceUpdate) {
        if (forceUpdate || entity.getRandom().nextInt(10) == 0) {  // More frequent updates in water
            this.targetPosition = findNewTargetPosition();
        }
        if (this.targetPosition != null) {
            entity.getNavigation().startMovingTo(targetPosition.x, targetPosition.y, targetPosition.z, getCurrentSpeed());
        }
    }

    private int getExecutionChance() {
        return entity.isInsideWaterOrBubbleColumn() ? waterExecutionChance : landExecutionChance;
    }

    private double getCurrentSpeed() {
        return entity.isInsideWaterOrBubbleColumn() ? waterSpeed : landSpeed;
    }

    private Vec3d findNewTargetPosition() {
        double angle, x, z, y = entity.getY();
        if (entity.isInsideWaterOrBubbleColumn()) {
            angle = Math.toRadians(entity.getYaw() + (Math.random() * 30 - 15));  // +/- 15 degree spread from current heading
            x = entity.getX() + Math.cos(angle) * 50;  // Significantly increased distance for water movement
            z = entity.getZ() + Math.sin(angle) * 50;
            y += (Math.random() * 8 - 4);  // Enhanced vertical movement
        } else {
            angle = Math.random() * 2 * Math.PI;
            x = entity.getX() + Math.cos(angle) * 10;  // Increased land target range
            z = entity.getZ() + Math.sin(angle) * 10;
        }
        return new Vec3d(x, y, z);
    }
}