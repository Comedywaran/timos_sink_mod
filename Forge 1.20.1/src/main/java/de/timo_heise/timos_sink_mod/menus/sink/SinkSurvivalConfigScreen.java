package de.timo_heise.timos_sink_mod.menus.sink;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class SinkSurvivalConfigScreen extends AbstractSinkConfigScreen {

    protected SinkSurvivalConfigScreen(SinkMenu menu, Inventory inv, Component originalTitle, boolean isCreative) {
        super(menu, inv, Component.translatable("gui.timos_sink_mod.config"), isCreative);
        this.originalTitle = originalTitle;
    }

    @Override
    protected AbstractSinkScreen.SinkTabs getTabType() {
        return AbstractSinkScreen.SinkTabs.SURVIVAL_CONFIG;
    }
}
