package com.lopew.blxckoutfx.fabric;

import com.lopew.blxckoutfx.BlxckoutFX;
import com.lopew.blxckoutfx.platform.BlxckoutFXPlatform;
import com.lopew.blxckoutfx.client.config.BlxckoutFXClientConfig;
import com.lopew.blxckoutfx.client.gui.BlxckoutFXGUIHandler;
import com.lopew.blxckoutfx.client.render.BlxckoutFXShaders;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;

public final class BlxckoutFXFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlxckoutFXPlatform.markFabricRuntime();
        BlxckoutFX.init();
        registerClientEvents();

        boolean buttonSystemEnabled = BlxckoutFXClientConfig.load().isInventoryButtonEnabled();
        BlxckoutFX.LOGGER.info("BlxckoutFX Fabric client loaded! Button system active: {}", buttonSystemEnabled ? "YES" : "NO");
    }

    private static void registerClientEvents() {
        ClientTickEvents.END_CLIENT_TICK.register(BlxckoutFXGUIHandler::handleClientTick);

        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            BlxckoutFXGUIHandler.onScreenInit(screen, button -> Screens.getWidgets(screen).add(button));
            ScreenMouseEvents.allowMouseClick(screen).register((currentScreen, event) ->
                    !BlxckoutFXGUIHandler.onMouseClicked(currentScreen, event.x(), event.y(), event.button())
            );
            ScreenMouseEvents.allowMouseDrag(screen).register((currentScreen, event, dragX, dragY) ->
                    !BlxckoutFXGUIHandler.onMouseDragged(currentScreen, event.x(), event.y(), event.button(), dragX, dragY)
            );
            ScreenMouseEvents.allowMouseRelease(screen).register((currentScreen, event) ->
                    !BlxckoutFXGUIHandler.onMouseReleased(currentScreen, event.x(), event.y(), event.button())
            );
            ScreenEvents.remove(screen).register(BlxckoutFXGUIHandler::onScreenClose);
        });
    }
}
