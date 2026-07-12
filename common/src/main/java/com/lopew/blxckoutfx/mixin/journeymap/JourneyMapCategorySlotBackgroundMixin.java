package com.lopew.blxckoutfx.mixin.journeymap;

import com.lopew.blxckoutfx.client.integration.journeymap.BlxckoutFXJourneyMapThemeSupport;
import com.lopew.blxckoutfx.client.render.BlxckoutFXColorUtil;
import com.lopew.blxckoutfx.client.render.BlxckoutFXShaders;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Pseudo
@Mixin(targets = "journeymap.client.ui.option.CategorySlot", remap = false)
public abstract class JourneyMapCategorySlotBackgroundMixin {
    @ModifyArg(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Ljourneymap/client/render/draw/DrawUtil;drawRectangle(Lnet/minecraft/client/gui/GuiGraphics;DDDDIF)V"
            ),
            index = 5,
            require = 0
    )
    private int blxckoutfx$darkenCategoryBackground(int color) {
        return BlxckoutFXJourneyMapThemeSupport.isOptionScreen()
                ? BlxckoutFXColorUtil.darkenColor(color, BlxckoutFXShaders.getCurrentPresetDivideFactor())
                : color;
    }
}
