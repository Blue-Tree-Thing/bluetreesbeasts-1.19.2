package net.fabricmc.bluetreebeasts.block.custom;

import net.fabricmc.bluetreebeasts.block.entity.SnifflerColonyEnterBlockEntity;
import net.fabricmc.bluetreebeasts.block.entity.SnifflerColonyFeedBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;


public class SnifflerColonyFeedBlock extends Block implements BlockEntityProvider {
    public SnifflerColonyFeedBlock() {
        super(FabricBlockSettings.of(Material.SOIL).strength(2.0f));
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new SnifflerColonyFeedBlockEntity(pos, state);
    }
}
