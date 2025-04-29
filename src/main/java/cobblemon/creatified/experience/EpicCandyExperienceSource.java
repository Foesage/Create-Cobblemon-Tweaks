package cobblemon.creatified.experience;

import com.cobblemon.mod.common.api.pokemon.experience.ExperienceSource;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class EpicCandyExperienceSource implements ExperienceSource {


    public ResourceLocation key() {
        return ResourceLocation.fromNamespaceAndPath("cobblemon", "epic_candy");
    }


    public Component displayName() {
        return Component.literal("Epic Candy");
    }


    public boolean isSidemod() {
        return true;
    }


    public boolean isCommand() {
        return false;
    }


    public boolean isInteraction() {
        return true;
    }


    public boolean isBattle() {
        return false;
    }
}
