package net.fabricmc.bluetreebeasts.entities.client;

import net.fabricmc.bluetreebeasts.BlueTreeBeasts;
import net.fabricmc.bluetreebeasts.entities.custom.HopperShellEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class HopperShellModel extends AnimatedGeoModel<HopperShellEntity> {



    @Override
    public Identifier getModelResource(HopperShellEntity object) {
        return new Identifier(BlueTreeBeasts.MODID, "geo/hopper_shell_projectile.geo.json");
    }

    @Override
    public Identifier getTextureResource(HopperShellEntity object) {
        return new Identifier(BlueTreeBeasts.MODID, "textures/entities/hopper_shell_projectile_texture.png");
    }

    @Override
    public Identifier getAnimationResource(HopperShellEntity animatable) {
        return new Identifier(BlueTreeBeasts.MODID, "animations/hopper_shell_projectile.animation.json");
    }
}
