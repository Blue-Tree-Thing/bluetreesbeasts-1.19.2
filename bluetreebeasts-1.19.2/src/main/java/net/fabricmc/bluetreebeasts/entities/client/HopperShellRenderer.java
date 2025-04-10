package net.fabricmc.bluetreebeasts.entities.client;

import net.fabricmc.bluetreebeasts.BlueTreeBeasts;
import net.fabricmc.bluetreebeasts.entities.custom.HopperShellEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

public class HopperShellRenderer extends GeoProjectilesRenderer<HopperShellEntity> {

    public HopperShellRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new HopperShellModel());
    }

    @Override
    public Identifier getTexture(HopperShellEntity entity) {
        return new Identifier(BlueTreeBeasts.MODID, "textures/entities/hopper_shell_projectile_texture.png");
    }

    @Override
    public RenderLayer getRenderType(HopperShellEntity animatable, float partialTicks, MatrixStack stack, VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, Identifier textureLocation) {
        stack.scale(1f,1f,1f);

        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}
