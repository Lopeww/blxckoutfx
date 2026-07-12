package com.lopew.blxckoutfx.mixin.pneumaticcraft;

import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "me.desht.pneumaticcraft.client.gui.widget.WidgetTank", remap = false)
public class PneumaticCraftWidgetTankMixin {
    @Inject(method = "renderWidget", at = @At("HEAD"), require = 0)
    private void blxckoutfx$suppressTankDarkening(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        BlxckoutFXRenderContext.suppressTextureDarkening();
    }

    @Inject(method = "renderWidget", at = @At("RETURN"), require = 0)
    private void blxckoutfx$allowTankDarkening(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        BlxckoutFXRenderContext.allowTextureDarkening();
    }
}
