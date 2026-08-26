package de.timo_heise.timos_sink_mod.menus.sink;

import com.mojang.logging.LogUtils;
import de.timo_heise.timos_sink_mod.menus.ClientUtil;
import de.timo_heise.timos_sink_mod.menus.widgets.CustomEditBox;
import de.timo_heise.timos_sink_mod.menus.widgets.FluidWidget;
import de.timo_heise.timos_sink_mod.menus.widgets.ItemWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;

import java.util.HashSet;
import java.util.Set;

public class SinkCreativeConfigScreen extends AbstractSinkConfigScreen {

    Set<EditBox> editBoxes = new HashSet<>();
    private FluidWidget tempFluid;
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

    }

    @Override
    protected void containerTick() {
        super.containerTick();

        for(EditBox editBox : editBoxes) {
            editBox.tick();
        }
    }

    private void subInit() {
        CustomEditBox tempBox = new CustomEditBox(font, leftPos + 33, topPos + 40, imageWidth - 43 , 12, Component.translatable("container.repair"), "");
        tempBox.setOnLooseFocus(this::onNameChanged);
        tempBox.setValidator(ClientUtil::isRealFluid);
        this.addRenderableWidget(tempBox);
        tempFluid = new FluidWidget(leftPos + 10, topPos + 27, font, new FluidStack(Fluids.WATER, 1));
        this.addRenderableOnly(tempFluid);
        dropPositions.add(tempFluid);
        ItemWidget tempItem = new ItemWidget(leftPos+10, topPos + 50, font, ItemStack.EMPTY);
        this.addRenderableOnly(tempItem);
        dropPositions.add(tempItem);

    }

    private void onNameChanged(String newFluid) {
        LogUtils.getLogger().debug("Changed fluid to {}", newFluid);
        tempFluid.setFluid(newFluid);
    }
}
