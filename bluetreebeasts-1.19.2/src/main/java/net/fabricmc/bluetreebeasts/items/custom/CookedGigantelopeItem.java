package net.fabricmc.bluetreebeasts.items.custom;



import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;


public class CookedGigantelopeItem extends Item {
    public static final FoodComponent COOKEDGIGANTELOPE = new FoodComponent.Builder().hunger(10).saturationModifier(6f).meat().build();


    public CookedGigantelopeItem(Settings settings) {
        super(settings);
    }

}
