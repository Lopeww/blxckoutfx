package com.lopew.blxckoutfx.mixin.core;

import com.lopew.blxckoutfx.client.integration.ae2.BlxckoutFXAe2Support;
import com.lopew.blxckoutfx.client.render.BlxckoutFXShaders;
import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import com.lopew.blxckoutfx.client.render.BlxckoutFXTextColors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Font.class, priority = 1)
public class FontMixin {
    @Unique
    private static final String EXTENDED_AE_TAG_STORAGE_BUS_SCREEN = "com.glodblock.github.extendedae.client.gui.GuiTagStorageBus";
    @Unique
    private static final String EXTENDED_AE_TAG_STORAGE_PLACEHOLDER_WHITELIST = "Input whitelist tags here";
    @Unique
    private static final String EXTENDED_AE_TAG_STORAGE_PLACEHOLDER_BLACKLIST = "Input blacklist tags here";
    @Unique
    private static final int EXTENDED_AE_TAG_STORAGE_PLACEHOLDER_COLOR = 0x7E7E7E;
    @Unique
    private static final ThreadLocal<String> blxckoutfx$currentStringDrawText = new ThreadLocal<>();
    @Unique
    private int blxckoutfx$ae2TextSuppressDepth;

    @Inject(
            method = "drawInternal(Ljava/lang/String;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/client/gui/Font$DisplayMode;IIZ)I",
            at = @At("HEAD")
    )
    private void blxckoutfx$suppressAe2StringTextDarkeningStart(CallbackInfoReturnable<Integer> cir) {
        if (!blxckoutfx$shouldSuppressAe2TextDarkening()) {
            return;
        }

        blxckoutfx$ae2TextSuppressDepth++;
        BlxckoutFXRenderContext.suppressTextureDarkening();
    }

    @Inject(
            method = "drawInternal(Ljava/lang/String;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/client/gui/Font$DisplayMode;IIZ)I",
            at = @At("RETURN")
    )
    private void blxckoutfx$suppressAe2StringTextDarkeningEnd(CallbackInfoReturnable<Integer> cir) {
        blxckoutfx$currentStringDrawText.remove();
        if (blxckoutfx$ae2TextSuppressDepth <= 0) {
            return;
        }

        blxckoutfx$ae2TextSuppressDepth--;
        BlxckoutFXRenderContext.allowTextureDarkening();
    }

    @Inject(
            method = "drawInternal(Lnet/minecraft/util/FormattedCharSequence;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/client/gui/Font$DisplayMode;II)I",
            at = @At("HEAD")
    )
    private void blxckoutfx$suppressAe2FormattedTextDarkeningStart(CallbackInfoReturnable<Integer> cir) {
        blxckoutfx$currentStringDrawText.remove();
        if (!blxckoutfx$shouldSuppressAe2TextDarkening()) {
            return;
        }

        blxckoutfx$ae2TextSuppressDepth++;
        BlxckoutFXRenderContext.suppressTextureDarkening();
    }

    @Inject(
            method = "drawInternal(Lnet/minecraft/util/FormattedCharSequence;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/client/gui/Font$DisplayMode;II)I",
            at = @At("RETURN")
    )
    private void blxckoutfx$suppressAe2FormattedTextDarkeningEnd(CallbackInfoReturnable<Integer> cir) {
        if (blxckoutfx$ae2TextSuppressDepth <= 0) {
            return;
        }

        blxckoutfx$ae2TextSuppressDepth--;
        BlxckoutFXRenderContext.allowTextureDarkening();
    }

    @ModifyVariable(
            method = "drawInternal(Ljava/lang/String;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/client/gui/Font$DisplayMode;IIZ)I",
            at = @At("HEAD"),
            argsOnly = true,
            index = 4
    )
    private int blxckoutfx$adjustStringDrawColor(int color) {
        return adjustDarkTextReadability(color);
    }

    @ModifyVariable(
            method = "drawInternal(Ljava/lang/String;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/client/gui/Font$DisplayMode;IIZ)I",
            at = @At("HEAD"),
            argsOnly = true,
            index = 1
    )
    private String blxckoutfx$captureStringDrawText(String text) {
        blxckoutfx$currentStringDrawText.set(text);
        return text;
    }

    @ModifyVariable(
            method = "drawInternal(Lnet/minecraft/util/FormattedCharSequence;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/client/gui/Font$DisplayMode;II)I",
            at = @At("HEAD"),
            argsOnly = true,
            index = 4
    )
    private int blxckoutfx$adjustFormattedDrawColor(int color) {
        return adjustDarkTextReadability(color);
    }

