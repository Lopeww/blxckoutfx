package com.lopew.blxckoutfx.client.render;

import com.lopew.blxckoutfx.BlxckoutFX;
import com.lopew.blxckoutfx.client.config.BlxckoutFXClientConfig;
import com.lopew.blxckoutfx.client.integration.ae2.BlxckoutFXAe2Support;
import com.lopew.blxckoutfx.client.screen.BlxckoutFXScreenExclusions;
import com.mojang.blaze3d.shaders.AbstractUniform;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;
import java.util.function.Consumer;

public final class BlxckoutFXShaders {
    private static final float PERCEPTION_R = 0.299F;
    private static final float PERCEPTION_G = 0.587F;
    private static final float PERCEPTION_B = 0.114F;
    private static final String BLXCKOUT_PRESET_KEY = "preset.blxckoutfx.blxckout";
    private static final float JEI_WIDGET_MAX_DIVIDE_FACTOR = 2.35F;
    private static final float BALANCED_MAX_DIVIDE_FACTOR = 3.2F;

    private static final ShaderPreset[] PRESETS = {
            new ShaderPreset("preset.blxckoutfx.off", 1.0F, PERCEPTION_R, PERCEPTION_G, PERCEPTION_B),
            new ShaderPreset("preset.blxckoutfx.soft", 2.0F, PERCEPTION_R, PERCEPTION_G, PERCEPTION_B),
            new ShaderPreset("preset.blxckoutfx.balanced", 3.2F, PERCEPTION_R, PERCEPTION_G, PERCEPTION_B),
            new ShaderPreset("preset.blxckoutfx.dark", 4.3F, PERCEPTION_R, PERCEPTION_G, PERCEPTION_B),
            new ShaderPreset(BLXCKOUT_PRESET_KEY, 5.5F, PERCEPTION_R, PERCEPTION_G, PERCEPTION_B)
    };

    private static ShaderInstance positionTexShader;
    private static ShaderInstance positionTexColorShader;
    private static ShaderInstance positionColorShader;
    private static ShaderInstance blxckoutPositionTexShader;
    private static ShaderInstance blxckoutPositionTexColorShader;
    private static ShaderInstance blxckoutPositionColorShader;
    private static int presetIndex = 2;

    static {
        presetIndex = clampPresetIndex(BlxckoutFXClientConfig.load().getPresetIndex());
    }

    public static void registerShaders(ShaderRegistrar registrar) throws IOException {
        registrar.register(
                new ResourceLocation(BlxckoutFX.MOD_ID, "smart_position_tex"),
                DefaultVertexFormat.POSITION_TEX,
                shader -> positionTexShader = shader
        );
        registrar.register(
                new ResourceLocation(BlxckoutFX.MOD_ID, "blxckout_smart_position_tex"),
                DefaultVertexFormat.POSITION_TEX,
                shader -> blxckoutPositionTexShader = shader
        );
        registrar.register(
                new ResourceLocation(BlxckoutFX.MOD_ID, "smart_position_tex_color"),
                DefaultVertexFormat.POSITION_TEX_COLOR,
                shader -> positionTexColorShader = shader
        );
        registrar.register(
                new ResourceLocation(BlxckoutFX.MOD_ID, "blxckout_smart_position_tex_color"),
                DefaultVertexFormat.POSITION_TEX_COLOR,
                shader -> blxckoutPositionTexColorShader = shader
        );
        registrar.register(
                new ResourceLocation(BlxckoutFX.MOD_ID, "smart_position_color"),
                DefaultVertexFormat.POSITION_COLOR,
                shader -> positionColorShader = shader
        );
        registrar.register(
                new ResourceLocation(BlxckoutFX.MOD_ID, "blxckout_smart_position_color"),
                DefaultVertexFormat.POSITION_COLOR,
                shader -> blxckoutPositionColorShader = shader
        );
    }

