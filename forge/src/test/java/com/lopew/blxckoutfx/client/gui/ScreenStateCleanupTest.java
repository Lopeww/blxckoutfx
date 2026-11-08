package com.lopew.blxckoutfx.client.gui;

import org.junit.jupiter.api.Test;

import java.util.IdentityHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ScreenStateCleanupTest {
    @Test
    void repeatedScreenInitializationReleasesAllObsoleteScreenEntries() {
        Map<Object, Object> inventoryButtons = new IdentityHashMap<>();
        Map<Object, Boolean> toolbarVisibility = new IdentityHashMap<>();
        Object previousScreen = null;

        for (int index = 0; index < 500; index++) {
            Object screen = new Object();
            ScreenStateCleanup.onScreenInit(inventoryButtons, toolbarVisibility);

            assertTrue(inventoryButtons.isEmpty());
            assertTrue(toolbarVisibility.isEmpty());
            if (previousScreen != null) {
                assertFalse(inventoryButtons.containsKey(previousScreen));
                assertFalse(toolbarVisibility.containsKey(previousScreen));
            }

            inventoryButtons.put(screen, new Object());
            toolbarVisibility.put(screen, Boolean.TRUE);
            assertEquals(1, inventoryButtons.size());
            assertEquals(1, toolbarVisibility.size());
            previousScreen = screen;
        }
    }

    @Test
    void closeRemovesStateRestoredDuringConfigRefreshAndAllowsReopen() {
        Map<Object, Object> inventoryButtons = new IdentityHashMap<>();
        Map<Object, Boolean> toolbarVisibility = new IdentityHashMap<>();
        Object closedScreen = new Object();
        inventoryButtons.put(closedScreen, new Object());
        toolbarVisibility.put(closedScreen, Boolean.TRUE);

        ScreenStateCleanup.onScreenClose(inventoryButtons, toolbarVisibility, closedScreen, () -> {
            // Config refresh can rediscover the closing screen's button while the client still points to it.
            inventoryButtons.put(closedScreen, new Object());
            toolbarVisibility.put(closedScreen, Boolean.TRUE);
        });

        assertFalse(inventoryButtons.containsKey(closedScreen));
        assertFalse(toolbarVisibility.containsKey(closedScreen));
        assertTrue(inventoryButtons.isEmpty());
        assertTrue(toolbarVisibility.isEmpty());

        Object reopenedScreen = new Object();
        ScreenStateCleanup.onScreenInit(inventoryButtons, toolbarVisibility);
        inventoryButtons.put(reopenedScreen, new Object());
        toolbarVisibility.put(reopenedScreen, Boolean.FALSE);
        assertEquals(1, inventoryButtons.size());
        assertEquals(1, toolbarVisibility.size());
    }

    @Test
    void screenTransitionRetainsOnlyTheActiveScreenInBothMaps() {
        Map<Object, Object> inventoryButtons = new IdentityHashMap<>();
        Map<Object, Boolean> toolbarVisibility = new IdentityHashMap<>();
        Object activeScreen = new Object();
        Object obsoleteScreenOne = new Object();
        Object obsoleteScreenTwo = new Object();
        inventoryButtons.put(activeScreen, new Object());
        inventoryButtons.put(obsoleteScreenOne, new Object());
        inventoryButtons.put(obsoleteScreenTwo, new Object());
        toolbarVisibility.put(activeScreen, Boolean.TRUE);
        toolbarVisibility.put(obsoleteScreenOne, Boolean.FALSE);
        toolbarVisibility.put(obsoleteScreenTwo, Boolean.TRUE);

        ScreenStateCleanup.retainOnlyCurrentScreen(inventoryButtons, toolbarVisibility, activeScreen);

        assertEquals(1, inventoryButtons.size());
        assertEquals(1, toolbarVisibility.size());
        assertTrue(inventoryButtons.containsKey(activeScreen));
        assertTrue(toolbarVisibility.containsKey(activeScreen));
        assertFalse(inventoryButtons.containsKey(obsoleteScreenOne));
        assertFalse(inventoryButtons.containsKey(obsoleteScreenTwo));
        assertFalse(toolbarVisibility.containsKey(obsoleteScreenOne));
        assertFalse(toolbarVisibility.containsKey(obsoleteScreenTwo));
    }

    @Test
    void reinitializingTheSameScreenClearsItsOldButtonAndToolbarState() {
        Map<Object, Object> inventoryButtons = new IdentityHashMap<>();
        Map<Object, Boolean> toolbarVisibility = new IdentityHashMap<>();
        Object screen = new Object();
        Object oldButton = new Object();
        inventoryButtons.put(screen, oldButton);
        toolbarVisibility.put(screen, Boolean.TRUE);

        ScreenStateCleanup.onScreenInit(inventoryButtons, toolbarVisibility);
        inventoryButtons.put(screen, new Object());
        toolbarVisibility.put(screen, Boolean.FALSE);

        assertEquals(1, inventoryButtons.size());
        assertEquals(1, toolbarVisibility.size());
        assertFalse(inventoryButtons.containsValue(oldButton));
        assertTrue(toolbarVisibility.containsKey(screen));
        assertFalse(toolbarVisibility.get(screen));
    }
}
