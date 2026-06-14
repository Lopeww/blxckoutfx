package com.lopew.blxckoutfx.client;

import net.minecraft.network.chat.Component;

public final class BlxckoutFXShaders {
    private static final String BLXCKOUT_PRESET_KEY = "preset.blxckoutfx.blxckout";
    private static final String BALANCED_PRESET_KEY = "preset.blxckoutfx.balanced";
    private static final String DARK_PRESET_KEY = "preset.blxckoutfx.dark";

    private static final ShaderPreset[] PRESETS = {
            new ShaderPreset("preset.blxckoutfx.off", 1.0F),
            new ShaderPreset("preset.blxckoutfx.soft", 2.0F),
            new ShaderPreset("preset.blxckoutfx.balanced", 3.2F),
            new ShaderPreset("preset.blxckoutfx.dark", 4.3F),
            new ShaderPreset(BLXCKOUT_PRESET_KEY, 5.5F)
    };

    private static int presetIndex = 2;

    /**
     * When true, item/slot contents are currently being rendered.
     * The shader uniform injection will force DivideFactor=1.0 during this time
     * so items appear at full brightness while panels remain darkened.
     */
    private static volatile boolean itemRenderingActive = false;

    static {
        presetIndex = clampPresetIndex(BlxckoutFXClientConfig.load().getPresetIndex());
    }

    public static void cyclePreset() {
        presetIndex++;

        if (presetIndex >= PRESETS.length) {
            presetIndex = 0;
        }

        BlxckoutFXClientConfig.load().setPresetIndex(presetIndex);
    }

    public static String getCurrentPresetName() {
        return getCurrentPresetComponent().getString();
    }

    public static Component getCurrentPresetComponent() {
        return Component.translatable(PRESETS[presetIndex].translationKey());
    }

    public static boolean isBlxckoutPresetActive() {
        return BLXCKOUT_PRESET_KEY.equals(PRESETS[presetIndex].translationKey());
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

    private static int clampPresetIndex(int index) {
        if (index < 0 || index >= PRESETS.length) {
            return 2;
        }

        return index;
    }

    public static float getCurrentPresetDivideFactor() {
        return PRESETS[presetIndex].divideFactor();
    }

    // ── Item rendering flag ─────────────────────────────────────────────────

    public static boolean isItemRenderingActive() {
        return itemRenderingActive;
    }

    public static void setItemRenderingActive(boolean active) {
        itemRenderingActive = active;
    }

    // ── Shader source for modified fragment shaders ─────────────────────────

    public static String getDarkPositionTexColorShaderSource() {
        return "#version 330\n" +
                "\n" +
                "layout(std140) uniform DynamicTransforms {\n" +
                "    mat4 ModelViewMat;\n" +
                "    vec4 ColorModulator;\n" +
                "    vec3 ModelOffset;\n" +
                "    mat4 TextureMat;\n" +
                "};\n" +
                "\n" +
                "uniform sampler2D Sampler0;\n" +
                "uniform float DivideFactor;\n" +
                "uniform vec3 PerceptionScale;\n" +
                "\n" +
                "in vec2 texCoord0;\n" +
                "in vec4 vertexColor;\n" +
                "\n" +
                "out vec4 fragColor;\n" +
                "\n" +
                "void main() {\n" +
                "    vec4 color = texture(Sampler0, texCoord0) * vertexColor;\n" +
                "    if (color.a == 0.0) {\n" +
                "        discard;\n" +
                "    }\n" +
                "\n" +
                "    float grey = dot(color.rgb, PerceptionScale);\n" +
                "    vec3 dif = abs(color.rgb - vec3(grey));\n" +
                "    vec3 scaled = pow(dif, 1.0 - PerceptionScale);\n" +
                "    float perceivedSaturation = length(scaled);\n" +
                "    color.rgb /= mix(DivideFactor, 1.0, pow(perceivedSaturation, 0.35));\n" +
                "\n" +
                "    fragColor = color * ColorModulator;\n" +
                "}\n";
    }

    public static String getDarkGuiShaderSource() {
        return "#version 330\n" +
                "\n" +
                "layout(std140) uniform DynamicTransforms {\n" +
                "    mat4 ModelViewMat;\n" +
                "    vec4 ColorModulator;\n" +
                "    vec3 ModelOffset;\n" +
                "    mat4 TextureMat;\n" +
                "};\n" +
                "\n" +
                "uniform float DivideFactor;\n" +
                "uniform vec3 PerceptionScale;\n" +
                "\n" +
                "in vec4 vertexColor;\n" +
                "\n" +
                "out vec4 fragColor;\n" +
                "\n" +
                "void main() {\n" +
                "    vec4 color = vertexColor;\n" +
                "    if (color.a == 0.0) {\n" +
                "        discard;\n" +
                "    }\n" +
                "\n" +
                "    float grey = dot(color.rgb, PerceptionScale);\n" +
                "    vec3 dif = abs(color.rgb - vec3(grey));\n" +
                "    vec3 scaled = pow(dif, 1.0 - PerceptionScale);\n" +
                "    float perceivedSaturation = length(scaled);\n" +
                "    color.rgb /= mix(DivideFactor, 1.0, pow(perceivedSaturation, 0.35));\n" +
                "\n" +
                "    fragColor = color * ColorModulator;\n" +
                "}\n";
    }

    private record ShaderPreset(String translationKey, float divideFactor) {}

    private BlxckoutFXShaders() {
    }
}
