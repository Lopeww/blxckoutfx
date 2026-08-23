package com.lopew.blxckoutfx.client.command;

import com.lopew.blxckoutfx.client.config.BlxckoutFXConfigScreen;
import com.lopew.blxckoutfx.client.reload.BlxckoutFXReloadService;
import net.minecraft.client.Minecraft;

/** Shared command actions; loaders only supply their client command builders. */
public final class BlxckoutFXDeveloperCommands {
    public static int openConfigScreen() {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.tell(() -> minecraft.setScreen(new BlxckoutFXConfigScreen(minecraft.screen)));
        return 1;
    }

    public static int reload() {
        return BlxckoutFXReloadService.reload() ? 1 : 0;
    }

    public static int reloadResources() {
        return BlxckoutFXReloadService.reloadResources() ? 1 : 0;
    }

    public static int reloadAll() {
        return BlxckoutFXReloadService.reloadAll() ? 1 : 0;
    }

    private BlxckoutFXDeveloperCommands() {
    }
}
