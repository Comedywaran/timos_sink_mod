package de.timo_heise.timos_sink_mod.menus;

import com.mojang.blaze3d.systems.RenderSystem;
import de.timo_heise.timos_sink_mod.TimosSinkMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public abstract class AbstractSinkConfigScreen extends Screen {
    protected int leftPos;
    protected int topPos;
    protected int imageWidth = 176;
    protected int imageHeight = 166;
    private static final ResourceLocation GUI_TEXTURE = ResourceLocation.fromNamespaceAndPath(TimosSinkMod.MOD_ID, "textures/gui/sink/blank_gui.png");
    private final BlockPos blockPos;
    private final boolean isCreative;
    private int[][] tabCoords;
    private final SinkScreen oldSinkScreen;

    protected AbstractSinkConfigScreen(Component pTitle, SinkScreen oldSinkScreen, BlockPos blockPos, boolean isCreative) {
        super(pTitle);
        this.blockPos = blockPos;
        this.isCreative = isCreative;
        this.oldSinkScreen = oldSinkScreen;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBg(guiGraphics, partialTick, mouseX, mouseY);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        SinkScreenUtil.renderTooltips(guiGraphics, tabCoords, mouseX, mouseY, font);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int pButton) {
        if (SinkScreenUtil.checkButtonPressed(tabCoords, getTabType(), mouseX, mouseY, minecraft, oldSinkScreen, blockPos, isCreative)) {return true;}
        return super.mouseClicked(mouseX, mouseY, pButton);
    }

    private void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI_TEXTURE);

        RenderSystem.enableDepthTest();
        tabCoords = SinkScreenUtil.renderTabButtons(guiGraphics, isCreative, getTabType(), leftPos + imageWidth, topPos);
        guiGraphics.blit(GUI_TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);
        RenderSystem.disableDepthTest(); // is this a problem if the DepthTest was enabled before?
    }

    @Override
    protected void init() {
        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;
    }

    protected abstract SinkScreenUtil.SinkTabs getTabType();
}
