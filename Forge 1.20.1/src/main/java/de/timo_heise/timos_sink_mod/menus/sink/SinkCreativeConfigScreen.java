package de.timo_heise.timos_sink_mod.menus.sink;

import com.mojang.logging.LogUtils;
import de.timo_heise.timos_sink_mod.TimosSinkMod;
import de.timo_heise.timos_sink_mod.menus.widgets.CustomEditBox;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashSet;
import java.util.Set;

public class SinkCreativeConfigScreen extends AbstractSinkConfigScreen {

    Set<EditBox> editBoxes = new HashSet<>();
    protected SinkCreativeConfigScreen(SinkMenu menu, Inventory inv, Component originalTitle, boolean isCreative) {
        super(menu, inv, Component.translatable("gui.timos_sink_mod.admin_config"), isCreative);
        this.originalTitle = originalTitle;
    }

    @Override
    protected AbstractSinkScreen.SinkTabs getTabType() {
        return AbstractSinkScreen.SinkTabs.CREATIVE_CONFIG;
    }

    @Override
    protected void init() {
        super.init();
        subInit();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        renderFluid(guiGraphics, new FluidStack(Fluids.WATER, 1), leftPos + 10, topPos + 37);

    }

    @Override
    protected void containerTick() {
        super.containerTick();

        for(EditBox editBox : editBoxes) {
            editBox.tick();
        }
    }

    private void subInit() {
        CustomEditBox tempBox = new CustomEditBox(this.font, leftPos + 33, topPos + 40, imageWidth - 43 , 12, Component.translatable("container.repair"), "");
        tempBox.setOnLooseFocus(this::onNameChanged);
        tempBox.setValidator(SinkCreativeConfigScreen::isValidFluid);
        this.addRenderableWidget(tempBox);
    }

    private void onNameChanged(String newName) {
        LogUtils.getLogger().info("Changed name to {}", newName);
    }

    static boolean isValidFluid(String fluid) {
        if (fluid == null) {return false;}
        fluid = fluid.trim().toLowerCase();
        ResourceLocation location = ResourceLocation.tryParse(fluid);
        if (location == null) {return false;}
        return ForgeRegistries.FLUIDS.containsKey(location);
    }

    public static void renderFluid(GuiGraphics guiGraphics, FluidStack fluid, int x, int y) {
        guiGraphics.blit(ResourceLocation.fromNamespaceAndPath(TimosSinkMod.MOD_ID, "textures/gui/sink/blank_gui.png"), x, y, 0,  166, 18, 18);
        if(fluid.getFluid().equals(Fluids.EMPTY)) {return;}

        IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(fluid.getFluid());
        int tint = extensions.getTintColor(fluid);
        ResourceLocation texture = extensions.getStillTexture(fluid);
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(texture);
        guiGraphics.blit(x+1, y+1, 0, 16, 16, sprite,
                (tint >> 16 & 0xFF) / 255.0F,
                (tint >> 8 & 0xFF) / 255.0F,
                (tint & 0xFF) / 255.0F,
                (tint >> 24 & 0xFF) / 255.0F);
    }
}
