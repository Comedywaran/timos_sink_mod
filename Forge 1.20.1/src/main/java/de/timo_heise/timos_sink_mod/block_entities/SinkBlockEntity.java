package de.timo_heise.timos_sink_mod.block_entities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

public class SinkBlockEntity extends BlockEntity {

    private Fluid fluid = Fluids.EMPTY;

    public SinkBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.SINK_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    // this saves the tags to the save
    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putString("Fluid", String.valueOf(ForgeRegistries.FLUIDS.getKey(fluid)));
    }

    // this load the tags from the save
    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        ResourceLocation rl = ResourceLocation.tryParse(tag.getString("Fluid"));
        fluid = (rl != null) ? ForgeRegistries.FLUIDS.getValue(rl) : Fluids.EMPTY;
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

}

