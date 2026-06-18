package com.lopew.blxckoutfx.mixin;

import com.lopew.blxckoutfx.client.BlxckoutFXRenderContext;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "me.desht.pneumaticcraft.client.gui.widget.WidgetEnergy", remap = false)
public class PneumaticCraftWidgetEnergyMixin {
    @Inject(method = "renderWidget", at = @At("HEAD"), require = 0)
    private void blxckoutfx$suppressEnergyWidgetDarkening(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        BlxckoutFXRenderContext.suppressTextureDarkening();
    }

    @Inject(method = "renderWidget", at = @At("RETURN"), require = 0)
    private void blxckoutfx$allowEnergyWidgetDarkening(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        BlxckoutFXRenderContext.allowTextureDarkening();
    }
}
