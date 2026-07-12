package com.lopew.blxckoutfx.mixin.jei;

import com.lopew.blxckoutfx.client.BlxckoutFXRenderContext;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "mezz.jei.gui.elements.GuiIconButton", remap = false)
public class GuiIconButtonMixin {
    @Inject(
            method = "drawButton",
            at = @At(
                    value = "INVOKE",
                    target = "Lmezz/jei/api/gui/IDrawable;draw(Lnet/minecraft/client/Minecraft;)V"
            ),
            require = 1,
            remap = false
    )
    private void blxckoutfx$suppressJeiIconButtonIconDarkening(Minecraft minecraft, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        BlxckoutFXRenderContext.suppressTextureDarkening();
    }

    @Inject(
            method = "drawButton",
            at = @At(
                    value = "INVOKE",
                    target = "Lmezz/jei/api/gui/IDrawable;draw(Lnet/minecraft/client/Minecraft;)V",
                    shift = At.Shift.AFTER
            ),
            require = 1,
            remap = false
    )
    private void blxckoutfx$restoreJeiIconButtonIconDarkening(Minecraft minecraft, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        BlxckoutFXRenderContext.allowTextureDarkening();
    }
}
