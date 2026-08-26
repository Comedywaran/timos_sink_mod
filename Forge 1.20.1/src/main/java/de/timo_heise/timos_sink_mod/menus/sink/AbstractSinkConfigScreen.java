package de.timo_heise.timos_sink_mod.menus.sink;

import de.timo_heise.timos_sink_mod.TimosSinkMod;
import de.timo_heise.timos_sink_mod.menus.ClientUtil;
import de.timo_heise.timos_sink_mod.menus.widgets.CustomEditBox;
import de.timo_heise.timos_sink_mod.menus.widgets.IStackDropTarget;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractSinkConfigScreen extends AbstractSinkScreen {
    protected final Set<IStackDropTarget> dropPositions = new HashSet<>();

    protected AbstractSinkConfigScreen(SinkMenu menu, Inventory inv, Component title, boolean isCreative) {
        super(menu, inv, title, isCreative, ResourceLocation.fromNamespaceAndPath(TimosSinkMod.MOD_ID, "textures/gui/sink/sink.png"));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int pButton) {
        if(getFocused() instanceof CustomEditBox && !getFocused().isMouseOver(mouseX, mouseY)) {
            getFocused().setFocused(false);
        }

        ItemStack carried = menu.getCarried();
        if(carried != null && !carried.isEmpty()) {
            for (IStackDropTarget dropTarget : dropPositions) {
                if (ClientUtil.betterIsHovering(dropTarget.getArea(), mouseX, mouseY)) {
                    if(dropTarget.acceptStack(carried, false)) {return true;}
                }
            }
        }

        return super.mouseClicked(mouseX, mouseY, pButton);
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        if (pKeyCode == 256) {
            this.minecraft.player.closeContainer();
        }
        if(getFocused() instanceof CustomEditBox) {
            if (pKeyCode == GLFW.GLFW_KEY_ENTER) {
                getFocused().setFocused(false);
                return true;
            }
            else {
                getFocused().keyPressed(pKeyCode, pScanCode, pModifiers);
                return true;
            }
        }
        return super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }

    public Set<IStackDropTarget> getDropPositions() {
        return dropPositions;
    }
}
