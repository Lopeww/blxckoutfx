package com.lopew.blxckoutfx.mixin.core;

import com.lopew.blxckoutfx.client.BlxckoutFXRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButtonLanguage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiButtonLanguage.class)
public class GuiButtonLanguageMixin {
    @Inject(method = "drawButton(Lnet/minecraft/client/Minecraft;IIF)V", at = @At("HEAD"), require = 1)
    private void blxckoutfx$enterLanguageButtonRender(Minecraft mc, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        BlxckoutFXRenderContext.enterButtonRender();
        BlxckoutFXRenderContext.enterLanguageButtonIconRemap();
    }

    @Inject(method = "drawButton(Lnet/minecraft/client/Minecraft;IIF)V", at = @At("RETURN"), require = 1)
    private void blxckoutfx$exitLanguageButtonRender(Minecraft mc, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        BlxckoutFXRenderContext.exitLanguageButtonIconRemap();
        BlxckoutFXRenderContext.exitButtonRender();
    }
}
