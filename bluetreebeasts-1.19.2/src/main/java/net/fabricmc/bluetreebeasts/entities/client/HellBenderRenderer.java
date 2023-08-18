package net.fabricmc.bluetreebeasts.entities.client;

import net.fabricmc.bluetreebeasts.BlueTreeBeasts;
import net.fabricmc.bluetreebeasts.entities.custom.HellBenderEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class HellBenderRenderer extends GeoEntityRenderer<HellBenderEntity> {
    public HellBenderRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new HellBenderModel());
    }

    @Override
    public Identifier getTexture(HellBenderEntity entity) {
        return new Identifier(BlueTreeBeasts.MODID, "textures/entities/hell_bender_texture.png");
    }

    @Override
    public boolean hasLabel(HellBenderEntity hellbender) {
        return hellbender.shouldRenderName() && hellbender.hasCustomName();
    }

    @Override
    public RenderLayer getRenderType(HellBenderEntity animatable, float partialTicks, MatrixStack stack, VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, Identifier textureLocation) {
        stack.scale(0.4f,0.4f,0.4f);

        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}
