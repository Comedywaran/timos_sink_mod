package de.timo_heise.timos_sink_mod.menus;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

public class SinkCreativeConfigScreen extends AbstractSinkConfigScreen {

    protected SinkCreativeConfigScreen(Component pTitle, SinkScreen oldSinkScreen, BlockPos blockPos, boolean isCreative) {
        super(pTitle, oldSinkScreen, blockPos, isCreative);
    }

    @Override
    protected SinkScreenUtil.SinkTabs getTabType() {
        return SinkScreenUtil.SinkTabs.CREATIVE_CONFIG;
    }
}
