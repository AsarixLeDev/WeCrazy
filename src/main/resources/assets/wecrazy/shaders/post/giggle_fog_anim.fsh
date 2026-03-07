#version 330

uniform sampler2D InSampler;
uniform sampler2D DepthSampler;

in vec2 texCoord;
out vec4 fragColor;

layout(std140) uniform FogUniform {
    float  Offset;
    float FogStrength;
    float FogScale;
    vec3  FogColor;
};

const float TAU = 6.28318530718;

// ── tileable value noise (per MUST be integer) ──────────────
float hash2d(vec2 p) {
    return fract(sin(dot(p, vec2(127.1, 311.7))) * 43758.5453123);
}

float noiseTile(vec2 p, float per) {
    vec2 i = floor(p);
    vec2 f = fract(p);

    vec2 ia = mod(i,             per);
    vec2 ib = mod(i + vec2(1.0), per);

    float n00 = hash2d(ia);
    float n10 = hash2d(vec2(ib.x, ia.y));
    float n01 = hash2d(vec2(ia.x, ib.y));
    float n11 = hash2d(ib);

    vec2 u = f * f * (3.0 - 2.0 * f);

    return mix(mix(n00, n10, u.x),
    mix(n01, n11, u.x), u.y);
}

// 5-octave fBm — per-octave OFFSETS instead of rotation
// so the tiling grid is never broken
float fbmTile(vec2 p, float per) {
    float val  = 0.0;
    float amp  = 0.5;
    float freq = 1.0;

    for (int i = 0; i < 5; i++) {
        vec2 off = vec2(float(i) * 5.43 + 1.7,
        float(i) * 3.19 + 4.3);
        val  += amp * noiseTile(p * freq + off, per * freq);
        freq *= 2.0;
        amp  *= 0.5;
    }
    return val;
}

void main() {
    vec4 base = texture(InSampler, texCoord);

    // depth → haze ramp
    float depth     = texture(DepthSampler, texCoord).r;
    float farFactor = smoothstep(0.45, 1.0, depth);
    farFactor *= farFactor;

    // ── loop phase 0→1 ────────────────────────────────────
    // Offset.x goes 0 → TAU
    float phase = Offset / TAU;

    // ── tiling setup ──────────────────────────────────────
    // PER = tile size in noise cells. MUST be integer.
    // All multipliers below (0.25, 0.5, 1, 2) keep PER*mult integer.
    float PER = 8.0;

    // Drift by WHOLE tile counts so phase 0 == phase 1
    vec2 drift = phase * PER * vec2(1.0, 1.0);

    vec2 p = texCoord * FogScale + drift;

    // ── domain warp #1 — big billowy shapes ───────────────
    float warpX = fbmTile(p * 0.25 + vec2(11.7, 3.2), PER * 0.25);
    float warpY = fbmTile(p * 0.25 + vec2(37.1, 7.8), PER * 0.25);
    p += vec2(warpX - 0.5, warpY - 0.5) * 2.8;

    // ── domain warp #2 — curl / swirl ─────────────────────
    float eps   = 0.15;
    float perH  = PER * 0.5;
    vec2  qCurl = p * 0.5;
    float gx = fbmTile(qCurl + vec2( eps, 0.0), perH)
    - fbmTile(qCurl + vec2(-eps, 0.0), perH);
    float gy = fbmTile(qCurl + vec2(0.0,  eps), perH)
    - fbmTile(qCurl + vec2(0.0, -eps), perH);
    p += 0.55 * vec2(gy, -gx);

    // ── density field ─────────────────────────────────────
    float nBase   = fbmTile(p,                          PER);
    float nDetail = fbmTile(p * 2.0 + vec2(5.7, 13.2), PER * 2.0);

    float ridge = 1.0 - abs(2.0 * nDetail - 1.0);
    float wisps = ridge * ridge * ridge;  // pow(x,3) without clamp

    float fogDensity = nBase   * 0.65
    + nDetail * 0.20
    + wisps   * 0.40;
    fogDensity = smoothstep(0.45, 0.95, fogDensity);

    // ── composite ─────────────────────────────────────────
    float fog = 1.0 - exp(-FogStrength * 2.0 * farFactor * fogDensity);
    fog = clamp(fog, 0.0, 1.0);

    fragColor = vec4(mix(base.rgb, FogColor, fog), 1.0);
    //fragColor = vec4(1-fragColor.rgb, 1.0);
}