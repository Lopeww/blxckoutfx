package com.lopew.blxckoutfx.client.integration.journeymap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Locale;

public final class BlxckoutFXJourneyMapThemeSupport {
    private static final String JOURNEYMAP_FULLSCREEN_CLASS = "journeymap.client.ui.fullscreen.Fullscreen";
    private static final String JOURNEYMAP_OPTION_SCREEN_PREFIX = "journeymap.client.ui.option.";
    private static final String JOURNEYMAP_WAYPOINT_MANAGER_SCREEN_PREFIX = "journeymap.client.ui.waypointmanager.";
    private static final String THEME_LOADER_CLASS = "journeymap.client.io.ThemeLoader";
    private static final String PURIST_THEME_NAME = "Purist";
    private static final String PURIST_THEME_NAME_NORMALIZED = normalizeThemeName(PURIST_THEME_NAME);

    private static volatile Method currentThemeGetter;
    private static volatile boolean currentThemeGetterResolved;

    public static boolean isPuristThemeActive() {
        return PURIST_THEME_NAME_NORMALIZED.equals(normalizeThemeName(getCurrentThemeName()));
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
        Screen screen = Minecraft.getInstance().gui.screen();
        return screen == null ? "" : screen.getClass().getName();
    }

    private static String getCurrentThemeName() {
        try {
            Method getCurrentTheme = getCurrentThemeGetter();
            if (getCurrentTheme == null) {
                return "";
            }
            Object theme = getCurrentTheme.invoke(null);
            return getThemeName(theme);
        } catch (ReflectiveOperationException | LinkageError exception) {
            return "";
        }
    }

    private static Method getCurrentThemeGetter() throws ReflectiveOperationException {
        if (currentThemeGetterResolved) {
            return currentThemeGetter;
        }

        synchronized (BlxckoutFXJourneyMapThemeSupport.class) {
            if (!currentThemeGetterResolved) {
                try {
                    currentThemeGetter = Class.forName(THEME_LOADER_CLASS).getMethod("getCurrentTheme");
                } finally {
                    currentThemeGetterResolved = true;
                }
            }
        }

        return currentThemeGetter;
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
        Screen screen = Minecraft.getInstance().gui.screen();
        return screen != null && JOURNEYMAP_FULLSCREEN_CLASS.equals(screen.getClass().getName());
    }

    private BlxckoutFXJourneyMapThemeSupport() {
    }
}
