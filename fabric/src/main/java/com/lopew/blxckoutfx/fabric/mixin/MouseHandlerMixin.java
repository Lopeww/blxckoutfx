package com.lopew.blxckoutfx.fabric.mixin;

import com.lopew.blxckoutfx.BlxckoutFX;
import com.lopew.blxckoutfx.client.BlxckoutFXGUIHandler;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {
    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    private double xpos;

    @Shadow
    private double ypos;

    @Shadow
    private int activeButton;

    @Shadow
    private double mousePressedTime;

    private static boolean blxckoutfx$loggedTitleDragPath;

    @Inject(method = "onMove(JDD)V", at = @At("HEAD"))
    private void blxckoutfx$handleTitleScreenDrag(long windowPointer, double mouseX, double mouseY, CallbackInfo ci) {
        Screen screen = this.minecraft.screen;

        if (!(screen instanceof TitleScreen) || this.minecraft.getOverlay() != null || this.activeButton != 0 || this.mousePressedTime <= 0.0D) {
            return;
        }

        Window window = this.minecraft.getWindow();

        if (windowPointer != window.getWindow()) {
            return;
        }

        double scaledMouseX = mouseX * window.getGuiScaledWidth() / window.getScreenWidth();
        double scaledMouseY = mouseY * window.getGuiScaledHeight() / window.getScreenHeight();
        double scaledDragX = (mouseX - this.xpos) * window.getGuiScaledWidth() / window.getScreenWidth();
        double scaledDragY = (mouseY - this.ypos) * window.getGuiScaledHeight() / window.getScreenHeight();

        if (BlxckoutFXGUIHandler.onMouseDragged(screen, scaledMouseX, scaledMouseY, this.activeButton, scaledDragX, scaledDragY)
                && !blxckoutfx$loggedTitleDragPath) {
            blxckoutfx$loggedTitleDragPath = true;
            BlxckoutFX.LOGGER.info("BlxckoutFX Fabric title drag path active: screen={}, render=MouseHandler.onMove",
                    screen.getClass().getName());
        }
    }
}
