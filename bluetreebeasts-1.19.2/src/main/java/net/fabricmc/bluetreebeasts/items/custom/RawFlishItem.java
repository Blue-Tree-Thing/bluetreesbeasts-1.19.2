package net.fabricmc.bluetreebeasts.items.custom;



import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public class RawFlishItem extends Item {
    public static final FoodComponent RAWFLISH = new FoodComponent.Builder().hunger(4).saturationModifier(2f).meat().build();

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if(Screen.hasShiftDown()){
            tooltip.add(Text.literal("It's sushi that flies. What more to say?").formatted(Formatting.AQUA));
        }else{
            tooltip.add(Text.literal("Press Shift For Enlightenment :)").formatted(Formatting.GOLD));
        }
    }
    public RawFlishItem(Settings settings) {
        super(settings);
    }
}
