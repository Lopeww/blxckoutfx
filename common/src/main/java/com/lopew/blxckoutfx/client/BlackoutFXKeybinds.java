package com.lopew.blxckoutfx.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

public final class BlackoutFXKeybinds {
    private static final KeyMapping CYCLE_PRESET = new KeyMapping(
            "key.blxckoutfx.cycle_preset",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_X,
            KeyMapping.Category.MISC
    );

    public static KeyMapping getCyclePresetKey() {
        return CYCLE_PRESET;
    }

    public static void handleClientTick(Minecraft minecraft) {
        if (minecraft == null) {
            return;
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
