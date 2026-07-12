#version 150

uniform vec4 ColorModulator;
uniform vec3 PerceptionScale;
uniform float DivideFactor;
uniform float Ae2UpgradePanelBorderMode;

in vec4 vertexColor;
out vec4 fragColor;

bool matchesColorTol(vec3 sampleColor, vec3 expectedColor, float tolerance) {
    return all(lessThanEqual(abs(sampleColor - expectedColor), vec3(tolerance)));
}

void main() {
    vec4 color = vertexColor;

    if (color.a == 0.0) {
        discard;
    }

    if (Ae2UpgradePanelBorderMode > 0.5
            && matchesColorTol(color.rgb, vec3(0.949020, 0.949020, 0.949020), 0.020)) {
        color.rgb = vec3(0.556863, 0.556863, 0.556863); 
        fragColor = color * ColorModulator;
        return;
    }

    float grey = dot(color.rgb, PerceptionScale);
    vec3 dif = abs(color.rgb - vec3(grey));
    vec3 scaled = pow(dif, 1.0 - PerceptionScale);
    float perceivedSaturation = length(scaled);
    color.rgb /= mix(DivideFactor, 1.0, pow(perceivedSaturation, 0.35));

    fragColor = color * ColorModulator;
}
