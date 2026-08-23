package com.lopew.blxckoutfx.mixin.jei;

import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "mezz.jei.library.render.FluidTankRenderer", remap = false)
public class JeiFluidTankRendererMixin {
    @Inject(
            method = "render(Lnet/minecraft/client/gui/GuiGraphics;Ljava/lang/Object;II)V",
            at = @At("HEAD"),
            require = 0
    )
    private void blxckoutfx$preserveJeiFluid(CallbackInfo ci) {
        // Both the ingredient list and recipe slots render fluids through this method.
        BlxckoutFXRenderContext.suppressTextureDarkening();
    }

    @Inject(
            method = "render(Lnet/minecraft/client/gui/GuiGraphics;Ljava/lang/Object;II)V",
            at = @At("RETURN"),
            require = 0
    )
    private void blxckoutfx$restoreJeiFluid(CallbackInfo ci) {
        BlxckoutFXRenderContext.allowTextureDarkening();
    }
}
