package com.lopew.blxckoutfx.mixin.core;

import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
public class AbstractContainerRecipeBookMixin {
    @Inject(
            method = "render(Lnet/minecraft/client/gui/GuiGraphics;IIF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;renderLabels(Lnet/minecraft/client/gui/GuiGraphics;II)V",
                    shift = At.Shift.AFTER
            )
    )
    private void blxckoutfx$renderInventoryRecipeBookBeforeJeiForeground(
            GuiGraphics graphics,
            int mouseX,
            int mouseY,
            float partialTick,
            CallbackInfo ci
    ) {
        if (!((Object) this instanceof InventoryScreen inventoryScreen)) {
            return;
        }

        RecipeBookComponent recipeBook = inventoryScreen.getRecipeBookComponent();
        if (!recipeBook.isVisible()) {
            return;
        }

        int leftPos = recipeBook.updateScreenPosition(graphics.guiWidth(), 176);
        int topPos = (graphics.guiHeight() - 166) / 2;

        graphics.pose().pushPose();
        graphics.pose().translate(-leftPos, -topPos, 0.0F);
        try {
            recipeBook.render(graphics, mouseX, mouseY, partialTick);
            recipeBook.renderGhostRecipe(graphics, leftPos, topPos, false, partialTick);
        } finally {
            graphics.pose().popPose();
        }
        BlxckoutFXRenderContext.enterInventoryRecipeBookPreForegroundRender();
    }
}
