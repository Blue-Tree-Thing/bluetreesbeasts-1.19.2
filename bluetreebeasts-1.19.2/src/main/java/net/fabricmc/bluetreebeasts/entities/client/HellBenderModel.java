package net.fabricmc.bluetreebeasts.entities.client;

import net.fabricmc.bluetreebeasts.BlueTreeBeasts;
import net.fabricmc.bluetreebeasts.entities.custom.HellBenderEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class HellBenderModel extends AnimatedGeoModel<HellBenderEntity>{

        @Override
        public Identifier getModelResource(HellBenderEntity object) {
            return new Identifier(BlueTreeBeasts.MODID, "geo/hell_bender.geo.json");
        }

        @Override
        public Identifier getTextureResource(HellBenderEntity object) {
            return new Identifier(BlueTreeBeasts.MODID, "textures/hell_bender_texture.png");
        }

        @Override
        public Identifier getAnimationResource(HellBenderEntity animatable) {
            return new Identifier(BlueTreeBeasts.MODID, "animations/hell_bender.animation.json");


        }


    }

