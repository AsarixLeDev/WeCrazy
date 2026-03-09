#version 330
#moj_import <minecraft:globals.glsl>

uniform sampler2D InSampler;

in vec2 texCoord;
out vec4 fragColor;

layout(std140) uniform LiquidUniform {
    float Phase;
    float Warp;
    float Scale;
    float Chromatic;
    float Overlay;
};

const float TAU = 6.283185307;

vec3 palette(float t) {
    return 0.5 + 0.5 * cos(TAU * (t + vec3(0.0, 0.33, 0.67)));
}

void main() {
    vec2 uv = texCoord;
    vec2 p  = (uv - vec2(0.5)) * 2.0;
    float r = length(p);

    vec2 q = p * Scale;

    // Every Phase coefficient is now an integer => exact 2π loop
    vec2 flow = vec2(
    sin(1.7 * q.y + 1.0 * Phase)
    + cos(1.3 * q.x - 2.0 * Phase)
    + sin(1.1 * (q.x + q.y) + 3.0 * Phase),

    cos(1.8 * q.x - 1.0 * Phase)
    + sin(1.5 * q.y + 2.0 * Phase)
    + cos(1.2 * (q.x - q.y) - 3.0 * Phase)
    ) / 3.0;

    vec2 flow2 = vec2(
    sin(2.6 * q.y - 2.0 * Phase),
    cos(2.4 * q.x + 3.0 * Phase)
    );

    vec2 offset = Warp * (0.75 * flow + 0.25 * flow2) * (0.6 + 0.4 * cos(Phase + r * 8.0));
    vec2 warped = uv + 0.08 * offset;

    vec2 dir = normalize(offset + 1e-6);
    vec2 ca  = dir * Chromatic * (0.4 + 0.6 * r);

    float rr = texture(InSampler, warped + ca).r;
    float gg = texture(InSampler, warped).g;
    float bb = texture(InSampler, warped - ca).b;

    vec3 col = vec3(rr, gg, bb);

    float bands = 0.5 + 0.5 * sin(
    7.0 * flow.x +
    6.0 * flow.y +
    10.0 * r -
    1.0 * Phase
    );

    vec3 ink = palette(0.15 * bands + 0.08 * sin(2.0 * Phase + dot(flow, flow) * 4.0));
    col += Overlay * bands * ink;

    fragColor = vec4(col, 1.0);
}