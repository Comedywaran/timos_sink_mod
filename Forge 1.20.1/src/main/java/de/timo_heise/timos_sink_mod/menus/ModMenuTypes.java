package de.timo_heise.timos_sink_mod.menus;

import de.timo_heise.timos_sink_mod.TimosSinkMod;
import de.timo_heise.timos_sink_mod.menus.sink.SinkMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, TimosSinkMod.MOD_ID);

    public static final RegistryObject<MenuType<SinkMenu>> SINK_MENU = MENUS.register("sink_menu", () -> IForgeMenuType.create(SinkMenu::new));

    public static void register(IEventBus eventBus) { MENUS.register(eventBus); }
}
