package com.lopew.blxckoutfx.mixin;

import com.lopew.blxckoutfx.client.BlxckoutFXRenderPipelines;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Pseudo
@Mixin(targets = "mezz.jei.common.gui.elements.ScalableDrawable", remap = false)
public class JeiScalableDrawableMixin {
    @Shadow
    @Final
    private Identifier spriteId;

    @ModifyArg(
            method = "draw(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIII)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lmezz/jei/common/platform/IPlatformRenderHelper;blitTiledSprite(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;Lnet/minecraft/client/resources/metadata/gui/GuiSpriteScaling$Tile;IIIII)V"
            ),
            index = 1,
            require = 0
    )
    private RenderPipeline blxckoutfx$useJeiWidgetPipelineForTiled(RenderPipeline pipeline) {
        return isJeiButtonPressedSprite(spriteId) ? BlxckoutFXRenderPipelines.JEI_WIDGET_TEXTURED : pipeline;
    }

    @ModifyArg(
            method = "draw(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIII)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lmezz/jei/common/platform/IPlatformRenderHelper;blitNineSlicedSprite(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;Lnet/minecraft/client/resources/metadata/gui/GuiSpriteScaling$NineSlice;IIII)V"
            ),
            index = 1,
            require = 0
    )
    private RenderPipeline blxckoutfx$useJeiWidgetPipelineForNineSlice(RenderPipeline pipeline) {
        return isJeiButtonPressedSprite(spriteId) ? BlxckoutFXRenderPipelines.JEI_WIDGET_TEXTURED : pipeline;
    }

    @ModifyArg(
            method = "draw(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIII)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;IIII)V"
            ),
            index = 0,
            require = 0
    )
    private RenderPipeline blxckoutfx$useJeiWidgetPipelineForSprite(RenderPipeline pipeline) {
        return isJeiButtonPressedSprite(spriteId) ? BlxckoutFXRenderPipelines.JEI_WIDGET_TEXTURED : pipeline;
    }

    private static boolean isJeiButtonPressedSprite(Identifier spriteId) {
        if (!"jei".equals(spriteId.getNamespace())) {
            return false;
        }

        return switch (spriteId.getPath()) {
            case "button_pressed", "button_pressed_highlighted" -> true;
            default -> false;
        };
    }
}
