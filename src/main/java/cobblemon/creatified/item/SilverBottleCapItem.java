package cobblemon.creatified.item;

import com.cobblemon.mod.common.api.pokemon.stats.Stat;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.net.messages.client.effect.SpawnSnowstormEntityParticlePacket;
import com.cobblemon.mod.common.pokemon.IVs;
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

import java.util.List;

import static com.cobblemon.mod.common.util.MiscUtilsKt.cobblemonResource;

public class SilverBottleCapItem extends Item {

    private final Stat stat;
    private final String tooltipTranslationKey;

    public SilverBottleCapItem(Properties properties, Stat stat, String tooltipTranslationKey) {
        super(properties);
        this.stat = stat;
        this.tooltipTranslationKey = tooltipTranslationKey;
    }

    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable(tooltipTranslationKey));
    }

    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        if (player.getMainHandItem().is(stack.getItem()) && target instanceof PokemonEntity pokemonEntity) {
            var level = player.level();

            if (!level.isClientSide) {
                if (pokemonEntity.getOwnerUUID() == null || !pokemonEntity.getOwnerUUID().equals(player.getUUID())) {
                    player.sendSystemMessage(Component.literal("You cannot use this item on someone else's Pokémon."));
                    return InteractionResult.FAIL;
                }

                var pokemon = pokemonEntity.getPokemon();
                IVs ivs = pokemon.getIvs();
                int oldIv = ivs.get(stat);

                if (oldIv != 31) {
                    pokemon.setIV(stat, 31);
                    stack.shrink(1);

                    // Play sound and Pokémon cry
                    level.playSound(null, target.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1.0f, 1.0f);
                    pokemonEntity.cry();

                    // Sparkle effect 1: shiny_ring
                    SpawnSnowstormEntityParticlePacket shinyPacket = new SpawnSnowstormEntityParticlePacket(
                            cobblemonResource("shiny_ring"),
                            pokemonEntity.getId(),
                            List.of("shiny_particles", "middle"),
                            null,
                            null
                    );

                    // Send both to nearby players
                    MinecraftServer server = ((ServerLevel) level).getServer();
                    for (ServerPlayer p : server.getPlayerList().getPlayers()) {
                        if (p.level().dimension().equals(level.dimension()) &&
                                p.distanceToSqr(pokemonEntity) <= 64 * 64) {
                            shinyPacket.sendToPlayer(p);
                        }
                    }

                    player.sendSystemMessage(Component.literal("Your ")
                            .append(pokemon.getSpecies().getTranslatedName())
                            .append("'s ")
                            .append(stat.getDisplayName())
                            .append(" IV has increased from ")
                            .append(Component.literal(String.valueOf(oldIv)))
                            .append(" to 31!"));
                } else {
                    player.sendSystemMessage(pokemon.getSpecies().getTranslatedName()
                            .copy()
                            .append(" already has perfect ")
                            .append(stat.getDisplayName())
                            .append(" IV!"));
                }
            }
            return InteractionResult.SUCCESS;
        }
        return super.interactLivingEntity(stack, player, target, hand);
    }
}
