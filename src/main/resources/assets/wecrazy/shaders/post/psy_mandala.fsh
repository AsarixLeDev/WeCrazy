#version 330
#moj_import <minecraft:globals.glsl>

uniform sampler2D InSampler;

in vec2 texCoord;
out vec4 fragColor;

layout(std140) uniform MandalaUniform {
    float Phase;
    float Petals;
    float RingFreq;
    float Distort;
    float Glow;
};

const float TAU = 6.28318530718;

vec3 palette(float t) {
    return 0.5 + 0.5 * cos(TAU * (t + vec3(0.0, 0.33, 0.67)));
}

void main() {
    vec2 uv = texCoord;
    vec2 p  = uv - vec2(0.5);

    float r = length(p);
    float a = atan(p.y, p.x);

    float spin   = 0.35 * sin(Phase);
    float radial = RingFreq * r * TAU - 2.0 * Phase;

    float petals = 0.5 + 0.5 * cos(Petals * (a + spin) + 2.0 * sin(radial));
    float rings  = 0.5 + 0.5 * cos(radial + 3.0 * petals - Phase);
    float star   = 0.5 + 0.5 * cos(0.5 * Petals * a - 4.0 * Phase + r * 20.0);

    float mask = smoothstep(0.25, 0.95, 0.55 * petals + 0.30 * rings + 0.25 * star);

    vec2 dir  = normalize(p + 1e-6);
    vec2 tang = vec2(-dir.y, dir.x);

    vec2 warped = uv
    + dir  * Distort * 0.030 * (petals - 0.5)
    + tang * Distort * 0.020 * (rings  - 0.5);

    vec3 base = texture(InSampler, warped).rgb;
    vec3 aura = palette(0.10 * petals + 0.12 * rings + 0.05 * sin(Phase));

    base += Glow * mask * aura * (0.5 + 0.5 * rings);
    base *= 1.0 + 0.15 * mask;

    fragColor = vec4(base, 1.0);
}