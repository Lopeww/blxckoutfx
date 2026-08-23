package com.lopew.blxckoutfx.mixin.core;

import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractWidget.class)
public class PatchouliBookButtonRenderMixin {
    @Unique
    private boolean blxckoutfx$patchouliBookButtonRender;

    @Inject(method = "render", at = @At("HEAD"))
    private void blxckoutfx$suppressPatchouliBookButtonDarkening(
            GuiGraphics guiGraphics,
            int mouseX,
            int mouseY,
            float partialTick,
            CallbackInfo ci
    ) {
        if (((Object) this).getClass().getName().startsWith("vazkii.patchouli.client.book.gui.button.GuiButtonBook")) {
            blxckoutfx$patchouliBookButtonRender = true;
            BlxckoutFXRenderContext.suppressTextureDarkening();
        }
    }

    @Inject(method = "render", at = @At("RETURN"))
    private void blxckoutfx$restorePatchouliBookButtonDarkening(
            GuiGraphics guiGraphics,
            int mouseX,
            int mouseY,
            float partialTick,
            CallbackInfo ci
    ) {
        if (!blxckoutfx$patchouliBookButtonRender) {
            return;
        }

        blxckoutfx$patchouliBookButtonRender = false;
        BlxckoutFXRenderContext.allowTextureDarkening();
    }
}
