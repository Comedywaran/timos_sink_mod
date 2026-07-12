package de.timo_heise.timos_sink_mod.block_entities;

import com.mojang.logging.LogUtils;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;

public class SinkFluidTank extends FluidTank {

    private int additionalFluidAmount = 0; // Additional Buffer for fluid produced in the same tick if the tank is already full
    private final SinkBlockEntity sinkBlockEntity;

    public SinkFluidTank(SinkBlockEntity sinkBlockEntity) {
        super(sinkBlockEntity.getFluidBufferSize());
        this.sinkBlockEntity = sinkBlockEntity;
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        return 0;
    }

    @Override
    public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
        int drained = maxDrain;
        if (additionalFluidAmount < drained)
            {drained = additionalFluidAmount;}
        if (action.execute())
            {additionalFluidAmount -= drained;}

        FluidStack stack = super.drain(maxDrain-drained, action);
        stack = new FluidStack(sinkBlockEntity.getFluid(), stack.getAmount()+drained);
        return stack;
    }

    @Override
    public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
        if (resource.getFluid() == (sinkBlockEntity.getFluid())) {
            return drain(resource.getAmount(), action);
        }
        return FluidStack.EMPTY;
    }

    @Override
    public boolean isFluidValid(FluidStack stack) {
        return stack.getFluid() == sinkBlockEntity.getFluid();
    }

    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        return isFluidValid(stack);
    }

    @Override
    public @NotNull FluidStack getFluid() {
        FluidStack fs = fluid.copy();
        if(fs.isEmpty())
            {fs = new FluidStack(sinkBlockEntity.getFluid(), 0);}
        if(!fs.isEmpty())
            {fs.grow(additionalFluidAmount);}
        return fs;
    }

    @Override
    public int getFluidAmount()
    {
        return fluid.getAmount()+additionalFluidAmount;
    }

    @Override
    public @NotNull FluidStack getFluidInTank(int tank) { return getFluid(); }

    public void addFluid(int amount) {
        if (getSpace() >= amount) {
            additionalFluidAmount = 0;
            if(fluid.isEmpty()) {
                fluid = new FluidStack(sinkBlockEntity.getFluid(), amount);
            }
            else {
                fluid.grow(amount);
            }
        }
        else {
            additionalFluidAmount = amount-getSpace();
            if(fluid.isEmpty()) {
                fluid = new FluidStack(sinkBlockEntity.getFluid(), capacity);
            }
            else {
                fluid.setAmount(capacity);
            }
        }
    }
}
