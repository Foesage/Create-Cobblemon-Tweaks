package cobblemon.tweaks;

import cobblemon.tweaks.block.ModBlocks;
import cobblemon.tweaks.item.ModItems;
import cobblemon.tweaks.spawn.RepelSpawnPredicate;
import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.mojang.logging.LogUtils;
import kotlin.Unit;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

@Mod(CreateCobblemonTweaks.MODID)
public class CreateCobblemonTweaks {

    public static final String MODID = "createcobblemontweaks";
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MOD_TAB = CREATIVE_MODE_TABS.register("mod_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.createcobblemontweaks"))
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
                output.accept(ModItems.HP_SILVER_BOTTLECAP.get());
                output.accept(ModItems.REPEL_BLOCK_ITEM.get());
            })
            .build());

    public CreateCobblemonTweaks(IEventBus modEventBus, ModContainer modContainer) {
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            CobblemonEvents.POKEMON_ENTITY_SPAWN.subscribe(Priority.NORMAL, spawnEvent -> {
                if (RepelSpawnPredicate.shouldCancelSpawn(spawnEvent)) {
                    spawnEvent.cancel();
                }
                return Unit.INSTANCE; // <- Cobblemon events require returning Unit.INSTANCE
            });
        });


        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.logDirtBlock) {
            LOGGER.info("DIRT BLOCK >> {}", net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(net.minecraft.world.level.block.Blocks.DIRT));
        }

        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);

        Config.items.forEach(item -> LOGGER.info("ITEM >> {}", item.toString()));
    }

    private static void onClientSetup(final FMLClientSetupEvent event) {
        LOGGER.info("HELLO FROM CLIENT SETUP");
        LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
    }
}
