package cobblemon.tweaks.spawn;

import cobblemon.tweaks.block.RepelBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import com.cobblemon.mod.common.api.events.entity.SpawnEvent;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;


public class RepelSpawnPredicate {

    private static final int BLOCK_RANGE = 48; // 3 chunks = 48 blocks

    public static boolean shouldCancelSpawn(SpawnEvent<PokemonEntity> event) {
        Level level = event.getEntity().level(); // Get the world (level)
        BlockPos spawnPos = event.getEntity().blockPosition();

        return isNearActiveRepelBlock(level, spawnPos);
    }


    private static boolean isNearActiveRepelBlock(Level level, BlockPos spawnPos) {
        int range = BLOCK_RANGE;
        BlockPos.MutableBlockPos checkPos = new BlockPos.MutableBlockPos();

        for (int x = -range; x <= range; x++) {
            for (int y = -16; y <= 16; y++) {
                for (int z = -range; z <= range; z++) {
                    checkPos.set(spawnPos.getX() + x, spawnPos.getY() + y, spawnPos.getZ() + z);
                    BlockState state = level.getBlockState(checkPos);

                    if (state.getBlock() instanceof RepelBlock) {
                        boolean powered = state.getValue(RepelBlock.POWERED);
                        if (!powered) {
                            return true; // Found an active Repel Block
                        }
                    }
                }
            }
        }

        return false; // No active repel block nearby
    }
}
