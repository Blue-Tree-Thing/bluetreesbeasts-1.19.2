package net.fabricmc.bluetreebeasts.entities.custom.ai;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.EnumSet;

public class GannetWhaleTransitionGoal extends Goal {
    private final PathAwareEntity entity;
    private final World world;
    private final double speed;

    public GannetWhaleTransitionGoal(PathAwareEntity entity, double speed) {
        this.entity = entity;
        this.world = entity.world;
        this.speed = speed;
        this.setControls(EnumSet.of(Control.MOVE));
    }

    @Override
    public boolean canStart() {
        if (!entity.isInsideWaterOrBubbleColumn() && isNearWater()) {
            return true;
        } else return entity.isInsideWaterOrBubbleColumn() && needsToExitWater();
    }

    private boolean isNearWater() {
        BlockPos pos = entity.getBlockPos();
        for (int dx = -2; dx <= 2; dx++) {
            for (int dz = -2; dz <= 2; dz++) {
                if (world.getFluidState(pos.add(dx, -1, dz)).isIn(FluidTags.WATER)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean needsToExitWater() {
        BlockPos pos = entity.getBlockPos();
        return world.getBlockState(pos.up()).isAir() && !world.getFluidState(pos.up()).isIn(FluidTags.WATER);
    }

    @Override
    public void start() {
        BlockPos targetPos = findTransitionBlock();
        if (targetPos != null) {
            entity.getNavigation().startMovingTo(targetPos.getX(), targetPos.getY(), targetPos.getZ(), speed);
            if (needsToExitWater()) {
                // Add upward velocity when exiting water
                entity.addVelocity(0, 0.5, 0); // Adjust the y-value as needed for desired jump effect
                entity.velocityModified = true;
                entity.setNoGravity(false); // Ensure gravity is on when out of water
            }
        }
    }

    @Override
    public void tick() {
        if (isNearWater() && !entity.isInsideWaterOrBubbleColumn()) {
            breakIceInPath();
        } else {
            entity.setNoGravity(false); // Ensure gravity is on when not near water
        }
    }

    private BlockPos findTransitionBlock() {
        BlockPos pos = entity.getBlockPos();
        for (int dx = -5; dx <= 5; dx++) {
            for (int dz = -5; dz <= 5; dz++) {
                BlockPos checkPos = pos.add(dx, -1, dz);
                if (world.getFluidState(checkPos).isIn(FluidTags.WATER)) {
                    return checkPos;
                }
            }
        }
        return null;
    }

    private void breakIceInPath() {
        BlockPos pos = entity.getBlockPos();
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                BlockPos checkPos = pos.add(dx, 0, dz);
                BlockState state = world.getBlockState(checkPos);
                if (state.isOf(Blocks.ICE) || state.isOf(Blocks.FROSTED_ICE)) {
                    world.breakBlock(checkPos, true);
                }
            }
        }
    }
}