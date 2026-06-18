package com.lopew.blxckoutfx.mixin;

import com.lopew.blxckoutfx.client.BlxckoutFXRenderPipelines;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Pseudo
@Mixin(targets = "dev.ftb.mods.ftblibrary.client.icon.ImageIconRenderer", remap = false)
public class FtbImageIconRendererMixin {
    @ModifyArg(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/state/gui/BlitRenderState;<init>(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/client/gui/render/TextureSetup;Lorg/joml/Matrix3x2f;IIIIFFFFILnet/minecraft/client/gui/navigation/ScreenRectangle;)V"
            ),
            index = 0,
            require = 0
    )
    private RenderPipeline blxckoutfx$useContentIconPipelineForBlit(RenderPipeline pipeline) {
        return BlxckoutFXRenderPipelines.CONTENT_ICON_TEXTURED;
    }

    @ModifyArg(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/state/gui/TiledBlitRenderState;<init>(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/client/gui/render/TextureSetup;Lorg/joml/Matrix3x2f;IIIIIIFFFFILnet/minecraft/client/gui/navigation/ScreenRectangle;)V"
            ),
            index = 0,
            require = 0
    )
    private RenderPipeline blxckoutfx$useContentIconPipelineForTiledBlit(RenderPipeline pipeline) {
        return BlxckoutFXRenderPipelines.CONTENT_ICON_TEXTURED;
    }
}
