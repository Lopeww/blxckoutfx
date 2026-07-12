package com.lopew.blxckoutfx.mixin.journeymap;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.lopew.blxckoutfx.client.integration.journeymap.BlxckoutFXJourneyMapThemeSupport;
import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderPipelines;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;

@Pseudo
@Mixin(targets = "journeymap.client.ui.theme.ThemeButton", remap = false)
public class JourneyMapThemeButtonMixin {
    @WrapOperation(
            method = "extractContents",
            at = @At(
                    value = "INVOKE",
                    target = "Ljourneymap/client/ui/theme/ThemeButton;drawNativeButton(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lcom/mojang/blaze3d/vertex/PoseStack;II)V"
            ),
            require = 0
    )
    private void blxckoutfx$keepThemeButtonBackgroundReadable(
            @Coerce Object self,
            GuiGraphicsExtractor graphics,
            PoseStack poseStack,
            int mouseX,
            int mouseY,
            Operation<Void> original
    ) {
        if (!BlxckoutFXJourneyMapThemeSupport.isPuristThemeActive()
                || BlxckoutFXJourneyMapThemeSupport.isWaypointManagerScreen()) {
            original.call(self, graphics, poseStack, mouseX, mouseY);
            return;
        }

        original.call(self, graphics, poseStack, mouseX, mouseY);
    }

    @org.spongepowered.asm.mixin.injection.ModifyArg(
            method = "drawNativeButton",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIII)V"
            ),
            index = 0,
            require = 0
    )
    private RenderPipeline blxckoutfx$useJourneyMapBackgroundPipeline(RenderPipeline pipeline) {
        if (BlxckoutFXJourneyMapThemeSupport.shouldBypassThemeButtonDarkening()
                || BlxckoutFXJourneyMapThemeSupport.isWaypointManagerScreen()) {
            return pipeline;
        }

        if (BlxckoutFXJourneyMapThemeSupport.isPuristThemeActive()) {
            return BlxckoutFXRenderPipelines.BUTTON_TEXTURED;
        }

        return pipeline;
    }

    @WrapOperation(
            method = "extractContents",
            at = @At(
                    value = "INVOKE",
                    target = "Ljourneymap/client/render/draw/DrawUtil;drawColoredTexture(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/resources/Identifier;IFDDDDFD)V"
            ),
            require = 0
    )
    private void blxckoutfx$keepThemeBackgroundReadable(
            GuiGraphicsExtractor graphics,
            Identifier texture,
            int color,
            float alpha,
            double x,
            double y,
            double width,
            double height,
            float scale,
            double rotation,
            Operation<Void> original
    ) {
        original.call(graphics, texture, color, alpha, x, y, width, height, scale, rotation);
    }
}
