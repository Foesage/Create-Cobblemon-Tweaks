package cobblemon.creatified.network;

import cobblemon.creatified.CobblemonCreatified;
import cobblemon.creatified.network.packet.RepelRangeParticlesPacket;
import cobblemon.creatified.network.packet.RepelRangeParticlesPacketClientHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

public class ModNetwork {

    public static void register(RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar(CobblemonCreatified.MODID);

        registrar.playToClient(
                RepelRangeParticlesPacket.TYPE,
                StreamCodec.of(
                        (RegistryFriendlyByteBuf buf, RepelRangeParticlesPacket packet) -> {
                            buf.writeBlockPos(packet.pos());
                            buf.writeInt(packet.radius());
                        },
                        buf -> new RepelRangeParticlesPacket(buf.readBlockPos(), buf.readInt())
                ),
                (packet, context) -> {
                    if (FMLEnvironment.dist.isClient()) {
                        RepelRangeParticlesPacketClientHandler.handle(packet);
                    }
                }
        );
    }

    public static void sendTo(ServerPlayer player, RepelRangeParticlesPacket packet) {
        PacketDistributor.sendToPlayer(player, packet);
    }

    public static void sendToNearby(ServerPlayer origin, BlockPos pos, RepelRangeParticlesPacket packet, double radius) {
        if (origin.level() == null) return;

        origin.level().players().stream()
                .filter(p -> p != origin)
                .filter(p -> p.blockPosition().closerThan(pos, radius))
                .filter(p -> p instanceof ServerPlayer)
                .map(p -> (ServerPlayer) p)
                .forEach(p -> sendTo(p, packet));
    }

    /**
     * Starts a repeating task that sends the repel particle packet every 20 ticks (1 sec)
     * for up to 5 minutes (6000 ticks). Debug logging included.
     */
    public static void startParticleEffectTimer(ServerLevel level, BlockPos pos, int radius) {
        final MinecraftServer server = level.getServer();
        final int intervalTicks = 20;
        final int maxTicks = 6000;
        final int[] ticksElapsed = {0};

        Runnable[] task = new Runnable[1];

        task[0] = () -> {
            System.out.println("[RepelTimer] Tick: " + ticksElapsed[0]);

            if (ticksElapsed[0] % intervalTicks == 0) {
                for (ServerPlayer player : level.players()) {
                    if (player.blockPosition().closerThan(pos, radius)) {
                        System.out.println("[RepelTimer] Sending packet to: " + player.getName().getString());
                        sendTo(player, new RepelRangeParticlesPacket(pos, radius));
                    }
                }
            }

            ticksElapsed[0]++;
            if (ticksElapsed[0] <= maxTicks) {
                server.execute(task[0]);
            } else {
                System.out.println("[RepelTimer] Timer complete.");
            }
        };

        System.out.println("[RepelTimer] Starting particle timer at " + pos + " with radius " + radius);
        server.execute(task[0]);
    }
}
