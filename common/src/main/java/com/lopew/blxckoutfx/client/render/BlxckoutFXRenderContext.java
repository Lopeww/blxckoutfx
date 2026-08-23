package com.lopew.blxckoutfx.client.render;

import net.minecraft.client.Minecraft;

public final class BlxckoutFXRenderContext {
    private static int buttonRenderDepth;
    private static int patchouliBookButtonRenderDepth;
    private static int inventoryButtonRenderDepth;
    private static int textureDarkeningSuppressDepth;
    private static int jeiWidgetRenderDepth;
    private static int jeiRecipeLayoutRenderDepth;
    private static int jeiInformationRecipeRenderDepth;
    private static int inventoryRecipeBookPreForegroundRenderDepth;
    private static int tfcJeiKnappingArrowRenderDepth;
    private static int pneumaticCraftWidgetRenderDepth;
    private static int balancedCappedDarkeningDepth;
    private static int ae2ScreenRenderDepth;
    private static int ae2CraftingTermScreenRenderDepth;
    private static int ae2TextureRenderDepth;
    private static int ae2StackSizeLabelRenderDepth;
    private static int ae2IconRenderDepth;
    private static int ae2TransferButtonRenderDepth;
    private static int ae2SearchFieldRenderDepth;
    private static int extendedAeTagPlaceholderRenderDepth;
    private static int ae2ScrollbarThumbDepth;
    private static int ae2CraftingTerminalSectionDepth;
    private static int ae2OutputSlotPaletteDepth;
    private static int ae2PatternModesBackgroundDepth;
    private static int ae2VerticalButtonBarBackgroundDepth;
    private static int ae2WarningTextRenderDepth;
    private static int ae2UpgradePanelBorderDepth;
    private static int modMenuIconButtonRenderDepth;
    private static int ae2MolecularAssemblerGridDepth;
    private static int ae2CraftingTerminalSectionX1;
    private static int ae2CraftingTerminalSectionY1;
    private static int ae2CraftingTerminalSectionX2;
    private static int ae2CraftingTerminalSectionY2;
    private static int ae2MolecularAssemblerGridX1;
    private static int ae2MolecularAssemblerGridY1;
    private static int ae2MolecularAssemblerGridX2;
    private static int ae2MolecularAssemblerGridY2;
    private static int ae2ConfigArrowDepth;
    private static int ae2ConfigArrowX1;
    private static int ae2ConfigArrowY1;
    private static int ae2ConfigArrowX2;
    private static int ae2ConfigArrowY2;
    private static int tfcCrucibleTemperatureRenderDepth;
    private static int tfcCrucibleVesselRenderDepth;
    private static int tfcCrucibleFluidRenderDepth;
    private static int tfcCrucibleTemperatureX1;
    private static int tfcCrucibleTemperatureY1;
    private static int tfcCrucibleTemperatureX2;
    private static int tfcCrucibleTemperatureY2;
    private static int tfcCrucibleVesselX1;
    private static int tfcCrucibleVesselY1;
    private static int tfcCrucibleVesselX2;
    private static int tfcCrucibleVesselY2;
    private static int tfcFirepitTemperatureRenderDepth;
    private static int tfcFirepitTemperatureX1;
    private static int tfcFirepitTemperatureY1;
    private static int tfcFirepitTemperatureX2;
    private static int tfcFirepitTemperatureY2;
    private static int tfcFirepitPotRenderDepth;
    private static int tfcFirepitPotFluidRenderDepth;
    private static int tfcFirepitPotX1;
    private static int tfcFirepitPotY1;
    private static int tfcFirepitPotX2;
    private static int tfcFirepitPotY2;

