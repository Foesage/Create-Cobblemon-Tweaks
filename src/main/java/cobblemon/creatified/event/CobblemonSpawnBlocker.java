package cobblemon.creatified.event;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.events.entity.SpawnEvent;
import cobblemon.creatified.CobblemonCreatified;
import kotlin.Unit;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;

import java.util.stream.StreamSupport;

public class CobblemonSpawnBlocker {

    public static void register() {
        CobblemonEvents.POKEMON_ENTITY_SPAWN.subscribe(Priority.HIGHEST, (SpawnEvent<?> event) -> {
            Entity entity = event.getEntity();
            if (!(entity instanceof PokemonEntity)) return Unit.INSTANCE;
            PokemonEntity pokemonEntity = (PokemonEntity) entity;

            Level level = pokemonEntity.level();
            BlockPos spawnPos = pokemonEntity.blockPosition();

            int radius = 64; // 🔥 Large radius for repel detection

            boolean blockerNearby = StreamSupport.stream(BlockPos.betweenClosed(
                    spawnPos.offset(-radius, -radius, -radius),
                    spawnPos.offset(radius, radius, radius)
            ).spliterator(), false).anyMatch(pos -> {
                BlockState state = level.getBlockState(pos);
                Block block = state.getBlock();
                ResourceLocation blockId = block.builtInRegistryHolder().key().location();
                return blockId != null && blockId.toString().equals("cobblemoncreatified:repel_block");
            });

            if (blockerNearby) {
                // ✅ Always mark RepelBlocked even for normal Pokémon
                pokemonEntity.getPersistentData().putBoolean("RepelBlocked", true);

                Pokemon pokemon = pokemonEntity.getPokemon();
                if (pokemon != null && pokemon.getShiny()) {
                    // 🔥 Instantly remove shiny Pokémon if near repel block
                    pokemonEntity.remove(Entity.RemovalReason.DISCARDED);
               //     CobblemonCreatified.LOGGER.info("[COBBLETWEAKS] Blocked and immediately removed shiny Pokémon at {}", spawnPos);
                } else {
                //    CobblemonCreatified.LOGGER.info("[COBBLETWEAKS] Marked normal Pokémon as RepelBlocked at {}", spawnPos);
                }
            }

            return Unit.INSTANCE;
        });
    }
}
