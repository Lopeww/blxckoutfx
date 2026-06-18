package com.lopew.blxckoutfx.mixin;

import com.lopew.blxckoutfx.client.BlxckoutFXTextColors;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Pseudo
@Mixin(targets = "me.desht.pneumaticcraft.client.gui.widget.WidgetTemperature", remap = false)
public class PneumaticCraftWidgetTemperatureMixin {
    @ModifyArg(
            method = {
                    "renderWidget",
                    "m_87963_",
                    "method_48579",
                    "drawTicks"
            },
            at = @At(
                    value = "INVOKE",
                    target = "Lme/desht/pneumaticcraft/client/util/GuiUtils;drawScaledText(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;IIIFZ)V"
            ),
            index = 5,
            require = 0
    )
    private int blxckoutfx$adjustTemperatureTextColor(int color) {
        return BlxckoutFXTextColors.adjustGeneralTextColor(color, true, false);
    }

    @ModifyArg(
            method = {
                    "renderWidget",
                    "m_87963_",
                    "method_48579",
                    "drawTicks"
            },
            at = @At(
                    value = "INVOKE",
                    target = "Lme/desht/pneumaticcraft/client/util/GuiUtils;drawScaledText(Lnet/minecraft/class_332;Lnet/minecraft/class_327;Lnet/minecraft/class_2561;IIIFZ)V"
            ),
            index = 5,
            require = 0
    )
    private int blxckoutfx$adjustIntermediaryTemperatureTextColor(int color) {
        return BlxckoutFXTextColors.adjustGeneralTextColor(color, true, false);
    }
}
