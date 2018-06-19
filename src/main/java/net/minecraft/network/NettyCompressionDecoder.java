package net.minecraft.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.DecoderException;
import java.util.List;
import java.util.zip.Inflater;

public class NettyCompressionDecoder extends ByteToMessageDecoder {

    private final Inflater field_179305_a;
    private int field_179304_b;

    public NettyCompressionDecoder(int i) {
        this.field_179304_b = i;
        this.field_179305_a = new Inflater();
    }

    protected void decode(ChannelHandlerContext channelhandlercontext, ByteBuf bytebuf, List<Object> list) throws Exception {
        if (bytebuf.readableBytes() != 0) {
            PacketBuffer packetdataserializer = new PacketBuffer(bytebuf);
            int i = packetdataserializer.func_150792_a();

            if (i == 0) {
                list.add(packetdataserializer.readBytes(packetdataserializer.readableBytes()));
            } else {
                if (i < this.field_179304_b) {
                    throw new DecoderException("Badly compressed packet - size of " + i + " is below server threshold of " + this.field_179304_b);
                }

                if (i > 2097152) {
                    throw new DecoderException("Badly compressed packet - size of " + i + " is larger than protocol maximum of " + 2097152);
                }

                byte[] abyte = new byte[packetdataserializer.readableBytes()];

                packetdataserializer.readBytes(abyte);
                this.field_179305_a.setInput(abyte);
                byte[] abyte1 = new byte[i];

                this.field_179305_a.inflate(abyte1);
                list.add(Unpooled.wrappedBuffer(abyte1));
                this.field_179305_a.reset();
            }

        }
    }

    public void func_179303_a(int i) {
        this.field_179304_b = i;
    }
}
