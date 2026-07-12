#version 120

uniform sampler2D Sampler0;
uniform vec3 PerceptionScale;
uniform float DivideFactor;
uniform float PreserveSaturatedIconColors;
uniform float LanguageButtonIconRemap;
uniform float RecipeBookButtonIconRemap;
uniform float RecipeBookSearchIconRemap;
uniform float RecipeBookToggleIconRemap;
uniform float AnvilHammerIconRemap;
uniform float BeaconPowerIconRemap;
uniform float Ae2SortButtonIconRemap;
uniform vec4 RecipeBookSearchIconUvRect;
uniform vec4 AnvilHammerIconUvRect;

bool matchesColor(vec3 color, vec3 target) {
    return distance(color, target) < 0.018;
}

bool isWithinUvRect(vec2 uv, vec4 rect) {
    return uv.x >= rect.x && uv.x <= rect.z && uv.y >= rect.y && uv.y <= rect.w;
}

bool isLanguageButtonIconColor(vec3 color) {
    return matchesColor(color, vec3(1.0, 1.0, 1.0))
        || matchesColor(color, vec3(0.815686, 0.815686, 0.815686))
        || matchesColor(color, vec3(0.207843, 0.207843, 0.207843))
        || matchesColor(color, vec3(0.137255, 0.192157, 0.627451))
        || matchesColor(color, vec3(0.164706, 0.235294, 0.678431))
        || matchesColor(color, vec3(0.2, 0.282353, 0.72549))
        || matchesColor(color, vec3(0.243137, 0.333333, 0.768627))
        || matchesColor(color, vec3(0.301961, 0.678431, 0.164706))
        || matchesColor(color, vec3(0.352941, 0.72549, 0.2))
        || matchesColor(color, vec3(0.376471, 0.478431, 0.866667))
        || matchesColor(color, vec3(0.407843, 0.768627, 0.243137))
        || matchesColor(color, vec3(0.529412, 0.627451, 0.937255))
        || matchesColor(color, vec3(0.560784, 0.866667, 0.376471))
        || matchesColor(color, vec3(0.788235, 0.819608, 0.980392))
        || matchesColor(color, vec3(0.827451, 0.858824, 0.996078))
        || matchesColor(color, vec3(0.866667, 0.890196, 1.0))
        || matchesColor(color, vec3(0.898039, 0.917647, 1.0));
}

bool isRecipeBookButtonIconColor(vec3 color) {
    return matchesColor(color, vec3(0.047059, 0.019608, 0.0))
        || matchesColor(color, vec3(0.082353, 0.054902, 0.019608))
        || matchesColor(color, vec3(0.105882, 0.211765, 0.105882))
        || matchesColor(color, vec3(0.109804, 0.058824, 0.0))
        || matchesColor(color, vec3(0.129412, 0.078431, 0.019608))
        || matchesColor(color, vec3(0.188235, 0.376471, 0.188235))
        || matchesColor(color, vec3(0.239216, 0.478431, 0.239216))
        || matchesColor(color, vec3(0.278431, 0.556863, 0.278431))
        || matchesColor(color, vec3(0.337255, 0.678431, 0.337255))
        || matchesColor(color, vec3(0.588235, 0.588235, 0.588235))
        || matchesColor(color, vec3(0.835294, 0.835294, 0.835294));
}

bool isRecipeBookSearchIconColor(vec3 color) {
    return !matchesColor(color, vec3(0.215686, 0.215686, 0.215686));
}