    private static int adjustDarkTextReadability(int color) {
        Minecraft minecraft = Minecraft.getInstance();
        Screen screen = minecraft.screen;
        boolean isPatchouliBookScreen = isPatchouliBookScreen(screen);

        if (BlxckoutFXRenderContext.isRenderingAe2StackSizeLabel()) {
            
            return color;
        }

        if (BlxckoutFXRenderContext.isRenderingAe2WarningText()) {
            return color;
        }

        if (!BlxckoutFXRenderContext.isRenderingInventoryButton() && BlxckoutFXAe2Support.isAe2Screen(screen)) {
            if (BlxckoutFXRenderContext.isRenderingExtendedAeTagPlaceholder()
                    && BlxckoutFXAe2Support.isBlackoutAe2ThemeActive()) {
                return blxckoutfx$withOriginalAlphaOrOpaque(color, EXTENDED_AE_TAG_STORAGE_PLACEHOLDER_COLOR);
            }

            if (blxckoutfx$isExtendedAeTagStoragePlaceholder(screen, blxckoutfx$currentStringDrawText.get())
                    && BlxckoutFXAe2Support.isBlackoutAe2ThemeActive()) {
                return blxckoutfx$withOriginalAlphaOrOpaque(color, EXTENDED_AE_TAG_STORAGE_PLACEHOLDER_COLOR);
            }

            if (blxckoutfx$isAe2WarningColor(color)) {
                return color;
            }

            if (!BlxckoutFXAe2Support.isAe2ThemeEnabled()) {
                
                return color;
            }

            if (BlxckoutFXAe2Support.isBlackoutAe2ThemeActive()) {
                if (BlxckoutFXRenderContext.isRenderingAe2SearchField()) {
                    return BlxckoutFXTextColors.adjustAe2BlackoutSearchTextColor(color);
                }
                return BlxckoutFXTextColors.adjustAe2BlackoutTextColor(color);
            }
            if (BlxckoutFXAe2Support.isBlxckoutAe2ThemeActive()) {
                
                
                
                
                if (!BlxckoutFXShaders.isEnabled()) {
                    return color;
                }
                if (BlxckoutFXShaders.isBlxckoutPresetActive()) {
                    return BlxckoutFXTextColors.adjustAe2BlxckoutTextColor(color);
                }
                return BlxckoutFXTextColors.adjustAe2GrayTextColor(color);
            }
        }

        if (isPneumaticCraftScreen(screen)) {
            return color;
        }

        if (color == 0 && !isPatchouliBookScreen) {
            return color;
        }

        if (!BlxckoutFXShaders.isEnabled()) {
            return color;
        }

        if (isPatchouliBookScreen && BlxckoutFXShaders.isSoftPresetActive()) {
            return color;
        }

        if (BlxckoutFXShaders.isBlxckoutPresetActive() && !isPatchouliBookScreen) {
            return color;
        }

        if (screen == null || (minecraft.level == null && !BlxckoutFXRenderContext.isRenderingButton())) {
            return color;
        }

        return BlxckoutFXTextColors.adjustGeneralTextColor(color, isPatchouliBookScreen, isPatchouliBookScreen);
    }

    private static boolean isPatchouliBookScreen(Screen screen) {
        return screen != null && screen.getClass().getName().startsWith("vazkii.patchouli.client.book.gui.");
    }

    private static boolean isPneumaticCraftScreen(Screen screen) {
        return screen != null && screen.getClass().getName().startsWith("me.desht.pneumaticcraft.client.gui.");
    }

    @Unique
    private static boolean blxckoutfx$isExtendedAeTagStoragePlaceholder(Screen screen, String text) {
        return screen != null
                && EXTENDED_AE_TAG_STORAGE_BUS_SCREEN.equals(screen.getClass().getName())
                && (EXTENDED_AE_TAG_STORAGE_PLACEHOLDER_WHITELIST.equals(text)
                || EXTENDED_AE_TAG_STORAGE_PLACEHOLDER_BLACKLIST.equals(text));
    }

    @Unique
    private static boolean blxckoutfx$isAe2WarningColor(int color) {
        int rgb = color & 0xFFFFFF;
        return rgb == 0xFC0000 || rgb == 0x3E0000;
    }

    @Unique
    private static int blxckoutfx$withOriginalAlphaOrOpaque(int originalColor, int rgb) {
        int alpha = (originalColor >>> 24) & 0xFF;
        if (alpha == 0 && (originalColor & 0xFFFFFF) != 0) {
            alpha = 255;
        }
        return (alpha << 24) | (rgb & 0xFFFFFF);
    }

    @Unique
    private static boolean blxckoutfx$shouldSuppressAe2TextDarkening() {
        Screen screen = Minecraft.getInstance().screen;
        return !BlxckoutFXRenderContext.isRenderingInventoryButton()
                && BlxckoutFXAe2Support.isAe2ThemeEnabled()
                && BlxckoutFXAe2Support.isAe2Screen(screen);
    }
}