    public static void applyCurrentPreset(ShaderInstance shader) {
        if (shader == null) {
            return;
        }

        ShaderPreset preset = getActivePresetForCurrentContext();
        setUniform(shader, "PerceptionScale", preset.perceptionR(), preset.perceptionG(), preset.perceptionB());
        setUniform(shader, "DivideFactor", getCurrentRenderDivideFactor(preset.divideFactor()));
        setUniform(shader, "Ae2ButtonPaletteMode", BlxckoutFXRenderContext.getAe2ButtonPaletteMode());
        setUniform(shader, "Ae2GlobalRemapMode", getAe2GlobalRemapMode());
        setUniform(
                shader,
                "Ae2IconRenderMode",
                BlxckoutFXRenderContext.isRenderingAe2BrightenIcon()
                        ? 3.0F
                        : BlxckoutFXRenderContext.isRenderingAe2PowerUnitIcon()
                                || BlxckoutFXRenderContext.isRenderingAe2ForceWhiteIcon()
                        ? 2.0F
                        : BlxckoutFXRenderContext.isRenderingAe2Icon() ? 1.0F : 0.0F
        );
        setUniform(shader, "Ae2TransferButtonMode", BlxckoutFXRenderContext.isRenderingAe2TransferButton() ? 1.0F : 0.0F);
        setUniform(
                shader,
                "Ae2TransferButtonRect",
                BlxckoutFXRenderContext.getAe2TransferButtonX1(),
                BlxckoutFXRenderContext.getAe2TransferButtonY1(),
                BlxckoutFXRenderContext.getAe2TransferButtonX2(),
                BlxckoutFXRenderContext.getAe2TransferButtonY2()
        );
        setUniform(
                shader,
                "Ae2SortButtonRect",
                BlxckoutFXRenderContext.getAe2SortButtonX1(),
                BlxckoutFXRenderContext.getAe2SortButtonY1(),
                BlxckoutFXRenderContext.getAe2SortButtonX2(),
                BlxckoutFXRenderContext.getAe2SortButtonY2()
        );
        setUniform(shader, "Ae2SortButtonMode", BlxckoutFXRenderContext.isRenderingAe2SortButton() ? 1.0F : 0.0F);
        setUniform(
                shader,
                "Ae2SchedulingButtonRect",
                BlxckoutFXRenderContext.getAe2SchedulingButtonX1(),
                BlxckoutFXRenderContext.getAe2SchedulingButtonY1(),
                BlxckoutFXRenderContext.getAe2SchedulingButtonX2(),
                BlxckoutFXRenderContext.getAe2SchedulingButtonY2()
        );
        setUniform(shader, "Ae2SchedulingButtonMode", BlxckoutFXRenderContext.isRenderingAe2SchedulingButton() ? 1.0F : 0.0F);
        setUniform(
                shader,
                "Ae2ScrollTrackRect",
                BlxckoutFXRenderContext.getAe2ScrollbarTrackX1(),
                BlxckoutFXRenderContext.getAe2ScrollbarTrackY1(),
                BlxckoutFXRenderContext.getAe2ScrollbarTrackX2(),
                BlxckoutFXRenderContext.getAe2ScrollbarTrackY2()
        );
        setUniform(
                shader,
                "Ae2CraftingSectionRect",
                BlxckoutFXRenderContext.getAe2CraftingTerminalSectionX1(),
                BlxckoutFXRenderContext.getAe2CraftingTerminalSectionY1(),
                BlxckoutFXRenderContext.getAe2CraftingTerminalSectionX2(),
                BlxckoutFXRenderContext.getAe2CraftingTerminalSectionY2()
        );
        setUniform(
                shader,
                "Ae2MolecularAssemblerGridRect",
                BlxckoutFXRenderContext.getAe2MolecularAssemblerGridX1(),
                BlxckoutFXRenderContext.getAe2MolecularAssemblerGridY1(),
                BlxckoutFXRenderContext.getAe2MolecularAssemblerGridX2(),
                BlxckoutFXRenderContext.getAe2MolecularAssemblerGridY2()
        );
        setUniform(shader, "Ae2UpgradePanelBorderMode", getAe2UpgradePanelBorderMode());
        setUniform(shader, "VanillaIconButtonMode", BlxckoutFXRenderContext.getVanillaIconButtonMode());
        setUniform(
                shader,
                "VanillaIconButtonRect",
                BlxckoutFXRenderContext.getVanillaIconButtonX1(),
                BlxckoutFXRenderContext.getVanillaIconButtonY1(),
                BlxckoutFXRenderContext.getVanillaIconButtonX2(),
                BlxckoutFXRenderContext.getVanillaIconButtonY2()
        );
        setUniform(shader, "PneumaticColorWhitelistEnabled", BlxckoutFXRenderContext.getPneumaticColorWhitelistEnabled());
        setUniform(shader, "PneumaticColorWhitelistCount", BlxckoutFXRenderContext.getPneumaticColorWhitelistCount());
        setUniform(shader, "PneumaticColorWhitelist0", getPneumaticWhitelistR(0), getPneumaticWhitelistG(0), getPneumaticWhitelistB(0));
        setUniform(shader, "PneumaticColorWhitelist1", getPneumaticWhitelistR(1), getPneumaticWhitelistG(1), getPneumaticWhitelistB(1));
        setUniform(shader, "PneumaticColorWhitelist2", getPneumaticWhitelistR(2), getPneumaticWhitelistG(2), getPneumaticWhitelistB(2));
        setUniform(shader, "PneumaticColorWhitelist3", getPneumaticWhitelistR(3), getPneumaticWhitelistG(3), getPneumaticWhitelistB(3));
        setUniform(shader, "PneumaticColorWhitelist4", getPneumaticWhitelistR(4), getPneumaticWhitelistG(4), getPneumaticWhitelistB(4));
        setUniform(shader, "PneumaticColorWhitelist5", getPneumaticWhitelistR(5), getPneumaticWhitelistG(5), getPneumaticWhitelistB(5));
        setUniform(shader, "PneumaticColorWhitelist6", getPneumaticWhitelistR(6), getPneumaticWhitelistG(6), getPneumaticWhitelistB(6));
        setUniform(shader, "PneumaticColorWhitelist7", getPneumaticWhitelistR(7), getPneumaticWhitelistG(7), getPneumaticWhitelistB(7));
        setUniform(shader, "PneumaticColorWhitelist8", getPneumaticWhitelistR(8), getPneumaticWhitelistG(8), getPneumaticWhitelistB(8));
        setUniform(shader, "PneumaticColorWhitelist9", getPneumaticWhitelistR(9), getPneumaticWhitelistG(9), getPneumaticWhitelistB(9));
        setUniform(shader, "PneumaticColorWhitelist10", getPneumaticWhitelistR(10), getPneumaticWhitelistG(10), getPneumaticWhitelistB(10));
        setUniform(shader, "PneumaticColorWhitelist11", getPneumaticWhitelistR(11), getPneumaticWhitelistG(11), getPneumaticWhitelistB(11));
    }

