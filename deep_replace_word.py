#!/usr/bin/env python3
from pathlib import Path
import argparse

ALLOWED_EXTS = {".java", ".json"}


def ask(question: str) -> bool:
    return input(f"{question} : ").strip().lower() in {"y", "yes"}


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--in", dest="old", required=True, help="word to find")
    parser.add_argument("--out", dest="new", required=True, help="word to replace with")
    parser.add_argument("--path", default=".", help="folder to scan recursively")
    args = parser.parse_args()

    root = Path(args.path)

    # Collect first so renames do not disturb the scan
    files = [p for p in root.rglob("*") if p.is_file() and p.suffix.lower() in ALLOWED_EXTS]

    for path in files:
        current = path

        # 1) Rename file if needed
        if args.old in current.name:
            new_name = current.name.replace(args.old, args.new)
            new_path = current.with_name(new_name)
            if ask(f"Found {current}. do you want to rename it to {new_name}?"):
                current.rename(new_path)
                current = new_path
                print(f"Renamed to {current}")

        # 2) Replace content if needed
        try:
            content = current.read_text(encoding="utf-8")
        except Exception as e:
            print(f"Skipped {current} (cannot read: {e})")
            continue

        if args.old in content:
            replaced = content.replace(args.old, args.new)
            preview_old = content[:200].replace("\n", "\\n")
            preview_new = replaced[:200].replace("\n", "\\n")

            if ask(f'Found content in {current} : "{preview_old}". do you want to replace it by "{preview_new}"?'):
                current.write_text(replaced, encoding="utf-8")
                print(f'Replaced with "{preview_new}"')


if __name__ == "__main__":
    main()