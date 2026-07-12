package com.lopew.blxckoutfx.client.screen;

import net.minecraft.client.gui.screens.Screen;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class BlxckoutFXScreenExclusions {
    private static final String[] EXCLUDED_SCREEN_CLASS_PREFIXES = {
            "com.lopew.blxckoutfx.client.config.BlxckoutFXConfigScreen",
            "dev.ftb.mods.ftbquests.client.gui."
    };
    private static final String[] BUTTON_ONLY_SCREEN_CLASS_PREFIXES = {
            "journeymap.client.ui.",
            "net.minecraft.client.gui.screens.ChatScreen"
    };
    private static final Map<Class<?>, Optional<Method>> WRAPPED_GUI_ACCESSORS = new ConcurrentHashMap<>();

    public static boolean isExcludedScreen(Screen screen) {
        if (screen == null) {
            return false;
        }

        return isExcludedClassName(screen.getClass().getName()) || isExcludedWrappedGui(screen);
    }

    public static boolean isButtonOnlyScreen(Screen screen) {
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

    private static boolean isExcludedWrappedGui(Screen screen) {
        Optional<Method> getGui = getWrappedGuiAccessor(screen.getClass());
        if (getGui.isEmpty()) {
            return false;
        }

        try {
            Object wrappedGui = getGui.get().invoke(screen);
            return wrappedGui != null && isExcludedClassName(wrappedGui.getClass().getName());
        } catch (ReflectiveOperationException ignored) {
            return false;
        }
    }

    private static Optional<Method> getWrappedGuiAccessor(Class<?> screenClass) {
        return WRAPPED_GUI_ACCESSORS.computeIfAbsent(screenClass, BlxckoutFXScreenExclusions::findWrappedGuiAccessor);
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

    private static boolean isExcludedClassName(String className) {
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
