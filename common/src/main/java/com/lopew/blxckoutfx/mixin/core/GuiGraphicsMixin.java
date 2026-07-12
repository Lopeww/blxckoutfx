package com.lopew.blxckoutfx.mixin.core;

import com.lopew.blxckoutfx.client.integration.ae2.BlxckoutFXAe2Support;
import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import net.minecraft.client.gui.Font;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mixin(GuiGraphics.class)
public class GuiGraphicsMixin {
    private static final String AE2_VERTICAL_BUTTONS_BG_PATH = "vertical_buttons_bg";
    private static final String AE2_PATTERN_MODES_BG_PATH = "guis/pattern_modes.png";
    private static final String MODMENU_CONFIGURE_BUTTON_PATH = "textures/gui/configure_button.png";
    private static final String MODMENU_FILTERS_BUTTON_PATH = "textures/gui/filters_button.png";

    @Inject(method = "innerBlit(Lnet/minecraft/resources/ResourceLocation;IIIIIFFFF)V", at = @At("HEAD"))
    private void blxckoutfx$enterAe2TextureRender(
            ResourceLocation atlasLocation,
            int x1,
            int x2,
            int y1,
            int y2,
            int blitOffset,
            float minU,
            float maxU,
            float minV,
            float maxV,
            CallbackInfo ci
    ) {
        if (BlxckoutFXAe2Support.isAe2Texture(atlasLocation)) {
            BlxckoutFXRenderContext.enterAe2TextureRender();
        }
    }

    @Inject(method = "innerBlit(Lnet/minecraft/resources/ResourceLocation;IIIIIFFFF)V", at = @At("RETURN"))
    private void blxckoutfx$exitAe2TextureRender(
            ResourceLocation atlasLocation,
            int x1,
            int x2,
            int y1,
            int y2,
            int blitOffset,
            float minU,
            float maxU,
            float minV,
            float maxV,
            CallbackInfo ci
    ) {
        if (BlxckoutFXAe2Support.isAe2Texture(atlasLocation)) {
            BlxckoutFXRenderContext.exitAe2TextureRender();
        }
    }

    @Inject(method = "innerBlit(Lnet/minecraft/resources/ResourceLocation;IIIIIFFFF)V", at = @At("HEAD"))
    private void blxckoutfx$suppressModMenuIconDarkening(
            ResourceLocation atlasLocation,
            int x1,
            int x2,
            int y1,
            int y2,
            int blitOffset,
            float minU,
            float maxU,
            float minV,
            float maxV,
            CallbackInfo ci
    ) {
        if (isModsScreenLogo(atlasLocation)) {
            BlxckoutFXRenderContext.suppressTextureDarkening();
        }
    }

    @Inject(method = "innerBlit(Lnet/minecraft/resources/ResourceLocation;IIIIIFFFF)V", at = @At("RETURN"))
    private void blxckoutfx$restoreModMenuIconDarkening(
            ResourceLocation atlasLocation,
            int x1,
            int x2,
            int y1,
            int y2,
            int blitOffset,
            float minU,
            float maxU,
            float minV,
            float maxV,
            CallbackInfo ci
    ) {
        if (isModsScreenLogo(atlasLocation)) {
            BlxckoutFXRenderContext.allowTextureDarkening();
        }
    }

    @Inject(method = "innerBlit(Lnet/minecraft/resources/ResourceLocation;IIIIIFFFF)V", at = @At("HEAD"))
    private void blxckoutfx$enterModMenuIconButtonRender(
            ResourceLocation atlasLocation,
            int x1,
            int x2,
            int y1,
            int y2,
            int blitOffset,
            float minU,
            float maxU,
            float minV,
            float maxV,
            CallbackInfo ci
    ) {
        if (isModMenuIconButtonTexture(atlasLocation)) {
            BlxckoutFXRenderContext.enterModMenuIconButtonRender();
        }
    }

    @Inject(method = "innerBlit(Lnet/minecraft/resources/ResourceLocation;IIIIIFFFF)V", at = @At("RETURN"))
    private void blxckoutfx$exitModMenuIconButtonRender(
            ResourceLocation atlasLocation,
            int x1,
            int x2,
            int y1,
            int y2,
            int blitOffset,
            float minU,
            float maxU,
            float minV,
            float maxV,
            CallbackInfo ci
    ) {
        if (isModMenuIconButtonTexture(atlasLocation)) {
            BlxckoutFXRenderContext.exitModMenuIconButtonRender();
        }
    }

    @Inject(method = "blitSprite(Lnet/minecraft/resources/ResourceLocation;IIIII)V", at = @At("HEAD"), require = 0)
    private void blxckoutfx$enterAe2VerticalButtonsBackground(
            ResourceLocation sprite,
            int x,
            int y,
            int blitOffset,
            int width,
            int height,
            CallbackInfo ci
    ) {
        if (blxckoutfx$isAe2VerticalButtonsBackgroundSprite(sprite)) {
            BlxckoutFXRenderContext.enterAe2VerticalButtonBarBackgroundRender();
        }
    }

    @Inject(method = "blitSprite(Lnet/minecraft/resources/ResourceLocation;IIIII)V", at = @At("RETURN"), require = 0)
    private void blxckoutfx$exitAe2VerticalButtonsBackground(
            ResourceLocation sprite,
            int x,
            int y,
            int blitOffset,
            int width,
            int height,
            CallbackInfo ci
    ) {
        if (blxckoutfx$isAe2VerticalButtonsBackgroundSprite(sprite)) {
            BlxckoutFXRenderContext.exitAe2VerticalButtonBarBackgroundRender();
        }
    }

