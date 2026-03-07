package ch.asarix.wecrazy;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(Registries.SOUND_EVENT, Wecrazy.MODID);

    public static final DeferredHolder<SoundEvent, SoundEvent> FART =
            SOUND_EVENTS.register("fart",
                    () -> SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath(Wecrazy.MODID, "fart")
                    )
            );
}
