package ch.asarix.wecrazy.entities;

import ch.asarix.wecrazy.ModEntities;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ConversionParams;
import net.minecraft.world.entity.animal.Cow;

public final class StonedConversions {
    private StonedConversions() {}

    public static void convertCow(Cow oldCow, ServerLevel level) {
        oldCow.convertTo(
                ModEntities.STONED_COW.get(),
                ConversionParams.single(
                        new StonedCow(ModEntities.STONED_COW.get(), level),
                        true,
                        true),
                (newCow) -> {
                    newCow.moveOrInterpolateTo(oldCow.position(), oldCow.getYRot(), oldCow.getXRot());
                    newCow.setDeltaMovement(oldCow.getDeltaMovement());

                    newCow.setHealth(oldCow.getHealth());
                    newCow.setAge(oldCow.getAge());

                    if (oldCow.hasCustomName()) {
                        newCow.setCustomName(oldCow.getCustomName());
                        newCow.setCustomNameVisible(oldCow.isCustomNameVisible());
                    }

                    newCow.setNoAi(oldCow.isNoAi());
                    newCow.setInvulnerable(oldCow.isInvulnerable());
                    newCow.setSilent(oldCow.isSilent());
                    newCow.setNoGravity(oldCow.isNoGravity());
                    newCow.setGlowingTag(oldCow.hasGlowingTag());

                    newCow.setVariant(oldCow.getVariant());
                });
    }
}