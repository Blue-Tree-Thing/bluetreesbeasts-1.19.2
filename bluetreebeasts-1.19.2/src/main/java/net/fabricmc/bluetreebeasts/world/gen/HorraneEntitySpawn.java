package net.fabricmc.bluetreebeasts.world.gen;

import net.fabricmc.bluetreebeasts.entities.ModEntities;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.BiomeKeys;


public class HorraneEntitySpawn {

    public static void addHorraneEntitySpawn(){
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(BiomeKeys.SAVANNA, BiomeKeys.WINDSWEPT_SAVANNA), SpawnGroup.CREATURE, ModEntities.HORRANE, 60, 2,4);
        SpawnRestriction.register(ModEntities.HORRANE, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::isValidNaturalSpawn);
    }


}
