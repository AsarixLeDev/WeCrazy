package ch.asarix.wecrazy.items.smokable;

import ch.asarix.wecrazy.client.shaders.AnimatedShader;
import ch.asarix.wecrazy.client.shaders.VoidPulseShader;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;

import java.util.List;
import java.util.function.Function;

public class MethPowderItem extends Item implements SmokableItem {
    public MethPowderItem(Properties properties) {
        super(properties);
    }

    @Override
    public Class<? extends AnimatedShader> shaderEffects() {
        return VoidPulseShader.class;
    }

    @Override
    public List<Function<Integer, MobEffectInstance>> getMinecraftEffects() {
        return List.of();
    }
}
