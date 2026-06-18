package com.lopew.blxckoutfx.mixin;

import com.lopew.blxckoutfx.client.BlxckoutFXShaders;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TextColor;
import net.minecraft.util.ARGB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.client.gui.Font$PreparedTextBuilder")
public class FontMixin {
    private static final int READABLE_TEXT_BRIGHTNESS = 184;

    @ModifyVariable(method = "<init>(FFIZZ)V", at = @At("HEAD"), argsOnly = true, ordinal = 0, require = 0)
    private int blxckoutfx$adjustSimpleConstructorColor(int color) {
        return adjustTextColor(color);
    }

    @ModifyVariable(method = "<init>(FFIIZZ)V", at = @At("HEAD"), argsOnly = true, ordinal = 0, require = 0)
    private int blxckoutfx$adjustBackgroundConstructorColor(int color) {
        return adjustTextColor(color);
    }

    @Inject(method = "getTextColor", at = @At("RETURN"), cancellable = true, require = 0)
    private void blxckoutfx$adjustStyledTextColor(TextColor textColor, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(adjustTextColor(cir.getReturnValue()));
    }

    private static int adjustTextColor(int color) {
        if (color == 0 || !BlxckoutFXShaders.isEnabled() || Minecraft.getInstance().screen == null) {
            return color;
        }

        if (!isDarkNeutralTextColor(color)) {
            return color;
        }

        int alpha = getEffectiveAlpha(color);
        return ARGB.color(alpha, READABLE_TEXT_BRIGHTNESS, READABLE_TEXT_BRIGHTNESS, READABLE_TEXT_BRIGHTNESS);
    }

    private static boolean isDarkNeutralTextColor(int color) {
        int red = color >> 16 & 0xFF;
        int green = color >> 8 & 0xFF;
        int blue = color & 0xFF;
        int max = Math.max(red, Math.max(green, blue));
        int min = Math.min(red, Math.min(green, blue));

        return max <= 96 && max - min <= 10;
    }

    private static int getEffectiveAlpha(int color) {
        int alpha = ARGB.alpha(color);
        if (alpha == 0 && (color & 0xFFFFFF) != 0) {
            return 255;
        }

        return alpha;
    }
}
