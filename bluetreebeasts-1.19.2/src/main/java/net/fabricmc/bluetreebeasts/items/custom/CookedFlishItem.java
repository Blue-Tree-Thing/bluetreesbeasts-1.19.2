package net.fabricmc.bluetreebeasts.items.custom;

import net.minecraft.item.FoodComponent;

public class CookedFlishItem {
    public static final FoodComponent COOKEDFLISH = new FoodComponent.Builder().hunger(8).saturationModifier(4f).meat().build();
}
