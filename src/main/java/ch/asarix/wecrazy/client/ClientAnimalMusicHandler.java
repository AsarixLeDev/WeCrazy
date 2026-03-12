package ch.asarix.wecrazy.client;

import ch.asarix.wecrazy.WeCrazy;
import ch.asarix.wecrazy.client.sound.StonedAnimalMusicInstance;
import ch.asarix.wecrazy.entities.StonedAnimal;
import ch.asarix.wecrazy.entities.StonedCow;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Animal;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

import java.util.*;

@EventBusSubscriber(modid = WeCrazy.MODID, value = Dist.CLIENT)
public final class ClientAnimalMusicHandler {
    private static final Map<UUID, StonedAnimalMusicInstance> PLAYING = new HashMap<>();

    private ClientAnimalMusicHandler() {
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null || mc.isPaused()) {
            stopAll(mc);
            return;
        }

        List<StonedCow> cows = mc.level.getEntitiesOfClass(
                StonedCow.class,
                mc.player.getBoundingBox().inflate(24.0)
        );

        Set<UUID> nearby = new HashSet<>();

        for (StonedCow cow : cows) {
            UUID id = cow.getUUID();
            nearby.add(id);

            if (!PLAYING.containsKey(id)) {
                StonedAnimalMusicInstance instance = new StonedAnimalMusicInstance(cow);
                PLAYING.put(id, instance);
                mc.getSoundManager().play(instance);
            }
        }

        Iterator<Map.Entry<UUID, StonedAnimalMusicInstance>> it = PLAYING.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<UUID, StonedAnimalMusicInstance> entry = it.next();
            UUID id = entry.getKey();
            StonedAnimalMusicInstance instance = entry.getValue();

            Entity e = mc.level.getEntity(id);

            if (!(e instanceof StonedAnimal animal) || !((Animal) animal).isAlive() || !nearby.contains(id) || instance.isStoppedFlag()) {
                mc.getSoundManager().stop(instance);
                it.remove();
            }
        }
    }

    private static void stopAll(Minecraft mc) {
        for (StonedAnimalMusicInstance instance : PLAYING.values()) {
            mc.getSoundManager().stop(instance);
        }
        PLAYING.clear();
    }
}