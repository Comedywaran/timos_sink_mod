package de.timo_heise.timos_sink_mod.menus;

import com.mojang.blaze3d.systems.RenderSystem;
import de.timo_heise.timos_sink_mod.TimosSinkMod;
import de.timo_heise.timos_sink_mod.blocks.ModBlocks;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import java.util.List;

public class SinkScreen extends AbstractContainerScreen<SinkMenu> {
    private static final ResourceLocation GUI_TEXTURE = ResourceLocation.fromNamespaceAndPath(TimosSinkMod.MOD_ID, "textures/gui/sink/sink.png");

    private record SinkTab (ItemLike icon, Component title, Object idk) {}
    private final List<SinkTab> tabs;
    private int selectedTabIndex = 0;
    private final boolean isCreative;

    public SinkScreen(SinkMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
        isCreative = inv.player.isCreative();
        if (isCreative) {
            tabs = List.of(new SinkTab(ModBlocks.SINK_BLOCK.get(), Component.translatable("block.timos_sink_mod.sink"), null),
                    new SinkTab(Items.CRAFTING_TABLE, Component.translatable("gui.timos_sink_mod.config"), null),
                    new SinkTab(Items.COMMAND_BLOCK, Component.translatable("gui.timos_sink_mod.admin_config"), null) );
        }
        else {
            tabs = List.of(new SinkTab(ModBlocks.SINK_BLOCK.get(), Component.translatable("block.timos_sink_mod.sink"), null),
                    new SinkTab(Items.CRAFTING_TABLE, Component.translatable("gui.timos_sink_mod.config"), null) );
        }
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI_TEXTURE);

        for(int i = 0; i<tabs.size(); i++) {
            if(i != selectedTabIndex)
            { renderTabButton(guiGraphics, false, tabs.size()-i-1, tabs.get(i).icon); }
        }

        guiGraphics.blit(GUI_TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);

        renderTabButton(guiGraphics, true, tabs.size()-selectedTabIndex-1, tabs.get(selectedTabIndex).icon);
    }

    @Override
    protected void init() {
        super.init();

        Button btn = Button.builder(Component.literal("Config"), b -> {
        }).bounds(leftPos + 20, topPos + 20, 120, 20).build();
        addRenderableWidget(btn);
    }

    private void renderTabButton(GuiGraphics guiGraphics, boolean isSelected, int index, ItemLike icon) {
        index = 6 - index;
        final int tabWidth = 26;
        final int tabHeight = 32;
        final boolean isTopTab = true; // kinda useless since all are at the top
        int resourceX = index * tabWidth; // position of texture in the resource
        int resourceY = 0;
        int x = this.leftPos + this.imageWidth - 27 * (7 - index) + 1; // position of the tab button
        int y = this.topPos;
        if (isSelected) {
            resourceY += tabHeight;
        }

        if (isTopTab) {
            y -= 28;
        } else {
            resourceY += 2*tabHeight;
            y += this.imageHeight - 4;
        }

        com.mojang.blaze3d.systems.RenderSystem.enableBlend(); //Forge: Make sure blend is enabled else tabs show a white border.
        guiGraphics.blit(ResourceLocation.fromNamespaceAndPath(TimosSinkMod.MOD_ID, "textures/gui/sink/tabs.png"), x, y, resourceX, resourceY, tabWidth, tabHeight);
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0.0F, 0.0F, 100.0F);
        x += 5;
        y += 8 + (isTopTab ? 1 : -1);
        if (icon != null) {
            ItemStack itemstack = new ItemStack(icon, 1);
            guiGraphics.renderItem(itemstack, x, y);
            guiGraphics.renderItemDecorations(this.font, itemstack, x, y);
        }
        guiGraphics.pose().popPose();
    }
}
