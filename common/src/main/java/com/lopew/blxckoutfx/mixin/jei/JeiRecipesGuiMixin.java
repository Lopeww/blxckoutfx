package com.lopew.blxckoutfx.mixin.jei;

import com.lopew.blxckoutfx.client.gui.BlxckoutFXGUIHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "mezz.jei.gui.recipes.RecipesGui", remap = false)
public class JeiRecipesGuiMixin {
    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lmezz/jei/gui/recipes/RecipeGuiTabs;draw(Lnet/minecraft/client/Minecraft;Lnet/minecraft/client/gui/GuiGraphics;IIF)V",
                    shift = At.Shift.AFTER
            ),
            require = 0
    )
    private void blxckoutfx$flushRecipeTabsBeforeTooltips(
            GuiGraphics graphics,
            int mouseX,
            int mouseY,
            float partialTick,
            CallbackInfo ci
    ) {
        // Tabs and option panels must finish before JEI starts its tooltip pass.
        graphics.flush();
    }

    @Inject(method = "render", at = @At("RETURN"), require = 0)
    private void blxckoutfx$renderInventoryButtonLast(
            GuiGraphics graphics,
            int mouseX,
            int mouseY,
            float partialTick,
            CallbackInfo ci
    ) {
        BlxckoutFXGUIHandler.renderJeiInventoryButton((Screen) (Object) this, graphics, mouseX, mouseY, partialTick);
    }
}
