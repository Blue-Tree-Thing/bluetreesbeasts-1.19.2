package net.fabricmc.bluetreebeasts.items.custom;



import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.FoodComponents;


public class QuackJarOfCowardiceItem extends FoodComponents {

public static final FoodComponent QUACKJAROFCOWARDICE = new FoodComponent.Builder().hunger(1).saturationModifier(.6f).statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 1300, 2), 1f).statusEffect(new StatusEffectInstance(StatusEffects.HASTE, 600, 2), 1f).statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 200, 3),.5f).build();

}
