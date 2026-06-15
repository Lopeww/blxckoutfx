package com.lopew.blxckoutfx.client;

import com.lopew.blxckoutfx.BlxckoutFX;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public final class BlackoutFXKeybinds {
    private static final KeyMapping.Category BLXCKOUTFX_CATEGORY = KeyMapping.Category.register(
            Identifier.fromNamespaceAndPath(BlxckoutFX.MOD_ID, BlxckoutFX.MOD_ID)
    );

    private static final KeyMapping OPEN_CONFIG = new KeyMapping(
            "key.blxckoutfx.open_config",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_O,
            BLXCKOUTFX_CATEGORY
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
            if (!(minecraft.screen instanceof BlxckoutFXConfigScreen)) {
                minecraft.setScreen(new BlxckoutFXConfigScreen(minecraft.screen));
            }
        }
    }

    private BlackoutFXKeybinds() {
    }
}
