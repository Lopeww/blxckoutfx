package com.lopew.blxckoutfx.mixin;

import com.lopew.blxckoutfx.client.BlxckoutFXShaders;
import com.lopew.blxckoutfx.client.BlxckoutFXRenderContext;
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

        if (shouldApply() && BlxckoutFXShaders.isBlxckoutPresetActive() && shader != null) {
            BlxckoutFXShaders.applyCurrentPreset(shader);
            cir.setReturnValue(shader);
        }
    }

    private static boolean shouldApply() {
        Minecraft minecraft = Minecraft.getInstance();
        Screen screen = minecraft.screen;

        return screen != null
                && BlxckoutFXShaders.isEnabled()
                && !BlxckoutFXRenderContext.isTextureDarkeningSuppressed()
                && (minecraft.level != null || BlxckoutFXRenderContext.isRenderingButton());
    }
}
