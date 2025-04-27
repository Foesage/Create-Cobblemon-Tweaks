package cobblemon.tweaks.item;

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
                    player.sendSystemMessage(Component.translatable("message.createcobblemontweaks.pokemon_fainted"));
                    return InteractionResult.FAIL;
                }

                if (pokemon.getLevel() >= 100) {
                    player.sendSystemMessage(Component.translatable("message.createcobblemontweaks.pokemon_max_level"));
                    return InteractionResult.FAIL;
                }

                int newLevel = Math.min(100, pokemon.getLevel() + 50);
                pokemon.setLevel(newLevel);

                level.playSound(null, target.getOnPos(), CobblemonSounds.BERRY_EAT, SoundSource.PLAYERS, 1.0f, 1.0f);

                player.sendSystemMessage(Component.literal(
                        pokemon.getSpecies().getName() + " is now level " + pokemon.getLevel() + "!"
                ));

                stack.shrink(1); // Consume the Epic Candy
            }
        }
        return super.interactLivingEntity(stack, player, target, hand);
    }



    public void appendHoverText(ItemStack arg, Item.TooltipContext arg2, List<Component> listComponent, TooltipFlag arg3) {
        listComponent.add(Component.translatable("tooltip.createcobblemontweaks.epic_candy"));
        super.appendHoverText(arg, arg2, listComponent, arg3);
}
}
