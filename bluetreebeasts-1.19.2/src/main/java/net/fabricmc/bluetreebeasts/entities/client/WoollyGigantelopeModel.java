package net.fabricmc.bluetreebeasts.entities.client;

import net.fabricmc.bluetreebeasts.BlueTreeBeasts;
import net.fabricmc.bluetreebeasts.entities.custom.GreaterGrapplerEntity;
import net.fabricmc.bluetreebeasts.entities.custom.WoollyGigantelopeEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class WoollyGigantelopeModel extends AnimatedGeoModel<WoollyGigantelopeEntity> {


    @Override
    public Identifier getModelResource(WoollyGigantelopeEntity object) {
        return  new Identifier(BlueTreeBeasts.MODID, "geo/woolly_gigantelope.geo.json");
    }

    @Override
    public Identifier getTextureResource(WoollyGigantelopeEntity object) {
        return  new Identifier(BlueTreeBeasts.MODID, "textures/woolly_gigantelope_texture.png");
    }

    @Override
    public Identifier getAnimationResource(WoollyGigantelopeEntity animatable) {
        return  new Identifier(BlueTreeBeasts.MODID, "animations/woolly_gigantelope.animation.json");
    }


    @Override
    public void setLivingAnimations(WoollyGigantelopeEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("head");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if ((head != null)) {

            head.setRotationX(extraData.headPitch * ((float) Math.PI/250));
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI/250));
        }
    }
}
