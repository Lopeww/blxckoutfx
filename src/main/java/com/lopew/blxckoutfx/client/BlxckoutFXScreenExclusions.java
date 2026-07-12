package com.lopew.blxckoutfx.client;

import net.minecraft.client.gui.GuiScreen;

public final class BlxckoutFXScreenExclusions {
    private static final String[] EXCLUDED_SCREEN_CLASS_NAMES = {
    };

    private static final String[] EXCLUDED_SCREEN_CLASS_PREFIXES = {
            "com.lopew.blxckoutfx.client.config.BlxckoutFXConfigScreen",
            "dev.ftb.mods.ftbquests.client.gui.",
            "com.feed_the_beast.ftbquests.gui."
    };

    private static final String[] BUTTON_ONLY_SCREEN_CLASS_PREFIXES = {
            "journeymap.client.ui.",
            "net.minecraft.client.gui.GuiChat"
    };

    public static boolean isExcludedScreen(GuiScreen screen) {
        if (screen == null) {
            return false;
        }

        String className = screen.getClass().getName();
        return isExcludedClassName(className) || isExcludedWrappedGui(screen);
    }

    public static boolean isButtonOnlyScreen(GuiScreen screen) {
        if (screen == null) {
            return false;
        }

        String className = screen.getClass().getName();
        for (String prefix : BUTTON_ONLY_SCREEN_CLASS_PREFIXES) {
            if (className.startsWith(prefix)) {
                return true;
            }
        }

        return false;
    }

    private static boolean isExcludedClassName(String className) {
        return matchesClassName(className) || matchesClassPrefix(className);
    }

    private static boolean isExcludedWrappedGui(GuiScreen screen) {
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
