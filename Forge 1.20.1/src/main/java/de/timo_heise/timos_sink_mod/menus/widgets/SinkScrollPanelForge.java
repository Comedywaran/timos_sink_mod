package de.timo_heise.timos_sink_mod.menus.widgets;

import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.client.gui.widget.ScrollPanel;

import java.util.*;
import java.util.function.Consumer;

/**
 * code inspired by {@link net.minecraftforge.client.gui.ModListScreen.InfoPanel}
 */
public class SinkScrollPanelForge extends ScrollPanel {
//    private ResourceLocation logoPath;
//    private Size2i logoDims = new Size2i(0, 0);
//    private List<FormattedCharSequence> lines = Collections.emptyList();
    private final Set<AbstractWidget> widgets = new LinkedHashSet<>();
    private final Set<Renderable> renderables = new LinkedHashSet<>();
    private int contentHeight = 0;
    private int lastRelativeY;

    public SinkScrollPanelForge(Minecraft mc, int x, int y, int width, int height)
    {
        super(mc, width, height, y, x);
    }

    @Override
    public int getContentHeight()
    {
        return 300;
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

        for(Renderable renderable : renderables) {
            if(renderable instanceof IRenderableWithSeperateTooltip) {
                ((IRenderableWithSeperateTooltip) renderable).renderTooltip(guiGraphics, mouseX, mouseY-lastRelativeY, partialTick);
            }
        }
    }

    @Override
    protected void drawPanel(GuiGraphics guiGraphics, int entryRight, int relativeY, Tesselator tess, int mouseX, int mouseY)
    {
        LogUtils.getLogger().debug("relative Y: "+ relativeY);
        float partialTick = Minecraft.getInstance().getFrameTime();
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0, relativeY, 0);
        for(Renderable renderable : renderables) {
            if(renderable instanceof IRenderableWithSeperateTooltip) {
                ((IRenderableWithSeperateTooltip) renderable).renderWithoutTooltip(guiGraphics, mouseX, mouseY-relativeY, partialTick);
            }
            else {
                renderable.render(guiGraphics, mouseX, mouseY-relativeY, partialTick);
            }
        }
        lastRelativeY = relativeY;
        guiGraphics.pose().popPose();
//        if (logoPath != null) {
//            RenderSystem.enableBlend();
//            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
//            // Draw the logo image inscribed in a rectangle with width entryWidth (minus some padding) and height 50
//            int headerHeight = 50;
//            guiGraphics.blitInscribed(logoPath, left + PADDING, relativeY, width - (PADDING * 2), headerHeight, logoDims.width, logoDims.height, false, true);
//            relativeY += headerHeight + PADDING;
//        }
//
//        for (FormattedCharSequence line : lines)
//        {
//            if (line != null)
//            {
//                RenderSystem.enableBlend();
//                guiGraphics.drawString(ModListScreen.this.font, line, left + PADDING, relativeY, 0xFFFFFF);
//                RenderSystem.disableBlend();
//            }
//            relativeY += font.lineHeight;
//        }
//
//        final Style component = findTextLine(mouseX, mouseY);
//        if (component!=null) {
//            guiGraphics.renderComponentHoverEffect(ModListScreen.this.font, component, mouseX, mouseY);
//        }
    }

//    private Style findTextLine(final int mouseX, final int mouseY) {
//        if (!isMouseOver(mouseX, mouseY))
//            return null;
//
//        double offset = (mouseY - top - PADDING - border) + scrollDistance;
//        if (logoPath != null) {
//            offset -= 50;
//        }
//        if (offset <= 0)
//            return null;
//
//        int lineIdx = (int) (offset / font.lineHeight);
//        if (lineIdx >= lines.size() || lineIdx < 0)
//            return null;
//
//        FormattedCharSequence line = lines.get(lineIdx);
//        if (line != null)
//        {
//            return font.getSplitter().componentStyleAtWidth(line, mouseX - left - border);
//        }
//        return null;
//    }
//
//    @Override
//    public boolean mouseClicked(final double mouseX, final double mouseY, final int button) {
//        final Style component = findTextLine((int) mouseX, (int) mouseY);
//        if (component != null) {
//            ModListScreen.this.handleComponentClicked(component);
//            return true;
//        }
//        return super.mouseClicked(mouseX, mouseY, button);
//    }

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
        contentHeight = Math.max(contentHeight, widget.getY() + widget.getHeight());
    }

    public void addRenderableOnly(Renderable renderable, int bottomPos) {
        renderables.add(renderable);
        contentHeight = Math.max(contentHeight, bottomPos);
    }
}
