package com.lopew.blxckoutfx.mixin.core;

import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookTabButton;
import net.minecraft.client.renderer.entity.ItemRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RecipeBookTabButton.class)
public class RecipeBookTabButtonItemMixin {
    @Unique
    private int blxckoutfx$itemDarkeningSuppressDepth;

    @Inject(
            method = "renderIcon(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/renderer/entity/ItemRenderer;)V",
            at = @At("HEAD")
    )
    private void blxckoutfx$preserveInventoryRecipeBookTabIcon(
            GuiGraphics graphics,
            ItemRenderer itemRenderer,
            CallbackInfo ci
    ) {
        blxckoutfx$beginItemPreservation();
    }

    @Inject(
            method = "renderIcon(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/renderer/entity/ItemRenderer;)V",
            at = @At("RETURN")
    )
    private void blxckoutfx$restoreInventoryRecipeBookTabIcon(
            GuiGraphics graphics,
            ItemRenderer itemRenderer,
            CallbackInfo ci
    ) {
        blxckoutfx$endItemPreservation();
    }

    @Unique
    private void blxckoutfx$beginItemPreservation() {
        if (!blxckoutfx$isInventoryScreen()) {
            return;
        }

        blxckoutfx$itemDarkeningSuppressDepth++;
        BlxckoutFXRenderContext.suppressTextureDarkening();
    }

    @Unique
    private void blxckoutfx$endItemPreservation() {
        if (blxckoutfx$itemDarkeningSuppressDepth <= 0) {
            return;
        }

        blxckoutfx$itemDarkeningSuppressDepth--;
        BlxckoutFXRenderContext.allowTextureDarkening();
    }

    @Unique
    private static boolean blxckoutfx$isInventoryScreen() {
        Screen screen = Minecraft.getInstance().screen;
        return screen != null && screen.getClass() == InventoryScreen.class;
    }
}
