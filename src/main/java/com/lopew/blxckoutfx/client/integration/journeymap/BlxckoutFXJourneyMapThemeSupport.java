package com.lopew.blxckoutfx.client.integration.journeymap;

import net.minecraft.client.Minecraft;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class BlxckoutFXJourneyMapThemeSupport {
    private static final String PURIST_THEME_NAME = "Purist";
    private static final String JOURNEYMAP_OPTIONS_MANAGER = "journeymap.client.ui.dialog.OptionsManager";
    private static final String JOURNEYMAP_OPTION_SCREEN_PREFIX = "journeymap.client.ui.option.";
    private static final Map<Class<?>, Field> THEME_FIELDS = new ConcurrentHashMap<>();
    private static final Map<Class<?>, Field> NAME_FIELDS = new ConcurrentHashMap<>();

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
            return nameField != null && PURIST_THEME_NAME.equals(nameField.get(theme));
        } catch (ReflectiveOperationException ignored) {
            return false;
        }
    }

    public static boolean isOptionScreen() {
        Minecraft minecraft = Minecraft.getMinecraft();
        if (minecraft == null || minecraft.currentScreen == null) {
            return false;
        }

        Class<?> current = minecraft.currentScreen.getClass();
        while (current != null) {
            String className = current.getName();
            if (JOURNEYMAP_OPTIONS_MANAGER.equals(className)
                    || className.startsWith(JOURNEYMAP_OPTION_SCREEN_PREFIX)) {
                return true;
            }
            current = current.getSuperclass();
        }
        return false;
    }

    private static Field findThemeField(Class<?> buttonClass) {
        Class<?> current = buttonClass;
        while (current != null) {
            try {
                Field field = current.getDeclaredField("theme");
                field.setAccessible(true);
                return field;
            } catch (NoSuchFieldException ignored) {
                current = current.getSuperclass();
            }
        }
        return null;
    }

    private static Field findNameField(Class<?> themeClass) {
        try {
            Field field = themeClass.getDeclaredField("name");
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException ignored) {
            return null;
        }
    }

    private BlxckoutFXJourneyMapThemeSupport() {
    }
}
