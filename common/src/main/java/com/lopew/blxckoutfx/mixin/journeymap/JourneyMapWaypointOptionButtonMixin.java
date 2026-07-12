package com.lopew.blxckoutfx.mixin.journeymap;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderPipelines;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "journeymap.client.ui.component.popupscreenbutton.ImagePopupButton", remap = false)
public abstract class JourneyMapWaypointOptionButtonMixin {
    @Shadow protected WidgetSprites sprites;

    @Unique
    private boolean blxckoutfx$raiseIconLayer;
    @Unique
    private Identifier blxckoutfx$optionTexture;
    @Unique
    private int blxckoutfx$optionTint = 0xFFFFFFFF;

    @Inject(method = "extractContents", at = @At("HEAD"), require = 0)
    private void blxckoutfx$beginWaypointOptionIconPass(CallbackInfo ci) {
        Identifier enabled = this.sprites != null ? this.sprites.enabled() : null;
        Identifier disabled = this.sprites != null ? this.sprites.disabled() : null;
        this.blxckoutfx$raiseIconLayer = isWaypointManagerScreen() && (isOptionSprite(enabled) || isOptionSprite(disabled));
    }

    @Inject(
            method = "extractContents",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blit(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIFFIIIII)V",
                    shift = At.Shift.BEFORE
            ),
            require = 0
    )
    private void blxckoutfx$raiseWaypointOptionIconLayer(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        if (this.blxckoutfx$raiseIconLayer) {
            graphics.nextStratum();
        }
    }

    @Inject(method = "extractContents", at = @At("RETURN"), require = 0)
    private void blxckoutfx$drawWaypointOptionIconOnTop(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        if (this.blxckoutfx$optionTexture == null) {
            return;
        }

        graphics.nextStratum();
        graphics.blit(
                BlxckoutFXRenderPipelines.WAYPOINT_ICON_TEXTURED,
                this.blxckoutfx$optionTexture,
                ((AbstractWidget) (Object) this).getX(),
                ((AbstractWidget) (Object) this).getY(),
                0.0F,
                0.0F,
                ((AbstractWidget) (Object) this).getWidth(),
                ((AbstractWidget) (Object) this).getHeight(),
                ((AbstractWidget) (Object) this).getWidth(),
                ((AbstractWidget) (Object) this).getHeight(),
                this.blxckoutfx$optionTint
        );
        this.blxckoutfx$optionTexture = null;
        this.blxckoutfx$optionTint = 0xFFFFFFFF;
    }

    @WrapOperation(
            method = "extractContents",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/components/WidgetSprites;get(ZZ)Lnet/minecraft/resources/Identifier;"
            ),
            require = 0
    )
    private Identifier blxckoutfx$captureWaypointOptionSprite(
            WidgetSprites sprites,
            boolean active,
            boolean hovered,
            Operation<Identifier> original
    ) {
        Identifier iconResource = original.call(sprites, active, hovered);
        this.blxckoutfx$optionTexture = iconResource;
        return iconResource;
    }

    @WrapOperation(
            method = "extractContents",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blit(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIFFIIIII)V"
            ),
            require = 0
    )
    private void blxckoutfx$logOriginalWaypointOptionDraw(
            GuiGraphicsExtractor graphics,
            com.mojang.blaze3d.pipeline.RenderPipeline pipeline,
            Identifier texture,
            int x,
            int y,
            float u,
            float v,
            int width,
            int height,
            int sourceWidth,
            int sourceHeight,
            int tint,
            Operation<Void> original
    ) {
        this.blxckoutfx$optionTint = tint;
        original.call(graphics, pipeline, texture, x, y, u, v, width, height, sourceWidth, sourceHeight, tint);
    }

    @Unique
    private static boolean isOptionSprite(Identifier texture) {
        if (texture == null) {
            return false;
        }

        return texture.getPath().endsWith("option.png");
    }

    @Unique
    private static boolean isWaypointManagerScreen() {
        return net.minecraft.client.Minecraft.getInstance().gui.screen() != null
                && net.minecraft.client.Minecraft.getInstance().gui.screen().getClass().getName().startsWith("journeymap.client.ui.waypointmanager.");
    }
}
