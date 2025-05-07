package cobblemon.creatified.util;

import com.cobblemon.mod.common.api.fishing.FishingBait;
import com.cobblemon.mod.common.api.fishing.FishingBait.Effect;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class ModEncounterUtils {

    public static boolean simulateNearbyEncounters(ServerLevel level, BlockPos pos, FishingBait bait) {
        AABB area = new AABB(pos).inflate(48); // 1 chunk radius
        List<PokemonEntity> nearbyPokemon = level.getEntitiesOfClass(PokemonEntity.class, area);

        boolean anyMatched = false;

        for (PokemonEntity entity : nearbyPokemon) {
            String species = entity.getPokemon().getSpecies().getName();

            boolean matchesBait = bait.getEffects().stream().anyMatch(effect -> {
                // Match by type
                if (effect.getType().getPath().equals("typing")) {
                    for (var type : entity.getPokemon().getTypes()) {
                        if (type.getName().equalsIgnoreCase(effect.getSubcategory().getPath())) {
                            return true;
                        }
                    }
                }

                // Match by egg group
                if (effect.getType().getPath().equals("egg_group")) {
                    return entity.getPokemon().getSpecies().getEggGroups().stream()
                            .anyMatch(group -> group.toString().equalsIgnoreCase(effect.getSubcategory().toString()));
                }

                return false;
            });

            if (matchesBait) {
                anyMatched = true;

                // 💬 Send chat message to nearby players
                level.players().forEach(player -> {
                    if (player.distanceToSqr(entity) < 1024) { // 32 block radius
                        player.displayClientMessage(
                                Component.literal("✨ The Luring Incense reacts to " + species + "!"),
                                false
                        );
                    }
                });

                // Console feedback
                System.out.println("✨ Luring Incense would attract: " + species);
            }
        }

        return anyMatched;
    }
}
