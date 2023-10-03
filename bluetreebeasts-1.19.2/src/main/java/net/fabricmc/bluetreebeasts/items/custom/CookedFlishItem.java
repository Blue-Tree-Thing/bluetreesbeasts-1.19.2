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


public class CookedFlishItem extends Item {
    public static final FoodComponent COOKEDFLISH = new FoodComponent.Builder().hunger(8).saturationModifier(4f).meat().build();


    public CookedFlishItem(Settings settings) {
        super(settings);
    }

}
