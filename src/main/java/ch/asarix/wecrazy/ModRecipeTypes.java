package ch.asarix.wecrazy;

import ch.asarix.wecrazy.grinder.GrindingRecipe;
import ch.asarix.wecrazy.grinder.GrindingRecipeSerializer;
import ch.asarix.wecrazy.stompcrafting.StompCraftingRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.*;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModRecipeTypes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, WeCrazy.MODID);

    public static final DeferredHolder<RecipeType<?>,RecipeType<GrindingRecipe>> GRINDING =
            RECIPE_TYPES.register("grinding", () -> RecipeType.simple(
                    ResourceLocation.fromNamespaceAndPath(WeCrazy.MODID, "grinding")
            ));

    public static final Supplier<RecipeType<StompCraftingRecipe>> STOMP_CRAFTING =
            RECIPE_TYPES.register("stomp_crafting", () -> RecipeType.simple(
                    ResourceLocation.fromNamespaceAndPath(WeCrazy.MODID, "stomp_crafting")
            ));

}
