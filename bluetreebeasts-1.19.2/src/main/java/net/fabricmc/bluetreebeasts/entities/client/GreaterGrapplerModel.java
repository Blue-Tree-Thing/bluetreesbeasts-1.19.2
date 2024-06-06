package net.fabricmc.bluetreebeasts.entities.client;

import net.fabricmc.bluetreebeasts.BlueTreeBeasts;
import net.fabricmc.bluetreebeasts.entities.custom.GreaterGrapplerEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class GreaterGrapplerModel extends AnimatedGeoModel<GreaterGrapplerEntity>{

        @Override
        public Identifier getModelResource(GreaterGrapplerEntity object) {
            return new Identifier(BlueTreeBeasts.MODID, "geo/greater_grappler.geo.json");
        }

        @Override
        public Identifier getTextureResource(GreaterGrapplerEntity object) {
            return new Identifier(BlueTreeBeasts.MODID, "textures/entities/greater_grappler_texture.png");
        }

        @Override
        public Identifier getAnimationResource(GreaterGrapplerEntity animatable) {
            return new Identifier(BlueTreeBeasts.MODID, "animations/greater_grappler.animation.json");
        }

    @Override
    public void setLivingAnimations(GreaterGrapplerEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setCustomAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("head");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if (head != null) {
            head.setRotationX(extraData.headPitch * ((float) Math.PI/270));
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI/270));
        }
    }
}



