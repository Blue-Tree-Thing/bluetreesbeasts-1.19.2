package net.fabricmc.bluetreebeasts.items.client;

import net.fabricmc.bluetreebeasts.BlueTreeBeasts;
import net.fabricmc.bluetreebeasts.items.custom.FlutterPackItem;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;



public class FlutterPackModel extends AnimatedGeoModel<FlutterPackItem> {

    @Override
    public Identifier getModelResource(FlutterPackItem object) {
        return new Identifier(BlueTreeBeasts.MODID, "geo/flutter_pack.geo.json");
    }

    @Override
    public Identifier getTextureResource(FlutterPackItem object) {
        return new Identifier(BlueTreeBeasts.MODID, "textures/item/flutter_pack_texture.png");
    }

    @Override
    public Identifier getAnimationResource(FlutterPackItem animatable) {
        return new Identifier(BlueTreeBeasts.MODID, "animations/flutter_pack.animation.json");
    }
}
