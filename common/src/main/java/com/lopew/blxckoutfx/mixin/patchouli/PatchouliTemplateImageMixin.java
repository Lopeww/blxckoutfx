package com.lopew.blxckoutfx.mixin.patchouli;

import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "vazkii.patchouli.client.book.template.component.ComponentImage", remap = false)
public class PatchouliTemplateImageMixin {
    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;m_280163_(Lnet/minecraft/resources/ResourceLocation;IIFFIIII)V"
            ),
            require = 0
    )
    private void blxckoutfx$suppressImageDarkening(CallbackInfo ci) {
        BlxckoutFXRenderContext.suppressTextureDarkening();
    }

    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;m_280163_(Lnet/minecraft/resources/ResourceLocation;IIFFIIII)V",
                    shift = At.Shift.AFTER
            ),
            require = 0
    )
    private void blxckoutfx$restoreImageDarkening(CallbackInfo ci) {
        BlxckoutFXRenderContext.allowTextureDarkening();
    }
}
