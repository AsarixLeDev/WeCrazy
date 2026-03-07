package ch.asarix.wecrazy.client;


import ch.asarix.wecrazy.ShaderEffect;
import ch.asarix.wecrazy.Wecrazy;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@EventBusSubscriber(modid = Wecrazy.MODID, value = Dist.CLIENT)
public final class GiggleFxCycle {
    private static int ticksLeft = 0;
    private static int frame = 0;
    private static ShaderEffect shaderEffect = null;

    public static void start(int durationTicks, ShaderEffect effect) {
        if (ticksLeft <= 0) frame = 0;
        ticksLeft = durationTicks;
        shaderEffect = effect;
    }

    @SubscribeEvent
    public static void tick(ClientTickEvent.Post e) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        if (ticksLeft > 0) {
            ticksLeft--;
            frame++;

            if (shaderEffect == null) return;

            String suffix = shaderEffect.formatFrameSuffix(frame & (shaderEffect.getFrames() - 1));

            String effectFile = shaderEffect.getName() + "_" + suffix;

            mc.gameRenderer.setPostEffect(
                    ResourceLocation.fromNamespaceAndPath(Wecrazy.MODID, effectFile)
            );

            if (ticksLeft == 0) {
                mc.gameRenderer.clearPostEffect();
            }
        }
    }
}