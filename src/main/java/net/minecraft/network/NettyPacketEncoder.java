package net.minecraft.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public class NettyPacketEncoder extends MessageToByteEncoder<Packet<?>> {

    private static final Logger field_150798_a = LogManager.getLogger();
    private static final Marker field_150797_b = MarkerManager.getMarker("PACKET_SENT", NetworkManager.field_150738_b);
    private final EnumPacketDirection field_152500_c;

    public NettyPacketEncoder(EnumPacketDirection enumprotocoldirection) {
        this.field_152500_c = enumprotocoldirection;
    }

    @Override
    protected void encode(ChannelHandlerContext channelhandlercontext, Packet<?> packet, ByteBuf bytebuf) throws Exception {
        EnumConnectionState enumprotocol = channelhandlercontext.channel().attr(NetworkManager.field_150739_c).get();

        if (enumprotocol == null) {
            throw new RuntimeException("ConnectionProtocol unknown: " + packet.toString());
        } else {
            Integer integer = enumprotocol.func_179246_a(this.field_152500_c, packet);

            if (NettyPacketEncoder.field_150798_a.isDebugEnabled()) {
                NettyPacketEncoder.field_150798_a.debug(NettyPacketEncoder.field_150797_b, "OUT: [{}:{}] {}", channelhandlercontext.channel().attr(NetworkManager.field_150739_c).get(), integer, packet.getClass().getName());
            }

            if (integer == null) {
                throw new IOException("Can\'t serialize unregistered packet");
            } else {
                PacketBuffer packetdataserializer = new PacketBuffer(bytebuf);

                packetdataserializer.func_150787_b(integer.intValue());

                try {
                    packet.func_148840_b(packetdataserializer);
                } catch (Throwable throwable) {
                    NettyPacketEncoder.field_150798_a.error(throwable);
                }

            }
        }
    }
}
