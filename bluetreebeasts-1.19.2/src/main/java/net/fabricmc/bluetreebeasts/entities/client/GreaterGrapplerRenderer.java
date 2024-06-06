package net.fabricmc.bluetreebeasts.entities.client;

import net.fabricmc.bluetreebeasts.BlueTreeBeasts;
import net.fabricmc.bluetreebeasts.entities.custom.GreaterGrapplerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class GreaterGrapplerRenderer extends GeoEntityRenderer<GreaterGrapplerEntity> {
    public GreaterGrapplerRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new GreaterGrapplerModel());
    }

    @Override
    public Identifier getTexture(GreaterGrapplerEntity entity) {
        return new Identifier(BlueTreeBeasts.MODID, "textures/entities/greater_grappler_texture.png");
    }



    @Override
    public RenderLayer getRenderType(GreaterGrapplerEntity animatable, float partialTicks, MatrixStack stack, VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, Identifier textureLocation) {
        stack.scale(1f,1f,1f);

        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}
