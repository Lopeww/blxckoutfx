package com.lopew.blxckoutfx.mixin.ae2;

import com.lopew.blxckoutfx.client.integration.ae2.BlxckoutFXAe2Support;
import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import com.lopew.blxckoutfx.client.render.BlxckoutFXShaders;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;












@Pseudo
@Mixin(targets = "appeng.client.gui.AEBaseScreen", remap = false)
public abstract class Ae2InterfaceConfigArrowMixin {
    @Unique
    private static final String INTERFACE_SCREEN_CLASS = "appeng.client.gui.implementations.InterfaceScreen";
    @Unique
    private static final String FAKE_SLOT_CLASS = "appeng.menu.slot.FakeSlot";

    @Unique
    private boolean blxckoutfx$configArrowContextActive;

    @Inject(method = "drawBG", at = @At("HEAD"), require = 0)
    private void blxckoutfx$enterConfigArrowContext(
            GuiGraphics guiGraphics,
            int offsetX,
            int offsetY,
            int mouseX,
            int mouseY,
            float partialTick,
            CallbackInfo ci
    ) {
        if (!blxckoutfx$shouldTrackConfigArrows()) {
            return;
        }

        Rect2i rect = blxckoutfx$resolveConfigSlotGuiRect(offsetX, offsetY);
        if (rect == null || rect.getWidth() <= 0 || rect.getHeight() <= 0) {
            return;
        }

        int[] fbRect = blxckoutfx$toFramebufferRect(rect);
        blxckoutfx$configArrowContextActive = true;
        BlxckoutFXRenderContext.enterAe2ConfigArrowRender(fbRect[0], fbRect[1], fbRect[2], fbRect[3]);
    }

    @Inject(method = "drawBG", at = @At("RETURN"), require = 0)
    private void blxckoutfx$exitConfigArrowContext(
            GuiGraphics guiGraphics,
            int offsetX,
            int offsetY,
            int mouseX,
            int mouseY,
            float partialTick,
            CallbackInfo ci
    ) {
        if (!blxckoutfx$configArrowContextActive) {
            return;
        }
        blxckoutfx$configArrowContextActive = false;
        BlxckoutFXRenderContext.exitAe2ConfigArrowRender();
    }

    @Unique
    private boolean blxckoutfx$shouldTrackConfigArrows() {
        
        
        if (!BlxckoutFXAe2Support.isBlxckoutAe2ThemeActive()) {
            return false;
        }
        
        
        if (!BlxckoutFXShaders.isEnabled()) {
            return false;
        }
        return INTERFACE_SCREEN_CLASS.equals(this.getClass().getName());
    }

    @Unique
    private Rect2i blxckoutfx$resolveConfigSlotGuiRect(int offsetX, int offsetY) {
        Object menuObj = blxckoutfx$readField(this, "menu");
        if (!(menuObj instanceof AbstractContainerMenu menu)) {
            return null;
        }

        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;

        
        for (Slot slot : menu.slots) {
            if (!FAKE_SLOT_CLASS.equals(slot.getClass().getName())) {
                continue;
            }

            if (slot.x < -1000 || slot.y < -1000) {
                continue;
            }

            int slotX = offsetX + slot.x;
            int slotY = offsetY + slot.y;
            minX = Math.min(minX, slotX);
            minY = Math.min(minY, slotY);
            maxX = Math.max(maxX, slotX + 16);
            maxY = Math.max(maxY, slotY + 16);
        }

        if (minX == Integer.MAX_VALUE) {
            return null;
        }

        
        
        
        return new Rect2i(minX, minY, Math.max(1, maxX - minX), Math.max(1, maxY - minY));
    }

    @Unique
    private static Object blxckoutfx$readField(Object target, String fieldName) {
        Class<?> current = target.getClass();
        while (current != null) {
            try {
                var field = current.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.get(target);
            } catch (ReflectiveOperationException ignored) {
                current = current.getSuperclass();
            }
        }
        return null;
    }

    @Unique
    private static int[] blxckoutfx$toFramebufferRect(Rect2i guiRect) {
        var window = Minecraft.getInstance().getWindow();
        double scale = window.getGuiScale();
        int framebufferHeight = window.getHeight();

        int x1 = (int) Math.floor(guiRect.getX() * scale);
        int x2 = (int) Math.ceil((guiRect.getX() + guiRect.getWidth()) * scale);
        int y1 = framebufferHeight - (int) Math.ceil((guiRect.getY() + guiRect.getHeight()) * scale);
        int y2 = framebufferHeight - (int) Math.floor(guiRect.getY() * scale);

        return new int[]{x1, y1, x2, y2};
    }
}

