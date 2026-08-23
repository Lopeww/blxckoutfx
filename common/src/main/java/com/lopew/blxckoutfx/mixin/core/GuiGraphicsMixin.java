package com.lopew.blxckoutfx.mixin.core;

import com.lopew.blxckoutfx.client.integration.ae2.BlxckoutFXAe2Support;
import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import net.minecraft.client.gui.Font;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mixin(GuiGraphics.class)
public class GuiGraphicsMixin {
    private static final String AE2_VERTICAL_BUTTONS_BG_PATH = "vertical_buttons_bg";
    private static final String AE2_PATTERN_MODES_BG_PATH = "guis/pattern_modes.png";
    private static final String MODMENU_CONFIGURE_BUTTON_PATH = "textures/gui/configure_button.png";
    private static final String MODMENU_FILTERS_BUTTON_PATH = "textures/gui/filters_button.png";
    private static final String RECIPE_BOOK_BUTTON_SPRITE_PATH = "recipe_book/button";
    private static final String RECIPE_BOOK_BUTTON_HIGHLIGHTED_SPRITE_PATH = "recipe_book/button_highlighted";
    private static final String RECIPE_BOOK_FILTER_ENABLED_SPRITE_PATH = "recipe_book/filter_enabled";
    private static final String RECIPE_BOOK_FILTER_ENABLED_HIGHLIGHTED_SPRITE_PATH = "recipe_book/filter_enabled_highlighted";
    private static final String RECIPE_BOOK_FILTER_DISABLED_SPRITE_PATH = "recipe_book/filter_disabled";
    private static final String RECIPE_BOOK_FILTER_DISABLED_HIGHLIGHTED_SPRITE_PATH = "recipe_book/filter_disabled_highlighted";
    private static final String RECIPE_BOOK_PAGE_FORWARD_SPRITE_PATH = "recipe_book/page_forward";
    private static final String RECIPE_BOOK_PAGE_FORWARD_HIGHLIGHTED_SPRITE_PATH = "recipe_book/page_forward_highlighted";
    private static final String RECIPE_BOOK_PAGE_BACKWARD_SPRITE_PATH = "recipe_book/page_backward";
    private static final String RECIPE_BOOK_PAGE_BACKWARD_HIGHLIGHTED_SPRITE_PATH = "recipe_book/page_backward_highlighted";
    private static final String RECIPE_BOOK_TEXTURE_PATH = "textures/gui/recipe_book.png";
    private static final int RECIPE_BOOK_SEARCH_ICON_OFFSET_X = 10;
    private static final int RECIPE_BOOK_SEARCH_ICON_OFFSET_Y = 14;
    private static final String[] RECIPE_BOOK_ICON_MASK = {
            "....................", "....................", "..........###.......",
            "........######......", "......#########.....", "....############....",
            "..###############...", "..###############...", "..###############...",
            "..###############...", "..###############...", "....############....",
            ".....#########......", "......######........", ".......###..........",
            "....................", "....................", "...................."
    };
    private static final String[] RECIPE_BOOK_FILTER_CHECK_MASK = {
            "..........................", "..........................", "..........................",
            "..........................", "..........................", ".........#................",
            "........##................", "...#...##.................", "...##.##..................",
            "....###...................", ".....#....................", "..........................",
            "..........................", "..........................", "..........................", ".........................."
    };
    private static final String[] RECIPE_BOOK_SEARCH_ICON_MASK = {
            "......####..", ".....######.", "....########", "....########",
            "....########", "....########", "...#########", "..#########.",
            ".#########..", ".####.......", "####........", ".##........."
    };
    private static final String[] RECIPE_BOOK_FILTER_X_MASK = {
            "..........................", "..........................", "..........................",
            "..........................", "..........................", "..##....##................",
            "...##..##.................", "....####..................", ".....##...................",
            "....####..................", "...##..##.................", "..##....##................",
            "..........................", "..........................", "..........................", ".........................."
    };
    private static final String[] RECIPE_BOOK_FILTER_ITEM_MASK = {
            "..........................", "..........................", ".................##.......",
            "...............######.....", ".............##########...", ".............###########..",
            ".............###########..", ".............###########..", ".............###########..",
            ".............###########..", ".............###########..", ".............##########...",
            "...............######.....", ".................##.......", "..........................", ".........................."
    };

