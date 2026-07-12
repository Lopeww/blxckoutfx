package com.lopew.blxckoutfx.neoforge;

import com.lopew.blxckoutfx.BlxckoutFX;
import com.lopew.blxckoutfx.platform.BlxckoutFXPlatform;
import com.lopew.blxckoutfx.client.config.BlxckoutFXClientConfig;
import com.lopew.blxckoutfx.client.config.BlxckoutFXConfigScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = BlxckoutFX.MOD_ID, dist = Dist.CLIENT)
public final class BlxckoutFXNeoForgeClient {
    public BlxckoutFXNeoForgeClient(IEventBus modBus, ModContainer container) {
        BlxckoutFXPlatform.markNeoForgeRuntime();

        container.registerExtensionPoint(IConfigScreenFactory.class,
                (IConfigScreenFactory) (modContainer, modListScreen) -> new BlxckoutFXConfigScreen(modListScreen));

        boolean buttonSystemEnabled = BlxckoutFXClientConfig.load().isInventoryButtonEnabled();
        BlxckoutFX.LOGGER.info("BlxckoutFX NeoForge client loaded! Button system active: {}", buttonSystemEnabled ? "YES" : "NO");
    }
}
