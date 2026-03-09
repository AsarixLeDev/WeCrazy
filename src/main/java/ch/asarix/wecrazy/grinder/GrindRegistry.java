package ch.asarix.wecrazy.grinder;

import ch.asarix.wecrazy.ModItems;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public final class GrindRegistry {
    private GrindRegistry() {}

    @Nullable
    public static GrindEntry get(ItemStack stack) {
        if (stack.is(ModItems.WEED_LEAF.get())) {
            return new GrindEntry(new ItemStack(ModItems.WEED_POWDER.get()), 3);
        }

        if (stack.is(ModItems.METH_SHARD.get())) {
            return new GrindEntry(new ItemStack(ModItems.METH_POWDER.get()), 8);
        }

        return null;
    }
}