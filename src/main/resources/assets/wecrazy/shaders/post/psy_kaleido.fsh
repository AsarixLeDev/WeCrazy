#version 330
#moj_import <minecraft:globals.glsl>

uniform sampler2D InSampler;

in vec2 texCoord;
out vec4 fragColor;

layout(std140) uniform KaleidoUniform {
    float Phase;
    float Segments;
    float Twist;
    float Warp;
    float Chromatic;
    float Glow;
};

const float TAU = 6.28318530718;

vec3 palette(float t) {
    return 0.5 + 0.5 * cos(TAU * (t + vec3(0.0, 0.33, 0.67)));
}

vec3 sampleChromatic(vec2 uv, vec2 dir, float amt) {
    vec2 ca = dir * amt;
    return vec3(
    texture(InSampler, uv + ca).r,
    texture(InSampler, uv).g,
    texture(InSampler, uv - ca).b
    );
}

void main() {
    vec2 uv = texCoord;
    vec2 p  = uv - vec2(0.5);

    float r = length(p);
    float a = atan(p.y, p.x);

    float seg = TAU / max(Segments, 1.0);

    a += 0.45 * Twist * sin(Phase);
    a += 0.15 * Warp * sin(6.0 * r - 2.0 * Phase);

    a = mod(a, seg);
    a = abs(a - 0.5 * seg);

    vec2 kp  = vec2(cos(a), sin(a)) * r;
    vec2 dir = normalize(kp + 1e-6);

    float breathe = 1.0 + 0.08 * cos(Phase + r * 10.0);

    vec2 warped = vec2(0.5) + kp * breathe;
    warped += 0.020 * Warp * vec2(
    sin(8.0 * kp.y + Phase),
    cos(8.0 * kp.x - Phase)
    );

    vec3 col = sampleChromatic(warped, dir, Chromatic * (0.3 + 0.7 * r));

    float prism = 0.5 + 0.5 * sin(18.0 * r - 8.0 * a - Phase);
    vec3 aura = palette(0.15 * prism + 0.08 * sin(Phase + r * 6.0));

    col += Glow * prism * aura * smoothstep(0.08, 0.95, r);

    fragColor = vec4(col, 1.0);
}