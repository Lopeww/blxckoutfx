package com.lopew.blxckoutfx.fabric;

import com.lopew.blxckoutfx.client.config.BlxckoutFXConfigScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.screens.Screen;

public final class BlxckoutFXModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<Screen> getModConfigScreenFactory() {
        return BlxckoutFXConfigScreen::new;
    }
}
