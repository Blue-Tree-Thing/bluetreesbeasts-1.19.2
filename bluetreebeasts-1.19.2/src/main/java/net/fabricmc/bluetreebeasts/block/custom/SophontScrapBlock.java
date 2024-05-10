package net.fabricmc.bluetreebeasts.block.custom;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;

public class SophontScrapBlock extends Block {
    public SophontScrapBlock() {
        super(FabricBlockSettings.of(Material.METAL).strength(3.0f));
    }

}
