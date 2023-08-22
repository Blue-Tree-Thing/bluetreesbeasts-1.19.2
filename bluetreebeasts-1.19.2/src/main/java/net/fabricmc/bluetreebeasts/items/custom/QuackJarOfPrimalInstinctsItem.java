package net.fabricmc.bluetreebeasts.items.custom;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.FoodComponents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class QuackJarOfPrimalInstinctsItem extends Item {

    public static final FoodComponent QUACKJAROFPRIMALINSTINCT = new FoodComponent.Builder().hunger(1).saturationModifier(.6f).statusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 1660, 2), 1f).statusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 1660, 2), 1f).statusEffect(new StatusEffectInstance(StatusEffects.HEALTH_BOOST, 1660, 3),1f).statusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 1660, 3),1f).build();

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if(Screen.hasShiftDown()){
            tooltip.add(Text.literal("This will get your blood pumping!").formatted(Formatting.AQUA));
        }else{
            tooltip.add(Text.literal("Press Shift For Enlightenment :)").formatted(Formatting.GOLD));
        }
    }
    public QuackJarOfPrimalInstinctsItem(Settings settings) {
        super(settings);
    }
}
