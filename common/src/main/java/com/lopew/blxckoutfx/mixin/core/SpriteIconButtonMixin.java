package com.lopew.blxckoutfx.mixin.core;

import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderPipelines;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.SpriteIconButton;
import net.minecraft.client.gui.screens.PauseScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(SpriteIconButton.class)
public class SpriteIconButtonMixin {
    @ModifyArg(
            method = "extractSprite",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIIIF)V"
            ),
            index = 0
    )
    private RenderPipeline blxckoutfx$preservePauseMenuIcon(RenderPipeline pipeline) {
        return Minecraft.getInstance().gui.screen() instanceof PauseScreen
                ? BlxckoutFXRenderPipelines.CONTENT_ICON_TEXTURED
                : pipeline;
    }
}
