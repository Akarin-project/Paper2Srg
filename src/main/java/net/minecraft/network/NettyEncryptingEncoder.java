package net.minecraft.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import javax.crypto.Cipher;

public class NettyEncryptingEncoder extends MessageToByteEncoder<ByteBuf> {

    private final NettyEncryptionTranslator encryptionCodec;

    public NettyEncryptingEncoder(Cipher cipher) {
        this.encryptionCodec = new NettyEncryptionTranslator(cipher);
    }

    protected void encode(ChannelHandlerContext channelhandlercontext, ByteBuf bytebuf, ByteBuf bytebuf1) throws Exception {
        this.encryptionCodec.cipher(bytebuf, bytebuf1);
    }

    protected void encode(ChannelHandlerContext channelhandlercontext, Object object, ByteBuf bytebuf) throws Exception {
        this.encode(channelhandlercontext, (ByteBuf) object, bytebuf);
    }
}
