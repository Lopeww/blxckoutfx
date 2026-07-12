package com.lopew.blxckoutfx.mixin.ae2;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.lopew.blxckoutfx.client.integration.ae2.BlxckoutFXAe2Support;
import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "appeng.client.gui.widgets.UpgradesPanel", remap = false)
public class Ae2UpgradesPanelBorderMixin {
    @Unique
    private static final int AE2_UPGRADE_BORDER_LIGHT = 0xFFF2F2F2;
    @Unique
    private static final int AE2_UPGRADE_BORDER_DARK = 0xFF8E8E8E;
    @Unique
    private boolean blxckoutfx$ae2CustomRemapContextActive;

    @Inject(method = "drawBackgroundLayer", at = @At("HEAD"), require = 0)
    private void blxckoutfx$enterAe2CustomRemapContext(
            GuiGraphicsExtractor guiGraphics,
            @SuppressWarnings("rawtypes") @org.spongepowered.asm.mixin.injection.Coerce Object screenBounds,
            @SuppressWarnings("rawtypes") @org.spongepowered.asm.mixin.injection.Coerce Object mouse,
            CallbackInfo ci
    ) {
        if (!BlxckoutFXAe2Support.isBlackoutAe2ThemeActive() || !BlxckoutFXRenderContext.isRenderingAe2Screen()) {
            return;
        }

        this.blxckoutfx$ae2CustomRemapContextActive = true;
        BlxckoutFXRenderContext.enterAe2CustomRemapRender();
    }

    @Inject(method = "drawBackgroundLayer", at = @At("RETURN"), require = 0)
    private void blxckoutfx$exitAe2CustomRemapContext(
            GuiGraphicsExtractor guiGraphics,
            @SuppressWarnings("rawtypes") @org.spongepowered.asm.mixin.injection.Coerce Object screenBounds,
            @SuppressWarnings("rawtypes") @org.spongepowered.asm.mixin.injection.Coerce Object mouse,
            CallbackInfo ci
    ) {
        if (!this.blxckoutfx$ae2CustomRemapContextActive) {
            return;
        }

        this.blxckoutfx$ae2CustomRemapContextActive = false;
        BlxckoutFXRenderContext.exitAe2CustomRemapRender();
    }

    @ModifyArg(
            method = "drawBackgroundLayer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;horizontalLine(IIII)V"
            ),
            index = 3,
            require = 0
    )
    private int blxckoutfx$remapUpgradePanelHorizontalBorder(int color) {
        return blxckoutfx$remapUpgradeBorderColor(color);
    }

    @ModifyArg(
            method = "drawBackgroundLayer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;verticalLine(IIII)V"
            ),
            index = 3,
            require = 0
    )
    private int blxckoutfx$remapUpgradePanelVerticalBorder(int color) {
        return blxckoutfx$remapUpgradeBorderColor(color);
    }

    @WrapOperation(
            method = "drawSlot",
            at = @At(
                    value = "INVOKE",
                    target = "Lappeng/client/gui/style/Blitter;blit(Lnet/minecraft/client/gui/GuiGraphicsExtractor;)V"
            ),
            require = 0
    )
    private static void blxckoutfx$forceUpgradeSlotBackgroundRemap(
            @Coerce Object blitter,
            GuiGraphicsExtractor guiGraphics,
            Operation<Void> original
    ) {
        if (!BlxckoutFXAe2Support.isBlackoutAe2ThemeActive() || !BlxckoutFXRenderContext.isRenderingAe2Screen()) {
            original.call(blitter, guiGraphics);
            return;
        }

        BlxckoutFXRenderContext.enterAe2CustomRemapRender();
        try {
            original.call(blitter, guiGraphics);
        } finally {
            BlxckoutFXRenderContext.exitAe2CustomRemapRender();
        }
    }

    @Unique
    private static int blxckoutfx$remapUpgradeBorderColor(int color) {
        if (!BlxckoutFXAe2Support.isBlackoutAe2ThemeActive()
                || !BlxckoutFXRenderContext.isRenderingAe2Screen()) {
            return color;
        }

        return color == AE2_UPGRADE_BORDER_LIGHT ? AE2_UPGRADE_BORDER_DARK : color;
    }
}
