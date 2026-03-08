package ch.asarix.wecrazy.listeners;

import ch.asarix.wecrazy.ModItems;
import ch.asarix.wecrazy.WeCrazy;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

@EventBusSubscriber(modid = WeCrazy.MODID, value = Dist.CLIENT)
public class CreativeTabEvents {
    @SubscribeEvent
    public static void buildContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(ModItems.WEED_SEEDS.get());
            event.accept(ModItems.WEED_LEAF.get());
        }
    }
}