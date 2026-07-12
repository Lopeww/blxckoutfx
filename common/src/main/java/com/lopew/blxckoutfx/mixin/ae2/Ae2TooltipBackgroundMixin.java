package com.lopew.blxckoutfx.mixin.ae2;

import com.lopew.blxckoutfx.client.integration.ae2.BlxckoutFXAe2Support;
import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil")
public class Ae2TooltipBackgroundMixin {
    @Inject(method = "extractTooltipBackground", at = @At("HEAD"), require = 0)
    private static void blxckoutfx$enterAe2TooltipBackgroundRender(
            GuiGraphicsExtractor graphics,
            int x,
            int y,
            int width,
            int height,
            Identifier texture,
            CallbackInfo ci
    ) {
        if (!BlxckoutFXAe2Support.isBlackoutAe2ThemeActive() || !BlxckoutFXRenderContext.isRenderingAe2Screen()) {
            return;
        }

        BlxckoutFXRenderContext.enterAe2TooltipBackgroundRender();
    }

    @Inject(method = "extractTooltipBackground", at = @At("RETURN"), require = 0)
    private static void blxckoutfx$exitAe2TooltipBackgroundRender(
            GuiGraphicsExtractor graphics,
            int x,
            int y,
            int width,
            int height,
            Identifier texture,
            CallbackInfo ci
    ) {
        if (!BlxckoutFXRenderContext.isRenderingAe2TooltipBackground()) {
            return;
        }

        BlxckoutFXRenderContext.exitAe2TooltipBackgroundRender();
    }
}
