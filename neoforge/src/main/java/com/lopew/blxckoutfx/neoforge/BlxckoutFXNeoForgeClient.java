package com.lopew.blxckoutfx.neoforge;

import com.lopew.blxckoutfx.BlxckoutFX;
import com.lopew.blxckoutfx.client.config.BlxckoutFXClientConfig;
import com.lopew.blxckoutfx.client.config.BlxckoutFXConfigScreen;
import com.lopew.blxckoutfx.client.render.BlxckoutFXShaders;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.gui.screens.Screen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;

import java.io.IOException;
import java.util.function.Supplier;

@Mod(value = BlxckoutFX.MOD_ID, dist = Dist.CLIENT)
public final class BlxckoutFXNeoForgeClient {
    public BlxckoutFXNeoForgeClient(IEventBus modBus, ModContainer container) {
        modBus.addListener(BlxckoutFXNeoForgeClient::registerShaders);
        container.registerExtensionPoint(IConfigScreenFactory.class,
                (Supplier<IConfigScreenFactory>) () -> new IConfigScreenFactory() {
                    @Override
                    public Screen createScreen(ModContainer modContainer, Screen parent) {
                        return new BlxckoutFXConfigScreen(parent);
                    }
                });

        boolean buttonSystemEnabled = BlxckoutFXClientConfig.load().isInventoryButtonEnabled();
        BlxckoutFX.LOGGER.info("BlxckoutFX NeoForge client loaded! Button system active: {}", buttonSystemEnabled ? "YES" : "NO");
    }

    private static void registerShaders(RegisterShadersEvent event) {
        try {
            BlxckoutFXShaders.registerShaders((id, vertexFormat, loadCallback) ->
                    event.registerShader(new ShaderInstance(event.getResourceProvider(), id, vertexFormat), loadCallback));
        } catch (IOException exception) {
            throw new RuntimeException("Failed to load BlxckoutFX shaders", exception);
        }
    }
}