bool isRecipeBookToggleIconColor(vec3 color) {
    return matchesColor(color, vec3(0.054902, 0.152941, 0.145098))
        || matchesColor(color, vec3(0.129412, 0.529412, 0.337255))
        || matchesColor(color, vec3(0.137255, 0.372549, 0.356863))
        || matchesColor(color, vec3(0.501961, 0.721569, 0.278431))
        || matchesColor(color, vec3(0.682353, 0.772549, 0.478431))
        || matchesColor(color, vec3(0.278431, 0.227451, 0.137255))
        || matchesColor(color, vec3(0.294118, 0.168627, 0.094118))
        || matchesColor(color, vec3(0.329412, 0.219608, 0.137255))
        || matchesColor(color, vec3(0.45098, 0.368627, 0.223529))
        || matchesColor(color, vec3(0.619608, 0.352941, 0.2))
        || matchesColor(color, vec3(0.623529, 0.517647, 0.301961))
        || matchesColor(color, vec3(0.686275, 0.447059, 0.27451))
        || matchesColor(color, vec3(0.705882, 0.239216, 0.172549));
}

bool isAnvilHammerIconColor(vec3 color) {
    return !matchesColor(color, vec3(0.776471, 0.776471, 0.776471));
}

bool isBeaconPowerIconColor(vec3 color) {
    return !matchesColor(color, vec3(0.129412, 0.129412, 0.129412))
        && !matchesColor(color, vec3(0.286275, 0.286275, 0.286275))
        && !matchesColor(color, vec3(0.215686, 0.215686, 0.215686))
        && !matchesColor(color, vec3(0.176471, 0.176471, 0.176471));
}

void main() {
    vec4 color = texture2D(Sampler0, gl_TexCoord[0].st) * gl_Color;

    float grey = dot(color.rgb, PerceptionScale);
    vec3 dif = abs(color.rgb - vec3(grey));
    vec3 scaled = pow(dif, vec3(1.0) - PerceptionScale);
    float perceivedSaturation = length(scaled);
    if (LanguageButtonIconRemap > 0.5 && isLanguageButtonIconColor(color.rgb)) {
        gl_FragColor = color;
        return;
    }

    if (RecipeBookButtonIconRemap > 0.5 && isRecipeBookButtonIconColor(color.rgb)) {
        gl_FragColor = color;
        return;
    }

    if (RecipeBookSearchIconRemap > 0.5
            && isWithinUvRect(gl_TexCoord[0].st, RecipeBookSearchIconUvRect)
            && isRecipeBookSearchIconColor(color.rgb)) {
        gl_FragColor = color;
        return;
    }

    if (RecipeBookToggleIconRemap > 0.5 && isRecipeBookToggleIconColor(color.rgb)) {
        gl_FragColor = color;
        return;
    }

    if (AnvilHammerIconRemap > 0.5
            && isWithinUvRect(gl_TexCoord[0].st, AnvilHammerIconUvRect)
            && isAnvilHammerIconColor(color.rgb)) {
        gl_FragColor = color;
        return;
    }

    if (BeaconPowerIconRemap > 0.5 && isBeaconPowerIconColor(color.rgb)) {
        gl_FragColor = color;
        return;
    }

    if (Ae2SortButtonIconRemap > 0.5) {
        if (matchesColor(color.rgb, vec3(0.250980, 0.250980, 0.250980))) {
            color.rgb = vec3(0.603922, 0.603922, 0.603922);
            gl_FragColor = color;
            return;
        }
        if (matchesColor(color.rgb, vec3(0.200000, 0.200000, 0.200000))) {
            color.rgb = vec3(0.541176, 0.541176, 0.541176);
            gl_FragColor = color;
            return;
        }
        if (matchesColor(color.rgb, vec3(0.101961, 0.101961, 0.101961))) {
            color.rgb = vec3(0.478431, 0.478431, 0.478431);
            gl_FragColor = color;
            return;
        }
        if (matchesColor(color.rgb, vec3(0.0, 0.0, 0.0))) {
            color.rgb = vec3(0.666667, 0.666667, 0.666667);
        }
        gl_FragColor = color;
        return;
    }

    if (PreserveSaturatedIconColors > 0.5 && perceivedSaturation > 0.18) {
        gl_FragColor = color;
        return;
    }

    color.rgb /= mix(DivideFactor, 1.0, pow(perceivedSaturation, 0.35));

    gl_FragColor = color;
}
