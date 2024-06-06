package net.fabricmc.bluetreebeasts.entities.custom.ai;

import net.fabricmc.bluetreebeasts.block.Modblocks;
import net.fabricmc.bluetreebeasts.entities.custom.CitySnifflerEntity;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.math.BlockPos;

import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.*;

public class CitySnifflerConstructNestGoal extends Goal {
    private final PathAwareEntity entity;
    private final World world;
    private BlockPos targetBlockPos = null;
    private boolean hasPlacedBlock = false;
    private int standOnBlockTimer = 0;

    public CitySnifflerConstructNestGoal(PathAwareEntity entity) {
        this.entity = entity;
        this.world = entity.getWorld();
        this.setControls(EnumSet.of(Control.MOVE));
    }

    @Override
    public boolean canStart() {
        if (hasPlacedBlock) {
            return false;
        }
        List<PathAwareEntity> nearbyEntities = world.getEntitiesByClass(
                PathAwareEntity.class,
                this.entity.getBoundingBox().expand(80),
                e -> e instanceof CitySnifflerEntity && !e.equals(this.entity));

        if (nearbyEntities.isEmpty()) {
            targetBlockPos = findSuitableLocation();
            return targetBlockPos != null;
        }
        return false;
    }

    @Override
    public void tick() {
        if (targetBlockPos != null && !hasPlacedBlock) {
            if (entity.getBlockPos().isWithinDistance(targetBlockPos, 2)) {
                placeBlockAndPrepareAccess(targetBlockPos);
                hasPlacedBlock = true;
                standOnBlockTimer = -20;
            } else {
                this.entity.getNavigation().startMovingTo(targetBlockPos.getX() + 0.5, targetBlockPos.getY(), targetBlockPos.getZ() + 0.5, 1.0);
            }
        }
        manageStandOnBlockBehavior();
    }

    private void placeBlockAndPrepareAccess(BlockPos blockPos) {
        world.setBlockState(blockPos, Modblocks.SNIFFLER_COLONY_ORIGIN_BLOCK.getDefaultState());
        ensureAccessibility(blockPos.up());
    }

    private void ensureAccessibility(BlockPos blockPos) {
        if (!world.getBlockState(blockPos).isAir()) {
            world.breakBlock(blockPos, true);
        }
        // Adjust the blocks around to create a mini ramp if needed
        for (Direction dir : Direction.values()) {
            if (dir != Direction.DOWN) {
                BlockPos sidePos = blockPos.offset(dir);
                if (world.getBlockState(sidePos).isAir()) {
                    world.setBlockState(sidePos, Blocks.DIRT.getDefaultState());
                }
            }
        }
    }

    private void manageStandOnBlockBehavior() {
        if (hasPlacedBlock) {
            if (standOnBlockTimer >= 0 && standOnBlockTimer < 400) {
                standOnBlockTimer++;
            } else if (standOnBlockTimer < 0) {
                standOnBlockTimer++;
                if (standOnBlockTimer == 0) {
                    BlockPos topOfBlockPos = targetBlockPos.up();
                    this.entity.getNavigation().startMovingTo(topOfBlockPos.getX() + 0.5, topOfBlockPos.getY(), topOfBlockPos.getZ() + 0.5, 1.0);
                }
            } else {
                resetGoalState();
            }
        }
    }

    private void resetGoalState() {
        standOnBlockTimer = 0;
        hasPlacedBlock = false;
        targetBlockPos = null;
    }

    private BlockPos findSuitableLocation() {
        BlockPos entityPos = this.entity.getBlockPos();
        for (int x = -5; x <= 5; x++) {
            for (int z = -5; z <= 5; z++) {
                BlockPos searchPos = entityPos.add(x, 0, z);
                if (world.getBlockState(searchPos).isOf(Blocks.GRASS_BLOCK)) {
                    return searchPos.up();
                }
            }
        }
        return null;
    }
}