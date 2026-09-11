package de.timo_heise.timos_sink_mod.menus.widgets.ScrollPanel;

import com.mojang.blaze3d.vertex.Tesselator;
import de.timo_heise.timos_sink_mod.menus.ClientUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraftforge.client.gui.widget.ScrollPanel;

import java.util.*;

/**
 * code inspired by {@link net.minecraftforge.client.gui.ModListScreen.InfoPanel}
 * <br>TODO: widgets, fancy graphics, mouse hovering outside of panel, getContentHeight(), getScrollAmount()?, comments
 */
public class SinkScrollPanel extends ScrollPanel {
    private final Set<AbstractWidget> widgets = new LinkedHashSet<>();
    private final Set<Renderable> renderables = new LinkedHashSet<>();
    private int yOffset;
    private static final int BORDER = 4;
    private static final int BARWIDTH = 6;

    public SinkScrollPanel(Minecraft mc, int x, int y, int width, int height)
    {
        super(mc, width, height, y, x, BORDER, BARWIDTH);
    }

    @Override
    public int getContentHeight()
    {
        return 100;
        //return Math.max(contentHeight, this.bottom - this.top - 8);
    }

//    @Override
//    protected int getScrollAmount()
//    {
//        return font.lineHeight * 3;
//    }


    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        if (ClientUtil.betterIsHovering(left, top, width-BARWIDTH, height, mouseX, mouseY)) {
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(0, yOffset, 0);
            for(Renderable renderable : renderables) {
                if(renderable instanceof IRenderableWithSeparateTooltip) {
                    ((IRenderableWithSeparateTooltip) renderable).renderTooltip(guiGraphics, mouseX, mouseY- yOffset, partialTick);
                }
            }
            guiGraphics.pose().popPose();
        }
    }

    @Override
    protected void drawPanel(GuiGraphics guiGraphics, int entryRight, int relativeY, Tesselator tess, int mouseX, int mouseY)
    {
        yOffset = relativeY-this.top;
        float partialTick = Minecraft.getInstance().getFrameTime();
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0, yOffset, 0);

        for(Renderable renderable : renderables) {
            if(renderable instanceof IRenderableWithSeparateTooltip) {
                ((IRenderableWithSeparateTooltip) renderable).renderWithoutTooltip(guiGraphics, mouseX, mouseY-yOffset, partialTick);
            }
            else {
                renderable.render(guiGraphics, mouseX, mouseY-yOffset, partialTick);
            }
        }

        guiGraphics.pose().popPose();
    }

    @Override
    public NarrationPriority narrationPriority() {
        return NarrationPriority.NONE;
    }

    @Override
    public void updateNarration(NarrationElementOutput pNarrationElementOutput) {}

    public void addWidget(AbstractWidget widget) {
        widgets.add(widget);
    }

    public void addRenderableWidget(AbstractWidget widget) {
        widgets.add(widget);
        renderables.add(widget);
    }

    public void addRenderableOnly(AbstractWidget widget) {
        renderables.add(widget);
    }

    public void addRenderableOnly(Renderable renderable) {
        renderables.add(renderable);
        //contentHeight = Math.max(contentHeight, bottomPos);
    }
}
