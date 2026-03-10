package ch.asarix.wecrazy;

import ch.asarix.wecrazy.menu.BangMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, WeCrazy.MODID);

    public static final Supplier<MenuType<BangMenu>> BANG_CONTAINER =
            MENUS.register("bang_container", () -> new MenuType<>(BangMenu::new, FeatureFlags.DEFAULT_FLAGS));
}
