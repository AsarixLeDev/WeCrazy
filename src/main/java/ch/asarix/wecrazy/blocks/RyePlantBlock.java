package ch.asarix.wecrazy.blocks;

import ch.asarix.wecrazy.ModBlockTypes;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;

public class RyePlantBlock extends BushBlock {
    public RyePlantBlock(Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<BushBlock> codec() {
        return ModBlockTypes.RYE.get();
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(Blocks.GRASS_BLOCK);
    }
}