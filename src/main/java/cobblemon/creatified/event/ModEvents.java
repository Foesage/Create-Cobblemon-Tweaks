package cobblemon.creatified.event;

import cobblemon.creatified.block.RepelBlock;
import cobblemon.creatified.event.ShinyTokenEvents;
import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.events.entity.SpawnEvent;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import kotlin.Unit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class ModEvents {

    public static void registerListeners() {
        // Register spawn blocking logic
        CobblemonEvents.POKEMON_ENTITY_SPAWN.subscribe(Priority.HIGHEST, ModEvents::onPokemonSpawn);

        // ✅ Correct way to hook in functional event listeners from ShinyTokenEvents
        ShinyTokenEvents.register();
    }

    private static Unit onPokemonSpawn(SpawnEvent<PokemonEntity> event) {
        Level level = event.getCtx().getWorld();
        BlockPos pos = event.getCtx().getPosition();

        if (level != null && pos != null && isNearRepelBlock(level, pos)) {
            event.cancel();
        }

        return Unit.INSTANCE;
    }

    private static boolean isNearRepelBlock(Level level, BlockPos pos) {
        int radius = 48; // 3 chunk radius
        int step = 3;    // Skip-check every 3 blocks for speed

        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

        for (int dx = -radius; dx <= radius; dx += step) {
            for (int dz = -radius; dz <= radius; dz += step) {
                for (int dy = -5; dy <= 5; dy++) {
                    mutablePos.set(pos.getX() + dx, pos.getY() + dy, pos.getZ() + dz);
                    Block block = level.getBlockState(mutablePos).getBlock();
                    if (block instanceof RepelBlock) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
