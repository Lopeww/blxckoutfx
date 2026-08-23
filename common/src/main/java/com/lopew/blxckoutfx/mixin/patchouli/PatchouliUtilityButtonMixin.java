package com.lopew.blxckoutfx.mixin.patchouli;

import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import net.minecraft.client.gui.components.AbstractWidget;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractWidget.class)
public class PatchouliUtilityButtonMixin {
    @Unique
    private boolean blxckoutfx$renderingPatchouliBookButton;

    @Inject(method = "render", at = @At("HEAD"))
    private void blxckoutfx$enterPatchouliBookButtonRender(GuiGraphics graphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        if (((Object) this).getClass().getName().startsWith("vazkii.patchouli.client.book.gui.button.GuiButtonBook")) {
            blxckoutfx$renderingPatchouliBookButton = true;
            BlxckoutFXRenderContext.enterPatchouliBookButtonRender();
        }
    }

    @Inject(method = "render", at = @At("RETURN"))
    private void blxckoutfx$exitPatchouliBookButtonRender(GuiGraphics graphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        if (!blxckoutfx$renderingPatchouliBookButton) {
            return;
        }

        blxckoutfx$renderingPatchouliBookButton = false;
        BlxckoutFXRenderContext.exitPatchouliBookButtonRender();
    }
}

