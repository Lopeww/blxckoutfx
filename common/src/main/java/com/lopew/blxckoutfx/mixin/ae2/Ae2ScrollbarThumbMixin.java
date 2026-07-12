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
@Mixin(targets = "appeng.client.gui.widgets.Scrollbar", remap = false)
public abstract class Ae2ScrollbarThumbMixin {
    @WrapOperation(
            method = "drawForegroundLayer",
            at = @At(
                    value = "INVOKE",
                    target = "Lappeng/client/gui/style/Blitter;blit(Lnet/minecraft/client/gui/GuiGraphics;)V"
            ),
            require = 0
    )
    private void blxckoutfx$remapAe2ScrollbarThumb(
            @Coerce Object blitter,
            GuiGraphics guiGraphics,
            Operation<Void> original
    ) {
        if (!blxckoutfx$shouldRemapThumb()) {
            original.call(blitter, guiGraphics);
            return;
        }

        BlxckoutFXRenderContext.enterAe2ScrollbarThumbRender();
        try {
            original.call(blitter, guiGraphics);
        } finally {
            BlxckoutFXRenderContext.exitAe2ScrollbarThumbRender();
        }
    }

    private boolean blxckoutfx$shouldRemapThumb() {
        return BlxckoutFXRenderContext.isRenderingAe2Screen()
                && BlxckoutFXAe2Support.isBlackoutAe2ThemeActive();
    }
}

