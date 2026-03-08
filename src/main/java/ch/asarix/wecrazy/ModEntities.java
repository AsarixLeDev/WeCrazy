package ch.asarix.wecrazy;

import ch.asarix.wecrazy.entities.StonedCow;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class ModEntities {
    public static final DeferredRegister.Entities ENTITY_TYPES =
            DeferredRegister.createEntities(WeCrazy.MODID);

    public static final Supplier<EntityType<StonedCow>> STONED_COW =
            ENTITY_TYPES.registerEntityType(
                    "stoned_cow",
                    StonedCow::new,
                    MobCategory.CREATURE,
                    builder -> builder
                            .sized(0.9F, 1.4F)
                            .clientTrackingRange(10)
            );

}