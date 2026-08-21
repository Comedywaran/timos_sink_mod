package de.timo_heise.timos_sink_mod.menus.sink;

import de.timo_heise.timos_sink_mod.TimosSinkMod;
import de.timo_heise.timos_sink_mod.menus.widgets.CustomEditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.lwjgl.glfw.GLFW;

public abstract class AbstractSinkConfigScreen extends AbstractSinkScreen {
    protected AbstractSinkConfigScreen(SinkMenu menu, Inventory inv, Component title, boolean isCreative) {
        super(menu, inv, title, isCreative, ResourceLocation.fromNamespaceAndPath(TimosSinkMod.MOD_ID, "textures/gui/sink/sink.png"));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int pButton) {
        if(getFocused() instanceof CustomEditBox && !getFocused().isMouseOver(mouseX, mouseY)) {
            getFocused().setFocused(false);
        }
        return super.mouseClicked(mouseX, mouseY, pButton);
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        if (pKeyCode == GLFW.GLFW_KEY_ENTER) {
            if(getFocused() instanceof CustomEditBox) {
                getFocused().setFocused(false);
                return true;
            }
        }
        return super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }
}
