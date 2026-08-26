package de.timo_heise.timos_sink_mod.menus.widgets;

import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

public interface IStackDropTarget {
    public abstract Rect2i getArea();

    public default boolean acceptStack(ItemStack stack, boolean simulate) {
        if(FluidUtil.getFluidContained(stack).isPresent()) {
            return acceptStack(FluidUtil.getFluidContained(stack).get(), simulate);
        }
        return false;
    }

    public default boolean acceptStack(FluidStack stack, boolean simulate) {
        return false;
    }

}
