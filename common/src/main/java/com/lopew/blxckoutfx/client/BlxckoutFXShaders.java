package com.lopew.blxckoutfx.client;

import com.lopew.blxckoutfx.BlxckoutFX;
import com.mojang.blaze3d.shaders.AbstractUniform;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;
import java.util.function.Consumer;

public final class BlxckoutFXShaders {
    private static ShaderInstance positionTexShader;
    private static ShaderInstance positionTexColorShader;
    private static ShaderInstance positionColorTexShader;
    private static ShaderInstance positionColorShader;
    private static final String BLXCKOUT_PRESET_KEY = "preset.blxckoutfx.blxckout";
    private static final float PERCEPTION_R = 0.299F;
    private static final float PERCEPTION_G = 0.587F;
    private static final float PERCEPTION_B = 0.114F;

    private static final ShaderPreset[] PRESETS = {
            new ShaderPreset("preset.blxckoutfx.off", 1.0F),
            new ShaderPreset("preset.blxckoutfx.soft", 2.0F),
            new ShaderPreset("preset.blxckoutfx.balanced", 3.2F),
            new ShaderPreset("preset.blxckoutfx.dark", 4.3F),
            new ShaderPreset(BLXCKOUT_PRESET_KEY, 5.5F)
    };

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
                new ResourceLocation(BlxckoutFX.MOD_ID, "smart_position_tex_color"),
                DefaultVertexFormat.POSITION_TEX_COLOR,
                shader -> positionTexColorShader = shader
        );

        registrar.register(
                new ResourceLocation(BlxckoutFX.MOD_ID, "smart_position_color_tex"),
                DefaultVertexFormat.POSITION_COLOR_TEX,
                shader -> positionColorTexShader = shader
        );

        registrar.register(
                new ResourceLocation(BlxckoutFX.MOD_ID, "smart_position_color"),
                DefaultVertexFormat.POSITION_COLOR,
                shader -> positionColorShader = shader
        );
    }

    public static void applyCurrentPreset(ShaderInstance shader) {
        if (shader == null) {
            return;
        }

        ShaderPreset preset = PRESETS[presetIndex];

        setUniform(shader, "PerceptionScale", PERCEPTION_R, PERCEPTION_G, PERCEPTION_B);
        setUniform(shader, "DivideFactor", preset.divideFactor());
    }

    public static void cyclePreset() {
        presetIndex++;

        if (presetIndex >= PRESETS.length) {
            presetIndex = 0;
        }

        BlxckoutFXClientConfig.load().setPresetIndex(presetIndex);
    }

    public static Component getCurrentPresetComponent() {
        return Component.translatable(PRESETS[presetIndex].translationKey());
    }

    public static boolean isBlxckoutPresetActive() {
        return BLXCKOUT_PRESET_KEY.equals(PRESETS[presetIndex].translationKey());
    }

    public static boolean isEnabled() {
        return presetIndex != 0;
    }

    public static int getPresetIndex() {
        return presetIndex;
    }

    public static float getCurrentDivideFactor() {
        return PRESETS[presetIndex].divideFactor();
    }

    private static void setUniform(ShaderInstance shader, String name, float value) {
        AbstractUniform uniform = shader.safeGetUniform(name);
        uniform.set(value);
    }

    private static void setUniform(ShaderInstance shader, String name, float x, float y, float z) {
        AbstractUniform uniform = shader.safeGetUniform(name);
        uniform.set(x, y, z);
    }

    private static int clampPresetIndex(int index) {
        if (index < 0 || index >= PRESETS.length) {
            return 2;
        }

        return index;
    }

    private record ShaderPreset(
            String translationKey,
            float divideFactor
    ) {
    }

    @FunctionalInterface
    public interface ShaderRegistrar {
        void register(ResourceLocation id, VertexFormat vertexFormat, Consumer<ShaderInstance> loadCallback) throws IOException;
    }

    public static ShaderInstance getPositionTexShader() {
        return positionTexShader;
    }

    public static ShaderInstance getPositionTexColorShader() {
        return positionTexColorShader;
    }

    public static ShaderInstance getPositionColorTexShader() {
        return positionColorTexShader;
    }

    public static ShaderInstance getPositionColorShader() {
        return positionColorShader;
    }

    private BlxckoutFXShaders() {
    }
}
