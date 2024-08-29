package net.fabricmc.bluetreebeasts.world.gen;

import net.fabricmc.bluetreebeasts.entities.ModEntities;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.BiomeKeys;


public class DesertHopperEntitySpawn {

    public static void addDesertHopperEntitySpawn(){
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(BiomeKeys.DESERT, BiomeKeys.BADLANDS), SpawnGroup.CREATURE, ModEntities.DESERT_HOPPER, 60, 2,4);
        SpawnRestriction.register(ModEntities.DESERT_HOPPER, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::isValidNaturalSpawn);
    }


}
