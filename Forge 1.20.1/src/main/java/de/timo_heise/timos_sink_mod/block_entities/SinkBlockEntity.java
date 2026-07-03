package de.timo_heise.timos_sink_mod.block_entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SinkBlockEntity extends BlockEntity implements ICapabilityProvider {

    private Fluid fluid = Fluids.EMPTY;
    private int productionPerTick = 0;
    private int fluidBufferSize = 0;
    private final SinkFluidTank tank = new SinkFluidTank(this);
    private boolean doPush = true;


    public SinkBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.SINK_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, SinkBlockEntity be) {
        if (level.isClientSide) return;
        be.tank.addFluid(be.productionPerTick);
        if(be.getDoPush()) {
            for (Direction direction : Direction.values()) {
                BlockEntity neighbor = level.getBlockEntity(pos.relative(direction));

                if (neighbor == null) {
                    continue;
                }

                neighbor.getCapability(ForgeCapabilities.FLUID_HANDLER, direction.getOpposite()).ifPresent(handler -> {
                            FluidStack simulated = be.tank.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE);
                            if (simulated.isEmpty()) { return; }

                            int accepted = handler.fill(simulated, IFluidHandler.FluidAction.SIMULATE);
                            if (accepted <= 0) { return; }

                            FluidStack drained = be.tank.drain(accepted, IFluidHandler.FluidAction.EXECUTE);
                            handler.fill(drained, IFluidHandler.FluidAction.EXECUTE);

                            be.setChanged();
                        });
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putString("Fluid", String.valueOf(ForgeRegistries.FLUIDS.getKey(fluid)));
        tag.putBoolean("Push", doPush);
        tag.putInt("ProductionPerTick", productionPerTick);
        tag.putInt("FluidBufferSize", fluidBufferSize);
        tank.writeToNBT(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        ResourceLocation rl = ResourceLocation.tryParse(tag.getString("Fluid"));
        fluid = (rl != null) ? ForgeRegistries.FLUIDS.getValue(rl) : Fluids.EMPTY;

        doPush = tag.getBoolean("Push");
        productionPerTick = tag.getInt("ProductionPerTick");
        fluidBufferSize = tag.getInt("FluidBufferSize");
        tank.readFromNBT(tag);
        tank.setCapacity(fluidBufferSize);
        if(tank.getFluid().getFluid() != fluid && tank.getFluid().getFluid() != Fluids.EMPTY) {tank.setFluid(FluidStack.EMPTY);}
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }


    private final LazyOptional<IFluidHandler> fluidCapability = LazyOptional.of(() -> tank);

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            return fluidCapability.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        fluidCapability.invalidate();
    }



    public Fluid getFluid() {
        return fluid;
    }

    public int getFluidBufferSize() {
        return fluidBufferSize;
    }

    public boolean getDoPush() {
        return doPush;
    }
}
