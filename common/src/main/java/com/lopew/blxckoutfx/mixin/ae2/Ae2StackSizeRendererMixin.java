package com.lopew.blxckoutfx.mixin.ae2;

import com.lopew.blxckoutfx.client.integration.ae2.BlxckoutFXAe2Support;
import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "appeng.client.gui.me.common.StackSizeRenderer", remap = false)
public class Ae2StackSizeRendererMixin {
    @Inject(
            method = "renderSizeLabel(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/gui/Font;FFLjava/lang/String;)V",
            at = @At("HEAD"),
            require = 0
    )
    private static void blxckoutfx$enterAe2StackSizeLabelRender(
            GuiGraphics guiGraphics,
            Font font,
            float x,
            float y,
            String text,
            CallbackInfo ci
    ) {
        if (BlxckoutFXAe2Support.isAe2Screen(Minecraft.getInstance().screen)) {
            BlxckoutFXRenderContext.enterAe2StackSizeLabelRender();
        }
    }

    @Inject(
            method = "renderSizeLabel(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/gui/Font;FFLjava/lang/String;)V",
            at = @At("RETURN"),
            require = 0
    )
    private static void blxckoutfx$exitAe2StackSizeLabelRender(
            GuiGraphics guiGraphics,
            Font font,
            float x,
            float y,
            String text,
            CallbackInfo ci
    ) {
        BlxckoutFXRenderContext.exitAe2StackSizeLabelRender();
    }

    @Inject(
            method = "renderSizeLabel(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/gui/Font;FFLjava/lang/String;Z)V",
            at = @At("HEAD"),
            require = 0
    )
    private static void blxckoutfx$enterAe2StackSizeLabelRenderLarge(
            GuiGraphics guiGraphics,
            Font font,
            float x,
            float y,
            String text,
            boolean largeFont,
            CallbackInfo ci
    ) {
        if (BlxckoutFXAe2Support.isAe2Screen(Minecraft.getInstance().screen)) {
            BlxckoutFXRenderContext.enterAe2StackSizeLabelRender();
        }
    }

    @Inject(
            method = "renderSizeLabel(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/gui/Font;FFLjava/lang/String;Z)V",
            at = @At("RETURN"),
            require = 0
    )
    private static void blxckoutfx$exitAe2StackSizeLabelRenderLarge(
            GuiGraphics guiGraphics,
            Font font,
            float x,
            float y,
            String text,
            boolean largeFont,
            CallbackInfo ci
    ) {
        BlxckoutFXRenderContext.exitAe2StackSizeLabelRender();
    }
}

