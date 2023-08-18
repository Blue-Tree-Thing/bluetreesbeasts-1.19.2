package net.fabricmc.bluetreebeasts.sounds;

import net.fabricmc.bluetreebeasts.BlueTreeBeasts;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModSounds {

    public static SoundEvent HELLBENDER_AMBIENT = registerSoundEvent("hell_bender_ambient");
    public static SoundEvent HELLBENDER_HURT = registerSoundEvent("hell_bender_hurt");
    public static SoundEvent HELLBENDER_DEATH = registerSoundEvent("hell_bender_death");

    private static SoundEvent registerSoundEvent(String name){
        Identifier id = new Identifier(BlueTreeBeasts.MODID, name);
        return Registry.register(Registry.SOUND_EVENT, id, new SoundEvent(id));
    }
    public static void registerModSounds(){
        BlueTreeBeasts.LOGGER.debug("Registering mod items for " + BlueTreeBeasts.MODID);
    }
}
