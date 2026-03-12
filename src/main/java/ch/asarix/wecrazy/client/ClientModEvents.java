package ch.asarix.wecrazy.client;

import ch.asarix.wecrazy.ModBlockEntities;
import ch.asarix.wecrazy.ModEntities;
import ch.asarix.wecrazy.ModMenus;
import ch.asarix.wecrazy.WeCrazy;
import ch.asarix.wecrazy.client.ber.GrindingBowlRenderer;
import ch.asarix.wecrazy.client.renderer.StonedCowRenderer;
import ch.asarix.wecrazy.client.screen.BangMenuScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(modid = WeCrazy.MODID, value = Dist.CLIENT)
public final class ClientModEvents {
    private ClientModEvents() {
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.STONED_COW.get(), StonedCowRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.GRINDING_BOWL.get(), ctx -> new GrindingBowlRenderer());
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenus.BANG_CONTAINER.get(), BangMenuScreen::new);
    }
}