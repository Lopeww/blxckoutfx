package com.lopew.blxckoutfx.mixin.ae2;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.lopew.blxckoutfx.client.integration.ae2.BlxckoutFXAe2Support;
import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;

@Pseudo
@Mixin(targets = "appeng.client.gui.widgets.IconButton", remap = false)
public class Ae2IconButtonMixin {
    @WrapOperation(
            method = "renderWidget",
            at = @At(
                    value = "INVOKE",
                    target = "Lappeng/client/gui/style/Blitter;blit(Lnet/minecraft/client/gui/GuiGraphics;)V",
                    ordinal = 1
            ),
            require = 0
    )
    private void blxckoutfx$keepAe2HalfIconReadable(
            @Coerce Object blitter,
            GuiGraphics guiGraphics,
            Operation<Void> original
    ) {
        blxckoutfx$renderAe2IconWithoutDarkening(blitter, guiGraphics, original);
    }

    @WrapOperation(
            method = "renderWidget",
            at = @At(
                    value = "INVOKE",
                    target = "Lappeng/client/gui/style/Blitter;blit(Lnet/minecraft/client/gui/GuiGraphics;)V",
                    ordinal = 3
            ),
            require = 0
    )
    private void blxckoutfx$keepAe2FullIconReadable(
            @Coerce Object blitter,
            GuiGraphics guiGraphics,
            Operation<Void> original
    ) {
        blxckoutfx$renderAe2IconWithoutDarkening(blitter, guiGraphics, original);
    }

    @Unique
    private void blxckoutfx$renderAe2IconWithoutDarkening(
            @Coerce Object blitter,
            GuiGraphics guiGraphics,
            Operation<Void> original
    ) {
        Button self = (Button) (Object) this;
        if (!BlxckoutFXAe2Support.isAe2ThemeEnabled()
                || !BlxckoutFXRenderContext.isRenderingAe2Screen()) {
            original.call(blitter, guiGraphics);
            return;
        }

        if (BlxckoutFXAe2Support.isBlackoutAe2ThemeActive()
                && blxckoutfx$shouldUseAe2TransferButtonPalette(self)) {
            BlxckoutFXRenderContext.enterAe2TransferButtonRender();
            try {
                original.call(blitter, guiGraphics);
            } finally {
                BlxckoutFXRenderContext.exitAe2TransferButtonRender();
            }
            return;
        }

        BlxckoutFXRenderContext.enterAe2IconRender();
        try {
            original.call(blitter, guiGraphics);
        } finally {
            BlxckoutFXRenderContext.exitAe2IconRender();
        }
    }

    @Unique
    private static boolean blxckoutfx$shouldUseAe2TransferButtonPalette(Button self) {
        if (self == null) {
            return false;
        }

        var currentScreen = Minecraft.getInstance().screen;
        if (currentScreen == null
                || !BlxckoutFXAe2Support.isAe2Screen(currentScreen)) {
            return false;
        }

        Object icon = blxckoutfx$invokeNoArg(self, "getIcon");
        if (!(icon instanceof Enum<?> enumIcon)) {
            return false;
        }

        String iconName = enumIcon.name();
        String className = self.getClass().getName();
        if ("appeng.client.gui.widgets.ActionButton".equals(className)
                && blxckoutfx$isAe2CraftingTerminalTransferButton(currentScreen, iconName)) {
            return true;
        }

        if (blxckoutfx$isAddonReactionChamberClearButton(self, iconName)) {
            return true;
        }

        if (blxckoutfx$isAe2PatternEncodingBlackThemeButton(currentScreen, iconName)) {
            return true;
        }

        return blxckoutfx$isWetPatternEncodingMiniButton(self);
    }

    @Unique
    private static boolean blxckoutfx$isAe2CraftingTerminalTransferButton(Object currentScreen, String iconName) {
        return BlxckoutFXAe2Support.isAe2CraftingTermScreenLike((net.minecraft.client.gui.screens.Screen) currentScreen)
                && ("S_ARROW_UP".equals(iconName)
                || "S_ARROW_DOWN".equals(iconName)
                || "S_STASH".equals(iconName)
                || "S_STASH_TO_PLAYER_INV".equals(iconName));
    }

    @Unique
    private static boolean blxckoutfx$isAe2PatternEncodingBlackThemeButton(Object currentScreen, String iconName) {
        return BlxckoutFXAe2Support.isAe2PatternEncodingTermScreenLike((net.minecraft.client.gui.screens.Screen) currentScreen)
                && ("S_CLEAR".equals(iconName)
                || "S_SUBSTITUTION_ENABLED".equals(iconName)
                || "S_SUBSTITUTION_DISABLED".equals(iconName)
                || "S_FLUID_SUBSTITUTION_ENABLED".equals(iconName)
                || "S_FLUID_SUBSTITUTION_DISABLED".equals(iconName));
    }

    @Unique
    private static boolean blxckoutfx$isAddonReactionChamberClearButton(Button self, String iconName) {
        return "net.pedroksl.ae2addonlib.client.widgets.AddonActionButton".equals(self.getClass().getName())
                && "S_CLEAR".equals(iconName);
    }

    @Unique
    private static boolean blxckoutfx$isWetPatternEncodingMiniButton(Button self) {
        var currentScreen = Minecraft.getInstance().screen;
        if (currentScreen == null
                || !"de.mari_023.ae2wtlib.wet.WETScreen".equals(currentScreen.getClass().getName())) {
            return false;
        }

        String className = self.getClass().getName();
        if (!"appeng.client.gui.widgets.ActionButton".equals(className)
                && !"appeng.client.gui.widgets.ToggleButton".equals(className)) {
            return false;
        }

        Object isHalfSize = blxckoutfx$invokeNoArg(self, "isHalfSize");
        Object isDisableBackground = blxckoutfx$invokeNoArg(self, "isDisableBackground");
        return Boolean.TRUE.equals(isHalfSize) && Boolean.TRUE.equals(isDisableBackground);
    }

    @Unique
    private static Object blxckoutfx$invokeNoArg(Object target, String methodName) {
        Class<?> current = target.getClass();
        while (current != null) {
            try {
                var method = current.getDeclaredMethod(methodName);
                method.setAccessible(true);
                return method.invoke(target);
            } catch (ReflectiveOperationException ignored) {
                current = current.getSuperclass();
            }
        }
        return null;
    }
}

