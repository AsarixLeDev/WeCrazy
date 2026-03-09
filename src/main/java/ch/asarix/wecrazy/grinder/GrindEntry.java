package ch.asarix.wecrazy.grinder;

import net.minecraft.world.item.ItemStack;

public record GrindEntry(ItemStack result, int clicksRequired) {
}