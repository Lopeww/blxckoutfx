package com.lopew.blxckoutfx.client.screen;

import net.minecraft.client.gui.screens.Screen;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class BlxckoutFXScreenExclusions {
    public enum ScreenPolicy {
        FULL,
        PARTIAL,
        NONE
    }

    private static final String[] EXCLUDED_SCREEN_CLASS_PREFIXES = {
            "com.lopew.blxckoutfx.client.config.BlxckoutFXConfigScreen",
            "dev.ftb.mods.ftbquests.client.gui.",
            "net.minecraft.client.gui.screens.ChatScreen",
            "vazkii.patchouli.client.book.gui.",
            "me.desht.pneumaticcraft.client.gui."
    };

    private static final Map<Class<?>, Optional<Method>> WRAPPED_GUI_ACCESSORS = new ConcurrentHashMap<>();

    public static boolean isExcludedScreen(Screen screen) {
        return getScreenPolicy(screen) == ScreenPolicy.NONE;
    }

    public static boolean isPartialScreen(Screen screen) {
        return getScreenPolicy(screen) == ScreenPolicy.PARTIAL;
    }

    public static boolean isFullScreen(Screen screen) {
        return getScreenPolicy(screen) == ScreenPolicy.FULL;
    }

    public static ScreenPolicy getScreenPolicy(Screen screen) {
        if (screen == null) {
            return ScreenPolicy.NONE;
        }

        if (isExcludedClassName(screen.getClass().getName()) || isExcludedWrappedGui(screen)) {
            return ScreenPolicy.NONE;
        }

        if (isJourneyMapFullscreen(screen)) {
            return ScreenPolicy.PARTIAL;
        }

        return ScreenPolicy.FULL;
    }

    private static boolean isExcludedClassName(String className) {
        for (String excludedClassPrefix : EXCLUDED_SCREEN_CLASS_PREFIXES) {
            if (className.startsWith(excludedClassPrefix)) {
                return true;
            }
        }

        return false;
    }

    private static boolean isExcludedWrappedGui(Screen screen) {
        try {
            Optional<Method> getGui = WRAPPED_GUI_ACCESSORS.computeIfAbsent(screen.getClass(), BlxckoutFXScreenExclusions::findWrappedGuiAccessor);
            if (getGui.isEmpty()) {
                return false;
            }

            Object wrappedGui = getGui.get().invoke(screen);
            return wrappedGui != null && isExcludedClassName(wrappedGui.getClass().getName());
        } catch (ReflectiveOperationException ignored) {
            return false;
        }
    }

    private static Optional<Method> findWrappedGuiAccessor(Class<?> screenClass) {
        if (!"dev.ftb.mods.ftblibrary.ui.ScreenWrapper".equals(screenClass.getName())
                && !"dev.ftb.mods.ftblibrary.client.gui.widget.ScreenWrapper".equals(screenClass.getName())) {
            return Optional.empty();
        }

        try {
            return Optional.of(screenClass.getMethod("getGui"));
        } catch (NoSuchMethodException ignored) {
            return Optional.empty();
        }
    }

    private static boolean isJourneyMapFullscreen(Screen screen) {
        return "journeymap.client.ui.fullscreen.Fullscreen".equals(screen.getClass().getName());
    }

    private BlxckoutFXScreenExclusions() {
    }
}
