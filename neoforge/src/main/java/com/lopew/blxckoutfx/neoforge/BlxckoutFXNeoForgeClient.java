package com.lopew.blxckoutfx.neoforge;

import com.lopew.blxckoutfx.BlxckoutFX;
import com.lopew.blxckoutfx.client.BlackoutFXKeybinds;
import com.lopew.blxckoutfx.client.BlxckoutFXClientConfig;
import com.lopew.blxckoutfx.client.BlxckoutFXConfigScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

@Mod(value = BlxckoutFX.MOD_ID, dist = Dist.CLIENT)
public final class BlxckoutFXNeoForgeClient {
    public BlxckoutFXNeoForgeClient(IEventBus modBus, ModContainer container) {
        modBus.addListener(BlxckoutFXNeoForgeClient::registerKeys);

        container.registerExtensionPoint(IConfigScreenFactory.class,
                (IConfigScreenFactory) (modContainer, modListScreen) -> new BlxckoutFXConfigScreen(modListScreen));

        boolean buttonSystemEnabled = BlxckoutFXClientConfig.load().isInventoryButtonEnabled();
        BlxckoutFX.LOGGER.info("BlxckoutFX NeoForge client loaded! Button system active: {}", buttonSystemEnabled ? "YES" : "NO");
    }

    private static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(BlackoutFXKeybinds.getCyclePresetKey());
        event.register(BlackoutFXKeybinds.getOpenConfigKey());
    }
}
