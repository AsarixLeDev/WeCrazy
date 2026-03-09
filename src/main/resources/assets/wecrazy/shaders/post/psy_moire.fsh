#version 330
#moj_import <minecraft:globals.glsl>

uniform sampler2D InSampler;

in vec2 texCoord;
out vec4 fragColor;

layout(std140) uniform MoireUniform {
    float Phase;
    float Frequency;
    float Warp;
    float Chromatic;
    float Strength;
};

const float TAU = 6.28318530718;

vec3 palette(float t) {
    return 0.5 + 0.5 * cos(TAU * (t + vec3(0.0, 0.33, 0.67)));
}

mat2 rot(float a) {
    float c = cos(a);
    float s = sin(a);
    return mat2(c, -s, s, c);
}

void main() {
    vec2 uv = texCoord;
    vec2 p  = (uv - vec2(0.5)) * 2.0;
    float r = length(p);

    vec2 p1 = rot( Phase)        * p * Frequency;
    vec2 p2 = rot(-0.73 * Phase) * p * (Frequency * 1.17);
    vec2 p3 = rot( 0.41 * Phase) * p * (Frequency * 0.83);

    float g1 = sin(TAU * p1.x) + sin(TAU * p1.y);
    float g2 = sin(TAU * p2.x) + sin(TAU * p2.y);
    float g3 = sin(TAU * (p3.x + p3.y));

    float moire = 0.5 + 0.5 * sin(
    3.5 * (g1 - g2) +
    2.0 * g3 +
    2.0 * cos(Phase + r * 14.0)
    );

    vec2 offset = 0.020 * Warp * vec2(g1 - g2, g2 - g3);
    vec2 warped = uv + offset;

    vec2 dir = normalize(offset + 1e-6);
    vec2 ca  = dir * Chromatic * (0.3 + 0.7 * moire);

    float rr = texture(InSampler, warped + ca).r;
    float gg = texture(InSampler, warped).g;
    float bb = texture(InSampler, warped - ca).b;

    vec3 col = vec3(rr, gg, bb);
    vec3 wave = palette(0.20 * moire + 0.08 * sin(Phase + r * 8.0));

    col = mix(col, col * 0.6 + wave * 1.4, Strength * moire);

    fragColor = vec4(col, 1.0);
}