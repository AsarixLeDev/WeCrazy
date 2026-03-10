package ch.asarix.wecrazy.items.smokable;

import ch.asarix.wecrazy.ShaderEffect;
import net.minecraft.world.item.Item;

public class MethPowderItem extends Item implements SmokableItem {
    public MethPowderItem(Properties properties) {
        super(properties);
    }

    @Override
    public ShaderEffect effect() {
        return ShaderEffect.LIQUID;
    }
}
