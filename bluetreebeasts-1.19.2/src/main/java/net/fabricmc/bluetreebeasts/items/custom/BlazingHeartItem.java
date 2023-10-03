package net.fabricmc.bluetreebeasts.items.custom;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.fabricmc.bluetreebeasts.effect.ModEffects;
import net.fabricmc.bluetreebeasts.items.ModItems;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;


public class BlazingHeartItem extends Item {

public static final UUID ATTACK_DAMAGE_MODIFIER = UUID.fromString("648D7064-6A60-4F59-8ABE-C2C23A6DD7A9");
public static final UUID MOVEMENT_SPEED_MODIFIER = UUID.fromString("7107DE5E-7CE8-4030-940E-514C1F160890");

    public BlazingHeartItem(Settings settings) {
        super(settings);
    }



    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (Screen.hasShiftDown()) {
            tooltip.add(Text.literal("Equip for immunity from most bad status effects!" +
                    " What else will it protect against?").formatted(Formatting.AQUA));
        } else {
            tooltip.add(Text.literal("Press Shift For Enlightenment :)").formatted(Formatting.GOLD));
        }
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(ItemStack stack, EquipmentSlot slot) {
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        Multimap <EntityAttribute, EntityAttributeModifier> modifier = super.getAttributeModifiers(slot);

        builder.putAll(modifier);

        if(slot == EquipmentSlot.OFFHAND){
            builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER,"Attack Damage Modifier", 2, EntityAttributeModifier.Operation.ADDITION));
            builder.put(EntityAttributes.GENERIC_MOVEMENT_SPEED, new EntityAttributeModifier(MOVEMENT_SPEED_MODIFIER,"Movement Speed Modifier", .25, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
        }
        return builder.build();
    }



    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (!world.isClient) {
            if (entity instanceof PlayerEntity player) {
                if (player.getOffHandStack().isOf(ModItems.BLAZINGHEART)) {
                    player.removeStatusEffect(StatusEffects.WITHER);
                    player.removeStatusEffect(StatusEffects.HUNGER);
                    player.removeStatusEffect(StatusEffects.POISON);
                    player.removeStatusEffect(StatusEffects.SLOWNESS);
                    player.removeStatusEffect(StatusEffects.WEAKNESS);
                    player.removeStatusEffect(StatusEffects.BAD_OMEN);
                    player.removeStatusEffect(StatusEffects.NAUSEA);
                    player.removeStatusEffect(StatusEffects.BLINDNESS);
                    player.removeStatusEffect(StatusEffects.DARKNESS);
                    player.removeStatusEffect(StatusEffects.LEVITATION);
                    player.removeStatusEffect(StatusEffects.MINING_FATIGUE);
                    player.removeStatusEffect(StatusEffects.UNLUCK);
                    player.removeStatusEffect(ModEffects.SPECTRALPOISON);
                }
            }
        }
    }
}
