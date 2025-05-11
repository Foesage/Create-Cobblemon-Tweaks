package cobblemon.creatified.tracker;

import net.minecraft.core.BlockPos;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ActiveRepelBlockTracker {
    public static final Set<BlockPos> ACTIVE_BLOCKS = ConcurrentHashMap.newKeySet();

    // ✅ Track the one block that's allowed to show a range indicator
    public static BlockPos activeRangeBlock = null;
}
