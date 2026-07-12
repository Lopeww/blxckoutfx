package com.lopew.blxckoutfx.mixin.ae2;

import com.lopew.blxckoutfx.client.integration.ae2.BlxckoutFXAe2Support;
import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import com.lopew.blxckoutfx.client.render.BlxckoutFXShaders;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Pseudo
@Mixin(targets = "appeng.client.gui.AEBaseScreen", remap = false)
public abstract class Ae2InterfaceConfigArrowMixin {
    @Unique
    private static final String INTERFACE_SCREEN_CLASS = "appeng.client.gui.implementations.InterfaceScreen";
    @Unique
    private static final String EXTENDED_INTERFACE_SCREEN_CLASS =
            "com.glodblock.github.extendedae.client.gui.GuiExInterface";
    @Unique
    private static final String FAKE_SLOT_CLASS = "appeng.menu.slot.FakeSlot";
    @Unique
    private boolean blxckoutfx$configArrowContextActive;

    @Inject(method = "drawBG", at = @At("HEAD"), require = 0)
    private void blxckoutfx$enterConfigSlotFocus(
            @Coerce Object guiGraphics,
            int offsetX,
            int offsetY,
            int mouseX,
            int mouseY,
            float partialTick,
            CallbackInfo ci
    ) {
        if (!blxckoutfx$shouldTrackConfigArrows() || blxckoutfx$configArrowContextActive) {
            return;
        }

        List<Rect2i> rects = blxckoutfx$resolveConfigSlotGuiRects(offsetX, offsetY);
        if (rects.isEmpty()) {
            return;
        }

        int[] firstRect = blxckoutfx$toFramebufferRect(rects.getFirst());
        int[] secondRect = rects.size() > 1
                ? blxckoutfx$toFramebufferRect(rects.get(1))
                : new int[]{0, 0, 0, 0};
        blxckoutfx$configArrowContextActive = true;
        BlxckoutFXRenderContext.enterAe2ConfigArrowRender(
                firstRect[0],
                firstRect[1],
                firstRect[2],
                firstRect[3],
                secondRect[0],
                secondRect[1],
                secondRect[2],
                secondRect[3]
        );
    }

    @Inject(method = "render", at = @At("RETURN"), require = 0)
    private void blxckoutfx$exitConfigSlotFocus(
            @Coerce Object guiGraphics,
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
        String screenClass = this.getClass().getName();
        return BlxckoutFXAe2Support.isBlxckoutThemeActive()
                && BlxckoutFXShaders.isEnabled()
                && (INTERFACE_SCREEN_CLASS.equals(screenClass)
                || EXTENDED_INTERFACE_SCREEN_CLASS.equals(screenClass));
    }

    @Unique
    private List<Rect2i> blxckoutfx$resolveConfigSlotGuiRects(int offsetX, int offsetY) {
        Object menuObj = blxckoutfx$readField(this, "menu");
        if (!(menuObj instanceof AbstractContainerMenu menu)) {
            return List.of();
        }

        List<Rect2i> slotRects = new ArrayList<>();

        for (Slot slot : menu.slots) {
            if (!FAKE_SLOT_CLASS.equals(slot.getClass().getName())) {
                continue;
            }

            if (slot.x < -1000 || slot.y < -1000) {
                continue;
            }

            int slotX = offsetX + slot.x;
            int slotY = offsetY + slot.y;
            slotRects.add(new Rect2i(slotX, slotY, 16, 16));
        }

        if (slotRects.isEmpty()) {
            return List.of();
        }

        slotRects.sort(Comparator.comparingInt(Rect2i::getY).thenComparingInt(Rect2i::getX));
        List<Rect2i> rows = new ArrayList<>(2);
        int index = 0;
        while (index < slotRects.size() && rows.size() < 2) {
            Rect2i first = slotRects.get(index);
            int rowY = first.getY();
            int minX = first.getX();
            int maxX = first.getX() + first.getWidth();
            index++;

            while (index < slotRects.size() && slotRects.get(index).getY() == rowY) {
                Rect2i slot = slotRects.get(index++);
                minX = Math.min(minX, slot.getX());
                maxX = Math.max(maxX, slot.getX() + slot.getWidth());
            }

            rows.add(new Rect2i(minX, rowY, Math.max(1, maxX - minX), 16));
        }
        return rows;
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
