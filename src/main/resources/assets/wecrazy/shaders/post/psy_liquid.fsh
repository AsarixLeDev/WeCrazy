#version 330
#moj_import <minecraft:globals.glsl>

uniform sampler2D InSampler;

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

vec2 clampUV(vec2 uv, vec2 res) {
    vec2 h = 0.5 / res;
    return clamp(uv, h, 1.0 - h);
}

vec4 sampleScreen(vec2 uv, vec2 res) {
    return textureLod(InSampler, clampUV(uv, res), 0.0);
}

void main() {
    vec2 res = vec2(textureSize(InSampler, 0));

    // Rebuild screen UV from fragment position instead of interpolated texCoord
    vec2 uv = gl_FragCoord.xy / res;

    // If your image comes out vertically flipped in Minecraft, use this instead:
    // uv.y = 1.0 - uv.y;

    float aspect = res.x / res.y;
    vec2 p = (uv - 0.5) * 2.0;
    p.x *= aspect;

    float r = length(p);
    vec2 q = p * max(Scale, 0.01);

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
    ) / 2.0;

    float breathe = 0.6 + 0.4 * cos(Phase + r * 8.0);
    vec2 offset = (0.75 * flow + 0.25 * flow2) * breathe;

    vec2 uvOffset = offset;
    uvOffset.x /= aspect;

    float warpAmt = 0.08 * max(Warp, 0.0);
    vec2 warped = uv + uvOffset * warpAmt;

    float chromaAmt = max(Chromatic, 0.0) * 0.012 * (0.4 + 0.6 * r);
    vec2 ca = uvOffset * chromaAmt;

    vec3 col = vec3(
    sampleScreen(warped + ca, res).r,
    sampleScreen(warped,      res).g,
    sampleScreen(warped - ca, res).b
    );

    float bands = 0.5 + 0.5 * sin(
    7.0 * flow.x +
    6.0 * flow.y +
    10.0 * r -
    1.0 * Phase
    );

    float inkT = 0.15 * bands
    + 0.08 * sin(2.0 * Phase + dot(flow, flow) * 4.0);
    vec3 ink = palette(inkT);

    col += max(Overlay, 0.0) * bands * ink;

    fragColor = vec4(clamp(col, 0.0, 1.0), 1.0);
}