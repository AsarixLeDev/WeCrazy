#!/usr/bin/env python3
"""
Generate Minecraft 1.21.10 post_effect preset JSONs (NeoForge-friendly).

This version supports:
- halo      -> <modid>:post/giggle_halo_anim
- smoke     -> <modid>:post/smoke_anim
- kaleido   -> <modid>:post/psy_kaleido
- liquid    -> <modid>:post/psy_liquid
- mandala   -> <modid>:post/psy_mandala
- moire     -> <modid>:post/psy_moire
- lsd       -> <modid>:post/psy_lsd

Example:
  python generate_post_presets.py \
      --modid wecrazy \
      --steps 64 \
      --resources ./src/main/resources \
      --effects kaleido liquid mandala moire

Notes:
- Uniform order in JSON MUST match GLSL block order exactly.
- "sampler_name": "In" becomes InSampler in GLSL.
- For perfect loops, Phase / Offset is generated as: TAU * i / steps
- To avoid framebuffer feedback artifacts, effects are generated as:
    minecraft:main -> swap -> minecraft:main
"""

from __future__ import annotations

import argparse
import json
import math
from dataclasses import dataclass
from pathlib import Path
from typing import Any, Callable, Dict, List


# -----------------------------
# Core structures
# -----------------------------

@dataclass(frozen=True)
class UniformEntry:
    name: str
    type: str
    value: Any


def u_float(name: str, v: float) -> UniformEntry:
    return UniformEntry(name, "float", float(v))


def u_vec2(name: str, x: float, y: float) -> UniformEntry:
    return UniformEntry(name, "vec2", [float(x), float(y)])


def u_vec3(name: str, x: float, y: float, z: float) -> UniformEntry:
    return UniformEntry(name, "vec3", [float(x), float(y), float(z)])


@dataclass
class EffectSpec:
    prefix: str
    fragment_shader_path: str
    uniform_block: str
    static_uniforms: List[UniformEntry]
    dynamic_uniforms_fn: Callable[[int, int], List[UniformEntry]]
    inputs: List[Dict[str, Any]]
    vertex_shader: str = "minecraft:core/screenquad"
    intermediate_target: str = "swap"
    blit_back: bool = True


# -----------------------------
# JSON builder
# -----------------------------

def build_uniform_block(
        uniform_block: str,
        dyn_uniforms: List[UniformEntry],
        static_uniforms: List[UniformEntry],
) -> Dict[str, Any]:
    uniform_list = [*dyn_uniforms, *static_uniforms]
    return {
        uniform_block: [
            {"name": u.name, "type": u.type, "value": u.value}
            for u in uniform_list
        ]
    }


def build_post_effect_json(spec: EffectSpec, dyn_uniforms: List[UniformEntry], modid: str) -> Dict[str, Any]:
    first_pass = {
        "vertex_shader": spec.vertex_shader,
        "fragment_shader": spec.fragment_shader_path,
        "inputs": spec.inputs,
        "output": spec.intermediate_target if spec.blit_back else "minecraft:main",
        "uniforms": build_uniform_block(spec.uniform_block, dyn_uniforms, spec.static_uniforms),
    }

    if not spec.blit_back:
        return {"passes": [first_pass]}

    blit_pass = {
        "vertex_shader": f"{modid}:post/liquid_triangle",
        "fragment_shader": f"{modid}:post/copy",
        "inputs": [
            {
                "sampler_name": "In",
                "target": spec.intermediate_target,
            }
        ],
        "output": "minecraft:main",
    }

    return {
        "targets": {
            spec.intermediate_target: {}
        },
        "passes": [first_pass, blit_pass],
    }

# -----------------------------
# Shared helpers
# -----------------------------

TAU = math.tau


def phase_uniform(i: int, steps: int) -> List[UniformEntry]:
    return [u_float("Phase", TAU * i / steps)]


def offset_uniform(i: int, steps: int) -> List[UniformEntry]:
    return [u_float("Offset", TAU * i / steps)]


def scalar_loop_uniform(name: str, i: int, steps: int) -> List[UniformEntry]:
    return [u_float(name, TAU * i / steps)]


def main_input_only() -> List[Dict[str, Any]]:
    return [{"sampler_name": "In", "target": "minecraft:main"}]


