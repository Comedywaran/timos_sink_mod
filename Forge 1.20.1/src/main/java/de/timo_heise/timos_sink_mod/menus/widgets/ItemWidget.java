package de.timo_heise.timos_sink_mod.menus.widgets;

import com.mojang.logging.LogUtils;
import de.timo_heise.timos_sink_mod.TimosSinkMod;
import de.timo_heise.timos_sink_mod.menus.ClientUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class ItemWidget implements Renderable, IStackDropTarget {
    private final int WIDTH = 18;
    private final int HEIGHT = 18;
    private final int x;
    private final int y;
    private final Font font;
    private ItemStack item;

    public ItemWidget(int x, int y, Font font, ItemStack item) {
        this.x = x;
        this.y = y;
        this.font =  font;
        this.item = item.copy();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        LogUtils.getLogger().debug(this +" "+item.getDisplayName().getString());
        renderItem(guiGraphics);
        if(ClientUtil.betterIsHovering(x+1, y+1, WIDTH-2, HEIGHT-2, mouseX, mouseY)) {
            highlightSlot(guiGraphics);
            renderTooltip(guiGraphics, mouseX, mouseY);
        }
    }

    private void renderItem(GuiGraphics guiGraphics) {
        guiGraphics.blit(ResourceLocation.fromNamespaceAndPath(TimosSinkMod.MOD_ID, "textures/gui/sink/sink.png"), x, y, 0,  166, WIDTH, HEIGHT);
        if(item.isEmpty()) return;
        guiGraphics.renderItem(item, x+1, y+1);
    }

    private void renderTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if(item.isEmpty()) return;
        List<Component> tooltip = item.getTooltipLines(Minecraft.getInstance().player, Minecraft.getInstance().options.advancedItemTooltips ? TooltipFlag.Default.ADVANCED : TooltipFlag.Default.NORMAL);
        guiGraphics.renderTooltip(font, tooltip, item.getTooltipImage(), item, mouseX, mouseY);
    }

    private void highlightSlot(GuiGraphics guiGraphics) {
        guiGraphics.fillGradient(RenderType.guiOverlay(), x+1, y+1, x+WIDTH-1, y+HEIGHT-1, 0x80FFFFFF, 0x80FFFFFF, 0);
    }

    public void setItem(ItemStack newItem) {
        LogUtils.getLogger().debug("Setting item to {}", newItem.getDisplayName().getString());
        item = newItem.copy();
    }

    public void setItem(String newItem) {
        setItem(new ItemStack(ClientUtil.getItemFromID(newItem), 1));
    }

    @Override
    public Rect2i getArea() {
        return new Rect2i(x, y, WIDTH, HEIGHT);
    }

    @Override
    public boolean acceptStack(ItemStack stack, boolean simulate) {
        if(ItemStack.matches(item, stack)) {return false;}

        if(!simulate) {
            setItem(stack);
        }
        return true;
    }

    private static List<Component> getTooltipFromItem(ItemStack itemstack, List<String> tooltip) {
        return itemstack.getTooltipLines(Minecraft.getInstance().player, Minecraft.getInstance().options.advancedItemTooltips ? TooltipFlag.Default.ADVANCED : TooltipFlag.Default.NORMAL);
    }
}
