package ch.asarix.wecrazy.blocks.entity;

import ch.asarix.wecrazy.ModBlockEntities;
import ch.asarix.wecrazy.grinder.GrindEntry;
import ch.asarix.wecrazy.grinder.GrindRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class GrindingBowlBlockEntity extends BlockEntity {
    private ItemStack storedStack = ItemStack.EMPTY;
    private int progress = 0;

    public GrindingBowlBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.GRINDING_BOWL.get(), pos, state);
    }

    public boolean isEmpty() {
        return this.storedStack.isEmpty();
    }

    public ItemStack getStoredStack() {
        return this.storedStack;
    }

    public int getProgress() {
        return this.progress;
    }

    public boolean canInsert(ItemStack stack) {
        return this.storedStack.isEmpty() && GrindRegistry.get(stack) != null;
    }

    public boolean insertOne(ItemStack playerStack) {
        if (!canInsert(playerStack)) {
            return false;
        }

        this.storedStack = playerStack.copyWithCount(1);
        this.progress = 0;

        playerStack.shrink(1);
        this.markUpdated();
        return true;
    }

    public boolean canGrind() {
        return !this.storedStack.isEmpty() && GrindRegistry.get(this.storedStack) != null;
    }

    public boolean grindOnce() {
        GrindEntry entry = GrindRegistry.get(this.storedStack);
        if (entry == null) {
            return false;
        }

        this.progress++;

        if (this.progress >= entry.clicksRequired()) {
            this.storedStack = entry.result().copy();
            this.progress = 0;
        }

        this.markUpdated();
        return true;
    }

    public ItemStack removeStack() {
        ItemStack out = this.storedStack;
        this.storedStack = ItemStack.EMPTY;
        this.progress = 0;
        this.markUpdated();
        return out;
    }

    private void markUpdated() {
        this.setChanged();

        if (this.level != null) {
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
        }
    }

    @Override
    public void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.storedStack = input.read("stored_item", ItemStack.CODEC).orElse(ItemStack.EMPTY);
        this.progress = input.getIntOr("progress", 0);
    }

    @Override
    public void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.storeNullable("stored_item", ItemStack.CODEC, this.storedStack.isEmpty() ? null : this.storedStack);
        output.putInt("progress", this.progress);
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        super.preRemoveSideEffects(pos, state);

        if (this.level instanceof ServerLevel serverLevel && !this.storedStack.isEmpty()) {
            Containers.dropItemStack(
                    serverLevel,
                    pos.getX() + 0.5,
                    pos.getY() + 0.5,
                    pos.getZ() + 0.5,
                    this.storedStack
            );
            this.storedStack = ItemStack.EMPTY;
            this.progress = 0;
        }
    }
}