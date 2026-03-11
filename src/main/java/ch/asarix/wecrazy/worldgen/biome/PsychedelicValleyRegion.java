package ch.asarix.wecrazy.worldgen.biome;

import ch.asarix.wecrazy.ModBiomes;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import terrablender.api.Region;
import terrablender.api.RegionType;
import terrablender.api.VanillaParameterOverlayBuilder;

import java.util.function.Consumer;

import static terrablender.api.ParameterUtils.*;

public class PsychedelicValleyRegion extends Region {
    public PsychedelicValleyRegion(ResourceLocation name, int weight) {
        super(name, RegionType.OVERWORLD, weight);
    }

    @Override
    public void addBiomes(Registry<Biome> registry,
                          Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
        VanillaParameterOverlayBuilder builder = new VanillaParameterOverlayBuilder();

        new ParameterPointListBuilder()
                .temperature(Temperature.span(Temperature.NEUTRAL, Temperature.WARM))
                .humidity(Humidity.span(Humidity.WET, Humidity.HUMID))
                .continentalness(Continentalness.NEAR_INLAND, Continentalness.MID_INLAND, Continentalness.FAR_INLAND)
                .depth(Depth.SURFACE)
                .weirdness(Weirdness.VALLEY)
                .build()
                .forEach(point -> builder.add(point, ModBiomes.PSYCHEDELIC_VALLEY));

        builder.build().forEach(mapper);
    }
}