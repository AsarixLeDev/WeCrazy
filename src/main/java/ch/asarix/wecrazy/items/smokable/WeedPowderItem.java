package ch.asarix.wecrazy.items.smokable;

import ch.asarix.wecrazy.ShaderEffect;
import net.minecraft.world.item.Item;

public class WeedPowderItem extends Item implements SmokableItem {
    public WeedPowderItem(Properties properties) {
        super(properties);
    }

    @Override
    public ShaderEffect effect() {
        return ShaderEffect.FOG;
    }
}
