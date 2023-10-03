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

    public RawFlishItem(Settings settings) {
        super(settings);
    }
}
