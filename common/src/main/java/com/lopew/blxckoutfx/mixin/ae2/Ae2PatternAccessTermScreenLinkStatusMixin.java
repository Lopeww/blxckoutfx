package com.lopew.blxckoutfx.mixin.ae2;

import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "appeng.client.gui.me.patternaccess.PatternAccessTermScreen", remap = false, priority = 2000)
public abstract class Ae2PatternAccessTermScreenLinkStatusMixin {
    @Inject(method = "renderLinkStatus(Lnet/minecraft/client/gui/GuiGraphics;Lappeng/api/storage/ILinkStatus;)V", at = @At("HEAD"), require = 0)
    private void blxckoutfx$enterAe2WarningTextRender(@Coerce Object guiGraphics, @Coerce Object linkStatus, CallbackInfo ci) {
        BlxckoutFXRenderContext.enterAe2WarningTextRender();
    }

    @Inject(method = "renderLinkStatus(Lnet/minecraft/client/gui/GuiGraphics;Lappeng/api/storage/ILinkStatus;)V", at = @At("RETURN"), require = 0)
    private void blxckoutfx$exitAe2WarningTextRender(@Coerce Object guiGraphics, @Coerce Object linkStatus, CallbackInfo ci) {
        BlxckoutFXRenderContext.exitAe2WarningTextRender();
    }
}

