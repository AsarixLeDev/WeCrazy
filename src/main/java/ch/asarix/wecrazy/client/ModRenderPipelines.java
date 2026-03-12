package ch.asarix.wecrazy.client;

import ch.asarix.wecrazy.WeCrazy;
import ch.asarix.wecrazy.client.shaders.*;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientChatEvent;

@EventBusSubscriber(modid = WeCrazy.MODID, value = Dist.CLIENT)
public final class ModRenderPipelines {

    @SubscribeEvent
    public static void onMessage(ClientChatEvent event) {
        ShaderManager.start(5*20, AcidWarpShader.class);
    }
}