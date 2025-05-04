package cobblemon.creatified.util;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.net.messages.client.effect.SpawnSnowstormEntityParticlePacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

import java.util.Arrays;
import java.util.List;

import static com.cobblemon.mod.common.util.MiscUtilsKt.cobblemonResource;

public class ParticleEffects {

    private static void sendParticleToNearbyPlayers(ServerLevel level, PokemonEntity entity, SpawnSnowstormEntityParticlePacket packet) {
        MinecraftServer server = level.getServer();
        List<ServerPlayer> players = server.getPlayerList().getPlayers();

        for (ServerPlayer player : players) {
            if (player.level().dimension().equals(level.dimension()) &&
                    player.distanceToSqr(entity) <= 64 * 64) {
                packet.sendToPlayer(player);
            }
        }
    }

    public static void playShinySparkle(PokemonEntity entity) {
        if (!(entity.level() instanceof ServerLevel level)) return;

        SpawnSnowstormEntityParticlePacket packet = new SpawnSnowstormEntityParticlePacket(
                cobblemonResource("shiny_ring"),
                entity.getId(),
                List.of("shiny_particles", "middle"),
                null,
                null
        );

        sendParticleToNearbyPlayers(level, entity, packet);
        System.out.println("[ParticleEffects] Played: shiny_ring");
    }

    public static void playEvolutionRing(PokemonEntity entity) {
        if (!(entity.level() instanceof ServerLevel level)) return;

        SpawnSnowstormEntityParticlePacket packet = new SpawnSnowstormEntityParticlePacket(
                cobblemonResource("evolution"),
                entity.getId(),
                List.of("default"),
                null,
                List.of("default")
        );

        sendParticleToNearbyPlayers(level, entity, packet);
        System.out.println("[ParticleEffects] Played: evolution");
    }

    public static void playEvoSparkleburst(PokemonEntity entity) {
        if (!(entity.level() instanceof ServerLevel level)) return;

        SpawnSnowstormEntityParticlePacket packet = new SpawnSnowstormEntityParticlePacket(
                cobblemonResource("evo_sparkleburst"),
                entity.getId(),
                List.of("evo_sparkleburst", "middle"),
                null,
                null
        );

        sendParticleToNearbyPlayers(level, entity, packet);
        System.out.println("[ParticleEffects] Played: evo_sparkleburst");
    }

    public static void playTestParticlesSequentially(PokemonEntity entity) {
        if (!(entity.level() instanceof ServerLevel level)) return;

        List<String> testEffects = Arrays.asList(
                "shiny_ring",
                "shiny_sparkle_rainbow",
                "evolution",
                "level_up",
                "power_up",
                "confetti_burst",
                "celebration",
                "sparkle_burst",
                "glow_aura"
        );

        for (String effect : testEffects) {
            SpawnSnowstormEntityParticlePacket packet = new SpawnSnowstormEntityParticlePacket(
                    cobblemonResource(effect),
                    entity.getId(),
                    List.of("default"),
                    null,
                    List.of("default")
            );

            for (ServerPlayer player : level.getPlayers(p ->
                    p.level().dimension().equals(level.dimension()) &&
                            p.distanceToSqr(entity) <= 64 * 64)) {
                packet.sendToPlayer(player);
            }

            System.out.println("[ParticleEffects] Played test particle: " + effect);
        }
    }
}
