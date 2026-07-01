package de.timo_heise.timos_sink_mod.block_entities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class SinkBlockEntity extends BlockEntity {

    public SinkBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.SINK_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    // this saves the tags to the save
    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
    }

    // this load the tags from the save
    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
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

