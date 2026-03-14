package ch.asarix.wecrazy;

import ch.asarix.wecrazy.grinder.GrindingRecipe;
import ch.asarix.wecrazy.grinder.GrindingRecipeSerializer;
import ch.asarix.wecrazy.stompcrafting.StompCraftingRecipe;
import ch.asarix.wecrazy.stompcrafting.StompCraftingRecipeSerializer;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, WeCrazy.MODID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<GrindingRecipe>> GRINDING =
            RECIPE_SERIALIZERS.register("grinding", GrindingRecipeSerializer::new);

    public static final Supplier<RecipeSerializer<StompCraftingRecipe>> STOMP_CRAFTING =
            RECIPE_SERIALIZERS.register("stomp_crafting", StompCraftingRecipeSerializer::new);
}
