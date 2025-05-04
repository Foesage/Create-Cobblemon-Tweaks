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

public class TestParticleItem extends Item {

    public TestParticleItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("DEBUG: Triggers evo_particles animation."));
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        if (!(target instanceof PokemonEntity pokemonEntity) || !player.getMainHandItem().is(stack.getItem())) {
            return super.interactLivingEntity(stack, player, target, hand);
        }

        Level level = player.level();
        if (!level.isClientSide) {
            // Play test sound
            level.playSound(null, target.blockPosition(), SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 1.0f, 1.0f);

            // Build evo_particles effect
            SpawnSnowstormEntityParticlePacket packet = new SpawnSnowstormEntityParticlePacket(
                    cobblemonResource("evo_sparkleburst"),
                    pokemonEntity.getId(),
                    List.of("evo_sparkleburst", "middle"),
                    null,
                    null
            );

            // Send to nearby players
            MinecraftServer server = ((ServerLevel) level).getServer();
            List<ServerPlayer> players = server.getPlayerList().getPlayers();

            for (ServerPlayer p : players) {
                if (p.level().dimension().equals(level.dimension()) &&
                        p.distanceToSqr(target) <= 64 * 64) {
                    packet.sendToPlayer(p);
                }
            }

            // Output debug message
            player.sendSystemMessage(Component.literal("✨ Triggered evo_particles animation."));
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }
}
