package ch.asarix.wecrazy;

import ch.asarix.wecrazy.items.WeedLeafItem;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(WeCrazy.MODID);

    public static final DeferredItem<Item> WEED_LEAF =
            ITEMS.registerItem("weed_leaf", props ->
                    new WeedLeafItem(props.food(new FoodProperties.Builder()
                            .nutrition(2)
                            .saturationModifier(0.3f)
                            .alwaysEdible()
                            .build()
                    ))
            );

    public static final DeferredItem<BlockItem> WEED_SEEDS =
            ITEMS.registerItem(
                    "weed_seeds",
                    props -> new BlockItem(ModBlocks.WEED_CROP.get(), props)
            );

    public static final DeferredItem<BlockItem> POOPY_SEEDS =
            ITEMS.registerItem(
                    "poopy_seeds",
                    props -> new BlockItem(ModBlocks.POOPY_CROP.get(), props)
            );

    public static final DeferredItem<BlockItem> METH_ORE_ITEM =
            ITEMS.registerSimpleBlockItem(ModBlocks.METH_ORE);

    public static final DeferredItem<BlockItem> DEEPSLATE_METH_ORE_ITEM =
            ITEMS.registerSimpleBlockItem(ModBlocks.DEEPSLATE_METH_ORE);

    public static final DeferredItem<Item> METH_SHARD =
            ITEMS.registerSimpleItem("meth_shard");
}