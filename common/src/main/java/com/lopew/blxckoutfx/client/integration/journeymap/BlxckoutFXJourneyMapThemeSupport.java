package com.lopew.blxckoutfx.client.integration.journeymap;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Locale;

public final class BlxckoutFXJourneyMapThemeSupport {
    private static final String JOURNEYMAP_FULLSCREEN_CLASS = "journeymap.client.ui.fullscreen.Fullscreen";
    private static final String JOURNEYMAP_OPTION_SCREEN_PREFIX = "journeymap.client.ui.option.";
    private static final String JOURNEYMAP_WAYPOINT_MANAGER_SCREEN_PREFIX = "journeymap.client.ui.waypointmanager.";
    private static final String THEME_LOADER_CLASS = "journeymap.client.io.ThemeLoader";
    private static final String PURIST_THEME_NAME = "Purist";

    public static boolean isPuristThemeActive() {
        return normalizeThemeName(getCurrentThemeName()).equals(normalizeThemeName(PURIST_THEME_NAME));
    }

    public static boolean shouldBypassThemeButtonDarkening() {
        return isJourneyMapFullscreen() && !isPuristThemeActive();
    }

    public static boolean isWaypointManagerScreen() {
        return currentScreenClassName().startsWith(JOURNEYMAP_WAYPOINT_MANAGER_SCREEN_PREFIX);
    }

    public static boolean isOptionScreen() {
        return currentScreenClassName().startsWith(JOURNEYMAP_OPTION_SCREEN_PREFIX);
    }

    public static boolean shouldDarkenOptionUi() {
        return isOptionScreen() || isWaypointManagerScreen();
    }

    private static String currentScreenClassName() {
        try {
            Class<?> minecraftClass = Class.forName("net.minecraft.client.Minecraft");
            Object minecraft = minecraftClass.getMethod("getInstance").invoke(null);
            if (minecraft == null) {
                return "";
            }

            Object screen = minecraftClass.getField("screen").get(minecraft);
            return screen != null ? screen.getClass().getName() : "";
        } catch (ReflectiveOperationException | LinkageError exception) {
            return "";
        }
    }

    private static String getCurrentThemeName() {
        try {
            Class<?> themeLoaderClass = Class.forName(THEME_LOADER_CLASS);
            Method getCurrentTheme = themeLoaderClass.getMethod("getCurrentTheme");
            Object theme = getCurrentTheme.invoke(null);
            return getThemeName(theme);
        } catch (ReflectiveOperationException | LinkageError exception) {
            return "";
        }
    }

    private static String getThemeName(Object theme) {
        if (theme == null) {
            return "";
        }

        try {
            Field nameField = theme.getClass().getField("name");
            Object value = nameField.get(theme);
            if (value != null) {
                return value.toString();
            }
        } catch (ReflectiveOperationException | LinkageError ignored) {
        }

        return String.valueOf(theme);
    }

    private static String normalizeThemeName(String themeName) {
        return themeName == null ? "" : themeName.trim().toLowerCase(Locale.ROOT);
    }

    private static boolean isJourneyMapFullscreen() {
        try {
            Class<?> minecraftClass = Class.forName("net.minecraft.client.Minecraft");
            Object minecraft = minecraftClass.getMethod("getInstance").invoke(null);
            if (minecraft == null) {
                return false;
            }

            Object screen = minecraftClass.getField("screen").get(minecraft);
            return screen != null && JOURNEYMAP_FULLSCREEN_CLASS.equals(screen.getClass().getName());
        } catch (ReflectiveOperationException | LinkageError exception) {
            return false;
        }
    }

    private BlxckoutFXJourneyMapThemeSupport() {
    }
}
