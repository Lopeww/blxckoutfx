package com.lopew.blxckoutfx.client.gui;

import java.util.Map;

final class ScreenStateCleanup {
    private ScreenStateCleanup() {
    }

    static void onScreenInit(Map<?, ?> inventoryButtons, Map<?, ?> toolbarVisibility) {
        inventoryButtons.clear();
        toolbarVisibility.clear();
    }

    static void onScreenClose(
            Map<?, ?> inventoryButtons,
            Map<?, ?> toolbarVisibility,
            Object screen,
            Runnable refreshConfig
    ) {
        try {
            refreshConfig.run();
        } finally {
            inventoryButtons.remove(screen);
            toolbarVisibility.remove(screen);
        }
    }

    static void retainOnlyCurrentScreen(Map<?, ?> inventoryButtons, Map<?, ?> toolbarVisibility, Object currentScreen) {
        retainOnlyCurrentScreen(inventoryButtons, currentScreen);
        retainOnlyCurrentScreen(toolbarVisibility, currentScreen);
    }

    private static void retainOnlyCurrentScreen(Map<?, ?> screenStates, Object currentScreen) {
        screenStates.keySet().removeIf(screen -> screen != currentScreen);
    }
}
