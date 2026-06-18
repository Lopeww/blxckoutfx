package com.lopew.blxckoutfx.mixin;

import com.lopew.blxckoutfx.client.BlxckoutFXRenderContext;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "mezz.jei.gui.input.GuiTextFieldFilter", remap = false)
public class JeiTextFieldFilterMixin {
    @Inject(method = {"renderWidget", "m_87963_", "method_48579"}, at = @At("HEAD"), require = 0)
    private void blxckoutfx$enterJeiWidgetRender(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        BlxckoutFXRenderContext.enterJeiWidgetRender();
    }

    @Inject(method = {"renderWidget", "m_87963_", "method_48579"}, at = @At("RETURN"), require = 0)
    private void blxckoutfx$exitJeiWidgetRender(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        BlxckoutFXRenderContext.exitJeiWidgetRender();
    }
}
