package cobblemon.creatified.item;

import cobblemon.creatified.datacomponent.ModComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class InertShinyTokenItem extends Item {

    private static final int MAX_CHARGE = 10;

    public InertShinyTokenItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        int charge = getCharge(stack);
        tooltip.add(Component.literal("§7Charge: §b" + charge + "§7/§b" + MAX_CHARGE));
        tooltip.add(Component.literal("§8Defeat or release shiny Pokémon to activate."));
        super.appendHoverText(stack, context, tooltip, flag);
    }

    public static int getCharge(ItemStack stack) {
        Integer stored = stack.get(ModComponents.SHINY_CHARGE.get());
        if (stored == null) {
            System.out.println("[ShinyToken] No charge found, defaulting to 0");
            return 0;
        }
        System.out.println("[ShinyToken] Current charge read: " + stored);
        return stored;
    }

    public static void addCharge(ItemStack stack, Player player) {
        int charge = getCharge(stack);
        if (charge >= MAX_CHARGE)
            return;

        charge++;
        stack.set(ModComponents.SHINY_CHARGE.get(), charge);
        System.out.println("[ShinyToken] Added charge: " + charge);

        // 🔊 Play charge gain sound
        if (!player.level().isClientSide()) {
            player.level().playSound(null, player.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.8F, 1.0F);
        }

        // 🪄 Promote to shiny token
        if (charge >= MAX_CHARGE) {
            stack.shrink(1);
            ItemStack shinyToken = new ItemStack(ModItems.SHINY_TOKEN.get());

            boolean added = player.getInventory().add(shinyToken);
            if (!added) {
                player.drop(shinyToken, false);
                System.out.println("[ShinyToken] Inventory full, dropped shiny token");
            } else {
                System.out.println("[ShinyToken] Shiny token added to inventory");
            }

            // 🔊 Play shiny unlock sound
            if (!player.level().isClientSide()) {
                player.level().playSound(null, player.blockPosition(), SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
            }

            player.sendSystemMessage(Component.literal("§dYour Shiny Token has been activated!"));
        }
    }
}
