package com.lopew.blxckoutfx.client;

import com.lopew.blxckoutfx.BlxckoutFX;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public final class BlackoutFXKeybinds {
    private static final KeyMapping.Category BLXCKOUTFX_CATEGORY = KeyMapping.Category.register(
            Identifier.fromNamespaceAndPath(BlxckoutFX.MOD_ID, BlxckoutFX.MOD_ID)
    );

    private static final KeyMapping CYCLE_PRESET = new KeyMapping(
            "key.blxckoutfx.cycle_preset",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_X,
            BLXCKOUTFX_CATEGORY
    );

    private static final KeyMapping OPEN_CONFIG = new KeyMapping(
            "key.blxckoutfx.open_config",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_O,
            BLXCKOUTFX_CATEGORY
    );

    public static KeyMapping getCyclePresetKey() {
        return CYCLE_PRESET;
    }

    public static KeyMapping getOpenConfigKey() {
        return OPEN_CONFIG;
    }

    public static void handleClientTick(Minecraft minecraft) {
        if (minecraft == null) {
            return;
        }

        while (OPEN_CONFIG.consumeClick()) {
            if (!(minecraft.screen instanceof BlxckoutFXConfigScreen)) {
                minecraft.setScreen(new BlxckoutFXConfigScreen(minecraft.screen));
            }
        }

        while (CYCLE_PRESET.consumeClick()) {
            BlxckoutFXShaders.cyclePreset();

            if (minecraft.player != null) {
                minecraft.player.sendOverlayMessage(
                        Component.empty()
                                .append("BlxckoutFX: ")
                                .append(BlxckoutFXShaders.getCurrentPresetComponent())
                );
            }
        }
    }

    private BlackoutFXKeybinds() {
    }
}
