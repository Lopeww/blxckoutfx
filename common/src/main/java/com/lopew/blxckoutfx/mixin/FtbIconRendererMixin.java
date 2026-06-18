package com.lopew.blxckoutfx.mixin;

import com.lopew.blxckoutfx.client.BlxckoutFXRenderContext;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(
        targets = {
                "dev.ftb.mods.ftblibrary.icon.AtlasSpriteIcon",
                "dev.ftb.mods.ftblibrary.icon.ImageIcon"
        },
        remap = false
)
public class FtbIconRendererMixin {
    @Inject(method = {
            "draw(Lnet/minecraft/client/gui/GuiGraphics;IIII)V",
            "draw(Lnet/minecraft/class_332;IIII)V"
    }, at = @At("HEAD"), require = 0)
    private void blxckoutfx$suppressFtbIconDarkening(GuiGraphics guiGraphics, int x, int y, int width, int height, CallbackInfo ci) {
        BlxckoutFXRenderContext.suppressTextureDarkening();
    }

    @Inject(method = {
            "draw(Lnet/minecraft/client/gui/GuiGraphics;IIII)V",
            "draw(Lnet/minecraft/class_332;IIII)V"
    }, at = @At("RETURN"), require = 0)
    private void blxckoutfx$restoreFtbIconDarkening(GuiGraphics guiGraphics, int x, int y, int width, int height, CallbackInfo ci) {
        BlxckoutFXRenderContext.allowTextureDarkening();
    }
}
