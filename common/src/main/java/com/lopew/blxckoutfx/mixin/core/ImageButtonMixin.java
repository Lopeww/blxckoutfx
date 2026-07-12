package com.lopew.blxckoutfx.mixin.core;

import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Button.OnPress;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ImageButton.class)
public class ImageButtonMixin {
    @Unique
    private static final int BLXCKOUTFX_ICON_BUTTON_LANGUAGE = 1;
    @Unique
    private static final int BLXCKOUTFX_ICON_BUTTON_ACCESSIBILITY = 2;
    @Unique
    private static final int BLXCKOUTFX_ICON_BUTTON_MODMENU_CONFIGURE = 3;
    @Unique
    private static final int BLXCKOUTFX_ICON_BUTTON_MODMENU_FILTERS = 4;
    @Unique
    private boolean blxckoutfx$vanillaIconButtonRender;
    @Unique
    private ResourceLocation blxckoutfx$resourceLocation;
    @Unique
    private int blxckoutfx$xTexStart;
    @Unique
    private int blxckoutfx$yTexStart;
    @Unique
    private int blxckoutfx$yDiffTex;
    @Unique
    private int blxckoutfx$textureWidth;
    @Unique
    private int blxckoutfx$textureHeight;

    @Inject(method = "<init>(IIIIIILnet/minecraft/resources/ResourceLocation;Lnet/minecraft/client/gui/components/Button$OnPress;)V", at = @At("RETURN"))
    private void blxckoutfx$captureStateShort(
            int x,
            int y,
            int width,
            int height,
            int xTexStart,
            int yTexStart,
            ResourceLocation resourceLocation,
            OnPress onPress,
            CallbackInfo ci
    ) {
        blxckoutfx$storeState(resourceLocation, xTexStart, yTexStart, height, 256, 256);
    }

    @Inject(method = "<init>(IIIIIIILnet/minecraft/resources/ResourceLocation;Lnet/minecraft/client/gui/components/Button$OnPress;)V", at = @At("RETURN"))
    private void blxckoutfx$captureStateDefaultTexture(
            int x,
            int y,
            int width,
            int height,
            int xTexStart,
            int yTexStart,
            int yDiffTex,
            ResourceLocation resourceLocation,
            OnPress onPress,
            CallbackInfo ci
    ) {
        blxckoutfx$storeState(resourceLocation, xTexStart, yTexStart, yDiffTex, 256, 256);
    }

    @Inject(method = "<init>(IIIIIIILnet/minecraft/resources/ResourceLocation;IILnet/minecraft/client/gui/components/Button$OnPress;)V", at = @At("RETURN"))
    private void blxckoutfx$captureStateSized(
            int x,
            int y,
            int width,
            int height,
            int xTexStart,
            int yTexStart,
            int yDiffTex,
            ResourceLocation resourceLocation,
            int textureWidth,
            int textureHeight,
            OnPress onPress,
            CallbackInfo ci
    ) {
        blxckoutfx$storeState(resourceLocation, xTexStart, yTexStart, yDiffTex, textureWidth, textureHeight);
    }

    @Inject(method = "<init>(IIIIIIILnet/minecraft/resources/ResourceLocation;IILnet/minecraft/client/gui/components/Button$OnPress;Lnet/minecraft/network/chat/Component;)V", at = @At("RETURN"))
    private void blxckoutfx$captureStateSizedWithNarration(
            int x,
            int y,
            int width,
            int height,
            int xTexStart,
            int yTexStart,
            int yDiffTex,
            ResourceLocation resourceLocation,
            int textureWidth,
            int textureHeight,
            OnPress onPress,
            net.minecraft.network.chat.Component narration,
            CallbackInfo ci
    ) {
        blxckoutfx$storeState(resourceLocation, xTexStart, yTexStart, yDiffTex, textureWidth, textureHeight);
    }

    @Inject(method = "renderWidget", at = @At("HEAD"))
    private void blxckoutfx$enterImageButtonRender(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        ImageButton button = (ImageButton) (Object) this;
        int mode = blxckoutfx$getIconButtonMode(button);

        BlxckoutFXRenderContext.enterButtonRender();
        if (mode == 0) {
            return;
        }

        this.blxckoutfx$vanillaIconButtonRender = true;
        BlxckoutFXRenderContext.enterVanillaIconButtonGuiRender(
                mode,
                button.getX(),
                button.getY(),
                button.getX() + button.getWidth(),
                button.getY() + button.getHeight()
        );
    }

    @Inject(method = "renderWidget", at = @At("RETURN"))
    private void blxckoutfx$exitImageButtonRender(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        if (this.blxckoutfx$vanillaIconButtonRender) {
            this.blxckoutfx$vanillaIconButtonRender = false;
            BlxckoutFXRenderContext.exitVanillaIconButtonRender();
        }
        BlxckoutFXRenderContext.exitButtonRender();
    }

    @Unique
    private int blxckoutfx$getIconButtonMode(ImageButton button) {
        if (button.getWidth() != 20 || button.getHeight() != 20 || this.blxckoutfx$yDiffTex != 20) {
            return 0;
        }

        if (blxckoutfx$isTexture("minecraft", "textures/gui/widgets.png")
                && this.blxckoutfx$xTexStart == 0
                && this.blxckoutfx$yTexStart == 106
                && this.blxckoutfx$textureWidth == 256
                && this.blxckoutfx$textureHeight == 256) {
            return BLXCKOUTFX_ICON_BUTTON_LANGUAGE;
        }

        if (blxckoutfx$isTexture("minecraft", "textures/gui/accessibility.png")
                && this.blxckoutfx$xTexStart == 0
                && this.blxckoutfx$yTexStart == 0
                && this.blxckoutfx$textureWidth == 32
                && this.blxckoutfx$textureHeight == 64) {
            return BLXCKOUTFX_ICON_BUTTON_ACCESSIBILITY;
        }

        if (this.blxckoutfx$xTexStart == 0
                && this.blxckoutfx$yTexStart == 0
                && this.blxckoutfx$textureWidth == 32
                && this.blxckoutfx$textureHeight == 64) {
            if (blxckoutfx$isTexture("modmenu", "textures/gui/configure_button.png")) {
                return BLXCKOUTFX_ICON_BUTTON_MODMENU_CONFIGURE;
            }
            if (blxckoutfx$isTexture("modmenu", "textures/gui/filters_button.png")) {
                return BLXCKOUTFX_ICON_BUTTON_MODMENU_FILTERS;
            }
        }

        return 0;
    }

    @Unique
    private boolean blxckoutfx$isTexture(String namespace, String path) {
        return this.blxckoutfx$resourceLocation != null
                && namespace.equals(this.blxckoutfx$resourceLocation.getNamespace())
                && path.equals(this.blxckoutfx$resourceLocation.getPath());
    }

    @Unique
    private void blxckoutfx$storeState(ResourceLocation resourceLocation, int xTexStart, int yTexStart, int yDiffTex, int textureWidth, int textureHeight) {
        this.blxckoutfx$resourceLocation = resourceLocation;
        this.blxckoutfx$xTexStart = xTexStart;
        this.blxckoutfx$yTexStart = yTexStart;
        this.blxckoutfx$yDiffTex = yDiffTex;
        this.blxckoutfx$textureWidth = textureWidth;
        this.blxckoutfx$textureHeight = textureHeight;
    }
}
