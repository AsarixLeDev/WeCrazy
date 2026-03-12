#version 150

uniform sampler2D InSampler;

layout(std140) uniform WobbleUniforms {
    float Time;
    vec2 ScreenSize;
    float Strength;
};

in vec2 texCoord;
out vec4 fragColor;

void main() {
    vec2 uv = texCoord;

    float waveX = sin(uv.y * 18.0 + Time * 2.5) * 0.008 * Strength;
    float waveY = cos(uv.x * 12.0 + Time * 1.7) * 0.006 * Strength;

    uv += vec2(waveX, waveY);

    fragColor = texture(InSampler, uv);
}