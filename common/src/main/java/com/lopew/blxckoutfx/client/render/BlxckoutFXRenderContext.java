package com.lopew.blxckoutfx.client.render;

public final class BlxckoutFXRenderContext {
    private static int buttonRenderDepth;
    private static int inventoryButtonRenderDepth;
    private static int textureDarkeningSuppressDepth;
    private static int jeiWidgetRenderDepth;
    private static int jeiRecipeLayoutRenderDepth;
    private static int jeiInformationRecipeRenderDepth;
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

    public static void enterButtonRender() {
        buttonRenderDepth++;
    }

    public static void exitButtonRender() {
        buttonRenderDepth = Math.max(0, buttonRenderDepth - 1);
    }

    public static boolean isRenderingButton() {
        return buttonRenderDepth > 0;
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
