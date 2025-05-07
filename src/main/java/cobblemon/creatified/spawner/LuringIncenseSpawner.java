package cobblemon.creatified.spawner;

import com.cobblemon.mod.common.api.spawning.SpawnerManager;
import com.cobblemon.mod.common.api.spawning.detail.SpawnPool;
import com.cobblemon.mod.common.api.spawning.detail.EntitySpawnResult;
import com.cobblemon.mod.common.api.spawning.detail.SpawnDetail;
import com.cobblemon.mod.common.api.spawning.spawner.FixedAreaSpawner;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

import java.util.Random;

public class LuringIncenseSpawner extends FixedAreaSpawner {

    private static final Random random = new Random();

    public LuringIncenseSpawner(String name, SpawnPool pool, SpawnerManager manager, ServerLevel level, BlockPos pos) {
        super(name, pool, manager, level, pos, 32, 16, getRandomTicksBetweenSpawns());
    }

    private static float getRandomTicksBetweenSpawns() {
        return 20F * (1 + random.nextInt(30)); // 1–30 seconds
    }

    @Override
    public boolean canSpawn() {
        return true; // Will be toggled via block entity or redstone later
    }

    public void afterSpawn(SpawnDetail detail, EntitySpawnResult result) {
        // Optional logging, effects, particles, etc.
    }
}
