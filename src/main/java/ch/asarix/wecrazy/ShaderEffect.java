package ch.asarix.wecrazy;

public enum ShaderEffect {
    FOG("giggle_fog", 512),
    HALO("giggle_halo", 64);
    private final String name;
    private final int frames;

    ShaderEffect(String name, int frames) {
        this.name = name;
        this.frames = frames;
    }

    public int getFrames() {
        return frames;
    }

    public String getName() {
        return name;
    }

    public String formatFrameSuffix(int frame) {
        return String.format("%0" + ((int) Math.ceil(Math.log10(frames))) + "d", frame);
    }
}
