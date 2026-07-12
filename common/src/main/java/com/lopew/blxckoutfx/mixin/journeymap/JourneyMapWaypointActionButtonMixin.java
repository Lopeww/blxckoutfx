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
@Mixin(targets = "journeymap.client.ui.component.buttons.BasicImageButton", remap = false)
public abstract class JourneyMapWaypointActionButtonMixin {
    @Unique
    private static final Identifier GRAY_TELEPORT_TEXTURE = Identifier.fromNamespaceAndPath("blxckoutfx", "journeymap/ui/img/teleport.png");
    @Unique
    private static final Identifier GRAY_PIN_TEXTURE = Identifier.fromNamespaceAndPath("blxckoutfx", "journeymap/ui/img/pin.png");
    @Unique
    private static final Identifier GRAY_SHARE_TEXTURE = Identifier.fromNamespaceAndPath("blxckoutfx", "journeymap/ui/img/share.png");

    @Shadow protected WidgetSprites sprites;
    @Shadow protected int texHeight;
    @Shadow protected int texWidth;
    @Shadow protected int xOffset;
    @Shadow protected int yOffset;

    @Unique
    private boolean blxckoutfx$raiseIconLayer;
    @Unique
    private Identifier blxckoutfx$waypointActionTexture;
    @Unique
    private int blxckoutfx$waypointActionTint = 0xFFFFFFFF;

    @Inject(method = "extractContents", at = @At("HEAD"), require = 0)
    private void blxckoutfx$beginWaypointActionIconPass(CallbackInfo ci) {
        Identifier enabled = this.sprites != null ? this.sprites.enabled() : null;
        Identifier disabled = this.sprites != null ? this.sprites.disabled() : null;
        this.blxckoutfx$raiseIconLayer = isWaypointManagerScreen() && (isWaypointActionSprite(enabled) || isWaypointActionSprite(disabled));
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
    private void blxckoutfx$raiseWaypointActionIconLayer(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        if (this.blxckoutfx$raiseIconLayer) {
            graphics.nextStratum();
        }
    }

    @Inject(method = "extractContents", at = @At("RETURN"), require = 0)
    private void blxckoutfx$drawWaypointActionIconOnTop(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        if (this.blxckoutfx$waypointActionTexture == null) {
            return;
        }

        graphics.nextStratum();
        graphics.blit(
                BlxckoutFXRenderPipelines.WAYPOINT_ICON_TEXTURED,
                blxckoutfx$getOverlayTexture(this.blxckoutfx$waypointActionTexture),
                this.blxckoutfx$getIconX(),
                this.blxckoutfx$getIconY(),
                0.0F,
                0.0F,
                this.texWidth,
                this.texHeight,
                this.texWidth,
                this.texHeight,
                this.blxckoutfx$waypointActionTint
        );
        this.blxckoutfx$waypointActionTexture = null;
        this.blxckoutfx$waypointActionTint = 0xFFFFFFFF;
    }

    @WrapOperation(
            method = "extractContents",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/components/WidgetSprites;get(ZZ)Lnet/minecraft/resources/Identifier;"
            ),
            require = 0
    )
    private Identifier blxckoutfx$useGrayWaypointActionSprite(
            WidgetSprites sprites,
            boolean active,
            boolean hovered,
            Operation<Identifier> original
    ) {
        Identifier iconResource = original.call(sprites, active, hovered);
        this.blxckoutfx$waypointActionTexture = iconResource;
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
    private void blxckoutfx$captureWaypointActionTint(
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
        this.blxckoutfx$waypointActionTint = tint;
        original.call(graphics, pipeline, texture, x, y, u, v, width, height, sourceWidth, sourceHeight, tint);
    }

    @Unique
    private static boolean isWaypointActionSprite(Identifier texture) {
        if (texture == null) {
            return false;
        }

        String path = texture.getPath();
        return path.endsWith("teleport.png")
                || path.endsWith("pin.png")
                || path.endsWith("share.png");
    }

    @Unique
    private static Identifier blxckoutfx$getOverlayTexture(Identifier texture) {
        if (texture == null) {
            return null;
        }

        String path = texture.getPath();
        if (path.endsWith("teleport.png")) {
            return GRAY_TELEPORT_TEXTURE;
        }

        if (path.endsWith("pin.png")) {
            return GRAY_PIN_TEXTURE;
        }

        if (path.endsWith("share.png")) {
            return GRAY_SHARE_TEXTURE;
        }

        return texture;
    }

    @Unique
    private int blxckoutfx$getIconX() {
        return ((AbstractWidget) (Object) this).getX() + this.xOffset;
    }

    @Unique
    private int blxckoutfx$getIconY() {
        return ((AbstractWidget) (Object) this).getY() + this.yOffset;
    }

    @Unique
    private static boolean isWaypointManagerScreen() {
        return net.minecraft.client.Minecraft.getInstance().gui.screen() != null
                && net.minecraft.client.Minecraft.getInstance().gui.screen().getClass().getName().startsWith("journeymap.client.ui.waypointmanager.");
    }
}
