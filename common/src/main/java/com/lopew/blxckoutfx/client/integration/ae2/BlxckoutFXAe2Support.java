package com.lopew.blxckoutfx.client.integration.ae2;

import com.lopew.blxckoutfx.client.config.BlxckoutFXClientConfig;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.Identifier;

public final class BlxckoutFXAe2Support {
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

    private static boolean themeEnabled = true;
    private static BlxckoutFXClientConfig.Ae2ThemeMode themeMode = BlxckoutFXClientConfig.Ae2ThemeMode.Blackout;

    public static void syncFromConfig(BlxckoutFXClientConfig config) {
        themeEnabled = config.isAe2ThemeEnabled();
        themeMode = config.getAe2ThemeMode();
    }

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

    public static boolean isAe2Texture(Identifier atlasLocation) {
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

    public static boolean isAe2ThemeEnabled() {
        return themeEnabled;
    }

    public static boolean isBlxckoutAe2ThemeActive() {
        return themeEnabled && themeMode == BlxckoutFXClientConfig.Ae2ThemeMode.Blxckout;
    }

    public static boolean isBlackoutAe2ThemeActive() {
        return themeEnabled && themeMode == BlxckoutFXClientConfig.Ae2ThemeMode.Blackout;
    }

    public static boolean isBlxckoutThemeActive() {
        return isBlxckoutAe2ThemeActive();
    }

    public static boolean shouldDarkenAe2Screen() {
        return isBlxckoutAe2ThemeActive();
    }

    public static void setAe2ThemeMode(BlxckoutFXClientConfig.Ae2ThemeMode mode) {
        themeMode = mode == null ? BlxckoutFXClientConfig.Ae2ThemeMode.Blackout : mode;
    }

    public static void setAe2ThemeEnabled(boolean enabled) {
        themeEnabled = enabled;
    }

    private BlxckoutFXAe2Support() {
    }
}