    @Inject(method = "innerBlit(Lnet/minecraft/resources/ResourceLocation;IIIIIFFFF)V", at = @At("HEAD"))
    private void blxckoutfx$enterAe2TextureRender(
            ResourceLocation atlasLocation,
            int x1,
            int x2,
            int y1,
            int y2,
            int blitOffset,
            float minU,
            float maxU,
            float minV,
            float maxV,
            CallbackInfo ci
    ) {
        if (BlxckoutFXAe2Support.isAe2Texture(atlasLocation)) {
            BlxckoutFXRenderContext.enterAe2TextureRender();
        }
    }

    @Inject(method = "innerBlit(Lnet/minecraft/resources/ResourceLocation;IIIIIFFFF)V", at = @At("RETURN"))
    private void blxckoutfx$exitAe2TextureRender(
            ResourceLocation atlasLocation,
            int x1,
            int x2,
            int y1,
            int y2,
            int blitOffset,
            float minU,
            float maxU,
            float minV,
            float maxV,
            CallbackInfo ci
    ) {
        if (BlxckoutFXAe2Support.isAe2Texture(atlasLocation)) {
            BlxckoutFXRenderContext.exitAe2TextureRender();
        }
    }

    @Inject(method = "innerBlit(Lnet/minecraft/resources/ResourceLocation;IIIIIFFFF)V", at = @At("HEAD"))
    private void blxckoutfx$suppressModMenuIconDarkening(
            ResourceLocation atlasLocation,
            int x1,
            int x2,
            int y1,
            int y2,
            int blitOffset,
            float minU,
            float maxU,
            float minV,
            float maxV,
            CallbackInfo ci
    ) {
        if (isModsScreenLogo(atlasLocation)) {
            BlxckoutFXRenderContext.suppressTextureDarkening();
        }
    }

    @Inject(method = "innerBlit(Lnet/minecraft/resources/ResourceLocation;IIIIIFFFF)V", at = @At("RETURN"))
    private void blxckoutfx$restoreModMenuIconDarkening(
            ResourceLocation atlasLocation,
            int x1,
            int x2,
            int y1,
            int y2,
            int blitOffset,
            float minU,
            float maxU,
            float minV,
            float maxV,
            CallbackInfo ci
    ) {
        if (isModsScreenLogo(atlasLocation)) {
            BlxckoutFXRenderContext.allowTextureDarkening();
        }
    }

    @Inject(method = "innerBlit(Lnet/minecraft/resources/ResourceLocation;IIIIIFFFF)V", at = @At("HEAD"))
    private void blxckoutfx$enterModMenuIconButtonRender(
            ResourceLocation atlasLocation,
            int x1,
            int x2,
            int y1,
            int y2,
            int blitOffset,
            float minU,
            float maxU,
            float minV,
            float maxV,
            CallbackInfo ci
    ) {
        if (isModMenuIconButtonTexture(atlasLocation)) {
            BlxckoutFXRenderContext.enterModMenuIconButtonRender();
        }
    }

    @Inject(method = "innerBlit(Lnet/minecraft/resources/ResourceLocation;IIIIIFFFF)V", at = @At("RETURN"))
    private void blxckoutfx$exitModMenuIconButtonRender(
            ResourceLocation atlasLocation,
            int x1,
            int x2,
            int y1,
            int y2,
            int blitOffset,
            float minU,
            float maxU,
            float minV,
            float maxV,
            CallbackInfo ci
    ) {
        if (isModMenuIconButtonTexture(atlasLocation)) {
            BlxckoutFXRenderContext.exitModMenuIconButtonRender();
        }
    }