def main_plus_depth() -> List[Dict[str, Any]]:
    return [
        {"sampler_name": "In", "target": "minecraft:main"},
        {"sampler_name": "Depth", "target": "minecraft:main", "use_depth_buffer": True},
    ]


# -----------------------------
# Effect specs
# -----------------------------

def make_halo_spec(modid: str) -> EffectSpec:
    # layout(std140) uniform HaloUniform {
    #     float Phase;
    #     float Warp;
    #     float HaloStrength;
    #     float EdgeStart;
    #     float EdgeEnd;
    #     float Chromatic;
    # };
    return EffectSpec(
        prefix="halo",
        fragment_shader_path=f"{modid}:post/giggle_halo_anim",
        uniform_block="HaloUniform",
        static_uniforms=[
            u_float("Warp", 0.25),
            u_float("HaloStrength", 0.90),
            u_float("EdgeStart", 0.35),
            u_float("EdgeEnd", 0.71),
            u_float("Chromatic", 0.012),
        ],
        dynamic_uniforms_fn=phase_uniform,
        inputs=main_input_only(),
    )


def make_smoke_spec(modid: str, use_depth: bool) -> EffectSpec:
    # layout(std140) uniform FogUniform {
    #     float Offset;
    #     float FogStrength;
    #     float FogScale;
    #     vec3  FogColor;
    #     float FogSpeed;
    # };
    #
    # Note:
    # FogSpeed is kept at 1.0 here so a translation-based tile loop
    # can actually close perfectly over one Offset cycle.
    return EffectSpec(
        prefix="smoke",
        fragment_shader_path=f"{modid}:post/smoke_anim",
        uniform_block="FogUniform",
        static_uniforms=[
            u_float("FogStrength", 1.2),
            u_float("FogScale", 1.3),
            u_vec3("FogColor", 0.90, 0.90, 0.90),
            u_float("FogSpeed", 32.0),
        ],
        dynamic_uniforms_fn=offset_uniform,
        inputs=main_plus_depth() if use_depth else main_input_only(),
    )


def make_kaleido_spec(modid: str) -> EffectSpec:
    # layout(std140) uniform KaleidoUniform {
    #     float Phase;
    #     float Segments;
    #     float Twist;
    #     float Warp;
    #     float Chromatic;
    #     float Glow;
    # };
    return EffectSpec(
        prefix="kaleido",
        fragment_shader_path=f"{modid}:post/psy_kaleido",
        uniform_block="KaleidoUniform",
        static_uniforms=[
            u_float("Segments", 8.0),
            u_float("Twist", 1.0),
            u_float("Warp", 1.0),
            u_float("Chromatic", 0.003),
            u_float("Glow", 0.35),
        ],
        dynamic_uniforms_fn=phase_uniform,
        inputs=main_input_only(),
    )


def make_liquid_spec(modid: str) -> EffectSpec:
    # layout(std140) uniform LiquidUniform {
    #     float Phase;
    #     float Warp;
    #     float Scale;
    #     float Chromatic;
    #     float Overlay;
    # };
    return EffectSpec(
        prefix="liquid",
        fragment_shader_path=f"{modid}:post/psy_liquid",
        uniform_block="LiquidUniform",
        static_uniforms=[
            u_float("Warp", 1.0),
            u_float("Scale", 5.0),
            u_float("Chromatic", 0.0025),
            u_float("Overlay", 0.30),
        ],
        dynamic_uniforms_fn=phase_uniform,
        vertex_shader=f"{modid}:post/liquid_triangle",
        inputs=main_input_only(),
    )


def make_mandala_spec(modid: str) -> EffectSpec:
    # layout(std140) uniform MandalaUniform {
    #     float Phase;
    #     float Petals;
    #     float RingFreq;
    #     float Distort;
    #     float Glow;
    # };
    return EffectSpec(
        prefix="mandala",
        fragment_shader_path=f"{modid}:post/psy_mandala",
        uniform_block="MandalaUniform",
        static_uniforms=[
            u_float("Petals", 10.0),
            u_float("RingFreq", 3.5),
            u_float("Distort", 1.0),
            u_float("Glow", 0.35),
        ],
        dynamic_uniforms_fn=phase_uniform,
        inputs=main_input_only(),
    )


