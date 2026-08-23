package com.lopew.blxckoutfx.mixin.patchouli;

import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Redraws only Patchouli book-button glyph pixels after their darkened base texture. */
@Mixin(GuiGraphics.class)
public class PatchouliBookGlyphMixin {
    @Unique private static final int BOOK_TEXTURE_WIDTH = 512;
    @Unique private static final int BOOK_TEXTURE_HEIGHT = 256;
    @Unique private static final int ADD_BOOKMARK_GLYPH_COLOR = 0xFFD0D0D0;
    @Unique private int blxckoutfx$redrawDepth;

    @Inject(method = "innerBlit(Lnet/minecraft/resources/ResourceLocation;IIIIIFFFF)V", at = @At("RETURN"))
    private void blxckoutfx$redrawPatchouliBookButtonGlyph(
            ResourceLocation texture,
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
        if (blxckoutfx$redrawDepth > 0 || !BlxckoutFXRenderContext.isRenderingPatchouliBookButton()) {
            return;
        }

        int sourceX = Math.round(minU * BOOK_TEXTURE_WIDTH);
        int sourceY = Math.round(minV * BOOK_TEXTURE_HEIGHT);
        int sourceWidth = Math.round((maxU - minU) * BOOK_TEXTURE_WIDTH);
        int sourceHeight = Math.round((maxV - minV) * BOOK_TEXTURE_HEIGHT);
        String[] mask = blxckoutfx$getGlyphMask(sourceX, sourceY, sourceWidth, sourceHeight);
        if (mask != null) {
            blxckoutfx$redrawMaskedGlyph((GuiGraphics) (Object) this, texture, x1, x2, y1, y2, sourceX, sourceY, mask);
            return;
        }

        String[] plusMask = blxckoutfx$getAddBookmarkPlusMask(sourceX, sourceY, sourceWidth, sourceHeight);
        if (plusMask != null) {
            blxckoutfx$redrawAddBookmarkPlus((GuiGraphics) (Object) this, x1, x2, y1, y2, plusMask);
        }
    }

    @Unique
    private static String[] blxckoutfx$getGlyphMask(int sourceX, int sourceY, int width, int height) {
        if (sourceY == 0 && width == 18 && height == 9 && (sourceX == 308 || sourceX == 326)) return BACK_MASK;
        if (sourceY == 0 && width == 18 && height == 10 && (sourceX == 272 || sourceX == 290)) return NEXT_ARROW_MASK;
        if (sourceY == 10 && width == 18 && height == 10 && (sourceX == 272 || sourceX == 290)) return PREVIOUS_ARROW_MASK;
        if (sourceY == 20 && width == 5 && height == 7 && (sourceX == 272 || sourceX == 277)) return SMALL_NEXT_ARROW_MASK;
        if (sourceY == 27 && width == 5 && height == 7 && (sourceX == 272 || sourceX == 277)) return SMALL_PREVIOUS_ARROW_MASK;
        if (sourceY == 9 && width == 11 && height == 11 && (sourceX == 308 || sourceX == 319)) return EDITOR_MASK;
        if (sourceY == 9 && width == 11 && height == 11 && (sourceX == 330 || sourceX == 341)) return RESIZE_MASK;
        if (sourceY == 20 && width == 11 && height == 11 && (sourceX == 308 || sourceX == 319)) return CONFIG_MASK;
        if (sourceY == 20 && width == 11 && height == 11 && (sourceX == 330 || sourceX == 341)) return ADVANCEMENTS_MASK;
        if (sourceY == 31 && width == 11 && height == 11 && (sourceX == 308 || sourceX == 319)) return MARK_READ_MASK;
        if (sourceY == 31 && width == 11 && height == 11 && (sourceX == 330 || sourceX == 341)) return HISTORY_MASK;
        return null;
    }

    @Unique
    private static String[] blxckoutfx$getAddBookmarkPlusMask(int sourceX, int sourceY, int width, int height) {
        if (sourceY != 170 || width != 13 || height != 10) return null;
        return sourceX == 272 ? ADD_BOOKMARK_PLUS_MASK : sourceX == 285 ? ADD_BOOKMARK_PLUS_HOVER_MASK : null;
    }

    @Unique
    private void blxckoutfx$redrawMaskedGlyph(GuiGraphics graphics, ResourceLocation texture, int x1, int x2, int y1, int y2,
                                                int sourceX, int sourceY, String[] mask) {
        blxckoutfx$redrawDepth++;
        BlxckoutFXRenderContext.suppressTextureDarkening();
        try {
            int maskWidth = mask[0].length();
            int maskHeight = mask.length;
            for (int row = 0; row < maskHeight; row++) {
                String maskRow = mask[row];
                int runStart = maskRow.indexOf('#');
                while (runStart >= 0) {
                    int runEnd = maskRow.indexOf('.', runStart);
                    if (runEnd < 0) runEnd = maskRow.length();
                    int drawX1 = blxckoutfx$map(x1, x2, runStart, maskWidth);
                    int drawX2 = blxckoutfx$map(x1, x2, runEnd, maskWidth);
                    int drawY1 = blxckoutfx$map(y1, y2, row, maskHeight);
                    int drawY2 = blxckoutfx$map(y1, y2, row + 1, maskHeight);
                    if (drawX1 < drawX2 && drawY1 < drawY2) {
                        graphics.blit(texture, drawX1, drawY1, drawX2 - drawX1, drawY2 - drawY1,
                                sourceX + runStart, sourceY + row, runEnd - runStart, 1, BOOK_TEXTURE_WIDTH, BOOK_TEXTURE_HEIGHT);
                    }
                    runStart = maskRow.indexOf('#', runEnd);
                }
            }
        } finally {
            BlxckoutFXRenderContext.allowTextureDarkening();
            blxckoutfx$redrawDepth--;
        }
    }

