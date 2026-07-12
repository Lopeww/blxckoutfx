package com.lopew.blxckoutfx.mixin.pneumaticcraft;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;

@Pseudo
@Mixin(targets = "me.desht.pneumaticcraft.client.gui.widget.WidgetTank", remap = false)
public class PneumaticCraftWidgetTankMixin {
    @WrapOperation(
            method = "renderWidget",
            at = @At(
                    value = "INVOKE",
                    target = "Lme/desht/pneumaticcraft/client/util/GuiUtils;drawFluid(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/renderer/Rect2i;Lnet/neoforged/neoforge/fluids/FluidStack;Lnet/neoforged/neoforge/fluids/IFluidTank;)V"
            ),
            require = 0
    )
    private void blxckoutfx$suppressTankFluidDarkening(
            GuiGraphics guiGraphics,
            Rect2i bounds,
            @Coerce Object fluidStack,
            @Coerce Object tank,
            Operation<Void> original
    ) {
        BlxckoutFXRenderContext.suppressTextureDarkening();
        try {
            original.call(guiGraphics, bounds, fluidStack, tank);
        } finally {
            BlxckoutFXRenderContext.allowTextureDarkening();
        }
    }

    @WrapOperation(
            method = "renderWidget",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;fill(IIIII)V"
            ),
            require = 0
    )
    private void blxckoutfx$suppressTankTickDarkening(
            GuiGraphics instance,
            int minX,
            int minY,
            int maxX,
            int maxY,
            int color,
            Operation<Void> original
    ) {
        BlxckoutFXRenderContext.suppressTextureDarkening();
        try {
            original.call(instance, minX, minY, maxX, maxY, color);
        } finally {
            BlxckoutFXRenderContext.allowTextureDarkening();
        }
    }
}

