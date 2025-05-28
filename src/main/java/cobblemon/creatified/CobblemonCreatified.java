package cobblemon.creatified;

import cobblemon.creatified.block.ModBlocks;
import cobblemon.creatified.block.entity.ModBlockEntities;
import cobblemon.creatified.client.ModClientSetup; // <-- Added import
import cobblemon.creatified.datacomponent.ModComponents;
import cobblemon.creatified.event.CobblemonSpawnBlocker;
import cobblemon.creatified.event.ModEvents;
import cobblemon.creatified.item.ModItems;
import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

@Mod(CobblemonCreatified.MODID)
public class CobblemonCreatified {

    public static final String MODID = "cobblemoncreatified";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MOD_TAB =
            CREATIVE_MODE_TABS.register("mod_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.cobblemoncreatified"))
                    .withTabsBefore(CreativeModeTabs.COMBAT)
                    .icon(() -> ModItems.PURPLE_APRICORN_MASH.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.PURPLE_APRICORN_MASH.get());
                        output.accept(ModItems.PURPLE_BALL_LID.get());
                        output.accept(ModItems.SUPERIOR_BALL_BASE.get());
                        output.accept(ModItems.MASTERFUL_BALL_LID.get());
                        output.accept(ModItems.SUPERIOR_SPHERE.get());
                        output.accept(ModItems.INCOMPLETE_MASTERFUL_BALL_LID.get());
                        output.accept(ModItems.EPIC_CANDY.get());
                        output.accept(ModItems.REPEL_BLOCK_ITEM.get());
                        output.accept(ModItems.SHINY_TOKEN.get());
                        output.accept(ModItems.GENDER_SWAP_TOKEN.get());
                        output.accept(ModItems.GOLD_BOTTLECAP.get());
                        output.accept(ModItems.HP_SILVER_BOTTLECAP.get());
                        output.accept(ModItems.ATK_SILVER_BOTTLECAP.get());
                        output.accept(ModItems.DEF_SILVER_BOTTLECAP.get());
                        output.accept(ModItems.SPATK_SILVER_BOTTLECAP.get());
                        output.accept(ModItems.SPDEF_SILVER_BOTTLECAP.get());
                        output.accept(ModItems.SPEED_SILVER_BOTTLECAP.get());
                        output.accept(ModItems.HP_CHARCOAL_BOTTLECAP.get());
                        output.accept(ModItems.ATK_CHARCOAL_BOTTLECAP.get());
                        output.accept(ModItems.DEF_CHARCOAL_BOTTLECAP.get());
                        output.accept(ModItems.SPATK_CHARCOAL_BOTTLECAP.get());
                        output.accept(ModItems.SPDEF_CHARCOAL_BOTTLECAP.get());
                        output.accept(ModItems.SPEED_CHARCOAL_BOTTLECAP.get());
                        output.accept(ModItems.INERT_SHINY_TOKEN.get());
                        output.accept(ModItems.COPPER_BALL_BLANK.get());
                        output.accept(ModItems.COPPER_BALL_BLANK_HALF.get());
                    })
                    .build());

    public static ResourceLocation resource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    public static ModelResourceLocation model(String path) {
        return new ModelResourceLocation(resource(path), "inventory");
    }

    public CobblemonCreatified(IEventBus modEventBus, ModContainer modContainer) {
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        ModComponents.DATA_COMPONENTS.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        if (FMLEnvironment.dist == Dist.CLIENT) {
            modEventBus.addListener(ModClientSetup::onClientSetup); // <-- Register client setup for item predicates, etc.
        }

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        if (ModList.get().isLoaded("spawn_notification")) {
            LOGGER.info("Spawn Notification detected, enabling BroadcastSpawnMixin");
        } else {
            LOGGER.warn("Spawn Notification not found. Broadcast blocking disabled.");
        }
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ModEvents.registerListeners();
            CobblemonSpawnBlocker.register();
            logConfigValues();
        });
    }

    private void logConfigValues() {
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.logDirtBlock) {
            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));
        }

        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);

        if (Config.items != null) {
            Config.items.forEach(item -> LOGGER.info("ITEM >> {}", item));
        } else {
            LOGGER.warn("Config.items is NULL! Config might not have loaded properly.");
        }
    }
}
