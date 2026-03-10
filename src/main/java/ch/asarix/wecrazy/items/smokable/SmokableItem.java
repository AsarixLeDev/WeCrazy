package ch.asarix.wecrazy.items.smokable;

import ch.asarix.wecrazy.ShaderEffect;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.List;
import java.util.function.Function;

public interface SmokableItem {
    ShaderEffect shaderEffects();

    List<Function<Integer, MobEffectInstance>> getMinecraftEffects();

    default void startEffects(int duration, ServerPlayer player) {
        for (Function<Integer, MobEffectInstance> func : getMinecraftEffects()) {
            player.addEffect(func.apply(duration));
        }
    }
}
