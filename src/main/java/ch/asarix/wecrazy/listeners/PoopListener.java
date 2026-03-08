package ch.asarix.wecrazy.listeners;

import ch.asarix.wecrazy.WeCrazy;
import ch.asarix.wecrazy.network.PoopPayload;
import ch.asarix.wecrazy.network.SneakPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

@EventBusSubscriber(modid = WeCrazy.MODID, value = Dist.CLIENT)
public class PoopListener {

    private static final int POOP_COOLDOWN_TICKS = 3 * 20; // 3 seconds
    private static int sneakHeldTicks = 0;

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) return;

        // Ignore when in menus/chat
        if (mc.screen != null) {
            sneakHeldTicks = 0;
            return;
        }

        boolean sneakDown = mc.options.keyShift.isDown(); // rebinding-safe
        if (!sneakDown) {
            sneakHeldTicks = 0;
            return;
        }
        if (sneakHeldTicks == 0) {
            ClientPacketDistributor.sendToServer(new SneakPayload());
        }

        sneakHeldTicks++;

        if (sneakHeldTicks >= POOP_COOLDOWN_TICKS) {
            sneakHeldTicks = 0; // reset, so it repeats every 3s if you keep holding
            // Send to server to spawn the item
            ClientPacketDistributor.sendToServer(new PoopPayload());
        }
    }
}