package com.lopew.blxckoutfx.mixin;

import com.lopew.blxckoutfx.client.BlxckoutFXRenderContext;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "mezz.jei.gui.elements.GuiIconButton", remap = false)
public class JeiGuiIconButtonMixin {
    @Inject(method = "renderWidget", at = @At("HEAD"), require = 0)
    private void blxckoutfx$enterJeiWidgetRender(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        BlxckoutFXRenderContext.enterJeiWidgetRender();
    }

    @Inject(method = "renderWidget", at = @At("RETURN"), require = 0)
    private void blxckoutfx$exitJeiWidgetRender(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        BlxckoutFXRenderContext.exitJeiWidgetRender();
    }
}
