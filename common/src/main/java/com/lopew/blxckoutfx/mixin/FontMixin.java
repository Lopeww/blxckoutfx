package com.lopew.blxckoutfx.mixin;

import com.lopew.blxckoutfx.client.BlxckoutFXShaders;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.util.FastColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Font.class)
public class FontMixin {
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
            method = "drawInternal(Lnet/minecraft/util/FormattedCharSequence;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/client/gui/Font$DisplayMode;II)I",
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

        boolean balancedActive = BlxckoutFXShaders.isBalancedPresetActive();
        boolean darkActive = BlxckoutFXShaders.isDarkPresetActive();

        if (!BlxckoutFXShaders.isEnabled() || (!balancedActive && !darkActive)) {
            return color;
        }

        if (Minecraft.getInstance().screen == null) {
            return color;
        }

        int alpha = FastColor.ARGB32.alpha(color);
        int red = FastColor.ARGB32.red(color);
        int green = FastColor.ARGB32.green(color);
        int blue = FastColor.ARGB32.blue(color);

        int max = Math.max(red, Math.max(green, blue));
        int min = Math.min(red, Math.min(green, blue));
        int saturation = max - min;

        int maxThreshold = darkActive ? 165 : 120;
        int saturationThreshold = darkActive ? 44 : 28;

        // Lift only dark grayscale-ish text so layered panels keep readable labels.
        if (max > maxThreshold || saturation > saturationThreshold) {
            return color;
        }

        int liftFloor = darkActive ? 210 : 170;
        int liftBonus = darkActive ? 120 : 90;
        int lifted = Math.min(255, Math.max(liftFloor, max + liftBonus));
        return FastColor.ARGB32.color(alpha, lifted, lifted, lifted);
    }
}
