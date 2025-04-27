package cobblemon.tweaks.item;

import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.IVs;
import net.minecraft.network.chat.Component;
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

public class AtkSilverBottleCapItem extends Item {

    public AtkSilverBottleCapItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        if (player.getMainHandItem().is(stack.getItem()) && target instanceof PokemonEntity pokemonEntity) {
            Level level = player.level();

            if (!level.isClientSide) {
                // Ownership check
                if (pokemonEntity.getOwnerUUID() == null || !pokemonEntity.getOwnerUUID().equals(player.getUUID())) {
                    player.sendSystemMessage(Component.literal("You cannot use this item on someone else's Pokémon."));
                    return InteractionResult.FAIL;
                }

                var pokemon = pokemonEntity.getPokemon();
                IVs ivs = pokemon.getIvs();
                int oldHpIv = ivs.get(Stats.HP);

                if (oldHpIv != 31) {
                    // Set HP IV to perfect
                    pokemon.setIV(Stats.HP, 31);

                    // Shrink the item (consume it)
                    stack.shrink(1);

                    // Play level-up sound
                    level.playSound(null, target.getOnPos(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1.0f, 1.0f);


                    // Message to player
                    player.sendSystemMessage(Component.literal(
                            "Your " + pokemon.getSpecies().getName() + "'s HP IV has increased from " + oldHpIv + " to 31!"
                    ));
                } else {
                    // Already perfect HP IV
                    player.sendSystemMessage(Component.literal(
                            pokemon.getSpecies().getName() + " already has perfect HP IV!"
                    ));
                }
            }
            return InteractionResult.SUCCESS;
        }

        return super.interactLivingEntity(stack, player, target, hand);
    }

    public void appendHoverText(ItemStack arg, TooltipContext arg2, List<Component> listComponent, TooltipFlag arg3) {
        listComponent.add(Component.translatable("tooltip.createcobblemontweaks.hp_silver_bottlecap"));
        super.appendHoverText(arg, arg2, listComponent, arg3);
    }
}
