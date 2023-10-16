package net.fabricmc.bluetreebeasts.entities.client;

import net.fabricmc.bluetreebeasts.BlueTreeBeasts;
import net.fabricmc.bluetreebeasts.entities.custom.HellBenderEntity;
import net.fabricmc.bluetreebeasts.entities.custom.TyflewEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class TyflewModel extends AnimatedGeoModel<TyflewEntity>{

        @Override
        public Identifier getModelResource(TyflewEntity object) {
            return new Identifier(BlueTreeBeasts.MODID, "geo/tyflew.geo.json");
        }

        @Override
        public Identifier getTextureResource(TyflewEntity object) {
            return new Identifier(BlueTreeBeasts.MODID, "textures/tyflew_texture.png");
        }

        @Override
        public Identifier getAnimationResource(TyflewEntity animatable) {
            return new Identifier(BlueTreeBeasts.MODID, "animations/tyflew.animation.json");
        }


    }

