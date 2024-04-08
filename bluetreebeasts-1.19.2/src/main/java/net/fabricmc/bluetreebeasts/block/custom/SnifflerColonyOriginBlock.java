package net.fabricmc.bluetreebeasts.block.custom;

import net.fabricmc.bluetreebeasts.block.Modblocks;
import net.fabricmc.bluetreebeasts.entities.custom.CitySnifflerEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SnifflerColonyOriginBlock extends Block {
    public SnifflerColonyOriginBlock() {
        super(FabricBlockSettings.of(Material.SOIL).strength(2.0f));
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        super.onSteppedOn(world, pos, state, entity);

        if (!world.isClient && entity instanceof CitySnifflerEntity && shouldConstructNest(world, pos)) {
            ServerWorld serverWorld = (ServerWorld) world;
            constructNest(serverWorld, pos);
        }
    }

    private boolean shouldConstructNest(World world, BlockPos pos) {
        // Additional logic can be added to check conditions like surrounding area, etc.
        return !isNestAlreadyPresent(world, pos);
    }

    private boolean isNestAlreadyPresent(World world, BlockPos pos) {
        // Implement a check to see if a nest already exists around this block
        // Placeholder logic
        return false;
    }

    private void constructNest(ServerWorld world, BlockPos originPos) {
        // Define bounds for the nest construction
        int radius = 1; // for a 3x3x3 cube
        BlockPos.Mutable mutablePos = new BlockPos.Mutable();

        // Iterate over a cube area to construct the nest
        for (int y = -1; y >= -3; y--) { // Building downwards
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    mutablePos.set(originPos).move(x, y, z);
                    BlockState newState = getBlockStateForPosition(x, y, z, world, mutablePos);
                    world.setBlockState(mutablePos, newState, 3);
                }
            }
        }

        // Replace the origin block last to avoid collapsing the entity
        world.setBlockState(originPos, Modblocks.SNIFFLER_COLONY_ENTER_BLOCK.getDefaultState(), 3);
    }

    private BlockState getBlockStateForPosition(int x, int y, int z, World world, BlockPos pos) {
        // Example logic to determine which block state to place based on position
        if (y == -3) {
            return Modblocks.SNIFFLER_COLONY_FEED_BLOCK.getDefaultState(); // Bottom layer
        } else {
            return Modblocks.SNIFFLER_COLONY_BLOCK.getDefaultState(); // Middle layers
        }
    }
}