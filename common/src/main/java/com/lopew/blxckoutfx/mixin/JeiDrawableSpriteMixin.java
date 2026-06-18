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
@Mixin(targets = "mezz.jei.common.gui.elements.DrawableSprite", remap = false)
public class JeiDrawableSpriteMixin {
    @Shadow
    @Final
    private Identifier spriteId;

    @ModifyArg(
            method = "draw(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIIIII)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lmezz/jei/common/platform/IPlatformRenderHelper;blitSprite(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;IIIIIIII)V"
            ),
            index = 1,
            require = 0
    )
    private RenderPipeline blxckoutfx$useJeiWidgetPipeline(RenderPipeline pipeline) {
        return isJeiOverlayButtonIcon(spriteId) ? BlxckoutFXRenderPipelines.JEI_WIDGET_TEXTURED : pipeline;
    }

    private static boolean isJeiOverlayButtonIcon(Identifier spriteId) {
        if (!"jei".equals(spriteId.getNamespace())) {
            return false;
        }

        return switch (spriteId.getPath()) {
            case "icons/config_button",
                 "icons/config_button_cheat",
                 "icons/bookmark_button_disabled",
                 "icons/bookmark_button_enabled",
                 "icons/history_button_disabled",
                 "icons/history_button_enabled",
                 "icons/recipe_bookmark",
                 "icons/recipe_transfer",
                 "icons/arrow_previous",
                 "icons/arrow_next",
                 "icons/bookmarks_first",
                 "icons/craftable_first" -> true;
            default -> false;
        };
    }
}
