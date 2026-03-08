package ch.asarix.wecrazy.entities;

import ch.asarix.wecrazy.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class StonedCow extends Cow implements StonedAnimal {
    public StonedCow(EntityType<? extends StonedCow> type, Level level) {
        super(type, level);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.STONED_COW_AMBIENT.value();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return ModSounds.STONED_COW_HURT.value();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.STONED_COW_DEATH.value();
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(ModSounds.STONED_COW_STEP.value(), 0.15F, 1.0F);
    }
}