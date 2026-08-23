package com.lopew.blxckoutfx.mixin.tfc;

import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Locale;

@Pseudo
@Mixin(
        value = GuiGraphics.class,
        targets = {
                "net.dries007.tfc.client.screen.AnvilScreen",
                "net.dries007.tfc.client.screen.BarrelScreen",
                "net.dries007.tfc.client.screen.BlastFurnaceScreen",
                "net.dries007.tfc.client.screen.CrucibleScreen",
                "net.dries007.tfc.client.screen.LogPileScreen",
                "net.dries007.tfc.client.screen.NestBoxScreen",
                "net.dries007.tfc.client.screen.SmallVesselInventoryScreen",
                "net.dries007.tfc.client.screen.button.AnvilPlanButton",
                "net.dries007.tfc.client.screen.button.BarrelSealButton",
                "net.dries007.tfc.client.screen.button.PowderkegSealButton",
                "net.dries007.tfc.client.screen.button.VesselSealButton",
                "com.eerussianguy.firmalife.client.screen.BeehiveScreen",
                "com.eerussianguy.firmalife.client.screen.BigBarrelScreen",
                "com.eerussianguy.firmalife.client.screen.StovetopGrillScreen",
                "com.eerussianguy.firmalife.client.screen.StovetopPotScreen",
                "net.dries007.tfc.compat.jei.category.KnappingRecipeCategory",
                "net.dries007.tfc.compat.patchouli.component.KnappingComponent"
        }
)
public class TfcGuiGraphicsMixin {
    @Unique
    private static final String BLXCKOUTFX_TFC_NAMESPACE = "tfc";
    @Unique
    private static final String BLXCKOUTFX_SCRIBING_TABLE_TEXTURE = "textures/gui/scribing_table.png";
    @Unique
    private static final String BLXCKOUTFX_SEWING_TABLE_TEXTURE = "textures/gui/sewing.png";
    @Unique
    private static final String BLXCKOUTFX_FIREPIT_TEXTURE_PREFIX = "textures/gui/fire_pit";
    @Unique
    private static final String BLXCKOUTFX_FIREPIT_POT_TEXTURE = "textures/gui/fire_pit_cooking_pot.png";
    @Unique
    private static final String BLXCKOUTFX_FIREPIT_GRILL_TEXTURE = "textures/gui/fire_pit_grill.png";
    private static final String BLXCKOUTFX_CRUCIBLE_TEXTURE = "textures/gui/crucible.png";
    @Unique
    private static final String BLXCKOUTFX_BLAST_FURNACE_TEXTURE = "textures/gui/blast_furnace.png";
    @Unique
    private static final String BLXCKOUTFX_BARREL_TEXTURE = "textures/gui/barrel.png";
    @Unique
    private static final String BLXCKOUTFX_POWDERKEG_TEXTURE = "textures/gui/powderkeg.png";
    @Unique
    private static final String BLXCKOUTFX_LARGE_VESSEL_TEXTURE = "textures/gui/large_vessel.png";
    @Unique
    private static final String BLXCKOUTFX_SMALL_INVENTORY_TEXTURE = "textures/gui/small_inventory.png";
    @Unique
    private static final String BLXCKOUTFX_FIRMALIFE_NAMESPACE = "firmalife";
    @Unique
    private static final String BLXCKOUTFX_SNS_NAMESPACE = "sns";
    @Unique
    private static final String BLXCKOUTFX_BEEHIVE_TEXTURE = "textures/gui/beehive.png";
    @Unique
    private static final String BLXCKOUTFX_SNS_CONTAINER_TEXTURE = "textures/gui/container.png";
    @Unique
    private static final String BLXCKOUTFX_STOVETOP_GRILL_TEXTURE = "textures/gui/stovetop_grill.png";
    @Unique
    private static final String BLXCKOUTFX_STOVETOP_POT_TEXTURE = "textures/gui/stovetop_pot.png";
    @Unique
    private static final String BLXCKOUTFX_ANVIL_TEXTURE = "textures/gui/anvil.png";
    @Unique
    private static final String BLXCKOUTFX_PERFECTED_ANVIL_HELPER_PACK_ID = "perfected anvil helper";
    @Unique
    private static final int BLXCKOUTFX_GUI_TEXTURE_SIZE = 256;
    @Unique
    private static final int BLXCKOUTFX_LOG_PILE_NEUTRAL_PIXEL_X = 69;
    @Unique
    private static final int BLXCKOUTFX_LOG_PILE_NEUTRAL_PIXEL_Y = 69;
    @Unique
    private static final int BLXCKOUTFX_BEEHIVE_HONEY_X = 43;
    @Unique
    private static final int BLXCKOUTFX_BEEHIVE_HONEY_Y = 40;
    @Unique
    private static final int BLXCKOUTFX_BEEHIVE_HONEY_WIDTH = 72;
    @Unique
    private static final int BLXCKOUTFX_BEEHIVE_HONEY_HEIGHT = 26;
    @Unique
    private static final int BLXCKOUTFX_STOVETOP_POT_X = 54;
    @Unique
    private static final int BLXCKOUTFX_STOVETOP_POT_Y = 48;
    @Unique
    private static final int BLXCKOUTFX_STOVETOP_POT_WIDTH = 32;
    @Unique
    private static final int BLXCKOUTFX_STOVETOP_POT_HEIGHT = 26;
    @Unique
    private static final int[][] BLXCKOUTFX_LOG_PILE_BACKGROUND_ARTIFACT_PIXELS = {
            {105, 59}, {106, 59}, {107, 59}, {105, 60}, {106, 60},
            {105, 61}, {104, 62}, {70, 69}, {103, 72}
    };
    @Unique
    private static final int BLXCKOUTFX_SCRIBING_LOGO_X = 18;
    @Unique
    private static final int BLXCKOUTFX_SCRIBING_LOGO_Y = 3;
    @Unique
    private static final int BLXCKOUTFX_SCRIBING_LOGO_WIDTH = 34;
    @Unique
    private static final int BLXCKOUTFX_SCRIBING_LOGO_HEIGHT = 40;
    @Unique
    private static final int BLXCKOUTFX_SEWING_WORKSPACE_X = 10;
    @Unique
    private static final int BLXCKOUTFX_SEWING_WORKSPACE_Y = 16;
    @Unique
    private static final int BLXCKOUTFX_SEWING_WORKSPACE_WIDTH = 107;
    @Unique
    private static final int BLXCKOUTFX_SEWING_WORKSPACE_HEIGHT = 59;
    @Unique
    private static final int BLXCKOUTFX_FIREPIT_TEMPERATURE_X = 30;
    @Unique
    private static final int BLXCKOUTFX_FIREPIT_TEMPERATURE_Y = 24;
    @Unique
    private static final int BLXCKOUTFX_FIREPIT_TEMPERATURE_WIDTH = 15;
    @Unique
    private static final int BLXCKOUTFX_FIREPIT_TEMPERATURE_HEIGHT = 54;
    @Unique
    private static final int BLXCKOUTFX_FIREPIT_POT_X = 121;
    @Unique
    private static final int BLXCKOUTFX_FIREPIT_POT_Y = 10;
    @Unique
    private static final int BLXCKOUTFX_FIREPIT_POT_WIDTH = 43;
    @Unique
    private static final int BLXCKOUTFX_FIREPIT_POT_HEIGHT = 48;
    @Unique
    private static final int BLXCKOUTFX_FIREPIT_GRILL_X = 59;
    @Unique
    private static final int BLXCKOUTFX_FIREPIT_GRILL_Y = 48;
    @Unique
    private static final int BLXCKOUTFX_FIREPIT_GRILL_WIDTH = 26;
    @Unique
    private static final int BLXCKOUTFX_FIREPIT_GRILL_HEIGHT = 30;
    @Unique
    private static final int BLXCKOUTFX_CRUCIBLE_TEMPERATURE_X = 7;
    @Unique
    private static final int BLXCKOUTFX_CRUCIBLE_TEMPERATURE_Y = 80;
    @Unique
    private static final int BLXCKOUTFX_CRUCIBLE_TEMPERATURE_WIDTH = 15;
    @Unique
    private static final int BLXCKOUTFX_CRUCIBLE_TEMPERATURE_HEIGHT = 52;
    @Unique
    private static final int BLXCKOUTFX_CRUCIBLE_VESSEL_X = 94;
    @Unique
    private static final int BLXCKOUTFX_CRUCIBLE_VESSEL_Y = 92;
    @Unique
    private static final int BLXCKOUTFX_CRUCIBLE_VESSEL_WIDTH = 42;
    @Unique
    private static final int BLXCKOUTFX_CRUCIBLE_VESSEL_HEIGHT = 40;
    @Unique
    private static final int BLXCKOUTFX_BLAST_FURNACE_TEMPERATURE_X = 8;
    @Unique
    private static final int BLXCKOUTFX_BLAST_FURNACE_TEMPERATURE_Y = 25;
    @Unique
    private static final int BLXCKOUTFX_BLAST_FURNACE_TEMPERATURE_WIDTH = 15;
    @Unique
    private static final int BLXCKOUTFX_BLAST_FURNACE_TEMPERATURE_HEIGHT = 51;
    @Unique
    private static final int BLXCKOUTFX_BLAST_FURNACE_INPUT_METER_X = 42;
    @Unique
    private static final int BLXCKOUTFX_BLAST_FURNACE_FUEL_METER_X = 124;
    @Unique
    private static final int BLXCKOUTFX_BLAST_FURNACE_METER_Y = 22;
    @Unique
    private static final int BLXCKOUTFX_BLAST_FURNACE_METER_WIDTH = 10;
    @Unique
    private static final int BLXCKOUTFX_BLAST_FURNACE_METER_HEIGHT = 66;
    @Unique
    // Includes the two directional arrows that frame the vessel.
    private static final int BLXCKOUTFX_BLAST_FURNACE_VESSEL_X = 56;
    @Unique
    private static final int BLXCKOUTFX_BLAST_FURNACE_VESSEL_Y = 52;
    @Unique
    private static final int BLXCKOUTFX_BLAST_FURNACE_VESSEL_WIDTH = 64;
    @Unique
    private static final int BLXCKOUTFX_BLAST_FURNACE_VESSEL_HEIGHT = 35;
    @Unique
    private static final int BLXCKOUTFX_ANVIL_VISUAL_X = 54;
    @Unique
    private static final int BLXCKOUTFX_ANVIL_VISUAL_Y = 8;
    @Unique
    private static final int BLXCKOUTFX_ANVIL_VISUAL_WIDTH = 68;
    @Unique
    private static final int BLXCKOUTFX_ANVIL_VISUAL_HEIGHT = 39;
    @Unique
    private static final int BLXCKOUTFX_ANVIL_FORGING_GRID_X = 52;
    @Unique
    private static final int BLXCKOUTFX_ANVIL_FORGING_GRID_Y = 50;
    @Unique
    private static final int BLXCKOUTFX_ANVIL_FORGING_GRID_WIDTH = 67;
    @Unique
    private static final int BLXCKOUTFX_ANVIL_FORGING_GRID_HEIGHT = 34;
    @Unique
    private static int blxckoutfx$sewingWorkspaceX1;
    @Unique
    private static int blxckoutfx$sewingWorkspaceY1;
    @Unique
    private static int blxckoutfx$sewingWorkspaceX2;
    @Unique
    private static int blxckoutfx$sewingWorkspaceY2;
    @Unique
    // Button backgrounds stay dark; only the icon pixels are redrawn unchanged.
    private static final String[] BLXCKOUTFX_CLOTH_ICON_MASK = {
            "....................",
            "....................",
            "....................",
            "....................",
            ".........###........",
            "........#####.......",
            ".......#######......",
            "......#########.....",
            ".....###########....",
            "...##############...",
            "...##############...",
            "....#############...",
            ".....############...",
            "......######.###....",
            "......#####.........",
            ".......##...........",
            "....................",
            "....................",
            "....................",
            "...................."
    };
    @Unique
    private static final String[] BLXCKOUTFX_REMOVE_STITCH_ICON_MASK = {
            "....................",
            "....................",
            "....................",
            "....#..........#....",
            "...###........###...",
            "....###......###....",
            ".....###....###.....",
            "......###..###......",
            ".......######.......",
            "........####........",
            "........####........",
            ".......######.......",
            "......###..###......",
            ".....###....###.....",
            "....###......###....",
            "...###........###...",
            "...##..........##...",
            "....................",
            "....................",
            "...................."
    };
    @Unique
    private static final String[] BLXCKOUTFX_STITCH_ICON_MASK = {
            "....................",
            "....................",
            "....................",
            "....................",
            "............####....",
            "...........##..#....",
            "...........##..#....",
            "..........#####.....",
            "..........###.......",
            ".........###........",
            "........###.........",
            ".......###..........",
            "......###...........",
            ".....###............",
            "....##..............",
            "....#...............",
            "....................",
            "....................",
            "....................",
            "...................."
    };
    @Unique
    private static final String[] BLXCKOUTFX_RECIPE_BOOK_ICON_MASK = {
            "....................",
            "....................",
            "..........###.......",
            "........######......",
            "......#########.....",
            "....############....",
            "..###############...",
            "..###############...",
            "..###############...",
            "..###############...",
            "..###############...",
            "....############....",
            ".....#########......",
            "......######........",
            ".......###..........",
            "....................",
            "....................",
            "...................."
    };
    @Unique
    private static final String[] BLXCKOUTFX_FIREPIT_RECIPE_QUESTION_MASK = {
            ".........",
            ".........",
            ".........",
            "...###...",
            "..#...#..",
            "..#...#..",
            "......#..",
            "....##...",
            "...#.....",
            "...#.....",
            ".........",
            "...#.....",
            ".........",
            "........."
    };
    @Unique
    private static final String[] BLXCKOUTFX_BARREL_SEAL_MASK = {
            "....................", "....................", "....................", "....................",
            "....................", "....................", "....###########.....", "....############....",
            "....###.....#.##....", ".....###....##.#....", "......##....##......", "......##....##......",
            "......##....##......", "......##....##......", "......########......", ".......#######......",
            "....................", "....................", "....................", "...................."
    };
    @Unique
    private static final String[] BLXCKOUTFX_BARREL_UNSEAL_MASK = {
            "....................", "....................", "............##......", "..........##.##.....",
            "........##.####.....", "......##.##...#.....", "....##.##...........", "....###.............",
            "....###.....#.......", ".....###....##......", "......##....##......", "......##....##......",
            "......##....##......", "......##....##......", "......########......", ".......#######......",
            "....................", "....................", "....................", "...................."
    };
    @Unique
    private static final String[][] BLXCKOUTFX_ANVIL_ACTION_MASKS = {
            {
                    "................", "................", "................", "................",
                    "................", "################", "################", "################",
                    "################", "################", "################", "................",
                    "................", "................", "................", "................"
            },
            {
                    "................", "................", "................", "................",
                    "................", "...###########..", "..############..", "..############..",
                    "..############..", "..############..", "..############..", "..######........",
                    "..######........", "..######........", "..#####.........", "................"
            },
            {
                    "................", "................", "................", "..###...........",
                    ".#######........", ".##############.", ".##############.", ".##############.",
                    ".##############.", ".##############.", ".##############.", ".#######........",
                    "..###...........", "................", "................", "................"
            },
            {
                    "................", "................", "................", "###.............",
                    "#####...........", "################", "################", ".###############",
                    ".###############", "..##############", "...#############", "................",
                    "................", "................", "................", "................"
            },
            {
                    "................", "................", "................", "................",
                    "...#####........", "...#####........", "...#####........", "...############.",
                    "...############.", "...#####........", "...#####........", "...#####........",
                    "...##..##.......", "...######.......", "....####........", ".....##........."
            },
            {
                    "................", "................", "...#####........", "...#####........",
                    "...#####........", "...############.", "...############.", "...#####........",
                    "...#####........", "...#####........", "...##..##.......", "...######.......",
                    "...######.......", "...######.......", "....####........", ".....##........."
            },
            {
                    "...####.........", "..######........", "..######........", "..#############.",
                    "..#############.", "..######........", "..######........", "...####.........",
                    "..##..##........", "..######........", "..######........", "..######........",
                    "..######........", "..######........", "...####.........", "....##.........."
            },
            {
                    "................", "................", "................", "................",
                    "................", "................", "..........######", ".......#########",
                    "################", "################", ".......#########", "..........######",
                    "................", "................", "................", "................"
            }
    };
    @Unique
    private static final String[] BLXCKOUTFX_ANVIL_HIT_RULE_MASK = {
            "................", "................", "................", "................",
            "...#####........", "...#####........", "...#####........", "...############.",
            "...############.", "...#####........", "...#####........", "...#####........",
            "................", "................", "................", "................"
    };
    @Unique
    private static final String[] BLXCKOUTFX_ANVIL_PLAN_ICON_MASK = {
            "..........####..", ".........######.", "........########", ".....#.#########",
            ".....#.#########", "...#############", ".....##########.", "...###########..",
            ".############...", "##########......", "#########.......", "#########.......",
            "########........", ".#######........", "..#####.........", "....##.........."
    };
    @Unique
    private static final String[] BLXCKOUTFX_ANVIL_JEI_QUESTION_MASK = {
            ".........", ".........", ".........", "...###...", "..#...#..", "..#...#..", "......#..",
            "....##...", "...#.....", "...#.....", ".........", "...#.....", ".........", "........."
    };
    @Unique
    private static int blxckoutfx$sewingIconOverlayDepth;
    @Unique
    private boolean blxckoutfx$anvilPlanIconDarkeningSuppressed;
    @Unique
    private boolean blxckoutfx$barrelFluidDarkeningSuppressed;
    @Unique
    private boolean blxckoutfx$beehiveHoneyDarkeningSuppressed;

