package com.lopew.blxckoutfx.mixin.core;

import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(InventoryScreen.class)
public class InventoryScreenRecipeBookMixin {
    @Redirect(
            method = "render(Lnet/minecraft/client/gui/GuiGraphics;IIF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screens/recipebook/RecipeBookComponent;render(Lnet/minecraft/client/gui/GuiGraphics;IIF)V"
            )
    )
    private void blxckoutfx$skipLateInventoryRecipeBookRender(
            RecipeBookComponent recipeBook,
            GuiGraphics graphics,
            int mouseX,
            int mouseY,
            float partialTick
    ) {
        if (!BlxckoutFXRenderContext.isRenderingInventoryRecipeBookPreForeground()) {
            recipeBook.render(graphics, mouseX, mouseY, partialTick);
        }
    }

    @Redirect(
            method = "render(Lnet/minecraft/client/gui/GuiGraphics;IIF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screens/recipebook/RecipeBookComponent;renderGhostRecipe(Lnet/minecraft/client/gui/GuiGraphics;IIZF)V"
            )
    )
    private void blxckoutfx$skipLateInventoryRecipeBookGhostRender(
            RecipeBookComponent recipeBook,
            GuiGraphics graphics,
            int leftPos,
            int topPos,
            boolean placeRecipe,
            float partialTick
    ) {
        if (!BlxckoutFXRenderContext.isRenderingInventoryRecipeBookPreForeground()) {
            recipeBook.renderGhostRecipe(graphics, leftPos, topPos, placeRecipe, partialTick);
            return;
        }

        BlxckoutFXRenderContext.exitInventoryRecipeBookPreForegroundRender();
    }
}
