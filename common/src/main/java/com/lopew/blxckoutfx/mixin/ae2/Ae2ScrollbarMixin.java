package com.lopew.blxckoutfx.mixin.ae2;

import com.lopew.blxckoutfx.client.integration.ae2.BlxckoutFXAe2Support;
import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "appeng.client.gui.widgets.Scrollbar", remap = false)
public class Ae2ScrollbarMixin {
    @Unique
    private boolean blxckoutfx$ae2CustomRemapContextActive;

    @Inject(method = "drawForegroundLayer", at = @At("HEAD"), require = 0)
    private void blxckoutfx$enterAe2CustomRemapContext(
            @Coerce Object guiGraphics,
            @Coerce Object bounds,
            @Coerce Object mousePos,
            CallbackInfo ci
    ) {
        if (!BlxckoutFXRenderContext.isRenderingAe2Screen()
                || !BlxckoutFXAe2Support.isBlackoutAe2ThemeActive()) {
            return;
        }

        this.blxckoutfx$ae2CustomRemapContextActive = true;
        BlxckoutFXRenderContext.enterAe2CustomRemapRender();
    }

    @Inject(method = "drawForegroundLayer", at = @At("RETURN"), require = 0)
    private void blxckoutfx$exitAe2CustomRemapContext(
            @Coerce Object guiGraphics,
            @Coerce Object bounds,
            @Coerce Object mousePos,
            CallbackInfo ci
    ) {
        if (!this.blxckoutfx$ae2CustomRemapContextActive) {
            return;
        }

        this.blxckoutfx$ae2CustomRemapContextActive = false;
        BlxckoutFXRenderContext.exitAe2CustomRemapRender();
    }
}
