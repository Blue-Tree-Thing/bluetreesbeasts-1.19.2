package net.fabricmc.bluetreebeasts;

import net.fabricmc.api.ClientModInitializer;

import net.fabricmc.bluetreebeasts.block.Modblocks;
import net.fabricmc.bluetreebeasts.entities.ModEntities;
import net.fabricmc.bluetreebeasts.entities.client.*;
import net.fabricmc.bluetreebeasts.items.ModItems;
import net.fabricmc.bluetreebeasts.items.client.FlutterPackRenderer;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;


import static net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry.register;

public class BlueTreeBeastsClient implements ClientModInitializer  {


    @Override
    public void onInitializeClient() {
        GeoArmorRenderer.registerArmorRenderer(new FlutterPackRenderer(), ModItems.FLUTTERPACK);
        BlockRenderLayerMap.INSTANCE.putBlock(Modblocks.WREATH_BLOCK, RenderLayer.getCutout());

        register(ModEntities.HELLBENDER, HellBenderRenderer::new);
        register(ModEntities.TYFLEW, TyflewRenderer::new);
        register(ModEntities.HOMINGFLISH, HomingFlishRenderer::new);
        register(ModEntities.GREATERGRAPPLER, GreaterGrapplerRenderer::new);
        register(ModEntities.FORESTFLISH, ForestFlishRenderer::new);
        register(ModEntities.OCEANFLISH, OceanFlishRenderer::new);
        register(ModEntities.WOOLLYGIGANTELOPE, WoollyGigantelopeRenderer::new);
        register(ModEntities.CITY_SNIFFLER, CitySnifflerRenderer::new);
        register(ModEntities.GANNETWHALE, GannetWhaleRenderer::new);
        register(ModEntities.DESERT_HOPPER, DesertHopperRenderer::new);
        register(ModEntities.HORRANE, HorraneRenderer::new);
    }}
