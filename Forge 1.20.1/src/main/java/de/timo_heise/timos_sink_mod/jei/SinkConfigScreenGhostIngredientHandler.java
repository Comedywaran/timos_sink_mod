package de.timo_heise.timos_sink_mod.jei;

import com.mojang.logging.LogUtils;
import de.timo_heise.timos_sink_mod.menus.sink.AbstractSinkConfigScreen;
import de.timo_heise.timos_sink_mod.menus.widgets.IStackDropTarget;
import mezz.jei.api.gui.handlers.IGhostIngredientHandler;
import mezz.jei.api.ingredients.ITypedIngredient;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class SinkConfigScreenGhostIngredientHandler implements IGhostIngredientHandler<AbstractSinkConfigScreen> {
    @Override
    public <I> List<Target<I>> getTargetsTyped(AbstractSinkConfigScreen gui, ITypedIngredient<I> ingredient, boolean doStart) {
        List<Target<I>> ret = new ArrayList<>();
        for(IStackDropTarget tar: gui.getDropPositions()) {
            Object i = ingredient.getIngredient();
            if(i instanceof ItemStack && tar.acceptStack((ItemStack) i, true)) {
                ret.add(makeTarget(tar));
            }
            else if(i instanceof FluidStack && tar.acceptStack((FluidStack) i, true)) {
                ret.add(makeTarget(tar));
            }
        }
        return ret;
    }

    @Override
    public void onComplete() {}

    private <I> Target<I> makeTarget(IStackDropTarget tar) {
        return new Target<I>() {
            @Override
            public Rect2i getArea() {
                return tar.getArea();
            }

            @Override
            public void accept(I ingredient) {
                if (ingredient instanceof ItemStack) {
                    tar.acceptStack((ItemStack) ingredient, false);
                }
                else if (ingredient instanceof FluidStack) {
                    tar.acceptStack((FluidStack) ingredient, false);
                }
                else {
                    LogUtils.getLogger().warn("Invalid Ghost ingredient type: {}, should be ItemStack or FluidStack", ingredient.getClass());
                }
            }
        };
    }
}
