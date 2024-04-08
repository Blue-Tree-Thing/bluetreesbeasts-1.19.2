package net.fabricmc.bluetreebeasts.block.custom;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;


public class SnifflerColonyFeedBlock extends Block {
    public SnifflerColonyFeedBlock() {
        super(FabricBlockSettings.of(Material.SOIL).strength(2.0f));
    }
}
