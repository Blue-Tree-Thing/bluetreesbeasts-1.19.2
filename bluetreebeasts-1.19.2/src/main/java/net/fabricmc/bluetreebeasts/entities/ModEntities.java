package net.fabricmc.bluetreebeasts.entities;

import net.fabricmc.bluetreebeasts.BlueTreeBeasts;
import net.fabricmc.bluetreebeasts.entities.custom.*;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import static net.minecraft.util.registry.Registry.register;
public class ModEntities {



    public static final EntityType<GreaterGrapplerEntity> GREATERGRAPPLER = register(
            Registry.ENTITY_TYPE, new Identifier(BlueTreeBeasts.MODID, "greater_grappler"), FabricEntityTypeBuilder.create
                    (SpawnGroup.MONSTER, GreaterGrapplerEntity::new).dimensions(EntityDimensions.fixed(2f,2f)).build());

    public static final EntityType<HorraneEntity> HORRANE = register(
            Registry.ENTITY_TYPE, new Identifier(BlueTreeBeasts.MODID, "horrane"), FabricEntityTypeBuilder.create
                    (SpawnGroup.CREATURE, HorraneEntity::new).dimensions(EntityDimensions.fixed(1f,1f)).build());

    public static final EntityType<ForestFlishEntity> FORESTFLISH = register(
            Registry.ENTITY_TYPE, new Identifier(BlueTreeBeasts.MODID, "forest_flish"), FabricEntityTypeBuilder.create
                    (SpawnGroup.CREATURE, ForestFlishEntity::new).dimensions(EntityDimensions.fixed(.5f,.5f)).build());

    public static final EntityType<OceanFlishEntity> OCEANFLISH = register(
            Registry.ENTITY_TYPE, new Identifier(BlueTreeBeasts.MODID, "ocean_flish"), FabricEntityTypeBuilder.create
                    (SpawnGroup.CREATURE, OceanFlishEntity::new).dimensions(EntityDimensions.fixed(1f,1f)).build());

    public static final EntityType<GannetWhaleEntity> GANNETWHALE = register(
            Registry.ENTITY_TYPE, new Identifier(BlueTreeBeasts.MODID, "gannet_whale"), FabricEntityTypeBuilder.create
                    (SpawnGroup.CREATURE, GannetWhaleEntity::new).dimensions(EntityDimensions.changing(2f,2f)).build());



    public static final EntityType<HopperShellEntity> HOPPERSHELL = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(BlueTreeBeasts.MODID, "hopper_shell_entity"), FabricEntityTypeBuilder.create(
                    SpawnGroup.MISC, HopperShellEntity::new).dimensions(EntityDimensions.fixed(0.5f, 0.5f)).build());


    public static final EntityType<WoollyGigantelopeEntity> WOOLLYGIGANTELOPE = register(
            Registry.ENTITY_TYPE, new Identifier(BlueTreeBeasts.MODID, "woolly_gigantelope"), FabricEntityTypeBuilder.create
                    (SpawnGroup.CREATURE, WoollyGigantelopeEntity::new).dimensions(EntityDimensions.changing(3f,3f)).build()
    );

    public static final EntityType<CitySnifflerEntity> CITY_SNIFFLER = register(
            Registry.ENTITY_TYPE, new Identifier(BlueTreeBeasts.MODID, "city_sniffler"), FabricEntityTypeBuilder.create
                    (SpawnGroup.CREATURE, CitySnifflerEntity::new).dimensions(EntityDimensions.changing(.5f,.5f)).build()
    );

    public static final EntityType<DesertHopperEntity> DESERT_HOPPER = register(
            Registry.ENTITY_TYPE, new Identifier(BlueTreeBeasts.MODID, "desert_hopper"), FabricEntityTypeBuilder.create
                    (SpawnGroup.CREATURE, DesertHopperEntity::new).dimensions(EntityDimensions.changing(.5f,.5f)).build()
    );


    public static void registerModEntities(){
        BlueTreeBeasts.LOGGER.debug("Registering mod entities for " + BlueTreeBeasts.MODID);
    }
}
