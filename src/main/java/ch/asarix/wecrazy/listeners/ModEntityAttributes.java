package ch.asarix.wecrazy.listeners;

import ch.asarix.wecrazy.ModEntities;
import ch.asarix.wecrazy.WeCrazy;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Cow;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

@EventBusSubscriber(modid = WeCrazy.MODID)
public final class ModEntityAttributes {
    private ModEntityAttributes() {}

    @SubscribeEvent
    public static void createDefaultAttributes(EntityAttributeCreationEvent event) {
        event.put(
                ModEntities.STONED_COW.get(),
                Cow.createMobAttributes()
                        .add(Attributes.MAX_HEALTH, 10.0D)
                        .add(Attributes.MOVEMENT_SPEED, 0.12D) // vanilla cow-ish behavior, but slower
                        .add(Attributes.TEMPT_RANGE, 10.0D)
                        .build()
        );
    }
}