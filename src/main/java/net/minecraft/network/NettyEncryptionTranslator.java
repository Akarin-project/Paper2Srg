package net.minecraft.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import javax.crypto.Cipher;
import javax.crypto.ShortBufferException;

public class NettyEncryptionTranslator {

    private final Cipher field_150507_a;
    private byte[] field_150505_b = new byte[0];
    private byte[] field_150506_c = new byte[0];

    protected NettyEncryptionTranslator(Cipher cipher) {
        this.field_150507_a = cipher;
    }

    private byte[] func_150502_a(ByteBuf bytebuf) {
        int i = bytebuf.readableBytes();

        if (this.field_150505_b.length < i) {
            this.field_150505_b = new byte[i];
        }

        bytebuf.readBytes(this.field_150505_b, 0, i);
        return this.field_150505_b;
    }

    protected ByteBuf func_150503_a(ChannelHandlerContext channelhandlercontext, ByteBuf bytebuf) throws ShortBufferException {
        int i = bytebuf.readableBytes();
        byte[] abyte = this.func_150502_a(bytebuf);
        ByteBuf bytebuf1 = channelhandlercontext.alloc().heapBuffer(this.field_150507_a.getOutputSize(i));

        bytebuf1.writerIndex(this.field_150507_a.update(abyte, 0, i, bytebuf1.array(), bytebuf1.arrayOffset()));
        return bytebuf1;
    }

    protected void func_150504_a(ByteBuf bytebuf, ByteBuf bytebuf1) throws ShortBufferException {
        int i = bytebuf.readableBytes();
        byte[] abyte = this.func_150502_a(bytebuf);
        int j = this.field_150507_a.getOutputSize(i);

        if (this.field_150506_c.length < j) {
            this.field_150506_c = new byte[j];
        }

        bytebuf1.writeBytes(this.field_150506_c, 0, this.field_150507_a.update(abyte, 0, i, this.field_150506_c));
    }
}
