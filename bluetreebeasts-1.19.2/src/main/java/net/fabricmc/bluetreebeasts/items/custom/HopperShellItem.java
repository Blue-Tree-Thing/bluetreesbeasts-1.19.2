package net.fabricmc.bluetreebeasts.items.custom;

import net.fabricmc.bluetreebeasts.entities.ModEntities;
import net.fabricmc.bluetreebeasts.entities.custom.HopperShellEntity;
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

public class HopperShellItem extends Item {
    public HopperShellItem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (Screen.hasShiftDown()) {
            tooltip.add(Text.literal("Equip to throw a Hopper's Shell!"
            ).formatted(Formatting.AQUA));
        } else {
            tooltip.add(Text.literal("Press Shift For Enlightenment :)").formatted(Formatting.GOLD));
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        // Ensure the item is used in the correct hand
        if (!world.isClient && user.getStackInHand(Hand.MAIN_HAND).isOf(ModItems.HOPPERSHELLITEM)) {
            HopperShellEntity hopperShell = ModEntities.HOPPERSHELL.create(world);
            if (hopperShell != null) {
                hopperShell.refreshPositionAndAngles(user.getX(), user.getEyeY() - 0.1, user.getZ(), user.getYaw(), user.getPitch());
                // Calculate the initial velocity vector based on the player's yaw
                double speed = 1.5; // Set the speed of the shell
                double yaw = Math.toRadians(user.getYaw());
                double xVelocity = -Math.sin(yaw) * speed;
                double zVelocity = Math.cos(yaw) * speed;
                hopperShell.setVelocity(xVelocity, 0, zVelocity);
                world.spawnEntity(hopperShell);
            }
            if (!user.getAbilities().creativeMode) {
                itemStack.decrement(1);
            }
            return TypedActionResult.success(itemStack, world.isClient());
        }
        return TypedActionResult.fail(itemStack);
    }
}
