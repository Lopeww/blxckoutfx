package com.lopew.blxckoutfx.mixin;

import com.lopew.blxckoutfx.client.BlxckoutFXRenderPipelines;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.gui.components.AbstractButton;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(AbstractButton.class)
public class AbstractButtonMixin {
    @ModifyArg(
            method = "extractDefaultSprite",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIIII)V"
            ),
            index = 0
    )
    private RenderPipeline blxckoutfx$useButtonPipeline(RenderPipeline pipeline) {
        return BlxckoutFXRenderPipelines.BUTTON_TEXTURED;
    }
}
