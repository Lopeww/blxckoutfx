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
@Mixin(targets = "me.desht.pneumaticcraft.client.gui.AbstractPneumaticCraftContainerScreen", remap = false)
public class PneumaticCraftContainerScreenMixin {
    private static final int[][] LIQUID_COMPRESSOR_UPGRADE_ICON_SEGMENTS = {
            {27, 14, 2, 1},
            {26, 15, 4, 1},
            {25, 16, 6, 1},
            {24, 17, 8, 1},
            {24, 18, 9, 1},
            {24, 19, 10, 1},
            {25, 20, 6, 1},
            {25, 21, 6, 1},
            {25, 22, 6, 1},
            {25, 23, 6, 1},
            {25, 24, 6, 1},
            {25, 25, 6, 1},
    };
    private static final int[][] THERMO_PLANT_SIDE_ARROW_SEGMENTS = {
            {132, 89, 2, 1},
            {131, 90, 4, 1},
            {130, 91, 6, 1},
            {129, 92, 8, 1},
            {128, 93, 10, 1},
            {127, 94, 12, 1},
            {130, 95, 6, 1},
            {130, 96, 6, 1},
            {130, 97, 6, 1},
            {130, 98, 6, 1},
            {130, 99, 6, 1},
            {130, 100, 6, 1},
    };

    @WrapOperation(
            method = "renderBg",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V"
            ),
            require = 0
    )
    private void blxckoutfx$redrawLiquidCompressorUpgradeIconWithoutDarkening(
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
        original.call(instance, atlasLocation, x, y, uOffset, vOffset, uWidth, vHeight);

        if (!blxckoutfx$isSupportedGuiForIconRedraw(atlasLocation)) {
            return;
        }

        BlxckoutFXRenderContext.suppressTextureDarkening();
        try {
            instance.setColor(1.0F, 1.0F, 1.0F, 1.0F);
            int[][] segments = blxckoutfx$isThermoPlantGui(atlasLocation)
                    ? THERMO_PLANT_SIDE_ARROW_SEGMENTS
                    : LIQUID_COMPRESSOR_UPGRADE_ICON_SEGMENTS;
            for (int[] segment : segments) {
                instance.blit(atlasLocation, x + segment[0], y + segment[1], segment[0], segment[1], segment[2], segment[3]);
            }
        } finally {
            BlxckoutFXRenderContext.allowTextureDarkening();
        }
    }

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

    private static boolean blxckoutfx$isSupportedGuiForIconRedraw(ResourceLocation atlasLocation) {
        return blxckoutfx$isLiquidCompressorGui(atlasLocation) || blxckoutfx$isThermoPlantGui(atlasLocation);
    }

    private static boolean blxckoutfx$isLiquidCompressorGui(ResourceLocation atlasLocation) {
        if (!"pneumaticcraft".equals(atlasLocation.getNamespace())) {
            return false;
        }

        String path = atlasLocation.getPath();
        return "textures/gui/gui_liquid_compressor.png".equals(path)
                || "textures/gui/gui_advanced_liquid_compressor.png".equals(path);
    }

    private static boolean blxckoutfx$isThermoPlantGui(ResourceLocation atlasLocation) {
        return "pneumaticcraft".equals(atlasLocation.getNamespace())
                && "textures/gui/gui_thermopneumatic_processing_plant.png".equals(atlasLocation.getPath());
    }
}

