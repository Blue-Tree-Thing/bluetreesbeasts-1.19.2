package net.fabricmc.bluetreebeasts.items.custom;




import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;



public class CookedFlishItem extends Item {
    public static final FoodComponent COOKEDFLISH = new FoodComponent.Builder().hunger(8).saturationModifier(4f).meat().build();


    public CookedFlishItem(Settings settings) {
        super(settings);
    }

}
