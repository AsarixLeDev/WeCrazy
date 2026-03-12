package ch.asarix.wecrazy.items.smokable;

import ch.asarix.wecrazy.client.shaders.AnimatedShader;
import ch.asarix.wecrazy.client.shaders.ChromaticDreamShader;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;

import java.util.List;
import java.util.function.Function;

public class WeedPowderItem extends Item implements SmokableItem {
    public WeedPowderItem(Properties properties) {
        super(properties);
    }

    @Override
    public Class<? extends AnimatedShader> shaderEffects() {
        return ChromaticDreamShader.class;
    }

    @Override
    public List<Function<Integer, MobEffectInstance>> getMinecraftEffects() {
        return List.of(duration -> new MobEffectInstance(MobEffects.SLOWNESS, duration, 4, false, false, false));
    }
}
