package com.lopew.blxckoutfx.client.integration.ae2;

import com.lopew.blxckoutfx.client.config.BlxckoutFXClientConfig;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;

public final class BlxckoutFXAe2Support {
    public static final String AE2_CRAFTING_TERM_SCREEN_CLASS = "appeng.client.gui.me.items.CraftingTermScreen";
    public static final String AE2_PATTERN_ENCODING_TERM_SCREEN_CLASS = "appeng.client.gui.me.items.PatternEncodingTermScreen";
    public static final String AE2_INSCRIBER_SCREEN_CLASS = "appeng.client.gui.implementations.InscriberScreen";
    private static final String[] AE2_SCREEN_CLASS_PREFIXES = {
            "appeng.",
            "com.glodblock.github.extendedae.",
            "de.mari_023.ae2wtlib.",
            "net.pedroksl.advanced_ae.",
            "gripe._90.megacells."
    };
    private static final String[] AE2_TEXTURE_NAMESPACES = {
            "ae2",
            "appliedenergistics2",
            "extendedae",
            "ae2wtlib",
            "advanced_ae",
            "megacells"
    };
    private static volatile BlxckoutFXClientConfig.Ae2ThemeMode ae2ThemeMode = BlxckoutFXClientConfig.load().getAe2ThemeMode();
    private static volatile boolean ae2ThemeEnabled = BlxckoutFXClientConfig.load().isAe2ThemeEnabled();

    public static boolean isAe2Screen(Screen screen) {
        if (screen == null) {
            return false;
        }

        String className = screen.getClass().getName();
        for (String prefix : AE2_SCREEN_CLASS_PREFIXES) {
            if (className.startsWith(prefix)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isAe2CraftingTermScreen(Screen screen) {
        return screen != null && AE2_CRAFTING_TERM_SCREEN_CLASS.equals(screen.getClass().getName());
    }

    public static boolean isAe2CraftingTermScreenLike(Screen screen) {
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

    public static boolean isAe2PatternEncodingTermScreenLike(Screen screen) {
        if (screen == null) {
            return false;
        }

        Class<?> current = screen.getClass();
        while (current != null) {
            if (AE2_PATTERN_ENCODING_TERM_SCREEN_CLASS.equals(current.getName())) {
                return true;
            }

            current = current.getSuperclass();
        }

        return false;
    }

    public static boolean isAe2InscriberScreenLike(Screen screen) {
        if (screen == null) {
            return false;
        }

        Class<?> current = screen.getClass();
        while (current != null) {
            if (AE2_INSCRIBER_SCREEN_CLASS.equals(current.getName())) {
                return true;
            }

            current = current.getSuperclass();
        }

        return false;
    }

    public static boolean isAe2Texture(ResourceLocation atlasLocation) {
        if (atlasLocation == null) {
            return false;
        }

        String namespace = atlasLocation.getNamespace();
        for (String ae2Namespace : AE2_TEXTURE_NAMESPACES) {
            if (ae2Namespace.equals(namespace)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isBlackoutSupported() {
        return !isFabricEnvironment();
    }

    public static BlxckoutFXClientConfig.Ae2ThemeMode getAe2ThemeMode() {
        return ae2ThemeMode;
    }

    public static boolean isAe2ThemeEnabled() {
        return ae2ThemeEnabled;
    }

    public static boolean isBlxckoutAe2ThemeActive() {
        return ae2ThemeEnabled && ae2ThemeMode == BlxckoutFXClientConfig.Ae2ThemeMode.Blxckout;
    }

    public static boolean isBlackoutAe2ThemeActive() {
        return ae2ThemeEnabled && ae2ThemeMode == BlxckoutFXClientConfig.Ae2ThemeMode.Blackout;
    }

    public static void setAe2ThemeMode(BlxckoutFXClientConfig.Ae2ThemeMode mode) {
        ae2ThemeMode = mode == null ? BlxckoutFXClientConfig.Ae2ThemeMode.Blxckout : mode;
    }

    public static void setAe2ThemeEnabled(boolean enabled) {
        ae2ThemeEnabled = enabled;
    }

    private static boolean isFabricEnvironment() {
        try {
            Class.forName("net.fabricmc.loader.api.FabricLoader", false, BlxckoutFXAe2Support.class.getClassLoader());
            return true;
        } catch (Throwable ignored) {
            return false;
        }
    }

    private BlxckoutFXAe2Support() {
    }
}
