package net.fabricmc.bluetreebeasts.entities.custom.ai;


import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.PathNode;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;

import java.util.EnumSet;

public class GannetWhaleWaterMovementGoal extends Goal {
    private final PathAwareEntity whale;
    private final double speed;
    private long lastChangeTime;
    private final long changeInterval = 9000; // Interval to change direction
    private Vec3d targetPosition;
    private final World world;
    private final double minClearanceAboveFloor = 3.0; // Minimum distance to stay above the ocean floor
    private final double minimumWaterDepth = 5.0; // Minimum depth to maintain clearance from the floor

    public GannetWhaleWaterMovementGoal(PathAwareEntity whale, double speed) {
        this.whale = whale;
        this.speed = speed;
        this.world = whale.getWorld();
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
        this.lastChangeTime = System.currentTimeMillis();
    }

    @Override
    public boolean canStart() {
        return whale.isTouchingWater();
    }

    @Override
    public boolean shouldContinue() {
        return whale.isTouchingWater() && !whale.getNavigation().isIdle();
    }

    @Override
    public void start() {
        changeSwimDirection();
    }

    @Override
    public void tick() {
        if (System.currentTimeMillis() - lastChangeTime > changeInterval || whale.getNavigation().isIdle()) {
            changeSwimDirection();
            lastChangeTime = System.currentTimeMillis();
        }

        if(whale.isSubmergedInWater()){
            whale.setVelocity(whale.getVelocity().add(0, 0.005, 0));
            whale.velocityDirty = true;
        }

        checkAndAvoidObstacles();
        if (currentWaterDepth() >= minimumWaterDepth) {
            maintainClearanceAboveFloor();
        }
    }

    private void changeSwimDirection() {
        double angle = whale.getRandom().nextDouble() * 360; // Full 360-degree range
        double pitch = calculatePitch(); // Calculate pitch based on the submersion status

        double horizontalDistance = 20 + 10 * whale.getRandom().nextDouble(); // Horizontal distance
        double verticalChange = Math.sin(Math.toRadians(pitch)) * horizontalDistance; // Vertical movement based on pitch
        double flatDistance = Math.cos(Math.toRadians(pitch)) * horizontalDistance; // Adjusted horizontal movement

        double dx = Math.cos(Math.toRadians(angle)) * flatDistance;
        double dz = Math.sin(Math.toRadians(angle)) * flatDistance;

        targetPosition = new Vec3d(whale.getX() + dx, findSuitableYPosition(whale.getY() + verticalChange), whale.getZ() + dz);

        whale.getNavigation().startMovingTo(targetPosition.x, targetPosition.y, targetPosition.z, this.speed);
    }

    private double calculatePitch() {
        return (whale.getRandom().nextDouble() - 0.5) * 60; // Standard pitch between -30 and +30 degrees
    }

    private double findSuitableYPosition(double proposedY) {
        int floorY = world.getTopY(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, (int) whale.getX(), (int) whale.getZ()) - 1;
        return Math.max(proposedY, floorY + minClearanceAboveFloor);
    }

    private double currentWaterDepth() {
        int surfaceY = world.getSeaLevel();
        int floorY = world.getTopY(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, (int) whale.getX(), (int) whale.getZ()) - 1;
        return surfaceY - floorY;
    }

    private void checkAndAvoidObstacles() {
        if (whale.getNavigation().getCurrentPath() != null && !whale.getNavigation().getCurrentPath().isFinished()) {
            PathNode nextNode = whale.getNavigation().getCurrentPath().getNode(whale.getNavigation().getCurrentPath().getCurrentNodeIndex());
            BlockPos nextBlockPos = new BlockPos(nextNode.x, nextNode.y, nextNode.z);

            if (world.getBlockState(nextBlockPos).isSolidBlock(world, nextBlockPos)) {
                // Reverse direction when encountering an obstacle
                targetPosition = new Vec3d(whale.getX() - (targetPosition.x - whale.getX()),
                        whale.getY() - (targetPosition.y - whale.getY()),
                        whale.getZ() - (targetPosition.z - whale.getZ()));
                whale.getNavigation().startMovingTo(targetPosition.x, targetPosition.y, targetPosition.z, this.speed);
            }
        }
    }

    private void maintainClearanceAboveFloor() {
        // Adjust the Y position to ensure clearance
        double currentY = whale.getY();
        int floorY = world.getTopY(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, (int) whale.getX(), (int) whale.getZ()) - 1;
        if (currentY - floorY < minClearanceAboveFloor) {
            whale.setVelocity(whale.getVelocity().add(0, 0.03, 0)); // Gradually increase the upward velocity
            whale.velocityDirty = true; // Ensures the new velocity is processed
        }
    }
}