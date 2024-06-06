package net.fabricmc.bluetreebeasts.entities.custom.ai;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;


import java.util.EnumSet;

public class GannetWhaleLandMovementGoal extends Goal {
    private final PathAwareEntity whale;
    private final double speed;
    private final World world;
    private long lastChangeTime;
    private final long changeInterval;  // Time interval to reconsider the current path
    private final double chanceToChangeDirection;  // Chance to change direction on each interval
    private Vec3d targetPosition;
    private final int maxDurationOnLand;  // Maximum time whale should stay on land
    private long startTime;  // Time when the goal started

    public GannetWhaleLandMovementGoal(PathAwareEntity whale, double speed, long changeInterval, double chanceToChangeDirection, int maxDurationOnLand) {
        this.whale = whale;
        this.speed = speed;
        this.world = whale.getWorld();
        this.changeInterval = changeInterval;
        this.chanceToChangeDirection = chanceToChangeDirection;
        this.maxDurationOnLand = maxDurationOnLand;
        this.setControls(EnumSet.of(Control.MOVE));
        this.lastChangeTime = System.currentTimeMillis();
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public boolean canStart() {
        return !whale.isTouchingWater() && whale.isOnGround();
    }

    @Override
    public boolean shouldContinue() {
        long duration = System.currentTimeMillis() - startTime;
        return canStart() && duration < maxDurationOnLand;
    }

    @Override
    public void start() {
        findWater(true);
        this.startTime = System.currentTimeMillis();  // Reset the timer when the goal starts
    }

    @Override
    public void tick() {
        if (System.currentTimeMillis() - lastChangeTime > changeInterval || whale.getNavigation().isIdle()) {
            findWater(false);
            lastChangeTime = System.currentTimeMillis();
        }
    }

    // Enhanced water finding logic
    private void findWater(boolean forceSearch) {
        BlockPos pos = new BlockPos(whale.getX(), whale.getY(), whale.getZ());
        BlockPos nearestWater = findNearestWater(pos);
        if (nearestWater != null || forceSearch) {
            targetPosition = Vec3d.ofBottomCenter(nearestWater != null ? nearestWater : pos);
            whale.getNavigation().startMovingTo(targetPosition.x, targetPosition.y, targetPosition.z, speed);
        } else if (whale.getRandom().nextDouble() < chanceToChangeDirection) {
            changeWalkDirection();
        }
    }

    private void changeWalkDirection() {
        double angle = 2 * Math.PI * whale.getRandom().nextDouble();
        double distance = 5 + 5 * whale.getRandom().nextDouble();
        double dx = Math.cos(angle) * distance;
        double dz = Math.sin(angle) * distance;

        BlockPos pos = new BlockPos(whale.getX() + dx, whale.getY(), whale.getZ() + dz);
        BlockPos groundPos = findSuitableLandPosition(pos);
        if (groundPos != null) {
            targetPosition = Vec3d.ofCenter(groundPos.up());
            whale.getNavigation().startMovingTo(targetPosition.x, targetPosition.y, targetPosition.z, speed);
        }
    }

    private BlockPos findNearestWater(BlockPos pos) {
        for (Direction dir : Direction.values()) {
            BlockPos checkPos = pos.offset(dir);
            if (world.getFluidState(checkPos).isIn(FluidTags.WATER)) {
                return checkPos;
            }
        }
        return null;
    }

    private BlockPos findSuitableLandPosition(BlockPos pos) {
        for (int i = 0; i <= 3; i++) {
            BlockPos checkPos = pos.down(i);
            if (world.getBlockState(checkPos).isSolidBlock(world, checkPos) && world.getBlockState(checkPos.up()).isAir()) {
                return checkPos;
            }
        }
        return null;
    }
}
