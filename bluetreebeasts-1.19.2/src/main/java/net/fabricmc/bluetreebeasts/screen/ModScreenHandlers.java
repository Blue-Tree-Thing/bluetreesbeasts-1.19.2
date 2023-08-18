package net.fabricmc.bluetreebeasts.screen;

import net.minecraft.screen.ScreenHandlerType;

public class ModScreenHandlers {

    public static ScreenHandlerType<QuackStationScreenHandler> QUACK_STATION_SCREEN_HANDLER;

    public static void registerAllScreenHandlers(){
        QUACK_STATION_SCREEN_HANDLER = new ScreenHandlerType<>(QuackStationScreenHandler::new);
    }
}
