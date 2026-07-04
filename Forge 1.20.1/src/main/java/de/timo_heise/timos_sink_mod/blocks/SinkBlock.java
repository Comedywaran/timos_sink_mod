package de.timo_heise.timos_sink_mod.blocks;

import de.timo_heise.timos_sink_mod.block_entities.ModBlockEntities;
import de.timo_heise.timos_sink_mod.block_entities.SinkBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SinkBlock extends BaseEntityBlock {

    public SinkBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : createTickerHelper(type, ModBlockEntities.SINK_BLOCK_ENTITY.get(), SinkBlockEntity::tick);
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SinkBlockEntity(pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level pLevel, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!pLevel.isClientSide()) {
            BlockEntity entity = pLevel.getBlockEntity(pos);
            if(entity instanceof SinkBlockEntity) {
                NetworkHooks.openScreen(((ServerPlayer)player), (SinkBlockEntity)entity, pos);
            } else {
                throw new IllegalStateException("Opened sink menu not on sink (?)");
            }
        }

        return InteractionResult.sidedSuccess(pLevel.isClientSide());
    }
}
