package de.timo_heise.timos_sink_mod.items;

import de.timo_heise.timos_sink_mod.TimosSinkMod;
import de.timo_heise.timos_sink_mod.blocks.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TimosSinkMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> SINK_TAB = CREATIVE_MODE_TABS.register("timos_sink_tab", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> ModBlocks.SINK_BLOCK.get().asItem().getDefaultInstance())
            .title(Component.translatable("itemGroup.timos_sink_tab"))
            .displayItems((parameters, output) -> {
                output.accept(ModBlocks.SINK_BLOCK.get());
            }).build());

    public static void register(IEventBus eventBus) { CREATIVE_MODE_TABS.register(eventBus); }
}
