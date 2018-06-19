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

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Marker RECEIVED_PACKET_MARKER = MarkerManager.getMarker("PACKET_SENT", NetworkManager.NETWORK_PACKETS_MARKER);
    private final EnumPacketDirection direction;

    public NettyPacketEncoder(EnumPacketDirection enumprotocoldirection) {
        this.direction = enumprotocoldirection;
    }

    @Override
    protected void encode(ChannelHandlerContext channelhandlercontext, Packet<?> packet, ByteBuf bytebuf) throws Exception {
        EnumConnectionState enumprotocol = channelhandlercontext.channel().attr(NetworkManager.PROTOCOL_ATTRIBUTE_KEY).get();

        if (enumprotocol == null) {
            throw new RuntimeException("ConnectionProtocol unknown: " + packet.toString());
        } else {
            Integer integer = enumprotocol.getPacketId(this.direction, packet);

            if (NettyPacketEncoder.LOGGER.isDebugEnabled()) {
                NettyPacketEncoder.LOGGER.debug(NettyPacketEncoder.RECEIVED_PACKET_MARKER, "OUT: [{}:{}] {}", channelhandlercontext.channel().attr(NetworkManager.PROTOCOL_ATTRIBUTE_KEY).get(), integer, packet.getClass().getName());
            }

            if (integer == null) {
                throw new IOException("Can\'t serialize unregistered packet");
            } else {
                PacketBuffer packetdataserializer = new PacketBuffer(bytebuf);

                packetdataserializer.writeVarInt(integer.intValue());

                try {
                    packet.writePacketData(packetdataserializer);
                } catch (Throwable throwable) {
                    NettyPacketEncoder.LOGGER.error(throwable);
                }

            }
        }
    }
}
