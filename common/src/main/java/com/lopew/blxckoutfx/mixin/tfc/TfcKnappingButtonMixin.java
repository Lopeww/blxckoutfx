package com.lopew.blxckoutfx.mixin.tfc;

import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "net.dries007.tfc.client.screen.button.KnappingButton", remap = false)
public class TfcKnappingButtonMixin {
    @Inject(
            method = "m_87963_(Lnet/minecraft/client/gui/GuiGraphics;IIF)V",
            at = @At("HEAD"),
            require = 0,
            remap = false
    )
    private void blxckoutfx$suppressGridCellDarkening(
            GuiGraphics guiGraphics,
            int mouseX,
            int mouseY,
            float partialTick,
            CallbackInfo ci
    ) {
        BlxckoutFXRenderContext.suppressTextureDarkening();
    }

    @Inject(
            method = "m_87963_(Lnet/minecraft/client/gui/GuiGraphics;IIF)V",
            at = @At("RETURN"),
            require = 0,
            remap = false
    )
    private void blxckoutfx$restoreGridCellDarkening(
            GuiGraphics guiGraphics,
            int mouseX,
            int mouseY,
            float partialTick,
            CallbackInfo ci
    ) {
        BlxckoutFXRenderContext.allowTextureDarkening();
    }
}
