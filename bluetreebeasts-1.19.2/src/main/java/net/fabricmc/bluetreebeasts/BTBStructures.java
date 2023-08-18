package net.fabricmc.bluetreebeasts;

import net.fabricmc.bluetreebeasts.structures.BlueTreeBeastsStructures;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.structure.StructureType;

public class BTBStructures {
    public static StructureType<BlueTreeBeastsStructures> BLUETREEBEASTS_STRUCTURE;


    public static void registerStructureFeatures() {
        BLUETREEBEASTS_STRUCTURE = Registry.register(Registry.STRUCTURE_TYPE, new Identifier(BlueTreeBeasts.MODID, "bluetreebeasts"), BlueTreeBeastsStructures::CODEC);
    }
}
