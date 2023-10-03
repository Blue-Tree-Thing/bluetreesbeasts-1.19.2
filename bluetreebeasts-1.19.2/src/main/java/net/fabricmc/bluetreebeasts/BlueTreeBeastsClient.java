package net.fabricmc.bluetreebeasts;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.bluetreebeasts.entities.ModEntities;
import net.fabricmc.bluetreebeasts.entities.client.*;
import net.fabricmc.bluetreebeasts.screen.ModScreenHandlers;
import net.fabricmc.bluetreebeasts.screen.BeastBuilderScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;


import static net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry.register;

public class BlueTreeBeastsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        register(ModEntities.HELLBENDER, HellBenderRenderer::new);
        register(ModEntities.GREATERGRAPPLER, GreaterGrapplerRenderer::new);
        register(ModEntities.WORNGLOVE, WornGloveRenderer::new);
        register(ModEntities.FORESTFLISH, ForestFlishRenderer::new);
        register(ModEntities.OCEANFLISH, OceanFlishRenderer::new);
        register(ModEntities.WOOLLYGIGANTELOPE, WoollyGigantelopeRenderer::new);



        HandledScreens.register(ModScreenHandlers.BEAST_BUILDER_SCREEN_HANDLER, BeastBuilderScreen::new);
    }
}
