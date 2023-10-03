package net.fabricmc.bluetreebeasts.entities.client;

import net.fabricmc.bluetreebeasts.BlueTreeBeasts;
import net.fabricmc.bluetreebeasts.entities.custom.WornGloveEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class WornGloveRenderer extends GeoEntityRenderer<WornGloveEntity> {
    public WornGloveRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new WornGloveModel());
    }

    @Override
    public Identifier getTexture(WornGloveEntity entity) {
        return new Identifier(BlueTreeBeasts.MODID, "textures/entities/worn_glove_texture.png");
    }



    @Override
    public RenderLayer getRenderType(WornGloveEntity animatable, float partialTicks, MatrixStack stack, VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, Identifier textureLocation) {
        stack.scale(1.5f,1.5f,1.5f);

        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}
