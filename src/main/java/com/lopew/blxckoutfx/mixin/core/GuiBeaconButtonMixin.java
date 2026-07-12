package com.lopew.blxckoutfx.mixin.core;

import com.lopew.blxckoutfx.client.BlxckoutFXRenderContext;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.client.gui.inventory.GuiBeacon$Button")
public abstract class GuiBeaconButtonMixin {
    @Inject(
            method = "drawButton(Lnet/minecraft/client/Minecraft;IIF)V",
            at = @At("HEAD"),
            require = 1
    )
    private void blxckoutfx$enterBeaconPowerIconRemap(Minecraft mc, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        if (((Object) this).getClass().getName().endsWith("$PowerButton")) {
            BlxckoutFXRenderContext.enterBeaconPowerIconRemap();
        }
    }

    @Inject(
            method = "drawButton(Lnet/minecraft/client/Minecraft;IIF)V",
            at = @At("RETURN"),
            require = 1
    )
    private void blxckoutfx$exitBeaconPowerIconRemap(Minecraft mc, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        if (((Object) this).getClass().getName().endsWith("$PowerButton")) {
            BlxckoutFXRenderContext.exitBeaconPowerIconRemap();
        }
    }
}
