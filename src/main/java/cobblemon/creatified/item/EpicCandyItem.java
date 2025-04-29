package cobblemon.creatified.item;

import com.cobblemon.mod.common.CobblemonSounds;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EpicCandyItem extends Item {

    public EpicCandyItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        if (player.getMainHandItem().is(stack.getItem()) && target instanceof PokemonEntity pokemonEntity) {
            Level level = player.level();
            Pokemon pokemon = pokemonEntity.getPokemon();

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

                level.playSound(null, target.blockPosition(), CobblemonSounds.BERRY_EAT, SoundSource.PLAYERS, 1.0f, 1.0f);

                player.sendSystemMessage(Component.literal(
                        pokemon.getSpecies().getName() + " is now level " + newLevel + "!"
                ));

                stack.shrink(1); // Consume the Epic Candy
            }
            return InteractionResult.SUCCESS;
        }
        return super.interactLivingEntity(stack, player, target, hand);
    }


    public void appendHoverText(ItemStack stack, @Nullable BlockGetter getter, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.cobblemoncreatified.epic_candy")); // (or whatever text)
        super.appendHoverText(stack, (TooltipContext) getter, tooltip, flag);
    }

}
