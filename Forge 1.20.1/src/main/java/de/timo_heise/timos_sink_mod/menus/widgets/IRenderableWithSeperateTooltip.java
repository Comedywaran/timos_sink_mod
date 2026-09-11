package de.timo_heise.timos_sink_mod.menus.widgets;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;

/**
 * This is a subinterface of Renderable that just adds the 2 methods {@link #renderWithoutTooltip} and {@link #renderTooltip} just for the use in the {@link ScrollPanelWidget} to be able to render the widget only in the {@link ScrollPanelWidget} but the tooltip outside of it. <br>
 * {@link ScrollPanelWidget} will just cut of the tooltip if this is not used.
 */
public interface IRenderableWithSeperateTooltip extends Renderable { //TODO: add this to ItemWidget
    public abstract void renderWithoutTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick);
    public abstract void renderTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick);
}
