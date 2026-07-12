package com.lopew.blxckoutfx.mixin.jei;

import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "mezz.jei.gui.input.GuiTextFieldFilter", remap = false)
public class JeiTextFieldFilterMixin {
    @Inject(method = "extractWidgetRenderState", at = @At("HEAD"), require = 0)
    private void blxckoutfx$enterJeiWidgetRender(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        BlxckoutFXRenderContext.enterJeiWidgetRender();
    }

    @Inject(method = "extractWidgetRenderState", at = @At("RETURN"), require = 0)
    private void blxckoutfx$exitJeiWidgetRender(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        BlxckoutFXRenderContext.exitJeiWidgetRender();
    }
}
