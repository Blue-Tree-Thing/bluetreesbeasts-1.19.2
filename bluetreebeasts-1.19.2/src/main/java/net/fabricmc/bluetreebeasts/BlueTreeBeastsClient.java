package net.fabricmc.bluetreebeasts;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.bluetreebeasts.entities.ModEntities;
import net.fabricmc.bluetreebeasts.entities.client.*;


import static net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry.register;

public class BlueTreeBeastsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        register(ModEntities.HELLBENDER, HellBenderRenderer::new);
        register(ModEntities.TYFLEW, TyflewRenderer::new);
        register(ModEntities.HOMINGFLISH, HomingFlishRenderer::new);
        register(ModEntities.GREATERGRAPPLER, GreaterGrapplerRenderer::new);
        register(ModEntities.FORESTFLISH, ForestFlishRenderer::new);
        register(ModEntities.OCEANFLISH, OceanFlishRenderer::new);
        register(ModEntities.WOOLLYGIGANTELOPE, WoollyGigantelopeRenderer::new);
        register(ModEntities.CITY_SNIFFLER, CitySnifflerRenderer::new);
    }
}
