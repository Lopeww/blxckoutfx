package com.lopew.blxckoutfx.fabric;

import com.lopew.blxckoutfx.BlxckoutFX;
import com.lopew.blxckoutfx.client.config.BlxckoutFXClientConfig;
import com.lopew.blxckoutfx.client.gui.BlxckoutFXGUIHandler;
import com.lopew.blxckoutfx.client.render.BlxckoutFXShaders;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.CoreShaderRegistrationCallback;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import org.lwjgl.glfw.GLFW;

public final class BlxckoutFXFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlxckoutFX.init();
        registerClientEvents();

        boolean buttonSystemEnabled = BlxckoutFXClientConfig.load().isInventoryButtonEnabled();
        BlxckoutFX.LOGGER.info("BlxckoutFX Fabric client loaded! Button system active: {}", buttonSystemEnabled ? "YES" : "NO");
    }

    private static void registerClientEvents() {
        ClientTickEvents.END_CLIENT_TICK.register(BlxckoutFXGUIHandler::handleClientTick);

        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) ->
                BlxckoutFXGUIHandler.onScreenInit(screen, button -> Screens.getButtons(screen).add(button))
        );
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) ->
                ScreenMouseEvents.allowMouseClick(screen).register((currentScreen, mouseX, mouseY, button) ->
                        button == GLFW.GLFW_MOUSE_BUTTON_1
                                || !BlxckoutFXGUIHandler.onMouseClicked(currentScreen, mouseX, mouseY, button))
        );
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) ->
                ScreenEvents.beforeRender(screen).register((renderedScreen, drawContext, mouseX, mouseY, tickDelta) ->
                        BlxckoutFXGUIHandler.handleScreenRender(renderedScreen, mouseX, mouseY))
        );
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) ->
                ScreenEvents.remove(screen).register(BlxckoutFXGUIHandler::onScreenClose)
        );

        CoreShaderRegistrationCallback.EVENT.register(context ->
                BlxckoutFXShaders.registerShaders(context::register)
        );
    }
}
