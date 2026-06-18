package com.lopew.blxckoutfx.mixin;

import com.lopew.blxckoutfx.client.BlxckoutFXRenderPipelines;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Pseudo
@Mixin(targets = "mezz.jei.gui.elements.ButtonSprites", remap = false)
public class JeiButtonSpritesMixin {
    @ModifyArg(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIIII)V"
            ),
            index = 0,
            require = 0
    )
    private RenderPipeline blxckoutfx$useJeiWidgetPipeline(RenderPipeline pipeline) {
        return BlxckoutFXRenderPipelines.JEI_WIDGET_TEXTURED;
    }
}
