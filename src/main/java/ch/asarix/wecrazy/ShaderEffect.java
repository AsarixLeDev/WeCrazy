package ch.asarix.wecrazy;

import ch.asarix.wecrazy.client.WeedFxCycle;
import net.minecraft.world.entity.player.Player;

public enum ShaderEffect {
    FOG("smoke", 64),
    HALO("halo", 64),
    KALEIDO("kaleido", 64),
    LIQUID("liquid", 64),
    MANDALA("mandala", 64),
    MOIRE("moire", 64);
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
        return String.format("%03d", frame);
    }

    public void start(int duration) {
        WeedFxCycle.start(duration, this);
    }
}
