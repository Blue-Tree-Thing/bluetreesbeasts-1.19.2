package net.fabricmc.bluetreebeasts.entities.client;

import net.fabricmc.bluetreebeasts.BlueTreeBeasts;
import net.fabricmc.bluetreebeasts.entities.custom.WornGloveEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class WornGloveModel extends AnimatedGeoModel<WornGloveEntity>{

        @Override
        public Identifier getModelResource(WornGloveEntity object) {
            return new Identifier(BlueTreeBeasts.MODID, "geo/worn_glove.geo.json");
        }

        @Override
        public Identifier getTextureResource(WornGloveEntity object) {
            return new Identifier(BlueTreeBeasts.MODID, "textures/worn_glove_texture.png");
        }

        @Override
        public Identifier getAnimationResource(WornGloveEntity animatable) {
            return new Identifier(BlueTreeBeasts.MODID, "animations/greater_grappler.animation.json");


        }
    @Override
    public void setLivingAnimations(WornGloveEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("head");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if (head != null) {
            head.setRotationX(extraData.headPitch * ((float) Math.PI/250));
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI/250));
        }
    }
}



