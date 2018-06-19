package net.minecraft.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.handler.codec.MessageToByteEncoder;

@Sharable
public class NettyVarint21FrameEncoder extends MessageToByteEncoder<ByteBuf> {

    public NettyVarint21FrameEncoder() {}

    @Override
    protected void encode(ChannelHandlerContext channelhandlercontext, ByteBuf bytebuf, ByteBuf bytebuf1) throws Exception {
        int i = bytebuf.readableBytes();
        int j = PacketBuffer.getVarIntSize(i);

        if (j > 3) {
            throw new IllegalArgumentException("unable to fit " + i + " into " + 3);
        } else {
            PacketBuffer packetdataserializer = new PacketBuffer(bytebuf1);

            packetdataserializer.ensureWritable(j + i);
            packetdataserializer.writeVarInt(i);
            packetdataserializer.writeBytes(bytebuf, bytebuf.readerIndex(), i);
        }
    }
}
