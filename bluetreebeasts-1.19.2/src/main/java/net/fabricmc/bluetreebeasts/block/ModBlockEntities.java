package net.fabricmc.bluetreebeasts.block;

import net.fabricmc.bluetreebeasts.BlueTreeBeasts;
import net.fabricmc.bluetreebeasts.block.entity.SnifflerColonyEnterBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlockEntities {
    public static BlockEntityType<SnifflerColonyEnterBlockEntity> SNIFFLER_COLONY_ENTER_BLOCK_ENTITY;

    public static void registerAllBlockEntities() {
        // Assuming your MODID is correctly referenced from BlueTreeBeasts.MODID
        SNIFFLER_COLONY_ENTER_BLOCK_ENTITY = Registry.register(
                Registry.BLOCK_ENTITY_TYPE,
                new Identifier(BlueTreeBeasts.MODID, "sniffler_colony_enter"),
                FabricBlockEntityTypeBuilder.create(SnifflerColonyEnterBlockEntity::new, Modblocks.SNIFFLER_COLONY_ENTER_BLOCK).build()
        );

        BlueTreeBeasts.LOGGER.info("Registering Modded Block Entities for " + BlueTreeBeasts.MODID);
    }
}
