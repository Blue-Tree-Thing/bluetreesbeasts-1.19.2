package net.fabricmc.bluetreebeasts.items.custom;


import net.fabricmc.bluetreebeasts.items.BTBToolMaterials;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.minecraft.text.Text;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.Formatting;
import net.minecraft.client.item.TooltipContext;
import org.jetbrains.annotations.Nullable;
import java.util.List;

public class HeartburnGlaiveItem extends SwordItem {

    public HeartburnGlaiveItem(Settings settings) {
        super(BTBToolMaterials.HEARTBURN_GLAIVE, 7, -2.4f, settings); // Adjust the damage and speed as desired

    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if(Screen.hasShiftDown()){
            tooltip.add(Text.literal("Will Burn Targets Until " +
                    "The Heat Death Of The Universe").formatted(Formatting.AQUA));
        }else{
            tooltip.add(Text.literal("Press Shift For Enlightenment :)").formatted(Formatting.GOLD));
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        user.incrementStat(Stats.USED.getOrCreateStat(this));
        user.swingHand(hand);
        return TypedActionResult.success(itemStack, world.isClient());
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        super.onStoppedUsing(stack, world, user, remainingUseTicks);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        target.setOnFireFor(86400);
        return super.postHit(stack, target, attacker);
    }
}
