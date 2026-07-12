package com.lopew.blxckoutfx.mixin.core;

import com.lopew.blxckoutfx.client.BlxckoutFXRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.recipebook.GuiButtonRecipeTab;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiButtonRecipeTab.class)
public class GuiButtonRecipeTabMixin {
    @Inject(method = "drawButton(Lnet/minecraft/client/Minecraft;IIF)V", at = @At("HEAD"), require = 1)
    private void blxckoutfx$enterRecipeTabRender(Minecraft mc, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        BlxckoutFXRenderContext.enterButtonRender();
        BlxckoutFXRenderContext.enterIconColorPreserve();
    }

    @Inject(method = "drawButton(Lnet/minecraft/client/Minecraft;IIF)V", at = @At("RETURN"), require = 1)
    private void blxckoutfx$exitRecipeTabRender(Minecraft mc, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        BlxckoutFXRenderContext.exitIconColorPreserve();
        BlxckoutFXRenderContext.exitButtonRender();
    }
}
