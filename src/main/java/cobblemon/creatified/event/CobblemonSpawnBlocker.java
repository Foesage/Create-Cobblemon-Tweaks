package cobblemon.creatified.event;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.events.entity.SpawnEvent;
import cobblemon.creatified.CobblemonCreatified;
import cobblemon.creatified.block.RepelBlock;
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

            int maxRadius = 64;
            int verticalRange = 64;

            boolean blockerNearby = StreamSupport.stream(BlockPos.betweenClosed(
                    spawnPos.offset(-maxRadius, -verticalRange, -maxRadius),
                    spawnPos.offset(maxRadius, verticalRange, maxRadius)
            ).spliterator(), false).anyMatch(pos -> {
                BlockState state = level.getBlockState(pos);
                Block block = state.getBlock();
                ResourceLocation blockId = block.builtInRegistryHolder().key().location();

                if (blockId != null
                        && blockId.toString().equals("cobblemoncreatified:repel_block")
                        && state.hasProperty(RepelBlock.POWERED)
                        && state.getValue(RepelBlock.POWERED)) {

                    int redstonePower = level.getBestNeighborSignal(pos);
                    int effectiveRadius = Math.max(1, (int)(maxRadius * (1.0 - redstonePower / 15.0)));

                    // Check if within effective radius
                    double dx = pos.getX() + 0.5 - spawnPos.getX();
                    double dz = pos.getZ() + 0.5 - spawnPos.getZ();
                    double horizontalDistanceSq = dx * dx + dz * dz;
                    int dy = Math.abs(pos.getY() - spawnPos.getY());

                    return horizontalDistanceSq <= effectiveRadius * effectiveRadius && dy <= verticalRange;
                }

                return false;
            });

            if (blockerNearby) {
                pokemonEntity.getPersistentData().putBoolean("RepelBlocked", true);

                Pokemon pokemon = pokemonEntity.getPokemon();
                if (pokemon != null && pokemon.getShiny()) {
                    pokemonEntity.remove(Entity.RemovalReason.DISCARDED);
                    CobblemonCreatified.LOGGER.info("[COBBLETWEAKS] Blocked and immediately removed shiny Pokémon at {}", spawnPos);
                } else {
                    pokemonEntity.remove(Entity.RemovalReason.DISCARDED);
                    CobblemonCreatified.LOGGER.info("[COBBLETWEAKS] Blocked and removed normal Pokémon at {}", spawnPos);
                }
            }

            return Unit.INSTANCE;
        });
    }
}
