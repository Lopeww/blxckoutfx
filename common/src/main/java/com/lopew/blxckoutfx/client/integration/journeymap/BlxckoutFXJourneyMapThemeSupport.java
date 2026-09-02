package com.lopew.blxckoutfx.client.integration.journeymap;

import net.minecraft.client.Minecraft;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class BlxckoutFXJourneyMapThemeSupport {
    private static final String PURIST_THEME_NAME = "Purist";
    private static final String[] JOURNEYMAP_ABOUT_SCREENS = {
            "journeymap.client.ui.dialog.AboutDialog",
            "journeymap.client.ui.dialog.about.AboutScreen"
    };
    private static final String JOURNEYMAP_OPTION_SCREEN_PREFIX = "journeymap.client.ui.option.";
    private static final String[] JOURNEYMAP_WAYPOINT_SCREEN_PREFIXES = {
            "journeymap.client.ui.waypoint.",
            "journeymap.client.ui.waypointmanager."
    };
    private static final Map<Class<?>, Field> THEME_FIELDS = new ConcurrentHashMap<>();
    private static final Map<Class<?>, Field> NAME_FIELDS = new ConcurrentHashMap<>();

    public static boolean shouldDarkenButtonUi() {
        return isOptionScreen() || isAboutScreen() || isWaypointScreen();
    }

    public static boolean isOptionScreen() {
        Minecraft minecraft = Minecraft.getInstance();
        return minecraft != null
                && minecraft.screen != null
                && hasClassPrefix(minecraft.screen.getClass(), JOURNEYMAP_OPTION_SCREEN_PREFIX);
    }

    private static boolean isAboutScreen() {
        Minecraft minecraft = Minecraft.getInstance();
        return minecraft != null
                && minecraft.screen != null
                && hasAnyClassName(minecraft.screen.getClass(), JOURNEYMAP_ABOUT_SCREENS);
    }

    private static boolean isWaypointScreen() {
        Minecraft minecraft = Minecraft.getInstance();
        return minecraft != null
                && minecraft.screen != null
                && hasAnyClassPrefix(minecraft.screen.getClass(), JOURNEYMAP_WAYPOINT_SCREEN_PREFIXES);
    }

    public static boolean isPuristTheme(Object button) {
        if (button == null) {
            return false;
        }

        try {
            Field themeField = THEME_FIELDS.computeIfAbsent(button.getClass(), BlxckoutFXJourneyMapThemeSupport::findThemeField);
            if (themeField == null) {
                return false;
            }

            Object theme = themeField.get(button);
            if (theme == null) {
                return false;
            }

            Field nameField = NAME_FIELDS.computeIfAbsent(theme.getClass(), BlxckoutFXJourneyMapThemeSupport::findNameField);
            if (nameField == null) {
                return false;
            }

            Object themeName = nameField.get(theme);
            return PURIST_THEME_NAME.equals(themeName);
        } catch (ReflectiveOperationException ignored) {
            return false;
        }
    }

    private static Field findThemeField(Class<?> buttonClass) {
        Class<?> current = buttonClass;
        while (current != null) {
            try {
                Field themeField = current.getDeclaredField("theme");
                themeField.setAccessible(true);
                return themeField;
            } catch (NoSuchFieldException ignored) {
                current = current.getSuperclass();
            }
        }

        return null;
    }

    private static Field findNameField(Class<?> themeClass) {
        try {
            Field nameField = themeClass.getDeclaredField("name");
            nameField.setAccessible(true);
            return nameField;
        } catch (NoSuchFieldException ignored) {
            return null;
        }
    }

    private static boolean hasClassPrefix(Class<?> type, String prefix) {
        Class<?> current = type;
        while (current != null) {
            if (current.getName().startsWith(prefix)) {
                return true;
            }
            current = current.getSuperclass();
        }

        return false;
    }

    private static boolean hasAnyClassPrefix(Class<?> type, String[] prefixes) {
        for (String prefix : prefixes) {
            if (hasClassPrefix(type, prefix)) {
                return true;
            }
        }

        return false;
    }

    private static boolean hasClassName(Class<?> type, String className) {
        Class<?> current = type;
        while (current != null) {
            if (current.getName().equals(className)) {
                return true;
            }
            current = current.getSuperclass();
        }

        return false;
    }

    private static boolean hasAnyClassName(Class<?> type, String[] classNames) {
        for (String className : classNames) {
            if (hasClassName(type, className)) {
                return true;
            }
        }

        return false;
    }

    private BlxckoutFXJourneyMapThemeSupport() {
    }
}
