package com.lopew.blxckoutfx.mixin.core;

import com.lopew.blxckoutfx.client.BlxckoutFXRenderContext;
import net.minecraft.client.gui.GuiRepair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiRepair.class)
public abstract class GuiRepairMixin {
    @Inject(method = "drawGuiContainerBackgroundLayer", at = @At("HEAD"), require = 0)
    private void blxckoutfx$enterAnvilHammerIconRemap(float partialTicks, int mouseX, int mouseY, CallbackInfo ci) {
        BlxckoutFXRenderContext.enterAnvilHammerIconRemap();
    }

    @Inject(method = "drawGuiContainerBackgroundLayer", at = @At("RETURN"), require = 0)
    private void blxckoutfx$exitAnvilHammerIconRemap(float partialTicks, int mouseX, int mouseY, CallbackInfo ci) {
        BlxckoutFXRenderContext.exitAnvilHammerIconRemap();
    }
}
