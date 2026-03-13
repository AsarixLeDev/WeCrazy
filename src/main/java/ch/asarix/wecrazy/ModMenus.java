package ch.asarix.wecrazy;

import ch.asarix.wecrazy.menu.SingleSlotMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, WeCrazy.MODID);

    public static final Supplier<MenuType<SingleSlotMenu>> BANG_CONTAINER =
            MENUS.register("bang_container", () -> new MenuType<>(SingleSlotMenu::new, FeatureFlags.DEFAULT_FLAGS));
}
