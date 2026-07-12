package com.lopew.blxckoutfx.mixin.ae2;

import com.lopew.blxckoutfx.client.integration.ae2.BlxckoutFXAe2Support;
import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderPipelines;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "com.glodblock.github.extendedae.client.gui.GuiExPatternTerminal", remap = false)
public class ExtendedAePatternAccessTerminalMixin {
    @Unique
    private boolean blxckoutfx$ae2CustomRemapContextActive;

    @Inject(method = "drawBG", at = @At("HEAD"), require = 0)
    private void blxckoutfx$enterAe2CustomRemapContext(
            GuiGraphicsExtractor guiGraphics,
            int offsetX,
            int offsetY,
            int mouseX,
            int mouseY,
            float partialTick,
            CallbackInfo ci
    ) {
        if (!BlxckoutFXAe2Support.isBlackoutAe2ThemeActive()
                || !BlxckoutFXRenderContext.isRenderingAe2Screen()) {
            return;
        }

        this.blxckoutfx$ae2CustomRemapContextActive = true;
        BlxckoutFXRenderContext.enterAe2CustomRemapRender();
    }

    @Inject(method = "drawBG", at = @At("RETURN"), require = 0)
    private void blxckoutfx$exitAe2CustomRemapContext(
            GuiGraphicsExtractor guiGraphics,
            int offsetX,
            int offsetY,
            int mouseX,
            int mouseY,
            float partialTick,
            CallbackInfo ci
    ) {
        if (!this.blxckoutfx$ae2CustomRemapContextActive) {
            return;
        }

        this.blxckoutfx$ae2CustomRemapContextActive = false;
        BlxckoutFXRenderContext.exitAe2CustomRemapRender();
    }

    @ModifyArg(
            method = "blit",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blit(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIFFIIII)V"
            ),
            index = 0,
            require = 0
    )
    private RenderPipeline blxckoutfx$useBlackoutPipeline(RenderPipeline pipeline) {
        if (BlxckoutFXAe2Support.isBlackoutAe2ThemeActive()
                && BlxckoutFXRenderContext.isRenderingAe2Screen()) {
            return BlxckoutFXRenderPipelines.AE2_BLACKOUT_TEXTURED;
        }

        return pipeline;
    }
}
