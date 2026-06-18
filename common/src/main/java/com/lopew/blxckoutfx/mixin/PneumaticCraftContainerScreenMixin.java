package com.lopew.blxckoutfx.mixin;

import com.lopew.blxckoutfx.client.BlxckoutFXTextColors;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Pseudo
@Mixin(targets = "me.desht.pneumaticcraft.client.gui.AbstractPneumaticCraftContainerScreen", remap = false)
public class PneumaticCraftContainerScreenMixin {
    @ModifyArg(
            method = "renderLabels",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;IIIZ)I",
                    ordinal = 0
            ),
            index = 4,
            require = 0
    )
    private int blxckoutfx$adjustMachineTitleColor(int color) {
        return BlxckoutFXTextColors.adjustGeneralTextColor(color, true, false);
    }

    @ModifyArg(
            method = "renderLabels",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;IIIZ)I",
                    ordinal = 1
            ),
            index = 4,
            require = 0
    )
    private int blxckoutfx$adjustInventoryTitleColor(int color) {
        return BlxckoutFXTextColors.adjustGeneralTextColor(color, true, false);
    }
}
