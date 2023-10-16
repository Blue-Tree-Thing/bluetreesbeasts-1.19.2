package net.fabricmc.bluetreebeasts.entities.client;

import net.fabricmc.bluetreebeasts.BlueTreeBeasts;
import net.fabricmc.bluetreebeasts.entities.custom.HellBenderEntity;
import net.fabricmc.bluetreebeasts.entities.custom.TyflewEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class TyflewRenderer extends GeoEntityRenderer<TyflewEntity> {
    public TyflewRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new TyflewModel());
    }

    @Override
    public Identifier getTexture(TyflewEntity entity) {
        return new Identifier(BlueTreeBeasts.MODID, "textures/entities/tyflew_texture.png");
    }

    @Override
    public boolean hasLabel(TyflewEntity tyflew) {
        return tyflew.shouldRenderName() && tyflew.hasCustomName();
    }

    @Override
    public RenderLayer getRenderType(TyflewEntity animatable, float partialTicks, MatrixStack stack, VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, Identifier textureLocation) {
        stack.scale(1.5f,1.5f,1.5f);

        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}
