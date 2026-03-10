package ch.asarix.wecrazy;

import ch.asarix.wecrazy.client.WeedFxCycle;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public enum ShaderEffect {
    FOG("smoke", 512),
    HALO("halo", 64),
    KALEIDO("kaleido", 64),
    LIQUID("liquid", 128),
    MANDALA("mandala", 64),
    LSD("lsd", 128),
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