    /** Clears render-pass scopes that must never survive a development reload. */
    public static void reset() {
        buttonRenderDepth = 0;
        patchouliBookButtonRenderDepth = 0;
        inventoryButtonRenderDepth = 0;
        textureDarkeningSuppressDepth = 0;
        jeiWidgetRenderDepth = 0;
        jeiRecipeLayoutRenderDepth = 0;
        jeiInformationRecipeRenderDepth = 0;
        inventoryRecipeBookPreForegroundRenderDepth = 0;
        tfcJeiKnappingArrowRenderDepth = 0;
        pneumaticCraftWidgetRenderDepth = 0;
        balancedCappedDarkeningDepth = 0;
        ae2ScreenRenderDepth = 0;
        ae2CraftingTermScreenRenderDepth = 0;
        ae2TextureRenderDepth = 0;
        ae2StackSizeLabelRenderDepth = 0;
        ae2IconRenderDepth = 0;
        ae2TransferButtonRenderDepth = 0;
        ae2SearchFieldRenderDepth = 0;
        extendedAeTagPlaceholderRenderDepth = 0;
        ae2ScrollbarThumbDepth = 0;
        ae2CraftingTerminalSectionDepth = 0;
        ae2OutputSlotPaletteDepth = 0;
        ae2PatternModesBackgroundDepth = 0;
        ae2VerticalButtonBarBackgroundDepth = 0;
        ae2WarningTextRenderDepth = 0;
        ae2UpgradePanelBorderDepth = 0;
        modMenuIconButtonRenderDepth = 0;
        ae2MolecularAssemblerGridDepth = 0;
        ae2ConfigArrowDepth = 0;
        tfcCrucibleTemperatureRenderDepth = 0;
        tfcCrucibleVesselRenderDepth = 0;
        tfcCrucibleFluidRenderDepth = 0;
        tfcFirepitTemperatureRenderDepth = 0;
        tfcFirepitPotRenderDepth = 0;
        tfcFirepitPotFluidRenderDepth = 0;

        ae2CraftingTerminalSectionX1 = ae2CraftingTerminalSectionY1 = 0;
        ae2CraftingTerminalSectionX2 = ae2CraftingTerminalSectionY2 = 0;
        ae2MolecularAssemblerGridX1 = ae2MolecularAssemblerGridY1 = 0;
        ae2MolecularAssemblerGridX2 = ae2MolecularAssemblerGridY2 = 0;
        ae2ConfigArrowX1 = ae2ConfigArrowY1 = 0;
        ae2ConfigArrowX2 = ae2ConfigArrowY2 = 0;
        tfcCrucibleTemperatureX1 = tfcCrucibleTemperatureY1 = 0;
        tfcCrucibleTemperatureX2 = tfcCrucibleTemperatureY2 = 0;
        tfcCrucibleVesselX1 = tfcCrucibleVesselY1 = 0;
        tfcCrucibleVesselX2 = tfcCrucibleVesselY2 = 0;
        tfcFirepitTemperatureX1 = tfcFirepitTemperatureY1 = 0;
        tfcFirepitTemperatureX2 = tfcFirepitTemperatureY2 = 0;
        tfcFirepitPotX1 = tfcFirepitPotY1 = 0;
        tfcFirepitPotX2 = tfcFirepitPotY2 = 0;
    }

    public static void enterButtonRender() {
        buttonRenderDepth++;
    }

    public static void exitButtonRender() {
        buttonRenderDepth = Math.max(0, buttonRenderDepth - 1);
    }

    public static boolean isRenderingButton() {
        return buttonRenderDepth > 0;
    }

    public static void enterPatchouliBookButtonRender() {
        patchouliBookButtonRenderDepth++;
    }

    public static void exitPatchouliBookButtonRender() {
        patchouliBookButtonRenderDepth = Math.max(0, patchouliBookButtonRenderDepth - 1);
    }

    public static boolean isRenderingPatchouliBookButton() {
        return patchouliBookButtonRenderDepth > 0;
    }

    public static void enterInventoryButtonRender() {
        inventoryButtonRenderDepth++;
    }

