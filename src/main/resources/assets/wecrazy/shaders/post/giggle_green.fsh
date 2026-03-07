#version 150

uniform sampler2D InSampler;

in vec2 texCoord;
out vec4 fragColor;

layout(std140) uniform GiggleUniform {
    float GreenStrength;
};

void main() {
    vec4 c = texture(InSampler, texCoord);
    float r = mix(c.r, 0.0, GreenStrength);
    float g = mix(c.g, 1.0, GreenStrength * 0.25);
    float b = mix(c.b, 0.0, GreenStrength);
    fragColor = vec4(r, g, b, c.a);
}