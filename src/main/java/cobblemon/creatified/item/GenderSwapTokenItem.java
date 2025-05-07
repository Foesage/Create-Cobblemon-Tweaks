package cobblemon.creatified.item;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.net.messages.client.effect.SpawnSnowstormEntityParticlePacket;
import com.cobblemon.mod.common.pokemon.Gender;
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

public class GenderSwapTokenItem extends Item {

    public GenderSwapTokenItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("§7Use on a Pokémon to change its gender."));
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        if (!(target instanceof PokemonEntity pokemonEntity))
            return InteractionResult.PASS;

        Level level = target.level();
        if (level.isClientSide)
            return InteractionResult.SUCCESS;

        var pokemon = pokemonEntity.getPokemon();

        // Check ownership
        if (pokemonEntity.getOwnerUUID() == null || !pokemonEntity.getOwnerUUID().equals(player.getUUID())) {
            player.sendSystemMessage(Component.literal("You cannot use this on someone else's Pokémon."));
            return InteractionResult.FAIL;
        }

        // Gender change logic with corresponding particle effect
        switch (pokemon.getGender().name()) {
            case "MALE" -> {
                pokemon.setGender(Gender.FEMALE);
                player.sendSystemMessage(Component.literal("♀ " + pokemon.getSpecies().getTranslatedName().getString() + " is now female."));
                stack.shrink(1);
                sendMultipleParticles((ServerLevel) level, pokemonEntity, "impact_fairy", 40);
            }
            case "FEMALE" -> {
                pokemon.setGender(Gender.MALE);
                player.sendSystemMessage(Component.literal("♂ " + pokemon.getSpecies().getTranslatedName().getString() + " is now male."));
                stack.shrink(1);
                sendMultipleParticles((ServerLevel) level, pokemonEntity, "impact_water", 40);
            }
            case "GENDERLESS" -> {
                player.sendSystemMessage(Component.literal(pokemon.getSpecies().getTranslatedName().getString() + " is genderless and can't be changed."));
                return InteractionResult.FAIL;
            }
        }

        // Feedback sound
        level.playSound(null, target.blockPosition(), SoundEvents.EVOKER_CAST_SPELL, SoundSource.PLAYERS, 2.0f, 0.7f);

        return InteractionResult.SUCCESS;
    }

    private void sendMultipleParticles(ServerLevel level, PokemonEntity entity, String particleName, int count) {
        MinecraftServer server = level.getServer();
        for (ServerPlayer p : server.getPlayerList().getPlayers()) {
            if (p.level().dimension().equals(level.dimension()) &&
                    p.distanceToSqr(entity) <= 64 * 64) {

                for (int i = 0; i < count; i++) {
                    SpawnSnowstormEntityParticlePacket packet = new SpawnSnowstormEntityParticlePacket(
                            cobblemonResource(particleName),
                            entity.getId(),
                            List.of(particleName, "middle"),
                            null,
                            null
                    );
                    packet.sendToPlayer(p);
                }
            }
        }
    }
}
