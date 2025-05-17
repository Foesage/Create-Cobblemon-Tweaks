package cobblemon.creatified.client;

import cobblemon.creatified.CobblemonCreatified;
import net.minecraft.client.resources.model.ModelResourceLocation;
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
}
