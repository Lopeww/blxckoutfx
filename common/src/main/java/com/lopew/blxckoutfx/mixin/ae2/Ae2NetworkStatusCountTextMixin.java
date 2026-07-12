package com.lopew.blxckoutfx.mixin.ae2;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.lopew.blxckoutfx.client.render.BlxckoutFXAe2TextColors;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;

@Pseudo
@Mixin(targets = "appeng.client.gui.me.networktool.NetworkStatusScreen", remap = false)
public class Ae2NetworkStatusCountTextMixin {
    @WrapOperation(
            method = "drawMachineCount",
            at = @At(
                    value = "INVOKE",
                    target = "Lappeng/client/gui/style/Color;toARGB()I"
            ),
            require = 0
    )
    private int blxckoutfx$adjustAe2NetworkStatusCountColor(@Coerce Object color, Operation<Integer> original) {
        return BlxckoutFXAe2TextColors.adjustThemeTextColor(original.call(color));
    }
}
