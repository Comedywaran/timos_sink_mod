package de.timo_heise.timos_sink_mod.menus;

import com.mojang.logging.LogUtils;
import de.timo_heise.timos_sink_mod.TimosSinkMod;
import de.timo_heise.timos_sink_mod.blocks.ModBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

public final class SinkScreenUtil {
    public enum SinkTabs {SINK, SURVIVAL_CONFIG, CREATIVE_CONFIG}
    public static final Component[] components = new Component[] {Component.translatable("block.timos_sink_mod.sink"), Component.translatable("gui.timos_sink_mod.config"), Component.translatable("gui.timos_sink_mod.admin_config")}; // order has to be same as in SinkTabs enum

    public static int[][] renderTabButtons(GuiGraphics guiGraphics, boolean isCreative, SinkTabs selectedTab, int rightPos, int topPos) {
        int[][] coords = new int[SinkTabs.values().length][];
        coords[SinkTabs.SINK.ordinal()] = renderTabButton(guiGraphics, (selectedTab == SinkTabs.SINK), isCreative ? 2 : 1, ModBlocks.SINK_BLOCK.get(), rightPos, topPos);
        coords[SinkTabs.SURVIVAL_CONFIG.ordinal()] = renderTabButton(guiGraphics, (selectedTab == SinkTabs.SURVIVAL_CONFIG), isCreative ? 1 : 0, Items.CRAFTING_TABLE, rightPos, topPos);
        if(isCreative) { coords[SinkTabs.CREATIVE_CONFIG.ordinal()] = renderTabButton(guiGraphics, (selectedTab == SinkTabs.CREATIVE_CONFIG), 0, Items.COMMAND_BLOCK, rightPos, topPos); }
        return coords;
    }

    private static int[] renderTabButton(GuiGraphics guiGraphics, boolean isSelected, int index, ItemLike icon, int rightPos, int topPos) {
        int[] coords = new int[] {rightPos - (26+1) * (index+1) + 1, topPos - 32 + 4, 26, 32}; // {x,y,width,height}
        int resourceX = (6-index) * 26; // position of texture in the resource
        int resourceY = 0;

        if (isSelected) { resourceY += 32; }

        com.mojang.blaze3d.systems.RenderSystem.enableBlend(); //Forge: Make sure blend is enabled else tabs show a white border.
        guiGraphics.pose().pushPose();
        if (isSelected) {guiGraphics.pose().translate(0.0F, 0.0F, 1.0F);}
        guiGraphics.blit(ResourceLocation.fromNamespaceAndPath(TimosSinkMod.MOD_ID, "textures/gui/sink/tabs.png"), coords[0], coords[1], resourceX, resourceY, coords[2], coords[3]);
        guiGraphics.pose().translate(0.0F, 0.0F, 1.0F);
        if (icon != null) {
            ItemStack itemstack = new ItemStack(icon, 1);
            guiGraphics.renderItem(itemstack, coords[0]+5, coords[1]+9);
        }
        guiGraphics.pose().popPose();
        return coords;
    }

    public static void renderTooltips(GuiGraphics guiGraphics, int[][] tabCoords, int mouseX, int mouseY, Font font) {
        for(SinkScreenUtil.SinkTabs tab : SinkScreenUtil.SinkTabs.values()) {
            int[] c = tabCoords[tab.ordinal()];
            if(c == null) {continue;}
            if(isHovering(c[0], c[1], c[2], c[3]-4, mouseX, mouseY)) {
                guiGraphics.renderTooltip(font, SinkScreenUtil.components[tab.ordinal()], (int) mouseX, (int) mouseY);
            }
        }
    }

    public static boolean checkButtonPressed(int[][] tabCoords, SinkTabs selectedTab, double mouseX, double mouseY) {
        for (SinkTabs tab : SinkTabs.values()) {
            int[] c = tabCoords[tab.ordinal()];
            if (tab == selectedTab || c == null) {continue;}
            if(isHovering(c[0], c[1], c[2], c[3]-4, mouseX, mouseY)) {
                LogUtils.getLogger().info("switch to tab {}", tab.toString());
                //minecraft.setScreen(new SinkSurivivalConfigScreen(components[tab.ordinal()]));
                return true;
            }
        }
        return false;
    }

    private static boolean isHovering(int pX, int pY, int pWidth, int pHeight, double pMouseX, double pMouseY) {
        return pMouseX >= (double)(pX) && pMouseX < (double)(pX + pWidth) && pMouseY >= (double)(pY) && pMouseY < (double)(pY + pHeight);
    }
}
