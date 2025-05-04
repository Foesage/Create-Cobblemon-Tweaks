package cobblemon.creatified.item;

import cobblemon.creatified.util.ParticleEffects;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.net.messages.client.effect.SpawnSnowstormEntityParticlePacket;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.IVs;
import com.cobblemon.mod.common.api.pokemon.stats.Stat;
import com.cobblemon.mod.common.api.pokemon.stats.Stats;
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

public class GoldBottlecapItem extends Item {

    private final String tooltipTranslationKey;

    public GoldBottlecapItem(Properties properties, String tooltipTranslationKey) {
        super(properties);
        this.tooltipTranslationKey = tooltipTranslationKey;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable(tooltipTranslationKey));
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        if (!(target instanceof PokemonEntity pokemonEntity) || !player.getMainHandItem().is(stack.getItem()))
            return super.interactLivingEntity(stack, player, target, hand);

        Level level = player.level();

        if (!level.isClientSide) {
            if (pokemonEntity.getOwnerUUID() == null || !pokemonEntity.getOwnerUUID().equals(player.getUUID())) {
                player.sendSystemMessage(Component.literal("You cannot use this item on someone else's Pokémon."));
                return InteractionResult.FAIL;
            }

            Pokemon pokemon = pokemonEntity.getPokemon();
            IVs ivs = pokemon.getIvs();
            boolean changed = false;

            for (Stat stat : List.of(
                    Stats.HP,
                    Stats.ATTACK,
                    Stats.DEFENCE,
                    Stats.SPECIAL_ATTACK,
                    Stats.SPECIAL_DEFENCE,
                    Stats.SPEED
            )) {
                if (ivs.get(stat) < 31) {
                    pokemon.setIV(stat, 31);
                    changed = true;
                }
            }

            if (!changed) {
                player.sendSystemMessage(pokemon.getSpecies().getTranslatedName()
                        .copy()
                        .append(" already has perfect IVs."));
                return InteractionResult.FAIL;
            }


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

            // Confirm and consume
            player.sendSystemMessage(Component.literal("🎯 All IVs maximized for ").append(pokemon.getSpecies().getTranslatedName()).append("!"));
            stack.shrink(1);
        }

        return InteractionResult.SUCCESS;
    }
}