    @Inject(method = "innerBlit(Lnet/minecraft/resources/ResourceLocation;IIIIIFFFF)V", at = @At("HEAD"), require = 0)
    private void blxckoutfx$preserveScribingLogoColors(
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
        if (blxckoutfx$isPerfectedAnvilHelperActive()
                && blxckoutfx$isPerfectedAnvilHelperActionSprite(texture, minU, maxU, minV, maxV)) {
            BlxckoutFXRenderContext.suppressTextureDarkening();
        }
        if (blxckoutfx$shouldPreserveScribingLogoColors(texture, x1, x2, y1, y2, minU, maxU, minV, maxV)) {
            BlxckoutFXRenderContext.enterTfcScribingLogoRender(
                    blxckoutfx$mapTextureCoordinate(x1, x2, minU, maxU, BLXCKOUTFX_SCRIBING_LOGO_X),
                    blxckoutfx$mapTextureCoordinate(y1, y2, minV, maxV, BLXCKOUTFX_SCRIBING_LOGO_Y),
                    blxckoutfx$mapTextureCoordinate(x1, x2, minU, maxU, BLXCKOUTFX_SCRIBING_LOGO_X + BLXCKOUTFX_SCRIBING_LOGO_WIDTH),
                    blxckoutfx$mapTextureCoordinate(y1, y2, minV, maxV, BLXCKOUTFX_SCRIBING_LOGO_Y + BLXCKOUTFX_SCRIBING_LOGO_HEIGHT)
            );
        }
        if (blxckoutfx$isFirepitDirectIcon(texture, minU, maxU, minV, maxV)) {
            BlxckoutFXRenderContext.suppressTextureDarkening();
        }
        if (blxckoutfx$isBlastFurnaceTemperatureIndicator(texture, minU, maxU, minV, maxV)) {
            BlxckoutFXRenderContext.suppressTextureDarkening();
        }
        if (blxckoutfx$isBlastFurnaceFillMeter(texture, minU, maxU, minV, maxV)) {
            BlxckoutFXRenderContext.suppressTextureDarkening();
        }
        if (blxckoutfx$isAnvilDirectIndicator(texture, minU, maxU, minV, maxV)) {
            BlxckoutFXRenderContext.suppressTextureDarkening();
        }
        if (blxckoutfx$isFirepitTemperatureBase(texture, minU, maxU, minV, maxV)) {
            BlxckoutFXRenderContext.enterTfcFirepitTemperatureRender(
                    blxckoutfx$mapTextureCoordinate(x1, x2, minU, maxU, BLXCKOUTFX_FIREPIT_TEMPERATURE_X),
                    blxckoutfx$mapTextureCoordinate(y1, y2, minV, maxV, BLXCKOUTFX_FIREPIT_TEMPERATURE_Y),
                    blxckoutfx$mapTextureCoordinate(x1, x2, minU, maxU, BLXCKOUTFX_FIREPIT_TEMPERATURE_X + BLXCKOUTFX_FIREPIT_TEMPERATURE_WIDTH),
                    blxckoutfx$mapTextureCoordinate(y1, y2, minV, maxV, BLXCKOUTFX_FIREPIT_TEMPERATURE_Y + BLXCKOUTFX_FIREPIT_TEMPERATURE_HEIGHT)
            );
        }
        if (blxckoutfx$isFirepitPotBase(texture, minU, maxU, minV, maxV)) {
            BlxckoutFXRenderContext.enterTfcFirepitPotRender(
                    blxckoutfx$mapTextureCoordinate(x1, x2, minU, maxU, BLXCKOUTFX_FIREPIT_POT_X),
                    blxckoutfx$mapTextureCoordinate(y1, y2, minV, maxV, BLXCKOUTFX_FIREPIT_POT_Y),
                    blxckoutfx$mapTextureCoordinate(x1, x2, minU, maxU, BLXCKOUTFX_FIREPIT_POT_X + BLXCKOUTFX_FIREPIT_POT_WIDTH),
                    blxckoutfx$mapTextureCoordinate(y1, y2, minV, maxV, BLXCKOUTFX_FIREPIT_POT_Y + BLXCKOUTFX_FIREPIT_POT_HEIGHT)
            );
        }
        if (blxckoutfx$isStovetopPotBase(texture, minU, maxU, minV, maxV)) {
            BlxckoutFXRenderContext.enterTfcFirepitPotRender(
                    blxckoutfx$mapTextureCoordinate(x1, x2, minU, maxU, BLXCKOUTFX_STOVETOP_POT_X),
                    blxckoutfx$mapTextureCoordinate(y1, y2, minV, maxV, BLXCKOUTFX_STOVETOP_POT_Y),
                    blxckoutfx$mapTextureCoordinate(x1, x2, minU, maxU, BLXCKOUTFX_STOVETOP_POT_X + BLXCKOUTFX_STOVETOP_POT_WIDTH),
                    blxckoutfx$mapTextureCoordinate(y1, y2, minV, maxV, BLXCKOUTFX_STOVETOP_POT_Y + BLXCKOUTFX_STOVETOP_POT_HEIGHT)
            );
        }
        if (blxckoutfx$isFirepitGrillBase(texture, minU, maxU, minV, maxV)) {
            BlxckoutFXRenderContext.enterTfcFirepitGrillRender(
                    blxckoutfx$mapTextureCoordinate(x1, x2, minU, maxU, BLXCKOUTFX_FIREPIT_GRILL_X),
                    blxckoutfx$mapTextureCoordinate(y1, y2, minV, maxV, BLXCKOUTFX_FIREPIT_GRILL_Y),
                    blxckoutfx$mapTextureCoordinate(x1, x2, minU, maxU, BLXCKOUTFX_FIREPIT_GRILL_X + BLXCKOUTFX_FIREPIT_GRILL_WIDTH),
                    blxckoutfx$mapTextureCoordinate(y1, y2, minV, maxV, BLXCKOUTFX_FIREPIT_GRILL_Y + BLXCKOUTFX_FIREPIT_GRILL_HEIGHT)
            );
        }
        if (blxckoutfx$isCrucibleBase(texture, minU, maxU, minV, maxV)) {
            BlxckoutFXRenderContext.enterTfcCrucibleTemperatureRender(
                    blxckoutfx$mapTextureCoordinate(x1, x2, minU, maxU, BLXCKOUTFX_CRUCIBLE_TEMPERATURE_X),
                    blxckoutfx$mapTextureCoordinate(y1, y2, minV, maxV, BLXCKOUTFX_CRUCIBLE_TEMPERATURE_Y),
                    blxckoutfx$mapTextureCoordinate(x1, x2, minU, maxU, BLXCKOUTFX_CRUCIBLE_TEMPERATURE_X + BLXCKOUTFX_CRUCIBLE_TEMPERATURE_WIDTH),
                    blxckoutfx$mapTextureCoordinate(y1, y2, minV, maxV, BLXCKOUTFX_CRUCIBLE_TEMPERATURE_Y + BLXCKOUTFX_CRUCIBLE_TEMPERATURE_HEIGHT)
            );
            BlxckoutFXRenderContext.enterTfcCrucibleVesselRender(
                    blxckoutfx$mapTextureCoordinate(x1, x2, minU, maxU, BLXCKOUTFX_CRUCIBLE_VESSEL_X),
                    blxckoutfx$mapTextureCoordinate(y1, y2, minV, maxV, BLXCKOUTFX_CRUCIBLE_VESSEL_Y),
                    blxckoutfx$mapTextureCoordinate(x1, x2, minU, maxU, BLXCKOUTFX_CRUCIBLE_VESSEL_X + BLXCKOUTFX_CRUCIBLE_VESSEL_WIDTH),
                    blxckoutfx$mapTextureCoordinate(y1, y2, minV, maxV, BLXCKOUTFX_CRUCIBLE_VESSEL_Y + BLXCKOUTFX_CRUCIBLE_VESSEL_HEIGHT)
            );
        }
        if (blxckoutfx$isBlastFurnaceBase(texture, minU, maxU, minV, maxV)) {
            BlxckoutFXRenderContext.enterTfcFirepitTemperatureRender(
                    blxckoutfx$mapTextureCoordinate(x1, x2, minU, maxU, BLXCKOUTFX_BLAST_FURNACE_INPUT_METER_X),
                    blxckoutfx$mapTextureCoordinate(y1, y2, minV, maxV, BLXCKOUTFX_BLAST_FURNACE_METER_Y),
                    blxckoutfx$mapTextureCoordinate(x1, x2, minU, maxU, BLXCKOUTFX_BLAST_FURNACE_INPUT_METER_X + BLXCKOUTFX_BLAST_FURNACE_METER_WIDTH),
                    blxckoutfx$mapTextureCoordinate(y1, y2, minV, maxV, BLXCKOUTFX_BLAST_FURNACE_METER_Y + BLXCKOUTFX_BLAST_FURNACE_METER_HEIGHT)
            );
            BlxckoutFXRenderContext.enterTfcFirepitPotRender(
                    blxckoutfx$mapTextureCoordinate(x1, x2, minU, maxU, BLXCKOUTFX_BLAST_FURNACE_FUEL_METER_X),
                    blxckoutfx$mapTextureCoordinate(y1, y2, minV, maxV, BLXCKOUTFX_BLAST_FURNACE_METER_Y),
                    blxckoutfx$mapTextureCoordinate(x1, x2, minU, maxU, BLXCKOUTFX_BLAST_FURNACE_FUEL_METER_X + BLXCKOUTFX_BLAST_FURNACE_METER_WIDTH),
                    blxckoutfx$mapTextureCoordinate(y1, y2, minV, maxV, BLXCKOUTFX_BLAST_FURNACE_METER_Y + BLXCKOUTFX_BLAST_FURNACE_METER_HEIGHT)
            );
            BlxckoutFXRenderContext.enterTfcCrucibleTemperatureRender(
                    blxckoutfx$mapTextureCoordinate(x1, x2, minU, maxU, BLXCKOUTFX_BLAST_FURNACE_TEMPERATURE_X),
                    blxckoutfx$mapTextureCoordinate(y1, y2, minV, maxV, BLXCKOUTFX_BLAST_FURNACE_TEMPERATURE_Y),
                    blxckoutfx$mapTextureCoordinate(x1, x2, minU, maxU, BLXCKOUTFX_BLAST_FURNACE_TEMPERATURE_X + BLXCKOUTFX_BLAST_FURNACE_TEMPERATURE_WIDTH),
                    blxckoutfx$mapTextureCoordinate(y1, y2, minV, maxV, BLXCKOUTFX_BLAST_FURNACE_TEMPERATURE_Y + BLXCKOUTFX_BLAST_FURNACE_TEMPERATURE_HEIGHT)
            );
            BlxckoutFXRenderContext.enterTfcCrucibleVesselRender(
                    blxckoutfx$mapTextureCoordinate(x1, x2, minU, maxU, BLXCKOUTFX_BLAST_FURNACE_VESSEL_X),
                    blxckoutfx$mapTextureCoordinate(y1, y2, minV, maxV, BLXCKOUTFX_BLAST_FURNACE_VESSEL_Y),
                    blxckoutfx$mapTextureCoordinate(x1, x2, minU, maxU, BLXCKOUTFX_BLAST_FURNACE_VESSEL_X + BLXCKOUTFX_BLAST_FURNACE_VESSEL_WIDTH),
                    blxckoutfx$mapTextureCoordinate(y1, y2, minV, maxV, BLXCKOUTFX_BLAST_FURNACE_VESSEL_Y + BLXCKOUTFX_BLAST_FURNACE_VESSEL_HEIGHT)
            );
        }
        if (blxckoutfx$isAnvilBase(texture, minU, maxU, minV, maxV)) {
            BlxckoutFXRenderContext.enterTfcAnvilForgingGridRender(
                    blxckoutfx$mapTextureCoordinate(x1, x2, minU, maxU, BLXCKOUTFX_ANVIL_FORGING_GRID_X),
                    blxckoutfx$mapTextureCoordinate(y1, y2, minV, maxV, BLXCKOUTFX_ANVIL_FORGING_GRID_Y),
                    blxckoutfx$mapTextureCoordinate(x1, x2, minU, maxU, BLXCKOUTFX_ANVIL_FORGING_GRID_X + BLXCKOUTFX_ANVIL_FORGING_GRID_WIDTH),
                    blxckoutfx$mapTextureCoordinate(y1, y2, minV, maxV, BLXCKOUTFX_ANVIL_FORGING_GRID_Y + BLXCKOUTFX_ANVIL_FORGING_GRID_HEIGHT)
            );
        }
        if (blxckoutfx$isSewingWorkspaceBase(texture, minU, maxU, minV, maxV)) {
            blxckoutfx$cacheSewingWorkspace(x1, x2, y1, y2, minU, maxU, minV, maxV);
            BlxckoutFXRenderContext.enterTfcSewingWorkspaceRender(
                    blxckoutfx$sewingWorkspaceX1,
                    blxckoutfx$sewingWorkspaceY1,
                    blxckoutfx$sewingWorkspaceX2,
                    blxckoutfx$sewingWorkspaceY2
            );
        } else if (blxckoutfx$isSewingWorkspaceBlit(texture, x1, x2, y1, y2)) {
            BlxckoutFXRenderContext.enterTfcSewingWorkspaceRender(x1, y1, x2, y2);
        }
    }

