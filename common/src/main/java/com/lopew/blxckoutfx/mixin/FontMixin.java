package com.lopew.blxckoutfx.mixin;

import com.lopew.blxckoutfx.client.BlxckoutFXShaders;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ARGB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(targets = "net.minecraft.client.gui.Font$PreparedTextBuilder")
public class FontMixin {
    @ModifyVariable(method = "<init>(FFIZZ)V", at = @At("HEAD"), argsOnly = true, ordinal = 0, require = 0)
    private int blxckoutfx$adjustSimpleConstructorColor(int color) {
        return adjustDarkTextReadability(color);
    }

    @ModifyVariable(method = "<init>(FFIIZZ)V", at = @At("HEAD"), argsOnly = true, ordinal = 0, require = 0)
    private int blxckoutfx$adjustBackgroundConstructorColor(int color) {
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

        int alpha = ARGB.alpha(color);
        int red = ARGB.red(color);
        int green = ARGB.green(color);
        int blue = ARGB.blue(color);

        int max = Math.max(red, Math.max(green, blue));
        int min = Math.min(red, Math.min(green, blue));
        int saturation = max - min;

        int maxThreshold = darkActive ? 165 : 120;
        int saturationThreshold = darkActive ? 44 : 28;

        if (max > maxThreshold || saturation > saturationThreshold) {
            return color;
        }

        int liftFloor = darkActive ? 210 : 170;
        int liftBonus = darkActive ? 120 : 90;
        int lifted = Math.min(255, Math.max(liftFloor, max + liftBonus));
        return ARGB.color(alpha, lifted, lifted, lifted);
    }
}
