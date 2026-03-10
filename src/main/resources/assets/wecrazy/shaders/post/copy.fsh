#version 330

uniform sampler2D InSampler;

out vec4 fragColor;

void main() {
    vec2 res = vec2(textureSize(InSampler, 0));
    vec2 uv  = gl_FragCoord.xy / res;
    vec2 h   = 0.5 / res;
    uv = clamp(uv, h, 1.0 - h);

    fragColor = textureLod(InSampler, uv, 0.0);
}