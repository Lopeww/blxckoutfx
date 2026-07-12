package com.lopew.blxckoutfx.mixin.ae2;

import com.lopew.blxckoutfx.client.integration.ae2.BlxckoutFXAe2Support;
import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import com.lopew.blxckoutfx.client.render.BlxckoutFXShaders;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "appeng.client.gui.widgets.UpgradesPanel", remap = false)
public abstract class Ae2UpgradesPanelBorderMixin {
    @Unique
    private static final int AE2_UPGRADE_BORDER_LIGHT = 0xFFF2F2F2;
    @Unique
    private static final int AE2_UPGRADE_BORDER_DARK = 0xFF8E8E8E;

    @Unique
    private boolean blxckoutfx$upgradeBorderContextActive;

    @Inject(method = "drawBackgroundLayer", at = @At("HEAD"), require = 0)
    private void blxckoutfx$enterUpgradeBorderContext(
            @Coerce Object guiGraphics,
            @Coerce Object bounds,
            @Coerce Object mousePosition,
            CallbackInfo ci
    ) {
        if (!blxckoutfx$shouldTrackUpgradeBorder()) {
            return;
        }

        blxckoutfx$upgradeBorderContextActive = true;
        BlxckoutFXRenderContext.enterAe2UpgradePanelBorderRender();
    }

    @Inject(method = "drawBackgroundLayer", at = @At("RETURN"), require = 0)
    private void blxckoutfx$exitUpgradeBorderContext(
            @Coerce Object guiGraphics,
            @Coerce Object bounds,
            @Coerce Object mousePosition,
            CallbackInfo ci
    ) {
        if (!blxckoutfx$upgradeBorderContextActive) {
            return;
        }

        blxckoutfx$upgradeBorderContextActive = false;
        BlxckoutFXRenderContext.exitAe2UpgradePanelBorderRender();
    }

    @Unique
    private static boolean blxckoutfx$shouldTrackUpgradeBorder() {
        return BlxckoutFXAe2Support.isBlxckoutAe2ThemeActive() && BlxckoutFXShaders.isEnabled();
    }

    @ModifyArg(
            method = "drawBackgroundLayer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;hLine(IIII)V"
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
                    target = "Lnet/minecraft/client/gui/GuiGraphics;vLine(IIII)V"
            ),
            index = 3,
            require = 0
    )
    private int blxckoutfx$remapUpgradePanelVerticalBorder(int color) {
        return blxckoutfx$remapUpgradeBorderColor(color);
    }

    @Unique
    private static int blxckoutfx$remapUpgradeBorderColor(int color) {
        if (!BlxckoutFXAe2Support.isBlxckoutAe2ThemeActive()) {
            return color;
        }

        if (!BlxckoutFXShaders.isEnabled()) {
            return color;
        }

        return color == AE2_UPGRADE_BORDER_LIGHT ? AE2_UPGRADE_BORDER_DARK : color;
    }
}

