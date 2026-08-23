package com.lopew.blxckoutfx.mixin.patchouli;

import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Preserves explicit Patchouli images and marking glyphs without exempting page surfaces. */
@Pseudo
@Mixin(targets = {
        "vazkii.patchouli.client.book.template.component.ComponentImage",
        "vazkii.patchouli.client.book.gui.GuiBook"
}, remap = false)
public class PatchouliContentMixin {
    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIFFIIII)V",
                    shift = At.Shift.BEFORE
            ),
            require = 0,
            remap = false
    )
    private void blxckoutfx$preserveTemplateImage(CallbackInfo ci) {
        BlxckoutFXRenderContext.suppressTextureDarkening();
    }

    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIFFIIII)V",
                    shift = At.Shift.AFTER
            ),
            require = 0,
            remap = false
    )
    private void blxckoutfx$restoreTemplateImage(CallbackInfo ci) {
        BlxckoutFXRenderContext.allowTextureDarkening();
    }

    @Inject(
            method = "drawMarking(Lnet/minecraft/client/gui/GuiGraphics;Lvazkii/patchouli/common/book/Book;IIILvazkii/patchouli/client/book/EntryDisplayState;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lvazkii/patchouli/client/book/gui/GuiBook;drawFromTexture(Lnet/minecraft/client/gui/GuiGraphics;Lvazkii/patchouli/common/book/Book;IIIIII)V",
                    shift = At.Shift.BEFORE
            ),
            require = 0,
            remap = false
    )
    private static void blxckoutfx$preserveMarkingGlyph(CallbackInfo ci) {
        BlxckoutFXRenderContext.suppressTextureDarkening();
    }

    @Inject(
            method = "drawMarking(Lnet/minecraft/client/gui/GuiGraphics;Lvazkii/patchouli/common/book/Book;IIILvazkii/patchouli/client/book/EntryDisplayState;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lvazkii/patchouli/client/book/gui/GuiBook;drawFromTexture(Lnet/minecraft/client/gui/GuiGraphics;Lvazkii/patchouli/common/book/Book;IIIIII)V",
                    shift = At.Shift.AFTER
            ),
            require = 0,
            remap = false
    )
    private static void blxckoutfx$restoreMarkingGlyph(CallbackInfo ci) {
        BlxckoutFXRenderContext.allowTextureDarkening();
    }
}
