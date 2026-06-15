package com.lopew.blxckoutfx.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

public final class BlackoutFXKeybinds {
    private static final KeyMapping OPEN_CONFIG = new KeyMapping(
            "key.blxckoutfx.open_config",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_O,
            "key.categories.blxckoutfx"
    );

    public static KeyMapping getOpenConfigKey() {
        return OPEN_CONFIG;
    }

    public static void handleClientTick(Minecraft minecraft) {
        if (minecraft == null) {
            return;
        }

        BlxckoutFXGUIHandler.handleClientTick(minecraft);

        while (OPEN_CONFIG.consumeClick()) {
            minecraft.setScreen(new BlxckoutFXConfigScreen(minecraft.screen));
        }
    }

    private BlackoutFXKeybinds() {
    }
}
