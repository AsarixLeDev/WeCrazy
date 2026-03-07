package ch.asarix.wecrazy.blocks;

import ch.asarix.wecrazy.ModItems;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class GiggleCropBlock extends CropBlock {
    public GiggleCropBlock(BlockBehaviour.Properties props) {
        super(props);
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return ModItems.GIGGLE_SEEDS.get();
    }
}