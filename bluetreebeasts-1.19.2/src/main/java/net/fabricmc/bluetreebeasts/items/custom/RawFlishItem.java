package net.fabricmc.bluetreebeasts.items.custom;




import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;



public class RawFlishItem extends Item {
    public static final FoodComponent RAWFLISH = new FoodComponent.Builder().hunger(4).saturationModifier(2f).meat().build();

    public RawFlishItem(Settings settings) {
        super(settings);
    }
}
