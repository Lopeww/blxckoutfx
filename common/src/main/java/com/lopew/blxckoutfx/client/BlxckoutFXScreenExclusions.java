package com.lopew.blxckoutfx.client;

import net.minecraft.client.gui.screens.Screen;

public final class BlxckoutFXScreenExclusions {
    private static final String[] EXCLUDED_SCREEN_CLASS_NAMES = {
    };

    private static final String[] EXCLUDED_SCREEN_CLASS_PREFIXES = {
            "dev.ftb.mods.ftbquests.client.gui."
    };

    public static boolean isExcludedScreen(Screen screen) {
        if (screen == null) {
            return false;
        }

        return isExcludedClassName(screen.getClass().getName()) || isExcludedWrappedGui(screen);
    }

    private static boolean isExcludedClassName(String className) {
        return matchesClassName(className) || matchesClassPrefix(className);
    }

    private static boolean isExcludedWrappedGui(Screen screen) {
        try {
            Object wrappedGui = screen.getClass().getMethod("getGui").invoke(screen);
            return wrappedGui != null && isExcludedClassName(wrappedGui.getClass().getName());
        } catch (ReflectiveOperationException ignored) {
            return false;
        }
    }

    private static boolean matchesClassName(String className) {
        for (String excludedClassName : EXCLUDED_SCREEN_CLASS_NAMES) {
            if (className.equals(excludedClassName)) {
                return true;
            }
        }

        return false;
    }

    private static boolean matchesClassPrefix(String className) {
        for (String excludedClassPrefix : EXCLUDED_SCREEN_CLASS_PREFIXES) {
            if (className.startsWith(excludedClassPrefix)) {
                return true;
            }
        }

        return false;
    }

    private BlxckoutFXScreenExclusions() {
    }
}
