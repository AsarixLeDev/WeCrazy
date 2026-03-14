package ch.asarix.wecrazy.client.ber;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.world.item.ItemStack;

public class StompCrafterRenderState extends BlockEntityRenderState {
    public ItemStack displayStack = ItemStack.EMPTY;
    public int progress = 0;
    public int lightCoords = 0;
}