package cobblemon.creatified.block.entity;

import cobblemon.creatified.CobblemonCreatified;
import cobblemon.creatified.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, CobblemonCreatified.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<LuringIncenseBlockEntity>> LURING_INCENSE =
            BLOCK_ENTITIES.register("luring_incense",
                    () -> BlockEntityType.Builder.of(
                            LuringIncenseBlockEntity::new,
                            ModBlocks.LURING_INCENSE.get()
                    ).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<RepelBlockEntity>> REPEL_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("repel_block",
                    () -> BlockEntityType.Builder.of(
                            RepelBlockEntity::new,
                            ModBlocks.REPEL_BLOCK.get()
                    ).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
