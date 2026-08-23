package com.lopew.blxckoutfx.mixin.patchouli;

import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = {
        "vazkii.patchouli.client.book.BookIcon$StackIcon",
        "vazkii.patchouli.client.book.BookIcon$TextureIcon"
}, remap = false)
public class PatchouliBookIconMixin {
    @Inject(method = "render", at = @At("HEAD"), require = 0)
    private void blxckoutfx$suppressBookIconDarkening(CallbackInfo ci) {
        BlxckoutFXRenderContext.suppressTextureDarkening();
    }

    @Inject(method = "render", at = @At("RETURN"), require = 0)
    private void blxckoutfx$restoreBookIconDarkening(CallbackInfo ci) {
        BlxckoutFXRenderContext.allowTextureDarkening();
    }
}
