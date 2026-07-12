package com.lopew.blxckoutfx.mixin.journeymap;

import com.lopew.blxckoutfx.client.BlxckoutFXShaders;
import com.lopew.blxckoutfx.client.integration.journeymap.BlxckoutFXJourneyMapThemeSupport;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Pseudo
@Mixin(targets = "journeymap.client.render.draw.DrawUtil", remap = false)
public class JourneyMapDrawUtilMixin {
    @ModifyVariable(
            method = "drawRectangle(DDDDIF)V",
            at = @At("HEAD"),
            argsOnly = true,
            ordinal = 0,
            require = 1,
            remap = false
    )
    private static int blxckoutfx$darkenJourneyMapOptionRectangle(int color) {
        if (!BlxckoutFXShaders.isEnabled() || !BlxckoutFXJourneyMapThemeSupport.isOptionScreen()) {
            return color;
        }

        float factor = Math.max(1.0F, BlxckoutFXShaders.getCurrentPresetDivideFactor());
        int alpha = color & 0xFF000000;
        int red = Math.round(((color >> 16) & 0xFF) / factor);
        int green = Math.round(((color >> 8) & 0xFF) / factor);
        int blue = Math.round((color & 0xFF) / factor);
        return alpha | red << 16 | green << 8 | blue;
    }
}
