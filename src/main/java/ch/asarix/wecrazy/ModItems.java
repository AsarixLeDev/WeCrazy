package ch.asarix.wecrazy;

import ch.asarix.wecrazy.items.BangItem;
import ch.asarix.wecrazy.items.WeedLeafItem;
import ch.asarix.wecrazy.items.smokable.MethPowderItem;
import ch.asarix.wecrazy.items.smokable.WeedPowderItem;
import com.jcraft.jorbis.Block;
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

    public static final DeferredItem<Item> BANG =
            ITEMS.registerItem("bang", BangItem::new);

    public static final DeferredItem<Item> WEED_POWDER =
            ITEMS.registerItem("weed_powder", WeedPowderItem::new);

    public static final DeferredItem<Item> METH_POWDER =
            ITEMS.registerItem("meth_powder", MethPowderItem::new);

    public static final DeferredItem<Item> GRINDING_TOOL =
            ITEMS.registerSimpleItem("grinding_tool", properties -> properties.stacksTo(1));

    public static final DeferredItem<BlockItem> GRINDING_BOWL =
            ITEMS.registerSimpleBlockItem(ModBlocks.GRINDING_BOWL);

    public static final DeferredItem<Item> LSD_BOTTLE =
            ITEMS.registerSimpleItem("lsd_bottle");

    public static final DeferredItem<Item> LSD_DROP =
            ITEMS.registerSimpleItem("lsd_drop", properties -> properties.food(new FoodProperties.Builder()
                    .nutrition(2)
                    .saturationModifier(0.3f)
                    .alwaysEdible()
                    .build()
            ));
}