package com.lopew.blxckoutfx.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public final class BlxckoutFXRenderContext {
    private static int buttonRenderDepth;
    private static int textureDarkeningSuppressDepth;
    private static int darkenedScreenRenderDepth;
    private static int itemRenderDepth;
    private static int iconColorPreserveDepth;
    private static int languageButtonIconRemapDepth;
    private static int recipeBookButtonIconRemapDepth;
    private static int recipeBookSearchIconRemapDepth;
    private static int recipeBookToggleIconRemapDepth;
    private static int anvilHammerIconRemapDepth;
    private static int beaconPowerIconRemapDepth;
    private static int ae2SortButtonIconRemapDepth;

    public static void enterButtonRender() {
        buttonRenderDepth++;
    }

    public static void exitButtonRender() {
        buttonRenderDepth = Math.max(0, buttonRenderDepth - 1);
    }

    public static boolean isRenderingButton() {
        return buttonRenderDepth > 0;
    }

    public static boolean isBalancedCappedDarkeningActive() {
        return isRenderingButton() && isCurrentScreenClassPrefixed("mezz.jei");
    }

    public static void enterIconColorPreserve() {
        iconColorPreserveDepth++;
    }

    public static void exitIconColorPreserve() {
        iconColorPreserveDepth = Math.max(0, iconColorPreserveDepth - 1);
    }

    public static boolean shouldPreserveIconColors() {
        return iconColorPreserveDepth > 0;
    }

    public static void enterLanguageButtonIconRemap() {
        languageButtonIconRemapDepth++;
    }

    public static void exitLanguageButtonIconRemap() {
        languageButtonIconRemapDepth = Math.max(0, languageButtonIconRemapDepth - 1);
    }

    public static boolean shouldRemapLanguageButtonIcon() {
        return languageButtonIconRemapDepth > 0;
    }

    public static void enterRecipeBookButtonIconRemap() {
        recipeBookButtonIconRemapDepth++;
    }

    public static void exitRecipeBookButtonIconRemap() {
        recipeBookButtonIconRemapDepth = Math.max(0, recipeBookButtonIconRemapDepth - 1);
    }

    public static boolean shouldRemapRecipeBookButtonIcon() {
        return recipeBookButtonIconRemapDepth > 0;
    }

    public static void enterRecipeBookSearchIconRemap() {
        recipeBookSearchIconRemapDepth++;
    }

    public static void exitRecipeBookSearchIconRemap() {
        recipeBookSearchIconRemapDepth = Math.max(0, recipeBookSearchIconRemapDepth - 1);
    }

    public static boolean shouldRemapRecipeBookSearchIcon() {
        return recipeBookSearchIconRemapDepth > 0;
    }

    public static void enterRecipeBookToggleIconRemap() {
        recipeBookToggleIconRemapDepth++;
    }

    public static void exitRecipeBookToggleIconRemap() {
        recipeBookToggleIconRemapDepth = Math.max(0, recipeBookToggleIconRemapDepth - 1);
    }

    public static boolean shouldRemapRecipeBookToggleIcon() {
        return recipeBookToggleIconRemapDepth > 0;
    }

    public static void enterAnvilHammerIconRemap() {
        anvilHammerIconRemapDepth++;
    }

    public static void exitAnvilHammerIconRemap() {
        anvilHammerIconRemapDepth = Math.max(0, anvilHammerIconRemapDepth - 1);
    }

    public static boolean shouldRemapAnvilHammerIcon() {
        return anvilHammerIconRemapDepth > 0;
    }

    public static void enterBeaconPowerIconRemap() {
        beaconPowerIconRemapDepth++;
    }

    public static void exitBeaconPowerIconRemap() {
        beaconPowerIconRemapDepth = Math.max(0, beaconPowerIconRemapDepth - 1);
    }

    public static boolean shouldRemapBeaconPowerIcon() {
        return beaconPowerIconRemapDepth > 0;
    }

    public static void enterAe2SortButtonIconRemap() {
        ae2SortButtonIconRemapDepth++;
    }

    public static void exitAe2SortButtonIconRemap() {
        ae2SortButtonIconRemapDepth = Math.max(0, ae2SortButtonIconRemapDepth - 1);
    }

    public static boolean shouldRemapAe2SortButtonIcon() {
        return ae2SortButtonIconRemapDepth > 0;
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

    public static void enterItemRender() {
        itemRenderDepth++;
        suppressTextureDarkening();
    }

    public static void exitItemRender() {
        itemRenderDepth = Math.max(0, itemRenderDepth - 1);
        allowTextureDarkening();
    }

    public static boolean isRenderingItem() {
        return itemRenderDepth > 0;
    }

    public static void enterDarkenedScreenRender() {
        darkenedScreenRenderDepth++;
    }

    public static void exitDarkenedScreenRender() {
        darkenedScreenRenderDepth = Math.max(0, darkenedScreenRenderDepth - 1);
    }

    public static boolean isDarkeningActiveScreen() {
        return darkenedScreenRenderDepth > 0;
    }

    private static boolean isCurrentScreenClassPrefixed(String prefix) {
        Minecraft minecraft = Minecraft.getMinecraft();
        if (minecraft == null) {
            return false;
        }

        GuiScreen screen = minecraft.currentScreen;
        return screen != null && screen.getClass().getName().startsWith(prefix);
    }

    private BlxckoutFXRenderContext() {
    }
}
