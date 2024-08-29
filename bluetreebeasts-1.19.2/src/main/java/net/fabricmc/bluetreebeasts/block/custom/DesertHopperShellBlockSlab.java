package net.fabricmc.bluetreebeasts.block.custom;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.SlabBlock;

public class DesertHopperShellBlockSlab extends SlabBlock {
    public DesertHopperShellBlockSlab() {
        super(FabricBlockSettings.of(Material.STONE).strength(3.0f));
    }

}
