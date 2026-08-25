package com.lopew.blxckoutfx.mixin.tfc;

import com.lopew.blxckoutfx.client.compat.tfc.TfcGuiGraphicsSupport;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiGraphics.class)
public class TfcGuiGraphicsVanillaMixin {
    @Inject(method = "innerBlit(Lnet/minecraft/resources/ResourceLocation;IIIIIFFFF)V", at = @At("HEAD"), require = 0)
    private void blxckoutfx$preserveDirectIndicators(ResourceLocation texture, int x1, int x2, int y1, int y2,
                                                       int blitOffset, float minU, float maxU, float minV, float maxV,
                                                       CallbackInfo ci) {
        TfcGuiGraphicsSupport.blxckoutfx$preserveDirectIndicators(
                texture, x1, x2, y1, y2, blitOffset, minU, maxU, minV, maxV
        );
    }

    @Inject(method = "innerBlit(Lnet/minecraft/resources/ResourceLocation;IIIIIFFFF)V", at = @At("RETURN"), require = 0)
    private void blxckoutfx$redrawColourData(ResourceLocation texture, int x1, int x2, int y1, int y2,
                                              int blitOffset, float minU, float maxU, float minV, float maxV,
                                              CallbackInfo ci) {
        TfcGuiGraphicsSupport.blxckoutfx$redrawColourData(
                (GuiGraphics) (Object) this, texture, x1, x2, y1, y2, blitOffset, minU, maxU, minV, maxV
        );
    }
}
