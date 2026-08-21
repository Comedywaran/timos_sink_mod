package de.timo_heise.timos_sink_mod.menus.sink;

import de.timo_heise.timos_sink_mod.TimosSinkMod;
import de.timo_heise.timos_sink_mod.networking.ModPacketHandler;
import de.timo_heise.timos_sink_mod.networking.packets.SinkConfigMenuChanged;
import net.minecraft.client.gui.components.Button;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.registries.ForgeRegistries;

public class SinkScreen extends AbstractSinkScreen {
    public SinkScreen(SinkMenu menu, Inventory inv, Component title) {
        this(menu, inv, title, inv.player.isCreative());
    }

    public SinkScreen(SinkMenu menu, Inventory inv, Component title, boolean isCreative) {
        super(menu, inv, title, isCreative, ResourceLocation.fromNamespaceAndPath(TimosSinkMod.MOD_ID, "textures/gui/sink/sink.png"));
        this.originalTitle = title;
    }

    @Override
    protected void init() {
        super.init();

        Button btn = Button.builder(Component.literal("Config"), b -> {
            ModPacketHandler.INSTANCE.sendToServer(new SinkConfigMenuChanged(menu.sinkBlockEntity.getBlockPos(), test())); //TODO: remove
        }).bounds(leftPos + 20, topPos + 20, 120, 20).build();
        addRenderableWidget(btn);
    }

    public static CompoundTag test() { // placeholder
        CompoundTag tag = new CompoundTag();
        tag.putString("Fluid", String.valueOf(ForgeRegistries.FLUIDS.getKey(Fluids.WATER)));
        return tag;
    }

    @Override
    protected AbstractSinkScreen.SinkTabs getTabType() {
        return AbstractSinkScreen.SinkTabs.SINK;
    }
}
