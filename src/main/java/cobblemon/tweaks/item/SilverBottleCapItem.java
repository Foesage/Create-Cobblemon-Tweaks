package cobblemon.tweaks.item;

import com.cobblemon.mod.common.api.pokemon.stats.Stat;
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

public class SilverBottleCapItem extends Item {

    private final Stat stat;
    private final String tooltipTranslationKey;

    public SilverBottleCapItem(Properties properties, Stat stat, String tooltipTranslationKey) {
        super(properties);
        this.stat = stat;
        this.tooltipTranslationKey = tooltipTranslationKey;
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        if (player.getMainHandItem().is(stack.getItem()) && target instanceof PokemonEntity pokemonEntity) {
            Level level = player.level();

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

                    level.playSound(null, target.getOnPos(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1.0f, 1.0f);

                    player.sendSystemMessage(Component.literal("Your ")
                            .append(Component.literal(pokemon.getSpecies().getName()))
                            .append("'s ")
                            .append(stat.getDisplayName())
                            .append(" IV has increased from ")
                            .append(Component.literal(String.valueOf(oldIv)))
                            .append(" to 31!"));
                } else {
                    player.sendSystemMessage(Component.literal(pokemon.getSpecies().getName())
                            .append(" already has perfect ")
                            .append(stat.getDisplayName())
                            .append(" IV!"));

                }
            }
            return InteractionResult.SUCCESS;
        }

        return super.interactLivingEntity(stack, player, target, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag flag) {
        components.add(Component.translatable(tooltipTranslationKey));
        super.appendHoverText(stack, context, components, flag);
    }
}
