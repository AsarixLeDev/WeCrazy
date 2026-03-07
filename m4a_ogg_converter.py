#!/usr/bin/env python3
from pathlib import Path
import argparse
import subprocess
import sys

def pick_vorbis_encoder() -> str:
    result = subprocess.run(
        ["ffmpeg", "-encoders"],
        capture_output=True,
        text=True,
        check=True
    )
    text = result.stdout.lower()

    if "libvorbis" in text:
        return "libvorbis"
    if " vorbis" in text or "\navorbis" in text:
        return "vorbis"

    print("No Vorbis encoder found in this FFmpeg build.", file=sys.stderr)
    print("Run: ffmpeg -encoders | findstr vorbis", file=sys.stderr)
    sys.exit(1)

def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("input", nargs="?", default="input")
    parser.add_argument("output", nargs="?", default="output")
    args = parser.parse_args()

    in_dir = Path(args.input)
    out_dir = Path(args.output)
    out_dir.mkdir(parents=True, exist_ok=True)

    if not in_dir.is_dir():
        print(f"Input folder not found: {in_dir}", file=sys.stderr)
        sys.exit(1)

    encoder = pick_vorbis_encoder()
    print(f"Using encoder: {encoder}")

    for src in in_dir.glob("*.m4a"):
        dst = out_dir / (src.stem + ".ogg")
        subprocess.run(
            [
                "ffmpeg", "-y",
                "-i", str(src),
                "-vn",
                "-ac", "2",
                "-ar", "44100",
                "-c:a", encoder,
                "-q:a", "4",
                "-strict", "-2",
                str(dst)
            ],
            check=True
        )
        print(f"Converted: {src.name} -> {dst.name}")

if __name__ == "__main__":
    main()