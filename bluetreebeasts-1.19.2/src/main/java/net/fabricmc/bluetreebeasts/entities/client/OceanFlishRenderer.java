package net.fabricmc.bluetreebeasts.entities.client;

import net.fabricmc.bluetreebeasts.BlueTreeBeasts;
import net.fabricmc.bluetreebeasts.entities.custom.ForestFlishEntity;
import net.fabricmc.bluetreebeasts.entities.custom.OceanFlishEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class OceanFlishRenderer extends GeoEntityRenderer<OceanFlishEntity> {

    public OceanFlishRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new OceanFlishModel());
    }

    @Override
    public Identifier getTexture(OceanFlishEntity entity) {
        return new Identifier(BlueTreeBeasts.MODID, "textures/entities/ocean_flish_texture.png");
    }

    @Override
    public RenderLayer getRenderType(OceanFlishEntity animatable, float partialTicks, MatrixStack stack, VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, Identifier textureLocation) {
        stack.scale(1f,1f,1f);

        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}
