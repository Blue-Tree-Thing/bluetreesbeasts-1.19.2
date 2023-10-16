package net.fabricmc.bluetreebeasts.items.custom;

import net.fabricmc.bluetreebeasts.entities.ModEntities;
import net.fabricmc.bluetreebeasts.entities.custom.HomingFlishEntity;
import net.fabricmc.bluetreebeasts.items.ModItems;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        // Ensure the item is used in the correct hand
        if (!world.isClient && user.getStackInHand(Hand.OFF_HAND).isOf(ModItems.TYFLEWSCALL) && !user.getStackInHand(Hand.MAIN_HAND).isOf(ModItems.TYFLEWSCALL)) {
            if (!world.isClient) {
                // Create and spawn the HomingFlishEntity
                HomingFlishEntity homingFlish = ModEntities.HOMINGFLISH.create(world);
                if (homingFlish != null) {
                    homingFlish.refreshPositionAndAngles(user.getX(), user.getY(), user.getZ(), 0.0F, 0.0F);
                    world.spawnEntity(homingFlish);
                }
                // Apply a cooldown to the item to prevent spamming
                user.getItemCooldownManager().set(this, 100); // Cooldown of 100 ticks (5 seconds)
            }
            return TypedActionResult.success(itemStack, world.isClient());
        }
        return TypedActionResult.fail(itemStack);
    }
}