    public static void exitInventoryButtonRender() {
        inventoryButtonRenderDepth = Math.max(0, inventoryButtonRenderDepth - 1);
    }

    public static boolean isRenderingInventoryButton() {
        return inventoryButtonRenderDepth > 0;
    }

    public static void suppressTextureDarkening() {
        textureDarkeningSuppressDepth++;
    }

    public static void allowTextureDarkening() {
        textureDarkeningSuppressDepth = Math.max(0, textureDarkeningSuppressDepth - 1);
    }

    public static boolean isTextureDarkeningSuppressed() {
        return textureDarkeningSuppressDepth > 0;
    }

    public static void enterJeiWidgetRender() {
        jeiWidgetRenderDepth++;
    }

    public static void exitJeiWidgetRender() {
        jeiWidgetRenderDepth = Math.max(0, jeiWidgetRenderDepth - 1);
    }

    public static boolean isRenderingJeiWidget() {
        return jeiWidgetRenderDepth > 0;
    }

    public static void enterJeiRecipeLayoutRender() {
        jeiRecipeLayoutRenderDepth++;
    }

    public static void exitJeiRecipeLayoutRender() {
        jeiRecipeLayoutRenderDepth = Math.max(0, jeiRecipeLayoutRenderDepth - 1);
    }

    public static boolean isRenderingJeiRecipeLayout() {
        return jeiRecipeLayoutRenderDepth > 0;
    }

    public static void enterJeiInformationRecipeRender() {
        jeiInformationRecipeRenderDepth++;
    }

    public static void exitJeiInformationRecipeRender() {
        jeiInformationRecipeRenderDepth = Math.max(0, jeiInformationRecipeRenderDepth - 1);
    }

    public static boolean isRenderingJeiInformationRecipe() {
        return jeiInformationRecipeRenderDepth > 0;
    }

    public static void enterInventoryRecipeBookPreForegroundRender() {
        inventoryRecipeBookPreForegroundRenderDepth++;
    }

    public static void exitInventoryRecipeBookPreForegroundRender() {
        inventoryRecipeBookPreForegroundRenderDepth = Math.max(0, inventoryRecipeBookPreForegroundRenderDepth - 1);
    }

    public static boolean isRenderingInventoryRecipeBookPreForeground() {
        return inventoryRecipeBookPreForegroundRenderDepth > 0;
    }

    public static void enterTfcJeiKnappingArrowRender() {
        tfcJeiKnappingArrowRenderDepth++;
    }

    public static void exitTfcJeiKnappingArrowRender() {
        tfcJeiKnappingArrowRenderDepth = Math.max(0, tfcJeiKnappingArrowRenderDepth - 1);
    }

    public static boolean isRenderingTfcJeiKnappingArrow() {
        return tfcJeiKnappingArrowRenderDepth > 0;
    }

    public static void enterTfcCrucibleTemperatureRender(int x1, int y1, int x2, int y2) {
        tfcCrucibleTemperatureRenderDepth++;
        setTfcCrucibleTemperatureRect(x1, y1, x2, y2);
    }

    public static void exitTfcCrucibleTemperatureRender() {
        tfcCrucibleTemperatureRenderDepth = Math.max(0, tfcCrucibleTemperatureRenderDepth - 1);
        if (tfcCrucibleTemperatureRenderDepth == 0) {
            tfcCrucibleTemperatureX1 = 0;
            tfcCrucibleTemperatureY1 = 0;
            tfcCrucibleTemperatureX2 = 0;
            tfcCrucibleTemperatureY2 = 0;
        }
    }

    public static float getTfcCrucibleTemperatureMode() {
        return tfcCrucibleTemperatureRenderDepth > 0 ? 1.0F : 0.0F;
    }

