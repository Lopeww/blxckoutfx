package com.lopew.blxckoutfx.mixin.jei;

import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderPipelines;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Pseudo
@Mixin(targets = "mezz.jei.common.gui.elements.DrawableSprite", remap = false)
public class JeiDrawableSpriteMixin {
    @ModifyArgs(
            method = "draw(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIIIII)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lmezz/jei/common/platform/IPlatformRenderHelper;blitSprite(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;IIIIIIII)V"
            ),
            require = 0
    )
    private void blxckoutfx$useJeiWidgetPipeline(Args args) {
        TextureAtlasSprite sprite = args.get(2);
        Identifier spriteId = sprite.contents().name();
        if (isJeiOverlayButtonIcon(spriteId)) {
            args.set(1, BlxckoutFXRenderPipelines.JEI_WIDGET_TEXTURED);
        }
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
