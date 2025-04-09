package net.fabricmc.bluetreebeasts.effect;

import net.fabricmc.bluetreebeasts.BlueTreeBeasts;
import net.minecraft.entity.effect.StatusEffect;

import net.minecraft.entity.effect.StatusEffectCategory;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModEffects {


    private static StatusEffect registerStatusEffect (String name, StatusEffect statusEffect){
        return Registry.register(Registry.STATUS_EFFECT, new Identifier(BlueTreeBeasts.MODID, name), statusEffect);
    }
    public static void registerEffects(){
        BlueTreeBeasts.LOGGER.debug("Registering mod effects for " + BlueTreeBeasts.MODID);
    }
}
