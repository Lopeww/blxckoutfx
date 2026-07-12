package com.lopew.blxckoutfx.mixin.ae2;

import com.lopew.blxckoutfx.client.integration.ae2.BlxckoutFXAe2Support;
import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Pseudo
@Mixin(targets = "appeng.client.gui.me.common.MEStorageScreen", remap = false)
public abstract class Ae2CraftingTerminalSectionMixin {
    @Unique
    private boolean blxckoutfx$craftingSectionContextActive;

    @Inject(method = "drawBG", at = @At("HEAD"), require = 0)
    private void blxckoutfx$enterCraftingSectionContext(
            GuiGraphics guiGraphics,
            int offsetX,
            int offsetY,
            int mouseX,
            int mouseY,
            float partialTick,
            CallbackInfo ci
    ) {
        if (!blxckoutfx$shouldTrackCraftingSection()) {
            return;
        }

        Rect2i section = blxckoutfx$resolveCraftingSectionGuiRect(offsetX, offsetY);
        if (section == null || section.getWidth() <= 0 || section.getHeight() <= 0) {
            return;
        }

        int[] fbRect = blxckoutfx$toFramebufferRect(section);
        blxckoutfx$craftingSectionContextActive = true;
        BlxckoutFXRenderContext.enterAe2CraftingTerminalSectionRender(fbRect[0], fbRect[1], fbRect[2], fbRect[3]);
    }

    @Inject(method = "drawBG", at = @At("RETURN"), require = 0)
    private void blxckoutfx$exitCraftingSectionContext(
            GuiGraphics guiGraphics,
            int offsetX,
            int offsetY,
            int mouseX,
            int mouseY,
            float partialTick,
            CallbackInfo ci
    ) {
        if (!blxckoutfx$craftingSectionContextActive) {
            return;
        }
        blxckoutfx$craftingSectionContextActive = false;
        BlxckoutFXRenderContext.exitAe2CraftingTerminalSectionRender();
    }

    @Unique
    private boolean blxckoutfx$shouldTrackCraftingSection() {
        Screen screen = Minecraft.getInstance().screen;
        return BlxckoutFXRenderContext.isRenderingAe2Screen()
                && BlxckoutFXRenderContext.isRenderingAe2CraftingTermScreen()
                && BlxckoutFXAe2Support.isAe2CraftingTermScreenLike(screen)
                && BlxckoutFXAe2Support.isBlackoutAe2ThemeActive();
    }

    @Unique
    private Rect2i blxckoutfx$resolveCraftingSectionGuiRect(int offsetX, int offsetY) {
        Object menuObj = blxckoutfx$readField(this, "menu");
        if (!(menuObj instanceof AbstractContainerMenu menu)) {
            return null;
        }

        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;

        
        for (Slot slot : menu.slots) {
            String slotClass = slot.getClass().getName();
            if (!"appeng.menu.slot.CraftingMatrixSlot".equals(slotClass)
                    && !"appeng.menu.slot.CraftingTermSlot".equals(slotClass)) {
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

        
        Object widgetsObj = blxckoutfx$readField(this, "widgets");
        if (widgetsObj != null) {
            Object widgetMapObj = blxckoutfx$readField(widgetsObj, "widgets");
            if (widgetMapObj instanceof Map<?, ?> widgetMap) {
                int[] expanded = blxckoutfx$computeExpandedBounds(widgetMap.get("clearCraftingGrid"), widgetMap.get("clearToPlayerInv"),
                        minX, minY, maxX, maxY);
                minX = expanded[0];
                minY = expanded[1];
                maxX = expanded[2];
                maxY = expanded[3];
            }
        }

        
        minX -= 4;
        minY -= 4;
        maxX += 4;
        maxY += 4;

        return new Rect2i(minX, minY, Math.max(1, maxX - minX), Math.max(1, maxY - minY));
    }

    @Unique
    private static int[] blxckoutfx$computeExpandedBounds(
            Object clearCraftingGrid,
            Object clearToPlayerInv,
            int minX,
            int minY,
            int maxX,
            int maxY
    ) {
        int[] bounds = new int[]{minX, minY, maxX, maxY};
        blxckoutfx$includeWidgetRect(clearCraftingGrid, bounds);
        blxckoutfx$includeWidgetRect(clearToPlayerInv, bounds);
        return bounds;
    }

    @Unique
    private static void blxckoutfx$includeWidgetRect(Object widgetObj, int[] bounds) {
        if (!(widgetObj instanceof AbstractWidget widget)) {
            return;
        }

        int x = widget.getX();
        int y = widget.getY();
        int w = widget.getWidth();
        int h = widget.getHeight();
        if (w <= 0 || h <= 0) {
            return;
        }

        bounds[0] = Math.min(bounds[0], x);
        bounds[1] = Math.min(bounds[1], y);
        bounds[2] = Math.max(bounds[2], x + w);
        bounds[3] = Math.max(bounds[3], y + h);
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