    @Inject(method = "blitSprite(Lnet/minecraft/resources/ResourceLocation;IIIII)V", at = @At("HEAD"), require = 0)
    private void blxckoutfx$enterAe2VerticalButtonsBackground(
            ResourceLocation sprite,
            int x,
            int y,
            int blitOffset,
            int width,
            int height,
            CallbackInfo ci
    ) {
        if (blxckoutfx$isAe2VerticalButtonsBackgroundSprite(sprite)) {
            BlxckoutFXRenderContext.enterAe2VerticalButtonBarBackgroundRender();
        }
    }

    @Inject(method = "blitSprite(Lnet/minecraft/resources/ResourceLocation;IIIII)V", at = @At("RETURN"), require = 0)
    private void blxckoutfx$exitAe2VerticalButtonsBackground(
            ResourceLocation sprite,
            int x,
            int y,
            int blitOffset,
            int width,
            int height,
            CallbackInfo ci
    ) {
        if (blxckoutfx$isAe2VerticalButtonsBackgroundSprite(sprite)) {
            BlxckoutFXRenderContext.exitAe2VerticalButtonBarBackgroundRender();
        }
    }

    @Inject(method = "blitSprite(Lnet/minecraft/resources/ResourceLocation;IIIII)V", at = @At("HEAD"), require = 0)
    private void blxckoutfx$preserveInventoryRecipeBookPageArrow(
            ResourceLocation sprite,
            int x,
            int y,
            int blitOffset,
            int width,
            int height,
            CallbackInfo ci
    ) {
        if (blxckoutfx$isInventoryRecipeBookPageArrow(sprite, width, height)) {
            BlxckoutFXRenderContext.suppressTextureDarkening();
        }
    }

    @Inject(method = "blitSprite(Lnet/minecraft/resources/ResourceLocation;IIIII)V", at = @At("RETURN"), require = 0)
    private void blxckoutfx$restoreInventoryRecipeBookPageArrow(
            ResourceLocation sprite,
            int x,
            int y,
            int blitOffset,
            int width,
            int height,
            CallbackInfo ci
    ) {
        if (blxckoutfx$isInventoryRecipeBookPageArrow(sprite, width, height)) {
            BlxckoutFXRenderContext.allowTextureDarkening();
        }
    }

    @Inject(method = "blitSprite(Lnet/minecraft/resources/ResourceLocation;IIIII)V", at = @At("RETURN"), require = 0)
    private void blxckoutfx$redrawInventoryRecipeBookIcon(
            ResourceLocation sprite,
            int x,
            int y,
            int blitOffset,
            int width,
            int height,
            CallbackInfo ci
    ) {
        if (!blxckoutfx$isInventoryRecipeBookButton(sprite, width, height)) {
            return;
        }

        GuiGraphics graphics = (GuiGraphics) (Object) this;
        BlxckoutFXRenderContext.suppressTextureDarkening();
        try {
            for (int row = 0; row < RECIPE_BOOK_ICON_MASK.length; row++) {
                String maskRow = RECIPE_BOOK_ICON_MASK[row];
                int runStart = maskRow.indexOf('#');
                while (runStart >= 0) {
                    int runEnd = maskRow.indexOf('.', runStart);
                    if (runEnd < 0) {
                        runEnd = maskRow.length();
                    }

                    graphics.blitSprite(sprite, 20, 18, runStart, row, x + runStart, y + row, runEnd - runStart, 1);
                    runStart = maskRow.indexOf('#', runEnd);
                }
            }
        } finally {
            BlxckoutFXRenderContext.allowTextureDarkening();
        }
    }

    @Inject(method = "blitSprite(Lnet/minecraft/resources/ResourceLocation;IIIII)V", at = @At("RETURN"), require = 0)
    private void blxckoutfx$redrawInventoryRecipeBookFilterIcon(
            ResourceLocation sprite,
            int x,
            int y,
            int blitOffset,
            int width,
            int height,
            CallbackInfo ci
    ) {
        if (!blxckoutfx$isInventoryRecipeBookFilter(sprite, width, height)) {
            return;
        }

        blxckoutfx$redrawMaskedSprite(
                (GuiGraphics) (Object) this,
                sprite,
                x,
                y,
                26,
                16,
                blxckoutfx$isRecipeBookFilterEnabled(sprite) ? RECIPE_BOOK_FILTER_CHECK_MASK : RECIPE_BOOK_FILTER_X_MASK,
                RECIPE_BOOK_FILTER_ITEM_MASK
        );
    }

