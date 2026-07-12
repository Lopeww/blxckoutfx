package com.lopew.blxckoutfx.mixin.ae2;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import com.lopew.blxckoutfx.client.render.BlxckoutFXShaders;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;

@Pseudo
@Mixin(targets = "net.pedroksl.advanced_ae.client.gui.widgets.AAEIconButton", remap = false)
public class AdvancedAeIconButtonMixin {
    @WrapOperation(
            method = "renderWidget",
            at = @At(
                    value = "INVOKE",
                    target = "Lappeng/client/gui/style/Blitter;blit(Lnet/minecraft/client/gui/GuiGraphics;)V",
                    ordinal = 1
            ),
            require = 0
    )
    private void blxckoutfx$keepHalfSizeIconReadable(
            @Coerce Object blitter,
            GuiGraphics guiGraphics,
            Operation<Void> original
    ) {
        blxckoutfx$renderIconWithoutDarkening(blitter, guiGraphics, original);
    }

    @WrapOperation(
            method = "renderWidget",
            at = @At(
                    value = "INVOKE",
                    target = "Lappeng/client/gui/style/Blitter;blit(Lnet/minecraft/client/gui/GuiGraphics;)V",
                    ordinal = 3
            ),
            require = 0
    )
    private void blxckoutfx$keepFullSizeIconReadable(
            @Coerce Object blitter,
            GuiGraphics guiGraphics,
            Operation<Void> original
    ) {
        blxckoutfx$renderIconWithoutDarkening(blitter, guiGraphics, original);
    }

    @Unique
    private static void blxckoutfx$renderIconWithoutDarkening(
            @Coerce Object blitter,
            GuiGraphics guiGraphics,
            Operation<Void> original
    ) {
        if (!BlxckoutFXShaders.isEnabled()) {
            original.call(blitter, guiGraphics);
            return;
        }

        BlxckoutFXRenderContext.suppressTextureDarkening();
        try {
            original.call(blitter, guiGraphics);
        } finally {
            BlxckoutFXRenderContext.allowTextureDarkening();
        }
    }
}