    @Inject(method = "innerBlit(Lnet/minecraft/resources/ResourceLocation;IIIIIFFFF)V", at = @At("RETURN"), require = 0)
    private void blxckoutfx$restoreScribingLogoColorDarkening(
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
        if (blxckoutfx$shouldPreserveScribingLogoColors(texture, x1, x2, y1, y2, minU, maxU, minV, maxV)) {
            BlxckoutFXRenderContext.exitTfcScribingLogoRender();
        }
        if (blxckoutfx$isFirepitDirectIcon(texture, minU, maxU, minV, maxV)) {
            BlxckoutFXRenderContext.allowTextureDarkening();
        }
        if (blxckoutfx$isBlastFurnaceTemperatureIndicator(texture, minU, maxU, minV, maxV)) {
            BlxckoutFXRenderContext.allowTextureDarkening();
        }
        if (blxckoutfx$isBlastFurnaceFillMeter(texture, minU, maxU, minV, maxV)) {
            BlxckoutFXRenderContext.allowTextureDarkening();
        }
        if (blxckoutfx$isAnvilDirectIndicator(texture, minU, maxU, minV, maxV)) {
            BlxckoutFXRenderContext.allowTextureDarkening();
        }
        String[] anvilActionMask = blxckoutfx$getAnvilActionMask(texture, minU, maxU, minV, maxV);
        if (anvilActionMask != null) {
            blxckoutfx$redrawScaledMaskedIcon((GuiGraphics) (Object) this, texture, x1, x2, y1, y2, minU, minV, anvilActionMask, 2);
        }
        if (blxckoutfx$isAnvilPlanIcon(texture, minU, maxU, minV, maxV)) {
            blxckoutfx$redrawScaledMaskedIcon((GuiGraphics) (Object) this, texture, x1, x2, y1, y2, minU, minV, BLXCKOUTFX_ANVIL_PLAN_ICON_MASK, 1);
        }
        if (blxckoutfx$isAnvilJeiQuestionIcon(texture, minU, maxU, minV, maxV)) {
            blxckoutfx$redrawScaledMaskedIcon((GuiGraphics) (Object) this, texture, x1, x2, y1, y2, minU, minV, BLXCKOUTFX_ANVIL_JEI_QUESTION_MASK, 1);
        }
        if (blxckoutfx$isBarrelQuestionIcon(texture, minU, maxU, minV, maxV)) {
            blxckoutfx$redrawMaskedIcon((GuiGraphics) (Object) this, texture, x1, x2, y1, y2, minU, minV, BLXCKOUTFX_FIREPIT_RECIPE_QUESTION_MASK);
        }
        if (blxckoutfx$isSmallInventoryArtifactBackground(texture, minU, maxU, minV, maxV)
                || blxckoutfx$isSnsContainerArtifactBackground(texture, minU, maxU, minV, maxV)) {
            blxckoutfx$redrawLogPileBackgroundArtifacts((GuiGraphics) (Object) this, texture, x1, x2, y1, y2, minU, maxU, minV, maxV);
        }
        if (blxckoutfx$isBeehiveBase(texture, minU, maxU, minV, maxV)) {
            blxckoutfx$redrawBeehiveIndicators((GuiGraphics) (Object) this, texture, x1, x2, y1, y2, minU, maxU, minV, maxV);
        }
        String[] barrelSealMask = blxckoutfx$getBarrelSealMask(texture, minU, maxU, minV, maxV);
        if (barrelSealMask != null) {
            blxckoutfx$redrawMaskedIcon((GuiGraphics) (Object) this, texture, x1, x2, y1, y2, minU, minV, barrelSealMask);
        }
        if (blxckoutfx$isFirepitTemperatureBase(texture, minU, maxU, minV, maxV)) {
            BlxckoutFXRenderContext.exitTfcFirepitTemperatureRender();
        }
        if (blxckoutfx$isFirepitPotBase(texture, minU, maxU, minV, maxV)) {
            BlxckoutFXRenderContext.exitTfcFirepitPotRender();
        }
        if (blxckoutfx$isStovetopPotBase(texture, minU, maxU, minV, maxV)) {
            BlxckoutFXRenderContext.exitTfcFirepitPotRender();
        }
        if (blxckoutfx$isFirepitGrillBase(texture, minU, maxU, minV, maxV)) {
            BlxckoutFXRenderContext.exitTfcFirepitGrillRender();
        }
        if (blxckoutfx$isCrucibleBase(texture, minU, maxU, minV, maxV)) {
            BlxckoutFXRenderContext.exitTfcCrucibleVesselRender();
            BlxckoutFXRenderContext.exitTfcCrucibleTemperatureRender();
        }
        if (blxckoutfx$isBlastFurnaceBase(texture, minU, maxU, minV, maxV)) {
            BlxckoutFXRenderContext.exitTfcCrucibleVesselRender();
            BlxckoutFXRenderContext.exitTfcCrucibleTemperatureRender();
            BlxckoutFXRenderContext.exitTfcFirepitPotRender();
            BlxckoutFXRenderContext.exitTfcFirepitTemperatureRender();
        }
        if (blxckoutfx$isAnvilBase(texture, minU, maxU, minV, maxV)) {
            BlxckoutFXRenderContext.exitTfcAnvilForgingGridRender();
        }
        if (blxckoutfx$isFirepitRecipeQuestion(texture, minU, maxU, minV, maxV)) {
            blxckoutfx$redrawMaskedIcon((GuiGraphics) (Object) this, texture, x1, x2, y1, y2, minU, minV, BLXCKOUTFX_FIREPIT_RECIPE_QUESTION_MASK);
        }
        if (blxckoutfx$isSewingWorkspaceBase(texture, minU, maxU, minV, maxV)
                || blxckoutfx$isSewingWorkspaceBlit(texture, x1, x2, y1, y2)) {
            BlxckoutFXRenderContext.exitTfcSewingWorkspaceRender();
        }
        String[] iconMask = blxckoutfx$getSewingIconMask(texture, minU, maxU, minV, maxV);
        if (iconMask != null) {
            if (blxckoutfx$isHoveredSewingRecipeBook(texture, minU, maxU, minV, maxV)) {
                blxckoutfx$redrawSewingRecipeBookBackground((GuiGraphics) (Object) this, texture, x1, y1, x2 - x1, y2 - y1);
            }
            blxckoutfx$redrawMaskedIcon((GuiGraphics) (Object) this, texture, x1, x2, y1, y2, minU, minV, iconMask);
        }
        if (blxckoutfx$isPerfectedAnvilHelperActive()
                && blxckoutfx$isPerfectedAnvilHelperActionSprite(texture, minU, maxU, minV, maxV)) {
            BlxckoutFXRenderContext.allowTextureDarkening();
        }
    }

