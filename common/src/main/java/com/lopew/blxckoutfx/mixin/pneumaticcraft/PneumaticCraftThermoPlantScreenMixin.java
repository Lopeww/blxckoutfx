package com.lopew.blxckoutfx.mixin.pneumaticcraft;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import com.lopew.blxckoutfx.client.render.BlxckoutFXTextColors;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Pseudo
@Mixin(targets = "me.desht.pneumaticcraft.client.gui.ThermoPlantScreen", remap = false)
public class PneumaticCraftThermoPlantScreenMixin {
    @ModifyArg(
            method = "renderLabels",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/util/FormattedCharSequence;FFIZ)I",
                    ordinal = 0
            ),
            index = 4,
            require = 0
    )
    private int blxckoutfx$adjustThermoTitleColor(int color) {
        return BlxckoutFXTextColors.adjustGeneralTextColor(color, true, false);
    }

    @WrapOperation(
            method = "renderBg",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V"
            ),
            require = 0
    )
    private void blxckoutfx$capThermoProgressArrowDarkening(
            GuiGraphics instance,
            ResourceLocation atlasLocation,
            int x,
            int y,
            int uOffset,
            int vOffset,
            int uWidth,
            int vHeight,
            Operation<Void> original
    ) {
        BlxckoutFXRenderContext.enterBalancedCappedDarkening();
        try {
            original.call(instance, atlasLocation, x, y, uOffset, vOffset, uWidth, vHeight);
        } finally {
            BlxckoutFXRenderContext.exitBalancedCappedDarkening();
        }
    }
}

