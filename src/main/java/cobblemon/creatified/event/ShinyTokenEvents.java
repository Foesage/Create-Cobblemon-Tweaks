package cobblemon.creatified.event;

import cobblemon.creatified.item.InertShinyTokenItem;
import cobblemon.creatified.item.ModItems;
import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.battles.model.actor.ActorType;
import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.events.battles.BattleVictoryEvent;
import com.cobblemon.mod.common.api.events.storage.ReleasePokemonEvent;
import com.cobblemon.mod.common.battles.actor.PlayerBattleActor;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kotlin.Unit;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class ShinyTokenEvents {

    public static void register() {
        CobblemonEvents.BATTLE_VICTORY.subscribe(Priority.NORMAL, event -> {
            handleBattleVictory(event);
            return Unit.INSTANCE;
        });

        CobblemonEvents.POKEMON_RELEASED_EVENT_POST.subscribe(Priority.NORMAL, event -> {
            handlePokemonRelease(event);
            return Unit.INSTANCE;
        });
    }

    private static void handleBattleVictory(BattleVictoryEvent event) {
        long wildsDefeated = event.getLosers().stream()
                .filter(actor -> actor.getType() == ActorType.WILD)
                .count();

        if (wildsDefeated == 0)
            return;

        for (BattleActor winner : event.getWinners()) {
            if (winner instanceof PlayerBattleActor playerActor) {
                UUID uuid = playerActor.getPlayerUUIDs().stream().findFirst().orElse(null);
                if (uuid == null) continue;

                ServerPlayer player = event.getBattle().getPlayers().stream()
                        .filter(p -> p.getUUID().equals(uuid))
                        .findFirst()
                        .orElse(null);

                if (player == null) continue;

                for (int i = 0; i < wildsDefeated; i++) {
                    applyCharge(player);
                }

                System.out.println("[ShinyToken] Applied " + wildsDefeated + " charge(s) to " + player.getName().getString());
            }
        }
    }

    private static void handlePokemonRelease(ReleasePokemonEvent event) {
        Pokemon pokemon = event.getPokemon();
        ServerPlayer player = event.getPlayer();

        if (pokemon.getShiny()) {
            applyCharge(player);
            System.out.println("[ShinyToken] Released shiny Pokémon. Applied 1 charge to " + player.getName().getString());
        }
    }

    private static void applyCharge(Player player) {
        for (ItemStack stack : player.getInventory().items) {
            if (stack.getItem() == ModItems.INERT_SHINY_TOKEN.get()) {
                InertShinyTokenItem.addCharge(stack, player);
                return;
            }
        }
    }
}
