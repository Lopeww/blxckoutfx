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
    private static final int READABLE_TEXT_BRIGHTNESS = 235;

    @ModifyVariable(method = "<init>(FFIZZ)V", at = @At("HEAD"), argsOnly = true, ordinal = 0, require = 0)
    private int blxckoutfx$adjustSimpleConstructorColor(int color) {
        return adjustDarkTextReadability(color);
    }

    @ModifyVariable(method = "<init>(FFIIZZ)V", at = @At("HEAD"), argsOnly = true, ordinal = 0, require = 0)
    private int blxckoutfx$adjustBackgroundConstructorColor(int color) {
        return adjustDarkTextReadability(color);
    }

    @Inject(method = "getTextColor", at = @At("RETURN"), cancellable = true, require = 0)
    private void blxckoutfx$adjustStyledTextColor(TextColor textColor, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(adjustDarkTextReadability(cir.getReturnValue()));
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
        return ARGB.color(alpha, READABLE_TEXT_BRIGHTNESS, READABLE_TEXT_BRIGHTNESS, READABLE_TEXT_BRIGHTNESS);
    }
}
