package de.timo_heise.timos_sink_mod.block_entities;

import de.timo_heise.timos_sink_mod.TimosSinkMod;
import de.timo_heise.timos_sink_mod.blocks.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, TimosSinkMod.MOD_ID);

    public static final RegistryObject<BlockEntityType<SinkBlockEntity>> SINK_BLOCK_ENTITY = BLOCK_ENTITIES.register("sink_block_entity", () -> BlockEntityType.Builder.of(SinkBlockEntity::new, ModBlocks.SINK_BLOCK.get()).build(null));

    public static void register(IEventBus eventBus) { BLOCK_ENTITIES.register(eventBus); }
}
