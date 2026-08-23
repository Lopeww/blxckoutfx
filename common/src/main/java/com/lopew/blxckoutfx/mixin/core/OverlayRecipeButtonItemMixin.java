package com.lopew.blxckoutfx.mixin.core;

import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.client.gui.screens.recipebook.OverlayRecipeComponent$OverlayRecipeButton")
public class OverlayRecipeButtonItemMixin {
    @Unique
    private int blxckoutfx$itemDarkeningSuppressDepth;

    @Inject(
            method = "renderWidget(Lnet/minecraft/client/gui/GuiGraphics;IIF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;renderItem(Lnet/minecraft/world/item/ItemStack;II)V",
                    shift = At.Shift.BEFORE
            )
    )
    private void blxckoutfx$preserveOverlayItem(
            GuiGraphics graphics,
            int mouseX,
            int mouseY,
            float partialTick,
            CallbackInfo ci
    ) {
        blxckoutfx$beginItemPreservation();
    }

    @Inject(
            method = "renderWidget(Lnet/minecraft/client/gui/GuiGraphics;IIF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;renderItem(Lnet/minecraft/world/item/ItemStack;II)V",
                    shift = At.Shift.AFTER
            )
    )
    private void blxckoutfx$restoreOverlayItem(
            GuiGraphics graphics,
            int mouseX,
            int mouseY,
            float partialTick,
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
