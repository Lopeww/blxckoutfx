package com.lopew.blxckoutfx.client.render;

import net.minecraft.client.Minecraft;

public final class BlxckoutFXRenderContext {
    private static final int PNEUMATIC_COLOR_WHITELIST_SIZE = 12;
    private static int buttonRenderDepth;
    private static int inventoryButtonRenderDepth;
    private static int textureDarkeningSuppressDepth;
    private static int jeiWidgetRenderDepth;
    private static int jeiRecipeLayoutRenderDepth;
    private static int jeiInformationRecipeRenderDepth;
    private static int pneumaticCraftWidgetRenderDepth;
    private static int pneumaticCraftScreenRenderDepth;
    private static int balancedCappedDarkeningDepth;
    private static int ae2ScreenRenderDepth;
    private static int ae2CraftingTermScreenRenderDepth;
    private static int ae2TextureRenderDepth;
    private static int ae2StackSizeLabelRenderDepth;
    private static int ae2IconRenderDepth;
    private static int ae2PowerUnitIconRenderDepth;
    private static int ae2ForceWhiteIconRenderDepth;
    private static int ae2BrightenIconRenderDepth;
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
    private static int ae2CraftingTerminalSectionX1;
    private static int ae2CraftingTerminalSectionY1;
    private static int ae2CraftingTerminalSectionX2;
    private static int ae2CraftingTerminalSectionY2;
    private static int ae2MolecularAssemblerGridDepth;
    private static int ae2MolecularAssemblerGridX1;
    private static int ae2MolecularAssemblerGridY1;
    private static int ae2MolecularAssemblerGridX2;
    private static int ae2MolecularAssemblerGridY2;
    private static int ae2TransferButtonX1;
    private static int ae2TransferButtonY1;
    private static int ae2TransferButtonX2;
    private static int ae2TransferButtonY2;
    private static int ae2SortButtonRenderDepth;
    private static int ae2SortButtonX1;
    private static int ae2SortButtonY1;
    private static int ae2SortButtonX2;
    private static int ae2SortButtonY2;
    private static int ae2SchedulingButtonRenderDepth;
    private static int ae2SchedulingButtonX1;
    private static int ae2SchedulingButtonY1;
    private static int ae2SchedulingButtonX2;
    private static int ae2SchedulingButtonY2;
    private static int vanillaIconButtonDepth;
    private static int vanillaIconButtonMode;
    private static int vanillaIconButtonX1;
    private static int vanillaIconButtonY1;
    private static int vanillaIconButtonX2;
    private static int vanillaIconButtonY2;
    private static int pneumaticColorWhitelistCount;
    private static final float[] pneumaticColorWhitelist = new float[PNEUMATIC_COLOR_WHITELIST_SIZE * 3];

    static {
        setPneumaticColorWhitelist(
                0x3081BF,
                0x7DFFF2,
                0x4AF0FF,
                0x29C2FF,
                0x3E8D5F,
                0x3EBD7D,
                0x9CEB88,
                0x4CE568
        );
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

    public static void enterInventoryButtonRender() {
        inventoryButtonRenderDepth++;
    }

    public static void exitInventoryButtonRender() {
        inventoryButtonRenderDepth = Math.max(0, inventoryButtonRenderDepth - 1);
    }

    public static boolean isRenderingInventoryButton() {
        return inventoryButtonRenderDepth > 0;
    }

    public static void enterVanillaIconButtonRender(int mode, int x1, int y1, int x2, int y2) {
        vanillaIconButtonDepth++;
        vanillaIconButtonMode = mode;
        vanillaIconButtonX1 = x1;
        vanillaIconButtonY1 = y1;
        vanillaIconButtonX2 = x2;
        vanillaIconButtonY2 = y2;
    }

    public static void enterVanillaIconButtonGuiRender(int mode, int guiX1, int guiY1, int guiX2, int guiY2) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft == null || minecraft.getWindow() == null) {
            enterVanillaIconButtonRender(mode, guiX1, guiY1, guiX2, guiY2);
            return;
        }