    @Inject(method = "innerBlit(Lnet/minecraft/resources/ResourceLocation;IIIIIFFFF)V", at = @At("HEAD"))
    private void blxckoutfx$enterAe2PatternModesBackground(
            ResourceLocation atlasLocation,
            int x1,
            int x2,
            int y1,
            int y2,
            int blitOffset,
            float minU,
            float maxU,
            float minV,
            float maxV,
            CallbackInfo ci
    ) {
        if (blxckoutfx$isAe2PatternModesBackground(atlasLocation)) {
            BlxckoutFXRenderContext.enterAe2PatternModesBackgroundRender();
        }
    }

    @Inject(method = "innerBlit(Lnet/minecraft/resources/ResourceLocation;IIIIIFFFF)V", at = @At("RETURN"))
    private void blxckoutfx$exitAe2PatternModesBackground(
            ResourceLocation atlasLocation,
            int x1,
            int x2,
            int y1,
            int y2,
            int blitOffset,
            float minU,
            float maxU,
            float minV,
            float maxV,
            CallbackInfo ci
    ) {
        if (blxckoutfx$isAe2PatternModesBackground(atlasLocation)) {
            BlxckoutFXRenderContext.exitAe2PatternModesBackgroundRender();
        }
    }

    @Inject(
            method = "renderTooltip(Lnet/minecraft/client/gui/Font;Ljava/util/List;Ljava/util/Optional;II)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void blxckoutfx$deferPneumaticCraftComponentTooltip(
            Font font,
            List<Component> tooltip,
            Optional<TooltipComponent> visualTooltipComponent,
            int mouseX,
            int mouseY,
            CallbackInfo ci
    ) {
        if (!shouldDeferPneumaticCraftTooltip() || tooltip == null || tooltip.isEmpty() || visualTooltipComponent.isPresent()) {
            return;
        }

        Screen screen = Minecraft.getInstance().screen;
        if (screen == null) {
            return;
        }

        List<FormattedCharSequence> lines = new ArrayList<>(tooltip.size());
        for (Component component : tooltip) {
            lines.add(component.getVisualOrderText());
        }

        screen.setTooltipForNextRenderPass(lines);
        ci.cancel();
    }

    @Inject(
            method = "renderTooltip(Lnet/minecraft/client/gui/Font;Ljava/util/List;II)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void blxckoutfx$deferPneumaticCraftFormattedTooltip(
            Font font,
            List<? extends FormattedCharSequence> tooltip,
            int mouseX,
            int mouseY,
            CallbackInfo ci
    ) {
        if (!shouldDeferPneumaticCraftTooltip() || tooltip == null || tooltip.isEmpty()) {
            return;
        }

        Screen screen = Minecraft.getInstance().screen;
        if (screen == null) {
            return;
        }

        screen.setTooltipForNextRenderPass(new ArrayList<>(tooltip));
        ci.cancel();
    }

    private static boolean isModsScreenLogo(ResourceLocation atlasLocation) {
        String namespace = atlasLocation.getNamespace();
        String path = atlasLocation.getPath();

        if (isFabricModMenuScreen()) {
            return ("modmenu".equals(namespace) && path.endsWith("_icon"))
                    || ("minecraft".equals(namespace) && "textures/misc/unknown_pack.png".equals(path));
        }

        if (isNeoForgeModListScreen()) {
            return "minecraft".equals(namespace)
                    && (path.startsWith("dynamic/modlogo_")
                    || "textures/gui/title/minecraft.png".equals(path)
                    || "textures/gui/title/minceraft.png".equals(path));
        }

        return false;
    }

    private static boolean isFabricModMenuScreen() {
        Screen screen = Minecraft.getInstance().screen;
        return screen != null && screen.getClass().getName().startsWith("com.terraformersmc.modmenu.gui.");
    }

    private static boolean isModMenuIconButtonTexture(ResourceLocation atlasLocation) {
        if (!isFabricModMenuScreen() || atlasLocation == null || !"modmenu".equals(atlasLocation.getNamespace())) {
            return false;
        }

        String path = atlasLocation.getPath();
        return MODMENU_CONFIGURE_BUTTON_PATH.equals(path) || MODMENU_FILTERS_BUTTON_PATH.equals(path);
    }

    private static boolean isNeoForgeModListScreen() {
        Screen screen = Minecraft.getInstance().screen;
        return screen != null && "net.neoforged.neoforge.client.gui.ModListScreen".equals(screen.getClass().getName());
    }

    private static boolean shouldDeferPneumaticCraftTooltip() {
        Screen screen = Minecraft.getInstance().screen;
        return screen != null
                && screen.getClass().getName().startsWith("me.desht.pneumaticcraft.client.gui.")
                && BlxckoutFXRenderContext.isRenderingPneumaticCraftWidget();
    }

    private static boolean blxckoutfx$isAe2VerticalButtonsBackgroundSprite(ResourceLocation sprite) {
        return sprite != null
                && BlxckoutFXRenderContext.isRenderingAe2Screen()
                && BlxckoutFXAe2Support.isBlackoutAe2ThemeActive()
                && "ae2".equals(sprite.getNamespace())
                && AE2_VERTICAL_BUTTONS_BG_PATH.equals(sprite.getPath());
    }

    private static boolean blxckoutfx$isAe2PatternModesBackground(ResourceLocation atlasLocation) {
        if (atlasLocation == null || !BlxckoutFXRenderContext.isRenderingAe2Screen()) {
            return false;
        }

        if (!BlxckoutFXAe2Support.isBlackoutAe2ThemeActive()) {
            return false;
        }

        if (!BlxckoutFXAe2Support.isAe2Texture(atlasLocation)) {
            return false;
        }

        String path = atlasLocation.getPath();
        return path != null && path.endsWith(AE2_PATTERN_MODES_BG_PATH);
    }

}

