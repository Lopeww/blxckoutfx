package com.lopew.blxckoutfx.client.integration.journeymap;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BlxckoutFXJourneyMapFullscreenShortcutTest {
    @Test
    void centersTheShortcutBelowZoomOutUsingTheToolbarGap() {
        BlxckoutFXJourneyMapFullscreenShortcut.Position position = BlxckoutFXJourneyMapFullscreenShortcut.defaultPosition(
                100, 20,
                10, 124, 20, 20,
                20
        );

        assertEquals(10, position.x());
        assertEquals(148, position.y());
    }

    @Test
    void doesNotCreateNegativeSpacingWhenToolbarWidgetsTouch() {
        BlxckoutFXJourneyMapFullscreenShortcut.Position position = BlxckoutFXJourneyMapFullscreenShortcut.defaultPosition(
                100, 20,
                10, 118, 18, 18,
                20
        );

        assertEquals(10, position.x());
        assertEquals(136, position.y());
    }
}
