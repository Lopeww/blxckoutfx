package com.lopew.blxckoutfx.mixinplugin;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.service.MixinService;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * Routes only the version-specific JourneyMap Button mixins before Mixin resolves their targets.
 */
public final class JourneyMapButtonMixinPlugin implements IMixinConfigPlugin {
    private static final String LEGACY_BUTTON_CLASS = "journeymap.client.ui.component.Button";
    private static final String CURRENT_BUTTON_CLASS = "journeymap.client.ui.component.buttons.Button";
    private static final String LEGACY_BUTTON_MIXIN = "com.lopew.blxckoutfx.mixin.journeymap.JourneyMapButtonBackgroundMixin";
    private static final String CURRENT_BUTTON_MIXIN = "com.lopew.blxckoutfx.mixin.journeymap.JourneyMap6ButtonBackgroundMixin";
    private static final String CURRENT_BASIC_IMAGE_BUTTON_MIXIN =
            "com.lopew.blxckoutfx.mixin.journeymap.JourneyMap6BasicImageButtonBackgroundMixin";
    private static final String CURRENT_FULLSCREEN_SHORTCUT_MIXIN =
            "com.lopew.blxckoutfx.mixin.journeymap.JourneyMap6FullscreenShortcutMixin";

    private ButtonGeneration buttonGeneration;

    @Override
    public void onLoad(String mixinPackage) {
        buttonGeneration = detectButtonGeneration();
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return shouldApplyButtonMixin(
                mixinClassName,
                buttonGeneration == ButtonGeneration.LEGACY,
                buttonGeneration == ButtonGeneration.CURRENT
        );
    }

    static boolean shouldApplyButtonMixin(String mixinClassName, boolean legacyButtonPresent, boolean currentButtonPresent) {
        if (LEGACY_BUTTON_MIXIN.equals(mixinClassName)) {
            return legacyButtonPresent && !currentButtonPresent;
        }
        if (CURRENT_BUTTON_MIXIN.equals(mixinClassName)
                || CURRENT_BASIC_IMAGE_BUTTON_MIXIN.equals(mixinClassName)
                || CURRENT_FULLSCREEN_SHORTCUT_MIXIN.equals(mixinClassName)) {
            return currentButtonPresent;
        }
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    private static ButtonGeneration detectButtonGeneration() {
        if (isClassPresent(CURRENT_BUTTON_CLASS)) {
            return ButtonGeneration.CURRENT;
        }
        if (isClassPresent(LEGACY_BUTTON_CLASS)) {
            return ButtonGeneration.LEGACY;
        }
        return ButtonGeneration.NONE;
    }

    private static boolean isClassPresent(String className) {
        try {
            // Bytecode lookup does not load or initialize the JourneyMap class.
            return MixinService.getService().getBytecodeProvider().getClassNode(className, true) != null;
        } catch (ClassNotFoundException | IOException | RuntimeException ignored) {
            return false;
        }
    }

    private enum ButtonGeneration {
        LEGACY,
        CURRENT,
        NONE
    }
}
