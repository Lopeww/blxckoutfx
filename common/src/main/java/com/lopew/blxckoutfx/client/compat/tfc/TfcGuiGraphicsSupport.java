package com.lopew.blxckoutfx.client.compat.tfc;

import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public final class TfcGuiGraphicsSupport {
    private TfcGuiGraphicsSupport() {
    }

    public static final int TEXTURE_SIZE = 256;
    public static final String TFC = "tfc";
    public static final String FIRMALIFE = "firmalife";
    public static final String ANVIL = "textures/gui/anvil.png";
    public static final String SCRIBING = "textures/gui/scribing_table.png";
    public static final String SEWING = "textures/gui/sewing.png";
    public static final String FIREPIT = "textures/gui/fire_pit.png";
    public static final String FIREPIT_POT = "textures/gui/fire_pit_cooking_pot.png";
    public static final String FIREPIT_GRILL = "textures/gui/fire_pit_grill.png";
    public static final String CRUCIBLE = "textures/gui/crucible.png";
    public static final String BLAST_FURNACE = "textures/gui/blast_furnace.png";
    public static final String BARREL = "textures/gui/barrel.png";
    public static final String POWDERKEG = "textures/gui/powderkeg.png";
    public static final String PLAYER_CALENDAR = "textures/gui/player_calendar.png";
    public static final String PLAYER_NUTRITION = "textures/gui/player_nutrition.png";
    public static final String LARGE_VESSEL = "textures/gui/large_vessel.png";
    public static final String STOVETOP_POT = "textures/gui/stovetop_pot.png";
    public static final String STOVETOP_GRILL = "textures/gui/stovetop_grill.png";
    public static final int CRUCIBLE_GAUGE_X = 11;
    public static final int CRUCIBLE_GAUGE_Y = 83;
    public static final int CRUCIBLE_GAUGE_WIDTH = 7;
    public static final int CRUCIBLE_GAUGE_HEIGHT = 46;
    public static final int CRUCIBLE_VESSEL_X = 94;
    public static final int CRUCIBLE_VESSEL_Y = 86;
    public static final int CRUCIBLE_VESSEL_WIDTH = 42;
    public static final int CRUCIBLE_VESSEL_HEIGHT = 41;
    public static final int FIREPIT_GAUGE_X = 34;
    public static final int FIREPIT_GAUGE_Y = 26;
    public static final int FIREPIT_GAUGE_WIDTH = 7;
    public static final int FIREPIT_GAUGE_HEIGHT = 50;
    public static final int POT_VISUAL_X = 121;
    public static final int POT_VISUAL_Y = 10;
    public static final int POT_VISUAL_WIDTH = 43;
    public static final int POT_VISUAL_HEIGHT = 48;
    public static final String[] ACTION_MASK_0 = {
            "................", "................", "................", "................",
            "................", "..##...#######..", ".####.#########.", ".####.#########.",
            ".##############.", ".##############.", "..############..", "................",
            "................", "................", "................", "................"
    };
    public static final String[] ACTION_MASK_1 = {
            "................", "................", "................", ".....########...",
            "....##########..", "...###########..", "..############..", "..############..",
            "..############..", "..######........", "..######........", "..######........",
            "...####.........", "................", "................", "................"
    };
    public static final String[] ACTION_MASK_2 = {
            "................", "................", "................", "..###...........",
            ".#######........", ".#############..", ".##############.", ".##############.",
            ".##############.", ".##############.", ".##############.", ".#######........",
            "..###...........", "................", "................", "................"
    };
    public static final String[] ACTION_MASK_3 = {
            "................", "................", "................", "..##............",
            ".####...........", ".#####..........", ".#############..", "..#############.",
            "..#############.", "...############.", "....###########.", "......########..",
            "................", "................", "................", "................"
    };
    public static final String[] ACTION_MASK_4 = {
            "................", "................", "........#.......", ".......###......",
            ".......###......", "...###..#.......", "..#####.........", "..############..",
            "..#############.", "..############..", "..#####.........", "...###..........",
            "................", "................", "................", "................"
    };
    public static final String[] ACTION_MASK_5 = {
            "................", "................", "........#.#.....", ".......#####....",
            "..####.#####....", ".######.#.#.....", ".######.........", ".#############..",
            ".##############.", ".#############..", ".######.........", ".######.........",
            "..####..........", "................", "................", "................"
    };
    public static final String[] ACTION_MASK_6 = {
            "................", "................", ".........#.#.#..", "..#####.#######.",
            ".##############.", ".#######.#.#.#..", ".#######........", ".#############..",
            ".##############.", ".#############..", ".#######........", ".#######........",
            ".#######........", "..#####.........", "................", "................"
    };
    public static final String[] ACTION_MASK_7 = {
            "................", "................", "................", "................",
            "...........###..", ".........######.", ".......########.", "..#############.",
            ".##############.", "..#############.", ".......########.", ".........######.",
            "...........###..", "................", "................", "................"
    };
    public static final String[][] ACTION_MASKS = {
            ACTION_MASK_0, ACTION_MASK_1, ACTION_MASK_2, ACTION_MASK_3,
            ACTION_MASK_4, ACTION_MASK_5, ACTION_MASK_6, ACTION_MASK_7
    };
    public static final String[] PLAN_QUESTION_MASK = {
            ".........", ".........", ".........", "...###...",
            "..#...#..", "..#...#..", "......#..", "....##...",
            "...#.....", "...#.....", ".........", "...#.....",
            ".........", "........."
    };
    public static final String[][] NUTRITION_ICON_MASKS = {
            {
                    ".....###.", "....#####", "...######", "..#######", ".#######.",
                    "#######..", "######...", "#####....", ".###....."
            },
            {
                    ".....##..", "....####.", "...######", "..#######", ".########",
                    "#########", "########.", ".######..", "..####..."
            },
            {
                    ".....##..", "....####.", "....#####", "...######", "..######.",
                    ".#######.", "#######..", "#####....", "###......"
            },
            {
                    ".....##..", "....####.", "...######", "..#######", "..######.",
                    "..#####..", "######...", "###......", ".##......"
            },
            {
                    ".....##..", "....####.", "...######", "..#######", ".########",
                    "#########", "########.", ".#####...", "..##....."
            }
    };
    public static final String[] JEI_KNAPPING_ARROW_MASK = {
            "..............#.......", "..............##......", "..............###.....", "..............####....",
            "..............#####...", "..............######..", "#####################.", "######################",
            "#####################.", "..............######..", "..............#####...", "..............####....",
            "..............###.....", "..............##......", "..............#.......", "......................"
    };
    public static final String[] SCRIBING_LOGO_MASK = {
            "..................................", "..................................", ".........................##.......", ".........................##.......",
            ".........................####.....", ".........................####.....", ".......................######.....", ".......................######.....",
            ".....................########.....", ".....................########.....", ".....................########.....", ".....................########.....",
            ".................##########.......", ".................##########.......", ".................##########.......", ".................##########.......",
            "...............############.......", "...............############.......", ".............##########...........", ".............##########...........",
            ".............##########...........", ".............##########...........", "...........##########.............", "...........##########.............",
            ".........##########...............", ".........##########...............", ".......##############.............", ".......##############.............",
            ".....##################...........", ".....##################...........", ".....##################...........", ".....##################...........",
            ".....##################...........", ".....##################...........", ".......##############.............", ".......##############.............",
            ".........########.................", ".........########.................", "..................................", ".................................."
    };
    public static final String[] SEWING_CLOTH_ICON_MASK = {
            "....................", "....................", "....................", "....................",
            ".........###........", "........#####.......", ".......#######......", "......#########.....",
            ".....###########....", "...##############...", "...##############...", "....#############...",
            ".....############...", "......######.###....", "......#####.........", ".......##...........",
            "....................", "....................", "....................", "...................."
    };
    public static final String[] SEWING_REMOVE_STITCH_ICON_MASK = {
            "....................", "....................", "....................", "....#..........#....",
            "...###........###...", "....###......###....", ".....###....###.....", "......###..###......",
            ".......######.......", "........####........", "........####........", ".......######.......",
            "......###..###......", ".....###....###.....", "....###......###....", "...###........###...",
            "...##..........##...", "....................", "....................", "...................."
    };
    public static final String[] SEWING_STITCH_ICON_MASK = {
            "....................", "....................", "....................", "....................",
            "............####....", "...........##..#....", "...........##..#....", "..........#####.....",
            "..........###.......", ".........###........", "........###.........", ".......###..........",
            "......###...........", ".....###............", "....##..............", "....#...............",
            "....................", "....................", "....................", "...................."
    };
    public static final String[] SEWING_RECIPE_BOOK_ICON_MASK = {
            "....................", "....................", "..........###.......", "........######......",
            "......#########.....", "....############....", "..###############...", "..###############...",
            "..###############...", "..###############...", "..###############...", "....############....",
            ".....#########......", "......######........", ".......###..........", "....................",
            "....................", "...................."
    };
    public static final String[] GRILL_SYMBOL_MASK = {
            "........########..........", "........########..........",
            "......############........", "......############........",
            "....####..##..########....", "....####..##..########....",
            "..####....######..######..", "..####....######..######..",
            "####......####......######", "####......####......######",
            "##......####......####..##", "##......####......####..##",
            "############....####....##", "############....####....##",
            "##....##..##..####......##", "##....##..##..####......##",
            "##......########........##", "##......########........##",
            "##....##..####........####", "##....##..####........####",
            "##..##....##........##....", "##..##....##........##....",
            "####......##......##......", "####......##......##......",
            "..........##....##........", "..........##....##........",
            "..........##..##..........", "..........##..##..........",
            "..........####............", "..........####............"
    };
    public static final String[] BLAST_FURNACE_LEFT_ARROW_MASK = {
            "##.........", ".##........", "..##.......", "...##......", "....##.....", ".....##....",
            "......##...", ".....##....", "....##.....", "...##......", "..##.......", ".##........",
            "##........."
    };
    public static final String[] BLAST_FURNACE_RIGHT_ARROW_MASK = {
            ".........##", "........###", ".......###.", "......###..", ".....###...", "....###....",
            "...###.....", "...###.....", "....###....", ".....###...", "......###..", ".......###.",
            "........###", ".........##"
    };
    public static final String[] BLAST_FURNACE_TEMPERATURE_MASK = {
            "...............", "...............", "...#########...", "...#########...", "...#########...",
            "...#########...", "...#########...", "...#########...", "...#########...", "...#########...",
            "...#########...", "...#########...", "...#########...", "...#########...", "...#########...",
            "...#########...", "...#########...", "...#########...", "...#########...", "...#########...",
            "...#########...", "...#########...", "...#########...", "...#########...", "...#########...",
            "...#########...", "...#########...", "...#########...", "...#########...", "...#########...",
            "...#########...", "...#########...", "...#########...", "...#########...", "...#########...",
            "...#########...", "...#########...", "...#########...", "...#########...", "...#########...",
            "...#########...", "...#########...", "...#########...", "...#########...", "...#########...",
            "...#########...", "...#########...", "...#########...", "...#########...", "...#########...",
            "...#########..."
    };
    public static final String[] BLAST_FURNACE_VESSEL_MASK = {
            "###....................................###", "###....................................###",
            "##########################################", "##########################################", "##########################################",
            "##########################################", "##########################################", "##########################################",
            "##########################################", "##########################################", "##########################################",
            "##########################################", "##########################################", "##########################################",
            "##########################################", "##########################################", "##########################################",
            "##########################################", "##########################################", "##########################################",
            "##########################################", "##########################################", "##########################################",
            "##########################################", "##########################################", "##########################################",
            "##########################################", "##########################################", "##########################################",
            "##########################################", "##########################################", "##########################################",
            "##########################################", ".########################################.", "..######################################.."
    };
    public static int blxckoutfx$redrawDepth;
    public static final String[] BARREL_SEAL_ICON_MASK = {
            "....................", "....................", "....................", "....................",
            "....................", "....................", "....###########.....", "....############....",
            "....###.....#.##....", ".....###....##.#....", "......##....##......", "......##....##......",
            "......##....##......", "......##....##......", "......########......", ".......#######......",
            "....................", "....................", "....................", "...................."
    };
    public static final String[] BARREL_UNSEAL_ICON_MASK = {
            "....................", "....................", "............##......", "..........##.##.....",
            "........##.####.....", "......##.##...#.....", "....##.##...........", "....###.............",
            "....###.....#.......", ".....###....##......", "......##....##......", "......##....##......",
            "......##....##......", "......##....##......", "......########......", ".......#######......",
            "....................", "....................", "....................", "...................."
    };
    public static final String[] BARREL_QUESTION_MASK = {
            ".........", ".........", ".........", "...###...", "..#...#..", "..#...#..", "......#..",
            "....##...", "...#.....", "...#.....", ".........", "...#.....", ".........", "........."
    };
    public static void blxckoutfx$preserveDirectIndicators(ResourceLocation texture, int x1, int x2, int y1, int y2,
                                                      int blitOffset, float minU, float maxU, float minV, float maxV) {
        if (blxckoutfx$isCrucibleBase(texture, minU, maxU, minV, maxV)) {
            BlxckoutFXRenderContext.enterTfcCrucibleTemperatureRender(
                    blxckoutfx$map(x1, x2, minU, maxU, CRUCIBLE_GAUGE_X),
                    blxckoutfx$map(y1, y2, minV, maxV, CRUCIBLE_GAUGE_Y),
                    blxckoutfx$map(x1, x2, minU, maxU, CRUCIBLE_GAUGE_X + CRUCIBLE_GAUGE_WIDTH),
                    blxckoutfx$map(y1, y2, minV, maxV, CRUCIBLE_GAUGE_Y + CRUCIBLE_GAUGE_HEIGHT)
            );
            BlxckoutFXRenderContext.enterTfcCrucibleVesselRender(
                    blxckoutfx$map(x1, x2, minU, maxU, CRUCIBLE_VESSEL_X),
                    blxckoutfx$map(y1, y2, minV, maxV, CRUCIBLE_VESSEL_Y),
                    blxckoutfx$map(x1, x2, minU, maxU, CRUCIBLE_VESSEL_X + CRUCIBLE_VESSEL_WIDTH),
                    blxckoutfx$map(y1, y2, minV, maxV, CRUCIBLE_VESSEL_Y + CRUCIBLE_VESSEL_HEIGHT)
            );
        } else if (blxckoutfx$isCrucibleTemperatureIndicator(texture, minU, maxU, minV, maxV)) {
            BlxckoutFXRenderContext.enterTfcCrucibleTemperatureRender(x1, y1, x2, y2);
        } else if (blxckoutfx$isFirepitBase(texture, minU, maxU, minV, maxV)) {
            BlxckoutFXRenderContext.enterTfcFirepitTemperatureRender(
                    blxckoutfx$map(x1, x2, minU, maxU, FIREPIT_GAUGE_X),
                    blxckoutfx$map(y1, y2, minV, maxV, FIREPIT_GAUGE_Y),
                    blxckoutfx$map(x1, x2, minU, maxU, FIREPIT_GAUGE_X + FIREPIT_GAUGE_WIDTH),
                    blxckoutfx$map(y1, y2, minV, maxV, FIREPIT_GAUGE_Y + FIREPIT_GAUGE_HEIGHT)
            );
        } else if (blxckoutfx$isFirepitTemperatureIndicator(texture, minU, maxU, minV, maxV)) {
            BlxckoutFXRenderContext.enterTfcFirepitTemperatureRender(x1, y1, x2, y2);
        } else if (blxckoutfx$isPotTemperatureBase(texture, minU, maxU, minV, maxV)
                || blxckoutfx$isStovetopPotBase(texture, minU, maxU, minV, maxV)) {
            BlxckoutFXRenderContext.enterTfcFirepitTemperatureRender(
                    blxckoutfx$map(x1, x2, minU, maxU, FIREPIT_GAUGE_X),
                    blxckoutfx$map(y1, y2, minV, maxV, FIREPIT_GAUGE_Y),
                    blxckoutfx$map(x1, x2, minU, maxU, FIREPIT_GAUGE_X + FIREPIT_GAUGE_WIDTH),
                    blxckoutfx$map(y1, y2, minV, maxV, FIREPIT_GAUGE_Y + FIREPIT_GAUGE_HEIGHT)
            );
            BlxckoutFXRenderContext.enterTfcFirepitPotRender(
                    blxckoutfx$map(x1, x2, minU, maxU, POT_VISUAL_X),
                    blxckoutfx$map(y1, y2, minV, maxV, POT_VISUAL_Y),
                    blxckoutfx$map(x1, x2, minU, maxU, POT_VISUAL_X + POT_VISUAL_WIDTH),
                    blxckoutfx$map(y1, y2, minV, maxV, POT_VISUAL_Y + POT_VISUAL_HEIGHT)
            );
        } else if (blxckoutfx$isPotTemperatureIndicator(texture, minU, maxU, minV, maxV)) {
            BlxckoutFXRenderContext.enterTfcFirepitTemperatureRender(x1, y1, x2, y2);
        }
        if (blxckoutfx$redrawDepth > 0 || blxckoutfx$isDirectIndicator(texture, minU, maxU, minV, maxV)) {
            BlxckoutFXRenderContext.suppressTextureDarkening();
        }
    }
    public static void blxckoutfx$redrawColourData(GuiGraphics graphics, ResourceLocation texture, int x1, int x2, int y1, int y2,
                                             int blitOffset, float minU, float maxU, float minV, float maxV) {
        if (blxckoutfx$isCrucibleBase(texture, minU, maxU, minV, maxV)) {
            BlxckoutFXRenderContext.exitTfcCrucibleVesselRender();
            BlxckoutFXRenderContext.exitTfcCrucibleTemperatureRender();
        } else if (blxckoutfx$isCrucibleTemperatureIndicator(texture, minU, maxU, minV, maxV)) {
            BlxckoutFXRenderContext.exitTfcCrucibleTemperatureRender();
        } else if (blxckoutfx$isFirepitBase(texture, minU, maxU, minV, maxV)
                || blxckoutfx$isFirepitTemperatureIndicator(texture, minU, maxU, minV, maxV)) {
            BlxckoutFXRenderContext.exitTfcFirepitTemperatureRender();
        } else if (blxckoutfx$isPotTemperatureBase(texture, minU, maxU, minV, maxV)
                || blxckoutfx$isStovetopPotBase(texture, minU, maxU, minV, maxV)) {
            BlxckoutFXRenderContext.exitTfcFirepitPotRender();
            BlxckoutFXRenderContext.exitTfcFirepitTemperatureRender();
        } else if (blxckoutfx$isPotTemperatureIndicator(texture, minU, maxU, minV, maxV)) {
            BlxckoutFXRenderContext.exitTfcFirepitTemperatureRender();
        }
        boolean redraw = blxckoutfx$redrawDepth > 0;
        if (redraw || blxckoutfx$isDirectIndicator(texture, minU, maxU, minV, maxV)) {
            BlxckoutFXRenderContext.allowTextureDarkening();
        }
        if (redraw) {
            return;
        }

        if (blxckoutfx$isTexture(texture, TFC, SCRIBING) && blxckoutfx$isScribingTableScreen(Minecraft.getInstance().screen)) {
            blxckoutfx$redrawMaskedRegion(graphics, texture, x1, x2, y1, y2, minU, maxU, minV, maxV, 18, 3, SCRIBING_LOGO_MASK);
        } else if (blxckoutfx$isTexture(texture, TFC, SEWING)) {
            // The stitch markers are separate later blits; keep the base surface bright without restoring its frame.
            blxckoutfx$redrawRegion(graphics, texture, x1, x2, y1, y2, minU, maxU, minV, maxV, 12, 18, 104, 56);
        } else if (blxckoutfx$isGrillBase(texture, minU, maxU, minV, maxV)
                || blxckoutfx$isStovetopGrillBase(texture, minU, maxU, minV, maxV)) {
            // Match the full firepit/pot temperature indicator, including its top and bottom caps.
            blxckoutfx$redrawRegion(graphics, texture, x1, x2, y1, y2, minU, maxU, minV, maxV,
                    FIREPIT_GAUGE_X, FIREPIT_GAUGE_Y, FIREPIT_GAUGE_WIDTH, FIREPIT_GAUGE_HEIGHT);
            blxckoutfx$redrawMaskedRegion(graphics, texture, x1, x2, y1, y2, minU, maxU, minV, maxV,
                    59, 48, GRILL_SYMBOL_MASK);
        } else if (blxckoutfx$isBlastFurnaceBase(texture, minU, maxU, minV, maxV)) {
            blxckoutfx$redrawMaskedRegion(graphics, texture, x1, x2, y1, y2, minU, maxU, minV, maxV,
                    8, 25, BLAST_FURNACE_TEMPERATURE_MASK);
            blxckoutfx$redrawMaskedRegion(graphics, texture, x1, x2, y1, y2, minU, maxU, minV, maxV,
                    56, 64, BLAST_FURNACE_LEFT_ARROW_MASK);
            blxckoutfx$redrawMaskedRegion(graphics, texture, x1, x2, y1, y2, minU, maxU, minV, maxV,
                    109, 63, BLAST_FURNACE_RIGHT_ARROW_MASK);
            blxckoutfx$redrawMaskedRegion(graphics, texture, x1, x2, y1, y2, minU, maxU, minV, maxV,
                    67, 52, BLAST_FURNACE_VESSEL_MASK);
        } else if (blxckoutfx$isNutritionScreen(Minecraft.getInstance().screen) && blxckoutfx$isTexture(texture, TFC, PLAYER_NUTRITION)) {
            for (int index = 0; index < NUTRITION_ICON_MASKS.length; index++) {
                blxckoutfx$redrawMaskedRegion(graphics, texture, x1, x2, y1, y2, minU, maxU, minV, maxV,
                        64, 16 + index * 11, NUTRITION_ICON_MASKS[index]);
            }
        } else if (blxckoutfx$isSmallInventoryBackground(texture, minU, maxU, minV, maxV)) {
            int[][] pixels = {{105, 59}, {106, 59}, {107, 59}, {105, 60}, {106, 60}, {105, 61}, {104, 62}, {70, 69}, {103, 72}};
            for (int[] pixel : pixels) {
                blxckoutfx$redrawDarkenedPixel(graphics, texture, x1, x2, y1, y2, minU, maxU, minV, maxV, pixel[0], pixel[1]);
            }
        }

        String[] actionMask = blxckoutfx$getActionMask(texture, minU, maxU, minV, maxV);
        if (actionMask != null) {
            blxckoutfx$redrawMaskedIcon(graphics, texture, x1, x2, y1, y2, minU, minV, actionMask);
        }

        String[] planQuestionMask = blxckoutfx$getPlanQuestionMask(texture, minU, maxU, minV, maxV);
        if (planQuestionMask != null) {
            blxckoutfx$redrawMaskedIcon(graphics, texture, x1, x2, y1, y2, minU, minV, planQuestionMask);
        }

        String[] jeiKnappingArrowMask = blxckoutfx$getJeiKnappingArrowMask(texture, minU, maxU, minV, maxV);
        if (jeiKnappingArrowMask != null) {
            blxckoutfx$redrawMaskedIcon(graphics, texture, x1, x2, y1, y2, minU, minV, jeiKnappingArrowMask);
        }

        String[] sewingIconMask = blxckoutfx$getSewingIconMask(texture, minU, maxU, minV, maxV);
        if (sewingIconMask != null) {
            blxckoutfx$redrawMaskedIcon(graphics, texture, x1, x2, y1, y2, minU, minV, sewingIconMask);
        }

        String[] sealIconMask = blxckoutfx$getSealIconMask(texture, minU, maxU, minV, maxV);
        if (sealIconMask != null) {
            blxckoutfx$redrawMaskedIcon(graphics, texture, x1, x2, y1, y2, minU, minV, sealIconMask);
        }

        if (blxckoutfx$isTfcQuestionIcon(texture, minU, maxU, minV, maxV)
                || blxckoutfx$isStovetopPotQuestionIcon(texture, minU, maxU, minV, maxV)) {
            blxckoutfx$redrawMaskedIcon(graphics, texture, x1, x2, y1, y2, minU, minV, BARREL_QUESTION_MASK);
        }
    }
    public static void blxckoutfx$redrawRegion(GuiGraphics graphics, ResourceLocation texture, int x1, int x2, int y1, int y2,
                                                  float minU, float maxU, float minV, float maxV,
                                                  int sourceX, int sourceY, int width, int height) {
        if (!blxckoutfx$covers(minU, maxU, sourceX, width) || !blxckoutfx$covers(minV, maxV, sourceY, height)) {
            return;
        }
        int targetX1 = blxckoutfx$map(x1, x2, minU, maxU, sourceX);
        int targetX2 = blxckoutfx$map(x1, x2, minU, maxU, sourceX + width);
        int targetY1 = blxckoutfx$map(y1, y2, minV, maxV, sourceY);
        int targetY2 = blxckoutfx$map(y1, y2, minV, maxV, sourceY + height);
        if (targetX1 >= targetX2 || targetY1 >= targetY2) {
            return;
        }
        blxckoutfx$redrawDepth++;
        try {
            graphics.blit(texture, targetX1, targetY1, targetX2 - targetX1, targetY2 - targetY1,
                    sourceX, sourceY, width, height, TEXTURE_SIZE, TEXTURE_SIZE);
        } finally {
            blxckoutfx$redrawDepth--;
        }
    }
    public static void blxckoutfx$redrawMaskedRegion(GuiGraphics graphics, ResourceLocation texture, int x1, int x2, int y1, int y2,
                                                        float minU, float maxU, float minV, float maxV,
                                                        int sourceX, int sourceY, String[] mask) {
        int width = mask[0].length();
        int height = mask.length;
        if (!blxckoutfx$covers(minU, maxU, sourceX, width) || !blxckoutfx$covers(minV, maxV, sourceY, height)) {
            return;
        }
        int targetX1 = blxckoutfx$map(x1, x2, minU, maxU, sourceX);
        int targetX2 = blxckoutfx$map(x1, x2, minU, maxU, sourceX + width);
        int targetY1 = blxckoutfx$map(y1, y2, minV, maxV, sourceY);
        int targetY2 = blxckoutfx$map(y1, y2, minV, maxV, sourceY + height);
        if (targetX1 < targetX2 && targetY1 < targetY2) {
            blxckoutfx$redrawMaskedIcon(graphics, texture, targetX1, targetX2, targetY1, targetY2,
                    sourceX / (float) TEXTURE_SIZE, sourceY / (float) TEXTURE_SIZE, mask);
        }
    }
    public static void blxckoutfx$redrawDarkenedPixel(GuiGraphics graphics, ResourceLocation texture, int x1, int x2, int y1, int y2,
                                                         float minU, float maxU, float minV, float maxV, int sourceX, int sourceY) {
        int targetX1 = blxckoutfx$map(x1, x2, minU, maxU, sourceX);
        int targetX2 = blxckoutfx$map(x1, x2, minU, maxU, sourceX + 1);
        int targetY1 = blxckoutfx$map(y1, y2, minV, maxV, sourceY);
        int targetY2 = blxckoutfx$map(y1, y2, minV, maxV, sourceY + 1);
        if (targetX1 < targetX2 && targetY1 < targetY2) {
            // Do not enter redrawDepth: this neutral panel pixel must receive normal darkening.
            graphics.blit(texture, targetX1, targetY1, targetX2 - targetX1, targetY2 - targetY1,
                    69, 69, 1, 1, TEXTURE_SIZE, TEXTURE_SIZE);
        }
    }
    public static boolean blxckoutfx$isDirectIndicator(ResourceLocation texture, float minU, float maxU, float minV, float maxV) {
        int x = Math.round(minU * TEXTURE_SIZE);
        int y = Math.round(minV * TEXTURE_SIZE);
        int width = Math.round((maxU - minU) * TEXTURE_SIZE);
        int height = Math.round((maxV - minV) * TEXTURE_SIZE);
        if (!blxckoutfx$isFirepitScreen(Minecraft.getInstance().screen)
                && !blxckoutfx$isPotScreen(Minecraft.getInstance().screen)
                && blxckoutfx$isFirepitTexture(texture) && x == 176 && y == 0 && width == 15 && height == 5) {
            return true;
        }
        if (blxckoutfx$isTexture(texture, TFC, FIREPIT_POT) && x == 193 && y >= 17 && y <= 37 && width == 11 && height <= 20) {
            return true;
        }
        if (blxckoutfx$isStovetopPotScreen(Minecraft.getInstance().screen)
                && blxckoutfx$isTexture(texture, FIRMALIFE, STOVETOP_POT)
                && x == 193 && y >= 17 && y <= 37 && width == 11 && height <= 20) {
            return true;
        }
        if (blxckoutfx$isBlastFurnaceScreen(Minecraft.getInstance().screen) && blxckoutfx$isTexture(texture, TFC, BLAST_FURNACE)) {
            if (x == 176 && y == 0 && width == 15 && height == 5) {
                return true;
            }
            return y == 1 && width == 10 && height >= 1 && height <= 64
                    && (x == 206 || x == 216 || x == 226);
        }
        if (blxckoutfx$isCalendarScreen(Minecraft.getInstance().screen)
                && blxckoutfx$isTexture(texture, TFC, PLAYER_CALENDAR)
                && x == 176 && y == 0 && width == 16 && height == 16) {
            return true;
        }
        if (TFC.equals(texture.getNamespace()) && texture.getPath().startsWith("textures/gui/knapping/") && width == 16 && height == 16) {
            return true;
        }
        return blxckoutfx$isAnvilIndicator(texture, x, y, width, height);
    }
    public static boolean blxckoutfx$isCrucibleBase(ResourceLocation texture, float minU, float maxU, float minV, float maxV) {
        return blxckoutfx$isCrucibleScreen(Minecraft.getInstance().screen)
                && blxckoutfx$isTexture(texture, TFC, CRUCIBLE)
                && blxckoutfx$covers(minU, maxU, CRUCIBLE_GAUGE_X, CRUCIBLE_GAUGE_WIDTH)
                && blxckoutfx$covers(minV, maxV, CRUCIBLE_GAUGE_Y, CRUCIBLE_GAUGE_HEIGHT)
                && blxckoutfx$covers(minU, maxU, CRUCIBLE_VESSEL_X, CRUCIBLE_VESSEL_WIDTH)
                && blxckoutfx$covers(minV, maxV, CRUCIBLE_VESSEL_Y, CRUCIBLE_VESSEL_HEIGHT);
    }
    public static boolean blxckoutfx$isCrucibleTemperatureIndicator(ResourceLocation texture, float minU, float maxU, float minV, float maxV) {
        if (!blxckoutfx$isCrucibleScreen(Minecraft.getInstance().screen) || !blxckoutfx$isTexture(texture, TFC, CRUCIBLE)) {
            return false;
        }
        return Math.round(minU * TEXTURE_SIZE) == 176
                && Math.round(minV * TEXTURE_SIZE) == 0
                && Math.round((maxU - minU) * TEXTURE_SIZE) == 15
                && Math.round((maxV - minV) * TEXTURE_SIZE) == 5;
    }
    public static boolean blxckoutfx$isFirepitBase(ResourceLocation texture, float minU, float maxU, float minV, float maxV) {
        return blxckoutfx$isFirepitScreen(Minecraft.getInstance().screen)
                && blxckoutfx$isTexture(texture, TFC, FIREPIT)
                && blxckoutfx$covers(minU, maxU, FIREPIT_GAUGE_X, FIREPIT_GAUGE_WIDTH)
                && blxckoutfx$covers(minV, maxV, FIREPIT_GAUGE_Y, FIREPIT_GAUGE_HEIGHT);
    }
    public static boolean blxckoutfx$isGrillBase(ResourceLocation texture, float minU, float maxU, float minV, float maxV) {
        return blxckoutfx$isGrillScreen(Minecraft.getInstance().screen)
                && blxckoutfx$isTexture(texture, TFC, FIREPIT_GRILL)
                && blxckoutfx$covers(minU, maxU, FIREPIT_GAUGE_X, FIREPIT_GAUGE_WIDTH)
                && blxckoutfx$covers(minV, maxV, FIREPIT_GAUGE_Y, FIREPIT_GAUGE_HEIGHT)
                && blxckoutfx$covers(minU, maxU, 59, GRILL_SYMBOL_MASK[0].length())
                && blxckoutfx$covers(minV, maxV, 48, GRILL_SYMBOL_MASK.length);
    }
    public static boolean blxckoutfx$isBlastFurnaceBase(ResourceLocation texture, float minU, float maxU, float minV, float maxV) {
        return blxckoutfx$isBlastFurnaceScreen(Minecraft.getInstance().screen)
                && blxckoutfx$isTexture(texture, TFC, BLAST_FURNACE)
                && blxckoutfx$covers(minU, maxU, 8, BLAST_FURNACE_TEMPERATURE_MASK[0].length())
                && blxckoutfx$covers(minV, maxV, 25, BLAST_FURNACE_TEMPERATURE_MASK.length)
                && blxckoutfx$covers(minU, maxU, 56, 64)
                && blxckoutfx$covers(minV, maxV, 52, 35);
    }
    public static boolean blxckoutfx$isFirepitTemperatureIndicator(ResourceLocation texture, float minU, float maxU, float minV, float maxV) {
        return blxckoutfx$isFirepitScreen(Minecraft.getInstance().screen)
                && blxckoutfx$isTexture(texture, TFC, FIREPIT)
                && Math.round(minU * TEXTURE_SIZE) == 176
                && Math.round(minV * TEXTURE_SIZE) == 0
                && Math.round((maxU - minU) * TEXTURE_SIZE) == 15
                && Math.round((maxV - minV) * TEXTURE_SIZE) == 5;
    }
    public static boolean blxckoutfx$isPotTemperatureBase(ResourceLocation texture, float minU, float maxU, float minV, float maxV) {
        return blxckoutfx$isPotScreen(Minecraft.getInstance().screen)
                && blxckoutfx$isTexture(texture, TFC, FIREPIT_POT)
                && blxckoutfx$covers(minU, maxU, FIREPIT_GAUGE_X, FIREPIT_GAUGE_WIDTH)
                && blxckoutfx$covers(minV, maxV, FIREPIT_GAUGE_Y, FIREPIT_GAUGE_HEIGHT)
                && blxckoutfx$covers(minU, maxU, POT_VISUAL_X, POT_VISUAL_WIDTH)
                && blxckoutfx$covers(minV, maxV, POT_VISUAL_Y, POT_VISUAL_HEIGHT);
    }
    public static boolean blxckoutfx$isStovetopPotBase(ResourceLocation texture, float minU, float maxU, float minV, float maxV) {
        return blxckoutfx$isStovetopPotScreen(Minecraft.getInstance().screen)
                && blxckoutfx$isTexture(texture, FIRMALIFE, STOVETOP_POT)
                && blxckoutfx$covers(minU, maxU, FIREPIT_GAUGE_X, FIREPIT_GAUGE_WIDTH)
                && blxckoutfx$covers(minV, maxV, FIREPIT_GAUGE_Y, FIREPIT_GAUGE_HEIGHT)
                && blxckoutfx$covers(minU, maxU, POT_VISUAL_X, POT_VISUAL_WIDTH)
                && blxckoutfx$covers(minV, maxV, POT_VISUAL_Y, POT_VISUAL_HEIGHT);
    }
    public static boolean blxckoutfx$isStovetopGrillBase(ResourceLocation texture, float minU, float maxU, float minV, float maxV) {
        return blxckoutfx$isStovetopGrillScreen(Minecraft.getInstance().screen)
                && blxckoutfx$isTexture(texture, FIRMALIFE, STOVETOP_GRILL)
                && blxckoutfx$covers(minU, maxU, FIREPIT_GAUGE_X, FIREPIT_GAUGE_WIDTH)
                && blxckoutfx$covers(minV, maxV, FIREPIT_GAUGE_Y, FIREPIT_GAUGE_HEIGHT)
                && blxckoutfx$covers(minU, maxU, 59, GRILL_SYMBOL_MASK[0].length())
                && blxckoutfx$covers(minV, maxV, 48, GRILL_SYMBOL_MASK.length);
    }
    public static boolean blxckoutfx$isPotTemperatureIndicator(ResourceLocation texture, float minU, float maxU, float minV, float maxV) {
        return blxckoutfx$isPotScreen(Minecraft.getInstance().screen)
                && blxckoutfx$isTexture(texture, TFC, FIREPIT_POT)
                && Math.round(minU * TEXTURE_SIZE) == 176
                && Math.round(minV * TEXTURE_SIZE) == 0
                && Math.round((maxU - minU) * TEXTURE_SIZE) == 15
                && Math.round((maxV - minV) * TEXTURE_SIZE) == 5;
    }
    public static boolean blxckoutfx$isFirepitTexture(ResourceLocation texture) {
        return texture != null && ((TFC.equals(texture.getNamespace()) && texture.getPath().startsWith("textures/gui/fire_pit"))
                || blxckoutfx$isTexture(texture, FIRMALIFE, STOVETOP_POT)
                || blxckoutfx$isTexture(texture, FIRMALIFE, STOVETOP_GRILL));
    }
    public static boolean blxckoutfx$isSealIcon(ResourceLocation texture, int x, int y, int width, int height) {
        boolean sealTexture = blxckoutfx$isTexture(texture, TFC, BARREL)
                || blxckoutfx$isTexture(texture, TFC, POWDERKEG)
                || blxckoutfx$isTexture(texture, TFC, LARGE_VESSEL);
        if (sealTexture && x == 236 && (y == 0 || y == 20) && width == 20 && height == 20) {
            return true;
        }
        return false;
    }
    public static String[] blxckoutfx$getSealIconMask(ResourceLocation texture, float minU, float maxU, float minV, float maxV) {
        if (!(blxckoutfx$isTexture(texture, TFC, BARREL)
                || blxckoutfx$isTexture(texture, TFC, POWDERKEG)
                || blxckoutfx$isTexture(texture, TFC, LARGE_VESSEL))
                || Math.round(minU * TEXTURE_SIZE) != 236 || Math.round((maxU - minU) * TEXTURE_SIZE) != 20
                || Math.round((maxV - minV) * TEXTURE_SIZE) != 20) {
            return null;
        }
        int sourceY = Math.round(minV * TEXTURE_SIZE);
        return sourceY == 0 ? BARREL_SEAL_ICON_MASK : sourceY == 20 ? BARREL_UNSEAL_ICON_MASK : null;
    }
    public static boolean blxckoutfx$isTfcQuestionIcon(ResourceLocation texture, float minU, float maxU, float minV, float maxV) {
        if (texture == null || !TFC.equals(texture.getNamespace())
                || Math.round(minV * TEXTURE_SIZE) != 0
                || Math.round((maxU - minU) * TEXTURE_SIZE) != 9
                || Math.round((maxV - minV) * TEXTURE_SIZE) != 14) {
            return false;
        }
        int sourceX = Math.round(minU * TEXTURE_SIZE);
        return sourceX == 227 || sourceX == 247;
    }
    public static boolean blxckoutfx$isStovetopPotQuestionIcon(ResourceLocation texture, float minU, float maxU, float minV, float maxV) {
        return blxckoutfx$isStovetopPotScreen(Minecraft.getInstance().screen)
                && blxckoutfx$isTexture(texture, FIRMALIFE, STOVETOP_POT)
                && Math.round(minU * TEXTURE_SIZE) == 247
                && Math.round(minV * TEXTURE_SIZE) == 0
                && Math.round((maxU - minU) * TEXTURE_SIZE) == 9
                && Math.round((maxV - minV) * TEXTURE_SIZE) == 14;
    }
    public static boolean blxckoutfx$isSmallInventoryBackground(ResourceLocation texture, float minU, float maxU, float minV, float maxV) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft == null || minecraft.screen == null || !blxckoutfx$isTexture(texture, TFC, "textures/gui/small_inventory.png")) {
            return false;
        }
        String screen = minecraft.screen.getClass().getName();
        return (screen.equals("net.dries007.tfc.client.screen.NestBoxScreen")
                || screen.equals("net.dries007.tfc.client.screen.SmallVesselInventoryScreen"))
                && blxckoutfx$covers(minU, maxU, 0, 176)
                && blxckoutfx$covers(minV, maxV, 0, 166);
    }
    public static String[] blxckoutfx$getActionMask(ResourceLocation texture, float minU, float maxU, float minV, float maxV) {
        if (!blxckoutfx$isAnvilScreen(Minecraft.getInstance().screen) || !blxckoutfx$isTexture(texture, TFC, ANVIL)) {
            return null;
        }
        int sourceX = Math.round(minU * TEXTURE_SIZE);
        int sourceY = Math.round(minV * TEXTURE_SIZE);
        int width = Math.round((maxU - minU) * TEXTURE_SIZE);
        int height = Math.round((maxV - minV) * TEXTURE_SIZE);
        return sourceY == 240 && width == 16 && height == 16 && sourceX >= 0 && sourceX <= 112 && sourceX % 16 == 0
                ? ACTION_MASKS[sourceX / 16]
                : null;
    }
    public static String[] blxckoutfx$getPlanQuestionMask(ResourceLocation texture, float minU, float maxU, float minV, float maxV) {
        if (!blxckoutfx$isAnvilScreen(Minecraft.getInstance().screen) || !blxckoutfx$isTexture(texture, TFC, ANVIL)) {
            return null;
        }
        int sourceX = Math.round(minU * TEXTURE_SIZE);
        int sourceY = Math.round(minV * TEXTURE_SIZE);
        int width = Math.round((maxU - minU) * TEXTURE_SIZE);
        int height = Math.round((maxV - minV) * TEXTURE_SIZE);
        return sourceX == 0 && sourceY == 207 && width == 9 && height == 14 ? PLAN_QUESTION_MASK : null;
    }
    public static String[] blxckoutfx$getJeiKnappingArrowMask(ResourceLocation texture, float minU, float maxU, float minV, float maxV) {
        if (!BlxckoutFXRenderContext.isRenderingTfcJeiKnappingArrow()
                || !blxckoutfx$isTexture(texture, TFC, "textures/gui/jei/icons.png")) {
            return null;
        }
        int sourceX = Math.round(minU * TEXTURE_SIZE);
        int sourceY = Math.round(minV * TEXTURE_SIZE);
        int width = Math.round((maxU - minU) * TEXTURE_SIZE);
        int height = Math.round((maxV - minV) * TEXTURE_SIZE);
        return (sourceX == 0 || sourceX == 22) && sourceY == 14 && width == 22 && height == 16
                ? JEI_KNAPPING_ARROW_MASK
                : null;
    }
    public static String[] blxckoutfx$getSewingIconMask(ResourceLocation texture, float minU, float maxU, float minV, float maxV) {
        if (!blxckoutfx$isSewingTableScreen(Minecraft.getInstance().screen) || !blxckoutfx$isTexture(texture, TFC, SEWING)) {
            return null;
        }
        int sourceX = Math.round(minU * TEXTURE_SIZE);
        int sourceY = Math.round(minV * TEXTURE_SIZE);
        int width = Math.round((maxU - minU) * TEXTURE_SIZE);
        int height = Math.round((maxV - minV) * TEXTURE_SIZE);
        if (sourceX == 236 && width == 20 && height == 20) {
            return switch (sourceY) {
                case 0, 20, 40, 60 -> SEWING_CLOTH_ICON_MASK;
                case 80, 100 -> SEWING_REMOVE_STITCH_ICON_MASK;
                case 120, 140 -> SEWING_STITCH_ICON_MASK;
                default -> null;
            };
        }
        return sourceX == 192 && width == 20 && height == 18 && (sourceY == 96 || sourceY == 114)
                ? SEWING_RECIPE_BOOK_ICON_MASK
                : null;
    }
    public static boolean blxckoutfx$isAnvilIndicator(ResourceLocation texture, int sourceX, int sourceY, int width, int height) {
        if (!blxckoutfx$isAnvilScreen(Minecraft.getInstance().screen) || !blxckoutfx$isTexture(texture, TFC, ANVIL)) {
            return false;
        }
        if (width == 16 && height == 16 && sourceX == 236 && (sourceY == 0 || sourceY == 16)) {
            return true;
        }
        if (sourceX == 198 && width == 20 && height == 22) {
            return true;
        }
        return (sourceX == 176 && sourceY == 0 && width == 5 && height == 5)
                || (sourceX == 181 && sourceY == 0 && width == 5 && height == 5)
                || (sourceX == 176 && sourceY == 7 && width == 5 && height == 7)
                || (sourceX == 186 && sourceY == 7 && width == 5 && height == 7)
                || (sourceX == 192 && sourceY == 5 && width == 1 && height == 5)
                || (sourceX == 181 && sourceY == 5 && width == 5 && height == 5);
    }
    public static void blxckoutfx$redrawMaskedIcon(GuiGraphics graphics, ResourceLocation texture, int x1, int x2, int y1, int y2,
                                                      float minU, float minV, String[] mask) {
        int sourceX = Math.round(minU * TEXTURE_SIZE);
        int sourceY = Math.round(minV * TEXTURE_SIZE);
        int maskWidth = mask[0].length();
        int maskHeight = mask.length;
        blxckoutfx$redrawDepth++;
        try {
            for (int row = 0; row < mask.length; row++) {
                String maskRow = mask[row];
                int runStart = maskRow.indexOf('#');
                while (runStart >= 0) {
                    int runEnd = maskRow.indexOf('.', runStart);
                    if (runEnd < 0) {
                        runEnd = maskRow.length();
                    }
                    int drawX1 = Math.round(x1 + (x2 - x1) * runStart / (float) maskWidth);
                    int drawX2 = Math.round(x1 + (x2 - x1) * runEnd / (float) maskWidth);
                    int drawY1 = Math.round(y1 + (y2 - y1) * row / (float) maskHeight);
                    int drawY2 = Math.round(y1 + (y2 - y1) * (row + 1) / (float) maskHeight);
                    if (drawX1 < drawX2 && drawY1 < drawY2) {
                        graphics.blit(texture, drawX1, drawY1, drawX2 - drawX1, drawY2 - drawY1,
                                sourceX + runStart, sourceY + row, runEnd - runStart, 1, TEXTURE_SIZE, TEXTURE_SIZE);
                    }
                    runStart = maskRow.indexOf('#', runEnd);
                }
            }
        } finally {
            blxckoutfx$redrawDepth--;
        }
    }
    public static boolean blxckoutfx$isAnvilScreen(Object target) {
        return target != null && "net.dries007.tfc.client.screen.AnvilScreen".equals(target.getClass().getName());
    }
    public static boolean blxckoutfx$isSewingTableScreen(Object target) {
        return target != null && "net.dries007.tfc.client.screen.SewingTableScreen".equals(target.getClass().getName());
    }
    public static boolean blxckoutfx$isScribingTableScreen(Object target) {
        return target != null && "net.dries007.tfc.client.screen.ScribingTableScreen".equals(target.getClass().getName());
    }
    public static boolean blxckoutfx$isCrucibleScreen(Object target) {
        return target != null && "net.dries007.tfc.client.screen.CrucibleScreen".equals(target.getClass().getName());
    }
    public static boolean blxckoutfx$isFirepitScreen(Object target) {
        return target != null && "net.dries007.tfc.client.screen.FirepitScreen".equals(target.getClass().getName());
    }
    public static boolean blxckoutfx$isBlastFurnaceScreen(Object target) {
        return target != null && "net.dries007.tfc.client.screen.BlastFurnaceScreen".equals(target.getClass().getName());
    }
    public static boolean blxckoutfx$isBarrelScreen(Object target) {
        return target != null && "net.dries007.tfc.client.screen.BarrelScreen".equals(target.getClass().getName());
    }
    public static boolean blxckoutfx$isGrillScreen(Object target) {
        return target != null && "net.dries007.tfc.client.screen.GrillScreen".equals(target.getClass().getName());
    }
    public static boolean blxckoutfx$isPotScreen(Object target) {
        return target != null && "net.dries007.tfc.client.screen.PotScreen".equals(target.getClass().getName());
    }
    public static boolean blxckoutfx$isStovetopPotScreen(Object target) {
        return target != null && "com.eerussianguy.firmalife.client.screen.StovetopPotScreen".equals(target.getClass().getName());
    }
    public static boolean blxckoutfx$isStovetopGrillScreen(Object target) {
        return target != null && "com.eerussianguy.firmalife.client.screen.StovetopGrillScreen".equals(target.getClass().getName());
    }
    public static boolean blxckoutfx$isKegScreen(Object target) {
        return target != null && "com.eerussianguy.firmalife.client.screen.KegScreen".equals(target.getClass().getName());
    }
    public static boolean blxckoutfx$isNutritionScreen(Object target) {
        return target != null && "net.dries007.tfc.client.screen.NutritionScreen".equals(target.getClass().getName());
    }
    public static boolean blxckoutfx$isCalendarScreen(Object target) {
        return target != null && "net.dries007.tfc.client.screen.CalendarScreen".equals(target.getClass().getName());
    }
    public static boolean blxckoutfx$isPatchouliPageImage(Object target) {
        return target != null && "vazkii.patchouli.client.book.page.PageImage".equals(target.getClass().getName());
    }
    public static boolean blxckoutfx$isTfcJeiRecipeLayout(Object target) {
        if (target == null || !"mezz.jei.library.gui.recipes.RecipeLayout".equals(target.getClass().getName())) {
            return false;
        }
        try {
            Object category = target.getClass().getMethod("getRecipeCategory").invoke(target);
            if (category == null) {
                return false;
            }
            String name = category.getClass().getName();
            return name.startsWith("net.dries007.tfc.compat.jei.") || name.startsWith("com.eerussianguy.firmalife.compat.jei.");
        } catch (ReflectiveOperationException ignored) {
            return false;
        }
    }
    public static boolean blxckoutfx$isTfcJeiKnappingRecipeLayout(Object target) {
        if (target == null || !"mezz.jei.library.gui.recipes.RecipeLayout".equals(target.getClass().getName())) {
            return false;
        }
        try {
            Object category = target.getClass().getMethod("getRecipeCategory").invoke(target);
            return category != null
                    && "net.dries007.tfc.compat.jei.category.KnappingRecipeCategory".equals(category.getClass().getName());
        } catch (ReflectiveOperationException ignored) {
            return false;
        }
    }
    public static boolean blxckoutfx$isAnvilPlanButton(Object target) {
        return target != null && "net.dries007.tfc.client.screen.button.AnvilPlanButton".equals(target.getClass().getName());
    }
    public static boolean blxckoutfx$isKnappingButton(Object target) {
        return target != null && "net.dries007.tfc.client.screen.button.KnappingButton".equals(target.getClass().getName());
    }
    public static boolean blxckoutfx$isKnappingScreen(Object target) {
        return target != null && "net.dries007.tfc.client.screen.KnappingScreen".equals(target.getClass().getName());
    }
    public static boolean blxckoutfx$isPlayerInventoryTabButton(Object target) {
        return target != null && "net.dries007.tfc.client.screen.button.PlayerInventoryTabButton".equals(target.getClass().getName());
    }
    public static boolean blxckoutfx$isTexture(ResourceLocation texture, String namespace, String path) {
        return texture != null && namespace.equals(texture.getNamespace()) && path.equals(texture.getPath());
    }
    public static boolean blxckoutfx$covers(float min, float max, int start, int length) {
        return min <= start / (float) TEXTURE_SIZE && max >= (start + length) / (float) TEXTURE_SIZE;
    }
    public static int blxckoutfx$map(int start, int end, float minUv, float maxUv, int sourceCoordinate) {
        return Math.round(start + (end - start) * ((sourceCoordinate / (float) TEXTURE_SIZE - minUv) / (maxUv - minUv)));
    }

}