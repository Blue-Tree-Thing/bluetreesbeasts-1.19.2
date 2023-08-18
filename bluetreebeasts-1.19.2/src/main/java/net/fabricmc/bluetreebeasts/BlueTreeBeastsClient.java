package net.fabricmc.bluetreebeasts;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.bluetreebeasts.block.ModBlocks;
import net.fabricmc.bluetreebeasts.entities.ModEntities;
import net.fabricmc.bluetreebeasts.entities.client.ForestFlishRenderer;
import net.fabricmc.bluetreebeasts.entities.client.GreaterGrapplerRenderer;
import net.fabricmc.bluetreebeasts.entities.client.HellBenderRenderer;
import net.fabricmc.bluetreebeasts.screen.ModScreenHandlers;
import net.fabricmc.bluetreebeasts.screen.QuackStationScreen;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;

import static net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry.register;

public class BlueTreeBeastsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        register(ModEntities.HELLBENDER, HellBenderRenderer::new);
        register(ModEntities.GREATERGRAPPLER, GreaterGrapplerRenderer::new);
        register(ModEntities.FORESTFLISH, ForestFlishRenderer::new);


        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.HEALCUP, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.GASGRASS, RenderLayer.getCutout());
        HandledScreens.register(ModScreenHandlers.QUACK_STATION_SCREEN_HANDLER, QuackStationScreen::new);
    }
}
