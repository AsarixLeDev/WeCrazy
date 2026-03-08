package ch.asarix.wecrazy.items;

import ch.asarix.wecrazy.ModSounds;
import ch.asarix.wecrazy.entities.StonedConversions;
import ch.asarix.wecrazy.entities.StonedCow;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class WeedLeafItem extends Item {
    public WeedLeafItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        if (!(target instanceof Cow cow)) {
            return InteractionResult.PASS;
        }

        if (target instanceof StonedCow) {
            return InteractionResult.PASS;
        }

        if (!(player.level() instanceof ServerLevel serverLevel)) {
            return InteractionResult.SUCCESS;
        }

        StonedConversions.convertCow(cow, serverLevel);
        serverLevel.playSound(
                null,
                cow.blockPosition(),
                ModSounds.WEED_FEED.value(),
                cow.getSoundSource(),
                1.0F,
                1.0F
        );

        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }

        return InteractionResult.SUCCESS;
    }
}