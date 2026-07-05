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

    private record SinkTab (ItemLike icon, Component title, int[] coords, Object idk) {}
    private final List<SinkTab> tabs;
    private int selectedTabIndex = 0;
    private final boolean isCreative;

    public SinkScreen(SinkMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
        isCreative = inv.player.isCreative();
        if (isCreative) {
            tabs = List.of(new SinkTab(ModBlocks.SINK_BLOCK.get(), Component.translatable("block.timos_sink_mod.sink"), new int[4], null),
                    new SinkTab(Items.CRAFTING_TABLE, Component.translatable("gui.timos_sink_mod.config"), new int[4], null),
                    new SinkTab(Items.COMMAND_BLOCK, Component.translatable("gui.timos_sink_mod.admin_config"), new int[4], null) );
        }
        else {
            tabs = List.of(new SinkTab(ModBlocks.SINK_BLOCK.get(), Component.translatable("block.timos_sink_mod.sink"), new int[4], null),
                    new SinkTab(Items.CRAFTING_TABLE, Component.translatable("gui.timos_sink_mod.config"), new int[4], null) );
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        for(SinkTab tab : tabs) {
            checkMouseHovering(guiGraphics, tab, mouseX, mouseY);
        }
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI_TEXTURE);

        for(int i = 0; i<tabs.size(); i++) {
            if(i != selectedTabIndex)
            { renderTabButton(guiGraphics, false, tabs.size()-i-1, tabs.get(i).icon, tabs.get(i).coords); }
        }

        guiGraphics.blit(GUI_TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);

        renderTabButton(guiGraphics, true, tabs.size()-selectedTabIndex-1, tabs.get(selectedTabIndex).icon, tabs.get(selectedTabIndex).coords);
    }

    @Override
    protected void init() {
        super.init();

        Button btn = Button.builder(Component.literal("Config"), b -> {
        }).bounds(leftPos + 20, topPos + 20, 120, 20).build();
        addRenderableWidget(btn);
    }

    private void renderTabButton(GuiGraphics guiGraphics, boolean isSelected, int index, ItemLike icon, int[] coords) {
        index = 6 - index;
        coords[2] = 26; // cords: {x,y,width,height}
        coords[3] = 32;
        coords[0] = leftPos + imageWidth - (coords[2]+1) * (7 - index) + 1;
        coords[1] = topPos - coords[3] + 4;
        int resourceX = index * 26; // position of texture in the resource
        int resourceY = 0;

        if (isSelected) { resourceY += 32; }

        com.mojang.blaze3d.systems.RenderSystem.enableBlend(); //Forge: Make sure blend is enabled else tabs show a white border.
        guiGraphics.blit(ResourceLocation.fromNamespaceAndPath(TimosSinkMod.MOD_ID, "textures/gui/sink/tabs.png"), coords[0], coords[1], resourceX, resourceY, coords[2], coords[3]);
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0.0F, 0.0F, 100.0F);
        if (icon != null) {
            ItemStack itemstack = new ItemStack(icon, 1);
            guiGraphics.renderItem(itemstack, coords[0]+5, coords[1]+9);
            guiGraphics.renderItemDecorations(this.font, itemstack, coords[0]+5, coords[1]+9);
        }
        guiGraphics.pose().popPose();
    }

    private void checkMouseHovering(GuiGraphics guiGraphics, SinkTab tab, double mouseX, double mouseY) {
        if(isHovering(tab.coords[0], tab.coords[1], tab.coords[2], tab.coords[3], mouseX+leftPos, mouseY+topPos)) {
            guiGraphics.renderTooltip(font, tab.title, (int) mouseX, (int) mouseY);
        }
    }
}
