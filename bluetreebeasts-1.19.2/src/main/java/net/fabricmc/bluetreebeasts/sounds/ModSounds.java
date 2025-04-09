package net.fabricmc.bluetreebeasts.sounds;

import net.fabricmc.bluetreebeasts.BlueTreeBeasts;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModSounds {




    private static SoundEvent registerSoundEvent(String name){
        Identifier id = new Identifier(BlueTreeBeasts.MODID, name);
        return Registry.register(Registry.SOUND_EVENT, id, new SoundEvent(id));
    }
    public static void registerModSounds(){
        BlueTreeBeasts.LOGGER.debug("Registering mod items for " + BlueTreeBeasts.MODID);
    }
}