    public static float getTfcCrucibleTemperatureX1() { return tfcCrucibleTemperatureX1; }
    public static float getTfcCrucibleTemperatureY1() { return tfcCrucibleTemperatureY1; }
    public static float getTfcCrucibleTemperatureX2() { return tfcCrucibleTemperatureX2; }
    public static float getTfcCrucibleTemperatureY2() { return tfcCrucibleTemperatureY2; }

    public static void enterTfcCrucibleVesselRender(int x1, int y1, int x2, int y2) {
        tfcCrucibleVesselRenderDepth++;
        setTfcCrucibleVesselRect(x1, y1, x2, y2);
    }

    public static void exitTfcCrucibleVesselRender() {
        tfcCrucibleVesselRenderDepth = Math.max(0, tfcCrucibleVesselRenderDepth - 1);
        clearTfcCrucibleVesselRectIfIdle();
    }

    public static void enterTfcCrucibleFluidRender(int x1, int y1, int x2, int y2) {
        tfcCrucibleFluidRenderDepth++;
        setTfcCrucibleVesselRect(x1, y1, x2, y2);
    }

    public static void exitTfcCrucibleFluidRender() {
        tfcCrucibleFluidRenderDepth = Math.max(0, tfcCrucibleFluidRenderDepth - 1);
        clearTfcCrucibleVesselRectIfIdle();
    }

    public static float getTfcCrucibleVesselMode() {
        if (tfcCrucibleFluidRenderDepth > 0) {
            return 2.0F;
        }
        return tfcCrucibleVesselRenderDepth > 0 ? 1.0F : 0.0F;
    }

    public static float getTfcCrucibleVesselX1() { return tfcCrucibleVesselX1; }
    public static float getTfcCrucibleVesselY1() { return tfcCrucibleVesselY1; }
    public static float getTfcCrucibleVesselX2() { return tfcCrucibleVesselX2; }
    public static float getTfcCrucibleVesselY2() { return tfcCrucibleVesselY2; }

    public static void enterTfcFirepitTemperatureRender(int x1, int y1, int x2, int y2) {
        tfcFirepitTemperatureRenderDepth++;
        int[] rect = toFramebufferRect(x1, y1, x2, y2);
        tfcFirepitTemperatureX1 = rect[0];
        tfcFirepitTemperatureY1 = rect[1];
        tfcFirepitTemperatureX2 = rect[2];
        tfcFirepitTemperatureY2 = rect[3];
    }

    public static void exitTfcFirepitTemperatureRender() {
        tfcFirepitTemperatureRenderDepth = Math.max(0, tfcFirepitTemperatureRenderDepth - 1);
        if (tfcFirepitTemperatureRenderDepth == 0) {
            tfcFirepitTemperatureX1 = 0;
            tfcFirepitTemperatureY1 = 0;
            tfcFirepitTemperatureX2 = 0;
            tfcFirepitTemperatureY2 = 0;
        }
    }

    public static float getTfcFirepitTemperatureMode() {
        return tfcFirepitTemperatureRenderDepth > 0 ? 1.0F : 0.0F;
    }

    public static float getTfcFirepitTemperatureX1() { return tfcFirepitTemperatureX1; }
    public static float getTfcFirepitTemperatureY1() { return tfcFirepitTemperatureY1; }
    public static float getTfcFirepitTemperatureX2() { return tfcFirepitTemperatureX2; }
    public static float getTfcFirepitTemperatureY2() { return tfcFirepitTemperatureY2; }

    public static void enterTfcFirepitPotRender(int x1, int y1, int x2, int y2) {
        tfcFirepitPotRenderDepth++;
        int[] rect = toFramebufferRect(x1, y1, x2, y2);
        tfcFirepitPotX1 = rect[0];
        tfcFirepitPotY1 = rect[1];
        tfcFirepitPotX2 = rect[2];
        tfcFirepitPotY2 = rect[3];
    }

    public static void exitTfcFirepitPotRender() {
        tfcFirepitPotRenderDepth = Math.max(0, tfcFirepitPotRenderDepth - 1);
        clearTfcFirepitPotRectIfIdle();
    }

