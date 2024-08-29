package net.fabricmc.bluetreebeasts.block.custom;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;

public class DesertHopperShellBlock extends Block {
    public DesertHopperShellBlock() {
        super(FabricBlockSettings.of(Material.STONE).strength(3.0f));
    }

}
