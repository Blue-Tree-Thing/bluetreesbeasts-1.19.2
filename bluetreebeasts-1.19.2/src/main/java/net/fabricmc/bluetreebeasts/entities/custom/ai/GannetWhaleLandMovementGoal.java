package net.fabricmc.bluetreebeasts.entities.custom.ai;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;


import java.util.EnumSet;

public class GannetWhaleLandMovementGoal extends Goal {
    private final PathAwareEntity whale;
    private final double speed;
    private final World world;
    private long lastCheckTime;
    private final long checkInterval;  // Longer interval to simulate resting
    private final double chanceToHeadToWater;  // Lowered chance to decide to head towards water
    private Vec3d targetPosition;
    private boolean isResting;  // New field to track resting state
    private final long restingTime;  // Time to rest after moving

    public GannetWhaleLandMovementGoal(PathAwareEntity whale, double speed, long checkInterval, double chanceToHeadToWater, long restingTime) {
        this.whale = whale;
        this.speed = speed;  // Reduced speed
        this.world = whale.getWorld();
        this.checkInterval = checkInterval;  // Increased interval for less frequent movement
        this.chanceToHeadToWater = chanceToHeadToWater;
        this.restingTime = restingTime;
        this.setControls(EnumSet.of(Control.MOVE));
        this.lastCheckTime = System.currentTimeMillis();
        this.isResting = false;
    }

    @Override
    public boolean canStart() {
        return !whale.isTouchingWater() && whale.isOnGround() && !isResting;
    }

    @Override
    public boolean shouldContinue() {
        return canStart() && !whale.getNavigation().isIdle();
    }

    @Override
    public void start() {
        randomWalk();
    }

    @Override
    public void tick() {
        if (isResting) {
            if (System.currentTimeMillis() - lastCheckTime > restingTime) {
                isResting = false;
                lastCheckTime = System.currentTimeMillis();
            }
            return;
        }

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastCheckTime > checkInterval) {
            if (whale.getRandom().nextFloat() < chanceToHeadToWater) {
                headToNearestWater();
            } else {
                randomWalk();
            }
            isResting = true;  // Begin resting after an action
            lastCheckTime = currentTime;
        }
    }

    private void randomWalk() {
        double angle = 2 * Math.PI * whale.getRandom().nextDouble();
        double distance = 5 + 5 * whale.getRandom().nextDouble();  // Decreased maximum distance
        double dx = Math.cos(angle) * distance;
        double dz = Math.sin(angle) * distance;

        BlockPos newPosition = new BlockPos(whale.getX() + dx, whale.getY(), whale.getZ() + dz);
        if (world.getBlockState(newPosition).isAir()) {
            targetPosition = Vec3d.ofCenter(newPosition);
            whale.getNavigation().startMovingTo(targetPosition.x, targetPosition.y, targetPosition.z, speed);
        }
    }

    private void headToNearestWater() {
        BlockPos nearestWater = findNearestWater(whale.getBlockPos());
        if (nearestWater != null) {
            targetPosition = Vec3d.ofCenter(nearestWater);
            whale.getNavigation().startMovingTo(targetPosition.x, targetPosition.y, targetPosition.z, speed);
            forceIntoWater();
        }
    }

    private BlockPos findNearestWater(BlockPos pos) {
        int range = 16;  // Range to check for water
        for (int dx = -range; dx <= range; dx++) {
            for (int dz = -range; dz <= range; dz++) {
                BlockPos checkPos = pos.add(dx, 0, dz);
                if (world.getFluidState(checkPos).isIn(FluidTags.WATER)) {
                    return checkPos;
                }
            }
        }
        return null;
    }

    private void forceIntoWater() {
        whale.setVelocity(0, -0.1, 0);  // Force whale downwards towards the water block
        whale.velocityDirty = true;
    }
}