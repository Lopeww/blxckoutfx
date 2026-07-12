package com.lopew.blxckoutfx.mixin.modmenu;

import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderPipelines;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Pseudo
@Mixin(targets = "com.terraformersmc.modmenu.gui.widget.LegacyTexturedButtonWidget", remap = false)
public class ModMenuLegacyTexturedButtonMixin {
    @ModifyArg(
            method = "extractContents",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blit(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIFFIIII)V"
            ),
            index = 0,
            require = 1
    )
    private RenderPipeline blxckoutfx$useIconPreservingPipeline(RenderPipeline pipeline) {
        return BlxckoutFXRenderPipelines.MODMENU_ICON_BUTTON_TEXTURED;
    }
}
