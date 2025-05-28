package cobblemon.creatified.mixin;

import com.cobblemon.mod.common.api.events.entity.SpawnEvent;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import us.timinc.mc.cobblemon.spawnnotification.events.BroadcastSpawn;

@Mixin(value = BroadcastSpawn.class, remap = false)
public abstract class BroadcastSpawnMixin {

    @Inject(method = "handle", at = @At("HEAD"), cancellable = true)
    private static void onHandle(SpawnEvent<?> evt, CallbackInfo ci) {
        Entity entity = evt.getEntity();
        if (entity == null || entity.isRemoved() || entity.getPersistentData().getBoolean("RepelBlocked")) {
            // System.out.println("[COBBLETWEAKS] Canceling BroadcastSpawn due to RepelBlocked or removed entity!");
            ci.cancel();
        }
    }
}
