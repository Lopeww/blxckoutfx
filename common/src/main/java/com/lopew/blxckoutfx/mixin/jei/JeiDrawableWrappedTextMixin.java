package com.lopew.blxckoutfx.mixin.jei;

import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import com.lopew.blxckoutfx.client.render.BlxckoutFXTextColors;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(targets = "mezz.jei.common.gui.elements.DrawableWrappedText", remap = false)
public class JeiDrawableWrappedTextMixin {
    @WrapOperation(
            method = "draw",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/util/FormattedCharSequence;IIIZ)I",
                    remap = true
            ),
            require = 0
    )
    private int blxckoutfx$suppressJeiWrappedTextDarkening(
            GuiGraphics instance,
            Font font,
            FormattedCharSequence text,
            int x,
            int y,
            int color,
            boolean dropShadow,
            Operation<Integer> original
    ) {
        if (!BlxckoutFXRenderContext.isRenderingJeiInformationRecipe()) {
            return original.call(instance, font, text, x, y, color, dropShadow);
        }

        BlxckoutFXRenderContext.suppressTextureDarkening();
        try {
            int adjustedColor = BlxckoutFXTextColors.adjustGeneralTextColor(color, true, false);
            return original.call(instance, font, text, x, y, adjustedColor, dropShadow);
        } finally {
            BlxckoutFXRenderContext.allowTextureDarkening();
        }
    }
}

