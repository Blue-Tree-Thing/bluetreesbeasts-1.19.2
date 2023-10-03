package net.fabricmc.bluetreebeasts.items.custom;

import net.fabricmc.bluetreebeasts.items.ModItems;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TyflewsCallItem extends Item {


    public TyflewsCallItem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (Screen.hasShiftDown()) {
            tooltip.add(Text.literal("Equip to summon Tyflutters!"
            ).formatted(Formatting.AQUA));
        } else {
            tooltip.add(Text.literal("Press Shift For Enlightenment :)").formatted(Formatting.GOLD));
        }
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        if (!world.isClient && user.getStackInHand(Hand.OFF_HAND).isOf(ModItems.TYFLEWSCALL) && !user.getStackInHand(Hand.MAIN_HAND).isOf(ModItems.TYFLEWSCALL)) {
            world.playSound(user, user.getX(), user.getY(), user.getZ(), SoundEvents.BLOCK_AMETHYST_CLUSTER_BREAK, SoundCategory.NEUTRAL, 1F, 2F); // plays a globalSoundEvent

            user.getItemCooldownManager().set(this,20);
        }
        return TypedActionResult.success(itemStack, world.isClient());
    }
}
