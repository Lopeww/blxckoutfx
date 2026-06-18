package com.lopew.blxckoutfx.mixin;

import com.lopew.blxckoutfx.client.BlxckoutFXRenderPipelines;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Pseudo
@Mixin(
        targets = {
                "dev.ftb.mods.ftblibrary.client.icon.AtlasSpriteIconRenderer",
                "dev.ftb.mods.ftblibrary.client.icon.TextureAtlasSpriteIconRenderer"
        },
        remap = false
)
public class FtbAtlasIconRendererMixin {
    @ModifyArg(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;IIIII)V"
            ),
            index = 0,
            require = 0
    )
    private RenderPipeline blxckoutfx$useContentIconPipeline(RenderPipeline pipeline) {
        return BlxckoutFXRenderPipelines.CONTENT_ICON_TEXTURED;
    }
}
