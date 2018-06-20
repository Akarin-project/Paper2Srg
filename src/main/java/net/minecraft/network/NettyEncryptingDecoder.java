package net.minecraft.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import java.util.List;
import javax.crypto.Cipher;

public class NettyEncryptingDecoder extends MessageToMessageDecoder<ByteBuf> {

    private final NettyEncryptionTranslator field_150509_a;

    public NettyEncryptingDecoder(Cipher cipher) {
        this.field_150509_a = new NettyEncryptionTranslator(cipher);
    }

    @Override
    protected void decode(ChannelHandlerContext channelhandlercontext, ByteBuf bytebuf, List<Object> list) throws Exception {
        list.add(this.field_150509_a.func_150503_a(channelhandlercontext, bytebuf));
    }
}
