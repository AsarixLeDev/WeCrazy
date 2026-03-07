#version 330

uniform sampler2D InSampler;
in vec2 texCoord;

// REQUIRED in 1.21.x post shaders
layout(std140) uniform SamplerInfo {
    vec2 OutSize;
    vec2 InSize;
};

// Globals block (same fields/order as minecraft:globals.glsl)
layout(std140) uniform Globals {
    vec2 ScreenSize;
    float GlintAlpha;
    float GameTime;
    int MenuBlurRadius;
};

out vec4 fragColor;

void main() {
    // If GameTime is 0..1 over a day, this becomes ~seconds (20 tps): 24000/20 = 1200
    float t = fract(GameTime * 1200.0);
    fragColor = vec4(t, 1.0 - t, 0.0, 1.0);
}