package net.fabricmc.bluetreebeasts.entities.client;

import net.fabricmc.bluetreebeasts.BlueTreeBeasts;
import net.fabricmc.bluetreebeasts.entities.custom.DesertHopperEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;


public class DesertHopperRenderer extends GeoEntityRenderer<DesertHopperEntity> {

    public DesertHopperRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new DesertHopperModel());
    }

    @Override
    public Identifier getTexture(DesertHopperEntity entity) {
        return new Identifier(BlueTreeBeasts.MODID, "textures/entities/desert_hopper_texture.png");
    }

    @Override
    public void render(DesertHopperEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStack, VertexConsumerProvider renderTypeBuffer, int packedLight) {
        if (isEntityMoving(entity)) {
            simulateJump(entity, matrixStack, partialTicks);
        }

        super.render(entity, entityYaw, partialTicks, matrixStack, renderTypeBuffer, packedLight);
    }

    private boolean isEntityMoving(DesertHopperEntity entity) {
        Vec3d velocity = entity.getVelocity();
        return velocity.x != 0.0 || velocity.z != 0.0; // Check if there is horizontal movement
    }

    private void simulateJump(DesertHopperEntity entity, MatrixStack matrixStack, float partialTicks) {
        float cycleLength = 10.0f; // Cycle every second
        float time = (entity.age + partialTicks) % cycleLength / cycleLength;
        float phase = time * 2.0f * (float)Math.PI; // Complete cycle every second
        float jumpHeight = MathHelper.sin(phase) * 0.2f; // Sine wave for smooth jump, 0.2 blocks high

        matrixStack.translate(0.0, jumpHeight, 0.0); // Apply the translation
    }

    @Override
    public RenderLayer getRenderType(DesertHopperEntity animatable, float partialTicks, MatrixStack stack, VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, Identifier textureLocation) {
        stack.scale(.7f,.7f,.7f);

        if(animatable.isBaby()){
            stack.scale(.4f,.4f,.4f);
        }
        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}
