package cobblemon.creatified.item;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.net.messages.client.effect.SpawnSnowstormEntityParticlePacket;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

import static com.cobblemon.mod.common.util.MiscUtilsKt.cobblemonResource;

public class ShinyTokenItem extends Item {

    public ShinyTokenItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.cobblemoncreatified.shiny_token"));
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        if (!(target instanceof PokemonEntity pokemonEntity) || !player.getMainHandItem().is(stack.getItem())) {
            return super.interactLivingEntity(stack, player, target, hand);
        }

        Level level = player.level();
        if (!level.isClientSide) {
            var pokemon = pokemonEntity.getPokemon();

            // Ownership check
            if (pokemonEntity.getOwnerUUID() == null || !pokemonEntity.getOwnerUUID().equals(player.getUUID())) {
                player.sendSystemMessage(Component.literal("You cannot use this on someone else's Pokémon."));
                return InteractionResult.FAIL;
            }

            // Already shiny check
            if (pokemon.getShiny()) {
                player.sendSystemMessage(Component.literal(pokemon.getSpecies().getTranslatedName().getString() + " is already shiny."));
                return InteractionResult.FAIL;
            }

            // Apply shiny status and consume item
            pokemon.setShiny(true);
            stack.shrink(1);

            // Play sound and Pokémon cry
            level.playSound(null, target.blockPosition(), SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 1.0f, 1.0f);
            pokemonEntity.cry();

            // Sparkle effect 1: shiny_ring
            SpawnSnowstormEntityParticlePacket shinyPacket = new SpawnSnowstormEntityParticlePacket(
                    cobblemonResource("shiny_ring"),
                    pokemonEntity.getId(),
                    List.of("shiny_particles", "middle"),
                    null,
                    null
            );

// Sparkle effect 2: evo_sparkleburst
            SpawnSnowstormEntityParticlePacket burstPacket = new SpawnSnowstormEntityParticlePacket(
                    cobblemonResource("evo_sparkleburst"),
                    pokemonEntity.getId(),
                    List.of("evo_sparkleburst", "middle"),
                    null,
                    null
            );

// Send both to nearby players
            MinecraftServer server = ((ServerLevel) level).getServer();
            for (ServerPlayer p : server.getPlayerList().getPlayers()) {
                if (p.level().dimension().equals(level.dimension()) &&
                        p.distanceToSqr(pokemonEntity) <= 64 * 64) {
                    shinyPacket.sendToPlayer(p);
                    burstPacket.sendToPlayer(p);
                }
            }


            // Confirmation message
            player.sendSystemMessage(Component.literal("✨ " + pokemon.getSpecies().getTranslatedName().getString() + " is now shiny!"));
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }
}
