package ch.asarix.wecrazy;

import ch.asarix.wecrazy.blocks.GiggleCropBlock;
import ch.asarix.wecrazy.blocks.PoopyCropBlock;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(Wecrazy.MODID);

    public static final DeferredBlock<GiggleCropBlock> GIGGLE_CROP =
            BLOCKS.registerBlock(
                    "giggle_crop",
                    GiggleCropBlock::new,
                    () -> BlockBehaviour.Properties.ofFullCopy(Blocks.WHEAT)
            );

    public static final DeferredBlock<PoopyCropBlock> POOPY_CROP =
            BLOCKS.registerBlock(
                    "poopy_crop",
                    PoopyCropBlock::new,
                    () -> BlockBehaviour.Properties.ofFullCopy(Blocks.WHEAT)
            );

    public static final DeferredBlock<DropExperienceBlock> METH_ORE = BLOCKS.registerBlock(
            "meth_ore",
            properties -> new DropExperienceBlock(
                    UniformInt.of(3, 7), // XP range
                    properties
            ),
            () -> BlockBehaviour.Properties.of()
                    .destroyTime(3.0F)
                    .explosionResistance(3.0F)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE)
    );

    public static final DeferredBlock<Block> DEEPSLATE_METH_ORE = BLOCKS.registerBlock(
            "deepslate_meth_ore",
            properties -> new DropExperienceBlock(
                    UniformInt.of(3, 7), // XP range
                    properties
            ),
            () -> BlockBehaviour.Properties.of()
                    .destroyTime(4.5F)
                    .explosionResistance(3.0F)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.DEEPSLATE)
    );
}