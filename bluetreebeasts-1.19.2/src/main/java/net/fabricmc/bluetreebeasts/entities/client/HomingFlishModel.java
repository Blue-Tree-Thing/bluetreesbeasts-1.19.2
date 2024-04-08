package net.fabricmc.bluetreebeasts.entities.client;

import net.fabricmc.bluetreebeasts.BlueTreeBeasts;
import net.fabricmc.bluetreebeasts.entities.custom.HomingFlishEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class HomingFlishModel extends AnimatedGeoModel<HomingFlishEntity> {



    @Override
    public Identifier getModelResource(HomingFlishEntity object) {
        return new Identifier(BlueTreeBeasts.MODID, "geo/tyflew_projectile.geo.json");
    }

    @Override
    public Identifier getTextureResource(HomingFlishEntity object) {
        return new Identifier(BlueTreeBeasts.MODID, "textures/entities/tyflew_projectile_texture.png");
    }

    @Override
    public Identifier getAnimationResource(HomingFlishEntity animatable) {
        return null;
    }
}
