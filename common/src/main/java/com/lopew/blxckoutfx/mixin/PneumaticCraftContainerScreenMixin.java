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
            method = {
                    "renderLabels",
                    "m_280003_",
                    "method_2389"
            },
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;IIIZ)I",
                    ordinal = 0
            ),
            index = 4,
            require = 0
    )
    private int blxckoutfx$adjustMachineTitleColor(int color) {
        return adjustTitleColor(color);
    }

    @ModifyArg(
            method = {
                    "renderLabels",
                    "m_280003_",
                    "method_2389"
            },
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;m_280614_(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;IIIZ)I",
                    ordinal = 0
            ),
            index = 4,
            require = 0
    )
    private int blxckoutfx$adjustObfuscatedMachineTitleColor(int color) {
        return adjustTitleColor(color);
    }

    @ModifyArg(
            method = {
                    "renderLabels",
                    "m_280003_",
                    "method_2389"
            },
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/class_332;method_51439(Lnet/minecraft/class_327;Lnet/minecraft/class_2561;IIIZ)I",
                    ordinal = 0
            ),
            index = 4,
            require = 0
    )
    private int blxckoutfx$adjustIntermediaryMachineTitleColor(int color) {
        return adjustTitleColor(color);
    }

    @ModifyArg(
            method = {
                    "renderLabels",
                    "m_280003_",
                    "method_2389"
            },
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;IIIZ)I",
                    ordinal = 1
            ),
            index = 4,
            require = 0
    )
    private int blxckoutfx$adjustInventoryTitleColor(int color) {
        return adjustTitleColor(color);
    }

    @ModifyArg(
            method = {
                    "renderLabels",
                    "m_280003_",
                    "method_2389"
            },
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;m_280614_(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;IIIZ)I",
                    ordinal = 1
            ),
            index = 4,
            require = 0
    )
    private int blxckoutfx$adjustObfuscatedInventoryTitleColor(int color) {
        return adjustTitleColor(color);
    }

    @ModifyArg(
            method = {
                    "renderLabels",
                    "m_280003_",
                    "method_2389"
            },
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/class_332;method_51439(Lnet/minecraft/class_327;Lnet/minecraft/class_2561;IIIZ)I",
                    ordinal = 1
            ),
            index = 4,
            require = 0
    )
    private int blxckoutfx$adjustIntermediaryInventoryTitleColor(int color) {
        return adjustTitleColor(color);
    }

    private static int adjustTitleColor(int color) {
        return BlxckoutFXTextColors.adjustGeneralTextColor(color, true, false);
    }
}
