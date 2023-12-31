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

    public static final EntityType<HellBenderEntity> HELLBENDER = register(
            Registry.ENTITY_TYPE, new Identifier(BlueTreeBeasts.MODID, "hell_bender"), FabricEntityTypeBuilder.create
                    (SpawnGroup.MONSTER, HellBenderEntity::new).dimensions(EntityDimensions.fixed(3f,1.5f)).fireImmune().build());

    public static final EntityType<TyflewEntity> TYFLEW = register(
            Registry.ENTITY_TYPE, new Identifier(BlueTreeBeasts.MODID, "tyflew"), FabricEntityTypeBuilder.create
                    (SpawnGroup.MONSTER, TyflewEntity::new).dimensions(EntityDimensions.fixed(3f,1.5f)).fireImmune().build());


    public static final EntityType<GreaterGrapplerEntity> GREATERGRAPPLER = register(
            Registry.ENTITY_TYPE, new Identifier(BlueTreeBeasts.MODID, "greater_grappler"), FabricEntityTypeBuilder.create
                    (SpawnGroup.MONSTER, GreaterGrapplerEntity::new).dimensions(EntityDimensions.fixed(2f,2f)).build());

    public static final EntityType<ForestFlishEntity> FORESTFLISH = register(
            Registry.ENTITY_TYPE, new Identifier(BlueTreeBeasts.MODID, "forest_flish"), FabricEntityTypeBuilder.create
                    (SpawnGroup.CREATURE, ForestFlishEntity::new).dimensions(EntityDimensions.fixed(.5f,.5f)).build());

    public static final EntityType<OceanFlishEntity> OCEANFLISH = register(
            Registry.ENTITY_TYPE, new Identifier(BlueTreeBeasts.MODID, "ocean_flish"), FabricEntityTypeBuilder.create
                    (SpawnGroup.CREATURE, OceanFlishEntity::new).dimensions(EntityDimensions.fixed(1f,1f)).build());

    public static final EntityType<WoollyGigantelopeEntity> WOOLLYGIGANTELOPE = register(
            Registry.ENTITY_TYPE, new Identifier(BlueTreeBeasts.MODID, "woolly_gigantelope"), FabricEntityTypeBuilder.create
                    (SpawnGroup.CREATURE, WoollyGigantelopeEntity::new).dimensions(EntityDimensions.changing(3f,3f)).build()
    );


    public static void registerModEntities(){
        BlueTreeBeasts.LOGGER.debug("Registering mod entities for " + BlueTreeBeasts.MODID);
    }
}