    public static void cyclePreset() {
        cyclePreset(1);
    }

    public static void cyclePresetBackward() {
        cyclePreset(-1);
    }

    public static boolean isEnabled() {
        return presetIndex != 0;
    }

    public static boolean isBlxckoutPresetActive() {
        return BLXCKOUT_PRESET_KEY.equals(PRESETS[presetIndex].translationKey());
    }

    public static boolean isSoftPresetActive() {
        return "preset.blxckoutfx.soft".equals(PRESETS[presetIndex].translationKey());
    }

    public static float getPresetDivideFactor(int index) {
        return PRESETS[clampPresetIndex(index)].divideFactor();
    }

    public static float getCurrentPresetDivideFactor() {
        return PRESETS[presetIndex].divideFactor();
    }

    public static boolean shouldApplyToScreen(net.minecraft.client.gui.screens.Screen screen) {
        return !BlxckoutFXScreenExclusions.isExcludedScreen(screen);
    }

    public static void setPresetIndex(int index) {
        syncPresetIndex(index);
        BlxckoutFXClientConfig.load().setPresetIndex(presetIndex);
    }

    public static void syncPresetIndex(int index) {
        presetIndex = clampPresetIndex(index);
    }

    public static Component getCurrentPresetComponent() {
        return Component.translatable(PRESETS[presetIndex].translationKey());
    }

