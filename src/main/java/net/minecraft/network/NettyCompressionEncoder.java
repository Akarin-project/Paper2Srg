package net.minecraft.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import java.util.zip.Deflater;

public class NettyCompressionEncoder extends MessageToByteEncoder<ByteBuf> {

    private final byte[] field_179302_a = new byte[8192];
    private final Deflater field_179300_b;
    private int field_179301_c;

    public NettyCompressionEncoder(int i) {
        this.field_179301_c = i;
        this.field_179300_b = new Deflater();
    }

    @Override
    protected void encode(ChannelHandlerContext channelhandlercontext, ByteBuf bytebuf, ByteBuf bytebuf1) throws Exception {
        int i = bytebuf.readableBytes();
        PacketBuffer packetdataserializer = new PacketBuffer(bytebuf1);

        if (i < this.field_179301_c) {
            packetdataserializer.func_150787_b(0);
            packetdataserializer.writeBytes(bytebuf);
        } else {
            byte[] abyte = new byte[i];

            bytebuf.readBytes(abyte);
            packetdataserializer.func_150787_b(abyte.length);
            this.field_179300_b.setInput(abyte, 0, i);
            this.field_179300_b.finish();

            while (!this.field_179300_b.finished()) {
                int j = this.field_179300_b.deflate(this.field_179302_a);

                packetdataserializer.writeBytes(this.field_179302_a, 0, j);
            }

            this.field_179300_b.reset();
        }

    }

    public void func_179299_a(int i) {
        this.field_179301_c = i;
    }
}
