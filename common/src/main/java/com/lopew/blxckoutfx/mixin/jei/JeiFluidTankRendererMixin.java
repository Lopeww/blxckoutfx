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
    private void blxckoutfx$suppressFluidDarkening(CallbackInfo ci) {
        BlxckoutFXRenderContext.suppressTextureDarkening();
    }

    @Inject(
            method = "render(Lnet/minecraft/client/gui/GuiGraphics;Ljava/lang/Object;II)V",
            at = @At("RETURN"),
            require = 0
    )
    private void blxckoutfx$restoreFluidDarkening(CallbackInfo ci) {
        BlxckoutFXRenderContext.allowTextureDarkening();
    }

    @Inject(
            method = "drawTextureWithMasking(Lorg/joml/Matrix4f;FFLnet/minecraft/client/renderer/texture/TextureAtlasSprite;JJF)V",
            at = @At("HEAD"),
            require = 0
    )
    private static void blxckoutfx$suppressTiledFluidSpriteDarkening(CallbackInfo ci) {
        // JEI draws fluids directly as atlas quads, bypassing GuiGraphics and its normal render path.
        BlxckoutFXRenderContext.suppressTextureDarkening();
    }

    @Inject(
            method = "drawTextureWithMasking(Lorg/joml/Matrix4f;FFLnet/minecraft/client/renderer/texture/TextureAtlasSprite;JJF)V",
            at = @At("RETURN"),
            require = 0
    )
    private static void blxckoutfx$restoreTiledFluidSpriteDarkening(CallbackInfo ci) {
        BlxckoutFXRenderContext.allowTextureDarkening();
    }
}
