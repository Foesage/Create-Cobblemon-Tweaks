package cobblemon.creatified.event;

import cobblemon.creatified.block.ModBlocks;
import cobblemon.creatified.block.RepelBlock;
import cobblemon.creatified.tracker.ActiveRepelBlockTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.bus.api.SubscribeEvent;

public class ClientEvents {

    private static final int MAX_RANGE = 64;            // Max horizontal repel radius
    private static final int VERTICAL_RANGE = 64;       // Vertical repel height
    private static final int VERTICAL_STEP = 16;        // Ring spacing
    private static final int PARTICLES_PER_RING = 36;   // Particles per circle

    private int tickCounter = 0;

    @SubscribeEvent
    public void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;

        tickCounter++;
        if (tickCounter % 5 != 0) return;

        BlockPos playerPos = mc.player.blockPosition();

        BlockPos selectedPos = null;
        int effectiveRadius = MAX_RANGE;

        for (BlockPos pos : ActiveRepelBlockTracker.ACTIVE_BLOCKS) {
            BlockState state = mc.level.getBlockState(pos);
            Block block = state.getBlock();

            if (block == ModBlocks.REPEL_BLOCK.get() && state.getValue(RepelBlock.SHOWING_RANGE)) {
                selectedPos = pos;
                int redstonePower = mc.level.getBestNeighborSignal(pos);
                effectiveRadius = Math.max(1, (int) (MAX_RANGE * (1.0 - redstonePower / 15.0)));
                break;
            }
        }

        if (selectedPos != null) {
            double centerX = selectedPos.getX() + 0.5;
            double centerZ = selectedPos.getZ() + 0.5;

            // Vertical cylinder rings
            for (int yOffset = -VERTICAL_RANGE; yOffset <= VERTICAL_RANGE; yOffset += VERTICAL_STEP) {
                double y = selectedPos.getY() + 1.0 + yOffset;

                for (int i = 0; i < PARTICLES_PER_RING; i++) {
                    double angle = 2 * Math.PI * i / PARTICLES_PER_RING;
                    double x = centerX + effectiveRadius * Math.cos(angle);
                    double z = centerZ + effectiveRadius * Math.sin(angle);
                    mc.level.addAlwaysVisibleParticle(ParticleTypes.END_ROD, true, x, y, z, 0, 0, 0);
                }
            }

            // X shape at top and bottom
            drawXParticles(mc, centerX, selectedPos.getY() + 1.0 + VERTICAL_RANGE, centerZ, effectiveRadius);
            drawXParticles(mc, centerX, selectedPos.getY() + 1.0 - VERTICAL_RANGE, centerZ, effectiveRadius);
        }
    }

    private void drawXParticles(Minecraft mc, double centerX, double y, double centerZ, int radius) {
        int count = 13; // Fewer particles
        double maxOffset = radius * 0.5; // 50% range for X arms

        for (int i = 0; i <= count; i++) {
            double offset = maxOffset * (i / (double) count - 0.5) * 2;

            // NW to SE
            mc.level.addAlwaysVisibleParticle(ParticleTypes.END_ROD, true,
                    centerX + offset, y, centerZ + offset, 0, 0, 0);

            // NE to SW
            mc.level.addAlwaysVisibleParticle(ParticleTypes.END_ROD, true,
                    centerX + offset, y, centerZ - offset, 0, 0, 0);
        }
    }
}
