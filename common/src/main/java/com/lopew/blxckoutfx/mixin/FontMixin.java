package com.lopew.blxckoutfx.mixin;

import com.lopew.blxckoutfx.BlxckoutFX;
import com.lopew.blxckoutfx.client.BlxckoutFXShaders;
import com.lopew.blxckoutfx.client.BlxckoutFXRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.util.FastColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Font.class)
public class FontMixin {
    private static boolean blxckoutfx$loggedFontPath;

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
        if (color == 0) {
            return color;
        }

        if (!BlxckoutFXShaders.isEnabled() || BlxckoutFXShaders.isBlxckoutPresetActive()) {
            return color;
        }

        Minecraft minecraft = Minecraft.getInstance();
        Screen screen = minecraft.screen;

        if (screen == null || (minecraft.level == null && !BlxckoutFXRenderContext.isRenderingButton())) {
            return color;
        }

        if (!blxckoutfx$loggedFontPath) {
            blxckoutfx$loggedFontPath = true;
            BlxckoutFX.LOGGER.info("BlxckoutFX font mixin active: screen={}, presetIndex={}, buttonRender={}, levelScreen={}",
                    screen.getClass().getName(),
                    BlxckoutFXShaders.getPresetIndex(),
                    BlxckoutFXRenderContext.isRenderingButton(),
                    minecraft.level != null);
        }

        int alpha = FastColor.ARGB32.alpha(color);
        int red = FastColor.ARGB32.red(color);
        int green = FastColor.ARGB32.green(color);
        int blue = FastColor.ARGB32.blue(color);

        int max = Math.max(red, Math.max(green, blue));
        int min = Math.min(red, Math.min(green, blue));
        int saturation = max - min;

        int maxThreshold = 120;
        int saturationThreshold = 28;

        // Lift only dark grayscale-ish text so layered panels keep readable labels.
        if (max > maxThreshold || saturation > saturationThreshold) {
            return color;
        }

        int liftFloor = 170;
        int liftBonus = 90;
        int lifted = Math.min(255, Math.max(liftFloor, max + liftBonus));
        return FastColor.ARGB32.color(alpha, lifted, lifted, lifted);
    }
}
