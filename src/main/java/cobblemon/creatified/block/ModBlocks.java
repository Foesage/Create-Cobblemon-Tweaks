package cobblemon.creatified.block;

import cobblemon.creatified.CobblemonCreatified;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(CobblemonCreatified.MODID);

    // Repel Block
    public static final DeferredBlock<Block> REPEL_BLOCK = BLOCKS.register("repel_block",
            () -> new RepelBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(5.0f, 6.0f)
                    .requiresCorrectToolForDrops()
                    // Removed .noOcclusion() to allow right-click interaction
            ));

    // Luring Incense Block
    public static final DeferredBlock<Block> LURING_INCENSE = BLOCKS.register("luring_incense",
            () -> new LuringIncenseBlock(BlockBehaviour.Properties.of()
                    .strength(1.0F)
                    .noOcclusion()
            ));
    public static final DeferredBlock<Block> TEST_CLICK_BLOCK = BLOCKS.register(
            "test_click_block",
            () -> new TestClickBlock(BlockBehaviour.Properties.of().strength(1.0F).noOcclusion())
    );



    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