    @Unique
    private void blxckoutfx$redrawAddBookmarkPlus(GuiGraphics graphics, int x1, int x2, int y1, int y2, String[] mask) {
        int maskWidth = mask[0].length();
        int maskHeight = mask.length;
        BlxckoutFXRenderContext.suppressTextureDarkening();
        try {
            for (int row = 0; row < maskHeight; row++) {
                String maskRow = mask[row];
                int runStart = maskRow.indexOf('#');
                while (runStart >= 0) {
                    int runEnd = maskRow.indexOf('.', runStart);
                    if (runEnd < 0) runEnd = maskRow.length();
                    int drawX1 = blxckoutfx$map(x1, x2, runStart, maskWidth);
                    int drawX2 = blxckoutfx$map(x1, x2, runEnd, maskWidth);
                    int drawY1 = blxckoutfx$map(y1, y2, row, maskHeight);
                    int drawY2 = blxckoutfx$map(y1, y2, row + 1, maskHeight);
                    if (drawX1 < drawX2 && drawY1 < drawY2) graphics.fill(drawX1, drawY1, drawX2, drawY2, ADD_BOOKMARK_GLYPH_COLOR);
                    runStart = maskRow.indexOf('#', runEnd);
                }
            }
        } finally {
            BlxckoutFXRenderContext.allowTextureDarkening();
        }
    }

    @Unique
    private static int blxckoutfx$map(int start, int end, int source, int sourceSize) {
        return Math.round(start + (end - start) * source / (float) sourceSize);
    }

    @Unique private static final String[] BACK_MASK = {
            "....##.......#####", "...###.......#####", "..################", ".#################",
            "##################", ".#################", "..################", "...###............", "....##............"
    };
    @Unique private static final String[] NEXT_ARROW_MASK = {
            "...........#######", "..........########", "...#.......#######", "..###.......######", ".#####.....#######",
            "##################", ".#################", "..############.###", "...##########...#.", "....########......"
    };
    @Unique private static final String[] PREVIOUS_ARROW_MASK = {
            "#######...........", "########..........", "#######.......#...", "######.......###..", "#######.....#####.",
            "##################", "#################.", "###.############..", ".#...##########...", "......########...."
    };
    @Unique private static final String[] SMALL_NEXT_ARROW_MASK = { "##...", "###..", "####.", "#####", "####.", "###..", "##..." };
    @Unique private static final String[] SMALL_PREVIOUS_ARROW_MASK = { "...##", "..###", ".####", "#####", ".####", "..###", "...##" };
    @Unique private static final String[] EDITOR_MASK = {
            "..##.......", ".####......", "######.....", "#######....", ".#######...", "..#######..",
            "...#######.", "....#######", ".....######", "......#####", ".......####"
    };
    @Unique private static final String[] RESIZE_MASK = {
            ".....#.....", "....###....", "...#####...", "..#######..", "..#######..", "...........",
            "..#######..", "..#######..", "...#####...", "....###....", ".....#....."
    };
    @Unique private static final String[] CONFIG_MASK = {
            ".....#.....", ".##.###.##.", ".#########.", "..##...##..", ".##..#..##.", "###.###.###",
            ".##..#..##.", "..##...##..", ".#########.", ".##.###.##.", ".....#....."
    };
    @Unique private static final String[] ADVANCEMENTS_MASK = {
            ".....#.....", "....###....", "...#####...", ".#########.", "###########", ".#########.",
            "..#######..", "..#######..", ".#########.", ".#########.", ".###...###."
    };
    @Unique private static final String[] MARK_READ_MASK = {
            "...........", "...........", "...#####...", "..#######..", ".###...###.", "###..#..###",
            ".###...###.", "..#######..", "...#####...", "...........", "..........."
    };
    @Unique private static final String[] HISTORY_MASK = {
            "..#######..", "..#.....#..", "..#.#####..", "...#####...", "...#####...", "....###....",
            "...#.#.#...", "...#####...", "..#######..", "..#######..", "..#######.."
    };
    @Unique private static final String[] ADD_BOOKMARK_PLUS_MASK = {
            ".............", ".............", "....###......", "....###......", "..#######....",
            "..#######....", "....###......", "....###......", ".............", "............."
    };
    @Unique private static final String[] ADD_BOOKMARK_PLUS_HOVER_MASK = {
            ".............", ".............", "......###....", "......###....", "....#######..",
            "....#######..", "......###....", "......###....", ".............", "............."
    };
}
