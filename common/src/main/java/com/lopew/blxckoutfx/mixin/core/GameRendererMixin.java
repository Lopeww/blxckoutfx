package com.lopew.blxckoutfx.mixin.core;

import com.lopew.blxckoutfx.client.render.BlxckoutFXShaders;
import com.lopew.blxckoutfx.client.integration.ae2.BlxckoutFXAe2Support;
import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Inject(method = "getPositionTexShader", at = @At("HEAD"), cancellable = true)
    private static void blxckoutfx$getPositionTexShader(CallbackInfoReturnable<ShaderInstance> cir) {
        ShaderInstance shader = BlxckoutFXAe2Support.isBlxckoutAe2ThemeActive()
                ? BlxckoutFXShaders.getBlxckoutPositionTexShader()
                : BlxckoutFXShaders.getPositionTexShader();

        if (shouldApply() && shader != null) {
            BlxckoutFXShaders.applyCurrentPreset(shader);
            cir.setReturnValue(shader);
        }
    }

    @Inject(method = "getPositionTexColorShader", at = @At("HEAD"), cancellable = true)
    private static void blxckoutfx$getPositionTexColorShader(CallbackInfoReturnable<ShaderInstance> cir) {
        ShaderInstance shader = BlxckoutFXAe2Support.isBlxckoutAe2ThemeActive()
                ? BlxckoutFXShaders.getBlxckoutPositionTexColorShader()
                : BlxckoutFXShaders.getPositionTexColorShader();

        if (shouldApply() && shader != null) {
            BlxckoutFXShaders.applyCurrentPreset(shader);
            cir.setReturnValue(shader);
        }
    }

    @Inject(method = "getPositionColorShader", at = @At("HEAD"), cancellable = true)
    private static void blxckoutfx$getPositionColorShader(CallbackInfoReturnable<ShaderInstance> cir) {
        ShaderInstance shader = BlxckoutFXAe2Support.isBlxckoutAe2ThemeActive()
                ? BlxckoutFXShaders.getBlxckoutPositionColorShader()
                : BlxckoutFXShaders.getPositionColorShader();

        if (shouldApply() && shouldApplyPositionColor() && shader != null) {
            BlxckoutFXShaders.applyCurrentPreset(shader);
            cir.setReturnValue(shader);
        }
    }

    private static boolean shouldApply() {
        Minecraft minecraft = Minecraft.getInstance();
        Screen screen = minecraft.screen;
        if (screen == null) {
            return false;
        }

        boolean isAe2Screen = BlxckoutFXAe2Support.isAe2Screen(screen);
        boolean renderingInventoryButton = BlxckoutFXRenderContext.isRenderingInventoryButton();

        if (isAe2Screen && !renderingInventoryButton && !BlxckoutFXAe2Support.isAe2ThemeEnabled()) {
            return BlxckoutFXShaders.isEnabled()
                    && BlxckoutFXShaders.shouldApplyToScreen(screen)
                    && !BlxckoutFXRenderContext.isTextureDarkeningSuppressed()
                    && (BlxckoutFXRenderContext.isRenderingJeiWidget()
                    || BlxckoutFXRenderContext.isRenderingJeiRecipeLayout()
                    || BlxckoutFXRenderContext.isRenderingJeiInformationRecipe());
        }

        boolean shaderEnabled = BlxckoutFXShaders.isEnabled()
                || (isAe2Screen && !renderingInventoryButton && BlxckoutFXAe2Support.isAe2ThemeEnabled());

        return shaderEnabled
                && BlxckoutFXShaders.shouldApplyToScreen(screen)
                && !BlxckoutFXRenderContext.isTextureDarkeningSuppressed()
                && (minecraft.level != null
                || BlxckoutFXRenderContext.isRenderingButton()
                || BlxckoutFXRenderContext.isRenderingModMenuIconButton()
                || (BlxckoutFXRenderContext.isRenderingAe2Screen() && !renderingInventoryButton));
    }

    private static boolean shouldApplyPositionColor() {
        Screen screen = Minecraft.getInstance().screen;
        if (screen != null
                && BlxckoutFXAe2Support.isAe2Screen(screen)
                && BlxckoutFXAe2Support.isAe2ThemeEnabled()
                && !BlxckoutFXRenderContext.isRenderingInventoryButton()) {
            return true;
        }

        return BlxckoutFXShaders.isBlxckoutPresetActive();
    }
}

