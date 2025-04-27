package cobblemon.tweaks.block;

import cobblemon.tweaks.CreateCobblemonTweaks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {

    // Register all blocks under your mod's namespace
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(CreateCobblemonTweaks.MODID);

    // Your Repel Block
    public static final DeferredBlock<Block> REPEL_BLOCK = BLOCKS.register("repel_block",
            () -> new RepelBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(5.0f, 6.0f)
                    .requiresCorrectToolForDrops()
                    .noOcclusion() // Optional depending on your block design
            ));

    // Also register the corresponding BlockItem (so it appears in inventory / creative tab)
    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
