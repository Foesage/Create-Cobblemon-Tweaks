package cobblemon.creatified.block.entity;

import cobblemon.creatified.block.RepelBlock;
import cobblemon.creatified.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashSet;
import java.util.Set;

public class RepelBlockEntity extends BlockEntity {

    public RepelBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.REPEL_BLOCK_ENTITY.get(), pos, state);
    }

    /** Called by the RepelBlock server-side ticker every tick. */
    public void tickServer() {
        if (!(level instanceof ServerLevel serverLevel)) return;

        BlockState state = getBlockState();
        if (!state.getValue(RepelBlock.POWERED)) return;

        int redstonePower = serverLevel.getBestNeighborSignal(worldPosition);
        int maxRadius = RepelBlock.BLOCKING_RADIUS;
        int effectiveRadius = Math.max(1, (int) (maxRadius * (1.0 - redstonePower / 15.0)));

        double centerX = worldPosition.getX() + 0.5;
        double centerY = worldPosition.getY() + 1.0;
        double centerZ = worldPosition.getZ() + 0.5;

        // ☁️ Cloudy aura to show block is active
        for (ServerPlayer player : serverLevel.players()) {
            if (player.distanceToSqr(centerX, centerY, centerZ) < 256 * 256) {
                serverLevel.sendParticles(
                        player,
                        ParticleTypes.CLOUD,
                        true,
                        centerX,
                        centerY + 0.25,
                        centerZ,
                        1,
                        0,
                        0.1,
                        0,
                        0.001
                );
            }
        }

        // 🌀 Show repel range if enabled
        if (!state.getValue(RepelBlock.SHOWING_RANGE)) return;

        // Disable other blocks' range indicators within 2x radius
        int disableRadius = effectiveRadius * 2;
        Set<BlockPos> toDisable = new HashSet<>();
        for (BlockPos pos : cobblemon.creatified.tracker.ActiveRepelBlockTracker.ACTIVE_BLOCKS) {
            if (!pos.equals(worldPosition) && pos.closerThan(worldPosition, disableRadius)) {
                BlockState otherState = serverLevel.getBlockState(pos);
                if (otherState.getBlock() instanceof RepelBlock && otherState.getValue(RepelBlock.SHOWING_RANGE)) {
                    serverLevel.setBlock(pos, otherState.setValue(RepelBlock.SHOWING_RANGE, false), 3);
                    toDisable.add(pos);
                }
            }
        }
        // Remove those from activeRangeBlock if necessary (optional cleanup)

        final int VERTICAL_RANGE = 64;
        final int VERTICAL_STEP = 16;
        final int PARTICLES_PER_RING = 36;

        // Draw stacked vertical rings
        for (int yOffset = -VERTICAL_RANGE; yOffset <= VERTICAL_RANGE; yOffset += VERTICAL_STEP) {
            double y = centerY + yOffset;

            for (int i = 0; i < PARTICLES_PER_RING; i++) {
                double angle = 2 * Math.PI * i / PARTICLES_PER_RING;
                double x = centerX + effectiveRadius * Math.cos(angle);
                double z = centerZ + effectiveRadius * Math.sin(angle);

                for (ServerPlayer player : serverLevel.players()) {
                    if (player.distanceToSqr(x, y, z) < 256 * 256) {
                        serverLevel.sendParticles(
                                player,
                                ParticleTypes.END_ROD,
                                true,
                                x, y, z,
                                1,
                                0, 0, 0,
                                0
                        );
                    }
                }
            }
        }

        drawXParticles(serverLevel, centerX, centerY + VERTICAL_RANGE, centerZ, effectiveRadius);
        drawXParticles(serverLevel, centerX, centerY - VERTICAL_RANGE, centerZ, effectiveRadius);
    }

    private void drawXParticles(ServerLevel serverLevel, double centerX, double y, double centerZ, int radius) {
        int count = 13;
        double maxOffset = radius * 0.5;

        for (int i = 0; i <= count; i++) {
            double offset = maxOffset * (i / (double) count - 0.5) * 2;

            double[] xOffsets = { offset, offset };
            double[] zOffsets = { offset, -offset };

            for (int j = 0; j < 2; j++) {
                double x = centerX + xOffsets[j];
                double z = centerZ + zOffsets[j];

                for (ServerPlayer player : serverLevel.players()) {
                    if (player.distanceToSqr(x, y, z) < 256 * 256) {
                        serverLevel.sendParticles(
                                player,
                                ParticleTypes.END_ROD,
                                true,
                                x, y, z,
                                1,
                                0, 0, 0,
                                0
                        );
                    }
                }
            }
        }
    }
}
