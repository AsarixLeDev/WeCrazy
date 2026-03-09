package ch.asarix.wecrazy.blocks;

import ch.asarix.wecrazy.ModItems;
import ch.asarix.wecrazy.blocks.entity.GrindingBowlBlockEntity;
import ch.asarix.wecrazy.grinder.GrindRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class GrindingBowlBlock extends Block implements EntityBlock {
    public GrindingBowlBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new GrindingBowlBlockEntity(pos, state);
    }

    @Override
    protected InteractionResult useItemOn(
            ItemStack stack,
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            InteractionHand hand,
            BlockHitResult hitResult
    ) {
        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof GrindingBowlBlockEntity bowl)) {
            return InteractionResult.PASS;
        }

        if (hand == InteractionHand.OFF_HAND) return InteractionResult.PASS;

        // Grinding tool click
        if (stack.is(ModItems.GRINDING_TOOL.get())) {
            if (!bowl.canGrind()) {
                return InteractionResult.PASS;
            }

            if (level.isClientSide()) {
                return InteractionResult.SUCCESS;
            }

            if (bowl.grindOnce()) {
                level.playSound(null, pos, SoundEvents.STONE_HIT, SoundSource.BLOCKS, 0.8F, 1.1F);
                return InteractionResult.CONSUME;
            }

            return InteractionResult.PASS;
        }

        // Insert input item
        if (bowl.isEmpty() && GrindRegistry.get(stack) != null) {
            if (level.isClientSide()) {
                return InteractionResult.SUCCESS;
            }

            if (bowl.insertOne(stack)) {
                level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.5F, 0.9F);
                return InteractionResult.CONSUME;
            }
        }

        if (stack.isEmpty()) {
            return useWithoutItem(state, level, pos, player, hitResult);
        }

        return InteractionResult.PASS;
    }

    @Override
    protected InteractionResult useWithoutItem(
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            BlockHitResult hitResult
    ) {
        BlockEntity be = level.getBlockEntity(pos);

        if (!(be instanceof GrindingBowlBlockEntity bowl) || bowl.isEmpty()) {
            return InteractionResult.PASS;
        }

        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        ItemStack out = bowl.removeStack();
        if (!player.addItem(out)) {
            Containers.dropItemStack(level, player.getX(), player.getY(), player.getZ(), out);
        }

        level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.5F, 1.1F);
        return InteractionResult.CONSUME;
    }
}