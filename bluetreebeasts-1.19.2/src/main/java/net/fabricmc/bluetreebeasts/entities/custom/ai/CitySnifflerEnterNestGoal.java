package net.fabricmc.bluetreebeasts.entities.custom.ai;

import net.fabricmc.bluetreebeasts.block.custom.SnifflerColonyEnterBlock;
import net.fabricmc.bluetreebeasts.entities.custom.CitySnifflerEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.EnumSet;

public class CitySnifflerEnterNestGoal extends Goal {
    private final CitySnifflerEntity sniffler;
    private final World world;
    private BlockPos targetPos;

    public CitySnifflerEnterNestGoal(CitySnifflerEntity sniffler) {
        this.sniffler = sniffler;
        this.world = sniffler.getWorld();
        this.setControls(EnumSet.of(Goal.Control.MOVE));
    }

    @Override
    public boolean canStart() {
        if (sniffler.isCarryingBlock() || sniffler.isInsideWaterOrBubbleColumn() || sniffler.isInsideNest() || !sniffler.canEnterNest()) {
            return false;
        }
        this.targetPos = findNearestEnterBlockPos();
        return targetPos != null;
    }

    @Override
    public void start() {
        if (this.targetPos != null) {
            this.sniffler.getNavigation().startMovingTo(
                    targetPos.getX() + 0.5,
                    targetPos.getY(),
                    targetPos.getZ() + 0.5,
                    1); // Adjusted speed for consistency and direct approach
        }
    }

    private BlockPos findNearestEnterBlockPos() {
        return BlockPos.findClosest(
                this.sniffler.getBlockPos(), 16, 6,
                pos -> this.world.getBlockState(pos).getBlock() instanceof SnifflerColonyEnterBlock).orElse(null);
    }

    @Override
    public void tick() {
        if (this.targetPos != null && this.sniffler.getBlockPos().isWithinDistance(targetPos, 2.0)) { // Use proximity check
            this.sniffler.getDataTracker().set(CitySnifflerEntity.INSIDE_NEST, true);
            this.stop(); // Stop the goal if the sniffler is close enough
        }
    }

    @Override
    public boolean shouldContinue() {
        return !this.sniffler.getNavigation().isIdle() && !this.sniffler.getBlockPos().isWithinDistance(targetPos, 2.0);
    }

    @Override
    public void stop() {
        if (this.sniffler.getBlockPos().isWithinDistance(targetPos, 2.0)) {
            this.sniffler.getDataTracker().set(CitySnifflerEntity.INSIDE_NEST, true);
        }
    }
}