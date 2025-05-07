package cobblemon.creatified.bait;

import com.cobblemon.mod.common.api.fishing.FishingBait;
import com.cobblemon.mod.common.api.fishing.FishingBait.Effect;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class ModBaits {

    public static final FishingBait LURING_INCENSE_BAIT = new FishingBait(
            ResourceLocation.fromNamespaceAndPath("cobblemoncreatified", "luring_incense"),
            List.of(
                    new Effect(ResourceLocation.fromNamespaceAndPath("cobblemon", "typing"), ResourceLocation.fromNamespaceAndPath("cobblemon", "fairy"), 1.0, 1.0),
                    new Effect(ResourceLocation.fromNamespaceAndPath("cobblemon", "typing"), ResourceLocation.fromNamespaceAndPath("cobblemon", "grass"), 1.0, 1.0),
                    new Effect(ResourceLocation.fromNamespaceAndPath("cobblemon", "egg_group"), ResourceLocation.fromNamespaceAndPath("cobblemon", "fairy"), 1.0, 1.0),
                    new Effect(ResourceLocation.fromNamespaceAndPath("cobblemon", "egg_group"), ResourceLocation.fromNamespaceAndPath("cobblemon", "grass"), 1.0, 1.0)
            )
    );
}
