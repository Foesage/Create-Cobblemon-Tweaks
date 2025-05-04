package cobblemon.creatified.item;

import cobblemon.creatified.CobblemonCreatified;
import cobblemon.creatified.block.ModBlocks;
import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.bus.api.IEventBus;

import java.util.List;

public class ModItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(CobblemonCreatified.MODID);

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

    public static final DeferredItem<SilverBottleCapItem> HP_SILVER_BOTTLECAP = ITEMS.register("hp_silver_bottlecap",
            () -> new SilverBottleCapItem(new Item.Properties(), Stats.HP, "tooltip.cobblemoncreatified.hp_silver_bottlecap"));

    public static final DeferredItem<SilverBottleCapItem> ATK_SILVER_BOTTLECAP = ITEMS.register("atk_silver_bottlecap",
            () -> new SilverBottleCapItem(new Item.Properties(), Stats.ATTACK, "tooltip.cobblemoncreatified.atk_silver_bottlecap"));

    public static final DeferredItem<SilverBottleCapItem> DEF_SILVER_BOTTLECAP = ITEMS.register("def_silver_bottlecap",
            () -> new SilverBottleCapItem(new Item.Properties(), Stats.DEFENCE, "tooltip.cobblemoncreatified.def_silver_bottlecap"));

    public static final DeferredItem<SilverBottleCapItem> SPATK_SILVER_BOTTLECAP = ITEMS.register("spatk_silver_bottlecap",
            () -> new SilverBottleCapItem(new Item.Properties(), Stats.SPECIAL_ATTACK, "tooltip.cobblemoncreatified.spatk_silver_bottlecap"));

    public static final DeferredItem<SilverBottleCapItem> SPDEF_SILVER_BOTTLECAP = ITEMS.register("spdef_silver_bottlecap",
            () -> new SilverBottleCapItem(new Item.Properties(), Stats.SPECIAL_DEFENCE, "tooltip.cobblemoncreatified.spdef_silver_bottlecap"));

    public static final DeferredItem<SilverBottleCapItem> SPEED_SILVER_BOTTLECAP = ITEMS.register("speed_silver_bottlecap",
            () -> new SilverBottleCapItem(new Item.Properties(), Stats.SPEED, "tooltip.cobblemoncreatified.speed_silver_bottlecap"));

    public static final DeferredItem<BottlecapItem> HP_CHARCOAL_BOTTLECAP = ITEMS.register("hp_charcoal_bottlecap",
            () -> new BottlecapItem(new Item.Properties(), Stats.HP, "tooltip.cobblemoncreatified.hp_charcoal_bottlecap", -1));

    public static final DeferredItem<BottlecapItem> ATK_CHARCOAL_BOTTLECAP = ITEMS.register("atk_charcoal_bottlecap",
            () -> new BottlecapItem(new Item.Properties(), Stats.ATTACK, "tooltip.cobblemoncreatified.atk_charcoal_bottlecap", -1));

    public static final DeferredItem<BottlecapItem> DEF_CHARCOAL_BOTTLECAP = ITEMS.register("def_charcoal_bottlecap",
            () -> new BottlecapItem(new Item.Properties(), Stats.DEFENCE, "tooltip.cobblemoncreatified.def_charcoal_bottlecap", -1));

    public static final DeferredItem<BottlecapItem> SPATK_CHARCOAL_BOTTLECAP = ITEMS.register("spatk_charcoal_bottlecap",
            () -> new BottlecapItem(new Item.Properties(), Stats.SPECIAL_ATTACK, "tooltip.cobblemoncreatified.spatk_charcoal_bottlecap", -1));

    public static final DeferredItem<BottlecapItem> SPDEF_CHARCOAL_BOTTLECAP = ITEMS.register("spdef_charcoal_bottlecap",
            () -> new BottlecapItem(new Item.Properties(), Stats.SPECIAL_DEFENCE, "tooltip.cobblemoncreatified.spdef_charcoal_bottlecap", -1));

    public static final DeferredItem<BottlecapItem> SPEED_CHARCOAL_BOTTLECAP = ITEMS.register("speed_charcoal_bottlecap",
            () -> new BottlecapItem(new Item.Properties(), Stats.SPEED, "tooltip.cobblemoncreatified.speed_charcoal_bottlecap", -1));

    public static final DeferredItem<ShinyTokenItem> SHINY_TOKEN = ITEMS.register("shiny_token",
            () -> new ShinyTokenItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));


    // Gold Bottlecap
    public static final DeferredItem<GoldBottlecapItem> GOLD_BOTTLECAP = ITEMS.register("gold_bottlecap",
            () -> new GoldBottlecapItem(new Item.Properties().stacksTo(1), "tooltip.cobblemoncreatified.gold_bottlecap"));



    //TEST ITEM
    public static final DeferredItem<TestParticleItem> TEST_PARTICLE_ITEM = ITEMS.register("test_particle_item",
            () -> new TestParticleItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));





    public static final DeferredItem<Item> REPEL_BLOCK_ITEM = ITEMS.register("repel_block",
            () -> new BlockItem(ModBlocks.REPEL_BLOCK.get(), new Item.Properties()) {
                @Override
                public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
                    tooltip.add(Component.translatable("tooltip.cobblemoncreatified.repel_block"));
                }
            });

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
