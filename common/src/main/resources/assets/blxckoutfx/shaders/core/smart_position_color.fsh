#version 150

uniform vec4 ColorModulator;
uniform vec3 PerceptionScale;
uniform float DivideFactor;

in vec4 vertexColor;
out vec4 fragColor;

void main() {
    vec4 color = vertexColor;

    if (color.a == 0.0) {
        discard;
    }

    float grey = dot(color.rgb, PerceptionScale);
    vec3 dif = abs(color.rgb - vec3(grey));
    vec3 scaled = pow(dif, 1.0 - PerceptionScale);
    float perceivedSaturation = length(scaled);
    color.rgb /= mix(DivideFactor, 1.0, pow(perceivedSaturation, 0.35));

    fragColor = color * ColorModulator;
}
