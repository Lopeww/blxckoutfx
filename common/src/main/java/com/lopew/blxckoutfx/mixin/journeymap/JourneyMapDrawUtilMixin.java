package com.lopew.blxckoutfx.mixin.journeymap;

import com.lopew.blxckoutfx.client.integration.journeymap.BlxckoutFXJourneyMapThemeSupport;
import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderPipelines;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Pseudo
@Mixin(targets = "journeymap.client.render.draw.DrawUtil", remap = false)
public class JourneyMapDrawUtilMixin {
    @ModifyArg(
            method = "drawTexture(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/resources/Identifier;IFDDDDZFDZZ)V",
            at = @At(
                    value = "INVOKE",
                    target = "Ljourneymap/client/render/draw/DrawUtil;drawTexture(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IFDDDDFFFFDZZZ)V"
            ),
            index = 1,
            require = 0
    )
    private static RenderPipeline blxckoutfx$useJourneyMapThemeButtonPipeline(RenderPipeline pipeline) {
        return BlxckoutFXJourneyMapThemeSupport.isOptionScreen()
                || BlxckoutFXJourneyMapThemeSupport.isPuristThemeActive()
                        && !BlxckoutFXJourneyMapThemeSupport.isWaypointManagerScreen()
                ? BlxckoutFXRenderPipelines.BUTTON_TEXTURED
                : pipeline;
    }
}
