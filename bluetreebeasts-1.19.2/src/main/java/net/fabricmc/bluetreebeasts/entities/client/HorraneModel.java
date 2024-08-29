package net.fabricmc.bluetreebeasts.entities.client;

import net.fabricmc.bluetreebeasts.BlueTreeBeasts;
import net.fabricmc.bluetreebeasts.entities.custom.HorraneEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class HorraneModel extends AnimatedGeoModel<HorraneEntity>{

        @Override
        public Identifier getModelResource(HorraneEntity object) {
            return new Identifier(BlueTreeBeasts.MODID, "geo/horrane.geo.json");
        }

        @Override
        public Identifier getTextureResource(HorraneEntity object) {
            return new Identifier(BlueTreeBeasts.MODID, "textures/entities/horrane_texture.png");
        }

        @Override
        public Identifier getAnimationResource(HorraneEntity animatable) {
            return new Identifier(BlueTreeBeasts.MODID, "animations/horrane.animation.json");
        }

    @Override
    public void setLivingAnimations(HorraneEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setCustomAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("head");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if (head != null) {
            head.setRotationX(extraData.headPitch * ((float) Math.PI/270));
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI/270));
        }
    }
}



