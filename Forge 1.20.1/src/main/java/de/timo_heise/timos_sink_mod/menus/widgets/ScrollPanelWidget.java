package de.timo_heise.timos_sink_mod.menus.widgets;

import de.timo_heise.timos_sink_mod.TimosSinkMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ScrollPanelWidget extends AbstractWidget implements Renderable {
    private final int WIDTH = 162;
    private final int HEIGHT = 55;
    private final int PANNELWIDTH = 145;
    private final int PANNELHEIGHT = 53;
    private final int x;
    private final int y;
    private List<AbstractWidget> widgets = new ArrayList<>();
    private List<Renderable> renderables = new ArrayList<>();
    private Map<Renderable, Consumer<Integer>> ySetters = new HashMap<>();
    private int panelScroll = 0;

    public ScrollPanelWidget(int pX, int pY, Component pMessage) {
        super(pX, pY, 162, 55, pMessage);
        x = pX;
        y = pY;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.blit(ResourceLocation.fromNamespaceAndPath(TimosSinkMod.MOD_ID, "textures/gui/sink/sink.png"), x,y, 18, 166, WIDTH, HEIGHT);
        guiGraphics.blit(ResourceLocation.fromNamespaceAndPath(TimosSinkMod.MOD_ID, "textures/gui/sink/sink.png"), x+149, y+1, (getMaxScroll()!=0) ? 176 : 188,  0, 12, 15);
        guiGraphics.enableScissor(x, y, x+WIDTH, y+HEIGHT);
        for(Renderable renderable : renderables) {
            if(renderable instanceof IRenderableWithSeperateTooltip)
            {
                ((IRenderableWithSeperateTooltip) renderable).renderWithoutTooltip(guiGraphics, mouseX, mouseY, partialTick);
                guiGraphics.disableScissor();
                ((IRenderableWithSeperateTooltip) renderable).renderTooltip(guiGraphics, mouseX, mouseY, partialTick);
                guiGraphics.enableScissor(x, y, x+WIDTH, y+HEIGHT);
            }
            else {
                renderable.render(guiGraphics, mouseX, mouseY, partialTick);
            }
        }
        guiGraphics.disableScissor();
    }

    private int getMaxScroll() {
        return 0;
    }

    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
        return super.mouseScrolled(pMouseX, pMouseY, pDelta);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {

    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
//        if (pKeyCode == 265 || pKeyCode == 264) {
//            //LogUtils.getLogger().debug("keycode: "+pKeyCode);
//        }
        for (AbstractWidget widget : widgets) {
            if (widget.keyPressed(pKeyCode, pScanCode, pModifiers)) {return true;}
        }
        return super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }

    @Override
    public boolean keyReleased(int pKeyCode, int pScanCode, int pModifiers) {
        return super.keyReleased(pKeyCode, pScanCode, pModifiers);
    }

    @Override
    public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
        for(AbstractWidget widget : widgets) {
            if(widget.mouseReleased(pMouseX, pMouseY, pButton)) {return true;}
        }
        return super.mouseReleased(pMouseX, pMouseY, pButton);
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        for(AbstractWidget widget : widgets) {
            if(widget.mouseClicked(pMouseX, pMouseY, pButton)) {return true;}
        }
        return false;
    }

    public void addWidget(AbstractWidget widget) {
        widgets.add(widget);
        ySetters.put(widget, widget::setY);
    }

    public void addRenderableWidget(AbstractWidget widget) {
        addWidget(widget);
        renderables.add(widget);
    }

    public void addRenderableOnly(Renderable renderable, Consumer<Integer> ySetter) {
        renderables.add(renderable);
        ySetters.put(renderable, ySetter);
    }

}
