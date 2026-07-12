package com.lopew.blxckoutfx.mixin.journeymap;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Pseudo
@Mixin(targets = "journeymap.client.ui.theme.ThemeButton", remap = false)
public class JourneyMapThemeButtonMixin {
    private static final String JOURNEYMAP_FULLSCREEN_CLASS = "journeymap.client.ui.fullscreen.Fullscreen";
    private static final String JOURNEYMAP_THEME_LOADER_CLASS = "journeymap.client.io.ThemeLoader";
    private static final String PURIST_THEME_NAME = "Purist";
    private static final String DRAW_COLORED_IMAGE_TARGET =
            "Ljourneymap/client/render/draw/DrawUtil;drawColoredImage(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/renderer/texture/DynamicTexture;IFDDFD)V";

    @Unique
    private int blxckoutfx$suppressThemeButtonDarkeningDepth;

    @Inject(method = "renderWidget", at = @At("HEAD"), require = 0)
    private void blxckoutfx$enterRenderWidgetDarkeningNamed(
            GuiGraphics guiGraphics,
            int mouseX,
            int mouseY,
            float partialTick,
            CallbackInfo ci
    ) {
        blxckoutfx$enterThemeButtonDarkeningSuppression();
    }

    @Inject(method = "renderWidget", at = @At("RETURN"), require = 0)
    private void blxckoutfx$exitRenderWidgetDarkeningNamed(
            GuiGraphics guiGraphics,
            int mouseX,
            int mouseY,
            float partialTick,
            CallbackInfo ci
    ) {
        blxckoutfx$exitThemeButtonDarkeningSuppression();
    }

    @Inject(method = "m_88315_", at = @At("HEAD"), require = 0)
    private void blxckoutfx$enterRenderWidgetDarkeningObf(
            GuiGraphics guiGraphics,
            int mouseX,
            int mouseY,
            float partialTick,
            CallbackInfo ci
    ) {
        blxckoutfx$enterThemeButtonDarkeningSuppression();
    }

    @Inject(method = "m_88315_", at = @At("RETURN"), require = 0)
    private void blxckoutfx$exitRenderWidgetDarkeningObf(
            GuiGraphics guiGraphics,
            int mouseX,
            int mouseY,
            float partialTick,
            CallbackInfo ci
    ) {
        blxckoutfx$exitThemeButtonDarkeningSuppression();
    }

    @WrapOperation(
            method = "renderWidget",
            at = @At(value = "INVOKE", target = DRAW_COLORED_IMAGE_TARGET, ordinal = 1),
            require = 1
    )
    private void blxckoutfx$renderJourneyMapIconOverlayNoDarkening(
            GuiGraphics guiGraphics,
            DynamicTexture texture,
            int color,
            float alpha,
            double x,
            double y,
            float scale,
            double z,
            Operation<Void> original
    ) {
        blxckoutfx$renderIconWithoutDarkening(original, guiGraphics, texture, color, alpha, x, y, scale, z);
    }

    @WrapOperation(
            method = "renderWidget",
            at = @At(value = "INVOKE", target = DRAW_COLORED_IMAGE_TARGET, ordinal = 2),
            require = 1
    )
    private void blxckoutfx$renderJourneyMapIconMainNoDarkening(
            GuiGraphics guiGraphics,
            DynamicTexture texture,
            int color,
            float alpha,
            double x,
            double y,
            float scale,
            double z,
            Operation<Void> original
    ) {
        blxckoutfx$renderIconWithoutDarkening(original, guiGraphics, texture, color, alpha, x, y, scale, z);
    }

    @Unique
    private static boolean blxckoutfx$shouldBypassThemeButtonDarkening() {
        Screen screen = Minecraft.getInstance().screen;
        if (screen == null || !JOURNEYMAP_FULLSCREEN_CLASS.equals(screen.getClass().getName())) {
            return false;
        }

        return !PURIST_THEME_NAME.equals(blxckoutfx$getCurrentThemeName());
    }

    @Unique
    private static String blxckoutfx$getCurrentThemeName() {
        try {
            Class<?> themeLoaderClass = Class.forName(JOURNEYMAP_THEME_LOADER_CLASS);
            Method getCurrentTheme = themeLoaderClass.getMethod("getCurrentTheme");
            Object theme = getCurrentTheme.invoke(null);
            return blxckoutfx$getThemeName(theme);
        } catch (ReflectiveOperationException | LinkageError exception) {
            return "";
        }
    }

    @Unique
    private static String blxckoutfx$getThemeName(Object theme) {
        if (theme == null) {
            return "";
        }

        try {
            Field nameField = theme.getClass().getField("name");
            Object value = nameField.get(theme);
            if (value != null) {
                return value.toString();
            }
        } catch (ReflectiveOperationException | LinkageError ignored) {
            return String.valueOf(theme);
        }

        return String.valueOf(theme);
    }

    @Unique
    private void blxckoutfx$enterThemeButtonDarkeningSuppression() {
        if (!blxckoutfx$shouldBypassThemeButtonDarkening()) {
            return;
        }

        if (blxckoutfx$suppressThemeButtonDarkeningDepth++ == 0) {
            BlxckoutFXRenderContext.suppressTextureDarkening();
        }
    }

    @Unique
    private void blxckoutfx$exitThemeButtonDarkeningSuppression() {
        if (blxckoutfx$suppressThemeButtonDarkeningDepth <= 0) {
            return;
        }

        blxckoutfx$suppressThemeButtonDarkeningDepth--;
        if (blxckoutfx$suppressThemeButtonDarkeningDepth == 0) {
            BlxckoutFXRenderContext.allowTextureDarkening();
        }
    }

    @Unique
    private void blxckoutfx$renderIconWithoutDarkening(
            Operation<Void> original,
            GuiGraphics guiGraphics,
            DynamicTexture texture,
            int color,
            float alpha,
            double x,
            double y,
            float scale,
            double z
    ) {
        if (!blxckoutfx$isJourneyMapFullscreen()) {
            original.call(guiGraphics, texture, color, alpha, x, y, scale, z);
            return;
        }

        BlxckoutFXRenderContext.suppressTextureDarkening();
        try {
            original.call(guiGraphics, texture, color, alpha, x, y, scale, z);
        } finally {
            BlxckoutFXRenderContext.allowTextureDarkening();
        }
    }

    @Unique
    private static boolean blxckoutfx$isJourneyMapFullscreen() {
        Screen screen = Minecraft.getInstance().screen;
        return screen != null && JOURNEYMAP_FULLSCREEN_CLASS.equals(screen.getClass().getName());
    }
}
