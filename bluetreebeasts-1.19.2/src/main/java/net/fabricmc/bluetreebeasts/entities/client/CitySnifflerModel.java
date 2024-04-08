package net.fabricmc.bluetreebeasts.entities.client;

import net.fabricmc.bluetreebeasts.BlueTreeBeasts;
import net.fabricmc.bluetreebeasts.entities.custom.CitySnifflerEntity;
import net.fabricmc.bluetreebeasts.entities.custom.GreaterGrapplerEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class CitySnifflerModel extends AnimatedGeoModel<CitySnifflerEntity>{

        @Override
        public Identifier getModelResource(CitySnifflerEntity object) {
            return new Identifier(BlueTreeBeasts.MODID, "geo/city_sniffler.geo.json");
        }

        @Override
        public Identifier getTextureResource(CitySnifflerEntity object) {
            return new Identifier(BlueTreeBeasts.MODID, "textures/entities/city_sniffler_texture.png");
        }

        @Override
        public Identifier getAnimationResource(CitySnifflerEntity animatable) {
            return new Identifier(BlueTreeBeasts.MODID, "animations/city_sniffler.animation.json");


        }

    @Override
    public void setLivingAnimations(CitySnifflerEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setCustomAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("head");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if (head != null) {
            head.setRotationX(extraData.headPitch * ((float) Math.PI/290));
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI/290));
        }
    }

}



