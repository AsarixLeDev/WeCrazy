package ch.asarix.wecrazy;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(Registries.SOUND_EVENT, WeCrazy.MODID);

    public static final DeferredHolder<SoundEvent, SoundEvent> FART =
            SOUND_EVENTS.register("fart",
                    () -> SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath(WeCrazy.MODID, "fart")
                    )
            );

    public static final Holder<SoundEvent> STONED_COW_AMBIENT =
            SOUND_EVENTS.register("stoned_cow_ambient", SoundEvent::createVariableRangeEvent);

    public static final Holder<SoundEvent> STONED_COW_HURT =
            SOUND_EVENTS.register("stoned_cow_hurt", SoundEvent::createVariableRangeEvent);

    public static final Holder<SoundEvent> STONED_COW_DEATH =
            SOUND_EVENTS.register("stoned_cow_death", SoundEvent::createVariableRangeEvent);

    public static final Holder<SoundEvent> STONED_COW_STEP =
            SOUND_EVENTS.register("stoned_cow_step", SoundEvent::createVariableRangeEvent);

    public static final Holder<SoundEvent> WEED_FEED =
            SOUND_EVENTS.register("weed_feed", SoundEvent::createVariableRangeEvent);

    public static final Holder<SoundEvent> STONED_ANIMAL_MUSIC =
            SOUND_EVENTS.register("stoned_animal_music", SoundEvent::createVariableRangeEvent);
}
