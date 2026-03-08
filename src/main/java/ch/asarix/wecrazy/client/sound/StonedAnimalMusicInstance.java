package ch.asarix.wecrazy.client.sound;

import ch.asarix.wecrazy.ModSounds;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.animal.Animal;

public class StonedAnimalMusicInstance extends AbstractTickableSoundInstance {
    private final Animal animal;
    private boolean stoppedFlag = false;

    public StonedAnimalMusicInstance(Animal animal) {
        super(ModSounds.STONED_ANIMAL_MUSIC.value(), SoundSource.NEUTRAL, RandomSource.create());
        this.animal = animal;
        this.looping = true;
        this.delay = 0;
        this.volume = 1.0F;
        this.pitch = 1.0F;
        this.x = animal.getX();
        this.y = animal.getY();
        this.z = animal.getZ();
    }

    @Override
    public void tick() {
        if (animal.isRemoved() || !animal.isAlive()) {
            this.stop();
            this.stoppedFlag = true;
            return;
        }

        this.x = animal.getX();
        this.y = animal.getY();
        this.z = animal.getZ();
    }


    public boolean isStoppedFlag() {
        return stoppedFlag;
    }
}