package ch.asarix.wecrazy.blocks;

import ch.asarix.wecrazy.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class PoopyCropBlock extends CropBlock {

    public PoopyCropBlock(BlockBehaviour.Properties props) {
        super(props);
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return ModItems.POOPY_SEEDS.get();
    }

    // 1) disable natural growth
    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        // do nothing => never grows naturally
    }

    // 2) manual growth
    public void fart(ServerLevel level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (state.getBlock() != this) return;

        int age = getAge(state);
        if (age >= getMaxAge()) return;

        int newAge = Math.min(getMaxAge(), age + 1); // grow by 1 stage per fart
        level.setBlock(pos, state.setValue(getAgeProperty(), newAge), 2);
    }
}