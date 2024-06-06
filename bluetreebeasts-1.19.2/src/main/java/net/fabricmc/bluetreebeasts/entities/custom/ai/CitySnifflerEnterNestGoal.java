package net.fabricmc.bluetreebeasts.entities.custom.ai;

import net.fabricmc.bluetreebeasts.block.custom.SnifflerColonyEnterBlock;
import net.fabricmc.bluetreebeasts.block.custom.SnifflerColonyFeedBlock;
import net.fabricmc.bluetreebeasts.block.entity.SnifflerColonyEnterBlockEntity;
import net.fabricmc.bluetreebeasts.block.entity.SnifflerColonyFeedBlockEntity;
import net.fabricmc.bluetreebeasts.entities.custom.CitySnifflerEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.*;

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
        if (sniffler.isInsideWaterOrBubbleColumn() || sniffler.isInsideNest() || !sniffler.canEnterNest()) {
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
        if (this.targetPos != null && this.sniffler.getBlockPos().isWithinDistance(targetPos, 2.0)) {
            this.sniffler.getDataTracker().set(CitySnifflerEntity.INSIDE_NEST, true);
            if (this.sniffler.hasSeeds()) {
                depositSeedInFeedBlock(this.sniffler);
            }
            if (this.sniffler.hasProduce()) {
                consumeProduceAndEnablePoison();
            }
            this.stop();
        }
    }

    private void consumeProduceAndEnablePoison() {
        if (this.sniffler.hasProduce()) {
            this.sniffler.decrementProduceCount();
            this.sniffler.enablePoison = true;
        }
    }

    private void depositSeedInFeedBlock(CitySnifflerEntity sniffler) {
        BlockPos feedBlockPos = targetPos.down(); // Assuming the feed block is directly under the enter block
        BlockEntity entity = this.world.getBlockEntity(feedBlockPos);
        if (entity instanceof SnifflerColonyFeedBlockEntity) {
            SnifflerColonyFeedBlockEntity feedBlock = (SnifflerColonyFeedBlockEntity) entity;
            if (!feedBlock.isFull()) {
                feedBlock.addSeed();
                sniffler.decreaseSeedCount(); // Assume there is a method in sniffler to manage its seed count
            } else {
                System.out.println("Feed block is full or not found.");
            }
        }
    }

    @Override
    public boolean shouldContinue() {
        return false;
    }

    @Override
    public void stop() {
        if (this.sniffler.getBlockPos().isWithinDistance(targetPos, 1.0)) {
            this.sniffler.getDataTracker().set(CitySnifflerEntity.INSIDE_NEST, true);
        }
    }
}