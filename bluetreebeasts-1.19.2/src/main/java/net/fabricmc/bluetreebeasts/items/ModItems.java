package net.fabricmc.bluetreebeasts.items;

import net.fabricmc.bluetreebeasts.BlueTreeBeasts;
import net.fabricmc.bluetreebeasts.entities.ModEntities;
import net.fabricmc.bluetreebeasts.items.custom.*;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModItems {


    public static final Item HELLBENDEREART = registerItem("hell_bender_heart", new Item(new Item.Settings().group(ModItemGroup.BLUETREEBEASTS).fireproof()));
    public static final Item GRAPPLERCHOP = registerItem("grappler_chop", new Item(new Item.Settings().group(ModItemGroup.BLUETREEBEASTS)));
    public static final Item RAWFLISH = registerItem("raw_flish", new Item(new Item.Settings().food(RawFlishItem.RAWFLISH).group(ModItemGroup.BLUETREEBEASTS)));
    public static final Item COOKEDFLISH = registerItem("cooked_flish", new Item(new Item.Settings().food(CookedFlishItem.COOKEDFLISH).group(ModItemGroup.BLUETREEBEASTS)));
    public static final Item RAWGIGANTELOPE = registerItem("raw_gigantelope", new Item(new Item.Settings().food(RawGigantelopeItem.RAWGIGANTELOPE).group(ModItemGroup.BLUETREEBEASTS)));
    public static final Item COOKEDGIGANTELOPE= registerItem("cooked_gigantelope", new Item(new Item.Settings().food(CookedGigantelopeItem.COOKEDGIGANTELOPE).group(ModItemGroup.BLUETREEBEASTS)));
    public static final Item BEASTLYBURLAP = registerItem("beastly_burlap", new Item(new Item.Settings().group(ModItemGroup.BLUETREEBEASTS)));
    public static final Item SAVAGESILK = registerItem("savage_silk", new Item(new Item.Settings().group(ModItemGroup.BLUETREEBEASTS)));

    public static final Item BLAZINGHEART = registerItem("blazing_heart", new BlazingHeartItem(new Item.Settings().group(ModItemGroup.BLUETREEBEASTS).fireproof()));
    public static final Item TYFLEWSCALL = registerItem("tyflews_call", new TyflewsCallItem(new Item.Settings().group(ModItemGroup.BLUETREEBEASTS).fireproof()));




    public static final SpawnEggItem HELLBENDERSPAWNEGG = (SpawnEggItem) registerItem ("hell_bender_spawn_egg", new SpawnEggItem(ModEntities.HELLBENDER, 0xf44600,0xeff660, new FabricItemSettings().group(ModItemGroup.BLUETREEBEASTS).fireproof()));
    public static final SpawnEggItem TYFLEWSPAWNEGG = (SpawnEggItem) registerItem ("tyflew_spawn_egg", new SpawnEggItem(ModEntities.TYFLEW, 0xf44600,0xeff660, new FabricItemSettings().group(ModItemGroup.BLUETREEBEASTS).fireproof()));



    public static final SpawnEggItem GREATERGRAPPLERSPAWNEGG = (SpawnEggItem) registerItem ("greater_grappler_spawn_egg", new SpawnEggItem(ModEntities.GREATERGRAPPLER, 0xfa9100,0xefd110, new FabricItemSettings().group(ModItemGroup.BLUETREEBEASTS).fireproof()));
    public static final SpawnEggItem FORESTFLISHSPAWNEGG = (SpawnEggItem) registerItem ("forest_flish_spawn_egg", new SpawnEggItem(ModEntities.FORESTFLISH, 0xa66420,0x9999, new FabricItemSettings().group(ModItemGroup.BLUETREEBEASTS).fireproof()));
    public static final SpawnEggItem OCEANFLISHSPAWNEGG = (SpawnEggItem) registerItem ("ocean_flish_spawn_egg", new SpawnEggItem(ModEntities.OCEANFLISH, 0xa77726,0xa33190, new FabricItemSettings().group(ModItemGroup.BLUETREEBEASTS).fireproof()));
    public static final SpawnEggItem WOOLLYGIGANTELOPESPAWNEGG = (SpawnEggItem) registerItem ("woolly_gigantelope_spawn_egg", new SpawnEggItem(ModEntities.WOOLLYGIGANTELOPE, 0xd111266,0xe99800, new FabricItemSettings().group(ModItemGroup.BLUETREEBEASTS).fireproof()));

    public static final Item HEARTBURN_GLAIVE = registerItem("heartburn_glaive", new HeartburnGlaiveItem(new FabricItemSettings().group(ModItemGroup.BLUETREEBEASTS).fireproof()));
    private static Item registerItem (String name, Item item){
        return Registry.register(Registry.ITEM, new Identifier(BlueTreeBeasts.MODID, name), item);
    }
    public static void registerModItems(){
        BlueTreeBeasts.LOGGER.debug("Registering mod items for " + BlueTreeBeasts.MODID);
    }
}