    @Inject(
            method = "m_7286_(Lnet/minecraft/client/gui/GuiGraphics;FII)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/dries007/tfc/client/RenderHelpers;fillAreaWithSprite(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;IIIIII)V",
                    shift = At.Shift.BEFORE,
                    remap = false
            ),
            require = 0,
            remap = false
    )
    private void blxckoutfx$suppressCrucibleFluidDarkening(
            GuiGraphics guiGraphics,
            float partialTick,
            int mouseX,
            int mouseY,
            CallbackInfo ci
    ) {
        BlxckoutFXRenderContext.suppressTextureDarkening();
    }

    @Inject(
            method = "m_7286_(Lnet/minecraft/client/gui/GuiGraphics;FII)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/dries007/tfc/client/RenderHelpers;fillAreaWithSprite(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;IIIIII)V",
                    shift = At.Shift.AFTER,
                    remap = false
            ),
            require = 0,
            remap = false
    )
    private void blxckoutfx$restoreCrucibleFluidDarkening(
            GuiGraphics guiGraphics,
            float partialTick,
            int mouseX,
            int mouseY,
            CallbackInfo ci
    ) {
        BlxckoutFXRenderContext.allowTextureDarkening();
    }

    @Inject(
            method = "m_7286_(Lnet/minecraft/client/gui/GuiGraphics;FII)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraftforge/common/util/LazyOptional;ifPresent(Lnet/minecraftforge/common/util/NonNullConsumer;)V",
                    shift = At.Shift.BEFORE,
                    remap = false
            ),
            require = 0,
            remap = false
    )
    private void blxckoutfx$suppressBarrelFluidDarkening(
            GuiGraphics guiGraphics,
            float partialTick,
            int mouseX,
            int mouseY,
            CallbackInfo ci
    ) {
        String screenClassName = ((Object) this).getClass().getName();
        if (screenClassName.equals("net.dries007.tfc.client.screen.BarrelScreen")
                || screenClassName.equals("com.eerussianguy.firmalife.client.screen.BigBarrelScreen")) {
            blxckoutfx$barrelFluidDarkeningSuppressed = true;
            BlxckoutFXRenderContext.suppressTextureDarkening();
        }
    }

    @Inject(
            method = "m_7286_(Lnet/minecraft/client/gui/GuiGraphics;FII)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraftforge/common/util/LazyOptional;ifPresent(Lnet/minecraftforge/common/util/NonNullConsumer;)V",
                    shift = At.Shift.AFTER,
                    remap = false
            ),
            require = 0,
            remap = false
    )
    private void blxckoutfx$restoreBarrelFluidDarkening(
            GuiGraphics guiGraphics,
            float partialTick,
            int mouseX,
            int mouseY,
            CallbackInfo ci
    ) {
        if (blxckoutfx$barrelFluidDarkeningSuppressed) {
            blxckoutfx$barrelFluidDarkeningSuppressed = false;
            BlxckoutFXRenderContext.allowTextureDarkening();
        }
    }

    @Inject(
            method = "m_7286_(Lnet/minecraft/client/gui/GuiGraphics;FII)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;m_280218_(Lnet/minecraft/resources/ResourceLocation;IIIIII)V",
                    shift = At.Shift.BEFORE,
                    remap = false
            ),
            require = 0,
            remap = false
    )
    private void blxckoutfx$suppressBeehiveHoneyDarkening(
            GuiGraphics guiGraphics,
            float partialTick,
            int mouseX,
            int mouseY,
            CallbackInfo ci
    ) {
        if (((Object) this).getClass().getName().equals("com.eerussianguy.firmalife.client.screen.BeehiveScreen")) {
            blxckoutfx$beehiveHoneyDarkeningSuppressed = true;
            BlxckoutFXRenderContext.suppressTextureDarkening();
        }
    }

    @Inject(
            method = "m_7286_(Lnet/minecraft/client/gui/GuiGraphics;FII)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;m_280218_(Lnet/minecraft/resources/ResourceLocation;IIIIII)V",
                    shift = At.Shift.AFTER,
                    remap = false
            ),
            require = 0,
            remap = false
    )
    private void blxckoutfx$restoreBeehiveHoneyDarkening(
            GuiGraphics guiGraphics,
            float partialTick,
            int mouseX,
            int mouseY,
            CallbackInfo ci
    ) {
        if (blxckoutfx$beehiveHoneyDarkeningSuppressed) {
            blxckoutfx$beehiveHoneyDarkeningSuppressed = false;
            BlxckoutFXRenderContext.allowTextureDarkening();
        }
    }

    @Inject(
            method = "draw(Lnet/dries007/tfc/common/recipes/KnappingRecipe;Lmezz/jei/api/gui/ingredient/IRecipeSlotsView;Lnet/minecraft/client/gui/GuiGraphics;DD)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/dries007/tfc/compat/jei/category/KnappingRecipeCategory;getTexture(Lmezz/jei/api/gui/ingredient/IRecipeSlotsView;Z)Lmezz/jei/api/gui/drawable/IDrawable;",
                    ordinal = 0,
                    remap = false
            ),
            require = 0,
            remap = false
    )
    private void blxckoutfx$suppressJeiKnappingDarkening(CallbackInfo ci) {
        BlxckoutFXRenderContext.suppressTextureDarkening();
    }

    @Inject(
            method = "draw(Lnet/dries007/tfc/common/recipes/KnappingRecipe;Lmezz/jei/api/gui/ingredient/IRecipeSlotsView;Lnet/minecraft/client/gui/GuiGraphics;DD)V",
            at = @At("RETURN"),
            require = 0,
            remap = false
    )
    private void blxckoutfx$restoreJeiKnappingDarkening(CallbackInfo ci) {
        BlxckoutFXRenderContext.allowTextureDarkening();
    }

    @Inject(
            method = "render(Lnet/minecraft/client/gui/GuiGraphics;Lvazkii/patchouli/api/IComponentRenderContext;IILnet/dries007/tfc/common/recipes/KnappingRecipe;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;II)V",
            at = @At("HEAD"),
            require = 0,
            remap = false
    )
    private static void blxckoutfx$suppressPatchouliKnappingDarkening(CallbackInfo ci) {
        BlxckoutFXRenderContext.suppressTextureDarkening();
    }

    @Inject(
            method = "render(Lnet/minecraft/client/gui/GuiGraphics;Lvazkii/patchouli/api/IComponentRenderContext;IILnet/dries007/tfc/common/recipes/KnappingRecipe;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;II)V",
            at = @At("RETURN"),
            require = 0,
            remap = false
    )
    private static void blxckoutfx$restorePatchouliKnappingDarkening(CallbackInfo ci) {
        BlxckoutFXRenderContext.allowTextureDarkening();
    }

    @Inject(
            method = "m_87963_(Lnet/minecraft/client/gui/GuiGraphics;IIF)V",
            at = @At("RETURN"),
            require = 0,
            remap = false
    )
    private void blxckoutfx$restoreAnvilStepButtonDarkening(
            GuiGraphics guiGraphics,
            int mouseX,
            int mouseY,
            float partialTick,
            CallbackInfo ci
    ) {
        if (blxckoutfx$anvilPlanIconDarkeningSuppressed) {
            blxckoutfx$anvilPlanIconDarkeningSuppressed = false;
            BlxckoutFXRenderContext.allowTextureDarkening();
        }
    }

    @Inject(
            method = "m_87963_(Lnet/minecraft/client/gui/GuiGraphics;IIF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;m_280163_(Lnet/minecraft/resources/ResourceLocation;IIFFIIII)V",
                    ordinal = 0,
                    shift = At.Shift.AFTER,
                    remap = false
            ),
            require = 0,
            remap = false
    )
    private void blxckoutfx$suppressAnvilPlanIconDarkening(
            GuiGraphics guiGraphics,
            int mouseX,
            int mouseY,
            float partialTick,
            CallbackInfo ci
    ) {
        if (((Object) this).getClass().getName().equals("net.dries007.tfc.client.screen.button.AnvilPlanButton")) {
            blxckoutfx$anvilPlanIconDarkeningSuppressed = true;
            BlxckoutFXRenderContext.suppressTextureDarkening();
        }
    }

    @Inject(
            method = "m_87963_(Lnet/minecraft/client/gui/GuiGraphics;IIF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;m_280163_(Lnet/minecraft/resources/ResourceLocation;IIFFIIII)V",
                    ordinal = 1,
                    shift = At.Shift.BEFORE,
                    remap = false
            ),
            require = 0,
            remap = false
    )
    private void blxckoutfx$darkenAnvilPlanFallbackIcon(
            GuiGraphics guiGraphics,
            int mouseX,
            int mouseY,
            float partialTick,
            CallbackInfo ci
    ) {
        if (blxckoutfx$anvilPlanIconDarkeningSuppressed) {
            BlxckoutFXRenderContext.allowTextureDarkening();
        }
    }

    @Inject(
            method = "m_87963_(Lnet/minecraft/client/gui/GuiGraphics;IIF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;m_280163_(Lnet/minecraft/resources/ResourceLocation;IIFFIIII)V",
                    ordinal = 1,
                    shift = At.Shift.AFTER,
                    remap = false
            ),
            require = 0,
            remap = false
    )
    private void blxckoutfx$restoreAnvilPlanIconDarkening(
            GuiGraphics guiGraphics,
            int mouseX,
            int mouseY,
            float partialTick,
            CallbackInfo ci
    ) {
        if (blxckoutfx$anvilPlanIconDarkeningSuppressed) {
            BlxckoutFXRenderContext.suppressTextureDarkening();
        }
    }

    @Unique
    private static boolean blxckoutfx$shouldPreserveScribingLogoColors(
            ResourceLocation texture,
            int x1,
            int x2,
            int y1,
            int y2,
            float minU,
            float maxU,
            float minV,
            float maxV
    ) {
        if (!blxckoutfx$isScribingTableTexture(texture)
                || x1 >= x2
                || y1 >= y2
                || minU >= maxU
                || minV >= maxV) {
            return false;
        }

        float logoMaxU = (BLXCKOUTFX_SCRIBING_LOGO_X + BLXCKOUTFX_SCRIBING_LOGO_WIDTH) / (float) BLXCKOUTFX_GUI_TEXTURE_SIZE;
        float logoMaxV = (BLXCKOUTFX_SCRIBING_LOGO_Y + BLXCKOUTFX_SCRIBING_LOGO_HEIGHT) / (float) BLXCKOUTFX_GUI_TEXTURE_SIZE;
        return minU <= BLXCKOUTFX_SCRIBING_LOGO_X / (float) BLXCKOUTFX_GUI_TEXTURE_SIZE
                && maxU >= logoMaxU
                && minV <= BLXCKOUTFX_SCRIBING_LOGO_Y / (float) BLXCKOUTFX_GUI_TEXTURE_SIZE
                && maxV >= logoMaxV;
    }

    @Unique
    private static int blxckoutfx$mapTextureCoordinate(int start, int end, float minUv, float maxUv, int textureCoordinate) {
        float targetUv = textureCoordinate / (float) BLXCKOUTFX_GUI_TEXTURE_SIZE;
        return Math.round(start + (end - start) * (targetUv - minUv) / (maxUv - minUv));
    }

    @Unique
    private static boolean blxckoutfx$isScribingTableTexture(ResourceLocation texture) {
        return texture != null
                && BLXCKOUTFX_TFC_NAMESPACE.equals(texture.getNamespace())
                && BLXCKOUTFX_SCRIBING_TABLE_TEXTURE.equals(texture.getPath());
    }

    @Unique
    private static boolean blxckoutfx$isSewingWorkspaceBase(ResourceLocation texture, float minU, float maxU, float minV, float maxV) {
        return blxckoutfx$isSewingTableTexture(texture)
                && minU <= BLXCKOUTFX_SEWING_WORKSPACE_X / (float) BLXCKOUTFX_GUI_TEXTURE_SIZE
                && maxU >= (BLXCKOUTFX_SEWING_WORKSPACE_X + BLXCKOUTFX_SEWING_WORKSPACE_WIDTH) / (float) BLXCKOUTFX_GUI_TEXTURE_SIZE
                && minV <= BLXCKOUTFX_SEWING_WORKSPACE_Y / (float) BLXCKOUTFX_GUI_TEXTURE_SIZE
                && maxV >= (BLXCKOUTFX_SEWING_WORKSPACE_Y + BLXCKOUTFX_SEWING_WORKSPACE_HEIGHT) / (float) BLXCKOUTFX_GUI_TEXTURE_SIZE;
    }

    @Unique
    private static void blxckoutfx$cacheSewingWorkspace(
            int x1,
            int x2,
            int y1,
            int y2,
            float minU,
            float maxU,
            float minV,
            float maxV
    ) {
        blxckoutfx$sewingWorkspaceX1 = blxckoutfx$mapTextureCoordinate(x1, x2, minU, maxU, BLXCKOUTFX_SEWING_WORKSPACE_X);
        blxckoutfx$sewingWorkspaceY1 = blxckoutfx$mapTextureCoordinate(y1, y2, minV, maxV, BLXCKOUTFX_SEWING_WORKSPACE_Y);
        blxckoutfx$sewingWorkspaceX2 = blxckoutfx$mapTextureCoordinate(x1, x2, minU, maxU, BLXCKOUTFX_SEWING_WORKSPACE_X + BLXCKOUTFX_SEWING_WORKSPACE_WIDTH);
        blxckoutfx$sewingWorkspaceY2 = blxckoutfx$mapTextureCoordinate(y1, y2, minV, maxV, BLXCKOUTFX_SEWING_WORKSPACE_Y + BLXCKOUTFX_SEWING_WORKSPACE_HEIGHT);
    }

    @Unique
    private static boolean blxckoutfx$isSewingWorkspaceBlit(ResourceLocation texture, int x1, int x2, int y1, int y2) {
        return blxckoutfx$isSewingTableTexture(texture)
                && x1 >= blxckoutfx$sewingWorkspaceX1
                && x2 <= blxckoutfx$sewingWorkspaceX2
                && y1 >= blxckoutfx$sewingWorkspaceY1
                && y2 <= blxckoutfx$sewingWorkspaceY2;
    }

    @Unique
    private static String[] blxckoutfx$getSewingIconMask(ResourceLocation texture, float minU, float maxU, float minV, float maxV) {
        if (blxckoutfx$sewingIconOverlayDepth > 0 || !blxckoutfx$isSewingTableTexture(texture)) {
            return null;
        }

        int sourceX = Math.round(minU * BLXCKOUTFX_GUI_TEXTURE_SIZE);
        int sourceY = Math.round(minV * BLXCKOUTFX_GUI_TEXTURE_SIZE);
        int sourceWidth = Math.round((maxU - minU) * BLXCKOUTFX_GUI_TEXTURE_SIZE);
        int sourceHeight = Math.round((maxV - minV) * BLXCKOUTFX_GUI_TEXTURE_SIZE);
        if (sourceX == 236 && sourceWidth == 20 && sourceHeight == 20) {
            return switch (sourceY) {
                case 0, 20, 40, 60 -> BLXCKOUTFX_CLOTH_ICON_MASK;
                case 80, 100 -> BLXCKOUTFX_REMOVE_STITCH_ICON_MASK;
                case 120, 140 -> BLXCKOUTFX_STITCH_ICON_MASK;
                default -> null;
            };
        }
        if (sourceX == 192 && sourceWidth == 20 && sourceHeight == 18 && (sourceY == 96 || sourceY == 114)) {
            return BLXCKOUTFX_RECIPE_BOOK_ICON_MASK;
        }
        return null;
    }

    @Unique
    private static boolean blxckoutfx$isHoveredSewingRecipeBook(
            ResourceLocation texture,
            float minU,
            float maxU,
            float minV,
            float maxV
    ) {
        return blxckoutfx$isSewingTableTexture(texture)
                && Math.round(minU * BLXCKOUTFX_GUI_TEXTURE_SIZE) == 192
                && Math.round(minV * BLXCKOUTFX_GUI_TEXTURE_SIZE) == 114
                && Math.round((maxU - minU) * BLXCKOUTFX_GUI_TEXTURE_SIZE) == 20
                && Math.round((maxV - minV) * BLXCKOUTFX_GUI_TEXTURE_SIZE) == 18;
    }

    @Unique
    private static void blxckoutfx$redrawSewingRecipeBookBackground(
            GuiGraphics guiGraphics,
            ResourceLocation texture,
            int x,
            int y,
            int width,
            int height
    ) {
        blxckoutfx$sewingIconOverlayDepth++;
        try {
            // The TFC hover sprite has a bright blue background that does not fit the darkened UI.
            guiGraphics.blit(texture, x, y, width, height, 192, 96, 20, 18, BLXCKOUTFX_GUI_TEXTURE_SIZE, BLXCKOUTFX_GUI_TEXTURE_SIZE);
        } finally {
            blxckoutfx$sewingIconOverlayDepth--;
        }
    }

    @Unique
    private static void blxckoutfx$redrawMaskedIcon(
            GuiGraphics guiGraphics,
            ResourceLocation texture,
            int x1,
            int x2,
            int y1,
            int y2,
            float minU,
            float minV,
            String[] iconMask
    ) {
        int sourceX = Math.round(minU * BLXCKOUTFX_GUI_TEXTURE_SIZE);
        int sourceY = Math.round(minV * BLXCKOUTFX_GUI_TEXTURE_SIZE);
        blxckoutfx$sewingIconOverlayDepth++;
        BlxckoutFXRenderContext.suppressTextureDarkening();
        try {
            for (int row = 0; row < iconMask.length; row++) {
                String maskRow = iconMask[row];
                int runStart = maskRow.indexOf('#');
                while (runStart >= 0) {
                    int runEnd = maskRow.indexOf('.', runStart);
                    if (runEnd < 0) {
                        runEnd = maskRow.length();
                    }

                    int drawX1 = blxckoutfx$mapIconCoordinate(x1, x2, runStart, maskRow.length());
                    int drawX2 = blxckoutfx$mapIconCoordinate(x1, x2, runEnd, maskRow.length());
                    int drawY1 = blxckoutfx$mapIconCoordinate(y1, y2, row, iconMask.length);
                    int drawY2 = blxckoutfx$mapIconCoordinate(y1, y2, row + 1, iconMask.length);
                    if (drawX1 < drawX2 && drawY1 < drawY2) {
                        guiGraphics.blit(
                                texture,
                                drawX1,
                                drawY1,
                                drawX2 - drawX1,
                                drawY2 - drawY1,
                                sourceX + runStart,
                                sourceY + row,
                                runEnd - runStart,
                                1,
                                BLXCKOUTFX_GUI_TEXTURE_SIZE,
                                BLXCKOUTFX_GUI_TEXTURE_SIZE
                        );
                    }
                    runStart = maskRow.indexOf('#', runEnd);
                }
            }
        } finally {
            BlxckoutFXRenderContext.allowTextureDarkening();
            blxckoutfx$sewingIconOverlayDepth--;
        }
    }

    @Unique
    private static void blxckoutfx$redrawScaledMaskedIcon(
            GuiGraphics guiGraphics,
            ResourceLocation texture,
            int x1,
            int x2,
            int y1,
            int y2,
            float minU,
            float minV,
            String[] iconMask,
            int sourceScale
    ) {
        int sourceX = Math.round(minU * BLXCKOUTFX_GUI_TEXTURE_SIZE);
        int sourceY = Math.round(minV * BLXCKOUTFX_GUI_TEXTURE_SIZE);
        blxckoutfx$sewingIconOverlayDepth++;
        BlxckoutFXRenderContext.suppressTextureDarkening();
        try {
            for (int row = 0; row < iconMask.length; row++) {
                String maskRow = iconMask[row];
                int runStart = maskRow.indexOf('#');
                while (runStart >= 0) {
                    int runEnd = maskRow.indexOf('.', runStart);
                    if (runEnd < 0) {
                        runEnd = maskRow.length();
                    }

                    int drawX1 = blxckoutfx$mapIconCoordinate(x1, x2, runStart, maskRow.length());
                    int drawX2 = blxckoutfx$mapIconCoordinate(x1, x2, runEnd, maskRow.length());
                    int drawY1 = blxckoutfx$mapIconCoordinate(y1, y2, row, iconMask.length);
                    int drawY2 = blxckoutfx$mapIconCoordinate(y1, y2, row + 1, iconMask.length);
                    if (drawX1 < drawX2 && drawY1 < drawY2) {
                        guiGraphics.blit(
                                texture,
                                drawX1,
                                drawY1,
                                drawX2 - drawX1,
                                drawY2 - drawY1,
                                sourceX + runStart * sourceScale,
                                sourceY + row * sourceScale,
                                (runEnd - runStart) * sourceScale,
                                sourceScale,
                                BLXCKOUTFX_GUI_TEXTURE_SIZE,
                                BLXCKOUTFX_GUI_TEXTURE_SIZE
                        );
                    }
                    runStart = maskRow.indexOf('#', runEnd);
                }
            }
        } finally {
            BlxckoutFXRenderContext.allowTextureDarkening();
            blxckoutfx$sewingIconOverlayDepth--;
        }
    }

    @Unique
    private static int blxckoutfx$mapIconCoordinate(int start, int end, int iconCoordinate, int iconSize) {
        return Math.round(start + (end - start) * iconCoordinate / (float) iconSize);
    }

    @Unique
    private static boolean blxckoutfx$isSewingTableTexture(ResourceLocation texture) {
        return texture != null
                && BLXCKOUTFX_TFC_NAMESPACE.equals(texture.getNamespace())
                && BLXCKOUTFX_SEWING_TABLE_TEXTURE.equals(texture.getPath());
    }

    @Unique
    private static boolean blxckoutfx$isAnvilTexture(ResourceLocation texture) {
        return texture != null
                && BLXCKOUTFX_TFC_NAMESPACE.equals(texture.getNamespace())
                && BLXCKOUTFX_ANVIL_TEXTURE.equals(texture.getPath());
    }

    @Unique
    private static boolean blxckoutfx$isBarrelTexture(ResourceLocation texture) {
        return texture != null
                && BLXCKOUTFX_TFC_NAMESPACE.equals(texture.getNamespace())
                && BLXCKOUTFX_BARREL_TEXTURE.equals(texture.getPath());
    }

    @Unique
    private static boolean blxckoutfx$isSmallInventoryArtifactBackground(
            ResourceLocation texture,
            float minU,
            float maxU,
            float minV,
            float maxV
    ) {
        Minecraft minecraft = Minecraft.getInstance();
        return minecraft != null
                && minecraft.screen != null
                && blxckoutfx$isSmallInventoryArtifactScreen(minecraft.screen.getClass().getName())
                && texture != null
                && BLXCKOUTFX_TFC_NAMESPACE.equals(texture.getNamespace())
                && BLXCKOUTFX_SMALL_INVENTORY_TEXTURE.equals(texture.getPath())
                && minU <= 0.0F
                && maxU >= 176.0F / BLXCKOUTFX_GUI_TEXTURE_SIZE
                && minV <= 0.0F
                && maxV >= 166.0F / BLXCKOUTFX_GUI_TEXTURE_SIZE;
    }

    @Unique
    private static boolean blxckoutfx$isSmallInventoryArtifactScreen(String screenClassName) {
        return screenClassName.equals("net.dries007.tfc.client.screen.LogPileScreen")
                || screenClassName.equals("net.dries007.tfc.client.screen.NestBoxScreen")
                || screenClassName.equals("net.dries007.tfc.client.screen.SmallVesselInventoryScreen");
    }

    @Unique
    private static boolean blxckoutfx$isSnsContainerArtifactBackground(
            ResourceLocation texture,
            float minU,
            float maxU,
            float minV,
            float maxV
    ) {
        Minecraft minecraft = Minecraft.getInstance();
        return minecraft != null
                && minecraft.screen != null
                && minecraft.screen.getClass().getName().equals("mod.traister101.sns.client.screen.ContainerItemScreen")
                && texture != null
                && BLXCKOUTFX_SNS_NAMESPACE.equals(texture.getNamespace())
                && BLXCKOUTFX_SNS_CONTAINER_TEXTURE.equals(texture.getPath())
                && minU <= 0.0F
                && maxU >= 176.0F / BLXCKOUTFX_GUI_TEXTURE_SIZE
                && minV <= 0.0F
                && maxV >= 166.0F / BLXCKOUTFX_GUI_TEXTURE_SIZE;
    }

    @Unique
    private static boolean blxckoutfx$isBeehiveBase(
            ResourceLocation texture,
            float minU,
            float maxU,
            float minV,
            float maxV
    ) {
        Minecraft minecraft = Minecraft.getInstance();
        return minecraft != null
                && minecraft.screen != null
                && minecraft.screen.getClass().getName().equals("com.eerussianguy.firmalife.client.screen.BeehiveScreen")
                && texture != null
                && BLXCKOUTFX_FIRMALIFE_NAMESPACE.equals(texture.getNamespace())
                && BLXCKOUTFX_BEEHIVE_TEXTURE.equals(texture.getPath())
                && minU <= 0.0F
                && maxU >= 176.0F / BLXCKOUTFX_GUI_TEXTURE_SIZE
                && minV <= 0.0F
                && maxV >= 166.0F / BLXCKOUTFX_GUI_TEXTURE_SIZE;
    }

    @Unique
    private static void blxckoutfx$redrawBeehiveIndicators(
            GuiGraphics guiGraphics,
            ResourceLocation texture,
            int x1,
            int x2,
            int y1,
            int y2,
            float minU,
            float maxU,
            float minV,
            float maxV
    ) {
        BlxckoutFXRenderContext.suppressTextureDarkening();
        try {
            blxckoutfx$redrawTextureRegion(
                    guiGraphics, texture, x1, x2, y1, y2, minU, maxU, minV, maxV,
                    BLXCKOUTFX_BEEHIVE_HONEY_X, BLXCKOUTFX_BEEHIVE_HONEY_Y,
                    BLXCKOUTFX_BEEHIVE_HONEY_WIDTH, BLXCKOUTFX_BEEHIVE_HONEY_HEIGHT
            );
        } finally {
            BlxckoutFXRenderContext.allowTextureDarkening();
        }
    }

    @Unique
    private static void blxckoutfx$redrawTextureRegion(
            GuiGraphics guiGraphics,
            ResourceLocation texture,
            int x1,
            int x2,
            int y1,
            int y2,
            float minU,
            float maxU,
            float minV,
            float maxV,
            int sourceX,
            int sourceY,
            int sourceWidth,
            int sourceHeight
    ) {
        int targetX1 = blxckoutfx$mapTextureCoordinate(x1, x2, minU, maxU, sourceX);
        int targetX2 = blxckoutfx$mapTextureCoordinate(x1, x2, minU, maxU, sourceX + sourceWidth);
        int targetY1 = blxckoutfx$mapTextureCoordinate(y1, y2, minV, maxV, sourceY);
        int targetY2 = blxckoutfx$mapTextureCoordinate(y1, y2, minV, maxV, sourceY + sourceHeight);
        if (targetX1 < targetX2 && targetY1 < targetY2) {
            guiGraphics.blit(
                    texture,
                    targetX1,
                    targetY1,
                    targetX2 - targetX1,
                    targetY2 - targetY1,
                    sourceX,
                    sourceY,
                    sourceWidth,
                    sourceHeight,
                    BLXCKOUTFX_GUI_TEXTURE_SIZE,
                    BLXCKOUTFX_GUI_TEXTURE_SIZE
            );
        }
    }

    @Unique
    private static void blxckoutfx$redrawLogPileBackgroundArtifacts(
            GuiGraphics guiGraphics,
            ResourceLocation texture,
            int x1,
            int x2,
            int y1,
            int y2,
            float minU,
            float maxU,
            float minV,
            float maxV
    ) {
        for (int[] pixel : BLXCKOUTFX_LOG_PILE_BACKGROUND_ARTIFACT_PIXELS) {
            int targetX1 = blxckoutfx$mapTextureCoordinate(x1, x2, minU, maxU, pixel[0]);
            int targetX2 = blxckoutfx$mapTextureCoordinate(x1, x2, minU, maxU, pixel[0] + 1);
            int targetY1 = blxckoutfx$mapTextureCoordinate(y1, y2, minV, maxV, pixel[1]);
            int targetY2 = blxckoutfx$mapTextureCoordinate(y1, y2, minV, maxV, pixel[1] + 1);
            if (targetX1 < targetX2 && targetY1 < targetY2) {
                guiGraphics.blit(
                        texture,
                        targetX1,
                        targetY1,
                        targetX2 - targetX1,
                        targetY2 - targetY1,
                        BLXCKOUTFX_LOG_PILE_NEUTRAL_PIXEL_X,
                        BLXCKOUTFX_LOG_PILE_NEUTRAL_PIXEL_Y,
                        1,
                        1,
                        BLXCKOUTFX_GUI_TEXTURE_SIZE,
                        BLXCKOUTFX_GUI_TEXTURE_SIZE
                );
            }
        }
    }

    @Unique
    private static boolean blxckoutfx$isSealButtonTexture(ResourceLocation texture) {
        return texture != null
                && BLXCKOUTFX_TFC_NAMESPACE.equals(texture.getNamespace())
                && (BLXCKOUTFX_BARREL_TEXTURE.equals(texture.getPath())
                || BLXCKOUTFX_POWDERKEG_TEXTURE.equals(texture.getPath())
                || BLXCKOUTFX_LARGE_VESSEL_TEXTURE.equals(texture.getPath()));
    }

    @Unique
    private static boolean blxckoutfx$isPerfectedAnvilHelperActive() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft == null) {
            return false;
        }
        return minecraft.getResourceManager()
                .getResourceStack(new ResourceLocation(BLXCKOUTFX_TFC_NAMESPACE, BLXCKOUTFX_ANVIL_TEXTURE))
                .stream()
                .map(resource -> resource.sourcePackId().toLowerCase(Locale.ROOT))
                .anyMatch(sourcePackId -> sourcePackId.contains(BLXCKOUTFX_PERFECTED_ANVIL_HELPER_PACK_ID));
    }

    @Unique
    private static boolean blxckoutfx$isFirepitDirectIcon(
            ResourceLocation texture,
            float minU,
            float maxU,
            float minV,
            float maxV
    ) {
        if (texture == null) {
            return false;
        }

        int sourceX = Math.round(minU * BLXCKOUTFX_GUI_TEXTURE_SIZE);
        int sourceY = Math.round(minV * BLXCKOUTFX_GUI_TEXTURE_SIZE);
        int sourceWidth = Math.round((maxU - minU) * BLXCKOUTFX_GUI_TEXTURE_SIZE);
        int sourceHeight = Math.round((maxV - minV) * BLXCKOUTFX_GUI_TEXTURE_SIZE);
        if ((blxckoutfx$isTfcFirepitTexture(texture) || blxckoutfx$isStovetopTexture(texture))
                && sourceX == 176 && sourceY == 0 && sourceWidth == 15 && sourceHeight == 5) {
            return true;
        }
        if (!BLXCKOUTFX_TFC_NAMESPACE.equals(texture.getNamespace())
                || !BLXCKOUTFX_FIREPIT_POT_TEXTURE.equals(texture.getPath())) {
            return false;
        }
        return sourceX == 193 && sourceY >= 17 && sourceY <= 37 && sourceWidth == 11 && sourceHeight <= 20;
    }

    @Unique
    private static boolean blxckoutfx$isTfcFirepitTexture(ResourceLocation texture) {
        return BLXCKOUTFX_TFC_NAMESPACE.equals(texture.getNamespace())
                && texture.getPath().startsWith(BLXCKOUTFX_FIREPIT_TEXTURE_PREFIX)
                && texture.getPath().endsWith(".png");
    }

    @Unique
    private static boolean blxckoutfx$isStovetopTexture(ResourceLocation texture) {
        return BLXCKOUTFX_FIRMALIFE_NAMESPACE.equals(texture.getNamespace())
                && (BLXCKOUTFX_STOVETOP_GRILL_TEXTURE.equals(texture.getPath())
                || BLXCKOUTFX_STOVETOP_POT_TEXTURE.equals(texture.getPath()));
    }

    @Unique
    private static boolean blxckoutfx$isBlastFurnaceTemperatureIndicator(
            ResourceLocation texture,
            float minU,
            float maxU,
            float minV,
            float maxV
    ) {
        return texture != null
                && BLXCKOUTFX_TFC_NAMESPACE.equals(texture.getNamespace())
                && BLXCKOUTFX_BLAST_FURNACE_TEXTURE.equals(texture.getPath())
                && Math.round(minU * BLXCKOUTFX_GUI_TEXTURE_SIZE) == 176
                && Math.round(minV * BLXCKOUTFX_GUI_TEXTURE_SIZE) == 0
                && Math.round((maxU - minU) * BLXCKOUTFX_GUI_TEXTURE_SIZE) == 15
                && Math.round((maxV - minV) * BLXCKOUTFX_GUI_TEXTURE_SIZE) == 5;
    }

    @Unique
    private static boolean blxckoutfx$isBlastFurnaceFillMeter(
            ResourceLocation texture,
            float minU,
            float maxU,
            float minV,
            float maxV
    ) {
        if (texture == null
                || !BLXCKOUTFX_TFC_NAMESPACE.equals(texture.getNamespace())
                || !BLXCKOUTFX_BLAST_FURNACE_TEXTURE.equals(texture.getPath())) {
            return false;
        }

        int sourceX = Math.round(minU * BLXCKOUTFX_GUI_TEXTURE_SIZE);
        int sourceY = Math.round(minV * BLXCKOUTFX_GUI_TEXTURE_SIZE);
        int sourceWidth = Math.round((maxU - minU) * BLXCKOUTFX_GUI_TEXTURE_SIZE);
        int sourceHeight = Math.round((maxV - minV) * BLXCKOUTFX_GUI_TEXTURE_SIZE);
        return sourceWidth == 10
                && ((sourceX == 246 && sourceY == 0 && sourceHeight == 66)
                || (sourceX == 236 && sourceY == 0 && sourceHeight >= 1 && sourceHeight <= 65)
                || (sourceX == 64 && sourceY == 1 && sourceHeight >= 1 && sourceHeight <= 64));
    }

    @Unique
    private static boolean blxckoutfx$isAnvilDirectIndicator(
            ResourceLocation texture,
            float minU,
            float maxU,
            float minV,
            float maxV
    ) {
        if (texture == null
                || !BLXCKOUTFX_TFC_NAMESPACE.equals(texture.getNamespace())
                || !BLXCKOUTFX_ANVIL_TEXTURE.equals(texture.getPath())) {
            return false;
        }

        int sourceX = Math.round(minU * BLXCKOUTFX_GUI_TEXTURE_SIZE);
        int sourceY = Math.round(minV * BLXCKOUTFX_GUI_TEXTURE_SIZE);
        int sourceWidth = Math.round((maxU - minU) * BLXCKOUTFX_GUI_TEXTURE_SIZE);
        int sourceHeight = Math.round((maxV - minV) * BLXCKOUTFX_GUI_TEXTURE_SIZE);
        return ((sourceX == 176 || sourceX == 181) && sourceY == 0 && sourceWidth == 5 && sourceHeight == 5)
                || (sourceX == 198 && sourceWidth == 20 && sourceHeight == 22);
    }

    @Unique
    private static boolean blxckoutfx$isAnvilActionIcon(ResourceLocation texture, float minU, float maxU, float minV, float maxV) {
        int sourceX = Math.round(minU * BLXCKOUTFX_GUI_TEXTURE_SIZE);
        int sourceY = Math.round(minV * BLXCKOUTFX_GUI_TEXTURE_SIZE);
        return blxckoutfx$sewingIconOverlayDepth == 0
                && texture != null
                && BLXCKOUTFX_TFC_NAMESPACE.equals(texture.getNamespace())
                && BLXCKOUTFX_ANVIL_TEXTURE.equals(texture.getPath())
                && ((sourceX >= 0 && sourceX <= 224 && sourceX % 32 == 0 && sourceY == 224)
                || (sourceX == 218 && sourceY == 18))
                && Math.round((maxU - minU) * BLXCKOUTFX_GUI_TEXTURE_SIZE) == 32
                && Math.round((maxV - minV) * BLXCKOUTFX_GUI_TEXTURE_SIZE) == 32;
    }

    @Unique
    private static boolean blxckoutfx$isPerfectedAnvilHelperActionSprite(
            ResourceLocation texture,
            float minU,
            float maxU,
            float minV,
            float maxV
    ) {
        return blxckoutfx$isAnvilActionIcon(texture, minU, maxU, minV, maxV)
                && Math.round(minV * BLXCKOUTFX_GUI_TEXTURE_SIZE) == 224;
    }

    @Unique
    private static String[] blxckoutfx$getAnvilActionMask(
            ResourceLocation texture,
            float minU,
            float maxU,
            float minV,
            float maxV
    ) {
        if (!blxckoutfx$isAnvilActionIcon(texture, minU, maxU, minV, maxV)) {
            return null;
        }
        int sourceX = Math.round(minU * BLXCKOUTFX_GUI_TEXTURE_SIZE);
        return sourceX == 218
                ? BLXCKOUTFX_ANVIL_HIT_RULE_MASK
                : BLXCKOUTFX_ANVIL_ACTION_MASKS[sourceX / 32];
    }

    @Unique
    private static boolean blxckoutfx$isAnvilPlanIcon(
            ResourceLocation texture,
            float minU,
            float maxU,
            float minV,
            float maxV
    ) {
        int sourceX = Math.round(minU * BLXCKOUTFX_GUI_TEXTURE_SIZE);
        int sourceY = Math.round(minV * BLXCKOUTFX_GUI_TEXTURE_SIZE);
        return texture != null
                && BLXCKOUTFX_TFC_NAMESPACE.equals(texture.getNamespace())
                && BLXCKOUTFX_ANVIL_TEXTURE.equals(texture.getPath())
                && ((sourceX == 219 && sourceY == 51) || (sourceX == 236 && sourceY == 0))
                && Math.round((maxU - minU) * BLXCKOUTFX_GUI_TEXTURE_SIZE) == 16
                && Math.round((maxV - minV) * BLXCKOUTFX_GUI_TEXTURE_SIZE) == 16;
    }

    @Unique
    private static boolean blxckoutfx$isBarrelQuestionIcon(
            ResourceLocation texture,
            float minU,
            float maxU,
            float minV,
            float maxV
    ) {
        return blxckoutfx$isBarrelTexture(texture)
                && Math.round(minU * BLXCKOUTFX_GUI_TEXTURE_SIZE) == 227
                && Math.round(minV * BLXCKOUTFX_GUI_TEXTURE_SIZE) == 0
                && Math.round((maxU - minU) * BLXCKOUTFX_GUI_TEXTURE_SIZE) == 9
                && Math.round((maxV - minV) * BLXCKOUTFX_GUI_TEXTURE_SIZE) == 14;
    }

    @Unique
    private static String[] blxckoutfx$getBarrelSealMask(
            ResourceLocation texture,
            float minU,
            float maxU,
            float minV,
            float maxV
    ) {
        if (!blxckoutfx$isSealButtonTexture(texture)
                || Math.round(minU * BLXCKOUTFX_GUI_TEXTURE_SIZE) != 236
                || Math.round((maxU - minU) * BLXCKOUTFX_GUI_TEXTURE_SIZE) != 20
                || Math.round((maxV - minV) * BLXCKOUTFX_GUI_TEXTURE_SIZE) != 20) {
            return null;
        }

        int sourceY = Math.round(minV * BLXCKOUTFX_GUI_TEXTURE_SIZE);
        return sourceY == 0 ? BLXCKOUTFX_BARREL_SEAL_MASK
                : sourceY == 20 ? BLXCKOUTFX_BARREL_UNSEAL_MASK
                : null;
    }

    @Unique
    private static boolean blxckoutfx$isAnvilJeiQuestionIcon(
            ResourceLocation texture,
            float minU,
            float maxU,
            float minV,
            float maxV
    ) {
        return texture != null
                && BLXCKOUTFX_TFC_NAMESPACE.equals(texture.getNamespace())
                && BLXCKOUTFX_ANVIL_TEXTURE.equals(texture.getPath())
                && Math.round(minU * BLXCKOUTFX_GUI_TEXTURE_SIZE) == 0
                && Math.round(minV * BLXCKOUTFX_GUI_TEXTURE_SIZE) == 207
                && Math.round((maxU - minU) * BLXCKOUTFX_GUI_TEXTURE_SIZE) == 9
                && Math.round((maxV - minV) * BLXCKOUTFX_GUI_TEXTURE_SIZE) == 14;
    }

    @Unique
    private static boolean blxckoutfx$isFirepitTemperatureBase(
            ResourceLocation texture,
            float minU,
            float maxU,
            float minV,
            float maxV
    ) {
        return texture != null
                && (blxckoutfx$isTfcFirepitTexture(texture) || blxckoutfx$isStovetopTexture(texture))
                && minU <= BLXCKOUTFX_FIREPIT_TEMPERATURE_X / (float) BLXCKOUTFX_GUI_TEXTURE_SIZE
                && maxU >= (BLXCKOUTFX_FIREPIT_TEMPERATURE_X + BLXCKOUTFX_FIREPIT_TEMPERATURE_WIDTH) / (float) BLXCKOUTFX_GUI_TEXTURE_SIZE
                && minV <= BLXCKOUTFX_FIREPIT_TEMPERATURE_Y / (float) BLXCKOUTFX_GUI_TEXTURE_SIZE
                && maxV >= (BLXCKOUTFX_FIREPIT_TEMPERATURE_Y + BLXCKOUTFX_FIREPIT_TEMPERATURE_HEIGHT) / (float) BLXCKOUTFX_GUI_TEXTURE_SIZE;
    }

    @Unique
    private static boolean blxckoutfx$isFirepitPotBase(
            ResourceLocation texture,
            float minU,
            float maxU,
            float minV,
            float maxV
    ) {
        return texture != null
                && BLXCKOUTFX_TFC_NAMESPACE.equals(texture.getNamespace())
                && BLXCKOUTFX_FIREPIT_POT_TEXTURE.equals(texture.getPath())
                && minU <= BLXCKOUTFX_FIREPIT_POT_X / (float) BLXCKOUTFX_GUI_TEXTURE_SIZE
                && maxU >= (BLXCKOUTFX_FIREPIT_POT_X + BLXCKOUTFX_FIREPIT_POT_WIDTH) / (float) BLXCKOUTFX_GUI_TEXTURE_SIZE
                && minV <= BLXCKOUTFX_FIREPIT_POT_Y / (float) BLXCKOUTFX_GUI_TEXTURE_SIZE
                && maxV >= (BLXCKOUTFX_FIREPIT_POT_Y + BLXCKOUTFX_FIREPIT_POT_HEIGHT) / (float) BLXCKOUTFX_GUI_TEXTURE_SIZE;
    }

    @Unique
    private static boolean blxckoutfx$isFirepitRecipeQuestion(
            ResourceLocation texture,
            float minU,
            float maxU,
            float minV,
            float maxV
    ) {
        return texture != null
                && BLXCKOUTFX_TFC_NAMESPACE.equals(texture.getNamespace())
                && BLXCKOUTFX_FIREPIT_POT_TEXTURE.equals(texture.getPath())
                && Math.round(minU * BLXCKOUTFX_GUI_TEXTURE_SIZE) == 247
                && Math.round(minV * BLXCKOUTFX_GUI_TEXTURE_SIZE) == 0
                && Math.round((maxU - minU) * BLXCKOUTFX_GUI_TEXTURE_SIZE) == 9
                && Math.round((maxV - minV) * BLXCKOUTFX_GUI_TEXTURE_SIZE) == 14;
    }

    @Unique
    private static boolean blxckoutfx$isFirepitGrillBase(
            ResourceLocation texture,
            float minU,
            float maxU,
            float minV,
            float maxV
    ) {
        return texture != null
                && ((BLXCKOUTFX_TFC_NAMESPACE.equals(texture.getNamespace())
                && BLXCKOUTFX_FIREPIT_GRILL_TEXTURE.equals(texture.getPath()))
                || (BLXCKOUTFX_FIRMALIFE_NAMESPACE.equals(texture.getNamespace())
                && BLXCKOUTFX_STOVETOP_GRILL_TEXTURE.equals(texture.getPath())))
                && minU <= BLXCKOUTFX_FIREPIT_GRILL_X / (float) BLXCKOUTFX_GUI_TEXTURE_SIZE
                && maxU >= (BLXCKOUTFX_FIREPIT_GRILL_X + BLXCKOUTFX_FIREPIT_GRILL_WIDTH) / (float) BLXCKOUTFX_GUI_TEXTURE_SIZE
                && minV <= BLXCKOUTFX_FIREPIT_GRILL_Y / (float) BLXCKOUTFX_GUI_TEXTURE_SIZE
                && maxV >= (BLXCKOUTFX_FIREPIT_GRILL_Y + BLXCKOUTFX_FIREPIT_GRILL_HEIGHT) / (float) BLXCKOUTFX_GUI_TEXTURE_SIZE;
    }

    @Unique
    private static boolean blxckoutfx$isStovetopPotBase(
            ResourceLocation texture,
            float minU,
            float maxU,
            float minV,
            float maxV
    ) {
        return texture != null
                && BLXCKOUTFX_FIRMALIFE_NAMESPACE.equals(texture.getNamespace())
                && BLXCKOUTFX_STOVETOP_POT_TEXTURE.equals(texture.getPath())
                && minU <= BLXCKOUTFX_STOVETOP_POT_X / (float) BLXCKOUTFX_GUI_TEXTURE_SIZE
                && maxU >= (BLXCKOUTFX_STOVETOP_POT_X + BLXCKOUTFX_STOVETOP_POT_WIDTH) / (float) BLXCKOUTFX_GUI_TEXTURE_SIZE
                && minV <= BLXCKOUTFX_STOVETOP_POT_Y / (float) BLXCKOUTFX_GUI_TEXTURE_SIZE
                && maxV >= (BLXCKOUTFX_STOVETOP_POT_Y + BLXCKOUTFX_STOVETOP_POT_HEIGHT) / (float) BLXCKOUTFX_GUI_TEXTURE_SIZE;
    }

    @Unique
    private static boolean blxckoutfx$isCrucibleBase(
            ResourceLocation texture,
            float minU,
            float maxU,
            float minV,
            float maxV
    ) {
        return texture != null
                && BLXCKOUTFX_TFC_NAMESPACE.equals(texture.getNamespace())
                && BLXCKOUTFX_CRUCIBLE_TEXTURE.equals(texture.getPath())
                && minU <= BLXCKOUTFX_CRUCIBLE_TEMPERATURE_X / (float) BLXCKOUTFX_GUI_TEXTURE_SIZE
                && maxU >= (BLXCKOUTFX_CRUCIBLE_TEMPERATURE_X + BLXCKOUTFX_CRUCIBLE_TEMPERATURE_WIDTH) / (float) BLXCKOUTFX_GUI_TEXTURE_SIZE
                && minV <= BLXCKOUTFX_CRUCIBLE_TEMPERATURE_Y / (float) BLXCKOUTFX_GUI_TEXTURE_SIZE
                && maxV >= (BLXCKOUTFX_CRUCIBLE_TEMPERATURE_Y + BLXCKOUTFX_CRUCIBLE_TEMPERATURE_HEIGHT) / (float) BLXCKOUTFX_GUI_TEXTURE_SIZE;
    }

    @Unique
    private static boolean blxckoutfx$isBlastFurnaceBase(
            ResourceLocation texture,
            float minU,
            float maxU,
            float minV,
            float maxV
    ) {
        return texture != null
                && BLXCKOUTFX_TFC_NAMESPACE.equals(texture.getNamespace())
                && BLXCKOUTFX_BLAST_FURNACE_TEXTURE.equals(texture.getPath())
                && minU <= BLXCKOUTFX_BLAST_FURNACE_TEMPERATURE_X / (float) BLXCKOUTFX_GUI_TEXTURE_SIZE
                && maxU >= (BLXCKOUTFX_BLAST_FURNACE_VESSEL_X + BLXCKOUTFX_BLAST_FURNACE_VESSEL_WIDTH) / (float) BLXCKOUTFX_GUI_TEXTURE_SIZE
                && minV <= BLXCKOUTFX_BLAST_FURNACE_TEMPERATURE_Y / (float) BLXCKOUTFX_GUI_TEXTURE_SIZE
                && maxV >= (BLXCKOUTFX_BLAST_FURNACE_VESSEL_Y + BLXCKOUTFX_BLAST_FURNACE_VESSEL_HEIGHT) / (float) BLXCKOUTFX_GUI_TEXTURE_SIZE;
    }

    @Unique
    private static boolean blxckoutfx$isAnvilBase(
            ResourceLocation texture,
            float minU,
            float maxU,
            float minV,
            float maxV
    ) {
        return texture != null
                && BLXCKOUTFX_TFC_NAMESPACE.equals(texture.getNamespace())
                && BLXCKOUTFX_ANVIL_TEXTURE.equals(texture.getPath())
                && minU <= BLXCKOUTFX_ANVIL_FORGING_GRID_X / (float) BLXCKOUTFX_GUI_TEXTURE_SIZE
                && maxU >= (BLXCKOUTFX_ANVIL_FORGING_GRID_X + BLXCKOUTFX_ANVIL_FORGING_GRID_WIDTH) / (float) BLXCKOUTFX_GUI_TEXTURE_SIZE
                && minV <= BLXCKOUTFX_ANVIL_FORGING_GRID_Y / (float) BLXCKOUTFX_GUI_TEXTURE_SIZE
                && maxV >= (BLXCKOUTFX_ANVIL_FORGING_GRID_Y + BLXCKOUTFX_ANVIL_FORGING_GRID_HEIGHT) / (float) BLXCKOUTFX_GUI_TEXTURE_SIZE;
    }
}