    @Inject(method = "blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V", at = @At("RETURN"), require = 0)
    private void blxckoutfx$redrawInventoryRecipeBookSearchIcon(
            ResourceLocation texture,
            int x,
            int y,
            int uOffset,
            int vOffset,
            int width,
            int height,
            CallbackInfo ci
    ) {
        if (!blxckoutfx$isInventoryRecipeBookPanel(texture, uOffset, vOffset, width, height)) {
            return;
        }

        GuiGraphics graphics = (GuiGraphics) (Object) this;
        BlxckoutFXRenderContext.suppressTextureDarkening();
        try {
            for (int row = 0; row < RECIPE_BOOK_SEARCH_ICON_MASK.length; row++) {
                String maskRow = RECIPE_BOOK_SEARCH_ICON_MASK[row];
                int runStart = maskRow.indexOf('#');
                while (runStart >= 0) {
                    int runEnd = maskRow.indexOf('.', runStart);
                    if (runEnd < 0) {
                        runEnd = maskRow.length();
                    }

                    graphics.blit(
                            texture,
                            x + RECIPE_BOOK_SEARCH_ICON_OFFSET_X + runStart,
                            y + RECIPE_BOOK_SEARCH_ICON_OFFSET_Y + row,
                            uOffset + RECIPE_BOOK_SEARCH_ICON_OFFSET_X + runStart,
                            vOffset + RECIPE_BOOK_SEARCH_ICON_OFFSET_Y + row,
                            runEnd - runStart,
                            1
                    );
                    runStart = maskRow.indexOf('#', runEnd);
                }
            }
        } finally {
            BlxckoutFXRenderContext.allowTextureDarkening();
        }
    }

    @Inject(method = "innerBlit(Lnet/minecraft/resources/ResourceLocation;IIIIIFFFF)V", at = @At("HEAD"))
    private void blxckoutfx$enterAe2PatternModesBackground(
            ResourceLocation atlasLocation,
            int x1,
            int x2,
            int y1,
            int y2,
            int blitOffset,
            float minU,
            float maxU,
            float minV,
            float maxV,
            CallbackInfo ci
    ) {
        if (blxckoutfx$isAe2PatternModesBackground(atlasLocation)) {
            BlxckoutFXRenderContext.enterAe2PatternModesBackgroundRender();
        }
    }

    @Inject(method = "innerBlit(Lnet/minecraft/resources/ResourceLocation;IIIIIFFFF)V", at = @At("RETURN"))
    private void blxckoutfx$exitAe2PatternModesBackground(
            ResourceLocation atlasLocation,
            int x1,
            int x2,
            int y1,
            int y2,
            int blitOffset,
            float minU,
            float maxU,
            float minV,
            float maxV,
            CallbackInfo ci
    ) {
        if (blxckoutfx$isAe2PatternModesBackground(atlasLocation)) {
            BlxckoutFXRenderContext.exitAe2PatternModesBackgroundRender();
        }
    }

    @Inject(
            method = "renderTooltip(Lnet/minecraft/client/gui/Font;Ljava/util/List;Ljava/util/Optional;II)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void blxckoutfx$deferPneumaticCraftComponentTooltip(
            Font font,
            List<Component> tooltip,
            Optional<TooltipComponent> visualTooltipComponent,
            int mouseX,
            int mouseY,
            CallbackInfo ci
    ) {
        if (!shouldDeferPneumaticCraftTooltip() || tooltip == null || tooltip.isEmpty() || visualTooltipComponent.isPresent()) {
            return;
        }

        Screen screen = Minecraft.getInstance().screen;
        if (screen == null) {
            return;
        }

        List<FormattedCharSequence> lines = new ArrayList<>(tooltip.size());
        for (Component component : tooltip) {
            lines.add(component.getVisualOrderText());
        }

        screen.setTooltipForNextRenderPass(lines);
        ci.cancel();
    }

