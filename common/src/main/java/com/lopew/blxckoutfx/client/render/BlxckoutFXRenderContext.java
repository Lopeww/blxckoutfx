package com.lopew.blxckoutfx.client.render;

public final class BlxckoutFXRenderContext {
    private static int buttonRenderDepth;
    private static int inventoryButtonRenderDepth;
    private static int inventoryButtonTooltipDepth;
    private static int textureDarkeningSuppressDepth;
    private static int journeyMapThemeButtonBackgroundRenderDepth;
    private static int jeiWidgetRenderDepth;
    private static int jeiRecipeLayoutRenderDepth;
    private static int jeiInformationRecipeRenderDepth;
    private static int balancedCappedDarkeningDepth;
    private static int ae2ScreenRenderDepth;
    private static int ae2TextureRenderDepth;
    private static int ae2IconRenderDepth;
    private static int ae2TransferButtonRenderDepth;
    private static int ae2TooltipBackgroundDepth;
    private static int extendedAeTagPlaceholderRenderDepth;
    private static int ae2ScrollbarThumbDepth;
    private static int ae2CraftingTerminalSectionDepth;
    private static int ae2OutputSlotPaletteDepth;
    private static int ae2PatternModesBackgroundDepth;
    private static int ae2VerticalButtonBarBackgroundDepth;
    private static int ae2UpgradePanelBorderDepth;
    private static int ae2CustomRemapDepth;
    private static int ae2ConfigArrowDepth;
    private static int ae2CraftingTerminalSectionX1;
    private static int ae2CraftingTerminalSectionY1;
    private static int ae2CraftingTerminalSectionX2;
    private static int ae2CraftingTerminalSectionY2;
    private static int ae2ConfigArrowX1;
    private static int ae2ConfigArrowY1;
    private static int ae2ConfigArrowX2;
    private static int ae2ConfigArrowY2;
    private static int ae2ConfigArrowSecondX1;
    private static int ae2ConfigArrowSecondY1;
    private static int ae2ConfigArrowSecondX2;
    private static int ae2ConfigArrowSecondY2;

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

    public static void enterInventoryButtonTooltipRender() {
        inventoryButtonTooltipDepth++;
    }

    public static void exitInventoryButtonTooltipRender() {
        inventoryButtonTooltipDepth = Math.max(0, inventoryButtonTooltipDepth - 1);
    }

    public static boolean isRenderingInventoryButtonTooltip() {
        return inventoryButtonTooltipDepth > 0;
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

    public static void enterJourneyMapThemeButtonBackgroundRender() {
        journeyMapThemeButtonBackgroundRenderDepth++;
    }

    public static void exitJourneyMapThemeButtonBackgroundRender() {
        journeyMapThemeButtonBackgroundRenderDepth = Math.max(0, journeyMapThemeButtonBackgroundRenderDepth - 1);
    }

    public static boolean isRenderingJourneyMapThemeButtonBackground() {
        return journeyMapThemeButtonBackgroundRenderDepth > 0;
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

    public static void enterAe2TextureRender() {
        ae2TextureRenderDepth++;
    }

    public static void exitAe2TextureRender() {
        ae2TextureRenderDepth = Math.max(0, ae2TextureRenderDepth - 1);
    }

    public static boolean isRenderingAe2Texture() {
        return ae2TextureRenderDepth > 0;
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

    public static void enterAe2TooltipBackgroundRender() {
        ae2TooltipBackgroundDepth++;
    }

    public static void exitAe2TooltipBackgroundRender() {
        ae2TooltipBackgroundDepth = Math.max(0, ae2TooltipBackgroundDepth - 1);
    }

    public static boolean isRenderingAe2TooltipBackground() {
        return ae2TooltipBackgroundDepth > 0;
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

    public static void enterAe2VerticalButtonBarBackgroundRender() {
        ae2VerticalButtonBarBackgroundDepth++;
    }

    public static void exitAe2VerticalButtonBarBackgroundRender() {
        ae2VerticalButtonBarBackgroundDepth = Math.max(0, ae2VerticalButtonBarBackgroundDepth - 1);
    }

    public static boolean isRenderingAe2VerticalButtonBarBackground() {
        return ae2VerticalButtonBarBackgroundDepth > 0;
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

    public static void enterAe2CustomRemapRender() {
        ae2CustomRemapDepth++;
    }

    public static void exitAe2CustomRemapRender() {
        ae2CustomRemapDepth = Math.max(0, ae2CustomRemapDepth - 1);
    }

    public static boolean isRenderingAe2CustomRemap() {
        return ae2CustomRemapDepth > 0;
    }

    public static void enterAe2ConfigArrowRender(
            int x1,
            int y1,
            int x2,
            int y2,
            int secondX1,
            int secondY1,
            int secondX2,
            int secondY2
    ) {
        ae2ConfigArrowDepth++;
        ae2ConfigArrowX1 = x1;
        ae2ConfigArrowY1 = y1;
        ae2ConfigArrowX2 = x2;
        ae2ConfigArrowY2 = y2;
        ae2ConfigArrowSecondX1 = secondX1;
        ae2ConfigArrowSecondY1 = secondY1;
        ae2ConfigArrowSecondX2 = secondX2;
        ae2ConfigArrowSecondY2 = secondY2;
    }

    public static void exitAe2ConfigArrowRender() {
        ae2ConfigArrowDepth = Math.max(0, ae2ConfigArrowDepth - 1);
        if (ae2ConfigArrowDepth == 0) {
            ae2ConfigArrowX1 = 0;
            ae2ConfigArrowY1 = 0;
            ae2ConfigArrowX2 = 0;
            ae2ConfigArrowY2 = 0;
            ae2ConfigArrowSecondX1 = 0;
            ae2ConfigArrowSecondY1 = 0;
            ae2ConfigArrowSecondX2 = 0;
            ae2ConfigArrowSecondY2 = 0;
        }
    }

    public static boolean isRenderingAe2ConfigArrow() {
        return ae2ConfigArrowDepth > 0;
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

    public static float getAe2ConfigArrowSecondRectX1() {
        return ae2ConfigArrowSecondX1;
    }

    public static float getAe2ConfigArrowSecondRectY1() {
        return ae2ConfigArrowSecondY1;
    }

    public static float getAe2ConfigArrowSecondRectX2() {
        return ae2ConfigArrowSecondX2;
    }

    public static float getAe2ConfigArrowSecondRectY2() {
        return ae2ConfigArrowSecondY2;
    }

    private BlxckoutFXRenderContext() {
    }
}
