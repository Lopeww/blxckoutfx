package com.lopew.blxckoutfx.mixin.journeymap;

import com.lopew.blxckoutfx.client.integration.journeymap.BlxckoutFXJourneyMapThemeSupport;
import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "journeymap.client.ui.theme.ThemeButton", remap = false)
public class JourneyMapThemeButtonMixin {
    @Inject(
            method = {
                    "drawNativeButton(Lcom/mojang/blaze3d/vertex/PoseStack;II)V",
                    "drawNativeButton(Lnet/minecraft/client/gui/GuiGraphics;II)V"
            },
            at = @At("HEAD"),
            require = 0
    )
    private void blxckoutfx$enterPuristBackgroundDarkening(CallbackInfo ci) {
        if (BlxckoutFXJourneyMapThemeSupport.isPuristTheme(this)) {
            BlxckoutFXRenderContext.enterButtonRender();
        }
    }

    @Inject(
            method = {
                    "drawNativeButton(Lcom/mojang/blaze3d/vertex/PoseStack;II)V",
                    "drawNativeButton(Lnet/minecraft/client/gui/GuiGraphics;II)V"
            },
            at = @At("RETURN"),
            require = 0
    )
    private void blxckoutfx$exitPuristBackgroundDarkening(CallbackInfo ci) {
        if (BlxckoutFXJourneyMapThemeSupport.isPuristTheme(this)) {
            BlxckoutFXRenderContext.exitButtonRender();
        }
    }
}
