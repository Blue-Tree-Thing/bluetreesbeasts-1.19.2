package net.fabricmc.bluetreebeasts.effect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;


import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;



public class SpectralPoisonEffect extends StatusEffect {

    protected SpectralPoisonEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }
    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {

        if(!entity.world.isClient()){
            entity.damage(DamageSource.MAGIC.setBypassesArmor().setBypassesProtection(),1);

        }
        super.applyUpdateEffect(entity, amplifier);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}
