package cobblemon.creatified.item;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.net.messages.client.effect.SpawnSnowstormEntityParticlePacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
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
        if (!player.getMainHandItem().is(stack.getItem()))
            return super.interactLivingEntity(stack, player, target, hand);

        Level level = player.level();
        if (level.isClientSide)
            return InteractionResult.PASS;

        ServerLevel serverLevel = (ServerLevel) level;
        MinecraftServer server = serverLevel.getServer();

        // Play debug sound
        level.playSound(null, target.blockPosition(), SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 1.0f, 1.0f);

        Entity anchor;
        List<String> particleBones;

        if (target instanceof PokemonEntity) {
            // Use the actual Pokémon model as the target
            anchor = target;
            particleBones = List.of("evo_sparkleburst", "middle");
        } else {
            // Create invisible Armor Stand as anchor
            BlockPos pos = target.blockPosition();
            ArmorStand marker = new ArmorStand(serverLevel, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
            marker.setInvisible(true);
            marker.setNoGravity(true);
            marker.setInvulnerable(true);
            // Can't set marker flag, but this is sufficient for particle anchoring
            serverLevel.addFreshEntity(marker);

            anchor = marker;
            particleBones = List.of("evo_sparkleburst");
        }

        // Build evo_particles effect
        SpawnSnowstormEntityParticlePacket packet = new SpawnSnowstormEntityParticlePacket(
                cobblemonResource("shiny_ring2"),
                anchor.getId(),
                List.of(), // <- No bone names
                null,
                null
        );


        // Send to nearby players
        for (ServerPlayer p : server.getPlayerList().getPlayers()) {
            if (p.level().dimension().equals(level.dimension()) &&
                    p.distanceToSqr(anchor) <= 64 * 64) {
                packet.sendToPlayer(p);
            }
        }

        // Output debug message
        player.sendSystemMessage(Component.literal("✨ Triggered evo_particles animation."));
        return InteractionResult.SUCCESS;
    }
}
