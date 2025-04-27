package cobblemon.tweaks.item;

import cobblemon.tweaks.CreateCobblemonTweaks;
import cobblemon.tweaks.block.ModBlocks;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.bus.api.IEventBus;

public class ModItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(CreateCobblemonTweaks.MODID);

    // Register all your items here
    public static final DeferredItem<Item> PURPLE_APRICORN_MASH = ITEMS.register("purple_apricorn_mash",
            () -> new Item(new Item.Properties().stacksTo(64))
    );

    public static final DeferredItem<Item> PURPLE_BALL_LID = ITEMS.register("purple_ball_lid",
            () -> new Item(new Item.Properties().stacksTo(64))
    );

    public static final DeferredItem<Item> SUPERIOR_BALL_BASE = ITEMS.register("superior_ball_base",
            () -> new Item(new Item.Properties().stacksTo(64))
    );

    public static final DeferredItem<Item> MASTERFUL_BALL_LID = ITEMS.register("masterful_ball_lid",
            () -> new Item(new Item.Properties().stacksTo(64))
    );

    public static final DeferredItem<Item> SUPERIOR_SPHERE = ITEMS.register("unrefined_superior_sphere",
            () -> new Item(new Item.Properties().stacksTo(64))
    );

    public static final DeferredItem<Item> INCOMPLETE_MASTERFUL_BALL_LID = ITEMS.register("incomplete_masterful_ball_lid",
            () -> new Item(new Item.Properties().stacksTo(64))
    );

    public static final DeferredItem<Item> EPIC_CANDY = ITEMS.register("epic_candy",
            () -> new EpicCandyItem(new Item.Properties()
                    .stacksTo(64)
                    .rarity(Rarity.EPIC))
    );

    public static final DeferredItem<Item> HP_SILVER_BOTTLECAP = ITEMS.register("hp_silver_bottlecap",
            () -> new HpSilverBottleCapItem(new Item.Properties()
                    .stacksTo(64)
                    .rarity(Rarity.UNCOMMON)
            )
    );

    public static final DeferredItem<Item> REPEL_BLOCK_ITEM = ModItems.ITEMS.register("repel_block",
            () -> new BlockItem(ModBlocks.REPEL_BLOCK.get(), new Item.Properties()));



    // Register the ITEMS to the mod event bus
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
