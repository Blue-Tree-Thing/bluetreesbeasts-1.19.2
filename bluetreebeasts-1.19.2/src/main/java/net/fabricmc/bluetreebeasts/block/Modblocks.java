package net.fabricmc.bluetreebeasts.block;

import net.fabricmc.bluetreebeasts.BlueTreeBeasts;
import net.fabricmc.bluetreebeasts.items.ModItemGroup;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Modblocks {

    public static final Block BLOBSTONE_BLOCK = registerBlock("blobstone_block", new Block(FabricBlockSettings.of(Material.STONE).strength(10000f,10000f)), ModItemGroup.BLUETREEBEASTS);

    private static Block registerBlock(String name, Block block, ItemGroup tab){
        registerBlockItem(name, block, tab);
        return Registry.register(Registry.BLOCK, new Identifier(BlueTreeBeasts.MODID, name),block);
    }
    private static Item registerBlockItem(String name, Block block, ItemGroup tab){
        return Registry.register(Registry.ITEM, new Identifier(BlueTreeBeasts.MODID, name), new BlockItem(block, new FabricItemSettings().group(tab)));
    }
    public static void registerModBlocks(){
        BlueTreeBeasts.LOGGER.debug("Registering Modded Blocks for" + BlueTreeBeasts.MODID);
    }
}
