package de.timo_heise.timos_sink_mod.menus.widgets.ScrollPanel;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.logging.LogUtils;
import de.timo_heise.timos_sink_mod.TimosSinkMod;
import de.timo_heise.timos_sink_mod.menus.ClientUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.widget.ScrollPanel;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Supplier;
/**
 * code inspired by {@link net.minecraftforge.client.gui.ModListScreen.InfoPanel}
 * <br>TODO: widgets, clicking on scroll bar, fancy scroll bar?, getScrollAmount()?
 */
public class SinkScrollPanel extends ScrollPanel {
    protected final Set<AbstractWidget> widgets = new LinkedHashSet<>();
    protected final Map<Renderable, Supplier<Integer>> renderables = new LinkedHashMap<>();
    protected int yOffset; // how much the panel is scrolled down (so 0 if not scrolled)
    protected static final int EXTRAYSPACE = 4; // adds some space below the last renderable for aesthetic reasons
    protected static final int SPACEBETWEENPANELANDBAR = 3; // space between the panel and the scroll bar on the right for texture
    protected static final int BARWIDTH = 6; // width of the scroll bar on the right
    protected static final ResourceLocation resourceLocation = ResourceLocation.fromNamespaceAndPath(TimosSinkMod.MOD_ID, "textures/gui/sink/sink.png");
    protected boolean scrollable; //TODO: add grayed out scroll bar

    /**
     * note that the actual panel will be 1 pixel smaller in every direction since this pixel is reserved for a border around the panel; also leave space to the right since the scrollbar is there
     */
    public SinkScrollPanel(Minecraft mc, int x, int y, int width, int height) {
        this(mc, x, y, width, height, getBGColor(), 0xFF8B8B8B, 0xFF555555, 0xFFC6C6C6);
    }

    /**
     * note that the actual panel will be 1 pixel smaller in every direction since this pixel is reserved for a border around the panel; also leave space to the right since the scrollbar is there
     */
    protected SinkScrollPanel(Minecraft mc, int x, int y, int width, int height, int bgColor, int barBgColor, int barColor, int barBorderColor)
    {
        super(mc, width-2, height-2, y+1, x+1, 0, BARWIDTH, bgColor, bgColor, barBgColor, barColor, barBorderColor);
    }

    @Override
    public int getContentHeight()
    {
        int contentHeight = 0;
        for(Supplier<Integer> supplier: renderables.values()) {
            contentHeight = Math.max(contentHeight, supplier.get());
        }
        contentHeight += EXTRAYSPACE;
        scrollable = contentHeight > this.height;
        return Math.max(contentHeight, this.height);
    }

//    @Override
//    protected int getScrollAmount()
//    {
//        return font.lineHeight * 3;
//    }


    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        drawFrame(guiGraphics);

