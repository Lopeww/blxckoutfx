package com.lopew.blxckoutfx.mixin.ae2;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.lopew.blxckoutfx.client.integration.ae2.BlxckoutFXAe2Support;
import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;

@Pseudo
@Mixin(
        targets = {
                "com.glodblock.github.extendedae.client.button.EPPButton",
                "com.glodblock.github.extendedae.client.gui.GuiExPatternTerminal$SearchButton"
        },
        remap = false
)
public class ExtendedAeIconButtonsMixin {
    @WrapOperation(
            method = "renderWidget",
            at = @At(
                    value = "INVOKE",
                    target = "Lappeng/client/gui/style/Blitter;blit(Lnet/minecraft/client/gui/GuiGraphics;)V",
                    ordinal = 1
            ),
            require = 0
    )
    private void blxckoutfx$keepPrimaryIconReadable(
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
    private void blxckoutfx$keepSecondaryIconReadable(
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
        if (!blxckoutfx$shouldBypassIconDarkening()) {
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

    @Unique
    private static boolean blxckoutfx$shouldBypassIconDarkening() {
        return BlxckoutFXRenderContext.isRenderingAe2Screen()
                && BlxckoutFXAe2Support.isAe2ThemeEnabled();
    }
}

