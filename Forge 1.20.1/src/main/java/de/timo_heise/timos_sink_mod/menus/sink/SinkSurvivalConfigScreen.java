package de.timo_heise.timos_sink_mod.menus.sink;

import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.logging.LogUtils;
import de.timo_heise.timos_sink_mod.menus.widgets.FluidWidget;
import de.timo_heise.timos_sink_mod.menus.widgets.ScrollPanelWidget;
import de.timo_heise.timos_sink_mod.menus.widgets.SinkScrollPanelForge;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.client.gui.widget.ScrollPanel;
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
        LogUtils.getLogger().debug("Top Pos: "+topPos);
    }

    @Override
    protected void init() {
        super.init();
//        ScrollPanelWidget tempScrollWidget = new ScrollPanelWidget(leftPos+7, topPos+15, null);
//        FluidWidget tempFluidWidget = new FluidWidget(leftPos+10, topPos+17, font, new FluidStack(Fluids.WATER, 1));
//        tempScrollWidget.addRenderableOnly(tempFluidWidget, tempFluidWidget::setY);
//        Button tempButton = Button.builder(Component.literal("aaaa"), x -> {LogUtils.getLogger().debug("tempButton");}).bounds(leftPos+20, topPos+17, 50, 18).build();
//        tempScrollWidget.addWidget(tempButton);
//        addRenderableWidget(tempScrollWidget);

//        ScrollPanel forgeScroll = new ScrollPanel(minecraft, 100, 50, topPos+15, leftPos+7) {
//
//            @Override
//            public void updateNarration(NarrationElementOutput pNarrationElementOutput) {
//
//            }
//
//            @Override
//            public NarrationPriority narrationPriority() {
//                return NarrationPriority.NONE;
//            }
//
//            @Override
//            protected int getContentHeight() {
//                return 100;
//            }
//
//            @Override
//            protected void drawPanel(GuiGraphics guiGraphics, int entryRight, int relativeY, Tesselator tess, int mouseX, int mouseY) {
//
//            }
//
//            @Override
//            public boolean mouseClicked(double mouseX, double mouseY, int button) {
//                addRenderableOnly(new FluidWidget(leftPos+10, topPos+17, font, new FluidStack(Fluids.WATER, 1)));
//                return super.mouseClicked(mouseX, mouseY, button);
//            }
//        };
//        addRenderableWidget(forgeScroll);
        SinkScrollPanelForge tempSinkScrollPanel = new SinkScrollPanelForge(minecraft, leftPos+7, topPos+15, 100, 50);
        tempSinkScrollPanel.addRenderableOnly(new FluidWidget(leftPos+10, topPos+17, font, new FluidStack(Fluids.WATER, 1)), /*topPos+17+*/18-18);
        addRenderableOnly(tempSinkScrollPanel);
    }
}
