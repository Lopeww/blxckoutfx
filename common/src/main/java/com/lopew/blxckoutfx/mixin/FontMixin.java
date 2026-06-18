package com.lopew.blxckoutfx.mixin;

import com.lopew.blxckoutfx.client.BlxckoutFXShaders;
import com.lopew.blxckoutfx.client.BlxckoutFXRenderContext;
import com.lopew.blxckoutfx.client.BlxckoutFXTextColors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Font.class)
public class FontMixin {
    @ModifyVariable(
            method = {
                    "drawInternal(Ljava/lang/String;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/client/gui/Font$DisplayMode;IIZ)I",
                    "m_271880_(Ljava/lang/String;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/client/gui/Font$DisplayMode;IIZ)I"
            },
            at = @At("HEAD"),
            argsOnly = true,
            index = 4
    )
    private int blxckoutfx$adjustStringDrawColor(int color) {
        return adjustDarkTextReadability(color);
    }

    @ModifyVariable(
            method = {
                    "drawInternal(Lnet/minecraft/util/FormattedCharSequence;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/client/gui/Font$DisplayMode;II)I",
                    "m_272085_(Lnet/minecraft/util/FormattedCharSequence;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/client/gui/Font$DisplayMode;II)I"
            },
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

        if (screen == null || !BlxckoutFXShaders.isEnabled()) {
            return color;
        }

        boolean isPatchouliBookScreen = isPatchouliBookScreen(screen);

        if (color == 0 && !isPatchouliBookScreen) {
            return color;
        }

        if (isPneumaticCraftScreen(screen)) {
            return color;
        }

        if (isPatchouliBookScreen && BlxckoutFXShaders.isSoftPresetActive()) {
            return color;
        }

        if (BlxckoutFXShaders.isBlxckoutPresetActive() && !isPatchouliBookScreen) {
            return color;
        }

        if (minecraft.level == null && !BlxckoutFXRenderContext.isRenderingButton()) {
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
}
