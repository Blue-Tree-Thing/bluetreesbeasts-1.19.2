package net.fabricmc.bluetreebeasts.entities.client;

import net.fabricmc.bluetreebeasts.BlueTreeBeasts;
import net.fabricmc.bluetreebeasts.entities.custom.DesertHopperEntity;
import net.fabricmc.bluetreebeasts.entities.custom.ForestFlishEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class DesertHopperModel extends AnimatedGeoModel<DesertHopperEntity> {


    @Override
    public Identifier getModelResource(DesertHopperEntity object) {
        return  new Identifier(BlueTreeBeasts.MODID, "geo/desert_hopper.geo.json");
    }

    @Override
    public Identifier getTextureResource(DesertHopperEntity object) {
        return  new Identifier(BlueTreeBeasts.MODID, "textures/entities/desert_hopper_texture.png");
    }

    @Override
    public Identifier getAnimationResource(DesertHopperEntity animatable) {
        return  new Identifier(BlueTreeBeasts.MODID, "animations/desert_hopper.animation.json");
    }
}
