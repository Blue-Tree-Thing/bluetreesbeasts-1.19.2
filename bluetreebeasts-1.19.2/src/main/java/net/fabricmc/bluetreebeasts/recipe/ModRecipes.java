package net.fabricmc.bluetreebeasts.recipe;

import net.fabricmc.bluetreebeasts.BlueTreeBeasts;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModRecipes {
    public static void registerRecipes() {
        Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(BlueTreeBeasts.MODID, QuackStationRecipe.Serializer.ID),
                QuackStationRecipe.Serializer.INSTANCE);
        Registry.register(Registry.RECIPE_TYPE, new Identifier(BlueTreeBeasts.MODID, QuackStationRecipe.Type.ID),
                QuackStationRecipe.Type.INSTANCE);
    }
}
