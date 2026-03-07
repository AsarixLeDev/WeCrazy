#!/usr/bin/env python3
"""
Generate Minecraft 1.21.10 post_effect preset JSONs (NeoForge-friendly).

It creates MANY post_effect JSON files with different constant uniform values,
so you can "animate" by cycling post effects from Java (preset cycling).

Example outputs:
  assets/wecrazy/post_effect/giggle_halo_00.json ... _31.json
  assets/wecrazy/post_effect/giggle_fog_00.json  ... _31.json

Usage:
  python generate_post_presets.py --modid wecrazy --steps 32 --resources ./src/main/resources

Notes:
- The GLSL uniform block field ORDER must match the JSON list order.
- "sampler_name": "In" becomes InSampler in GLSL (same convention you're using).
"""

from __future__ import annotations

import argparse
import json
import math
from dataclasses import dataclass
from pathlib import Path
from typing import Any, Dict, List, Tuple, Optional


# -----------------------------
# Core "abstract" structures
# -----------------------------

@dataclass(frozen=True)
class UniformEntry:
    name: str
    type: str               # "float", "vec2", "vec3", "int", etc.
    value: Any              # number, [x,y], [r,g,b], etc.

def u_float(name: str, v: float) -> UniformEntry:
    return UniformEntry(name, "float", float(v))

def u_vec2(name: str, x: float, y: float) -> UniformEntry:
    return UniformEntry(name, "vec2", [float(x), float(y)])

def u_vec3(name: str, x: float, y: float, z: float) -> UniformEntry:
    return UniformEntry(name, "vec3", [float(x), float(y), float(z)])


@dataclass
class EffectSpec:
    # File IDs become: <prefix>_00..N.json
    prefix: str

    # fragment shader id: "<modid>:post/<shader_name>"
    fragment_shader_path: str

    # uniform block name used in JSON and in GLSL: layout(std140) uniform <block> { ... };
    uniform_block: str

    # static uniforms included in every preset (in THIS exact order!)
    static_uniforms: List[UniformEntry]

    # function that returns dynamic uniforms for frame i (must be list in correct order relative to GLSL!)
    dynamic_uniforms_fn: callable

    # inputs for the pass
    # Each input is a dict in the PostChainConfig style you already use.
    inputs: List[Dict[str, Any]]

    # output target
    output: str = "minecraft:main"

    # vertex shader id
    vertex_shader: str = "minecraft:core/screenquad"


def build_post_effect_json(spec: EffectSpec, dyn_uniforms: List[UniformEntry]) -> Dict[str, Any]:
    # IMPORTANT: uniform list order must match GLSL block layout order
    uniform_list = [*dyn_uniforms, *spec.static_uniforms]

    obj = {
        "passes": [
            {
                "vertex_shader": spec.vertex_shader,
                "fragment_shader": spec.fragment_shader_path,
                "inputs": spec.inputs,
                "output": spec.output,
                "uniforms": {
                    spec.uniform_block: [
                        {"name": u.name, "type": u.type, "value": u.value}
                        for u in uniform_list
                    ]
                },
            }
        ]
    }
    return obj


# -----------------------------
# Effect definitions: Halo + Fog
# -----------------------------

def make_halo_spec(modid: str) -> EffectSpec:
    """
    Halo animation uses a dynamic 'Phase' float that loops from 0..2pi.
    Your GLSL should have:
      layout(std140) uniform HaloUniform {
          float Phase;
          float Warp;
          float HaloStrength;
          float EdgeStart;
          float EdgeEnd;
          float Chromatic;
      };
    And use Phase in sin/cos.
    """
    return EffectSpec(
        prefix="giggle_halo",
        fragment_shader_path=f"{modid}:post/giggle_halo_anim",
        uniform_block="HaloUniform",
        # static uniforms (ORDER must match GLSL AFTER Phase)
        static_uniforms=[
            u_float("Warp", 0.25),
            u_float("HaloStrength", 0.90),
            u_float("EdgeStart", 0.35),
            u_float("EdgeEnd", 0.71),
            u_float("Chromatic", 0.012),
        ],
        dynamic_uniforms_fn=lambda i, steps: [u_float("Phase", (math.tau * i) / steps)],
        inputs=[
            {"sampler_name": "In", "target": "minecraft:main"}
        ],
    )


def make_fog_spec(modid: str, use_depth: bool) -> EffectSpec:
    """
    Fog animation uses a dynamic 'Offset' vec2 (loops smoothly).
    Your GLSL should have something like:
      layout(std140) uniform FogUniform {
          vec2 Offset;
          float FogStrength;
          float FogScale;
          vec3 FogColor;   // optional if you include it here
      };

    And sample noise at: fbm(texCoord * FogScale + Offset)

    If use_depth=True, JSON will add a second input with use_depth_buffer,
    which you can sample as DepthSampler in GLSL if you want.
    """
    inputs = [{"sampler_name": "In", "target": "minecraft:main"}]
    if use_depth:
        inputs.append({"sampler_name": "Depth", "target": "minecraft:main", "use_depth_buffer": True})

    return EffectSpec(
        prefix="giggle_fog",
        fragment_shader_path=f"{modid}:post/giggle_fog_anim",
        uniform_block="FogUniform",
        static_uniforms=[
            u_float("FogStrength", 0.55),
            u_float("FogScale", 3.0),
            u_vec3("FogColor", 0.90, 0.90, 0.90),  # optional: remove if your GLSL doesn't have it
        ],
        dynamic_uniforms_fn=lambda i, steps: [u_float("Offset", (math.tau * i) / steps)],
        inputs=inputs,
    )


# -----------------------------
# Generation
# -----------------------------

def write_presets(resources_dir: Path, modid: str, spec: EffectSpec, steps: int) -> None:
    out_dir = resources_dir / "assets" / modid / "post_effect"
    out_dir.mkdir(parents=True, exist_ok=True)

    for i in range(steps):
        dyn_uniforms = spec.dynamic_uniforms_fn(i, steps)
        data = build_post_effect_json(spec, dyn_uniforms)

        filename = f"{spec.prefix}_{i:03d}.json"
        path = out_dir / filename
        path.write_text(json.dumps(data, indent=2), encoding="utf-8")

    print(f"[OK] Wrote {steps} presets to: {out_dir}  (prefix={spec.prefix}_XX)")


def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("--modid", required=True, help="Your mod id (e.g. wecrazy)")
    ap.add_argument("--steps", type=int, default=32, help="How many presets (16/32/64 etc.)")
    ap.add_argument("--resources", required=True, help="Path to src/main/resources")
    ap.add_argument("--fog-depth", action="store_true", help="Include Depth input (use_depth_buffer)")
    args = ap.parse_args()

    resources_dir = Path(args.resources).resolve()
    modid = args.modid

    halo = make_halo_spec(modid)
    fog = make_fog_spec(modid, use_depth=args.fog_depth)

    # write_presets(resources_dir, modid, halo, args.steps)
    write_presets(resources_dir, modid, fog, args.steps)

    print("\nNext:")
    print(f" - Post effect IDs become: {modid}:giggle_halo_00 .. {modid}:giggle_halo_{args.steps-1:02d}")
    print(f" - and:                {modid}:giggle_fog_00  .. {modid}:giggle_fog_{args.steps-1:02d}")
    print(" - Cycle them from Java each tick for animation.")


if __name__ == "__main__":
    main()