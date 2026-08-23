package com.lopew.blxckoutfx.fabric;

import com.lopew.blxckoutfx.BlxckoutFX;
import com.lopew.blxckoutfx.client.config.BlxckoutFXClientConfig;
import com.lopew.blxckoutfx.client.config.BlxckoutFXConfigScreen;
import com.lopew.blxckoutfx.client.gui.BlxckoutFXGUIHandler;
import com.lopew.blxckoutfx.client.reload.BlxckoutFXReloadService;
import com.lopew.blxckoutfx.client.render.BlxckoutFXShaders;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.CoreShaderRegistrationCallback;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.Minecraft;

public final class BlxckoutFXFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlxckoutFX.init();
        BlxckoutFXClientConfig.load();
        registerClientEvents();
        registerClientCommands();
    }

    private static void registerClientEvents() {
        ClientTickEvents.END_CLIENT_TICK.register(BlxckoutFXGUIHandler::handleClientTick);

        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) ->
                BlxckoutFXGUIHandler.onScreenInit(screen, button -> Screens.getButtons(screen).add(button))
        );
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) ->
                ScreenMouseEvents.allowMouseClick(screen).register((currentScreen, mouseX, mouseY, button) ->
                        !BlxckoutFXGUIHandler.onMouseClicked(currentScreen, mouseX, mouseY, button))
        );
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) ->
                ScreenMouseEvents.allowMouseRelease(screen).register((currentScreen, mouseX, mouseY, button) ->
                        !BlxckoutFXGUIHandler.onMouseReleased(currentScreen, mouseX, mouseY, button))
        );
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) ->
                ScreenEvents.afterRender(screen).register((renderedScreen, drawContext, mouseX, mouseY, tickDelta) ->
                        BlxckoutFXGUIHandler.handleScreenRender(renderedScreen, mouseX, mouseY))
        );
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) ->
                ScreenEvents.remove(screen).register(BlxckoutFXGUIHandler::onScreenClose)
        );

        CoreShaderRegistrationCallback.EVENT.register(context ->
                BlxckoutFXShaders.registerShaders(context::register)
        );
    }

    private static void registerClientCommands() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(
                ClientCommandManager.literal("blxckout")
                        .executes(context -> openConfigScreen())
                        .then(ClientCommandManager.literal("reload")
                                .executes(context -> BlxckoutFXReloadService.reload() ? 1 : 0))
                        .then(ClientCommandManager.literal("reload-resources")
                                .executes(context -> BlxckoutFXReloadService.reloadResources() ? 1 : 0))
                        .then(ClientCommandManager.literal("reload-all")
                                .executes(context -> BlxckoutFXReloadService.reloadAll() ? 1 : 0))
        ));
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(
                ClientCommandManager.literal("blxckoutfx")
                        .executes(context -> openConfigScreen())
                        .then(ClientCommandManager.literal("reload")
                                .executes(context -> BlxckoutFXReloadService.reload() ? 1 : 0))
                        .then(ClientCommandManager.literal("reload-resources")
                                .executes(context -> BlxckoutFXReloadService.reloadResources() ? 1 : 0))
                        .then(ClientCommandManager.literal("reload-all")
                                .executes(context -> BlxckoutFXReloadService.reloadAll() ? 1 : 0))
        ));
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(
                ClientCommandManager.literal("bfx")
                        .executes(context -> openConfigScreen())
                        .then(ClientCommandManager.literal("reload")
                                .executes(context -> BlxckoutFXReloadService.reload() ? 1 : 0))
                        .then(ClientCommandManager.literal("reload-resources")
                                .executes(context -> BlxckoutFXReloadService.reloadResources() ? 1 : 0))
                        .then(ClientCommandManager.literal("reload-all")
                                .executes(context -> BlxckoutFXReloadService.reloadAll() ? 1 : 0))
        ));
    }

    private static int openConfigScreen() {
        Minecraft minecraft = Minecraft.getInstance();
        // Chat closes immediately after command execution, so open on the next client task.
        minecraft.tell(() -> minecraft.setScreen(new BlxckoutFXConfigScreen(minecraft.screen)));
        return 1;
    }
}
