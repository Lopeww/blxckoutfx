package com.lopew.blxckoutfx.mixin.core;

import com.lopew.blxckoutfx.client.integration.ae2.BlxckoutFXAe2Support;
import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
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
        if (isModsScreenLogo(atlasLocation)) {
            BlxckoutFXRenderContext.suppressTextureDarkening();
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
        if (blxckoutfx$isAe2Texture(atlasLocation)) {
            BlxckoutFXRenderContext.exitAe2TextureRender();
        }
        if (isModsScreenLogo(atlasLocation)) {
            BlxckoutFXRenderContext.allowTextureDarkening();
        }
    }

    @Inject(
            method = "renderTooltip(Lnet/minecraft/client/gui/Font;Ljava/util/List;Ljava/util/Optional;II)V",
            at = @At("HEAD"),
            cancellable = true,
            require = 0
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
            cancellable = true,
            require = 0
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

    private static boolean blxckoutfx$isAe2Texture(ResourceLocation atlasLocation) {
        return BlxckoutFXAe2Support.isAe2Texture(atlasLocation);
    }

    private static boolean isModsScreenLogo(ResourceLocation atlasLocation) {
        if (atlasLocation == null) {
            return false;
        }

        String namespace = atlasLocation.getNamespace();
        String path = atlasLocation.getPath();

        if (isFabricModMenuScreen()) {
            return ("modmenu".equals(namespace) && path.endsWith("_icon"))
                    || ("minecraft".equals(namespace) && "textures/misc/unknown_pack.png".equals(path));
        }

        if (isForgeModListScreen()) {
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

    private static boolean isForgeModListScreen() {
        Screen screen = Minecraft.getInstance().screen;
        return screen != null && screen.getClass().getName().startsWith("net.minecraftforge.client.gui.ModListScreen");
    }

    private static boolean shouldDeferPneumaticCraftTooltip() {
        Screen screen = Minecraft.getInstance().screen;
        return screen != null
                && screen.getClass().getName().startsWith("me.desht.pneumaticcraft.client.gui.")
                && BlxckoutFXRenderContext.isRenderingPneumaticCraftWidget();
    }
}
