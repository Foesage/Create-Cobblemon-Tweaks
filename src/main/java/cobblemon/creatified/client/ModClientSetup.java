package cobblemon.creatified.client;

import cobblemon.creatified.client.model.BallModelLoader;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.bus.api.SubscribeEvent;

@Mod.EventBusSubscriber(
        modid = "cobblemoncreatified",
        bus = Mod.EventBusSubscriber.Bus.MOD,
        value = Dist.CLIENT
)
public class ModClientSetup {

    /**
     * Registers custom model loaders for items.
     */
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void registerGeometryLoaders(ModelEvent.RegisterGeometryLoaders event) {
        // "ball_model_loader" matches the "loader" property in your item model JSON
        event.register("ball_model_loader", new BallModelLoader());
    }
}