        double guiScale = Math.max(1.0D, minecraft.getWindow().getGuiScale());
        int framebufferHeight = minecraft.getWindow().getHeight();
        int x1 = (int) Math.floor(guiX1 * guiScale);
        int x2 = (int) Math.ceil(guiX2 * guiScale);
        int y1 = framebufferHeight - (int) Math.ceil(guiY2 * guiScale);
        int y2 = framebufferHeight - (int) Math.floor(guiY1 * guiScale);
        enterVanillaIconButtonRender(mode, x1, y1, x2, y2);
    }

    public static void exitVanillaIconButtonRender() {
        vanillaIconButtonDepth = Math.max(0, vanillaIconButtonDepth - 1);
        if (vanillaIconButtonDepth == 0) {
            vanillaIconButtonMode = 0;
            vanillaIconButtonX1 = 0;
            vanillaIconButtonY1 = 0;
            vanillaIconButtonX2 = 0;
            vanillaIconButtonY2 = 0;
        }
    }

    public static boolean isRenderingVanillaIconButton() {
        return vanillaIconButtonDepth > 0;
    }

    public static float getVanillaIconButtonMode() { return vanillaIconButtonMode; }
    public static float getVanillaIconButtonX1() { return vanillaIconButtonX1; }
    public static float getVanillaIconButtonY1() { return vanillaIconButtonY1; }
    public static float getVanillaIconButtonX2() { return vanillaIconButtonX2; }
    public static float getVanillaIconButtonY2() { return vanillaIconButtonY2; }

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

    public static void enterPneumaticCraftScreenRender() {
        pneumaticCraftScreenRenderDepth++;
    }

    public static void exitPneumaticCraftScreenRender() {
        pneumaticCraftScreenRenderDepth = Math.max(0, pneumaticCraftScreenRenderDepth - 1);
    }

    public static boolean isRenderingPneumaticCraftScreen() {
        return pneumaticCraftScreenRenderDepth > 0;
    }

    public static void setPneumaticColorWhitelist(int... rgbColors) {
        pneumaticColorWhitelistCount = Math.min(rgbColors.length, PNEUMATIC_COLOR_WHITELIST_SIZE);
        for (int i = 0; i < pneumaticColorWhitelist.length; i++) {
            pneumaticColorWhitelist[i] = 0.0F;
        }

        for (int i = 0; i < pneumaticColorWhitelistCount; i++) {
            int color = rgbColors[i];
            int baseIndex = i * 3;
            pneumaticColorWhitelist[baseIndex] = ((color >> 16) & 0xFF) / 255.0F;
            pneumaticColorWhitelist[baseIndex + 1] = ((color >> 8) & 0xFF) / 255.0F;
            pneumaticColorWhitelist[baseIndex + 2] = (color & 0xFF) / 255.0F;
        }
    }

    public static float getPneumaticColorWhitelistEnabled() {
        return isRenderingPneumaticCraftScreen() && pneumaticColorWhitelistCount > 0 ? 1.0F : 0.0F;
    }

    public static float getPneumaticColorWhitelistCount() {
        return pneumaticColorWhitelistCount;
    }

    public static float getPneumaticColorWhitelistR(int index) {
        return blxckoutfx$getPneumaticColorComponent(index, 0);
    }

    public static float getPneumaticColorWhitelistG(int index) {
        return blxckoutfx$getPneumaticColorComponent(index, 1);
    }

    public static float getPneumaticColorWhitelistB(int index) {
        return blxckoutfx$getPneumaticColorComponent(index, 2);
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

    public static void enterAe2PowerUnitIconRender() {
        ae2PowerUnitIconRenderDepth++;
    }

    public static void exitAe2PowerUnitIconRender() {
        ae2PowerUnitIconRenderDepth = Math.max(0, ae2PowerUnitIconRenderDepth - 1);
    }

    public static boolean isRenderingAe2PowerUnitIcon() {
        return ae2PowerUnitIconRenderDepth > 0;
    }

    public static void enterAe2ForceWhiteIconRender() {
        ae2ForceWhiteIconRenderDepth++;
    }

    public static void exitAe2ForceWhiteIconRender() {
        ae2ForceWhiteIconRenderDepth = Math.max(0, ae2ForceWhiteIconRenderDepth - 1);
    }

    public static boolean isRenderingAe2ForceWhiteIcon() {
        return ae2ForceWhiteIconRenderDepth > 0;
    }

    public static void enterAe2BrightenIconRender() {
        ae2BrightenIconRenderDepth++;
    }

    public static void exitAe2BrightenIconRender() {
        ae2BrightenIconRenderDepth = Math.max(0, ae2BrightenIconRenderDepth - 1);
    }

    public static boolean isRenderingAe2BrightenIcon() {
        return ae2BrightenIconRenderDepth > 0;
    }

    public static void enterAe2TransferButtonRender() {
        ae2TransferButtonRenderDepth++;
    }

    public static void enterAe2TransferButtonRender(int x1, int y1, int x2, int y2) {
        ae2TransferButtonRenderDepth++;
        ae2TransferButtonX1 = x1;
        ae2TransferButtonY1 = y1;
        ae2TransferButtonX2 = x2;
        ae2TransferButtonY2 = y2;
    }

    public static void exitAe2TransferButtonRender() {
        ae2TransferButtonRenderDepth = Math.max(0, ae2TransferButtonRenderDepth - 1);
        if (ae2TransferButtonRenderDepth == 0) {
            ae2TransferButtonX1 = 0;
            ae2TransferButtonY1 = 0;
            ae2TransferButtonX2 = 0;
            ae2TransferButtonY2 = 0;
        }
    }

    public static boolean isRenderingAe2TransferButton() {
        return ae2TransferButtonRenderDepth > 0;
    }

    public static float getAe2TransferButtonX1() { return ae2TransferButtonX1; }
    public static float getAe2TransferButtonY1() { return ae2TransferButtonY1; }
    public static float getAe2TransferButtonX2() { return ae2TransferButtonX2; }
    public static float getAe2TransferButtonY2() { return ae2TransferButtonY2; }

    public static void enterAe2SortButtonRender(int x1, int y1, int x2, int y2) {
        ae2SortButtonRenderDepth++;
        ae2SortButtonX1 = x1;
        ae2SortButtonY1 = y1;
        ae2SortButtonX2 = x2;
        ae2SortButtonY2 = y2;
    }

    public static void exitAe2SortButtonRender() {
        ae2SortButtonRenderDepth = Math.max(0, ae2SortButtonRenderDepth - 1);
        if (ae2SortButtonRenderDepth == 0) {
            ae2SortButtonX1 = 0;
            ae2SortButtonY1 = 0;
            ae2SortButtonX2 = 0;
            ae2SortButtonY2 = 0;
        }
    }

    public static boolean isRenderingAe2SortButton() {
        return ae2SortButtonRenderDepth > 0;
    }

    public static float getAe2SortButtonX1() { return ae2SortButtonX1; }
    public static float getAe2SortButtonY1() { return ae2SortButtonY1; }
    public static float getAe2SortButtonX2() { return ae2SortButtonX2; }
    public static float getAe2SortButtonY2() { return ae2SortButtonY2; }

    public static void enterAe2SchedulingButtonRender(int x1, int y1, int x2, int y2) {
        ae2SchedulingButtonRenderDepth++;
        ae2SchedulingButtonX1 = x1;
        ae2SchedulingButtonY1 = y1;
        ae2SchedulingButtonX2 = x2;
        ae2SchedulingButtonY2 = y2;
    }

    public static void exitAe2SchedulingButtonRender() {
        ae2SchedulingButtonRenderDepth = Math.max(0, ae2SchedulingButtonRenderDepth - 1);
        if (ae2SchedulingButtonRenderDepth == 0) {
            ae2SchedulingButtonX1 = 0;
            ae2SchedulingButtonY1 = 0;
            ae2SchedulingButtonX2 = 0;
            ae2SchedulingButtonY2 = 0;
        }
    }

    public static boolean isRenderingAe2SchedulingButton() {
        return ae2SchedulingButtonRenderDepth > 0;
    }

    public static float getAe2SchedulingButtonX1() { return ae2SchedulingButtonX1; }
    public static float getAe2SchedulingButtonY1() { return ae2SchedulingButtonY1; }
    public static float getAe2SchedulingButtonX2() { return ae2SchedulingButtonX2; }
    public static float getAe2SchedulingButtonY2() { return ae2SchedulingButtonY2; }

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
        if (isRenderingAe2CraftingTerminalSection()) return 7.0F;
        if (isRenderingAe2VerticalButtonBarBackground()) return 5.0F;
        if (isRenderingAe2OutputSlotPalette()) return 5.0F;
        if (isRenderingAe2PatternModesBackground()) return 5.0F;
        if (isRenderingAe2ScrollbarThumb()) return 3.0F;
        return 0.0F;
    }

    public static float getAe2ScrollbarTrackX1() { return 0.0F; }
    public static float getAe2ScrollbarTrackY1() { return 0.0F; }
    public static float getAe2ScrollbarTrackX2() { return 0.0F; }
    public static float getAe2ScrollbarTrackY2() { return 0.0F; }
    public static float getAe2CraftingTerminalSectionX1() { return ae2CraftingTerminalSectionX1; }
    public static float getAe2CraftingTerminalSectionY1() { return ae2CraftingTerminalSectionY1; }
    public static float getAe2CraftingTerminalSectionX2() { return ae2CraftingTerminalSectionX2; }
    public static float getAe2CraftingTerminalSectionY2() { return ae2CraftingTerminalSectionY2; }

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

    public static float getAe2MolecularAssemblerGridX1() { return ae2MolecularAssemblerGridX1; }
    public static float getAe2MolecularAssemblerGridY1() { return ae2MolecularAssemblerGridY1; }
    public static float getAe2MolecularAssemblerGridX2() { return ae2MolecularAssemblerGridX2; }
    public static float getAe2MolecularAssemblerGridY2() { return ae2MolecularAssemblerGridY2; }

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

    private static float blxckoutfx$getPneumaticColorComponent(int index, int component) {
        if (index < 0 || index >= pneumaticColorWhitelistCount) {
            return 0.0F;
        }
        return pneumaticColorWhitelist[index * 3 + component];
    }

    private BlxckoutFXRenderContext() {
    }
}
