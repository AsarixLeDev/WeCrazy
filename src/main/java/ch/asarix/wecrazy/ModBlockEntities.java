package ch.asarix.wecrazy;

import ch.asarix.wecrazy.blocks.entity.GrindingBowlBlockEntity;
import ch.asarix.wecrazy.blocks.entity.StompCrafterBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, WeCrazy.MODID);

    public static final Supplier<BlockEntityType<GrindingBowlBlockEntity>> GRINDING_BOWL =
            BLOCK_ENTITY_TYPES.register(
                    "grinding_bowl",
                    () -> new BlockEntityType<>(
                            GrindingBowlBlockEntity::new,
                            false,
                            ModBlocks.GRINDING_BOWL.get()
                    )
            );

    public static final Supplier<BlockEntityType<StompCrafterBlockEntity>> STOMP_CRAFTER =
            BLOCK_ENTITY_TYPES.register("stomp_crafter",
                    () -> new BlockEntityType<>(
                            StompCrafterBlockEntity::new,
                            false,
                            ModBlocks.STOMP_CRAFTER.get()
                    ));
}