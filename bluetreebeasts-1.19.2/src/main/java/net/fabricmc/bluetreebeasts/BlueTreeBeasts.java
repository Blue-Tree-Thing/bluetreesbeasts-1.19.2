package net.fabricmc.bluetreebeasts;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.bluetreebeasts.block.ModBlockEntities;
import net.fabricmc.bluetreebeasts.block.Modblocks;
import net.fabricmc.bluetreebeasts.effect.ModEffects;
import net.fabricmc.bluetreebeasts.entities.ModEntities;
import net.fabricmc.bluetreebeasts.entities.custom.*;
import net.fabricmc.bluetreebeasts.items.ModItems;
import net.fabricmc.bluetreebeasts.sounds.ModSounds;
import net.fabricmc.bluetreebeasts.world.gen.*;

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
		ModBlockEntities.registerAllBlockEntities();
		Modblocks.registerModBlocks();
		ModSounds.registerModSounds();
		ModEffects.registerEffects();
		GeckoLib.initialize();
		ModEntities.registerModEntities();

		ForestFlishEntitySpawn.addForestFlishEntitySpawn();
		OceanFlishEntitySpawn.addOceanFlishEntitySpawn();
		WoollyGigantelopeEntitySpawn.addWoollyGigantelopeEntitySpawn();
		DesertHopperEntitySpawn.addDesertHopperEntitySpawn();
		HorraneEntitySpawn.addHorraneEntitySpawn();
		FabricDefaultAttributeRegistry.register(ModEntities.HELLBENDER, HellBenderEntity.setAttributes());
		FabricDefaultAttributeRegistry.register(ModEntities.TYFLEW, TyflewEntity.setAttributes());
		FabricDefaultAttributeRegistry.register(ModEntities.HOMINGFLISH, HomingFlishEntity.createHomingFlishAttributes());
		FabricDefaultAttributeRegistry.register(ModEntities.GREATERGRAPPLER, GreaterGrapplerEntity.setAttributes());
		FabricDefaultAttributeRegistry.register(ModEntities.FORESTFLISH, ForestFlishEntity.setAttributes());
		FabricDefaultAttributeRegistry.register(ModEntities.DESERT_HOPPER, DesertHopperEntity.setAttributes());
		FabricDefaultAttributeRegistry.register(ModEntities.OCEANFLISH, OceanFlishEntity.setAttributes());
		FabricDefaultAttributeRegistry.register(ModEntities.WOOLLYGIGANTELOPE, WoollyGigantelopeEntity.setAttributes());
		FabricDefaultAttributeRegistry.register(ModEntities.CITY_SNIFFLER, CitySnifflerEntity.setAttributes());
		FabricDefaultAttributeRegistry.register(ModEntities.GANNETWHALE, GannetWhaleEntity.setAttributes());
		FabricDefaultAttributeRegistry.register(ModEntities.HORRANE, HorraneEntity.setAttributes());

	}
}
