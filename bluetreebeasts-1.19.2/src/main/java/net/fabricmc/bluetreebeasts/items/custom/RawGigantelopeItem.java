package net.fabricmc.bluetreebeasts.items.custom;



import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;


public class RawGigantelopeItem extends Item {
    public static final FoodComponent RAWGIGANTELOPE = new FoodComponent.Builder().hunger(5).saturationModifier(3f).meat().build();

    public RawGigantelopeItem(Settings settings) {
        super(settings);
    }
}
