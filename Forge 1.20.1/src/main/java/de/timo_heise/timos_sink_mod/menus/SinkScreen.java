package de.timo_heise.timos_sink_mod.menus;

import com.mojang.blaze3d.systems.RenderSystem;
import de.timo_heise.timos_sink_mod.TimosSinkMod;
import de.timo_heise.timos_sink_mod.networking.ModPacketHandler;
import de.timo_heise.timos_sink_mod.networking.packets.SinkConfigMenuChanged;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.registries.ForgeRegistries;

public class SinkScreen extends AbstractContainerScreen<SinkMenu> {
    private static final ResourceLocation GUI_TEXTURE = ResourceLocation.fromNamespaceAndPath(TimosSinkMod.MOD_ID, "textures/gui/sink/sink.png");

    private final boolean isCreative;
    private int[][] tabCoords;

    public SinkScreen(SinkMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
        isCreative = inv.player.isCreative();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        SinkScreenUtil.renderTooltips(guiGraphics, tabCoords, mouseX, mouseY, font);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int pButton) {
        if (SinkScreenUtil.checkButtonPressed(tabCoords, SinkScreenUtil.SinkTabs.SINK, mouseX, mouseY, minecraft, this, menu.sinkBlockEntity.getBlockPos(), isCreative)) {return true;}
        return super.mouseClicked(mouseX, mouseY, pButton);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI_TEXTURE);

        RenderSystem.enableDepthTest();
        tabCoords = SinkScreenUtil.renderTabButtons(guiGraphics, isCreative, SinkScreenUtil.SinkTabs.SINK, leftPos + imageWidth, topPos);
        guiGraphics.blit(GUI_TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);
        RenderSystem.disableDepthTest(); // is this a problem if the DepthTest was enabled before?
    }

    @Override
    protected void init() {
        super.init();

        Button btn = Button.builder(Component.literal("Config"), b -> {
            ModPacketHandler.INSTANCE.sendToServer(new SinkConfigMenuChanged(menu.sinkBlockEntity.getBlockPos(), test())); //TODO: remove
        }).bounds(leftPos + 20, topPos + 20, 120, 20).build();
        addRenderableWidget(btn);
    }

    public static CompoundTag test() { // placeholder
        CompoundTag tag = new CompoundTag();
        tag.putString("Fluid", String.valueOf(ForgeRegistries.FLUIDS.getKey(Fluids.WATER)));
        return tag;
    }
}