    @Inject(
            method = "renderTooltip(Lnet/minecraft/client/gui/Font;Ljava/util/List;II)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void blxckoutfx$deferPneumaticCraftFormattedTooltip(
            Font font,
            List<? extends FormattedCharSequence> tooltip,
            int mouseX,
            int mouseY,
            CallbackInfo ci
    ) {
        if (!shouldDeferPneumaticCraftTooltip() || tooltip == null || tooltip.isEmpty()) {
            return;
        }

        Screen screen = Minecraft.getInstance().screen;
        if (screen == null) {
            return;
        }

        screen.setTooltipForNextRenderPass(new ArrayList<>(tooltip));
        ci.cancel();
    }

    private static boolean isModsScreenLogo(ResourceLocation atlasLocation) {
        String namespace = atlasLocation.getNamespace();
        String path = atlasLocation.getPath();

        if (isFabricModMenuScreen()) {
            return ("modmenu".equals(namespace) && path.endsWith("_icon"))
                    || ("minecraft".equals(namespace) && "textures/misc/unknown_pack.png".equals(path));
        }

        if (isNeoForgeModListScreen()) {
            return "minecraft".equals(namespace)
                    && (path.startsWith("dynamic/modlogo_")
                    || "textures/gui/title/minecraft.png".equals(path)
                    || "textures/gui/title/minceraft.png".equals(path));
        }

        return false;
    }

    private static boolean isFabricModMenuScreen() {
        Screen screen = Minecraft.getInstance().screen;
        return screen != null && screen.getClass().getName().startsWith("com.terraformersmc.modmenu.gui.");
    }

    private static boolean isModMenuIconButtonTexture(ResourceLocation atlasLocation) {
        if (!isFabricModMenuScreen() || atlasLocation == null || !"modmenu".equals(atlasLocation.getNamespace())) {
            return false;
        }

        String path = atlasLocation.getPath();
        return MODMENU_CONFIGURE_BUTTON_PATH.equals(path) || MODMENU_FILTERS_BUTTON_PATH.equals(path);
    }

    private static boolean blxckoutfx$isInventoryRecipeBookButton(ResourceLocation sprite, int width, int height) {
        Screen screen = Minecraft.getInstance().screen;
        if (sprite == null || width != 20 || height != 18 || screen == null || screen.getClass() != InventoryScreen.class) {
            return false;
        }

        if (!"minecraft".equals(sprite.getNamespace())) {
            return false;
        }

        String path = sprite.getPath();
        return RECIPE_BOOK_BUTTON_SPRITE_PATH.equals(path)
                || RECIPE_BOOK_BUTTON_HIGHLIGHTED_SPRITE_PATH.equals(path);
    }

    private static boolean blxckoutfx$isInventoryRecipeBookPanel(
            ResourceLocation texture,
            int uOffset,
            int vOffset,
            int width,
            int height
    ) {
        return blxckoutfx$isVanillaInventoryScreen(texture)
                && RECIPE_BOOK_TEXTURE_PATH.equals(texture.getPath())
                && uOffset == 1
                && vOffset == 1
                && width == 147
                && height == 166;
    }

    private static boolean blxckoutfx$isInventoryRecipeBookFilter(ResourceLocation sprite, int width, int height) {
        if (!blxckoutfx$isVanillaInventoryScreen(sprite) || width != 26 || height != 16) {
            return false;
        }

        String path = sprite.getPath();
        return RECIPE_BOOK_FILTER_ENABLED_SPRITE_PATH.equals(path)
                || RECIPE_BOOK_FILTER_ENABLED_HIGHLIGHTED_SPRITE_PATH.equals(path)
                || RECIPE_BOOK_FILTER_DISABLED_SPRITE_PATH.equals(path)
                || RECIPE_BOOK_FILTER_DISABLED_HIGHLIGHTED_SPRITE_PATH.equals(path);
    }

