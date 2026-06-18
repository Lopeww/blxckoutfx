package com.lopew.blxckoutfx.mixin;

import com.lopew.blxckoutfx.client.BlxckoutFXRenderContext;
import com.lopew.blxckoutfx.client.BlxckoutFXTextColors;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "me.desht.pneumaticcraft.client.render.pressure_gauge.PressureGaugeRenderer2D", remap = false)
public class PneumaticCraftPressureGaugeMixin {
    @Inject(method = "drawPressureGauge(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/gui/Font;FFFFFIII)V", at = @At("HEAD"), require = 0)
    private static void blxckoutfx$suppressPressureGaugeDarkening(GuiGraphics guiGraphics, Font font, float minPressure, float dangerPressure, float pressure, float x, float y, int width, int height, int textColor, CallbackInfo ci) {
        BlxckoutFXRenderContext.suppressTextureDarkening();
    }

    @Inject(method = "drawPressureGauge(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/gui/Font;FFFFFIII)V", at = @At("RETURN"), require = 0)
    private static void blxckoutfx$allowPressureGaugeDarkening(GuiGraphics guiGraphics, Font font, float minPressure, float dangerPressure, float pressure, float x, float y, int width, int height, int textColor, CallbackInfo ci) {
        BlxckoutFXRenderContext.allowTextureDarkening();
    }

    @ModifyArg(
            method = "drawText",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Ljava/lang/String;IIIZ)I"
            ),
            index = 4,
            require = 0
    )
    private static int blxckoutfx$adjustPressureGaugeTextColor(int color) {
        return BlxckoutFXTextColors.adjustGeneralTextColor(color, true, false);
    }
}
