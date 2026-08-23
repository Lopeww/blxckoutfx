package com.lopew.blxckoutfx.mixin.patchouli;

import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "vazkii.patchouli.client.book.gui.GuiBookEntry", remap = false)
public class PatchouliEntryContentMixin {
    @Inject(method = "drawForegroundElements", at = @At("HEAD"), require = 0)
    private void blxckoutfx$suppressEntryContentDarkening(CallbackInfo ci) {
        // Page components are mod-defined, so the common entry render pass is the stable integration point.
        BlxckoutFXRenderContext.suppressTextureDarkening();
    }

    @Inject(method = "drawForegroundElements", at = @At("RETURN"), require = 0)
    private void blxckoutfx$restoreEntryContentDarkening(CallbackInfo ci) {
        BlxckoutFXRenderContext.allowTextureDarkening();
    }
}
