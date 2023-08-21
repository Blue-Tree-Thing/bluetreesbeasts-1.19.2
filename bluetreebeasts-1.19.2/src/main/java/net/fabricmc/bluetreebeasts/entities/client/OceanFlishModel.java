package net.fabricmc.bluetreebeasts.entities.client;

import net.fabricmc.bluetreebeasts.BlueTreeBeasts;
import net.fabricmc.bluetreebeasts.entities.custom.OceanFlishEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class OceanFlishModel extends AnimatedGeoModel<OceanFlishEntity> {


    @Override
    public Identifier getModelResource(OceanFlishEntity object) {
        return  new Identifier(BlueTreeBeasts.MODID, "geo/ocean_flish.geo.json");
    }

    @Override
    public Identifier getTextureResource(OceanFlishEntity object) {
        return  new Identifier(BlueTreeBeasts.MODID, "textures/ocean_flish_texture.png");
    }

    @Override
    public Identifier getAnimationResource(OceanFlishEntity animatable) {
        return  new Identifier(BlueTreeBeasts.MODID, "animations/ocean_flish.animation.json");
    }
}
