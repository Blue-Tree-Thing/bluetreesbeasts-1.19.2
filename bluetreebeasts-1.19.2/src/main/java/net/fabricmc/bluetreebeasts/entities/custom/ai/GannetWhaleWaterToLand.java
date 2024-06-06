package net.fabricmc.bluetreebeasts.entities.custom.ai;

import net.fabricmc.bluetreebeasts.entities.custom.GannetWhaleEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.EnumSet;

public class GannetWhaleWaterToLand extends Goal {
    private final GannetWhaleEntity whale;
    private final double speed;
    private final World world;
    private Vec3d targetLandPos;
    private final int checkInterval = 20; // Interval in ticks to check for nearby land
    private int ticksSinceLastCheck = 0;

    public GannetWhaleWaterToLand(GannetWhaleEntity whale, double speed) {
        this.whale = whale;
        this.speed = speed;
        this.world = whale.getWorld();
        this.setControls(EnumSet.of(Control.MOVE));
    }

    @Override
    public boolean canStart() {
        return whale.isTouchingWater() && (whale.hasEgg || wantsToReturnToLand());
    }

    @Override
    public boolean shouldContinue() {
        return whale.isTouchingWater() && targetLandPos != null && !whale.getNavigation().isIdle();
    }

    @Override
    public void start() {
        updateTargetLandPosition();
    }

    @Override
    public void tick() {
        ticksSinceLastCheck++;
        if (ticksSinceLastCheck >= checkInterval || whale.getNavigation().isIdle()) {
            updateTargetLandPosition();
            ticksSinceLastCheck = 0;
        }

        if (!whale.isTouchingWater() && whale.isOnGround()) {
            whale.getNavigation().stop(); // Stop navigation once fully on land
        } else if (!whale.isOnGround()) {
            // Add upward velocity to help whale hop onto land when approaching the shore
            whale.setVelocity(whale.getVelocity().add(0, 0.1, 0)); // Adjust vertical speed to help get over ledges
            whale.velocityDirty = true; // Ensures the new velocity is processed
        }
    }

    private void updateTargetLandPosition() {
        Vec3d newLandPos = findNearestLand();
        if (newLandPos != null && (targetLandPos == null || !targetLandPos.equals(newLandPos))) {
            whale.setVelocity(whale.getVelocity().add(0, 0.1, 0)); // Adjust vertical speed to help get over ledges

            targetLandPos = newLandPos;
            whale.getNavigation().startMovingTo(targetLandPos.x, targetLandPos.y, targetLandPos.z, this.speed);
        }
    }

    private Vec3d findNearestLand() {
        BlockPos pos = whale.getBlockPos();
        int range = 15; // Checking within a reasonable range around the whale
        for (int dx = -range; dx <= range; dx++) {
            for (int dz = -range; dz <= range; dz++) {
                BlockPos checkPos = pos.add(dx, 0, dz);
                if (world.getFluidState(checkPos).isEmpty() && world.getBlockState(checkPos.down()).isSolidBlock(world, checkPos.down())) {
                    return Vec3d.ofCenter(checkPos.up());
                }
            }
        }
        return null;
    }

    private boolean wantsToReturnToLand() {
        return whale.hasEgg || whale.getRandom().nextFloat() < 0.05; // 5% chance each tick to want to return to land
    }

    @Override
    public void stop() {
        whale.getNavigation().stop();
    }
}