    public static void enterTfcFirepitPotFluidRender(int x1, int y1, int x2, int y2) {
        tfcFirepitPotFluidRenderDepth++;
        int[] rect = toFramebufferRect(x1, y1, x2, y2);
        tfcFirepitPotX1 = rect[0];
        tfcFirepitPotY1 = rect[1];
        tfcFirepitPotX2 = rect[2];
        tfcFirepitPotY2 = rect[3];
    }

    public static void exitTfcFirepitPotFluidRender() {
        tfcFirepitPotFluidRenderDepth = Math.max(0, tfcFirepitPotFluidRenderDepth - 1);
        clearTfcFirepitPotRectIfIdle();
    }

    private static void clearTfcFirepitPotRectIfIdle() {
        if (tfcFirepitPotRenderDepth == 0 && tfcFirepitPotFluidRenderDepth == 0) {
            tfcFirepitPotX1 = 0;
            tfcFirepitPotY1 = 0;
            tfcFirepitPotX2 = 0;
            tfcFirepitPotY2 = 0;
        }
    }

    public static float getTfcFirepitPotMode() {
        if (tfcFirepitPotFluidRenderDepth > 0) {
            return 2.0F;
        }
        return tfcFirepitPotRenderDepth > 0 ? 1.0F : 0.0F;
    }

    public static float getTfcFirepitPotX1() { return tfcFirepitPotX1; }
    public static float getTfcFirepitPotY1() { return tfcFirepitPotY1; }
    public static float getTfcFirepitPotX2() { return tfcFirepitPotX2; }
    public static float getTfcFirepitPotY2() { return tfcFirepitPotY2; }

    private static void setTfcCrucibleTemperatureRect(int x1, int y1, int x2, int y2) {
        int[] rect = toFramebufferRect(x1, y1, x2, y2);
        tfcCrucibleTemperatureX1 = rect[0];
        tfcCrucibleTemperatureY1 = rect[1];
        tfcCrucibleTemperatureX2 = rect[2];
        tfcCrucibleTemperatureY2 = rect[3];
    }

    private static void setTfcCrucibleVesselRect(int x1, int y1, int x2, int y2) {
        int[] rect = toFramebufferRect(x1, y1, x2, y2);
        tfcCrucibleVesselX1 = rect[0];
        tfcCrucibleVesselY1 = rect[1];
        tfcCrucibleVesselX2 = rect[2];
        tfcCrucibleVesselY2 = rect[3];
    }

    private static void clearTfcCrucibleVesselRectIfIdle() {
        if (tfcCrucibleVesselRenderDepth == 0 && tfcCrucibleFluidRenderDepth == 0) {
            tfcCrucibleVesselX1 = 0;
            tfcCrucibleVesselY1 = 0;
            tfcCrucibleVesselX2 = 0;
            tfcCrucibleVesselY2 = 0;
        }
    }

