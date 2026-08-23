package de.timo_heise.timos_sink_mod.menus.sink;

import com.mojang.blaze3d.systems.RenderSystem;
import de.timo_heise.timos_sink_mod.TimosSinkMod;
import de.timo_heise.timos_sink_mod.blocks.ModBlocks;
import de.timo_heise.timos_sink_mod.menus.ClientUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

public abstract class AbstractSinkScreen extends AbstractContainerScreen<SinkMenu>  {
    public enum SinkTabs {SINK, SURVIVAL_CONFIG, CREATIVE_CONFIG}
    public static final Component[] components = new Component[] {Component.translatable("block.timos_sink_mod.sink"), Component.translatable("gui.timos_sink_mod.config"), Component.translatable("gui.timos_sink_mod.admin_config")}; // order has to be same as in SinkTabs enum

    protected final boolean isCreative;
    protected final ResourceLocation GUI_TEXTURE;
    protected Inventory inv;
    protected Component originalTitle;
    private ClientUtil.RectPos[] tabCoords;

    public AbstractSinkScreen(SinkMenu menu, Inventory inv, Component title, boolean isCreative, ResourceLocation GUI_TEXTURE) {
        super(menu, inv, title);
        this.inv = inv;
        this.isCreative = isCreative;
        this.GUI_TEXTURE = GUI_TEXTURE;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI_TEXTURE);

        RenderSystem.enableDepthTest();
        tabCoords = renderTabButtons(guiGraphics, isCreative, getTabType(), leftPos + imageWidth, topPos);
        guiGraphics.blit(GUI_TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);
        RenderSystem.disableDepthTest();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (checkButtonPressed(mouseX, mouseY)) {return true;}
        return super.mouseClicked(mouseX, mouseY, button);
    }


    private boolean checkButtonPressed(double mouseX, double mouseY) {
        for (SinkTabs tab : SinkTabs.values()) {
            ClientUtil.RectPos c = tabCoords[tab.ordinal()];
            if (tab == getTabType() || c == null) {continue;}
            if(ClientUtil.betterIsHovering(c.x(), c.y(), c.width(), c.height()-4, mouseX, mouseY)) {
                switchScreen(tab);
                return true;
            }
        }
        return false;
    }

    private void switchScreen(SinkTabs tab) {
        switch(tab) {
            case SINK:
                minecraft.setScreen(new SinkScreen(menu, inv, originalTitle, isCreative));
                break;
            case SURVIVAL_CONFIG:
                minecraft.setScreen(new SinkSurvivalConfigScreen(menu, inv, originalTitle, isCreative));
                break;
            case CREATIVE_CONFIG:
                minecraft.setScreen(new SinkCreativeConfigScreen(menu, inv, originalTitle, isCreative));
                break;
        }
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderTooltip(guiGraphics, mouseX, mouseY);
        for(AbstractSinkScreen.SinkTabs tab : AbstractSinkScreen.SinkTabs.values()) {
            ClientUtil.RectPos c = tabCoords[tab.ordinal()];
            if(c == null) {continue;}
            if(ClientUtil.betterIsHovering(c.x(), c.y(), c.width(), c.height()-4, mouseX, mouseY)) {
                guiGraphics.renderTooltip(font, AbstractSinkScreen.components[tab.ordinal()], (int) mouseX, (int) mouseY);
            }
        }
    }

    private static ClientUtil.RectPos[] renderTabButtons(GuiGraphics guiGraphics, boolean isCreative, AbstractSinkScreen.SinkTabs selectedTab, int rightPos, int topPos) {
        ClientUtil.RectPos[] poss = new ClientUtil.RectPos[AbstractSinkScreen.SinkTabs.values().length];
        poss[AbstractSinkScreen.SinkTabs.SINK.ordinal()] = renderTabButton(guiGraphics, (selectedTab == AbstractSinkScreen.SinkTabs.SINK), isCreative ? 2 : 1, ModBlocks.SINK_BLOCK.get(), rightPos, topPos);
        poss[AbstractSinkScreen.SinkTabs.SURVIVAL_CONFIG.ordinal()] = renderTabButton(guiGraphics, (selectedTab == AbstractSinkScreen.SinkTabs.SURVIVAL_CONFIG), isCreative ? 1 : 0, Items.CRAFTING_TABLE, rightPos, topPos);
        if(isCreative) { poss[AbstractSinkScreen.SinkTabs.CREATIVE_CONFIG.ordinal()] = renderTabButton(guiGraphics, (selectedTab == AbstractSinkScreen.SinkTabs.CREATIVE_CONFIG), 0, Items.COMMAND_BLOCK, rightPos, topPos); }
        return poss;
    }

    private static ClientUtil.RectPos renderTabButton(GuiGraphics guiGraphics, boolean isSelected, int index, ItemLike icon, int rightPos, int topPos) {
        ClientUtil.RectPos pos = new ClientUtil.RectPos(rightPos - (26+1) * (index+1) + 1, topPos - 32 + 4, 26, 32);
        int resourceX = (6-index) * 26; // position of texture in the resource
        int resourceY = 0;

        if (isSelected) { resourceY += 32; }

        com.mojang.blaze3d.systems.RenderSystem.enableBlend(); //Forge: Make sure blend is enabled else tabs show a white border. (idk man, I just copied this from vanilla)
        guiGraphics.pose().pushPose();
        if (isSelected) {guiGraphics.pose().translate(0.0F, 0.0F, 1.0F);}
        guiGraphics.blit(ResourceLocation.fromNamespaceAndPath(TimosSinkMod.MOD_ID, "textures/gui/sink/tabs.png"), pos.x(), pos.y(), resourceX, resourceY, pos.width(), pos.height());
        guiGraphics.pose().translate(0.0F, 0.0F, 1.0F);
        if (icon != null) {
            ItemStack itemstack = new ItemStack(icon, 1);
            guiGraphics.renderItem(itemstack, pos.x()+5, pos.y()+9);
        }
        guiGraphics.pose().popPose();
        return pos;
    }

    protected abstract SinkTabs getTabType();
}
