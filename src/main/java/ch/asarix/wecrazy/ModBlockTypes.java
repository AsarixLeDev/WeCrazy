package ch.asarix.wecrazy;

import ch.asarix.wecrazy.blocks.RyePlantBlock;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class ModBlockTypes {
    public static final DeferredRegister<MapCodec<? extends Block>> BLOCK_TYPES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_TYPE, WeCrazy.MODID);

    public static final Supplier<MapCodec<BushBlock>> RYE =
            BLOCK_TYPES.register("rye", () -> BlockBehaviour.simpleCodec(RyePlantBlock::new));
}