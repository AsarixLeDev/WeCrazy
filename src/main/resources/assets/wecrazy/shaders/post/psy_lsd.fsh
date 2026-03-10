#version 330
#moj_import <minecraft:globals.glsl>

uniform sampler2D InSampler;

in vec2 texCoord;
out vec4 fragColor;

layout(std140) uniform LiquidUniform {
    float Phase;
    float Amplitude;
    float Frequency;
    float Swirl;
    float Chromatic;
    float Caustics;
};

const float TAU = 6.28318530718;

vec3 spectrum(float t) {
    return 0.5 + 0.5 * cos(TAU * (t + vec3(0.0, 0.33, 0.67)));
}

vec3 sampleChromatic(vec2 uv, vec2 dir, float amt) {
    vec2 d = dir * amt;
    return vec3(
    texture(InSampler, uv + d).r,
    texture(InSampler, uv).g,
    texture(InSampler, uv - d).b
    );
}

vec2 liquidDisplace(vec2 p, float t) {
    vec2 d = vec2(0.0);
    d.x += sin(p.y * 3.1 + t      ) * cos(p.x * 2.7 - t * 0.7) * 0.55;
    d.y += cos(p.x * 2.9 + t * 0.9) * sin(p.y * 3.3 + t * 1.1) * 0.55;
    d.x += sin(p.y * 7.3 - t * 1.3 + d.y * 2.0) * 0.28;
    d.y += cos(p.x * 6.7 + t * 1.7 + d.x * 2.0) * 0.28;
    d.x += sin(p.y * 13.1 + t * 2.1 + d.x * 3.0) * 0.14;
    d.y += cos(p.x * 11.9 - t * 1.9 + d.y * 3.0) * 0.14;
    d.x += sin(p.y * 21.7 - t * 2.7 + d.y * 5.0) * 0.07;
    d.y += cos(p.x * 19.3 + t * 2.3 + d.x * 5.0) * 0.07;
    return d;
}

float caustic(vec2 p, float t) {
    float v = 0.0;
    for (float i = 1.0; i <= 4.0; i += 1.0) {
        vec2 q = p * (1.5 + i)
        + vec2(sin(t * 0.7 * i + i), cos(t * 0.9 * i));
        v += 1.0 / (1.0 + 50.0 * abs(sin(q.x + sin(q.y + t * i * 0.4))));
    }
    return v * 0.25;
}

void main() {
    vec2 uv = texCoord;
    vec2 ct = vec2(0.5);
    vec2 p  = uv - ct;

    float r = length(p);
    float a = atan(p.y, p.x);

    // ── vortex swirl ──
    float sw = Swirl * 2.5 * sin(Phase) * exp(-r * 1.8)
    + Swirl * 1.2 * cos(Phase * 2.0 + r * 12.0) * exp(-r * 3.5);
    float a2 = a + sw;
    vec2 uv1 = ct + vec2(cos(a2), sin(a2)) * r;

    // ── multi-octave liquid flow ──
    vec2 flow = liquidDisplace(uv1 * Frequency, Phase);
    vec2 uv2  = uv1 + flow * Amplitude * 0.07;

    // ── concentric ripples ──
    float rd = length(uv2 - ct);
    vec2  rn = normalize(uv2 - ct + 1e-7);
    float rp = sin(rd * 45.0 - Phase * 5.0) * 0.012 * Amplitude
    + sin(rd * 28.0 + Phase * 3.0) * 0.008 * Amplitude;
    uv2 += rn * rp;

    // ── pulsating magnification ──
    float pulse = 1.0 + 0.14 * Amplitude * sin(Phase + rd * 10.0)
    + 0.07 * Amplitude * cos(Phase * 2.0 - rd * 14.0);
    uv2 = ct + (uv2 - ct) * pulse;

    // ── secondary fine warp ──
    uv2.x += sin(uv2.y * 22.0 * Frequency + Phase * 2.5) * 0.018 * Amplitude;
    uv2.y += cos(uv2.x * 20.0 * Frequency - Phase * 1.8) * 0.018 * Amplitude;

    // ── chromatic aberration ──
    vec2  delta = uv2 - uv;
    float cAmt  = Chromatic * (length(delta) * 3.0 + 0.08);
    vec3  col   = sampleChromatic(uv2, normalize(delta + 1e-7), cAmt);

    // ── caustic light overlay ──
    float cst    = caustic(uv * 3.5, Phase);
    vec3  cstCol = spectrum(cst * 0.4 + Phase / TAU);
    col += Caustics * cst * cstCol * 0.45;

    // ── specular liquid sheen ──
    col += pow(max(cst, 0.0), 4.0) * Caustics * vec3(0.5, 0.75, 1.0) * 0.35;

    // ── prismatic edge glow ──
    float edge = smoothstep(0.05, 0.85, r * 1.6);
    col += edge * spectrum(a / TAU + Phase / TAU) * Caustics * 0.12;

    fragColor = vec4(col, 1.0);
}