package net.minecraft.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import java.util.zip.Deflater;

public class NettyCompressionEncoder extends MessageToByteEncoder<ByteBuf> {

    private final byte[] buffer = new byte[8192];
    private final Deflater deflater;
    private int threshold;

    public NettyCompressionEncoder(int i) {
        this.threshold = i;
        this.deflater = new Deflater();
    }

    protected void encode(ChannelHandlerContext channelhandlercontext, ByteBuf bytebuf, ByteBuf bytebuf1) throws Exception {
        int i = bytebuf.readableBytes();
        PacketBuffer packetdataserializer = new PacketBuffer(bytebuf1);

        if (i < this.threshold) {
            packetdataserializer.writeVarInt(0);
            packetdataserializer.writeBytes(bytebuf);
        } else {
            byte[] abyte = new byte[i];

            bytebuf.readBytes(abyte);
            packetdataserializer.writeVarInt(abyte.length);
            this.deflater.setInput(abyte, 0, i);
            this.deflater.finish();

            while (!this.deflater.finished()) {
                int j = this.deflater.deflate(this.buffer);

                packetdataserializer.writeBytes(this.buffer, 0, j);
            }

            this.deflater.reset();
        }

    }

    public void setCompressionThreshold(int i) {
        this.threshold = i;
    }

    protected void encode(ChannelHandlerContext channelhandlercontext, Object object, ByteBuf bytebuf) throws Exception {
        this.encode(channelhandlercontext, (ByteBuf) object, bytebuf);
    }
}
