package com.lopew.blxckoutfx.mixinplugin;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JourneyMapButtonMixinPluginTest {
    private static final String LEGACY_BUTTON_MIXIN =
            "com.lopew.blxckoutfx.mixin.journeymap.JourneyMapButtonBackgroundMixin";
    private static final String CURRENT_BUTTON_MIXIN =
            "com.lopew.blxckoutfx.mixin.journeymap.JourneyMap6ButtonBackgroundMixin";
    private static final String CURRENT_BASIC_IMAGE_BUTTON_MIXIN =
            "com.lopew.blxckoutfx.mixin.journeymap.JourneyMap6BasicImageButtonBackgroundMixin";
    private static final String CURRENT_FULLSCREEN_SHORTCUT_MIXIN =
            "com.lopew.blxckoutfx.mixin.journeymap.JourneyMap6FullscreenShortcutMixin";

    @Test
    void routesOnlyTheVersionSpecificButtonMixins() {
        assertTrue(shouldApply(LEGACY_BUTTON_MIXIN, true, false));
        assertFalse(shouldApply(LEGACY_BUTTON_MIXIN, true, true));
        assertFalse(shouldApply(LEGACY_BUTTON_MIXIN, false, false));

        assertTrue(shouldApply(CURRENT_BUTTON_MIXIN, false, true));
        assertTrue(shouldApply(CURRENT_BUTTON_MIXIN, true, true));
        assertFalse(shouldApply(CURRENT_BUTTON_MIXIN, true, false));

        assertTrue(shouldApply(CURRENT_BASIC_IMAGE_BUTTON_MIXIN, false, true));
        assertTrue(shouldApply(CURRENT_BASIC_IMAGE_BUTTON_MIXIN, true, true));
        assertFalse(shouldApply(CURRENT_BASIC_IMAGE_BUTTON_MIXIN, true, false));

        assertTrue(shouldApply(CURRENT_FULLSCREEN_SHORTCUT_MIXIN, false, true));
        assertTrue(shouldApply(CURRENT_FULLSCREEN_SHORTCUT_MIXIN, true, true));
        assertFalse(shouldApply(CURRENT_FULLSCREEN_SHORTCUT_MIXIN, true, false));
    }

    @Test
    void leavesUnrelatedMixinsUnconditionallyEligible() {
        String[] unrelatedMixins = {
                "com.lopew.blxckoutfx.mixin.core.AbstractButtonMixin",
                "com.lopew.blxckoutfx.mixin.core.GameRendererMixin",
                "com.lopew.blxckoutfx.mixin.jei.JeiGuiIconButtonMixin",
                "com.lopew.blxckoutfx.mixin.patchouli.PatchouliBookScreenMixin",
                "com.lopew.blxckoutfx.mixin.journeymap.JourneyMapThemeButtonMixin"
        };

        for (String mixin : unrelatedMixins) {
            assertTrue(shouldApply(mixin, false, false), mixin);
            assertTrue(shouldApply(mixin, true, false), mixin);
            assertTrue(shouldApply(mixin, false, true), mixin);
            assertTrue(shouldApply(mixin, true, true), mixin);
        }
    }

    private static boolean shouldApply(String mixinClassName, boolean legacyPresent, boolean currentPresent) {
        return JourneyMapButtonMixinPlugin.shouldApplyButtonMixin(mixinClassName, legacyPresent, currentPresent);
    }
}
