package com.lopew.blxckoutfx.mixin.journeymap;

import com.lopew.blxckoutfx.client.integration.journeymap.BlxckoutFXJourneyMapThemeSupport;
import com.lopew.blxckoutfx.client.render.BlxckoutFXShaders;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Pseudo
@Mixin(targets = "journeymap.client.ui.component.Button", remap = false)
public class JourneyMapButtonBackgroundMixin {
    @ModifyArg(
            method = "m_88315_(Lnet/minecraft/client/gui/GuiGraphics;IIF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Ljourneymap/client/render/draw/DrawUtil;drawRectangle(Lcom/mojang/blaze3d/vertex/PoseStack;DDDDIF)V"
            ),
            index = 5,
            require = 0
    )
    private int blxckoutfx$darkenJourneyMapButtonColor(int color) {
        if (!BlxckoutFXShaders.isEnabled() || !BlxckoutFXJourneyMapThemeSupport.shouldDarkenButtonUi()) {
            return color;
        }

        return blxckoutfx$darkenColor(color, BlxckoutFXShaders.getCurrentPresetDivideFactor());
    }

    @Unique
    private static int blxckoutfx$darkenColor(int color, float divideFactor) {
        float factor = Math.max(1.0F, divideFactor);
        int alpha = color & 0xFF000000;
        int red = Math.round(((color >> 16) & 0xFF) / factor);
        int green = Math.round(((color >> 8) & 0xFF) / factor);
        int blue = Math.round((color & 0xFF) / factor);

        return alpha | red << 16 | green << 8 | blue;
    }
}
