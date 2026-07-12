package com.lopew.blxckoutfx.mixin.core;

import com.lopew.blxckoutfx.BlxckoutFX;
import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import com.lopew.blxckoutfx.client.render.BlxckoutFXShaders;
import com.lopew.blxckoutfx.client.screen.BlxckoutFXScreenExclusions;
import com.lopew.blxckoutfx.client.integration.ae2.BlxckoutFXAe2Support;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Unique
    private static boolean blxckoutfx$loggedShaderHook;

    @Inject(method = "getPositionTexShader", at = @At("HEAD"), cancellable = true)
    private static void blxckoutfx$getPositionTexShader(CallbackInfoReturnable<ShaderInstance> cir) {
        ShaderInstance shader = BlxckoutFXShaders.getPositionTexShader();
        if (shouldApply() && shader != null) {
            BlxckoutFXShaders.applyCurrentPreset(shader);
            cir.setReturnValue(shader);
        }
    }

    @Inject(method = "getPositionTexColorShader", at = @At("HEAD"), cancellable = true)
    private static void blxckoutfx$getPositionTexColorShader(CallbackInfoReturnable<ShaderInstance> cir) {
        ShaderInstance shader = BlxckoutFXShaders.getPositionTexColorShader();
        if (shouldApply() && shader != null) {
            BlxckoutFXShaders.applyCurrentPreset(shader);
            cir.setReturnValue(shader);
        }
    }

    @Inject(method = "getPositionColorShader", at = @At("HEAD"), cancellable = true)
    private static void blxckoutfx$getPositionColorShader(CallbackInfoReturnable<ShaderInstance> cir) {
        ShaderInstance shader = BlxckoutFXShaders.getPositionColorShader();
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
        boolean renderingJei = BlxckoutFXRenderContext.isRenderingJeiWidget()
                || BlxckoutFXRenderContext.isRenderingJeiRecipeLayout()
                || BlxckoutFXRenderContext.isRenderingJeiInformationRecipe();

        boolean shaderEnabled = BlxckoutFXShaders.isEnabled();

        boolean buttonOnlyScreen = BlxckoutFXScreenExclusions.isButtonOnlyScreen(screen);

        boolean apply = shaderEnabled
                && BlxckoutFXShaders.shouldApplyToScreen(screen)
                && !BlxckoutFXRenderContext.isTextureDarkeningSuppressed()
                && (BlxckoutFXRenderContext.isRenderingButton()
                || (!buttonOnlyScreen && (minecraft.level != null
                || (BlxckoutFXRenderContext.isRenderingAe2Screen() && !renderingInventoryButton))));

        if (apply && !blxckoutfx$loggedShaderHook) {
            blxckoutfx$loggedShaderHook = true;
            BlxckoutFX.LOGGER.info("BlxckoutFX shader hook active");
        }

        return apply;
    }

    private static boolean shouldApplyPositionColor() {
        Screen screen = Minecraft.getInstance().screen;
        if (screen != null
                && BlxckoutFXAe2Support.isAe2Screen(screen)
                && BlxckoutFXAe2Support.isBlxckoutAe2ThemeActive()
                && !BlxckoutFXRenderContext.isRenderingInventoryButton()) {
            return true;
        }

        return BlxckoutFXShaders.isBlxckoutPresetActive();
    }
}
