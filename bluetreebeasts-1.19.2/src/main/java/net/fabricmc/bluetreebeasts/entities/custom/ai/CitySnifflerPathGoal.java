package net.fabricmc.bluetreebeasts.entities.custom.ai;

import net.fabricmc.bluetreebeasts.block.Modblocks;
import net.fabricmc.bluetreebeasts.entities.custom.CitySnifflerEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;

import java.util.EnumSet;

public class CitySnifflerPathGoal extends Goal {
    private final CitySnifflerEntity sniffler;
    private long lastPlaceTime;

    public CitySnifflerPathGoal(CitySnifflerEntity sniffler) {
        this.sniffler = sniffler;
        this.setControls(EnumSet.of(Control.MOVE));
        this.lastPlaceTime = 0;
    }

    @Override
    public boolean canStart() {
        if (System.currentTimeMillis() - lastPlaceTime < 100000) {
            // Check only every 100 seconds to reduce computation
            return false;
        }
        BlockPos pos = sniffler.getBlockPos().down();
        BlockState state = sniffler.world.getBlockState(pos);
        return isReplaceableSurface(state) && !isNearbySnifflerColonyBlock(pos);
    }

    private boolean isReplaceableSurface(BlockState state) {
        return state.isOf(Blocks.DIRT) || state.isOf(Blocks.COARSE_DIRT) || state.isOf(Blocks.MUD) || state.isOf(Blocks.GRASS_BLOCK);
    }

    private boolean isNearbySnifflerColonyBlock(BlockPos pos) {
        return sniffler.world.getBlockState(pos).isOf(Modblocks.SNIFFLER_COLONY_BLOCK) ||
                sniffler.world.getBlockState(pos).isOf(Modblocks.SNIFFLER_COLONY_ENTER_BLOCK);
    }

    @Override
    public void start() {
        BlockPos pos = sniffler.getBlockPos().down();
        sniffler.world.setBlockState(pos, Modblocks.SNIFFLER_COLONY_BLOCK.getDefaultState());
        lastPlaceTime = System.currentTimeMillis();
    }
}
