package de.timo_heise.timos_sink_mod.networking.packets;

import de.timo_heise.timos_sink_mod.block_entities.SinkBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record SinkConfigMenuChanged(BlockPos pos, CompoundTag nbtTag) {

    public SinkConfigMenuChanged(FriendlyByteBuf buf) {
        this(buf.readBlockPos(), buf.readNbt());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeNbt(nbtTag);
    }

    public static void serverHandle(SinkConfigMenuChanged msg, Supplier<NetworkEvent.Context> ctx) {
        ServerPlayer player = ctx.get().getSender();
        Level level = player.level();
        ctx.get().enqueueWork(() -> {
            BlockEntity blockEntity = level.getBlockEntity(msg.pos);
            if(blockEntity instanceof SinkBlockEntity) {
                ((SinkBlockEntity) blockEntity).tryChangeNbt(msg.nbtTag, player.isCreative());
            }
        });
        ctx.get().setPacketHandled(true);
    }
}

