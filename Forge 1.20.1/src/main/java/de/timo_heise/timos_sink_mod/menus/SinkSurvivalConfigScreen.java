package de.timo_heise.timos_sink_mod.menus;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

public class SinkSurvivalConfigScreen extends AbstractSinkConfigScreen {

    protected SinkSurvivalConfigScreen(Component pTitle, SinkScreen oldSinkScreen, BlockPos blockPos, boolean isCreative) {
        super(pTitle, oldSinkScreen, blockPos, isCreative);
    }

    @Override
    protected SinkScreenUtil.SinkTabs getTabType() {
        return SinkScreenUtil.SinkTabs.SURVIVAL_CONFIG;
    }
}
