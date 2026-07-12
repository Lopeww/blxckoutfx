package com.lopew.blxckoutfx.mixin.ae2;

import com.lopew.blxckoutfx.client.integration.ae2.BlxckoutFXAe2Support;
import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderPipelines;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Pseudo
@Mixin(targets = "appeng.client.gui.widgets.VerticalButtonBar", remap = false)
public class Ae2VerticalButtonBarMixin {
    @ModifyArg(
            method = "drawBackgroundLayer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIII)V"
            ),
            index = 0,
            require = 0
    )
    private RenderPipeline blxckoutfx$useAe2BlackoutPipelineForToolbarBackground(RenderPipeline pipeline) {
        if (BlxckoutFXAe2Support.isBlackoutAe2ThemeActive()) {
            return BlxckoutFXRenderPipelines.AE2_BLACKOUT_TEXTURED;
        }

        return pipeline;
    }
}
