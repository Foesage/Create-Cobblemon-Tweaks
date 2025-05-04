package cobblemon.creatified.item;

import com.cobblemon.mod.common.api.pokemon.stats.Stat;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.net.messages.client.effect.SpawnSnowstormEntityParticlePacket;
import com.cobblemon.mod.common.pokemon.IVs;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
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
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.List;

import static com.cobblemon.mod.common.util.MiscUtilsKt.cobblemonResource;

public class BottlecapItem extends Item {

    private final Stat stat;
    private final String tooltipTranslationKey;
    private final int delta;

    public BottlecapItem(Properties properties, Stat stat, String tooltipTranslationKey, int delta) {
        super(properties);
        this.stat = stat;
        this.tooltipTranslationKey = tooltipTranslationKey;
        this.delta = delta;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable(tooltipTranslationKey));
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        if (player.getMainHandItem().is(stack.getItem()) && target instanceof PokemonEntity pokemonEntity) {
            Level level = player.level();

            if (!level.isClientSide) {
                if (pokemonEntity.getOwnerUUID() == null || !pokemonEntity.getOwnerUUID().equals(player.getUUID())) {
                    player.sendSystemMessage(Component.literal("You cannot use this on someone else's Pokémon."));
                    return InteractionResult.FAIL;
                }

                var pokemon = pokemonEntity.getPokemon();
                IVs ivs = pokemon.getIvs();
                int current = ivs.get(stat);
                int updated = Math.max(0, Math.min(31, current + delta));

                if (updated == current) {
                    player.sendSystemMessage(Component.literal("⚠ No change: ")
                            .append(pokemon.getSpecies().getTranslatedName())
                            .append("'s ")
                            .append(stat.getDisplayName())
                            .append(" IV is already at ")
                            .append(Component.literal(String.valueOf(current)))
                            .append("."));
                    return InteractionResult.FAIL;
                }

                // Apply change and consume item
                pokemon.setIV(stat, updated);
                stack.shrink(1);

                // Play appropriate sound
                if (delta < 0) {
                    level.playSound(
                            null,
                            target.blockPosition(),
                            BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.fromNamespaceAndPath("minecraft", "block.fire.extinguish"))

                    ,
                            SoundSource.PLAYERS,
                            1.0f,
                            0.9f
                    );
                } else {
                    level.playSound(null, target.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1.0f, 1.0f);
                }

                // Trigger custom particle effect: heavy_collapse_smoke
                SpawnSnowstormEntityParticlePacket smokePacket = new SpawnSnowstormEntityParticlePacket(
                        cobblemonResource("heavy_collapse_smoke"),
                        pokemonEntity.getId(),
                        List.of("heavy_collapse_smoke", "middle"),
                        null,
                        null
                );


                // Send both to nearby players
                MinecraftServer server = ((ServerLevel) level).getServer();
                for (ServerPlayer p : server.getPlayerList().getPlayers()) {
                    if (p.level().dimension().equals(level.dimension()) &&
                            p.distanceToSqr(pokemonEntity) <= 64 * 64) {
                        smokePacket.sendToPlayer(p);
                    }
                }



                // Notify player
                player.sendSystemMessage(Component.literal("🎯 ")
                        .append(pokemon.getSpecies().getTranslatedName())
                        .append("'s ")
                        .append(stat.getDisplayName())
                        .append(" IV changed from ")
                        .append(Component.literal(String.valueOf(current)))
                        .append(" to ")
                        .append(Component.literal(String.valueOf(updated)))
                        .append("!"));
            }

            return InteractionResult.SUCCESS;
        }

        return super.interactLivingEntity(stack, player, target, hand);
    }
}
