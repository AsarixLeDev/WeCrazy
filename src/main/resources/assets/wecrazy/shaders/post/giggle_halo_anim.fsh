#version 330
#moj_import <minecraft:globals.glsl>

uniform sampler2D InSampler;
in vec2 texCoord;
out vec4 fragColor;

layout(std140) uniform HaloUniform {
    float Phase;
    float Warp;
    float HaloStrength;
    float EdgeStart;
    float EdgeEnd;
    float Chromatic;
};

void main() {
    vec2 uv = texCoord;
    vec2 center = vec2(0.5);
    vec2 p = uv - center;
    float r = length(p);

    float edge = smoothstep(EdgeStart, EdgeEnd, r);

    float pulse = 0.6 + 0.4 * sin(Phase); // animated via preset cycling

    vec2 dir = normalize(p + 1e-6);
    vec2 warped = uv + dir * (Warp * edge * pulse);

    vec2 ca = dir * (Chromatic * edge);
    float rr = texture(InSampler, warped + ca).r;
    float gg = texture(InSampler, warped).g;
    float bb = texture(InSampler, warped - ca).b;

    vec3 col = vec3(rr, gg, bb);
    col += edge * HaloStrength * pulse;

    fragColor = vec4(col, 1.0);
}