package com.lopew.blxckoutfx.mixin.ae2;

import com.lopew.blxckoutfx.client.integration.ae2.BlxckoutFXAe2Support;
import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderPipelines;
import com.lopew.blxckoutfx.client.render.BlxckoutFXShaders;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Pseudo
@Mixin(targets = "appeng.client.gui.style.Blitter", remap = false)
public class Ae2BlitterMixin {
    @Unique
    private static final String AE2_STATES_TEXTURE_PATH = "textures/guis/states.png";
    @Unique
    private static final String AE2_EXTRA_PANELS_TEXTURE_PATH = "textures/guis/extra_panels.png";
    @Unique
    private static final String AE2_PATTERN_MODES_TEXTURE_PATH = "textures/guis/pattern_modes.png";
    @Unique
    private static final String AE2_WIRELESS_ACCESS_POINT_TEXTURE_PATH = "textures/guis/wireless_access_point.png";
    @Unique
    private static final String AE2_WIRELESS_HUB_TEXTURE_PATH = "textures/guis/wireless_hub.png";

    @ModifyArg(
            method = "blit(Lnet/minecraft/client/gui/GuiGraphicsExtractor;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/state/gui/BlitRenderState;<init>(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/client/gui/render/TextureSetup;Lorg/joml/Matrix3x2f;IIIIFFFFILnet/minecraft/client/gui/navigation/ScreenRectangle;)V"
            ),
            index = 0,
            require = 0
    )
    private RenderPipeline blxckoutfx$useContentIconPipelineForSuppressedAe2Blits(RenderPipeline pipeline) {
        if (BlxckoutFXRenderContext.isRenderingAe2TransferButton()
                && BlxckoutFXAe2Support.isBlackoutAe2ThemeActive()) {
            return BlxckoutFXRenderPipelines.AE2_TRANSFER_BUTTON_TEXTURED;
        }

        if (BlxckoutFXRenderContext.isRenderingAe2Icon()
                && BlxckoutFXAe2Support.isBlackoutAe2ThemeActive()) {
            return BlxckoutFXRenderPipelines.CONTENT_ICON_TEXTURED;
        }

        if (BlxckoutFXRenderContext.isRenderingAe2CustomRemap()
                && BlxckoutFXAe2Support.isBlackoutAe2ThemeActive()) {
            return BlxckoutFXRenderPipelines.AE2_BLACKOUT_TEXTURED;
        }

        if (BlxckoutFXAe2Support.isBlackoutAe2ThemeActive()
                && BlxckoutFXRenderContext.isRenderingAe2Screen()
                && blxckoutfx$isAe2BlackoutSlotBackgroundBlit()) {
            return BlxckoutFXRenderPipelines.AE2_BLACKOUT_TEXTURED;
        }

        if (BlxckoutFXRenderContext.isTextureDarkeningSuppressed()
                && BlxckoutFXAe2Support.isBlxckoutThemeActive()
                && BlxckoutFXShaders.isEnabled()) {
            return BlxckoutFXRenderPipelines.CONTENT_ICON_TEXTURED;
        }

        return pipeline;
    }

    @Unique
    private boolean blxckoutfx$isAe2BlackoutSlotBackgroundBlit() {
        Identifier texture = blxckoutfx$readField("texture", Identifier.class);
        if (texture == null || !"ae2".equals(texture.getNamespace())) {
            return false;
        }

        String path = texture.getPath();
        if (AE2_EXTRA_PANELS_TEXTURE_PATH.equals(path)) {
            return true;
        }

        Rect2i srcRect = blxckoutfx$readField("srcRect", Rect2i.class);
        if (AE2_PATTERN_MODES_TEXTURE_PATH.equals(path)) {
            return blxckoutfx$matchesRect(srcRect, 0, 0, 124, 66)
                    || blxckoutfx$matchesRect(srcRect, 0, 70, 124, 66)
                    || blxckoutfx$matchesRect(srcRect, 128, 70, 124, 66)
                    || blxckoutfx$matchesRect(srcRect, 0, 140, 124, 66)
                    || blxckoutfx$matchesRect(srcRect, 124, 140, 20, 22)
                    || blxckoutfx$matchesRect(srcRect, 124, 162, 20, 22)
                    || blxckoutfx$matchesRect(srcRect, 124, 184, 20, 22);
        }

        if (AE2_WIRELESS_ACCESS_POINT_TEXTURE_PATH.equals(path)) {
            return blxckoutfx$matchesRect(srcRect, 203, 0, 31, 92);
        }

        if (AE2_WIRELESS_HUB_TEXTURE_PATH.equals(path)) {
            return blxckoutfx$matchesRect(srcRect, 176, 0, 16, 16);
        }

        if (!AE2_STATES_TEXTURE_PATH.equals(path)) {
            return false;
        }

        return blxckoutfx$matchesRect(srcRect, 192, 192, 18, 18)
                || blxckoutfx$matchesRect(srcRect, 240, 0, 16, 16)
                || blxckoutfx$matchesRect(srcRect, 224, 0, 16, 16)
                || blxckoutfx$matchesRect(srcRect, 240, 208, 16, 16)
                || blxckoutfx$matchesRect(srcRect, 240, 192, 16, 16);
    }

    @Unique
    private <T> T blxckoutfx$readField(String fieldName, Class<T> fieldType) {
        Class<?> currentClass = this.getClass();
        while (currentClass != null) {
            try {
                var field = currentClass.getDeclaredField(fieldName);
                field.setAccessible(true);
                Object value = field.get(this);
                return fieldType.isInstance(value) ? fieldType.cast(value) : null;
            } catch (ReflectiveOperationException ignored) {
                currentClass = currentClass.getSuperclass();
            }
        }

        return null;
    }

    @Unique
    private static boolean blxckoutfx$matchesRect(Rect2i rect, int x, int y, int width, int height) {
        return rect != null
                && rect.getX() == x
                && rect.getY() == y
                && rect.getWidth() == width
                && rect.getHeight() == height;
    }
}
