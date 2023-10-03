package net.fabricmc.bluetreebeasts.world.gen;

import net.fabricmc.bluetreebeasts.entities.ModEntities;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;


public class OceanFlishEntitySpawn {

    public static void addOceanFlishEntitySpawn(){
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(BiomeKeys.OCEAN, BiomeKeys.LUKEWARM_OCEAN, BiomeKeys.WARM_OCEAN, BiomeKeys.MUSHROOM_FIELDS), SpawnGroup.CREATURE, ModEntities.OCEANFLISH, 50, 5,8);
        SpawnRestriction.register(ModEntities.OCEANFLISH, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.WORLD_SURFACE_WG, AnimalEntity::isValidNaturalSpawn);
    }


}
