package net.fabricmc.bluetreebeasts.block.entity;

import net.fabricmc.bluetreebeasts.BlueTreeBeasts;
import net.fabricmc.bluetreebeasts.block.ModBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlockEntities {
    public static BlockEntityType<QuackStationBlockEntity> QUACKSTATION;

    public static void registerBlockEntities(){
        QUACKSTATION = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(BlueTreeBeasts.MODID, "quack_station"), FabricBlockEntityTypeBuilder.create(QuackStationBlockEntity::new, ModBlocks.QUACKSTATION).build(null));
    }
}
