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

    private static final Logger field_150800_a = LogManager.getLogger();
    private static final Marker field_150799_b = MarkerManager.getMarker("PACKET_RECEIVED", NetworkManager.field_150738_b);
    private final EnumPacketDirection field_152499_c;

    public NettyPacketDecoder(EnumPacketDirection enumprotocoldirection) {
        this.field_152499_c = enumprotocoldirection;
    }

    protected void decode(ChannelHandlerContext channelhandlercontext, ByteBuf bytebuf, List<Object> list) throws Exception {
        if (bytebuf.readableBytes() != 0) {
            PacketBuffer packetdataserializer = new PacketBuffer(bytebuf);
            int i = packetdataserializer.func_150792_a();
            Packet packet = ((EnumConnectionState) channelhandlercontext.channel().attr(NetworkManager.field_150739_c).get()).func_179244_a(this.field_152499_c, i);

            if (packet == null) {
                throw new IOException("Bad packet id " + i);
            } else {
                packet.func_148837_a(packetdataserializer);
                if (packetdataserializer.readableBytes() > 0) {
                    throw new IOException("Packet " + ((EnumConnectionState) channelhandlercontext.channel().attr(NetworkManager.field_150739_c).get()).func_150759_c() + "/" + i + " (" + packet.getClass().getSimpleName() + ") was larger than I expected, found " + packetdataserializer.readableBytes() + " bytes extra whilst reading packet " + i);
                } else {
                    list.add(packet);
                    if (NettyPacketDecoder.field_150800_a.isDebugEnabled()) {
                        NettyPacketDecoder.field_150800_a.debug(NettyPacketDecoder.field_150799_b, " IN: [{}:{}] {}", channelhandlercontext.channel().attr(NetworkManager.field_150739_c).get(), Integer.valueOf(i), packet.getClass().getName());
                    }

                }
            }
        }
    }
}
