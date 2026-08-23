package com.lopew.blxckoutfx.mixin.tfc;

import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "net.dries007.tfc.client.screen.button.PlayerInventoryTabButton", remap = false)
public class TfcPlayerInventoryTabButtonMixin {
    @Inject(
            method = "m_87963_(Lnet/minecraft/client/gui/GuiGraphics;IIF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;m_280411_(Lnet/minecraft/resources/ResourceLocation;IIIIFFIIII)V",
                    shift = At.Shift.BEFORE,
                    remap = false
            ),
            require = 0,
            remap = false
    )
    private void blxckoutfx$suppressTabIconDarkening(
            GuiGraphics graphics,
            int mouseX,
            int mouseY,
            float partialTick,
            CallbackInfo ci
    ) {
        BlxckoutFXRenderContext.suppressTextureDarkening();
    }

    @Inject(
            method = "m_87963_(Lnet/minecraft/client/gui/GuiGraphics;IIF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;m_280411_(Lnet/minecraft/resources/ResourceLocation;IIIIFFIIII)V",
                    shift = At.Shift.AFTER,
                    remap = false
            ),
            require = 0,
            remap = false
    )
    private void blxckoutfx$restoreTabIconDarkening(
            GuiGraphics graphics,
            int mouseX,
            int mouseY,
            float partialTick,
            CallbackInfo ci
    ) {
        BlxckoutFXRenderContext.allowTextureDarkening();
    }
}
