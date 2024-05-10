package net.fabricmc.bluetreebeasts.block.custom;

import net.fabricmc.bluetreebeasts.block.entity.SnifflerColonyEnterBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;


public class SnifflerColonyEnterBlock extends Block implements BlockEntityProvider {
    public SnifflerColonyEnterBlock() {
        super(FabricBlockSettings.of(Material.SOIL).ticksRandomly().strength(1.0f));
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new SnifflerColonyEnterBlockEntity(pos, state);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        super.randomTick(state, world, pos, random);
        BlockEntity be = world.getBlockEntity(pos);
        if (be instanceof SnifflerColonyEnterBlockEntity) {
            ((SnifflerColonyEnterBlockEntity) be).discoverFeedBlock();
            System.out.println("Random tick at Block Entity Position: " + pos);
            ((SnifflerColonyEnterBlockEntity) be).releaseSnifflers();
        }
    }
}
