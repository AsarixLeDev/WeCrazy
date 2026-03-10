package ch.asarix.wecrazy.listeners;

import ch.asarix.wecrazy.ModItems;
import ch.asarix.wecrazy.ShaderEffect;
import ch.asarix.wecrazy.WeCrazy;
import ch.asarix.wecrazy.client.WeedFxCycle;
import ch.asarix.wecrazy.items.smokable.SmokableItem;
import net.minecraft.client.Minecraft;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;

@EventBusSubscriber(modid = WeCrazy.MODID, value = Dist.CLIENT)
public final class ItemUsedClientListener {

    @SubscribeEvent
    public static void onFinishUsing(LivingEntityUseItemEvent.Finish event) {
        if (!(event.getEntity() instanceof Player player)) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || event.getEntity() != mc.player) return;

        if (event.getItem().is(ModItems.WEED_LEAF.get())) {
            WeedFxCycle.start(10 * 20, ((SmokableItem) ModItems.WEED_POWDER.get()).shaderEffects());
            player.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 200, 4, false, false, false));
        }

        if (event.getItem().is(ModItems.LSD_DROP.get())) {
            WeedFxCycle.start(10 * 20, ShaderEffect.LSD);
        }

    }
}