    public static ShaderInstance getPositionTexShader() {
        return positionTexShader;
    }

    public static ShaderInstance getPositionTexColorShader() {
        return positionTexColorShader;
    }

    public static ShaderInstance getPositionColorShader() {
        return positionColorShader;
    }

    public static ShaderInstance getBlxckoutPositionTexShader() {
        return blxckoutPositionTexShader;
    }

    public static ShaderInstance getBlxckoutPositionTexColorShader() {
        return blxckoutPositionTexColorShader;
    }

    public static ShaderInstance getBlxckoutPositionColorShader() {
        return blxckoutPositionColorShader;
    }

    private static float getCurrentRenderDivideFactor(float divideFactor) {
        if (BlxckoutFXRenderContext.isRenderingAe2Screen()
                && !BlxckoutFXRenderContext.isRenderingInventoryButton()
                && !BlxckoutFXAe2Support.isAe2ThemeEnabled()
                && BlxckoutFXRenderContext.isRenderingAe2Texture()) {
            return 1.0F;
        }

        if (BlxckoutFXRenderContext.isRenderingJeiWidget()) {
            return Math.min(divideFactor, JEI_WIDGET_MAX_DIVIDE_FACTOR);
        }

        if (BlxckoutFXRenderContext.isBalancedCappedDarkeningActive()) {
            return Math.min(divideFactor, BALANCED_MAX_DIVIDE_FACTOR);
        }
        return divideFactor;
    }

    private static float getAe2GlobalRemapMode() {
        return 0.0F;
    }

    private static float getAe2UpgradePanelBorderMode() {
        return 0.0F;
    }

    private static float getPneumaticWhitelistR(int index) {
        return BlxckoutFXRenderContext.getPneumaticColorWhitelistR(index);
    }

    private static float getPneumaticWhitelistG(int index) {
        return BlxckoutFXRenderContext.getPneumaticColorWhitelistG(index);
    }

    private static float getPneumaticWhitelistB(int index) {
        return BlxckoutFXRenderContext.getPneumaticColorWhitelistB(index);
    }

    private static ShaderPreset getActivePresetForCurrentContext() {
        return PRESETS[presetIndex];
    }

    private static void cyclePreset(int delta) {
        int nextIndex = presetIndex + delta;
        while (nextIndex < 0) {
            nextIndex += PRESETS.length;
        }
        while (nextIndex >= PRESETS.length) {
            nextIndex -= PRESETS.length;
        }

        setPresetIndex(nextIndex);
    }

    private static void setUniform(ShaderInstance shader, String name, float value) {
        AbstractUniform uniform = shader.safeGetUniform(name);
        if (uniform != null) {
            uniform.set(value);
        }
    }

    private static void setUniform(ShaderInstance shader, String name, float x, float y, float z) {
        AbstractUniform uniform = shader.safeGetUniform(name);
        if (uniform != null) {
            uniform.set(x, y, z);
        }
    }

    private static void setUniform(ShaderInstance shader, String name, float x, float y, float z, float w) {
        AbstractUniform uniform = shader.safeGetUniform(name);
        if (uniform != null) {
            uniform.set(x, y, z, w);
        }
    }

    private static int clampPresetIndex(int index) {
        if (index < 0 || index >= PRESETS.length) {
            return 2;
        }
        return index;
    }

    private record ShaderPreset(
            String translationKey,
            float divideFactor,
            float perceptionR,
            float perceptionG,
            float perceptionB
    ) {
    }

    @FunctionalInterface
    public interface ShaderRegistrar {
        void register(ResourceLocation id, VertexFormat vertexFormat, Consumer<ShaderInstance> loadCallback) throws IOException;
    }

    private BlxckoutFXShaders() {
    }
}