    private static int[] toFramebufferRect(int x1, int y1, int x2, int y2) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft == null || minecraft.getWindow() == null) {
            return new int[]{0, 0, 0, 0};
        }
        double scale = Math.max(1.0D, minecraft.getWindow().getGuiScale());
        int framebufferHeight = minecraft.getWindow().getHeight();
        return new int[]{
                (int) Math.floor(x1 * scale),
                framebufferHeight - (int) Math.ceil(y2 * scale),
                (int) Math.ceil(x2 * scale),
                framebufferHeight - (int) Math.floor(y1 * scale)
        };
    }

    public static void enterPneumaticCraftWidgetRender() {
        pneumaticCraftWidgetRenderDepth++;
    }

    public static void exitPneumaticCraftWidgetRender() {
        pneumaticCraftWidgetRenderDepth = Math.max(0, pneumaticCraftWidgetRenderDepth - 1);
    }

    public static boolean isRenderingPneumaticCraftWidget() {
        return pneumaticCraftWidgetRenderDepth > 0;
    }

    public static void enterBalancedCappedDarkening() {
        balancedCappedDarkeningDepth++;
    }

    public static void exitBalancedCappedDarkening() {
        balancedCappedDarkeningDepth = Math.max(0, balancedCappedDarkeningDepth - 1);
    }

    public static boolean isBalancedCappedDarkeningActive() {
        return balancedCappedDarkeningDepth > 0;
    }

    public static void enterAe2ScreenRender() {
        ae2ScreenRenderDepth++;
    }

    public static void exitAe2ScreenRender() {
        ae2ScreenRenderDepth = Math.max(0, ae2ScreenRenderDepth - 1);
    }

    public static boolean isRenderingAe2Screen() {
        return ae2ScreenRenderDepth > 0;
    }

    public static void enterAe2CraftingTermScreenRender() {
        ae2CraftingTermScreenRenderDepth++;
    }

    public static void exitAe2CraftingTermScreenRender() {
        ae2CraftingTermScreenRenderDepth = Math.max(0, ae2CraftingTermScreenRenderDepth - 1);
    }

    public static boolean isRenderingAe2CraftingTermScreen() {
        return ae2CraftingTermScreenRenderDepth > 0;
    }

    public static void enterAe2TextureRender() {
        ae2TextureRenderDepth++;
    }

    public static void exitAe2TextureRender() {
        ae2TextureRenderDepth = Math.max(0, ae2TextureRenderDepth - 1);
    }

    public static boolean isRenderingAe2Texture() {
        return ae2TextureRenderDepth > 0;
    }

    public static void enterAe2StackSizeLabelRender() {
        ae2StackSizeLabelRenderDepth++;
    }

    public static void exitAe2StackSizeLabelRender() {
        ae2StackSizeLabelRenderDepth = Math.max(0, ae2StackSizeLabelRenderDepth - 1);
    }

    public static boolean isRenderingAe2StackSizeLabel() {
        return ae2StackSizeLabelRenderDepth > 0;
    }

    public static void enterAe2IconRender() {
        ae2IconRenderDepth++;
    }

    public static void exitAe2IconRender() {
        ae2IconRenderDepth = Math.max(0, ae2IconRenderDepth - 1);
    }

    public static boolean isRenderingAe2Icon() {
        return ae2IconRenderDepth > 0;
    }

    public static void enterAe2TransferButtonRender() {
        ae2TransferButtonRenderDepth++;
    }

    public static void exitAe2TransferButtonRender() {
        ae2TransferButtonRenderDepth = Math.max(0, ae2TransferButtonRenderDepth - 1);
    }

    public static boolean isRenderingAe2TransferButton() {
        return ae2TransferButtonRenderDepth > 0;
    }

    public static void enterAe2SearchFieldRender() {
        ae2SearchFieldRenderDepth++;
    }

    public static void exitAe2SearchFieldRender() {
        ae2SearchFieldRenderDepth = Math.max(0, ae2SearchFieldRenderDepth - 1);
    }

    public static boolean isRenderingAe2SearchField() {
        return ae2SearchFieldRenderDepth > 0;
    }

    public static void enterExtendedAeTagPlaceholderRender() {
        extendedAeTagPlaceholderRenderDepth++;
    }

    public static void exitExtendedAeTagPlaceholderRender() {
        extendedAeTagPlaceholderRenderDepth = Math.max(0, extendedAeTagPlaceholderRenderDepth - 1);
    }

    public static boolean isRenderingExtendedAeTagPlaceholder() {
        return extendedAeTagPlaceholderRenderDepth > 0;
    }

    public static void enterAe2ScrollbarThumbRender() {
        ae2ScrollbarThumbDepth++;
    }

    public static void exitAe2ScrollbarThumbRender() {
        ae2ScrollbarThumbDepth = Math.max(0, ae2ScrollbarThumbDepth - 1);
    }

    public static boolean isRenderingAe2ScrollbarThumb() {
        return ae2ScrollbarThumbDepth > 0;
    }

    public static void enterAe2CraftingTerminalSectionRender(int x1, int y1, int x2, int y2) {
        ae2CraftingTerminalSectionDepth++;
        ae2CraftingTerminalSectionX1 = x1;
        ae2CraftingTerminalSectionY1 = y1;
        ae2CraftingTerminalSectionX2 = x2;
        ae2CraftingTerminalSectionY2 = y2;
    }

    public static void exitAe2CraftingTerminalSectionRender() {
        ae2CraftingTerminalSectionDepth = Math.max(0, ae2CraftingTerminalSectionDepth - 1);
    }

    public static boolean isRenderingAe2CraftingTerminalSection() {
        return ae2CraftingTerminalSectionDepth > 0;
    }

    public static void enterAe2OutputSlotPaletteRender() {
        ae2OutputSlotPaletteDepth++;
    }

    public static void exitAe2OutputSlotPaletteRender() {
        ae2OutputSlotPaletteDepth = Math.max(0, ae2OutputSlotPaletteDepth - 1);
    }

    public static boolean isRenderingAe2OutputSlotPalette() {
        return ae2OutputSlotPaletteDepth > 0;
    }

    public static void enterAe2PatternModesBackgroundRender() {
        ae2PatternModesBackgroundDepth++;
    }

    public static void exitAe2PatternModesBackgroundRender() {
        ae2PatternModesBackgroundDepth = Math.max(0, ae2PatternModesBackgroundDepth - 1);
    }

    public static boolean isRenderingAe2PatternModesBackground() {
        return ae2PatternModesBackgroundDepth > 0;
    }

    public static void enterAe2MolecularAssemblerGridRender(int x1, int y1, int x2, int y2) {
        ae2MolecularAssemblerGridDepth++;
        ae2MolecularAssemblerGridX1 = x1;
        ae2MolecularAssemblerGridY1 = y1;
        ae2MolecularAssemblerGridX2 = x2;
        ae2MolecularAssemblerGridY2 = y2;
    }

    public static void exitAe2MolecularAssemblerGridRender() {
        ae2MolecularAssemblerGridDepth = Math.max(0, ae2MolecularAssemblerGridDepth - 1);
        if (ae2MolecularAssemblerGridDepth == 0) {
            ae2MolecularAssemblerGridX1 = 0;
            ae2MolecularAssemblerGridY1 = 0;
            ae2MolecularAssemblerGridX2 = 0;
            ae2MolecularAssemblerGridY2 = 0;
        }
    }

    public static boolean isRenderingAe2MolecularAssemblerGrid() {
        return ae2MolecularAssemblerGridDepth > 0;
    }

    public static void enterAe2VerticalButtonBarBackgroundRender() {
        ae2VerticalButtonBarBackgroundDepth++;
    }

    public static void exitAe2VerticalButtonBarBackgroundRender() {
        ae2VerticalButtonBarBackgroundDepth = Math.max(0, ae2VerticalButtonBarBackgroundDepth - 1);
    }

    public static boolean isRenderingAe2VerticalButtonBarBackground() {
        return ae2VerticalButtonBarBackgroundDepth > 0;
    }

    public static float getAe2ButtonPaletteMode() {
        if (isRenderingAe2CraftingTerminalSection()) {
            return 7.0F;
        }

        if (isRenderingAe2VerticalButtonBarBackground()) {
            return 5.0F;
        }

        if (isRenderingAe2OutputSlotPalette()) {
            return 5.0F;
        }

        if (isRenderingAe2PatternModesBackground()) {
            return 5.0F;
        }

        if (isRenderingAe2ScrollbarThumb()) {
            return 3.0F;
        }

        return 0.0F;
    }

    public static float getAe2ScrollbarTrackX1() {
        return 0.0F;
    }

    public static float getAe2ScrollbarTrackY1() {
        return 0.0F;
    }

    public static float getAe2ScrollbarTrackX2() {
        return 0.0F;
    }

    public static float getAe2ScrollbarTrackY2() {
        return 0.0F;
    }

    public static float getAe2CraftingTerminalSectionX1() {
        return ae2CraftingTerminalSectionX1;
    }

    public static float getAe2CraftingTerminalSectionY1() {
        return ae2CraftingTerminalSectionY1;
    }

    public static float getAe2CraftingTerminalSectionX2() {
        return ae2CraftingTerminalSectionX2;
    }

    public static float getAe2CraftingTerminalSectionY2() {
        return ae2CraftingTerminalSectionY2;
    }

    public static float getAe2MolecularAssemblerGridX1() {
        return ae2MolecularAssemblerGridX1;
    }

    public static float getAe2MolecularAssemblerGridY1() {
        return ae2MolecularAssemblerGridY1;
    }

    public static float getAe2MolecularAssemblerGridX2() {
        return ae2MolecularAssemblerGridX2;
    }

    public static float getAe2MolecularAssemblerGridY2() {
        return ae2MolecularAssemblerGridY2;
    }

    public static void enterAe2ConfigArrowRender(int x1, int y1, int x2, int y2) {
        ae2ConfigArrowDepth++;
        ae2ConfigArrowX1 = x1;
        ae2ConfigArrowY1 = y1;
        ae2ConfigArrowX2 = x2;
        ae2ConfigArrowY2 = y2;
    }

    public static void exitAe2ConfigArrowRender() {
        ae2ConfigArrowDepth = Math.max(0, ae2ConfigArrowDepth - 1);
        
        
        
        
        if (ae2ConfigArrowDepth == 0) {
            ae2ConfigArrowX1 = 0;
            ae2ConfigArrowY1 = 0;
            ae2ConfigArrowX2 = 0;
            ae2ConfigArrowY2 = 0;
        }
    }

    public static boolean isRenderingAe2ConfigArrow() {
        return ae2ConfigArrowDepth > 0;
    }

    public static float getAe2ConfigArrowRectX1() {
        return ae2ConfigArrowX1;
    }

    public static float getAe2ConfigArrowRectY1() {
        return ae2ConfigArrowY1;
    }

    public static float getAe2ConfigArrowRectX2() {
        return ae2ConfigArrowX2;
    }

    public static float getAe2ConfigArrowRectY2() {
        return ae2ConfigArrowY2;
    }

    public static void enterAe2WarningTextRender() {
        ae2WarningTextRenderDepth++;
    }

    public static void exitAe2WarningTextRender() {
        ae2WarningTextRenderDepth = Math.max(0, ae2WarningTextRenderDepth - 1);
    }

    public static boolean isRenderingAe2WarningText() {
        return ae2WarningTextRenderDepth > 0;
    }

    public static void enterAe2UpgradePanelBorderRender() {
        ae2UpgradePanelBorderDepth++;
    }

    public static void exitAe2UpgradePanelBorderRender() {
        ae2UpgradePanelBorderDepth = Math.max(0, ae2UpgradePanelBorderDepth - 1);
    }

    public static boolean isRenderingAe2UpgradePanelBorder() {
        return ae2UpgradePanelBorderDepth > 0;
    }

    public static void enterModMenuIconButtonRender() {
        modMenuIconButtonRenderDepth++;
    }

    public static void exitModMenuIconButtonRender() {
        modMenuIconButtonRenderDepth = Math.max(0, modMenuIconButtonRenderDepth - 1);
    }

    public static boolean isRenderingModMenuIconButton() {
        return modMenuIconButtonRenderDepth > 0;
    }

    private BlxckoutFXRenderContext() {
    }
}
