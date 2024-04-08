package net.fabricmc.bluetreebeasts.entities.client;

import net.fabricmc.bluetreebeasts.BlueTreeBeasts;
import net.fabricmc.bluetreebeasts.entities.custom.ForestFlishEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ForestFlishModel extends AnimatedGeoModel<ForestFlishEntity> {


    @Override
    public Identifier getModelResource(ForestFlishEntity object) {
        return  new Identifier(BlueTreeBeasts.MODID, "geo/forest_flish.geo.json");
    }

    @Override
    public Identifier getTextureResource(ForestFlishEntity object) {
        return  new Identifier(BlueTreeBeasts.MODID, "textures/entities/forest_flish_texture.png");
    }

    @Override
    public Identifier getAnimationResource(ForestFlishEntity animatable) {
        return  new Identifier(BlueTreeBeasts.MODID, "animations/forest_flish.animation.json");
    }
}
