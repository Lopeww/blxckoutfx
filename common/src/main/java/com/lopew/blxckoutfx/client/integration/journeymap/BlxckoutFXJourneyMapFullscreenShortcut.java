package com.lopew.blxckoutfx.client.integration.journeymap;

import com.lopew.blxckoutfx.client.gui.BlxckoutFXGUIHandler;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Connects the shared BlxckoutFX shortcut widget to JourneyMap 6's fullscreen toolbar.
 */
public final class BlxckoutFXJourneyMapFullscreenShortcut {
    private static final String FULLSCREEN_CLASS = "journeymap.client.ui.fullscreen.Fullscreen";
    private static final Map<Class<?>, Optional<ZoomButtons>> ZOOM_BUTTONS = new ConcurrentHashMap<>();

    public static void layoutShortcut(Screen screen) {
        if (screen == null || !FULLSCREEN_CLASS.equals(screen.getClass().getName())) {
            return;
        }

        ZOOM_BUTTONS.computeIfAbsent(screen.getClass(), BlxckoutFXJourneyMapFullscreenShortcut::findZoomButtons)
                .ifPresent(buttons -> buttons.layout(screen));
    }

    public static Position defaultPosition(
            int zoomInY,
            int zoomInHeight,
            int zoomOutX,
            int zoomOutY,
            int zoomOutWidth,
            int zoomOutHeight,
            int shortcutWidth
    ) {
        int gap = Math.max(0, zoomOutY - (zoomInY + zoomInHeight));
        int x = zoomOutX + Math.max(0, (zoomOutWidth - shortcutWidth) / 2);
        int y = zoomOutY + zoomOutHeight + gap;
        return new Position(x, y);
    }

    private static Optional<ZoomButtons> findZoomButtons(Class<?> fullscreenClass) {
        try {
            Field zoomIn = fullscreenClass.getDeclaredField("buttonZoomIn");
            Field zoomOut = fullscreenClass.getDeclaredField("buttonZoomOut");
            zoomIn.setAccessible(true);
            zoomOut.setAccessible(true);
            return Optional.of(new ZoomButtons(zoomIn, zoomOut));
        } catch (NoSuchFieldException | SecurityException ignored) {
            return Optional.empty();
        }
    }

    private record ZoomButtons(Field zoomIn, Field zoomOut) {
        private void layout(Screen screen) {
            try {
                Object zoomInButton = this.zoomIn.get(screen);
                Object zoomOutButton = this.zoomOut.get(screen);
                if (zoomInButton instanceof AbstractWidget zoomIn && zoomOutButton instanceof AbstractWidget zoomOut) {
                    BlxckoutFXGUIHandler.layoutJourneyMapFullscreenShortcut(screen, zoomIn, zoomOut);
                }
            } catch (IllegalAccessException ignored) {
                // The shortcut is optional; leave JourneyMap untouched if its internals change.
            }
        }
    }

    public record Position(int x, int y) {
    }

    private BlxckoutFXJourneyMapFullscreenShortcut() {
    }
}
