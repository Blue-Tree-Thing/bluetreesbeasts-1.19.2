package net.fabricmc.bluetreebeasts.entities.client;

import net.fabricmc.bluetreebeasts.BlueTreeBeasts;
import net.fabricmc.bluetreebeasts.entities.custom.GannetWhaleEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class GannetWhaleModel extends AnimatedGeoModel<GannetWhaleEntity>{

        @Override
        public Identifier getModelResource(GannetWhaleEntity object) {
            return new Identifier(BlueTreeBeasts.MODID, "geo/gannet_whale.geo.json");
        }

        @Override
        public Identifier getTextureResource(GannetWhaleEntity object) {
            return new Identifier(BlueTreeBeasts.MODID, "textures/entities/gannet_whale_texture.png");
        }

        @Override
        public Identifier getAnimationResource(GannetWhaleEntity animatable) {
            return new Identifier(BlueTreeBeasts.MODID, "animations/gannet_whale.animation.json");


        }

    @Override
    public void setLivingAnimations(GannetWhaleEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setCustomAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("head");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if (head != null) {
            head.setRotationX(extraData.headPitch * ((float) Math.PI/290));
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI/290));
        }
    }

}



