package ch.asarix.wecrazy.items.smokable;

import ch.asarix.wecrazy.ShaderEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;

import java.util.List;
import java.util.function.Function;

public class MethPowderItem extends Item implements SmokableItem {
    public MethPowderItem(Properties properties) {
        super(properties);
    }

    @Override
    public ShaderEffect shaderEffects() {
        return ShaderEffect.LIQUID;
    }

    @Override
    public List<Function<Integer, MobEffectInstance>> getMinecraftEffects() {
        return List.of();
    }
}
