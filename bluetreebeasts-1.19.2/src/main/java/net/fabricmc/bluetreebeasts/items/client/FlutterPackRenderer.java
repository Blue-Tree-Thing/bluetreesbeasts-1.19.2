package net.fabricmc.bluetreebeasts.items.client;


import net.fabricmc.bluetreebeasts.BlueTreeBeasts;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;
import net.fabricmc.bluetreebeasts.items.custom.FlutterPackItem;

import java.util.ArrayList;
import java.util.List;


public class FlutterPackRenderer extends GeoArmorRenderer<FlutterPackItem> {
    public FlutterPackRenderer() {
        super(new FlutterPackModel());
        this.headBone = null; // No head part, set to null or simply remove this line if not used at all
        this.bodyBone = "all";
        this.leftArmBone = null;
        this.rightArmBone = null;
        this.leftLegBone = null;
        this.rightLegBone = null;
        this.leftBootBone = null;
        this.rightBootBone = null;
    }
    @Override
    public Identifier getTextureLocation(FlutterPackItem object) {
        return new Identifier(BlueTreeBeasts.MODID, "textures/item/flutter_pack_texture.png");
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, ItemStack stack, LivingEntity entity,
                       EquipmentSlot slot, int light, BipedEntityModel<LivingEntity> contextModel) {
        if (stack == null || stack.isEmpty() || !(stack.getItem() instanceof FlutterPackItem)) {
            System.out.println("Attempted to render with an invalid or null ItemStack.");
            return; // Avoid proceeding with null or incorrect item
        }

        super.render(matrices, vertexConsumers, stack, entity, slot, light, contextModel);
    }
}