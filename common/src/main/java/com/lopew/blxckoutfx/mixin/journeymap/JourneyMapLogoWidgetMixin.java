package com.lopew.blxckoutfx.mixin.journeymap;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(targets = "journeymap.client.ui.component.widgets.LogoWidget", remap = false)
public class JourneyMapLogoWidgetMixin {
    @WrapOperation(
            method = "renderWidget",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIFFIIII)V"
            ),
            require = 0
    )
    private void blxckoutfx$renderJourneyMapLogoNoDarkening(
            GuiGraphics guiGraphics,
            ResourceLocation atlasLocation,
            int x,
            int y,
            int uOffset,
            int vOffset,
            float uWidth,
            float vHeight,
            int textureWidth,
            int textureHeight,
            int imageWidth,
            int imageHeight,
            Operation<Void> original
    ) {
        blxckoutfx$renderLogoWithoutDarkening(original, guiGraphics, atlasLocation, x, y, uOffset, vOffset, uWidth, vHeight, textureWidth, textureHeight, imageWidth, imageHeight);
    }

    @WrapOperation(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIFFIIII)V"
            ),
            require = 0
    )
    private void blxckoutfx$renderJourneyMapLogoNoDarkeningLegacy(
            GuiGraphics guiGraphics,
            ResourceLocation atlasLocation,
            int x,
            int y,
            int uOffset,
            int vOffset,
            float uWidth,
            float vHeight,
            int textureWidth,
            int textureHeight,
            int imageWidth,
            int imageHeight,
            Operation<Void> original
    ) {
        blxckoutfx$renderLogoWithoutDarkening(original, guiGraphics, atlasLocation, x, y, uOffset, vOffset, uWidth, vHeight, textureWidth, textureHeight, imageWidth, imageHeight);
    }

    @WrapOperation(
            method = "method_48579",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIFFIIII)V"
            ),
            require = 0
    )
    private void blxckoutfx$renderJourneyMapLogoNoDarkeningFabric(
            GuiGraphics guiGraphics,
            ResourceLocation atlasLocation,
            int x,
            int y,
            int uOffset,
            int vOffset,
            float uWidth,
            float vHeight,
            int textureWidth,
            int textureHeight,
            int imageWidth,
            int imageHeight,
            Operation<Void> original
    ) {
        blxckoutfx$renderLogoWithoutDarkening(original, guiGraphics, atlasLocation, x, y, uOffset, vOffset, uWidth, vHeight, textureWidth, textureHeight, imageWidth, imageHeight);
    }

    @Unique
    private void blxckoutfx$renderLogoWithoutDarkening(
            Operation<Void> original,
            GuiGraphics guiGraphics,
            ResourceLocation atlasLocation,
            int x,
            int y,
            int uOffset,
            int vOffset,
            float uWidth,
            float vHeight,
            int textureWidth,
            int textureHeight,
            int imageWidth,
            int imageHeight
    ) {
        BlxckoutFXRenderContext.suppressTextureDarkening();
        try {
            original.call(guiGraphics, atlasLocation, x, y, uOffset, vOffset, uWidth, vHeight, textureWidth, textureHeight, imageWidth, imageHeight);
        } finally {
            BlxckoutFXRenderContext.allowTextureDarkening();
        }
    }
}

