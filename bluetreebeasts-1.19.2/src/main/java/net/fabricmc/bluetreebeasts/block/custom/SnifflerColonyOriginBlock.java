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

        // Construct the nest structure layer by layer
        // Top layer - Enter Block at the origin position
        world.setBlockState(originPos, Modblocks.SNIFFLER_COLONY_ENTER_BLOCK.getDefaultState(), 3);

        // Middle layer - Feed Block directly under the enter block
        BlockPos feedPos = originPos.down();
        world.setBlockState(feedPos, Modblocks.SNIFFLER_COLONY_FEED_BLOCK.getDefaultState(), 3);

        // Fill the remaining area around and below the feed block with colony blocks
        for (int y = 0; y >= -2; y--) { // Adjusted to -2 to fit the new design
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    mutablePos.set(originPos).move(x, y, z);
                    if (mutablePos.equals(originPos) || mutablePos.equals(feedPos)) {
                        // Skip the enter and feed blocks, already placed
                        continue;
                    }
                    BlockState newState = Modblocks.SNIFFLER_COLONY_BLOCK.getDefaultState(); // Use normal colony block
                    world.setBlockState(mutablePos, newState, 3);
                }
            }
        }
    }
}