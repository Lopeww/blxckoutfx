package com.lopew.blxckoutfx.mixin.jei;

import com.lopew.blxckoutfx.client.gui.BlxckoutFXGUIHandler;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "mezz.jei.gui.recipes.RecipesGui", remap = false)
public class JeiRecipesGuiMixin {
    @Inject(method = {"render", "m_88315_", "method_25394"}, at = @At("RETURN"), require = 0)
    private void blxckoutfx$renderInventoryButton(
            GuiGraphics graphics,
            int mouseX,
            int mouseY,
            float partialTick,
            CallbackInfo ci
    ) {
        BlxckoutFXGUIHandler.renderJeiInventoryButton(
                (net.minecraft.client.gui.screens.Screen) (Object) this,
                graphics,
                mouseX,
                mouseY,
                partialTick
        );
    }
}
