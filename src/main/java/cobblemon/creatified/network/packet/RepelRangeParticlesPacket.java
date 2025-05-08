package cobblemon.creatified.network.packet;

import cobblemon.creatified.CobblemonCreatified;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record RepelRangeParticlesPacket(BlockPos pos, int radius) implements CustomPacketPayload {

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(CobblemonCreatified.MODID, "repel_range_particles");
    public static final Type<RepelRangeParticlesPacket> TYPE = new Type<>(ID);

    @Override
    public Type<RepelRangeParticlesPacket> type() {
        return TYPE;
    }

    public static RepelRangeParticlesPacket decode(RegistryFriendlyByteBuf buf) {
        return new RepelRangeParticlesPacket(buf.readBlockPos(), buf.readInt());
    }

    public static RegistryFriendlyByteBuf encode(RegistryFriendlyByteBuf buf, RepelRangeParticlesPacket packet) {
        buf.writeBlockPos(packet.pos());
        buf.writeInt(packet.radius());
        return buf;
    }
}
