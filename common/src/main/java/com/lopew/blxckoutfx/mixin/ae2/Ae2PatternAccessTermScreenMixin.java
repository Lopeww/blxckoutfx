package com.lopew.blxckoutfx.mixin.ae2;

import com.lopew.blxckoutfx.client.integration.ae2.BlxckoutFXAe2Support;
import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderPipelines;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Pseudo
@Mixin(targets = "appeng.client.gui.me.patternaccess.PatternAccessTermScreen", remap = false)
public class Ae2PatternAccessTermScreenMixin {
    @ModifyArg(
            method = "blit",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blit(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIFFIIII)V"
            ),
            index = 0,
            require = 0
    )
    private RenderPipeline blxckoutfx$useAe2BlackoutPipeline(RenderPipeline pipeline) {
        if (BlxckoutFXAe2Support.isBlackoutAe2ThemeActive()
                && BlxckoutFXRenderContext.isRenderingAe2Screen()) {
            return BlxckoutFXRenderPipelines.AE2_BLACKOUT_TEXTURED;
        }

        return pipeline;
    }
}
