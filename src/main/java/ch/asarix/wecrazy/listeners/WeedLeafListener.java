package ch.asarix.wecrazy.listeners;

import ch.asarix.wecrazy.ModItems;
import ch.asarix.wecrazy.ShaderEffect;
import ch.asarix.wecrazy.Wecrazy;
import ch.asarix.wecrazy.client.WeedFxCycle;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;

@EventBusSubscriber(modid = Wecrazy.MODID, value = Dist.CLIENT)
public final class WeedLeafListener {

    @SubscribeEvent
    public static void onFinishUsing(LivingEntityUseItemEvent.Finish event) {
        if (!(event.getEntity() instanceof Player)) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || event.getEntity() != mc.player) return;

        if (!event.getItem().is(ModItems.WEED_LEAF.get())) return;

        WeedFxCycle.start(10 * 20, ShaderEffect.FOG);
    }
}