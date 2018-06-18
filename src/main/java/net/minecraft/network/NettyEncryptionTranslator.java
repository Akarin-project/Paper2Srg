package net.minecraft.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import javax.crypto.Cipher;
import javax.crypto.ShortBufferException;

public class NettyEncryptionTranslator {

    private final Cipher cipher;
    private byte[] inputBuffer = new byte[0];
    private byte[] outputBuffer = new byte[0];

    protected NettyEncryptionTranslator(Cipher cipher) {
        this.cipher = cipher;
    }

    private byte[] bufToBytes(ByteBuf bytebuf) {
        int i = bytebuf.readableBytes();

        if (this.inputBuffer.length < i) {
            this.inputBuffer = new byte[i];
        }

        bytebuf.readBytes(this.inputBuffer, 0, i);
        return this.inputBuffer;
    }

    protected ByteBuf decipher(ChannelHandlerContext channelhandlercontext, ByteBuf bytebuf) throws ShortBufferException {
        int i = bytebuf.readableBytes();
        byte[] abyte = this.bufToBytes(bytebuf);
        ByteBuf bytebuf1 = channelhandlercontext.alloc().heapBuffer(this.cipher.getOutputSize(i));

        bytebuf1.writerIndex(this.cipher.update(abyte, 0, i, bytebuf1.array(), bytebuf1.arrayOffset()));
        return bytebuf1;
    }

    protected void cipher(ByteBuf bytebuf, ByteBuf bytebuf1) throws ShortBufferException {
        int i = bytebuf.readableBytes();
        byte[] abyte = this.bufToBytes(bytebuf);
        int j = this.cipher.getOutputSize(i);

        if (this.outputBuffer.length < j) {
            this.outputBuffer = new byte[j];
        }

        bytebuf1.writeBytes(this.outputBuffer, 0, this.cipher.update(abyte, 0, i, this.outputBuffer));
    }
}
