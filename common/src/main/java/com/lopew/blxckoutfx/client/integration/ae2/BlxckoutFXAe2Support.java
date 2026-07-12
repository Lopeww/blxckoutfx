package com.lopew.blxckoutfx.client.integration.ae2;

import com.lopew.blxckoutfx.client.config.BlxckoutFXClientConfig;
import net.minecraft.resources.ResourceLocation;

public final class BlxckoutFXAe2Support {
    private static final String AE2_CRAFTING_TERM_SCREEN_CLASS = "appeng.client.gui.me.items.CraftingTermScreen";
    private static BlxckoutFXClientConfig.Ae2ThemeMode ae2ThemeMode = BlxckoutFXClientConfig.Ae2ThemeMode.Blxckout;
    private static boolean ae2ThemeEnabled = true;

    public static boolean isAe2Screen(net.minecraft.client.gui.screens.Screen screen) {
        if (screen == null) {
            return false;
        }

        String className = screen.getClass().getName();
        return className.startsWith("appeng.")
                || className.startsWith("com.glodblock.github.extendedae.")
                || className.startsWith("net.pedroksl.advanced_ae.")
                || className.startsWith("net.pedroksl.ae2addonlib.")
                || className.startsWith("de.mari_023.ae2wtlib.");
    }

    public static boolean isAe2Texture(ResourceLocation location) {
        if (location == null) {
            return false;
        }

        String namespace = location.getNamespace();
        return "ae2".equals(namespace)
                || "appeng".equals(namespace)
                || "extendedae".equals(namespace)
                || "advanced_ae".equals(namespace)
                || "ae2addonlib".equals(namespace)
                || "ae2wtlib".equals(namespace);
    }

    public static boolean isAe2ThemeEnabled() {
        return ae2ThemeEnabled;
    }

    public static void setAe2ThemeEnabled(boolean enabled) {
        ae2ThemeEnabled = enabled;
    }

    public static void setAe2ThemeMode(BlxckoutFXClientConfig.Ae2ThemeMode mode) {
        ae2ThemeMode = mode == null ? BlxckoutFXClientConfig.Ae2ThemeMode.Blxckout : mode;
    }

    public static boolean isBlxckoutAe2ThemeActive() {
        return ae2ThemeEnabled && ae2ThemeMode == BlxckoutFXClientConfig.Ae2ThemeMode.Blxckout;
    }

    public static boolean isAe2CraftingTermScreenLike(net.minecraft.client.gui.screens.Screen screen) {
        if (screen == null) {
            return false;
        }

        Class<?> current = screen.getClass();
        while (current != null) {
            if (AE2_CRAFTING_TERM_SCREEN_CLASS.equals(current.getName())) {
                return true;
            }
            current = current.getSuperclass();
        }

        return false;
    }

    private BlxckoutFXAe2Support() {
    }
}
