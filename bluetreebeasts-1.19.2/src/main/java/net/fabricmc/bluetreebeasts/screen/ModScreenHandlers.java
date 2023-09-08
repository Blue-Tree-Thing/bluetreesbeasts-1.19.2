package net.fabricmc.bluetreebeasts.screen;

import net.minecraft.screen.ScreenHandlerType;

public class ModScreenHandlers {

    public static ScreenHandlerType<BeastBuilderScreenHandler> BEAST_BUILDER_SCREEN_HANDLER;

    public static void registerAllScreenHandlers(){
        BEAST_BUILDER_SCREEN_HANDLER = new ScreenHandlerType<>(BeastBuilderScreenHandler::new);
    }
}
