package net.fabricmc.bluetreebeasts.block;

import net.fabricmc.bluetreebeasts.BlueTreeBeasts;
import net.fabricmc.bluetreebeasts.block.custom.QuackStationBlock;
import net.fabricmc.bluetreebeasts.items.ModItemGroup;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlocks {

    public static final Block HEALCUP = registerBlock("healcup", new PlantBlock(FabricBlockSettings.of(Material.PLANT).collidable(false)), ModItemGroup.BLUETREEBEASTS);
    public static final Block GASGRASS = registerBlock("gasgrass", new PlantBlock(FabricBlockSettings.of(Material.PLANT).collidable(false)), ModItemGroup.BLUETREEBEASTS);
    public static final Block QUACKSTATION = registerBlock("quack_station", new QuackStationBlock(FabricBlockSettings.of(Material.WOOD).strength(2f).requiresTool().nonOpaque()), ModItemGroup.BLUETREEBEASTS);

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
