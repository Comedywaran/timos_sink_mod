package de.timo_heise.timos_sink_mod.menus.sink;

import de.timo_heise.timos_sink_mod.block_entities.SinkBlockEntity;
import de.timo_heise.timos_sink_mod.blocks.ModBlocks;
import de.timo_heise.timos_sink_mod.menus.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SinkMenu extends AbstractContainerMenu {
    public final SinkBlockEntity sinkBlockEntity;

    public SinkMenu(int id, Inventory inv, FriendlyByteBuf buf) { // Idk what this is for
        this(id, inv, (SinkBlockEntity) inv.player.level().getBlockEntity(buf.readBlockPos()));
    }

    public SinkMenu(int containerId, Inventory inv, SinkBlockEntity sinkBlockEntity) {
        super(ModMenuTypes.SINK_MENU.get(), containerId);
        this.sinkBlockEntity = sinkBlockEntity;

        addPlayerInventory(inv);
        addPlayerHotbar(inv);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(sinkBlockEntity.getLevel(), sinkBlockEntity.getBlockPos()), player, ModBlocks.SINK_BLOCK.get());
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }
}