        if (ClientUtil.betterIsHovering(left, top, width-SPACEBETWEENPANELANDBAR-BARWIDTH, height, mouseX, mouseY)) {
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(0, yOffset, 0);
            for(Renderable renderable : renderables.keySet()) {
                if(renderable instanceof IRenderableWithSeparateTooltip) {
                    ((IRenderableWithSeparateTooltip) renderable).renderTooltip(guiGraphics, mouseX, mouseY- yOffset, partialTick);
                }
            }
            guiGraphics.pose().popPose();
        }
    }

    /**
     * drawes the frame around the panel
     */
    protected void drawFrame(GuiGraphics guiGraphics) {

        int panelRight = right-SPACEBETWEENPANELANDBAR-BARWIDTH;
        for(int i = 1; i <= width-SPACEBETWEENPANELANDBAR-BARWIDTH-2; i++) {
            // top edge:
            guiGraphics.blit(resourceLocation, left+i, top-1, 23, 168, 1, 2);
            // bottom edge:
            guiGraphics.blit(resourceLocation, left+i, top+height-1, 23, 173, 1, 2);
        }
        for(int i = 1; i <= height-2; i++) {
            // left edge:
            guiGraphics.blit(resourceLocation, left-1, top+i, 20, 171, 2, 1);
            // right edge:
            guiGraphics.blit(resourceLocation, panelRight-1, top+i, 25, 171, 11, 1);
        }
        
        // corners:
        // top left:
        guiGraphics.blit(resourceLocation, left-1, top-1, 20, 168, 2, 2);
        // top right:
        guiGraphics.blit(resourceLocation, panelRight-1, top-1, 25, 168, 11, 2);
        // bottom left:
        guiGraphics.blit(resourceLocation, left-1, bottom-1, 20, 173, 2, 2);
        // bottom right:
        guiGraphics.blit(resourceLocation, panelRight-1, bottom-1, 25, 173, 11, 2);
    }

    @Override
    protected void drawPanel(GuiGraphics guiGraphics, int entryRight, int relativeY, Tesselator tess, int mouseX, int mouseY)
    {
        boolean mouseInPanel = mouseY >= (double) top && mouseY < (double) bottom; // only checks y-coordinate, since x doesn't change
        yOffset = relativeY-this.top;
        float partialTick = Minecraft.getInstance().getFrameTime();
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0, yOffset, 0);

        for(Renderable renderable : renderables.keySet()) {
            if(renderable instanceof IRenderableWithSeparateTooltip) {
                // top-100-getContentHeight() is just that the renderable doesn't think the player is hovering over it, may break some renderable
                ((IRenderableWithSeparateTooltip) renderable).renderWithoutTooltip(guiGraphics, mouseX, mouseInPanel ? mouseY-yOffset : top-100-getContentHeight(), partialTick);
            }
            else {
                // see above
                renderable.render(guiGraphics, mouseX, mouseInPanel ? mouseY-yOffset : top-100-getContentHeight(), partialTick);
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

    /**
     * adds an {@link AbstractWidget} to be accessible (but not rendered) inside the {@link SinkScrollPanel}
     * @param widget
     */
    public void addWidget(AbstractWidget widget) {
        widgets.add(widget);
    }

    /**
     * adds an {@link AbstractWidget} to be rendered and accessible inside the {@link SinkScrollPanel}.
     * @param widget the widget to be added; uses {@code widget.getY() + widget.getHeight()} for the bottom coordinate
     */
    public void addRenderableWidget(AbstractWidget widget) {
        addWidget(widget);
        addRenderableOnly(widget);
    }

    /**
     * adds an {@link AbstractWidget} to be rendered and accessible inside the {@link SinkScrollPanel}.
     * @param widget the widget to be added
     * @param bottom the bottom coordinate of the renderable, used to calculate the panel's scrollable range; {@code -1} if this renderable should be ignored
     */
    public void addRenderableWidget(AbstractWidget widget, int bottom) {
        addWidget(widget);
        addRenderableOnly(widget, bottom);
    }

    /**
     * adds an {@link AbstractWidget} to be rendered and accessible inside the {@link SinkScrollPanel}.
     * @param widget the widget to be added
     * @param bottomGetter a supplier that returns the bottom coordinate of the widget, used to calculate the panel's scrollable range; {@code -1} if this widget should be ignored
     */
    public void addRenderableWidget(AbstractWidget widget, Supplier<Integer> bottomGetter) {
        addWidget(widget);
        addRenderableOnly(widget, bottomGetter);
    }

    /**
     * adds a {@link Renderable} to be rendered inside the {@link SinkScrollPanel}.
     * @param widget the renderable to be rendered; uses {@code widget.getY() + widget.getHeight()} for the bottom coordinate
     */
    public void addRenderableOnly(AbstractWidget widget) {
        renderables.put(widget, () -> widget.getY() + widget.getHeight());
    }

    /**
     * adds a {@link Renderable} to be rendered inside the {@link SinkScrollPanel}.
     * @param renderable the renderable to be rendered
     * @param bottom the bottom coordinate of the renderable, used to calculate the panel's scrollable range; {@code -1} if this renderable should be ignored
     */
    public void addRenderableOnly(Renderable renderable, int bottom) {
        renderables.put(renderable, () -> bottom);
    }

    /**
     * adds a {@link Renderable} to be rendered inside the {@link SinkScrollPanel}.
     * @param renderable the renderable to be rendered
     * @param bottomGetter a supplier that returns the bottom coordinate of the renderable, used to calculate the panel's scrollable range; {@code -1} if this renderable should be ignored
     */
    public void addRenderableOnly(Renderable renderable, Supplier<Integer> bottomGetter) {
        renderables.put(renderable, bottomGetter);
    }

    private static int getBGColor() {
        try (InputStream stream = Minecraft.getInstance().getResourceManager().getResource(resourceLocation).orElseThrow(() -> new IOException("Texture not found")).open();
             NativeImage image = NativeImage.read(stream)) {

            int abgr = image.getPixelRGBA(23, 171); // don't know why it's called getPixelRGBA() if it returns abgr, but ok I guess
            return (abgr & 0xFF00FF00) | ((abgr & 0x00FF0000) >>> 16) | ((abgr & 0x000000FF) << 16);
        } catch (IOException e) {
            LogUtils.getLogger().warn("Could not load texture \"{}\" for rendering SinkScrollPanel {}", resourceLocation, e);
            return 0x00000000;
        }
    }
}
