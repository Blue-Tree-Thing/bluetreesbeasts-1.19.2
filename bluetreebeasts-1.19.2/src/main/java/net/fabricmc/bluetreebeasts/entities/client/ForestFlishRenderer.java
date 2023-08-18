package net.fabricmc.bluetreebeasts.entities.client;

import net.fabricmc.bluetreebeasts.BlueTreeBeasts;
import net.fabricmc.bluetreebeasts.entities.custom.ForestFlishEntity;
import net.fabricmc.bluetreebeasts.entities.custom.GreaterGrapplerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class ForestFlishRenderer extends GeoEntityRenderer<ForestFlishEntity> {

    public ForestFlishRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new ForestFlishModel());
    }

    @Override
    public Identifier getTexture(ForestFlishEntity entity) {
        return new Identifier(BlueTreeBeasts.MODID, "textures/entities/forest_flish_texture.png");
    }

    @Override
    public RenderLayer getRenderType(ForestFlishEntity animatable, float partialTicks, MatrixStack stack, VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, Identifier textureLocation) {
        stack.scale(.6f,.6f,.6f);

        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}
