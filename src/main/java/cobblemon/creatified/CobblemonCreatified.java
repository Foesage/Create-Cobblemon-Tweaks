package cobblemon.creatified;

import cobblemon.creatified.block.ModBlocks;
import cobblemon.creatified.item.ModItems;
import cobblemon.creatified.event.ModEvents;
import cobblemon.creatified.event.CobblemonSpawnBlocker;
import cobblemon.creatified.event.ClientEvents;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

@Mod(CobblemonCreatified.MODID)
public class CobblemonCreatified {

    public static final String MODID = "cobblemoncreatified";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MOD_TAB = CREATIVE_MODE_TABS.register("mod_tab", () -> CreativeModeTab.builder()
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
                output.accept(ModItems.HP_SILVER_BOTTLECAP.get());
                output.accept(ModItems.ATK_SILVER_BOTTLECAP.get());
                output.accept(ModItems.DEF_SILVER_BOTTLECAP.get());
                output.accept(ModItems.SPATK_SILVER_BOTTLECAP.get());
                output.accept(ModItems.SPDEF_SILVER_BOTTLECAP.get());
                output.accept(ModItems.SPEED_SILVER_BOTTLECAP.get());
            })
            .build());

    public CobblemonCreatified(IEventBus modEventBus, ModContainer modContainer) {
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(CobblemonCreatified::onClientSetup);

        // ✅ Register client-side events immediately
        NeoForge.EVENT_BUS.register(new ClientEvents());

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

    private static void onClientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());

        });
    }


    private void logConfigValues() {
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.logDirtBlock) {
            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));
        }

        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);

        if (Config.items != null) {
            Config.items.forEach(item -> LOGGER.info("ITEM >> {}", item.toString()));
        } else {
            LOGGER.warn("Config.items is NULL! Config might not have loaded properly.");
        }
    }
}
