package net.minecraft.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.io.IOException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public class NettyPacketDecoder extends ByteToMessageDecoder {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Marker RECEIVED_PACKET_MARKER = MarkerManager.getMarker("PACKET_RECEIVED", NetworkManager.NETWORK_PACKETS_MARKER);
    private final EnumPacketDirection direction;

    public NettyPacketDecoder(EnumPacketDirection enumprotocoldirection) {
        this.direction = enumprotocoldirection;
    }

    protected void decode(ChannelHandlerContext channelhandlercontext, ByteBuf bytebuf, List<Object> list) throws Exception {
        if (bytebuf.readableBytes() != 0) {
            PacketBuffer packetdataserializer = new PacketBuffer(bytebuf);
            int i = packetdataserializer.readVarInt();
            Packet packet = ((EnumConnectionState) channelhandlercontext.channel().attr(NetworkManager.PROTOCOL_ATTRIBUTE_KEY).get()).getPacket(this.direction, i);

            if (packet == null) {
                throw new IOException("Bad packet id " + i);
            } else {
                packet.readPacketData(packetdataserializer);
                if (packetdataserializer.readableBytes() > 0) {
                    throw new IOException("Packet " + ((EnumConnectionState) channelhandlercontext.channel().attr(NetworkManager.PROTOCOL_ATTRIBUTE_KEY).get()).getId() + "/" + i + " (" + packet.getClass().getSimpleName() + ") was larger than I expected, found " + packetdataserializer.readableBytes() + " bytes extra whilst reading packet " + i);
                } else {
                    list.add(packet);
                    if (NettyPacketDecoder.LOGGER.isDebugEnabled()) {
                        NettyPacketDecoder.LOGGER.debug(NettyPacketDecoder.RECEIVED_PACKET_MARKER, " IN: [{}:{}] {}", channelhandlercontext.channel().attr(NetworkManager.PROTOCOL_ATTRIBUTE_KEY).get(), Integer.valueOf(i), packet.getClass().getName());
                    }

                }
            }
        }
    }
}
