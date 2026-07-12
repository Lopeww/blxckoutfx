package com.lopew.blxckoutfx.mixin.ae2;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.lopew.blxckoutfx.client.integration.ae2.BlxckoutFXAe2Support;
import com.lopew.blxckoutfx.client.render.BlxckoutFXAe2TextColors;
import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "appeng.client.gui.AEBaseScreen", remap = false)
public class Ae2AeBaseScreenMixin {
    @Unique
    private boolean blxckoutfx$ae2CustomRemapContextActive;

    @Inject(method = "drawBG", at = @At("HEAD"), require = 0)
    private void blxckoutfx$enterAe2CustomRemapContext(
            @Coerce Object guiGraphics,
            int offsetX,
            int offsetY,
            int mouseX,
            int mouseY,
            float partialTick,
            CallbackInfo ci
    ) {
        if (!BlxckoutFXAe2Support.isBlackoutAe2ThemeActive()) {
            return;
        }

        blxckoutfx$ae2CustomRemapContextActive = true;
        BlxckoutFXRenderContext.enterAe2CustomRemapRender();
    }

    @Inject(method = "drawBG", at = @At("RETURN"), require = 0)
    private void blxckoutfx$exitAe2CustomRemapContext(
            @Coerce Object guiGraphics,
            int offsetX,
            int offsetY,
            int mouseX,
            int mouseY,
            float partialTick,
            CallbackInfo ci
    ) {
        if (!blxckoutfx$ae2CustomRemapContextActive) {
            return;
        }

        blxckoutfx$ae2CustomRemapContextActive = false;
        BlxckoutFXRenderContext.exitAe2CustomRemapRender();
    }

    @WrapOperation(
            method = "drawText",
            at = @At(
                    value = "INVOKE",
                    target = "Lappeng/client/gui/style/Color;toARGB()I"
            ),
            require = 0
    )
    private int blxckoutfx$adjustAe2TextColor(@Coerce Object color, Operation<Integer> original) {
        return BlxckoutFXAe2TextColors.adjustThemeTextColor(original.call(color));
    }
}
