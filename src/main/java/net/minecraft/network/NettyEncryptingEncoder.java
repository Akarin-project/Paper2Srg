package net.minecraft.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import javax.crypto.Cipher;

public class NettyEncryptingEncoder extends MessageToByteEncoder<ByteBuf> {

    private final NettyEncryptionTranslator field_150750_a;

    public NettyEncryptingEncoder(Cipher cipher) {
        this.field_150750_a = new NettyEncryptionTranslator(cipher);
    }

    protected void encode(ChannelHandlerContext channelhandlercontext, ByteBuf bytebuf, ByteBuf bytebuf1) throws Exception {
        this.field_150750_a.func_150504_a(bytebuf, bytebuf1);
    }

    protected void encode(ChannelHandlerContext channelhandlercontext, Object object, ByteBuf bytebuf) throws Exception {
        this.encode(channelhandlercontext, (ByteBuf) object, bytebuf);
    }
}
