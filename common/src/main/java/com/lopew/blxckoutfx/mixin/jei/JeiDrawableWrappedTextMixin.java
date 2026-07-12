package com.lopew.blxckoutfx.mixin.jei;

import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import com.lopew.blxckoutfx.client.render.BlxckoutFXTextColors;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Pseudo
@Mixin(targets = "mezz.jei.common.gui.elements.DrawableWrappedText", remap = false)
public class JeiDrawableWrappedTextMixin {
    @Redirect(
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
            boolean dropShadow
    ) {
        if (!BlxckoutFXRenderContext.isRenderingJeiInformationRecipe()) {
            return instance.drawString(font, text, x, y, color, dropShadow);
        }

        BlxckoutFXRenderContext.suppressTextureDarkening();
        try {
            int adjustedColor = BlxckoutFXTextColors.adjustGeneralTextColor(color, true, false);
            return instance.drawString(font, text, x, y, adjustedColor, dropShadow);
        } finally {
            BlxckoutFXRenderContext.allowTextureDarkening();
        }
    }
}
