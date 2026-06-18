package com.lopew.blxckoutfx.forge;

import com.lopew.blxckoutfx.BlxckoutFX;
import com.lopew.blxckoutfx.client.BlackoutFXKeybinds;
import com.lopew.blxckoutfx.client.BlxckoutFXClientConfig;
import com.lopew.blxckoutfx.client.BlxckoutFXConfigScreen;
import com.lopew.blxckoutfx.client.BlxckoutFXShaders;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.io.IOException;

public final class BlxckoutFXForgeClient {
    public static void init() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(BlxckoutFXForgeClient::registerKeys);
        modBus.addListener(BlxckoutFXForgeClient::registerShaders);

        ModLoadingContext.get().registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory((minecraft, screen) -> new BlxckoutFXConfigScreen(screen))
        );

        boolean buttonSystemEnabled = BlxckoutFXClientConfig.load().isInventoryButtonEnabled();
        BlxckoutFX.LOGGER.info("BlxckoutFX Forge client loaded! Button system active: {}", buttonSystemEnabled ? "YES" : "NO");
    }

    private static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(BlackoutFXKeybinds.getOpenConfigKey());
    }

    private static void registerShaders(RegisterShadersEvent event) {
        try {
            BlxckoutFXShaders.registerShaders((id, vertexFormat, loadCallback) ->
                    event.registerShader(new ShaderInstance(event.getResourceProvider(), id, vertexFormat), loadCallback));
        } catch (IOException exception) {
            BlxckoutFXShaders.disableShaders();
            BlxckoutFX.LOGGER.error("Failed to load BlxckoutFX shaders; disabling screen darkening so the game can continue.", exception);
        }
    }

    private BlxckoutFXForgeClient() {
    }
}
