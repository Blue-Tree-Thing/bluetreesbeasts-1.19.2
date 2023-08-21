package net.fabricmc.bluetreebeasts.items;

import net.fabricmc.bluetreebeasts.BlueTreeBeasts;
import net.fabricmc.bluetreebeasts.entities.ModEntities;
import net.fabricmc.bluetreebeasts.items.custom.HeartburnGlaiveItem;
import net.fabricmc.bluetreebeasts.items.custom.QuackJarOfCowardiceItem;
import net.fabricmc.bluetreebeasts.items.custom.QuackJarOfPrimalInstinctsItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModItems {


    public static final Item HELLBENDEREART = registerItem("hell_bender_heart", new Item(new Item.Settings().group(ModItemGroup.BLUETREEBEASTS).fireproof()));
    public static final Item GRAPPLERCHOP = registerItem("grappler_chop", new Item(new Item.Settings().group(ModItemGroup.BLUETREEBEASTS)));
    public static final Item EMPTYQUACKJAR = registerItem("empty_quack_jar", new Item(new Item.Settings().group(ModItemGroup.BLUETREEBEASTS)));
    public static final Item QUACKJAROFPRIMALINSTINCT = registerItem("quack_jar_of_primal_instinct", new Item(new Item.Settings().food(QuackJarOfPrimalInstinctsItem.QUACKJAROFPRIMALINSTINCT).group(ModItemGroup.BLUETREEBEASTS)));
    public static final Item QUACKJAROFCOWARDICE = registerItem("quack_jar_of_cowardice", new Item(new Item.Settings().food(QuackJarOfCowardiceItem.QUACKJAROFCOWARDICE).group(ModItemGroup.BLUETREEBEASTS)));
    public static final Item COWARDICELEAF = registerItem("cowardice_leaf", new Item(new Item.Settings().group(ModItemGroup.BLUETREEBEASTS)));

    public static final SpawnEggItem HELLBENDERSPAWNEGG = (SpawnEggItem) registerItem ("hell_bender_spawn_egg", new SpawnEggItem(ModEntities.HELLBENDER, 0xf44600,0xeff660, new FabricItemSettings().group(ModItemGroup.BLUETREEBEASTS).fireproof()));
    public static final SpawnEggItem GREATERGRAPPLERSPAWNEGG = (SpawnEggItem) registerItem ("greater_grappler_spawn_egg", new SpawnEggItem(ModEntities.GREATERGRAPPLER, 0xfa9100,0xefd110, new FabricItemSettings().group(ModItemGroup.BLUETREEBEASTS).fireproof()));
    public static final SpawnEggItem FORESTFLISHSPAWNEGG = (SpawnEggItem) registerItem ("forest_flish_spawn_egg", new SpawnEggItem(ModEntities.FORESTFLISH, 0xa66420,0x9999, new FabricItemSettings().group(ModItemGroup.BLUETREEBEASTS).fireproof()));
    public static final SpawnEggItem OCEANFLISHSPAWNEGG = (SpawnEggItem) registerItem ("ocean_flish_spawn_egg", new SpawnEggItem(ModEntities.OCEANFLISH, 0xa77726,0xa33190, new FabricItemSettings().group(ModItemGroup.BLUETREEBEASTS).fireproof()));

    public static final Item HEARTBURN_GLAIVE = registerItem("heartburn_glaive",new HeartburnGlaiveItem(BTBToolMaterials.GLAIVE, 7, -2f, new FabricItemSettings().group(ModItemGroup.BLUETREEBEASTS).fireproof()));

    private static Item registerItem (String name, Item item){
        return Registry.register(Registry.ITEM, new Identifier(BlueTreeBeasts.MODID, name), item);
    }
    public static void registerModItems(){
        BlueTreeBeasts.LOGGER.debug("Registering mod items for " + BlueTreeBeasts.MODID);
    }
}
