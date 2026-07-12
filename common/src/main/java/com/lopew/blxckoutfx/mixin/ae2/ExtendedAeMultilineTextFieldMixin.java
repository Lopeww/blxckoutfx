package com.lopew.blxckoutfx.mixin.ae2;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.lopew.blxckoutfx.client.integration.ae2.BlxckoutFXAe2Support;
import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(targets = "com.glodblock.github.extendedae.client.gui.widget.MultilineTextFieldWidget", remap = false)
public class ExtendedAeMultilineTextFieldMixin {
    @WrapOperation(
            method = "extractWidgetRenderState",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;text(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;III)V"
            ),
            require = 0
    )
    private void blxckoutfx$markTagPlaceholderRender(
            GuiGraphicsExtractor guiGraphics,
            Font font,
            Component text,
            int x,
            int y,
            int color,
            Operation<Void> original
    ) {
        if (!blxckoutfx$shouldMarkPlaceholderRender()) {
            original.call(guiGraphics, font, text, x, y, color);
            return;
        }

        BlxckoutFXRenderContext.enterExtendedAeTagPlaceholderRender();
        try {
            original.call(guiGraphics, font, text, x, y, color);
        } finally {
            BlxckoutFXRenderContext.exitExtendedAeTagPlaceholderRender();
        }
    }

    @Unique
    private static boolean blxckoutfx$shouldMarkPlaceholderRender() {
        return BlxckoutFXRenderContext.isRenderingAe2Screen()
                && BlxckoutFXAe2Support.isBlackoutAe2ThemeActive();
    }
}
