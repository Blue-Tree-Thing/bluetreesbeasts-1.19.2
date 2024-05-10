package net.fabricmc.bluetreebeasts.block;

import net.fabricmc.bluetreebeasts.BlueTreeBeasts;
import net.fabricmc.bluetreebeasts.block.custom.*;
import net.fabricmc.bluetreebeasts.items.ModItemGroup;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Modblocks {

    // Define blocks
    public static final Block SNIFFLER_COLONY_ORIGIN_BLOCK = new SnifflerColonyOriginBlock();
    public static final Block SNIFFLER_COLONY_ENTER_BLOCK = new SnifflerColonyEnterBlock();
    public static final Block SNIFFLER_COLONY_BLOCK = new SnifflerColonyBlock();
    public static final Block SNIFFLER_COLONY_FEED_BLOCK = new SnifflerColonyFeedBlock();
    public static final Block SOPHONT_SCRAP = new SophontScrapBlock();
    public static final Block EVER_METAL = new EverMetalBlock();

    private static Block registerBlock(String name, Block block, ItemGroup tab){
        registerBlockItem(name, block, tab);
        return Registry.register(Registry.BLOCK, new Identifier(BlueTreeBeasts.MODID, name), block);
    }

    private static Item registerBlockItem(String name, Block block, ItemGroup tab){
        return Registry.register(Registry.ITEM, new Identifier(BlueTreeBeasts.MODID, name), new BlockItem(block, new FabricItemSettings().group(tab)));
    }

    public static void registerModBlocks(){
        BlueTreeBeasts.LOGGER.debug("Registering Modded Blocks for " + BlueTreeBeasts.MODID);

        // Register blocks
        registerBlock("sniffler_colony_origin_block", SNIFFLER_COLONY_ORIGIN_BLOCK, ModItemGroup.BLUETREEBEASTS);
        registerBlock("sniffler_colony_enter_block", SNIFFLER_COLONY_ENTER_BLOCK, ModItemGroup.BLUETREEBEASTS);
        registerBlock("sniffler_colony_block", SNIFFLER_COLONY_BLOCK, ModItemGroup.BLUETREEBEASTS);
        registerBlock("sniffler_colony_feed_block", SNIFFLER_COLONY_FEED_BLOCK, ModItemGroup.BLUETREEBEASTS);
        registerBlock("sophont_scrap", SOPHONT_SCRAP, ModItemGroup.BLUETREEBEASTS);
        registerBlock("ever_metal_block", EVER_METAL, ModItemGroup.BLUETREEBEASTS);
    }
}
