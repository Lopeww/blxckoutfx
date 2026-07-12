package com.lopew.blxckoutfx.mixin.core;

import com.lopew.blxckoutfx.client.BlxckoutFXRenderContext;
import com.lopew.blxckoutfx.client.BlxckoutFXShaders;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldVertexBufferUploader.class)
public class WorldVertexBufferUploaderMixin {
    @Unique
    private boolean blxckoutfx$shaderBound;

    @Inject(method = "draw", at = @At("HEAD"), require = 1)
    private void blxckoutfx$bindDarkeningShader(BufferBuilder buffer, CallbackInfo ci) {
        this.blxckoutfx$shaderBound = false;
        if (shouldDarken()) {
            this.blxckoutfx$shaderBound = BlxckoutFXShaders.bindForFormat(buffer.getVertexFormat());
        }
    }

    @Inject(method = "draw", at = @At("RETURN"), require = 1)
    private void blxckoutfx$unbindDarkeningShader(BufferBuilder buffer, CallbackInfo ci) {
        if (this.blxckoutfx$shaderBound) {
            this.blxckoutfx$shaderBound = false;
            BlxckoutFXShaders.unbind();
        }
    }

    private static boolean shouldDarken() {
        if (!BlxckoutFXRenderContext.isDarkeningActiveScreen() || !BlxckoutFXShaders.isEnabled()) {
            return false;
        }
        if (BlxckoutFXRenderContext.isTextureDarkeningSuppressed()) {
            return false;
        }

        GuiScreen screen = Minecraft.getMinecraft().currentScreen;
        if (screen == null || !BlxckoutFXShaders.shouldApplyToScreen(screen)) {
            return false;
        }

        if (BlxckoutFXRenderContext.isRenderingButton()) {
            return true;
        }

        return Minecraft.getMinecraft().world != null && !com.lopew.blxckoutfx.client.BlxckoutFXScreenExclusions.isButtonOnlyScreen(screen);
    }
}
