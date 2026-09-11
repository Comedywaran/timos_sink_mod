package de.timo_heise.timos_sink_mod.menus.sink;

import de.timo_heise.timos_sink_mod.menus.widgets.FluidWidget;
import de.timo_heise.timos_sink_mod.menus.widgets.ScrollPanel.SinkScrollPanel;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;

public class SinkSurvivalConfigScreen extends AbstractSinkConfigScreen {

    protected SinkSurvivalConfigScreen(SinkMenu menu, Inventory inv, Component originalTitle, boolean isCreative) {
        super(menu, inv, Component.translatable("gui.timos_sink_mod.config"), isCreative);
        this.originalTitle = originalTitle;
    }

    @Override
    protected AbstractSinkScreen.SinkTabs getTabType() {
        return AbstractSinkScreen.SinkTabs.SURVIVAL_CONFIG;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    protected void init() {
        super.init();
        SinkScrollPanel tempSinkScrollPanel = new SinkScrollPanel(minecraft, leftPos+7, topPos+15, 162, 55);
        tempSinkScrollPanel.addRenderableOnly(new FluidWidget(leftPos+10, topPos+17, font, new FluidStack(Fluids.WATER, 1)));
        addRenderableWidget(tempSinkScrollPanel);
    }
}
