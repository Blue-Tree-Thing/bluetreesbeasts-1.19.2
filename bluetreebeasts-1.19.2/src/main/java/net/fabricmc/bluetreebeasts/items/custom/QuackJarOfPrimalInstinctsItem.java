package net.fabricmc.bluetreebeasts.items.custom;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.FoodComponents;

public class QuackJarOfPrimalInstinctsItem extends FoodComponents {

    public static final FoodComponent QUACKJAROFPRIMALINSTINCT = new FoodComponent.Builder().hunger(1).saturationModifier(.6f).statusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 1660, 2), 1f).statusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 1660, 2), 1f).statusEffect(new StatusEffectInstance(StatusEffects.HEALTH_BOOST, 1660, 3),1f).statusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 1660, 3),1f).build();

}
