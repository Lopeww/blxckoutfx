package com.lopew.blxckoutfx.mixin.core;

import com.lopew.blxckoutfx.client.BlxckoutFXShaders;
import com.lopew.blxckoutfx.client.render.BlxckoutFXTextColors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(FontRenderer.class)
public class FontRendererMixin {
    // Change the public draw boundary once. renderString is invoked twice for shadowed text
    // and is rewritten by SmoothFont, while this signature is retained by both renderers.
    @ModifyVariable(
            method = "drawString(Ljava/lang/String;FFIZ)I",
            at = @At("HEAD"),
            argsOnly = true,
            ordinal = 0,
            require = 1
    )
    private int blxckoutfx$adjustTextColor(int color) {
        GuiScreen screen = Minecraft.getMinecraft().currentScreen;
        if (screen == null || !BlxckoutFXShaders.isEnabled() || !BlxckoutFXShaders.shouldApplyToScreen(screen)) {
            return color;
        }
        if (Minecraft.getMinecraft().world == null && !com.lopew.blxckoutfx.client.BlxckoutFXRenderContext.isRenderingButton()) {
            return color;
        }

        if (BlxckoutFXShaders.isBlxckoutPresetActive()) {
            return color;
        }

        return BlxckoutFXTextColors.adjustGeneralTextColor(color);
    }
}
