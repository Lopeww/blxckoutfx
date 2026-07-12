package com.lopew.blxckoutfx.mixin.ae2;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.lopew.blxckoutfx.client.integration.ae2.BlxckoutFXAe2Support;
import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import com.lopew.blxckoutfx.client.render.BlxckoutFXShaders;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "net.pedroksl.ae2addonlib.client.widgets.AddonIconButton", remap = false)
public class Ae2AddonIconMixin {
    @Unique
    private boolean blxckoutfx$ae2CustomRemapContextActive;

    @Inject(method = "extractContents", at = @At("HEAD"), require = 0)
    private void blxckoutfx$enterAe2CustomRemapContext(
            GuiGraphicsExtractor guiGraphics,
            int mouseX,
            int mouseY,
            float partialTick,
            CallbackInfo ci
    ) {
        if (!BlxckoutFXAe2Support.isBlackoutAe2ThemeActive()) {
            return;
        }

        this.blxckoutfx$ae2CustomRemapContextActive = true;
        BlxckoutFXRenderContext.enterAe2CustomRemapRender();
    }

    @Inject(method = "extractContents", at = @At("RETURN"), require = 0)
    private void blxckoutfx$exitAe2CustomRemapContext(
            GuiGraphicsExtractor guiGraphics,
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

    @WrapOperation(
            method = "extractContents",
            at = @At(
                    value = "INVOKE",
                    target = "Lappeng/client/gui/style/Blitter;blit(Lnet/minecraft/client/gui/GuiGraphicsExtractor;)V",
                    ordinal = 1
            ),
            require = 0
    )
    private void blxckoutfx$keepAddonPrimaryIconReadable(@Coerce Object blitter, GuiGraphicsExtractor guiGraphics, Operation<Void> original) {
        blxckoutfx$renderIconWithoutDarkening(blitter, guiGraphics, original);
    }

    @WrapOperation(
            method = "extractContents",
            at = @At(
                    value = "INVOKE",
                    target = "Lappeng/client/gui/style/Blitter;blit(Lnet/minecraft/client/gui/GuiGraphicsExtractor;)V",
                    ordinal = 3
            ),
            require = 0
    )
    private void blxckoutfx$keepAddonSecondaryIconReadable(@Coerce Object blitter, GuiGraphicsExtractor guiGraphics, Operation<Void> original) {
        blxckoutfx$renderIconWithoutDarkening(blitter, guiGraphics, original);
    }

    @Unique
    private void blxckoutfx$renderIconWithoutDarkening(@Coerce Object blitter, GuiGraphicsExtractor guiGraphics, Operation<Void> original) {
        boolean blackoutThemeActive = BlxckoutFXAe2Support.isBlackoutAe2ThemeActive();
        boolean blxckoutThemeActive = BlxckoutFXAe2Support.isBlxckoutThemeActive() && BlxckoutFXShaders.isEnabled();
        if (!blackoutThemeActive && !blxckoutThemeActive) {
            original.call(blitter, guiGraphics);
            return;
        }

        BlxckoutFXRenderContext.enterAe2IconRender();
        BlxckoutFXRenderContext.suppressTextureDarkening();
        try {
            original.call(blitter, guiGraphics);
        } finally {
            BlxckoutFXRenderContext.allowTextureDarkening();
            BlxckoutFXRenderContext.exitAe2IconRender();
        }
    }
}
