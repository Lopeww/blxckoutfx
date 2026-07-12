package com.lopew.blxckoutfx.client;

import com.lopew.blxckoutfx.client.render.BlxckoutFXShaderProgram;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public final class BlxckoutFXShaders {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String BLXCKOUT_PRESET_KEY = "preset.blxckoutfx.blxckout";
    private static final String SOFT_PRESET_KEY = "preset.blxckoutfx.soft";
    private static final String BALANCED_PRESET_KEY = "preset.blxckoutfx.balanced";
    private static final String DARK_PRESET_KEY = "preset.blxckoutfx.dark";

    private static final ShaderPreset[] PRESETS = {
            new ShaderPreset("preset.blxckoutfx.off", 1.0F),
            new ShaderPreset(SOFT_PRESET_KEY, 2.0F),
            new ShaderPreset(BALANCED_PRESET_KEY, 3.2F),
            new ShaderPreset(DARK_PRESET_KEY, 4.3F),
            new ShaderPreset(BLXCKOUT_PRESET_KEY, 5.5F)
    };

    private static final float PERCEPTION_SCALE_R = 0.299F;
    private static final float PERCEPTION_SCALE_G = 0.587F;
    private static final float PERCEPTION_SCALE_B = 0.114F;

    private static final String VERTEX_SHADER_PATH = "assets/blxckoutfx/shaders/blxckout.vsh";
    private static final String POS_COLOR_FRAGMENT_PATH = "assets/blxckoutfx/shaders/posColor.fsh";
    private static final String POS_TEX_FRAGMENT_PATH = "assets/blxckoutfx/shaders/posTex.fsh";
    private static final float RECIPE_BOOK_TEXTURE_SIZE = 256.0F;
    private static final float RECIPE_BOOK_SEARCH_ICON_U1 = 8.0F / RECIPE_BOOK_TEXTURE_SIZE;
    private static final float RECIPE_BOOK_SEARCH_ICON_V1 = 11.0F / RECIPE_BOOK_TEXTURE_SIZE;
    private static final float RECIPE_BOOK_SEARCH_ICON_U2 = 24.0F / RECIPE_BOOK_TEXTURE_SIZE;
    private static final float RECIPE_BOOK_SEARCH_ICON_V2 = 27.0F / RECIPE_BOOK_TEXTURE_SIZE;
    private static final float ANVIL_TEXTURE_SIZE = 256.0F;
    private static final float ANVIL_HAMMER_ICON_U1 = 14.0F / ANVIL_TEXTURE_SIZE;
    private static final float ANVIL_HAMMER_ICON_V1 = 4.0F / ANVIL_TEXTURE_SIZE;
    private static final float ANVIL_HAMMER_ICON_U2 = 48.0F / ANVIL_TEXTURE_SIZE;
    private static final float ANVIL_HAMMER_ICON_V2 = 37.0F / ANVIL_TEXTURE_SIZE;

    private static int presetIndex = 2;

    private static BlxckoutFXShaderProgram posColorProgram;
    private static BlxckoutFXShaderProgram posTexProgram;
    private static boolean shadersLoaded;
    private static boolean shaderLoadFailed;

    static {
        presetIndex = clampPresetIndex(BlxckoutFXClientConfig.getPresetIndex());
    }

    public static void cyclePreset() {
        int oldIndex = presetIndex;
        presetIndex++;

        if (presetIndex >= PRESETS.length) {
            presetIndex = 0;
        }

        LOGGER.info("BlxckoutFX preset cycled: {} -> {} ({})", oldIndex, presetIndex, getCurrentPresetName());
        BlxckoutFXClientConfig.setPresetIndex(presetIndex);
    }

    public static void cyclePresetBackward() {
        int oldIndex = presetIndex;
        presetIndex--;

        if (presetIndex < 0) {
            presetIndex = PRESETS.length - 1;
        }

        LOGGER.info("BlxckoutFX preset cycled backward: {} -> {} ({})", oldIndex, presetIndex, getCurrentPresetName());
        BlxckoutFXClientConfig.setPresetIndex(presetIndex);
    }

    public static void syncPresetIndex(int index) {
        presetIndex = clampPresetIndex(index);
    }

    public static String getCurrentPresetName() {
        return PRESETS[presetIndex].translationKey();
    }

    public static boolean isBlxckoutPresetActive() {
        return BLXCKOUT_PRESET_KEY.equals(PRESETS[presetIndex].translationKey());
    }

    public static boolean isSoftPresetActive() {
        return SOFT_PRESET_KEY.equals(PRESETS[presetIndex].translationKey());
    }

    public static boolean isBalancedPresetActive() {
        return BALANCED_PRESET_KEY.equals(PRESETS[presetIndex].translationKey());
    }

    public static boolean isDarkPresetActive() {
        return DARK_PRESET_KEY.equals(PRESETS[presetIndex].translationKey());
    }

    public static boolean isEnabled() {
        return presetIndex != 0;
    }

    public static float getCurrentPresetDivideFactor() {
        return PRESETS[presetIndex].divideFactor();
    }

    public static float getEffectivePresetDivideFactor() {
        float divideFactor = getCurrentPresetDivideFactor();
        if (BlxckoutFXRenderContext.isBalancedCappedDarkeningActive()) {
            divideFactor = Math.min(divideFactor, getBalancedPresetDivideFactor());
        }
        return divideFactor;
    }

    public static boolean shouldApplyToScreen(GuiScreen screen) {
        return !BlxckoutFXScreenExclusions.isExcludedScreen(screen);
    }

    public static boolean bindForFormat(VertexFormat format) {
        ensureShadersLoaded();
        if (!shadersLoaded || !shouldHandleFormat(format)) {
            return false;
        }

        boolean textured = format.hasUvOffset(0);
        if (!textured && !isBlxckoutPresetActive()) {
            return false;
        }

        BlxckoutFXShaderProgram program = textured ? posTexProgram : posColorProgram;
        program.bind();
        program.setUniform3f("PerceptionScale", PERCEPTION_SCALE_R, PERCEPTION_SCALE_G, PERCEPTION_SCALE_B);
        program.setUniform1f("DivideFactor", getEffectivePresetDivideFactor());
        if (textured) {
            program.setUniform1f("PreserveSaturatedIconColors", BlxckoutFXRenderContext.shouldPreserveIconColors() ? 1.0F : 0.0F);
            program.setUniform1f("LanguageButtonIconRemap", BlxckoutFXRenderContext.shouldRemapLanguageButtonIcon() ? 1.0F : 0.0F);
            program.setUniform1f("RecipeBookButtonIconRemap", BlxckoutFXRenderContext.shouldRemapRecipeBookButtonIcon() ? 1.0F : 0.0F);
            program.setUniform1f("RecipeBookSearchIconRemap", BlxckoutFXRenderContext.shouldRemapRecipeBookSearchIcon() ? 1.0F : 0.0F);
            program.setUniform1f("RecipeBookToggleIconRemap", BlxckoutFXRenderContext.shouldRemapRecipeBookToggleIcon() ? 1.0F : 0.0F);
            program.setUniform1f("AnvilHammerIconRemap", BlxckoutFXRenderContext.shouldRemapAnvilHammerIcon() ? 1.0F : 0.0F);
            program.setUniform1f("BeaconPowerIconRemap", BlxckoutFXRenderContext.shouldRemapBeaconPowerIcon() ? 1.0F : 0.0F);
            program.setUniform1f("Ae2SortButtonIconRemap", BlxckoutFXRenderContext.shouldRemapAe2SortButtonIcon() ? 1.0F : 0.0F);
            program.setUniform4f(
                    "RecipeBookSearchIconUvRect",
                    RECIPE_BOOK_SEARCH_ICON_U1,
                    RECIPE_BOOK_SEARCH_ICON_V1,
                    RECIPE_BOOK_SEARCH_ICON_U2,
                    RECIPE_BOOK_SEARCH_ICON_V2
            );
            program.setUniform4f(
                    "AnvilHammerIconUvRect",
                    ANVIL_HAMMER_ICON_U1,
                    ANVIL_HAMMER_ICON_V1,
                    ANVIL_HAMMER_ICON_U2,
                    ANVIL_HAMMER_ICON_V2
            );
        }
        return true;
    }

    public static void unbind() {
        if (!shadersLoaded) {
            return;
        }
        BlxckoutFXShaderProgram.unbind();
    }

    private static void ensureShadersLoaded() {
        if (shadersLoaded || shaderLoadFailed) {
            return;
        }

        try {
            String vertexSource = readShaderSource(VERTEX_SHADER_PATH);
            posColorProgram = BlxckoutFXShaderProgram.compile(vertexSource, readShaderSource(POS_COLOR_FRAGMENT_PATH));
            posTexProgram = BlxckoutFXShaderProgram.compile(vertexSource, readShaderSource(POS_TEX_FRAGMENT_PATH));

            posTexProgram.bind();
            posTexProgram.setUniform1i("Sampler0", 0);
            BlxckoutFXShaderProgram.unbind();

            shadersLoaded = true;
            LOGGER.info("[BLXCKOUTFX] Darkening shaders compiled successfully");
        } catch (Exception e) {
            shaderLoadFailed = true;
            LOGGER.error("[BLXCKOUTFX] Failed to compile darkening shaders, GUI darkening is disabled", e);
        }
    }

    private static boolean shouldHandleFormat(VertexFormat format) {
        if (format == null || format.hasNormal() || format.hasUvOffset(1)) {
            return false;
        }

        boolean hasPosition = false;
        for (VertexFormatElement element : format.getElements()) {
            VertexFormatElement.EnumUsage usage = element.getUsage();
            if (usage == VertexFormatElement.EnumUsage.POSITION) {
                hasPosition = true;
            } else if (usage != VertexFormatElement.EnumUsage.COLOR && usage != VertexFormatElement.EnumUsage.UV) {
                return false;
            }
        }

        return hasPosition;
    }

    private static String readShaderSource(String path) throws IOException {
        try (InputStream stream = BlxckoutFXShaders.class.getClassLoader().getResourceAsStream(path)) {
            if (stream == null) {
                throw new IOException("Shader resource not found on classpath: " + path);
            }

            StringBuilder builder = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line).append('\n');
                }
            }
            return builder.toString();
        }
    }

    private static int clampPresetIndex(int index) {
        if (index < 0 || index >= PRESETS.length) {
            return 2;
        }

        return index;
    }

    private static float getBalancedPresetDivideFactor() {
        for (ShaderPreset preset : PRESETS) {
            if (BALANCED_PRESET_KEY.equals(preset.translationKey())) {
                return preset.divideFactor();
            }
        }
        return 3.2F;
    }

    private static class ShaderPreset {
        private final String translationKey;
        private final float divideFactor;

        ShaderPreset(String translationKey, float divideFactor) {
            this.translationKey = translationKey;
            this.divideFactor = divideFactor;
        }

        String translationKey() {
            return translationKey;
        }

        float divideFactor() {
            return divideFactor;
        }
    }

    private BlxckoutFXShaders() {
    }
}
