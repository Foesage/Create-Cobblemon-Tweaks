package cobblemon.creatified.mixin;

import com.cobblemon.mod.common.api.events.entity.SpawnEvent;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import us.timinc.mc.cobblemon.spawnnotification.events.PlayShinySound;

@Mixin(value = PlayShinySound.class, remap = false)
public abstract class PlayShinySoundMixin {

    @Inject(method = "handle", at = @At("HEAD"), cancellable = true)
    private static void onHandle(SpawnEvent<PokemonEntity> event, CallbackInfo ci) {
        Entity entity = event.getEntity();
       if (entity == null || entity.isRemoved() || entity.getPersistentData().getBoolean("RepelBlocked")) {
           System.out.println("[COBBLETWEAKS] Canceling PlayShinySound for RepelBlocked or removed entity!");
           ci.cancel();
       }
    }
}
