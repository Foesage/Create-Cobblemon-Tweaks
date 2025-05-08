package cobblemon.creatified.network.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RepelRangeParticlesPacketClientHandler {

    public static void handle(RepelRangeParticlesPacket packet) {
        System.out.println("[Client] Received particle packet at " + packet.pos() + ", radius: " + packet.radius());

        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            System.out.println("[Client] WARNING: level is null!");
            return;
        }

        BlockPos pos = packet.pos();
        int radius = packet.radius();

        double centerX = pos.getX() + 0.5;
        double centerZ = pos.getZ() + 0.5;
        double baseY = pos.getY() + 1.0;

        int verticalRange = 64;
        int verticalStep = 16;
        int particlesPerRing = 36;

        for (int yOffset = -verticalRange; yOffset <= verticalRange; yOffset += verticalStep) {
            double y = baseY + yOffset;
            for (int i = 0; i < particlesPerRing; i++) {
                double angle = 2 * Math.PI * i / particlesPerRing;
                double x = centerX + radius * Math.cos(angle);
                double z = centerZ + radius * Math.sin(angle);
                level.addAlwaysVisibleParticle(ParticleTypes.END_ROD, true, x, y, z, 0, 0, 0);
            }
        }

        drawXParticles(level, centerX, baseY + verticalRange, centerZ, radius);
        drawXParticles(level, centerX, baseY - verticalRange, centerZ, radius);
    }

    private static void drawXParticles(ClientLevel level, double centerX, double y, double centerZ, int radius) {
        int count = 13;
        double maxOffset = radius * 0.5;

        for (int i = 0; i <= count; i++) {
            double offset = maxOffset * (i / (double) count - 0.5) * 2;
            level.addAlwaysVisibleParticle(ParticleTypes.END_ROD, true, centerX + offset, y, centerZ + offset, 0, 0, 0);
            level.addAlwaysVisibleParticle(ParticleTypes.END_ROD, true, centerX + offset, y, centerZ - offset, 0, 0, 0);
        }
    }
}
