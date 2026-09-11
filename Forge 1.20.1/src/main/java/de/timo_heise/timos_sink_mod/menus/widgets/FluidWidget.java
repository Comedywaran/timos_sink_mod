package de.timo_heise.timos_sink_mod.menus.widgets;

import de.timo_heise.timos_sink_mod.TimosSinkMod;
import de.timo_heise.timos_sink_mod.menus.ClientUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;

public class FluidWidget implements IRenderableWithSeperateTooltip, IStackDropTarget {
    private final int WIDTH = 18;
    private final int HEIGHT = 18;
    private final int x;
    private int y;
    private final Font font;
    private FluidStack fluid;

    public FluidWidget(int x, int y, Font font, FluidStack fluid) {
        this.x = x;
        this.y = y;
        this.font =  font;
        this.fluid = fluid.copy();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderWithoutTooltip(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY, partialTick);
    }

    public void renderWithoutTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderFluid(guiGraphics);
        if(ClientUtil.betterIsHovering(x+1, y+1, WIDTH-2, HEIGHT-2, mouseX, mouseY)) {
            highlightSlot(guiGraphics);
        }
    }

    @Override
    public void renderTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY,  float partialTick) {
        if(!ClientUtil.betterIsHovering(x+1, y+1, WIDTH-2, HEIGHT-2, mouseX, mouseY)) return;
        if(fluid.getFluid() == Fluids.EMPTY) return;
        ArrayList<Component> tooltip = new ArrayList<Component>();
        tooltip.add(fluid.getDisplayName());
        ResourceLocation resourceLocation = ForgeRegistries.FLUIDS.getKey(fluid.getFluid());
        if (resourceLocation != null && Minecraft.getInstance().options.advancedItemTooltips) {
            tooltip.add(Component.literal(resourceLocation.toString()).withStyle(ChatFormatting.DARK_GRAY));
        }
        guiGraphics.renderComponentTooltip(font, tooltip, mouseX, mouseY);
    }

    private void renderFluid(GuiGraphics guiGraphics) {
        guiGraphics.blit(ResourceLocation.fromNamespaceAndPath(TimosSinkMod.MOD_ID, "textures/gui/sink/sink.png"), x, y, 0,  166, WIDTH, HEIGHT);
        if(fluid.getFluid().equals(Fluids.EMPTY)) {return;}

        IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(fluid.getFluid());
        int tint = extensions.getTintColor(fluid);
        ResourceLocation texture = extensions.getStillTexture(fluid);
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(texture);
        guiGraphics.blit(x+1, y+1, 0, WIDTH-2, HEIGHT-2, sprite,
                (tint >> 16 & 0xFF) / 255.0F,
                (tint >> 8 & 0xFF) / 255.0F,
                (tint & 0xFF) / 255.0F,
                (tint >> 24 & 0xFF) / 255.0F);
    }

    private void highlightSlot(GuiGraphics guiGraphics) {
        guiGraphics.fillGradient(RenderType.guiOverlay(), x+1, y+1, x+WIDTH-1, y+HEIGHT-1, 0x80FFFFFF, 0x80FFFFFF, 0);
    }

    public void setFluid(FluidStack newFluid) {
        fluid = newFluid.copy();
    }

    public void setFluid(String newFluid) {
        setFluid(new FluidStack(ClientUtil.getFluidFromID(newFluid), 1));
    }

    @Override
    public Rect2i getArea() {
        return new Rect2i(x, y, WIDTH, HEIGHT);
    }

    @Override
    public boolean acceptStack(FluidStack stack, boolean simulate) {
        if(fluid.isFluidStackIdentical(stack)) {return false;}

        if(!simulate) {
            setFluid(stack);
        }
        return true;
    }

    public void setY(int newY) {
        y = newY;
    }
}
