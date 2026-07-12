package com.lopew.blxckoutfx.mixin.journeymap;

import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderPipelines;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Pseudo
@Mixin(targets = "journeymap.client.ui.option.CategorySlot", remap = false)
public abstract class JourneyMapWebMapDownloadButtonMixin {
    @ModifyArg(
            method = "renderWebMapDownloadButtons(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Ljourneymap/client/ui/component/buttons/Button;Lnet/minecraft/resources/Identifier;IIIII)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blit(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIFFIIII)V",
                    remap = true
            ),
            index = 0,
            require = 0
    )
    private RenderPipeline blxckoutfx$useContentIconPipeline(RenderPipeline pipeline) {
        return BlxckoutFXRenderPipelines.CONTENT_ICON_TEXTURED;
    }
}
