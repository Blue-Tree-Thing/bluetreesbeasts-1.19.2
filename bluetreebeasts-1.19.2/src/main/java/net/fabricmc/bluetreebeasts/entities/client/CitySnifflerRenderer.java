package net.fabricmc.bluetreebeasts.entities.client;

import net.fabricmc.bluetreebeasts.BlueTreeBeasts;
import net.fabricmc.bluetreebeasts.entities.custom.CitySnifflerEntity;
import net.fabricmc.bluetreebeasts.entities.custom.GreaterGrapplerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class CitySnifflerRenderer extends GeoEntityRenderer<CitySnifflerEntity> {
    public CitySnifflerRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new CitySnifflerModel());
    }

    @Override
    public Identifier getTexture(CitySnifflerEntity entity) {
        return new Identifier(BlueTreeBeasts.MODID, "textures/entities/city_sniffler_texture.png");
    }



    @Override
    public RenderLayer getRenderType(CitySnifflerEntity animatable, float partialTicks, MatrixStack stack, VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, Identifier textureLocation) {
        stack.scale(.5f,.5f,.5f);

        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}
