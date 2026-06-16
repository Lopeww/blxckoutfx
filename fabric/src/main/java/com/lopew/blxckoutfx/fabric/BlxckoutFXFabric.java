package com.lopew.blxckoutfx.fabric;

import com.lopew.blxckoutfx.BlxckoutFX;
import com.lopew.blxckoutfx.client.BlackoutFXKeybinds;
import com.lopew.blxckoutfx.client.BlxckoutFXClientConfig;
import com.lopew.blxckoutfx.client.BlxckoutFXGUIHandler;
import com.lopew.blxckoutfx.client.BlxckoutFXShaders;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.CoreShaderRegistrationCallback;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;

public final class BlxckoutFXFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlxckoutFX.init();
        registerClientEvents();

        boolean buttonSystemEnabled = BlxckoutFXClientConfig.load().isInventoryButtonEnabled();
        BlxckoutFX.LOGGER.info("BlxckoutFX Fabric client loaded! Button system active: {}", buttonSystemEnabled ? "YES" : "NO");
    }

    private static void registerClientEvents() {
        KeyBindingHelper.registerKeyBinding(BlackoutFXKeybinds.getOpenConfigKey());
        ClientTickEvents.END_CLIENT_TICK.register(BlackoutFXKeybinds::handleClientTick);

        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            BlxckoutFXGUIHandler.onScreenInit(screen, button -> Screens.getButtons(screen).add(button));

            ScreenMouseEvents.allowMouseClick(screen).register((currentScreen, mouseX, mouseY, button) ->
                    !BlxckoutFXGUIHandler.onMouseClicked(currentScreen, mouseX, mouseY, button)
            );

            ScreenMouseEvents.allowMouseRelease(screen).register((currentScreen, mouseX, mouseY, button) ->
                    !BlxckoutFXGUIHandler.onMouseReleased(currentScreen, mouseX, mouseY, button)
            );
        });

        CoreShaderRegistrationCallback.EVENT.register(context ->
                BlxckoutFXShaders.registerShaders(context::register)
        );
    }
}
