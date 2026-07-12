package com.lopew.blxckoutfx.mixin.ae2;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.lopew.blxckoutfx.client.integration.ae2.BlxckoutFXAe2Support;
import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;

@Pseudo
@Mixin(targets = "appeng.client.gui.widgets.TabButton", remap = false)
public class Ae2TabButtonMixin {
    @WrapOperation(
            method = "renderWidget",
            at = @At(
                    value = "INVOKE",
                    target = "Lappeng/client/gui/style/Blitter;blit(Lnet/minecraft/client/gui/GuiGraphics;)V",
                    ordinal = 1
            ),
            require = 0
    )
    private void blxckoutfx$keepAe2TabIconReadable(
            @Coerce Object blitter,
            GuiGraphics guiGraphics,
            Operation<Void> original
    ) {
        if (!BlxckoutFXAe2Support.isBlxckoutAe2ThemeActive()
                || !BlxckoutFXRenderContext.isRenderingAe2Screen()) {
            original.call(blitter, guiGraphics);
            return;
        }

        BlxckoutFXRenderContext.suppressTextureDarkening();
        BlxckoutFXRenderContext.enterAe2IconRender();
        try {
            original.call(blitter, guiGraphics);
        } finally {
            BlxckoutFXRenderContext.exitAe2IconRender();
            BlxckoutFXRenderContext.allowTextureDarkening();
        }
    }
}
