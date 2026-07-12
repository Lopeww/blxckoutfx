package com.lopew.blxckoutfx.client.render;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import com.lopew.blxckoutfx.client.gui.BlxckoutFXGUIHandler;
import com.lopew.blxckoutfx.client.screen.BlxckoutFXScreenExclusions;

public final class BlxckoutFXShaders {
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

    private static int presetIndex = 2;

    public static void cyclePreset() {
        int next = presetIndex + 1;
        if (next >= PRESETS.length) {
            next = 0;
        }

        presetIndex = clampPresetIndex(next);
        BlxckoutFXGUIHandler.getConfig().setPresetIndex(presetIndex);
    }

    public static void cyclePresetBackward() {
        int next = presetIndex - 1;
        if (next < 0) {
            next = PRESETS.length - 1;
        }

        presetIndex = clampPresetIndex(next);
        BlxckoutFXGUIHandler.getConfig().setPresetIndex(presetIndex);
    }

    public static void syncPresetIndex(int index) {
        presetIndex = clampPresetIndex(index);
    }

    public static String getCurrentPresetName() {
        return getCurrentPresetComponent().getString();
    }

    public static int getCurrentPresetIndex() {
        return presetIndex;
    }

    public static Component getCurrentPresetComponent() {
        return Component.translatable(PRESETS[presetIndex].translationKey());
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

    public static boolean shouldApplyToScreen(Screen screen) {
        return BlxckoutFXScreenExclusions.isFullScreen(screen);
    }

    public static String getDarkPositionTexShaderSource() {
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
                "uniform float Ae2CustomRemapMode;\n" +
                "\n" +
                "in vec2 texCoord0;\n" +
                "\n" +
                "out vec4 fragColor;\n" +
                "\n" +
                "bool matchesColorTol(vec3 sampleColor, vec3 expectedColor, float tolerance) {\n" +
                "    return all(lessThanEqual(abs(sampleColor - expectedColor), vec3(tolerance)));\n" +
                "}\n" +
                "\n" +
                "vec3 applyAe2CustomRemap(vec3 rgb) {\n" +
                "    if (matchesColorTol(rgb, vec3(0.254902, 0.247059, 0.329412), 0.020)) {\n" +
                "        return vec3(0.039216, 0.035294, 0.054902);\n" +
                "    }\n" +
                "    if (matchesColorTol(rgb, vec3(0.529412, 0.560784, 0.647059), 0.020)) {\n" +
                "        return vec3(0.113725, 0.125490, 0.160784);\n" +
                "    }\n" +
                "    if (matchesColorTol(rgb, vec3(0.949020, 0.949020, 0.949020), 0.020)) {\n" +
                "        return vec3(0.556863, 0.556863, 0.556863);\n" +
                "    }\n" +
                "    if (matchesColorTol(rgb, vec3(0.796078, 0.800000, 0.831373), 0.020)) {\n" +
                "        return vec3(0.258824, 0.262745, 0.294118);\n" +
                "    }\n" +
                "    if (matchesColorTol(rgb, vec3(0.678431, 0.690196, 0.768627), 0.020)) {\n" +
                "        return vec3(0.176471, 0.184314, 0.235294);\n" +
                "    }\n" +
                "    if (matchesColorTol(rgb, vec3(0.603922, 0.623529, 0.705882), 0.020)) {\n" +
                "        return vec3(0.141176, 0.149020, 0.192157);\n" +
                "    }\n" +
                "    if (matchesColorTol(rgb, vec3(0.411765, 0.427451, 0.533333), 0.020)) {\n" +
                "        return vec3(0.078431, 0.082353, 0.113725);\n" +
                "    }\n" +
                "    if (matchesColorTol(rgb, vec3(0.301961, 0.301961, 0.403922), 0.020)) {\n" +
                "        return vec3(0.050980, 0.050980, 0.074510);\n" +
                "    }\n" +
                "    return rgb;\n" +
                "}\n" +
                "\n" +
                "void main() {\n" +
                "    vec4 color = texture(Sampler0, texCoord0);\n" +
                "    if (color.a == 0.0) {\n" +
                "        discard;\n" +
                "    }\n" +
                "\n" +
                "    if (Ae2CustomRemapMode > 0.5) {\n" +
                "        color.rgb = applyAe2CustomRemap(color.rgb);\n" +
                "        fragColor = color * ColorModulator;\n" +
                "        return;\n" +
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
                "uniform float Ae2CustomRemapMode;\n" +
                "uniform vec4 Ae2ConfigArrowRect;\n" +
                "uniform vec4 Ae2ConfigArrowSecondRect;\n" +
                "\n" +
                "in vec2 texCoord0;\n" +
                "in vec4 vertexColor;\n" +
                "\n" +
                "out vec4 fragColor;\n" +
                "\n" +
                "bool matchesColorTol(vec3 a, vec3 b, float tol) {\n" +
                "    vec3 diff = abs(a - b);\n" +
                "    return diff.r <= tol && diff.g <= tol && diff.b <= tol;\n" +
                "}\n" +
                "\n" +
                "vec3 applyAe2CustomRemap(vec3 rgb) {\n" +
                "    if (matchesColorTol(rgb, vec3(0.254902, 0.247059, 0.329412), 0.020)) {\n" +
                "        return vec3(0.039216, 0.035294, 0.054902);\n" +
                "    }\n" +
                "    if (matchesColorTol(rgb, vec3(0.529412, 0.560784, 0.647059), 0.020)) {\n" +
                "        return vec3(0.113725, 0.125490, 0.160784);\n" +
                "    }\n" +
                "    if (matchesColorTol(rgb, vec3(0.949020, 0.949020, 0.949020), 0.020)) {\n" +
                "        return vec3(0.556863, 0.556863, 0.556863);\n" +
                "    }\n" +
                "    if (matchesColorTol(rgb, vec3(0.796078, 0.800000, 0.831373), 0.020)) {\n" +
                "        return vec3(0.258824, 0.262745, 0.294118);\n" +
                "    }\n" +
                "    if (matchesColorTol(rgb, vec3(0.678431, 0.690196, 0.768627), 0.020)) {\n" +
                "        return vec3(0.176471, 0.184314, 0.235294);\n" +
                "    }\n" +
                "    if (matchesColorTol(rgb, vec3(0.603922, 0.623529, 0.705882), 0.020)) {\n" +
                "        return vec3(0.141176, 0.149020, 0.192157);\n" +
                "    }\n" +
                "    if (matchesColorTol(rgb, vec3(0.411765, 0.427451, 0.533333), 0.020)) {\n" +
                "        return vec3(0.078431, 0.082353, 0.113725);\n" +
                "    }\n" +
                "    if (matchesColorTol(rgb, vec3(0.301961, 0.301961, 0.403922), 0.020)) {\n" +
                "        return vec3(0.050980, 0.050980, 0.074510);\n" +
                "    }\n" +
                "    return rgb;\n" +
                "}\n" +
                "\n" +
                "void main() {\n" +
                "    vec4 color = texture(Sampler0, texCoord0) * vertexColor;\n" +
                "    if (color.a == 0.0) {\n" +
                "        discard;\n" +
                "    }\n" +
                "\n" +
                "    if (Ae2CustomRemapMode > 0.5) {\n" +
                "        color.rgb = applyAe2CustomRemap(color.rgb);\n" +
                "        fragColor = color * ColorModulator;\n" +
                "        return;\n" +
                "    }\n" +
                "\n" +
                "    bool inFirstArrowRect = Ae2ConfigArrowRect.z > Ae2ConfigArrowRect.x && Ae2ConfigArrowRect.w > Ae2ConfigArrowRect.y\n" +
                "            && gl_FragCoord.x >= Ae2ConfigArrowRect.x && gl_FragCoord.x < Ae2ConfigArrowRect.z\n" +
                "            && gl_FragCoord.y >= Ae2ConfigArrowRect.y && gl_FragCoord.y < Ae2ConfigArrowRect.w;\n" +
                "    bool inSecondArrowRect = Ae2ConfigArrowSecondRect.z > Ae2ConfigArrowSecondRect.x && Ae2ConfigArrowSecondRect.w > Ae2ConfigArrowSecondRect.y\n" +
                "            && gl_FragCoord.x >= Ae2ConfigArrowSecondRect.x && gl_FragCoord.x < Ae2ConfigArrowSecondRect.z\n" +
                "            && gl_FragCoord.y >= Ae2ConfigArrowSecondRect.y && gl_FragCoord.y < Ae2ConfigArrowSecondRect.w;\n" +
                "    if (inFirstArrowRect || inSecondArrowRect) {\n" +
                "        if (matchesColorTol(color.rgb, vec3(0.949020, 0.949020, 0.949020), 0.003)) {\n" +
                "            color.rgb = vec3(0.556863, 0.556863, 0.556863);\n" +
                "            fragColor = color * ColorModulator;\n" +
                "            return;\n" +
                "        }\n" +
                "        if (matchesColorTol(color.rgb, vec3(0.411765, 0.427451, 0.533333), 0.003)) {\n" +
                "            color.rgb = vec3(0.227451, 0.227451, 0.227451);\n" +
                "            fragColor = color * ColorModulator;\n" +
                "            return;\n" +
                "        }\n" +
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

    public static String getCleanPositionTexColorShaderSource() {
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
                "    fragColor = color * ColorModulator;\n" +
                "}\n";
    }

    public static String getModMenuIconButtonPositionTexColorShaderSource() {
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
                "bool matchesColor(vec3 sampleColor, vec3 expectedColor) {\n" +
                "    return all(lessThanEqual(abs(sampleColor - expectedColor), vec3(0.003922)));\n" +
                "}\n" +
                "\n" +
                "void main() {\n" +
                "    vec4 color = texture(Sampler0, texCoord0) * vertexColor;\n" +
                "    if (color.a == 0.0) {\n" +
                "        discard;\n" +
                "    }\n" +
                "\n" +
                "    bool preserveIcon = matchesColor(color.rgb, vec3(1.0))\n" +
                "            || matchesColor(color.rgb, vec3(0.925490))\n" +
                "            || matchesColor(color.rgb, vec3(0.870588))\n" +
                "            || matchesColor(color.rgb, vec3(0.772549));\n" +
                "    if (!preserveIcon) {\n" +
                "        float grey = dot(color.rgb, PerceptionScale);\n" +
                "        vec3 dif = abs(color.rgb - vec3(grey));\n" +
                "        vec3 scaled = pow(dif, 1.0 - PerceptionScale);\n" +
                "        float perceivedSaturation = length(scaled);\n" +
                "        color.rgb /= mix(DivideFactor, 1.0, pow(perceivedSaturation, 0.35));\n" +
                "    }\n" +
                "\n" +
                "    fragColor = color * ColorModulator;\n" +
                "}\n";
    }

    public static String getAe2BlackoutPositionTexColorShaderSource() {
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
                "\n" +
                "in vec2 texCoord0;\n" +
                "in vec4 vertexColor;\n" +
                "\n" +
                "out vec4 fragColor;\n" +
                "\n" +
                "bool matchesColorTol(vec3 sampleColor, vec3 expectedColor, float tolerance) {\n" +
                "    return all(lessThanEqual(abs(sampleColor - expectedColor), vec3(tolerance)));\n" +
                "}\n" +
                "\n" +
                "vec3 applyAe2BlackoutRemap(vec3 rgb) {\n" +
                "    if (matchesColorTol(rgb, vec3(0.254902, 0.247059, 0.329412), 0.020)) {\n" +
                "        return vec3(0.039216, 0.035294, 0.054902);\n" +
                "    }\n" +
                "    if (matchesColorTol(rgb, vec3(0.529412, 0.560784, 0.647059), 0.020)) {\n" +
                "        return vec3(0.113725, 0.125490, 0.160784);\n" +
                "    }\n" +
                "    if (matchesColorTol(rgb, vec3(0.949020, 0.949020, 0.949020), 0.020)) {\n" +
                "        return vec3(0.556863, 0.556863, 0.556863);\n" +
                "    }\n" +
                "    if (matchesColorTol(rgb, vec3(0.796078, 0.800000, 0.831373), 0.020)) {\n" +
                "        return vec3(0.258824, 0.262745, 0.294118);\n" +
                "    }\n" +
                "    if (matchesColorTol(rgb, vec3(0.678431, 0.690196, 0.768627), 0.020)) {\n" +
                "        return vec3(0.176471, 0.184314, 0.235294);\n" +
                "    }\n" +
                "    if (matchesColorTol(rgb, vec3(0.603922, 0.623529, 0.705882), 0.020)) {\n" +
                "        return vec3(0.141176, 0.149020, 0.192157);\n" +
                "    }\n" +
                "    if (matchesColorTol(rgb, vec3(0.411765, 0.427451, 0.533333), 0.020)) {\n" +
                "        return vec3(0.078431, 0.082353, 0.113725);\n" +
                "    }\n" +
                "    if (matchesColorTol(rgb, vec3(0.301961, 0.301961, 0.403922), 0.020)) {\n" +
                "        return vec3(0.050980, 0.050980, 0.074510);\n" +
                "    }\n" +
                "    return rgb;\n" +
                "}\n" +
                "\n" +
                "void main() {\n" +
                "    vec4 color = texture(Sampler0, texCoord0) * vertexColor;\n" +
                "    if (color.a == 0.0) {\n" +
                "        discard;\n" +
                "    }\n" +
                "\n" +
                "    color.rgb = applyAe2BlackoutRemap(color.rgb);\n" +
                "    fragColor = color * ColorModulator;\n" +
                "}\n";
    }

    public static String getAe2TransferButtonPositionTexColorShaderSource() {
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
                "\n" +
                "in vec2 texCoord0;\n" +
                "in vec4 vertexColor;\n" +
                "\n" +
                "out vec4 fragColor;\n" +
                "\n" +
                "bool matchesColorTol(vec3 sampleColor, vec3 expectedColor, float tolerance) {\n" +
                "    return all(lessThanEqual(abs(sampleColor - expectedColor), vec3(tolerance)));\n" +
                "}\n" +
                "\n" +
                "vec3 applyAe2TransferButtonRemap(vec3 rgb) {\n" +
                "    if (matchesColorTol(rgb, vec3(0.301961, 0.301961, 0.403922), 0.020)) {\n" +
                "        return vec3(0.050980, 0.050980, 0.074510);\n" +
                "    }\n" +
                "    if (matchesColorTol(rgb, vec3(0.411765, 0.427451, 0.533333), 0.020)) {\n" +
                "        return vec3(0.078431, 0.082353, 0.113725);\n" +
                "    }\n" +
                "    return rgb;\n" +
                "}\n" +
                "\n" +
                "void main() {\n" +
                "    vec4 color = texture(Sampler0, texCoord0) * vertexColor;\n" +
                "    if (color.a == 0.0) {\n" +
                "        discard;\n" +
                "    }\n" +
                "\n" +
                "    color.rgb = applyAe2TransferButtonRemap(color.rgb);\n" +
                "    fragColor = color * ColorModulator;\n" +
                "}\n";
    }

    private static int clampPresetIndex(int index) {
        if (index < 0 || index >= PRESETS.length) {
            return 2;
        }

        return index;
    }

    private record ShaderPreset(String translationKey, float divideFactor) {}

    private BlxckoutFXShaders() {
    }
}
