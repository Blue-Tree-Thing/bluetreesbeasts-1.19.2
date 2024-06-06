package net.fabricmc.bluetreebeasts.entities.client;

import net.fabricmc.bluetreebeasts.BlueTreeBeasts;
import net.fabricmc.bluetreebeasts.entities.custom.GannetWhaleEntity;
import net.fabricmc.bluetreebeasts.entities.custom.WoollyGigantelopeEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class GannetWhaleRenderer extends GeoEntityRenderer<GannetWhaleEntity> {
    public GannetWhaleRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new GannetWhaleModel());
    }

    @Override
    public Identifier getTexture(GannetWhaleEntity entity) {
        return new Identifier(BlueTreeBeasts.MODID, "textures/entities/gannet_whale_texture.png");
    }


    @Override
    public RenderLayer getRenderType(GannetWhaleEntity animatable, float partialTicks, MatrixStack stack, VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, Identifier textureLocation) {
        stack.scale(1f,1f,1f);

        if(animatable.isBaby()){
            stack.scale(.25f,.25f,.25f);
        }

        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}
