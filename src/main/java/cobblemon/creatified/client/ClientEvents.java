package cobblemon.creatified.client;

import cobblemon.creatified.CobblemonCreatified;
import cobblemon.creatified.item.ModItems;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;

@OnlyIn(Dist.CLIENT)
public class ClientEvents {

    public static void register(IEventBus modEventBus) {
        modEventBus.addListener(ClientEvents::onRegisterReloadListeners);
        modEventBus.addListener(ClientEvents::onModelLoad);

        // Register item predicates as soon as the mod event bus is ready (client only)
        modEventBus.addListener(event -> registerProperties());
    }

    @SubscribeEvent
    public static void onModelLoad(ModelEvent.RegisterAdditional event) {
        // Register both the 2D and 3D models used by BallItemRenderer
        event.register(CobblemonCreatified.model("item/copper_ball_blank/item"));
        event.register(CobblemonCreatified.model("item/copper_ball_blank/item_in_hand"));
    }

    @SubscribeEvent
    public static void onRegisterReloadListeners(RegisterClientReloadListenersEvent event) {
        // Reserved for model reload hooks or texture reloads
    }

    /** Registers item predicates for model overrides */
    public static void registerProperties() {
        ItemProperties.register(
                ModItems.COPPER_BALL_BLANK.get(),
                ResourceLocation.fromNamespaceAndPath("cobblemoncreatified", "in_gui"), // Predicate name for your model json
                (ItemStack stack, ClientLevel level, LivingEntity entity, int seed) -> {
                    // If not held by an entity, it's in GUI/inventory
                    return (entity == null) ? 1.0F : 0.0F;
                }
        );
    }
}
