package com.lopew.blxckoutfx.mixin.journeymap;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(targets = "journeymap.client.ui.option.CategorySlot", remap = false)
public class JourneyMapCategorySlotMixin {
    @WrapOperation(
            method = "renderWebMapDownloadButtons",
            at = @At(
                    value = "INVOKE",
                    target = "Ljourneymap/client/render/draw/DrawUtil;drawQuad(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/renderer/texture/DynamicTexture;DDDDZD)V"
            ),
            require = 0
    )
    private void blxckoutfx$renderWebMapLogoWithoutDarkening(
            GuiGraphics guiGraphics,
            DynamicTexture texture,
            double x,
            double y,
            double width,
            double height,
            boolean centered,
            double z,
            Operation<Void> original
    ) {
        blxckoutfx$renderNoDarkening(original, guiGraphics, texture, x, y, width, height, centered, z);
    }

    @Unique
    private void blxckoutfx$renderNoDarkening(
            Operation<Void> original,
            GuiGraphics guiGraphics,
            DynamicTexture texture,
            double x,
            double y,
            double width,
            double height,
            boolean centered,
            double z
    ) {
        BlxckoutFXRenderContext.suppressTextureDarkening();
        try {
            original.call(guiGraphics, texture, x, y, width, height, centered, z);
        } finally {
            BlxckoutFXRenderContext.allowTextureDarkening();
        }
    }
}

