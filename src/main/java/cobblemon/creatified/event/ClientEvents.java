package cobblemon.creatified.event;

import cobblemon.creatified.block.ModBlocks;
import cobblemon.creatified.block.RepelBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.bus.api.SubscribeEvent;

public class ClientEvents {

    private final int REPEL_RADIUS = 48;

    @SubscribeEvent
    public void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;

        BlockPos playerPos = mc.player.blockPosition();

        for (BlockPos pos : BlockPos.betweenClosed(
                playerPos.offset(-REPEL_RADIUS, -5, -REPEL_RADIUS),
                playerPos.offset(REPEL_RADIUS, 5, REPEL_RADIUS))) {

            BlockState state = mc.level.getBlockState(pos);
            Block block = state.getBlock();

            if (block == ModBlocks.REPEL_BLOCK.get() && state.getValue(RepelBlock.SHOWING_RANGE)) {
                double centerX = pos.getX() + 0.5;
                double centerY = pos.getY() + 1.0;
                double centerZ = pos.getZ() + 0.5;

                for (double angle = 0; angle < 360; angle += 30) {
                    double rad = Math.toRadians(angle);
                    double x = centerX + REPEL_RADIUS * Math.cos(rad);
                    double z = centerZ + REPEL_RADIUS * Math.sin(rad);

                    mc.level.addParticle(ParticleTypes.END_ROD, x, centerY, z, 0, 0, 0);
                }
            }
        }
    }
}

