#version 120

uniform vec3 PerceptionScale;
uniform float DivideFactor;

void main() {
    vec4 color = gl_Color;

    float grey = dot(color.rgb, PerceptionScale);
    vec3 dif = abs(color.rgb - vec3(grey));
    vec3 scaled = pow(dif, vec3(1.0) - PerceptionScale);
    float perceivedSaturation = length(scaled);
    color.rgb /= mix(DivideFactor, 1.0, pow(perceivedSaturation, 0.35));

    gl_FragColor = color;
}
