package cobblemon.creatified.item;

import cobblemon.creatified.CobblemonCreatified;
import cobblemon.creatified.block.ModBlocks;
import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

public class ModItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(CobblemonCreatified.MODID);

    // Apricorn-related
    public static final DeferredItem<Item> PURPLE_APRICORN_MASH = ITEMS.register("purple_apricorn_mash",
            () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> PURPLE_BALL_LID = ITEMS.register("purple_ball_lid",
            () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> SUPERIOR_BALL_BASE = ITEMS.register("superior_ball_base",
            () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> MASTERFUL_BALL_LID = ITEMS.register("masterful_ball_lid",
            () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> SUPERIOR_SPHERE = ITEMS.register("unrefined_superior_sphere",
            () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> INCOMPLETE_MASTERFUL_BALL_LID = ITEMS.register("incomplete_masterful_ball_lid",
            () -> new Item(new Item.Properties().stacksTo(64)));

    // Candy
    public static final DeferredItem<Item> EPIC_CANDY = ITEMS.register("epic_candy",
            () -> new EpicCandyItem(new Item.Properties().stacksTo(64).rarity(Rarity.EPIC)));

    // Silver Bottle Caps
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

    // Charcoal Bottle Caps (IV debuffs)
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

    // Token Items
    public static final DeferredItem<ShinyTokenItem> SHINY_TOKEN = ITEMS.register("shiny_token",
            () -> new ShinyTokenItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));
    public static final DeferredItem<InertShinyTokenItem> INERT_SHINY_TOKEN = ITEMS.register("inert_shiny_token",
            () -> new InertShinyTokenItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<GenderSwapTokenItem> GENDER_SWAP_TOKEN = ITEMS.register("gender_swap_token",
            () -> new GenderSwapTokenItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));

    // Gold Bottle Cap
    public static final DeferredItem<GoldBottlecapItem> GOLD_BOTTLECAP = ITEMS.register("gold_bottlecap",
            () -> new GoldBottlecapItem(new Item.Properties().stacksTo(1), "tooltip.cobblemoncreatified.gold_bottlecap"));

    // Particle test item
    public static final DeferredItem<TestParticleItem> TEST_PARTICLE_ITEM = ITEMS.register("test_particle_item",
            () -> new TestParticleItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));

    // Repel Block
    public static final DeferredItem<Item> REPEL_BLOCK_ITEM = ITEMS.register("repel_block",
            () -> new BlockItem(ModBlocks.REPEL_BLOCK.get(), new Item.Properties()) {
                @Override
                public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
                    tooltip.add(Component.translatable("tooltip.cobblemoncreatified.repel_block"));
                }




            });

    public static final DeferredItem<Item> COPPER_BALL_BLANK = ITEMS.register("copper_ball_blank", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> COPPER_BALL_HALF = ITEMS.register("copper_ball_half", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> COPPER_BALL_BASE = ITEMS.register("copper_ball_base", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> POKE_BALL_LID = ITEMS.register("poke_ball_lid", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> CITRINE_BALL_LID = ITEMS.register("citrine_ball_lid", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> VERDANT_BALL_LID = ITEMS.register("verdant_ball_lid", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> AZURE_BALL_LID = ITEMS.register("azure_ball_lid", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> ROSEATE_BALL_LID = ITEMS.register("roseate_ball_lid", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> SLATE_BALL_LID = ITEMS.register("slate_ball_lid", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> PREMIER_BALL_LID = ITEMS.register("premier_ball_lid", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> SAFARI_BALL_LID = ITEMS.register("safari_ball_lid", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> HEAL_BALL_LID = ITEMS.register("heal_ball_lid", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> HEAL_BALL_BASE = ITEMS.register("heal_ball_base", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> RED_APRICORN_ZEST = ITEMS.register("red_apricorn_zest", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> YELLOW_APRICORN_ZEST = ITEMS.register("yellow_apricorn_zest", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> GREEN_APRICORN_ZEST = ITEMS.register("green_apricorn_zest", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> BLUE_APRICORN_ZEST = ITEMS.register("blue_apricorn_zest", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> PINK_APRICORN_ZEST = ITEMS.register("pink_apricorn_zest", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> BLACK_APRICORN_ZEST = ITEMS.register("black_apricorn_zest", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> WHITE_APRICORN_ZEST = ITEMS.register("white_apricorn_zest", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> IRON_BALL_BLANK = ITEMS.register("iron_ball_blank", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> IRON_BALL_HALF = ITEMS.register("iron_ball_half", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> IRON_BALL_BASE = ITEMS.register("iron_ball_base", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> GREAT_BALL_LID = ITEMS.register("great_ball_lid", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> FAST_BALL_LID = ITEMS.register("fast_ball_lid", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> LEVEL_BALL_LID = ITEMS.register("level_ball_lid", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> LURE_BALL_LID = ITEMS.register("lure_ball_lid", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> HEAVY_BALL_LID = ITEMS.register("heavy_ball_lid", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> FRIEND_BALL_LID = ITEMS.register("friend_ball_lid", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> MOON_BALL_LID = ITEMS.register("moon_ball_lid", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> SPORT_BALL_LID = ITEMS.register("sport_ball_lid", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> PARK_BALL_LID = ITEMS.register("park_ball_lid", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> NET_BALL_LID = ITEMS.register("net_ball_lid", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> DIVE_BALL_LID = ITEMS.register("dive_ball_lid", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> NEST_BALL_LID = ITEMS.register("nest_ball_lid", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> RED_APRICORN_FLOUR = ITEMS.register("red_apricorn_flour", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> YELLOW_APRICORN_FLOUR = ITEMS.register("yellow_apricorn_flour", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> GREEN_APRICORN_FLOUR = ITEMS.register("green_apricorn_flour", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> BLUE_APRICORN_FLOUR = ITEMS.register("blue_apricorn_flour", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> PINK_APRICORN_FLOUR = ITEMS.register("pink_apricorn_flour", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> BLACK_APRICORN_FLOUR = ITEMS.register("black_apricorn_flour", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> RED_APRICORN_MASH = ITEMS.register("red_apricorn_mash", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> YELLOW_APRICORN_MASH = ITEMS.register("yellow_apricorn_mash", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> GREEN_APRICORN_MASH = ITEMS.register("green_apricorn_mash", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> BLUE_APRICORN_MASH = ITEMS.register("blue_apricorn_mash", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> PINK_APRICORN_MASH = ITEMS.register("pink_apricorn_mash", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> BLACK_APRICORN_MASH = ITEMS.register("black_apricorn_mash", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> WHITE_APRICORN_MASH = ITEMS.register("white_apricorn_mash", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> GOLD_BALL_BLANK = ITEMS.register("gold_ball_blank", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> GOLD_BALL_HALF = ITEMS.register("gold_ball_half", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> GOLD_BALL_BASE = ITEMS.register("gold_ball_base", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> ULTRA_BALL_LID = ITEMS.register("ultra_ball_lid", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> LOVE_BALL_LID = ITEMS.register("love_ball_lid", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> REPEAT_BALL_LID = ITEMS.register("repeat_ball_lid", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> TIMER_BALL_LID = ITEMS.register("timer_ball_lid", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> LUXURY_BALL_LID = ITEMS.register("luxury_ball_lid", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> DUSK_BALL_LID = ITEMS.register("dusk_ball_lid", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> QUICK_BALL_LID = ITEMS.register("quick_ball_lid", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> RED_APRICORN_POWDER = ITEMS.register("red_apricorn_powder", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> YELLOW_APRICORN_POWDER = ITEMS.register("yellow_apricorn_powder", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> GREEN_APRICORN_POWDER = ITEMS.register("green_apricorn_powder", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> BLUE_APRICORN_POWDER = ITEMS.register("blue_apricorn_powder", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> PINK_APRICORN_POWDER = ITEMS.register("pink_apricorn_powder", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> BLACK_APRICORN_POWDER = ITEMS.register("black_apricorn_powder", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> WHITE_APRICORN_POWDER = ITEMS.register("white_apricorn_powder", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> RED_APRICORN_PRIMER = ITEMS.register("red_apricorn_primer", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> YELLOW_APRICORN_PRIMER = ITEMS.register("yellow_apricorn_primer", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> GREEN_APRICORN_PRIMER = ITEMS.register("green_apricorn_primer", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> BLUE_APRICORN_PRIMER = ITEMS.register("blue_apricorn_primer", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> PINK_APRICORN_PRIMER = ITEMS.register("pink_apricorn_primer", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> BLACK_APRICORN_PRIMER = ITEMS.register("black_apricorn_primer", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> WHITE_APRICORN_PRIMER = ITEMS.register("white_apricorn_primer", () -> new Item(new Item.Properties().stacksTo(64)));

    // ✅ Luring Incense Block
    public static final DeferredItem<Item> LURING_INCENSE_ITEM = ITEMS.register("luring_incense",
            () -> new BlockItem(ModBlocks.LURING_INCENSE.get(), new Item.Properties()) {
                @Override
                public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
                    tooltip.add(Component.translatable("tooltip.cobblemoncreatified.luring_incense"));
                }
            });

    public static final DeferredItem<Item> TEST_CLICK_BLOCK_ITEM = ITEMS.register("test_click_block",
            () -> new BlockItem(ModBlocks.TEST_CLICK_BLOCK.get(), new Item.Properties()));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
