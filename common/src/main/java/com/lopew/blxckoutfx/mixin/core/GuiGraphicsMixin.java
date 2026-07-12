package com.lopew.blxckoutfx.mixin.core;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.lopew.blxckoutfx.client.integration.ae2.BlxckoutFXAe2Support;
import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiGraphicsExtractor.class)
public class GuiGraphicsMixin {
    private static final String AE2_VERTICAL_BUTTONS_BG_PATH = "vertical_buttons_bg";
    private static final String AE2_PATTERN_MODES_BG_PATH = "guis/pattern_modes.png";

    @Inject(method = "blit(Lnet/minecraft/resources/Identifier;IIIIFFFF)V", at = @At("HEAD"), require = 0)
    private void blxckoutfx$enterAe2TextureRender(
            Identifier atlasLocation,
            int x1,
            int x2,
            int y1,
            int y2,
            float minU,
            float maxU,
            float minV,
            float maxV,
            CallbackInfo ci
    ) {
        if (BlxckoutFXAe2Support.isAe2Texture(atlasLocation)) {
            BlxckoutFXRenderContext.enterAe2TextureRender();
        }

        if (blxckoutfx$isAe2PatternModesBackground(atlasLocation)) {
            BlxckoutFXRenderContext.enterAe2PatternModesBackgroundRender();
        }
    }

    @Inject(method = "blit(Lnet/minecraft/resources/Identifier;IIIIFFFF)V", at = @At("RETURN"), require = 0)
    private void blxckoutfx$exitAe2TextureRender(
            Identifier atlasLocation,
            int x1,
            int x2,
            int y1,
            int y2,
            float minU,
            float maxU,
            float minV,
            float maxV,
            CallbackInfo ci
    ) {
        if (blxckoutfx$isAe2PatternModesBackground(atlasLocation)) {
            BlxckoutFXRenderContext.exitAe2PatternModesBackgroundRender();
        }

        if (BlxckoutFXAe2Support.isAe2Texture(atlasLocation)) {
            BlxckoutFXRenderContext.exitAe2TextureRender();
        }
    }

    @Inject(
            method = "blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIII)V",
            at = @At("HEAD"),
            require = 0
    )
    private void blxckoutfx$enterAe2VerticalButtonsBackground(
            RenderPipeline pipeline,
            Identifier sprite,
            int x,
            int y,
            int width,
            int height,
            CallbackInfo ci
    ) {
        if (blxckoutfx$isAe2VerticalButtonsBackgroundSprite(sprite)) {
            BlxckoutFXRenderContext.enterAe2VerticalButtonBarBackgroundRender();
        }
    }

    @Inject(
            method = "blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIII)V",
            at = @At("RETURN"),
            require = 0
    )
    private void blxckoutfx$exitAe2VerticalButtonsBackground(
            RenderPipeline pipeline,
            Identifier sprite,
            int x,
            int y,
            int width,
            int height,
            CallbackInfo ci
    ) {
        if (blxckoutfx$isAe2VerticalButtonsBackgroundSprite(sprite)) {
            BlxckoutFXRenderContext.exitAe2VerticalButtonBarBackgroundRender();
        }
    }

    private static boolean blxckoutfx$isAe2VerticalButtonsBackgroundSprite(Identifier sprite) {
        return sprite != null
                && BlxckoutFXRenderContext.isRenderingAe2Screen()
                && BlxckoutFXAe2Support.isBlackoutAe2ThemeActive()
                && "ae2".equals(sprite.getNamespace())
                && AE2_VERTICAL_BUTTONS_BG_PATH.equals(sprite.getPath());
    }

    private static boolean blxckoutfx$isAe2PatternModesBackground(Identifier atlasLocation) {
        if (atlasLocation == null || !BlxckoutFXRenderContext.isRenderingAe2Screen()) {
            return false;
        }

        if (!BlxckoutFXAe2Support.isBlackoutAe2ThemeActive()) {
            return false;
        }

        if (!BlxckoutFXAe2Support.isAe2Texture(atlasLocation)) {
            return false;
        }

        String path = atlasLocation.getPath();
        return path != null && path.endsWith(AE2_PATTERN_MODES_BG_PATH);
    }
}
