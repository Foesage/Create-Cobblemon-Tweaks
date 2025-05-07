package cobblemon.creatified.block.entity;

import cobblemon.creatified.block.entity.ModBlockEntities;
import cobblemon.creatified.spawner.LuringIncenseSpawner;
import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.spawning.CobblemonSpawnPools;
import com.cobblemon.mod.common.api.spawning.detail.SpawnPool;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class LuringIncenseBlockEntity extends BlockEntity {

    private LuringIncenseSpawner spawner;

    public LuringIncenseBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.LURING_INCENSE.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, LuringIncenseBlockEntity be) {
        if (!(level instanceof ServerLevel serverLevel)) return;

        boolean powered = state.getValue(BlockStateProperties.POWERED);

        if (!powered) {
            if (be.spawner == null) {
                SpawnPool pool = CobblemonSpawnPools.INSTANCE.getWORLD_SPAWN_POOL();
                if (pool == null || pool.getDetails().isEmpty()) return;

                be.spawner = new LuringIncenseSpawner(
                        "luring_incense_" + pos.asLong(),
                        pool,
                        Cobblemon.INSTANCE.getBestSpawner().getSpawnerManagers().get(0),
                        serverLevel,
                        pos
                );

                Cobblemon.INSTANCE.getBestSpawner().getSpawnerManagers().get(0).registerSpawner(be.spawner);
            }
        } else {
            if (be.spawner != null) {
                Cobblemon.INSTANCE.getBestSpawner().getSpawnerManagers().get(0).unregisterSpawner(be.spawner);
                be.spawner = null;
            }
        }
    }

    @Override
    public void setRemoved() {
        if (this.spawner != null && this.level instanceof ServerLevel) {
            Cobblemon.INSTANCE.getBestSpawner().getSpawnerManagers().get(0).unregisterSpawner(this.spawner);
            this.spawner = null;
        }
        super.setRemoved();
    }
}
