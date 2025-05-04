package cobblemon.creatified.item;

import com.cobblemon.mod.common.CobblemonSounds;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.net.messages.client.effect.SpawnSnowstormEntityParticlePacket;
import com.cobblemon.mod.common.pokemon.Pokemon;
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

public class EpicCandyItem extends Item {

    public EpicCandyItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.cobblemoncreatified.epic_candy"));
        super.appendHoverText(stack, context, tooltip, flag);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        if (player.getMainHandItem().is(stack.getItem()) && target instanceof PokemonEntity pokemonEntity) {
            Pokemon pokemon = pokemonEntity.getPokemon();
            Level level = player.level();

            if (!level.isClientSide) {
                if (pokemon.isFainted()) {
                    player.sendSystemMessage(Component.translatable("message.cobblemoncreatified.pokemon_fainted"));
                    return InteractionResult.FAIL;
                }

                if (pokemon.getLevel() >= 100) {
                    player.sendSystemMessage(Component.translatable("message.cobblemoncreatified.pokemon_max_level"));
                    return InteractionResult.FAIL;
                }

                int newLevel = Math.min(100, pokemon.getLevel() + 50);
                pokemon.setLevel(newLevel);

                //SOUNDS -- Use CobblemonSounds for cobblemon sounds or SoundEvents for vanilla sounds.
                // Play eating sound
                level.playSound(null, target.blockPosition(), CobblemonSounds.BERRY_EAT, SoundSource.PLAYERS, 1.0f, 1.0f);

                // Play totem effect
                level.playSound(null, target.blockPosition(), CobblemonSounds.POKE_BALL_SEND_OUT, SoundSource.PLAYERS, 1.15f, 0.60f);

                // Pokémon cry
                pokemonEntity.cry();

                // Sparkle effect 1: evo_sparklefetti
                SpawnSnowstormEntityParticlePacket shinyPacket = new SpawnSnowstormEntityParticlePacket(
                        cobblemonResource("evo_sparklefetti"),
                        pokemonEntity.getId(),
                        List.of("evo_sparklefetti", "middle"),
                        null,
                        null
                );

                // Sparkle effect 2: evo_implode
                SpawnSnowstormEntityParticlePacket burstPacket = new SpawnSnowstormEntityParticlePacket(
                        cobblemonResource("evo_implode"),
                        pokemonEntity.getId(),
                        List.of("evo_implode", "middle"),
                        null,
                        null
                );

                // Send packets to nearby players
                MinecraftServer server = ((ServerLevel) level).getServer();
                for (ServerPlayer p : server.getPlayerList().getPlayers()) {
                    if (p.level().dimension().equals(level.dimension()) &&
                            p.distanceToSqr(pokemonEntity) <= 64 * 64) {
                        shinyPacket.sendToPlayer(p);
                        burstPacket.sendToPlayer(p);
                    }
                }

                // Feedback
                player.sendSystemMessage(Component.literal(
                        pokemon.getSpecies().getName() + " is now level " + newLevel + "!"
                ));

                // Consume item
                stack.shrink(1);
            }

            return InteractionResult.SUCCESS;
        }

        return super.interactLivingEntity(stack, player, target, hand);
    }
}
