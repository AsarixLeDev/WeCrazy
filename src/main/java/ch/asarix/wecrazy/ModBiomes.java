package ch.asarix.wecrazy;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

public final class ModBiomes {
    public static final ResourceKey<Biome> PSYCHEDELIC_VALLEY = ResourceKey.create(
            Registries.BIOME,
            ResourceLocation.fromNamespaceAndPath(WeCrazy.MODID, "psychedelic_valley")
    );
}