package de.timo_heise.timos_sink_mod.menus;

import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashSet;
import java.util.Set;

public class SinkCreativeConfigScreen extends AbstractSinkConfigScreen {

    Set<EditBox> editBoxes = new HashSet<>();
    protected SinkCreativeConfigScreen(Component pTitle, SinkScreen oldSinkScreen, BlockPos blockPos, boolean isCreative) {
        super(pTitle, oldSinkScreen, blockPos, isCreative);
    }

    @Override
    protected SinkScreenUtil.SinkTabs getTabType() {
        return SinkScreenUtil.SinkTabs.CREATIVE_CONFIG;
    }

    @Override
    protected void init() {
        super.init();
        subInit();
    }

    @Override
    public void tick() {
        super.tick();

        for(EditBox editBox : editBoxes) {
            editBox.tick();
        }
    }

    private void subInit() {
        SinkEditBox tempBox = new SinkEditBox(this.font, leftPos + 62, topPos + 24, 103, 12, Component.translatable("container.repair"), "");
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
}
