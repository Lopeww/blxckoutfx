package com.lopew.blxckoutfx.mixin.ae2;

import com.lopew.blxckoutfx.client.integration.ae2.BlxckoutFXAe2Support;
import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "appeng.client.gui.AEBaseScreen", remap = false)
public class Ae2StorageCellSlotMixin {
    @Unique
    private boolean blxckoutfx$slotRemapActive;
    @Unique
    private boolean blxckoutfx$optionalSlotBackgroundRemapActive;

    @Inject(method = "renderAppEngSlot", at = @At("HEAD"), require = 0)
    private void blxckoutfx$enterSlotRemap(
            GuiGraphicsExtractor guiGraphics,
            @Coerce Object slot,
            int mouseX,
            int mouseY,
            CallbackInfo ci
    ) {
        if (!BlxckoutFXAe2Support.isBlackoutAe2ThemeActive()
                || !BlxckoutFXRenderContext.isRenderingAe2Screen()
                || !blxckoutfx$shouldRemapSlot(slot)) {
            return;
        }

        this.blxckoutfx$slotRemapActive = true;
        BlxckoutFXRenderContext.enterAe2CustomRemapRender();
    }

    @Inject(method = "renderAppEngSlot", at = @At("RETURN"), require = 0)
    private void blxckoutfx$exitSlotRemap(
            GuiGraphicsExtractor guiGraphics,
            @Coerce Object slot,
            int mouseX,
            int mouseY,
            CallbackInfo ci
    ) {
        if (!this.blxckoutfx$slotRemapActive) {
            return;
        }

        this.blxckoutfx$slotRemapActive = false;
        BlxckoutFXRenderContext.exitAe2CustomRemapRender();
    }

    @Inject(method = "drawOptionalSlotBackground", at = @At("HEAD"), require = 0)
    private void blxckoutfx$enterOptionalSlotBackgroundRemap(
            GuiGraphicsExtractor guiGraphics,
            @Coerce Object slot,
            boolean renderDisabled,
            CallbackInfo ci
    ) {
        if (!BlxckoutFXAe2Support.isBlackoutAe2ThemeActive()
                || !BlxckoutFXRenderContext.isRenderingAe2Screen()
                || !blxckoutfx$shouldRemapSlot(slot)) {
            return;
        }

        this.blxckoutfx$optionalSlotBackgroundRemapActive = true;
        BlxckoutFXRenderContext.enterAe2CustomRemapRender();
    }

    @Inject(method = "drawOptionalSlotBackground", at = @At("RETURN"), require = 0)
    private void blxckoutfx$exitOptionalSlotBackgroundRemap(
            GuiGraphicsExtractor guiGraphics,
            @Coerce Object slot,
            boolean renderDisabled,
            CallbackInfo ci
    ) {
        if (!this.blxckoutfx$optionalSlotBackgroundRemapActive) {
            return;
        }

        this.blxckoutfx$optionalSlotBackgroundRemapActive = false;
        BlxckoutFXRenderContext.exitAe2CustomRemapRender();
    }

    @Unique
    private static boolean blxckoutfx$shouldRemapSlot(Object slot) {
        return blxckoutfx$isRestrictedInputSlotType(slot, "STORAGE_CELLS")
                || blxckoutfx$isRestrictedInputSlotType(slot, "FUEL")
                || blxckoutfx$isRestrictedInputSlotType(slot, "UPGRADE")
                || blxckoutfx$isRestrictedInputSlotType(slot, "UPGRADES")
                || blxckoutfx$isRestrictedInputSlotType(slot, "TRASH")
                || blxckoutfx$isRestrictedInputSlotType(slot, "STORAGE_COMPONENT")
                || blxckoutfx$isRestrictedInputSlotType(slot, "BLANK_PATTERN")
                || blxckoutfx$isRestrictedInputSlotType(slot, "ENCODED_PATTERN")
                || blxckoutfx$hasSemantic(slot, "CRAFTING_GRID")
                || blxckoutfx$hasSemantic(slot, "CRAFTING_RESULT")
                || blxckoutfx$hasSemantic(slot, "PROCESSING_INPUTS")
                || blxckoutfx$hasSemantic(slot, "PROCESSING_OUTPUTS")
                || blxckoutfx$hasSemantic(slot, "SMITHING_TABLE_TEMPLATE")
                || blxckoutfx$hasSemantic(slot, "SMITHING_TABLE_BASE")
                || blxckoutfx$hasSemantic(slot, "SMITHING_TABLE_ADDITION")
                || blxckoutfx$hasSemantic(slot, "STONECUTTING_INPUT")
                || blxckoutfx$hasSemantic(slot, "BLANK_PATTERN")
                || blxckoutfx$hasSemantic(slot, "ENCODED_PATTERN")
                || blxckoutfx$hasSemantic(slot, "STORAGE")
                || blxckoutfx$hasSemantic(slot, "INSCRIBER_PLATE_TOP")
                || blxckoutfx$hasSemantic(slot, "INSCRIBER_PLATE_BOTTOM")
                || blxckoutfx$hasSemantic(slot, "MACHINE_INPUT")
                || blxckoutfx$hasSemantic(slot, "MACHINE_OUTPUT")
                || blxckoutfx$hasSemantic(slot, "UPGRADE");
    }

    @Unique
    private static boolean blxckoutfx$isRestrictedInputSlotType(Object slot, String typeName) {
        if (slot == null) {
            return false;
        }

        try {
            Class<?> currentClass = slot.getClass();
            while (currentClass != null) {
                if ("appeng.menu.slot.RestrictedInputSlot".equals(currentClass.getName())) {
                    var whichField = currentClass.getDeclaredField("which");
                    whichField.setAccessible(true);
                    Object which = whichField.get(slot);
                    return typeName.equals(String.valueOf(which));
                }
                currentClass = currentClass.getSuperclass();
            }
        } catch (ReflectiveOperationException ignored) {
            return false;
        }

        return false;
    }

    @Unique
    private static boolean blxckoutfx$hasSemantic(Object slot, String semanticName) {
        if (slot == null) {
            return false;
        }

        try {
            Class<?> currentClass = slot.getClass();
            while (currentClass != null) {
                if ("appeng.menu.slot.AppEngSlot".equals(currentClass.getName())) {
                    var menuField = currentClass.getDeclaredField("menu");
                    menuField.setAccessible(true);
                    Object menu = menuField.get(slot);
                    if (menu == null) {
                        return false;
                    }

                    var getSlotSemantic = menu.getClass().getMethod("getSlotSemantic", net.minecraft.world.inventory.Slot.class);
                    Object semantic = getSlotSemantic.invoke(menu, slot);
                    return semanticName.equals(blxckoutfx$getSlotSemanticId(semantic));
                }
                currentClass = currentClass.getSuperclass();
            }
        } catch (ReflectiveOperationException ignored) {
            return false;
        }

        return false;
    }

    @Unique
    private static String blxckoutfx$getSlotSemanticId(Object semantic) {
        if (semantic == null) {
            return "";
        }

        try {
            var idMethod = semantic.getClass().getDeclaredMethod("id");
            idMethod.setAccessible(true);
            Object id = idMethod.invoke(semantic);
            if (id != null) {
                return String.valueOf(id);
            }
        } catch (ReflectiveOperationException | RuntimeException ignored) {
            return String.valueOf(semantic);
        }

        return String.valueOf(semantic);
    }
}
