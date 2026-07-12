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
@Mixin(targets = "appeng.client.gui.widgets.IconButton", remap = false)
public class Ae2IconButtonMixin {
    @Unique
    private boolean blxckoutfx$ae2CustomRemapContextActive;
    @Unique
    private Boolean blxckoutfx$ae2TransferStyledButton;

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
                    ordinal = 0
            ),
            require = 0
    )
    private void blxckoutfx$forceTransferButtonHalfBackgroundRemap(@Coerce Object blitter, GuiGraphicsExtractor guiGraphics, Operation<Void> original) {
        blxckoutfx$renderTransferButtonBackground(blitter, guiGraphics, original);
    }

    @WrapOperation(
            method = "extractContents",
            at = @At(
                    value = "INVOKE",
                    target = "Lappeng/client/gui/style/Blitter;blit(Lnet/minecraft/client/gui/GuiGraphicsExtractor;)V",
                    ordinal = 2
            ),
            require = 0
    )
    private void blxckoutfx$forceTransferButtonFullBackgroundRemap(@Coerce Object blitter, GuiGraphicsExtractor guiGraphics, Operation<Void> original) {
        blxckoutfx$renderTransferButtonBackground(blitter, guiGraphics, original);
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
    private void blxckoutfx$keepAe2HalfIconReadable(@Coerce Object blitter, GuiGraphicsExtractor guiGraphics, Operation<Void> original) {
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
    private void blxckoutfx$keepAe2FullIconReadable(@Coerce Object blitter, GuiGraphicsExtractor guiGraphics, Operation<Void> original) {
        blxckoutfx$renderIconWithoutDarkening(blitter, guiGraphics, original);
    }

    @Unique
    private void blxckoutfx$renderIconWithoutDarkening(@Coerce Object blitter, GuiGraphicsExtractor guiGraphics, Operation<Void> original) {
        boolean blackoutThemeActive = BlxckoutFXAe2Support.isBlackoutAe2ThemeActive();
        boolean blxckoutThemeActive = BlxckoutFXAe2Support.isBlxckoutThemeActive() && BlxckoutFXShaders.isEnabled();
        if (blackoutThemeActive && blxckoutfx$isTransferStyledButton()) {
            BlxckoutFXRenderContext.enterAe2TransferButtonRender();
            try {
                original.call(blitter, guiGraphics);
            } finally {
                BlxckoutFXRenderContext.exitAe2TransferButtonRender();
            }
            return;
        }

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

    @Unique
    private void blxckoutfx$renderTransferButtonBackground(@Coerce Object blitter, GuiGraphicsExtractor guiGraphics, Operation<Void> original) {
        if (!blxckoutfx$shouldForceTransferButtonBackgroundRemap()) {
            original.call(blitter, guiGraphics);
            return;
        }

        BlxckoutFXRenderContext.enterAe2TransferButtonRender();
        BlxckoutFXRenderContext.enterAe2CustomRemapRender();
        try {
            original.call(blitter, guiGraphics);
        } finally {
            BlxckoutFXRenderContext.exitAe2CustomRemapRender();
            BlxckoutFXRenderContext.exitAe2TransferButtonRender();
        }
    }

    @Unique
    private boolean blxckoutfx$shouldForceTransferButtonBackgroundRemap() {
        return BlxckoutFXAe2Support.isBlackoutAe2ThemeActive()
                && BlxckoutFXRenderContext.isRenderingAe2Screen()
                && blxckoutfx$isTransferStyledButton();
    }

    @Unique
    private boolean blxckoutfx$isTransferStyledButton() {
        if (this.blxckoutfx$ae2TransferStyledButton != null) {
            return this.blxckoutfx$ae2TransferStyledButton;
        }

        try {
            Object icon = blxckoutfx$readCurrentIcon();
            String iconName = String.valueOf(icon);
            this.blxckoutfx$ae2TransferStyledButton =
                    "S_ARROW_UP".equals(iconName)
                            || "S_ARROW_DOWN".equals(iconName)
                            || "ARROW_UP".equals(iconName)
                            || "ARROW_DOWN".equals(iconName)
                            || "S_CLEAR".equals(iconName)
                            || "S_SUBSTITUTION_ENABLED".equals(iconName)
                            || "S_SUBSTITUTION_DISABLED".equals(iconName)
                            || "S_FLUID_SUBSTITUTION_ENABLED".equals(iconName)
                            || "S_FLUID_SUBSTITUTION_DISABLED".equals(iconName);
        } catch (ReflectiveOperationException ignored) {
            this.blxckoutfx$ae2TransferStyledButton = false;
        }

        return this.blxckoutfx$ae2TransferStyledButton;
    }

    @Unique
    private Object blxckoutfx$readCurrentIcon() throws ReflectiveOperationException {
        Class<?> currentClass = this.getClass();
        while (currentClass != null) {
            try {
                if ("appeng.client.gui.widgets.ActionButton".equals(currentClass.getName())) {
                    var iconField = currentClass.getDeclaredField("icon");
                    iconField.setAccessible(true);
                    return iconField.get(this);
                }

                if ("net.pedroksl.ae2addonlib.client.widgets.AddonActionButton".equals(currentClass.getName())) {
                    var iconField = currentClass.getDeclaredField("icon");
                    iconField.setAccessible(true);
                    return iconField.get(this);
                }

                if ("appeng.client.gui.widgets.ToggleButton".equals(currentClass.getName())) {
                    var stateField = currentClass.getDeclaredField("state");
                    stateField.setAccessible(true);
                    boolean state = stateField.getBoolean(this);

                    var iconField = currentClass.getDeclaredField(state ? "iconOn" : "iconOff");
                    iconField.setAccessible(true);
                    return iconField.get(this);
                }
            } catch (NoSuchFieldException ignored) {
            }
            currentClass = currentClass.getSuperclass();
        }

        return null;
    }
}
