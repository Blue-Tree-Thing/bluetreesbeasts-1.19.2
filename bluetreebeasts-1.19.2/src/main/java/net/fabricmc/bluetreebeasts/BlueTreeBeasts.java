package net.fabricmc.bluetreebeasts;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.bluetreebeasts.block.ModBlocks;
import net.fabricmc.bluetreebeasts.block.entity.ModBlockEntities;
import net.fabricmc.bluetreebeasts.effect.ModEffects;
import net.fabricmc.bluetreebeasts.entities.ModEntities;
import net.fabricmc.bluetreebeasts.entities.custom.ForestFlishEntity;
import net.fabricmc.bluetreebeasts.entities.custom.GreaterGrapplerEntity;
import net.fabricmc.bluetreebeasts.entities.custom.HellBenderEntity;
import net.fabricmc.bluetreebeasts.entities.custom.OceanFlishEntity;
import net.fabricmc.bluetreebeasts.items.ModItems;
import net.fabricmc.bluetreebeasts.recipe.ModRecipes;
import net.fabricmc.bluetreebeasts.screen.ModScreenHandlers;
import net.fabricmc.bluetreebeasts.sounds.ModSounds;
import net.fabricmc.bluetreebeasts.world.gen.ModEntitySpawn;
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
		ModBlockEntities.registerBlockEntities();
		ModRecipes.registerRecipes();
		ModScreenHandlers.registerAllScreenHandlers();
		GeckoLib.initialize();
		ModEntities.registerModEntities();
		ModEntitySpawn.addEntitySpawn();
		FabricDefaultAttributeRegistry.register(ModEntities.HELLBENDER, HellBenderEntity.setAttributes());
		FabricDefaultAttributeRegistry.register(ModEntities.GREATERGRAPPLER, GreaterGrapplerEntity.setAttributes());
		FabricDefaultAttributeRegistry.register(ModEntities.FORESTFLISH, ForestFlishEntity.setAttributes());
		FabricDefaultAttributeRegistry.register(ModEntities.OCEANFLISH, OceanFlishEntity.setAttributes());

	}
}
