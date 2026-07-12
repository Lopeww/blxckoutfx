package com.lopew.blxckoutfx.mixin.ae2;

import com.lopew.blxckoutfx.client.integration.ae2.BlxckoutFXAe2Support;
import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import com.lopew.blxckoutfx.client.render.BlxckoutFXShaders;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "appeng.client.gui.AEBaseScreen", remap = false)
public abstract class Ae2MolecularAssemblerOptionalSlotMixin {
    @Unique
    private static final String MOLECULAR_ASSEMBLER_SCREEN_CLASS = "appeng.client.gui.implementations.MolecularAssemblerScreen";
    @Unique
    private static final String MOLECULAR_ASSEMBLER_PATTERN_SLOT_CLASS = "appeng.menu.slot.MolecularAssemblerPatternSlot";
    @Unique
    private static final String EXTENDED_AE_MOLECULAR_ASSEMBLER_SCREEN_CLASS =
            "com.glodblock.github.extendedae.client.gui.GuiExMolecularAssembler";
    @Unique
    private static final String EXTENDED_AE_MOLECULAR_ASSEMBLER_PATTERN_SLOT_CLASS =
            "com.glodblock.github.extendedae.container.ContainerExMolecularAssembler$ExMolecularAssemblerPatternSlot";
    @Unique
    private static final int AE2_SLOT_BACKGROUND_SIZE = 18;

    @Unique
    private boolean blxckoutfx$molecularAssemblerGridContextActive;

    @Inject(method = "drawOptionalSlotBackground", at = @At("HEAD"), require = 0)
    private void blxckoutfx$enterMolecularAssemblerOptionalSlotRender(
            @Coerce Object guiGraphics,
            @Coerce Object slot,
            boolean alwaysDraw,
            CallbackInfo ci
    ) {
        if (!blxckoutfx$shouldTrackMolecularAssemblerSlot(slot)) {
            return;
        }

        int leftPos = blxckoutfx$readIntField(this, "leftPos", 0);
        int topPos = blxckoutfx$readIntField(this, "topPos", 0);
        int[] backgroundPos = blxckoutfx$resolveBackgroundPos(slot);
        if (backgroundPos == null) {
            return;
        }

        int x1 = leftPos + backgroundPos[0];
        int y1 = topPos + backgroundPos[1];
        int[] framebufferRect = blxckoutfx$toFramebufferRect(
                x1,
                y1,
                x1 + AE2_SLOT_BACKGROUND_SIZE,
                y1 + AE2_SLOT_BACKGROUND_SIZE
        );

        blxckoutfx$molecularAssemblerGridContextActive = true;
        BlxckoutFXRenderContext.enterAe2MolecularAssemblerGridRender(
                framebufferRect[0],
                framebufferRect[1],
                framebufferRect[2],
                framebufferRect[3]
        );
    }

    @Inject(method = "drawOptionalSlotBackground", at = @At("RETURN"), require = 0)
    private void blxckoutfx$exitMolecularAssemblerOptionalSlotRender(
            @Coerce Object guiGraphics,
            @Coerce Object slot,
            boolean alwaysDraw,
            CallbackInfo ci
    ) {
        if (!blxckoutfx$molecularAssemblerGridContextActive) {
            return;
        }

        blxckoutfx$molecularAssemblerGridContextActive = false;
        BlxckoutFXRenderContext.exitAe2MolecularAssemblerGridRender();
    }

    @Unique
    private boolean blxckoutfx$shouldTrackMolecularAssemblerSlot(Object slot) {
        return BlxckoutFXRenderContext.isRenderingAe2Screen()
                && BlxckoutFXAe2Support.isBlxckoutAe2ThemeActive()
                && BlxckoutFXShaders.isEnabled()
                && blxckoutfx$isSupportedMolecularAssemblerScreen(this)
                && blxckoutfx$isSupportedMolecularAssemblerSlot(slot);
    }

    @Unique
    private static boolean blxckoutfx$isSupportedMolecularAssemblerScreen(Object screen) {
        return blxckoutfx$isClassOrSuperclass(screen, MOLECULAR_ASSEMBLER_SCREEN_CLASS)
                || blxckoutfx$isClassOrSuperclass(screen, EXTENDED_AE_MOLECULAR_ASSEMBLER_SCREEN_CLASS);
    }

    @Unique
    private static boolean blxckoutfx$isSupportedMolecularAssemblerSlot(Object slot) {
        return blxckoutfx$isClassOrSuperclass(slot, MOLECULAR_ASSEMBLER_PATTERN_SLOT_CLASS)
                || blxckoutfx$isClassOrSuperclass(slot, EXTENDED_AE_MOLECULAR_ASSEMBLER_PATTERN_SLOT_CLASS);
    }

    @Unique
    private static int[] blxckoutfx$resolveBackgroundPos(Object slot) {
        try {
            Object point = slot.getClass().getMethod("getBackgroundPos").invoke(slot);
            if (point == null) {
                return null;
            }

            Integer x = blxckoutfx$invokeInt(point, "getX");
            Integer y = blxckoutfx$invokeInt(point, "getY");
            if (x != null && y != null) {
                return new int[]{x, y};
            }
        } catch (ReflectiveOperationException ignored) {
            
        }

        Integer slotX = blxckoutfx$readIntegerField(slot, "x");
        Integer slotY = blxckoutfx$readIntegerField(slot, "y");
        if (slotX == null || slotY == null) {
            return null;
        }

        return new int[]{slotX - 1, slotY - 1};
    }

    @Unique
    private static boolean blxckoutfx$isClassOrSuperclass(Object value, String className) {
        if (value == null) {
            return false;
        }

        Class<?> current = value.getClass();
        while (current != null) {
            if (className.equals(current.getName())) {
                return true;
            }
            current = current.getSuperclass();
        }
        return false;
    }

    @Unique
    private static Integer blxckoutfx$invokeInt(Object target, String methodName) throws ReflectiveOperationException {
        Object value = target.getClass().getMethod(methodName).invoke(target);
        return value instanceof Number number ? number.intValue() : null;
    }

    @Unique
    private static int blxckoutfx$readIntField(Object target, String fieldName, int fallback) {
        Integer value = blxckoutfx$readIntegerField(target, fieldName);
        return value == null ? fallback : value;
    }

    @Unique
    private static Integer blxckoutfx$readIntegerField(Object target, String fieldName) {
        Object value = blxckoutfx$readField(target, fieldName);
        return value instanceof Number number ? number.intValue() : null;
    }

    @Unique
    private static Object blxckoutfx$readField(Object target, String fieldName) {
        Class<?> current = target == null ? null : target.getClass();
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
    private static int[] blxckoutfx$toFramebufferRect(int x1, int y1, int x2, int y2) {
        var window = Minecraft.getInstance().getWindow();
        double scale = window.getGuiScale();
        int framebufferHeight = window.getHeight();

        int fbX1 = (int) Math.floor(x1 * scale);
        int fbX2 = (int) Math.ceil(x2 * scale);
        int fbY1 = framebufferHeight - (int) Math.ceil(y2 * scale);
        int fbY2 = framebufferHeight - (int) Math.floor(y1 * scale);

        return new int[]{fbX1, fbY1, fbX2, fbY2};
    }
}

