package ch.asarix.wecrazy.client;

import ch.asarix.wecrazy.ModEntities;
import ch.asarix.wecrazy.WeCrazy;
import ch.asarix.wecrazy.client.renderer.StonedCowRenderer;
import net.minecraft.client.renderer.entity.CowRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = WeCrazy.MODID, value = Dist.CLIENT)
public final class ClientModEvents {
    private ClientModEvents() {}

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.STONED_COW.get(), StonedCowRenderer::new);
    }
}