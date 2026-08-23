package com.lopew.blxckoutfx.mixin.patchouli;

import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "vazkii.patchouli.client.book.gui.GuiBook", remap = false)
public class PatchouliBookMarkingMixin {
    @Inject(
            method = "drawMarking(Lnet/minecraft/client/gui/GuiGraphics;Lvazkii/patchouli/common/book/Book;IIILvazkii/patchouli/client/book/EntryDisplayState;)V",
            at = @At("HEAD"),
            require = 0
    )
    private static void blxckoutfx$suppressBookMarkingDarkening(CallbackInfo ci) {
        BlxckoutFXRenderContext.suppressTextureDarkening();
    }

    @Inject(
            method = "drawMarking(Lnet/minecraft/client/gui/GuiGraphics;Lvazkii/patchouli/common/book/Book;IIILvazkii/patchouli/client/book/EntryDisplayState;)V",
            at = @At("RETURN"),
            require = 0
    )
    private static void blxckoutfx$restoreBookMarkingDarkening(CallbackInfo ci) {
        BlxckoutFXRenderContext.allowTextureDarkening();
    }
}
