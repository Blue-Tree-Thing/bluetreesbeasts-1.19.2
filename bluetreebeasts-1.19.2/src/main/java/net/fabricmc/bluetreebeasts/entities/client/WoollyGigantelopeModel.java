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
        if(object.isBaby()){
            return  new Identifier(BlueTreeBeasts.MODID, "geo/baby_gigantelope.geo.json");
        }else{
        return  new Identifier(BlueTreeBeasts.MODID, "geo/woolly_gigantelope.geo.json");}
    }

    @Override
    public Identifier getTextureResource(WoollyGigantelopeEntity object) {
        if(object.isBaby()){
            return  new Identifier(BlueTreeBeasts.MODID, "textures/entities/baby_gigantelope_texture.png");
        }else{
        return  new Identifier(BlueTreeBeasts.MODID, "textures/entities/woolly_gigantelope_texture.png");}

    }

    @Override
    public Identifier getAnimationResource(WoollyGigantelopeEntity animatable) {
        if(animatable.isBaby()){
            return new Identifier(BlueTreeBeasts.MODID, "animations/baby_gigantelope.animation.json");
        }else{
        return  new Identifier(BlueTreeBeasts.MODID, "animations/woolly_gigantelope.animation.json");}
    }


    @Override
    public void setLivingAnimations(WoollyGigantelopeEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setCustomAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("head");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if ((head != null)) {

            head.setRotationX(extraData.headPitch * ((float) Math.PI/270));
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI/270));
        }
    }
}
