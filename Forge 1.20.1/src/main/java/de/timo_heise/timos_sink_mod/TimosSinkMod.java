package de.timo_heise.timos_sink_mod;

import com.mojang.logging.LogUtils;
import de.timo_heise.timos_sink_mod.block_entities.ModBlockEntities;
import de.timo_heise.timos_sink_mod.blocks.ModBlocks;
import de.timo_heise.timos_sink_mod.items.ModCreativeTabs;
import de.timo_heise.timos_sink_mod.items.ModItems;
import de.timo_heise.timos_sink_mod.menus.ModMenuTypes;
import de.timo_heise.timos_sink_mod.menus.sink.SinkMenu;
import de.timo_heise.timos_sink_mod.menus.sink.SinkScreen;
import de.timo_heise.timos_sink_mod.networking.ModPacketHandler;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(TimosSinkMod.MOD_ID)
public class TimosSinkMod
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "timos_sink_mod";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public TimosSinkMod(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();

        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModCreativeTabs.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModPacketHandler.register();

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {}

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            MenuScreens.<SinkMenu, SinkScreen>register(ModMenuTypes.SINK_MENU.get(), SinkScreen::new);
        }
    }
}