def make_moire_spec(modid: str) -> EffectSpec:
    # layout(std140) uniform MoireUniform {
    #     float Phase;
    #     float Frequency;
    #     float Warp;
    #     float Chromatic;
    #     float Strength;
    # };
    return EffectSpec(
        prefix="moire",
        fragment_shader_path=f"{modid}:post/psy_moire",
        uniform_block="MoireUniform",
        static_uniforms=[
            u_float("Frequency", 6.0),
            u_float("Warp", 1.0),
            u_float("Chromatic", 0.002),
            u_float("Strength", 0.55),
        ],
        dynamic_uniforms_fn=phase_uniform,
        inputs=main_input_only(),
    )


def make_lsd_spec(modid: str) -> EffectSpec:
    # layout(std140) uniform LiquidUniform {
    #     float Phase;
    #     float Amplitude;
    #     float Frequency;
    #     float Swirl;
    #     float Chromatic;
    #     float Caustics;
    # };
    return EffectSpec(
        prefix="lsd",
        fragment_shader_path=f"{modid}:post/psy_lsd",
        uniform_block="LiquidUniform",
        static_uniforms=[
            u_float("Amplitude", 1.2),
            u_float("Frequency", 1.1),
            u_float("Swirl", 1.0),
            u_float("Chromatic", 0.7),
            u_float("Caustics", 1.2),
        ],
        dynamic_uniforms_fn=phase_uniform,
        inputs=main_input_only(),
    )


# -----------------------------
# Writing
# -----------------------------

def write_presets(resources_dir: Path, modid: str, spec: EffectSpec, steps: int) -> None:
    out_dir = resources_dir / "assets" / modid / "post_effect"
    out_dir.mkdir(parents=True, exist_ok=True)

    for i in range(steps):
        data = build_post_effect_json(spec, spec.dynamic_uniforms_fn(i, steps), modid)
        path = out_dir / f"{spec.prefix}_{i:03d}.json"
        path.write_text(json.dumps(data, indent=2), encoding="utf-8")

    print(f"[OK] Wrote {steps:>4} presets -> {out_dir}  ({spec.prefix}_XXX.json)")


# -----------------------------
# CLI
# -----------------------------

def main() -> None:
    ap = argparse.ArgumentParser()
    ap.add_argument("--modid", required=True, help="Your mod id, e.g. wecrazy")
    ap.add_argument("--steps", type=int, default=64, help="Default preset count per effect")
    ap.add_argument("--resources", required=True, help="Path to src/main/resources")
    ap.add_argument(
        "--effects",
        nargs="+",
        default=["kaleido", "liquid", "mandala", "moire"],
        help="Effects to generate: halo smoke kaleido liquid mandala moire lsd all",
    )
    ap.add_argument(
        "--fog-depth",
        action="store_true",
        help="For smoke only: include Depth input (use_depth_buffer=true)",
    )
    args = ap.parse_args()

    resources_dir = Path(args.resources).resolve()
    modid = args.modid

    requested = set(args.effects)
    if "all" in requested:
        requested = {"halo", "smoke", "kaleido", "liquid", "mandala", "moire", "lsd"}

    builders = {
        "halo": lambda: make_halo_spec(modid),
        "smoke": lambda: make_smoke_spec(modid, use_depth=args.fog_depth),
        "kaleido": lambda: make_kaleido_spec(modid),
        "liquid": lambda: make_liquid_spec(modid),
        "mandala": lambda: make_mandala_spec(modid),
        "moire": lambda: make_moire_spec(modid),
        "lsd": lambda: make_lsd_spec(modid),
    }

    steps_by_effect = {
        "halo": 64,
        "smoke": 512,
        "liquid": 128,
        "lsd": 128,
    }

    unknown = sorted(requested - builders.keys())
    if unknown:
        raise SystemExit(f"Unknown effect(s): {', '.join(unknown)}")

    for name in ["halo", "smoke", "kaleido", "liquid", "mandala", "moire", "lsd"]:
        if name in requested:
            effect_steps = steps_by_effect.get(name, args.steps)
            write_presets(resources_dir, modid, builders[name](), effect_steps)


if __name__ == "__main__":
    main()