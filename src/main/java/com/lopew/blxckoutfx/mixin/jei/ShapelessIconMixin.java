package com.lopew.blxckoutfx.mixin.jei;

import com.lopew.blxckoutfx.client.BlxckoutFXRenderContext;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "mezz.jei.gui.recipes.ShapelessIcon", remap = false)
public class ShapelessIconMixin {
    @Inject(
            method = "draw(Lnet/minecraft/client/Minecraft;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lmezz/jei/api/gui/IDrawable;draw(Lnet/minecraft/client/Minecraft;)V"
            ),
            require = 0,
            remap = false
    )
    private void blxckoutfx$suppressShapelessIconDarkening(Minecraft minecraft, int recipeWidth, CallbackInfo ci) {
        BlxckoutFXRenderContext.suppressTextureDarkening();
    }

    @Inject(
            method = "draw(Lnet/minecraft/client/Minecraft;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lmezz/jei/api/gui/IDrawable;draw(Lnet/minecraft/client/Minecraft;)V",
                    shift = At.Shift.AFTER
            ),
            require = 0,
            remap = false
    )
    private void blxckoutfx$restoreShapelessIconDarkening(Minecraft minecraft, int recipeWidth, CallbackInfo ci) {
        BlxckoutFXRenderContext.allowTextureDarkening();
    }
}
