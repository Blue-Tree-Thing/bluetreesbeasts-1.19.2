package net.fabricmc.bluetreebeasts;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.bluetreebeasts.block.ModBlocks;
import net.fabricmc.bluetreebeasts.effect.ModEffects;
import net.fabricmc.bluetreebeasts.entities.ModEntities;
import net.fabricmc.bluetreebeasts.entities.custom.*;
import net.fabricmc.bluetreebeasts.items.ModItems;
import net.fabricmc.bluetreebeasts.recipe.ModRecipes;
import net.fabricmc.bluetreebeasts.sounds.ModSounds;
import net.fabricmc.bluetreebeasts.world.gen.ForestFlishEntitySpawn;
import net.fabricmc.bluetreebeasts.world.gen.OceanFlishEntitySpawn;
import net.fabricmc.bluetreebeasts.world.gen.WoollyGigantelopeEntitySpawn;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.bernie.geckolib3.GeckoLib;


public class BlueTreeBeasts implements ModInitializer {

	public static final String MODID = "bluetreebeasts";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	@Override
	public void onInitialize() {
		BTBStructures.registerStructureFeatures();
		ModItems.registerModItems();
		ModSounds.registerModSounds();
		ModBlocks.registerModBlocks();
		ModEffects.registerEffects();
		ModRecipes.registerRecipes();
		GeckoLib.initialize();
		ModEntities.registerModEntities();
		ForestFlishEntitySpawn.addForestFlishEntitySpawn();
		OceanFlishEntitySpawn.addOceanFlishEntitySpawn();
		WoollyGigantelopeEntitySpawn.addWoollyGigantelopeEntitySpawn();
		FabricDefaultAttributeRegistry.register(ModEntities.HELLBENDER, HellBenderEntity.setAttributes());
		FabricDefaultAttributeRegistry.register(ModEntities.GREATERGRAPPLER, GreaterGrapplerEntity.setAttributes());
		FabricDefaultAttributeRegistry.register(ModEntities.FORESTFLISH, ForestFlishEntity.setAttributes());
		FabricDefaultAttributeRegistry.register(ModEntities.OCEANFLISH, OceanFlishEntity.setAttributes());
		FabricDefaultAttributeRegistry.register(ModEntities.WOOLLYGIGANTELOPE, WoollyGigantelopeEntity.setAttributes());

	}
}
