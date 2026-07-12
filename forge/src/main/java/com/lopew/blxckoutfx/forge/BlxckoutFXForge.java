package com.lopew.blxckoutfx.forge;

import com.lopew.blxckoutfx.BlxckoutFX;
import com.lopew.blxckoutfx.client.config.BlxckoutFXClientConfig;
import com.lopew.blxckoutfx.client.config.BlxckoutFXConfigScreen;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(BlxckoutFX.MOD_ID)
public final class BlxckoutFXForge {
    public BlxckoutFXForge() {
        BlxckoutFX.init();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);

        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory((minecraft, screen) -> new BlxckoutFXConfigScreen(screen)));
    }

    private void onClientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(BlxckoutFXClientConfig::load);
    }
}
