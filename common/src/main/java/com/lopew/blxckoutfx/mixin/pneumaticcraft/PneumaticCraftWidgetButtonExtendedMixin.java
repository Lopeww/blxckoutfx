package com.lopew.blxckoutfx.mixin.pneumaticcraft;

import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "me.desht.pneumaticcraft.client.gui.widget.WidgetButtonExtended", remap = false)
public class PneumaticCraftWidgetButtonExtendedMixin {
    @Unique
    private boolean blxckoutfx$cappedThermoDumpButtonDarkening;

    @Inject(method = "renderWidget", at = @At("HEAD"), require = 0)
    private void blxckoutfx$capThermoDumpButtonDarkening(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        if (!blxckoutfx$isThermoDumpButton()) {
            return;
        }

        blxckoutfx$cappedThermoDumpButtonDarkening = true;
        BlxckoutFXRenderContext.enterBalancedCappedDarkening();
    }

    @Inject(method = "renderWidget", at = @At("RETURN"), require = 0)
    private void blxckoutfx$restoreThermoDumpButtonDarkeningCap(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        if (!blxckoutfx$cappedThermoDumpButtonDarkening) {
            return;
        }

        blxckoutfx$cappedThermoDumpButtonDarkening = false;
        BlxckoutFXRenderContext.exitBalancedCappedDarkening();
    }

    @Unique
    private boolean blxckoutfx$isThermoDumpButton() {
        Screen screen = Minecraft.getInstance().screen;
        if (screen == null || !"me.desht.pneumaticcraft.client.gui.ThermoPlantScreen".equals(screen.getClass().getName())) {
            return false;
        }

        try {
            Object tag = this.getClass().getMethod("getTag").invoke(this);
            return "dump".equals(tag);
        } catch (ReflectiveOperationException ignored) {
            return false;
        }
    }
}

