package com.lopew.blxckoutfx.mixin.journeymap;

import com.lopew.blxckoutfx.client.integration.journeymap.BlxckoutFXJourneyMapThemeSupport;
import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import com.lopew.blxckoutfx.client.render.BlxckoutFXShaders;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "journeymap.client.ui.component.buttons.BasicImageButton", remap = false)
public class JourneyMap6BasicImageButtonBackgroundMixin {
    @Unique
    private boolean blxckoutfx$darkeningBackground;

    @Inject(
            method = "m_87963_(Lnet/minecraft/client/gui/GuiGraphics;IIF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Ljourneymap/client/ui/GuiUtils;drawContinuousTexturedBox(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/resources/ResourceLocation;IIIIIIIIIIIIF)V",
                    shift = At.Shift.BEFORE
            ),
            require = 1
    )
    private void blxckoutfx$enterBackgroundDarkening(CallbackInfo ci) {
        if (!BlxckoutFXShaders.isEnabled() || !BlxckoutFXJourneyMapThemeSupport.shouldDarkenButtonUi()) {
            return;
        }

        this.blxckoutfx$darkeningBackground = true;
        BlxckoutFXRenderContext.enterButtonRender();
    }

    @Inject(
            method = "m_87963_(Lnet/minecraft/client/gui/GuiGraphics;IIF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Ljourneymap/client/ui/GuiUtils;drawContinuousTexturedBox(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/resources/ResourceLocation;IIIIIIIIIIIIF)V",
                    shift = At.Shift.AFTER
            ),
            require = 1
    )
    private void blxckoutfx$exitBackgroundDarkening(CallbackInfo ci) {
        if (!this.blxckoutfx$darkeningBackground) {
            return;
        }

        this.blxckoutfx$darkeningBackground = false;
        BlxckoutFXRenderContext.exitButtonRender();
    }
}
