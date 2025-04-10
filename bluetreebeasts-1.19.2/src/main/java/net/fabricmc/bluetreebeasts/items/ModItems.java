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


    public static final Item GRAPPLERCHOP = registerItem("grappler_chop", new Item(new Item.Settings().group(ModItemGroup.BLUETREEBEASTS)));
    public static final Item RAWFLISH = registerItem("raw_flish", new Item(new Item.Settings().food(RawFlishItem.RAWFLISH).group(ModItemGroup.BLUETREEBEASTS)));
    public static final Item COOKEDFLISH = registerItem("cooked_flish", new Item(new Item.Settings().food(CookedFlishItem.COOKEDFLISH).group(ModItemGroup.BLUETREEBEASTS)));
    public static final Item RAWGIGANTELOPE = registerItem("raw_gigantelope", new Item(new Item.Settings().food(RawGigantelopeItem.RAWGIGANTELOPE).group(ModItemGroup.BLUETREEBEASTS)));
    public static final Item COOKEDGIGANTELOPE= registerItem("cooked_gigantelope", new Item(new Item.Settings().food(CookedGigantelopeItem.COOKEDGIGANTELOPE).group(ModItemGroup.BLUETREEBEASTS)));
    public static final Item GIGANTELOPEANTLER = registerItem("gigantelope_antler", new Item(new Item.Settings().group(ModItemGroup.BLUETREEBEASTS)));
    public static final Item HORRANEHIDE = registerItem("horrane_hide", new Item(new Item.Settings().group(ModItemGroup.BLUETREEBEASTS)));
    public static final Item SNIFFLERPRODUCE = registerItem("sniffler_produce", new Item(new Item.Settings().group(ModItemGroup.BLUETREEBEASTS)));
    public static final Item HOPPERSHELLFRAGMENTITEM = registerItem("hopper_shell_fragment", new Item(new Item.Settings().group(ModItemGroup.BLUETREEBEASTS)));
    public static final Item HOPPERSHELLITEM = registerItem("hopper_shell", new HopperShellItem(new Item.Settings().group(ModItemGroup.BLUETREEBEASTS)));


    public static final Item DERELICT_MECHANISM = registerItem("derelict_mechanism", new Item(new Item.Settings().group(ModItemGroup.BLUETREEBEASTS)));
    public static final Item OCEAN_FLISH_WING = registerItem("ocean_flish_wing", new Item(new Item.Settings().group(ModItemGroup.BLUETREEBEASTS)));
    public static final Item FOREST_FLISH_WING = registerItem("forest_flish_wing", new Item(new Item.Settings().group(ModItemGroup.BLUETREEBEASTS)));

    public static final Item METALDETECTORITEM = registerItem("metal_detector", new MetalDetectorItem(new Item.Settings().group(ModItemGroup.BLUETREEBEASTS).fireproof()));
    public static final Item FLUTTERPACK = registerItem("flutter_pack", new FlutterPackItem(new Item.Settings().group(ModItemGroup.BLUETREEBEASTS).fireproof()));




    // Hellbender: Bold Orange (#FF8C00) and Light Orange (#FFA500)

    // Tyflew: Grey (#808080) and Light Blue (#ADD8E6)

    // Woolly Gigantelope: Light Gray (#D3D3D3) and Beige (#F5F5DC)
    public static final SpawnEggItem WOOLLYGIGANTELOPESPAWNEGG = (SpawnEggItem) registerItem ("woolly_gigantelope_spawn_egg", new SpawnEggItem(ModEntities.WOOLLYGIGANTELOPE, 0xD3D3D3, 0xF5F5DC, new FabricItemSettings().group(ModItemGroup.BLUETREEBEASTS).fireproof()));

    // Ocean Flish: Aquamarine (#7FFFD4) and Scarlet (#FF2400)
    public static final SpawnEggItem OCEANFLISHSPAWNEGG = (SpawnEggItem) registerItem ("ocean_flish_spawn_egg", new SpawnEggItem(ModEntities.OCEANFLISH, 0x7FFFD4, 0xFF2400, new FabricItemSettings().group(ModItemGroup.BLUETREEBEASTS).fireproof()));

    // Forest Flish: Lime (#00FF00) and Orange (#FFA500)
    public static final SpawnEggItem FORESTFLISHSPAWNEGG = (SpawnEggItem) registerItem ("forest_flish_spawn_egg", new SpawnEggItem(ModEntities.FORESTFLISH, 0x00FF00, 0xFFA500, new FabricItemSettings().group(ModItemGroup.BLUETREEBEASTS).fireproof()));

    // Greater Grappler: Brown (#A52A2A) and Light Brown (#D2B48C)
    public static final SpawnEggItem GREATERGRAPPLERSPAWNEGG = (SpawnEggItem) registerItem ("greater_grappler_spawn_egg", new SpawnEggItem(ModEntities.GREATERGRAPPLER, 0xA52A2A, 0xD2B48C, new FabricItemSettings().group(ModItemGroup.BLUETREEBEASTS).fireproof()));

    public static final SpawnEggItem CITYSNIFFLERSPAWNEGG = (SpawnEggItem) registerItem ("city_sniffler_spawn_egg", new SpawnEggItem(ModEntities.CITY_SNIFFLER, 0x32CD32, 0xD2B48C, new FabricItemSettings().group(ModItemGroup.BLUETREEBEASTS).fireproof()));

    public static final SpawnEggItem GANNETWHALESPAWNEGG = (SpawnEggItem) registerItem ("gannet_whale_spawn_egg", new SpawnEggItem(ModEntities.GANNETWHALE, 0x32CD32, 0xD2B48C, new FabricItemSettings().group(ModItemGroup.BLUETREEBEASTS).fireproof()));

    public static final SpawnEggItem DESERTHOPPERSPAWNEGG = (SpawnEggItem) registerItem ("desert_hopper_spawn_egg", new SpawnEggItem(ModEntities.DESERT_HOPPER, 0x32CD32, 0xD2B48C, new FabricItemSettings().group(ModItemGroup.BLUETREEBEASTS).fireproof()));

    public static final SpawnEggItem HORRANESPAWNEGG = (SpawnEggItem) registerItem ("horrane_spawn_egg", new SpawnEggItem(ModEntities.HORRANE, 0x32CD32, 0xD2B48C, new FabricItemSettings().group(ModItemGroup.BLUETREEBEASTS).fireproof()));

    private static Item registerItem (String name, Item item){
        return Registry.register(Registry.ITEM, new Identifier(BlueTreeBeasts.MODID, name), item);
    }
    public static void registerModItems(){
        BlueTreeBeasts.LOGGER.debug("Registering mod items for " + BlueTreeBeasts.MODID);
    }
}