    private static boolean blxckoutfx$isInventoryRecipeBookPageArrow(ResourceLocation sprite, int width, int height) {
        if (!blxckoutfx$isVanillaInventoryScreen(sprite) || width != 12 || height != 17) {
            return false;
        }

        String path = sprite.getPath();
        return RECIPE_BOOK_PAGE_FORWARD_SPRITE_PATH.equals(path)
                || RECIPE_BOOK_PAGE_FORWARD_HIGHLIGHTED_SPRITE_PATH.equals(path)
                || RECIPE_BOOK_PAGE_BACKWARD_SPRITE_PATH.equals(path)
                || RECIPE_BOOK_PAGE_BACKWARD_HIGHLIGHTED_SPRITE_PATH.equals(path);
    }

    private static boolean blxckoutfx$isRecipeBookFilterEnabled(ResourceLocation sprite) {
        String path = sprite.getPath();
        return RECIPE_BOOK_FILTER_ENABLED_SPRITE_PATH.equals(path)
                || RECIPE_BOOK_FILTER_ENABLED_HIGHLIGHTED_SPRITE_PATH.equals(path);
    }

    private static boolean blxckoutfx$isVanillaInventoryScreen(ResourceLocation sprite) {
        Screen screen = Minecraft.getInstance().screen;
        return sprite != null
                && "minecraft".equals(sprite.getNamespace())
                && screen != null
                && screen.getClass() == InventoryScreen.class;
    }

    private static void blxckoutfx$redrawMaskedSprite(GuiGraphics graphics, ResourceLocation sprite, int x, int y,
                                                        int spriteWidth, int spriteHeight, String[]... masks) {
        BlxckoutFXRenderContext.suppressTextureDarkening();
        try {
            for (String[] mask : masks) {
                for (int row = 0; row < mask.length; row++) {
                    String maskRow = mask[row];
                    int runStart = maskRow.indexOf('#');
                    while (runStart >= 0) {
                        int runEnd = maskRow.indexOf('.', runStart);
                        if (runEnd < 0) {
                            runEnd = maskRow.length();
                        }

                        graphics.blitSprite(sprite, spriteWidth, spriteHeight, runStart, row, x + runStart, y + row, runEnd - runStart, 1);
                        runStart = maskRow.indexOf('#', runEnd);
                    }
                }
            }
        } finally {
            BlxckoutFXRenderContext.allowTextureDarkening();
        }
    }

    private static boolean isNeoForgeModListScreen() {
        Screen screen = Minecraft.getInstance().screen;
        return screen != null && "net.neoforged.neoforge.client.gui.ModListScreen".equals(screen.getClass().getName());
    }

    private static boolean shouldDeferPneumaticCraftTooltip() {
        Screen screen = Minecraft.getInstance().screen;
        return screen != null
                && screen.getClass().getName().startsWith("me.desht.pneumaticcraft.client.gui.")
                && BlxckoutFXRenderContext.isRenderingPneumaticCraftWidget();
    }

    private static boolean blxckoutfx$isAe2VerticalButtonsBackgroundSprite(ResourceLocation sprite) {
        return sprite != null
                && BlxckoutFXRenderContext.isRenderingAe2Screen()
                && BlxckoutFXAe2Support.isBlackoutAe2ThemeActive()
                && "ae2".equals(sprite.getNamespace())
                && AE2_VERTICAL_BUTTONS_BG_PATH.equals(sprite.getPath());
    }

    private static boolean blxckoutfx$isAe2PatternModesBackground(ResourceLocation atlasLocation) {
        if (atlasLocation == null || !BlxckoutFXRenderContext.isRenderingAe2Screen()) {
            return false;
        }

        if (!BlxckoutFXAe2Support.isBlackoutAe2ThemeActive()) {
            return false;
        }

        if (!BlxckoutFXAe2Support.isAe2Texture(atlasLocation)) {
            return false;
        }

        String path = atlasLocation.getPath();
        return path != null && path.endsWith(AE2_PATTERN_MODES_BG_PATH);
    }

}

