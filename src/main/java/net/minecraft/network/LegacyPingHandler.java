package net.minecraft.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.destroystokyo.paper.network.PaperLegacyStatusClient;
import net.minecraft.server.MinecraftServer;

public class LegacyPingHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LogManager.getLogger();
    private final NetworkSystem networkSystem;
    private ByteBuf buf; // Paper

    public LegacyPingHandler(NetworkSystem serverconnection) {
        this.networkSystem = serverconnection;
    }

    public void channelRead(ChannelHandlerContext channelhandlercontext, Object object) throws Exception {
        ByteBuf bytebuf = (ByteBuf) object;
        // Paper start - Make legacy ping handler more reliable
        if (this.buf != null) {
            try {
                readLegacy1_6(channelhandlercontext, bytebuf);
            } finally {
                bytebuf.release();
            }
            return;
        }
        // Paper end
        bytebuf.markReaderIndex();
        boolean flag = true;

        try {
            if (bytebuf.readUnsignedByte() == 254) {
                InetSocketAddress inetsocketaddress = (InetSocketAddress) channelhandlercontext.channel().remoteAddress();
                MinecraftServer minecraftserver = this.networkSystem.getServer();
                int i = bytebuf.readableBytes();
                String s;
                com.destroystokyo.paper.event.server.PaperServerListPingEvent event; // Paper

                switch (i) {
                case 0:
                    LegacyPingHandler.LOGGER.debug("Ping: (<1.3.x) from {}:{}", inetsocketaddress.getAddress(), Integer.valueOf(inetsocketaddress.getPort()));
                    // Paper start - Call PaperServerListPingEvent and use results
                    event = PaperLegacyStatusClient.processRequest(minecraftserver, inetsocketaddress, 39, null);
                    if (event == null) {
                        channelhandlercontext.close();
                        break;
                    }
                    s = String.format("%s\u00a7%d\u00a7%d", PaperLegacyStatusClient.getUnformattedMotd(event), event.getNumPlayers(), event.getMaxPlayers());
                    // Paper end
                    this.writeAndFlush(channelhandlercontext, this.getStringBuffer(s));
                    break;

                case 1:
                    if (bytebuf.readUnsignedByte() != 1) {
                        return;
                    }

                    LegacyPingHandler.LOGGER.debug("Ping: (1.4-1.5.x) from {}:{}", inetsocketaddress.getAddress(), Integer.valueOf(inetsocketaddress.getPort()));
                    // Paper start - Call PaperServerListPingEvent and use results
                    event = PaperLegacyStatusClient.processRequest(minecraftserver, inetsocketaddress, 61, null);
                    if (event == null) {
                        channelhandlercontext.close();
                        break;
                    }
                    s = String.format("\u00a71\u0000%d\u0000%s\u0000%s\u0000%d\u0000%d", event.getProtocolVersion(), event.getVersion(),
                            PaperLegacyStatusClient.getMotd(event), event.getNumPlayers(), event.getMaxPlayers());
                    // Paper end
                    this.writeAndFlush(channelhandlercontext, this.getStringBuffer(s));
                    break;

                default:
                    // Paper start - Replace with improved version below
                    if (bytebuf.readUnsignedByte() != 0x01 || bytebuf.readUnsignedByte() != 0xFA) return;
                    readLegacy1_6(channelhandlercontext, bytebuf);
                    /*
                    boolean flag1 = bytebuf.readUnsignedByte() == 1;

                    flag1 &= bytebuf.readUnsignedByte() == 250;
                    flag1 &= "MC|PingHost".equals(new String(bytebuf.readBytes(bytebuf.readShort() * 2).array(), StandardCharsets.UTF_16BE));
                    int j = bytebuf.readUnsignedShort();

                    flag1 &= bytebuf.readUnsignedByte() >= 73;
                    flag1 &= 3 + bytebuf.readBytes(bytebuf.readShort() * 2).array().length + 4 == j;
                    flag1 &= bytebuf.readInt() <= '\uffff';
                    flag1 &= bytebuf.readableBytes() == 0;
                    if (!flag1) {
                        return;
                    }

                    LegacyPingHandler.a.debug("Ping: (1.6) from {}:{}", inetsocketaddress.getAddress(), Integer.valueOf(inetsocketaddress.getPort()));
                    String s1 = String.format("\u00a71\u0000%d\u0000%s\u0000%s\u0000%d\u0000%d", new Object[] { Integer.valueOf(127), minecraftserver.getVersion(), minecraftserver.getMotd(), Integer.valueOf(minecraftserver.H()), Integer.valueOf(minecraftserver.I())});
                    ByteBuf bytebuf1 = this.a(s1);

                    try {
                        this.a(channelhandlercontext, bytebuf1);
                    } finally {
                        bytebuf1.release();
                    }
                    */
                    // Paper end
                }

                bytebuf.release();
                flag = false;
                return;
            }
        } catch (RuntimeException runtimeexception) {
            return;
        } finally {
            if (flag) {
                bytebuf.resetReaderIndex();
                channelhandlercontext.channel().pipeline().remove("legacy_query");
                channelhandlercontext.fireChannelRead(object);
            }

        }

    }

    // Paper start
    private void readLegacy1_6(ChannelHandlerContext ctx, ByteBuf part) {
        ByteBuf buf = this.buf;

        if (buf == null) {
            this.buf = buf = ctx.alloc().buffer();
            buf.markReaderIndex();
        } else {
            buf.resetReaderIndex();
        }

        buf.writeBytes(part);

        // Short + Short + Byte + Short + Int
        if (!buf.isReadable(2 + 2 + 1 + 2 + 4)) {
            return;
        }

        short length = buf.readShort();
        if (!buf.isReadable(length * 2)) {
            return;
        }

        if (!buf.readBytes(length * 2).toString(StandardCharsets.UTF_16BE).equals("MC|PingHost")) {
            removeHandler(ctx);
            return;
        }

        if (!buf.isReadable(2)) {
            return;
        }

        length = buf.readShort();
        if (!buf.isReadable(length)) {
            return;
        }

        MinecraftServer server = this.networkSystem.getServer();
        int protocolVersion = buf.readByte();
        length = buf.readShort();
        String host = buf.readBytes(length * 2).toString(StandardCharsets.UTF_16BE);
        int port = buf.readInt();

        if (buf.isReadable()) {
            removeHandler(ctx);
            return;
        }

        buf.release();
        this.buf = null;

        LOGGER.debug("Ping: (1.6) from {}", ctx.channel().remoteAddress());

        InetSocketAddress virtualHost = com.destroystokyo.paper.network.PaperNetworkClient.prepareVirtualHost(host, port);
        com.destroystokyo.paper.event.server.PaperServerListPingEvent event = PaperLegacyStatusClient.processRequest(
                server, (InetSocketAddress) ctx.channel().remoteAddress(), protocolVersion, virtualHost);
        if (event == null) {
            ctx.close();
            return;
        }

        String response = String.format("\u00a71\u0000%d\u0000%s\u0000%s\u0000%d\u0000%d", event.getProtocolVersion(), event.getVersion(),
                PaperLegacyStatusClient.getMotd(event), event.getNumPlayers(), event.getMaxPlayers());
        this.writeAndFlush(ctx, this.getStringBuffer(response));
    }

    private void removeHandler(ChannelHandlerContext ctx) {
        ByteBuf buf = this.buf;
        this.buf = null;

        buf.resetReaderIndex();
        ctx.pipeline().remove("legacy_query");
        ctx.fireChannelRead(buf);
    }
    // Paper end

    private void writeAndFlush(ChannelHandlerContext channelhandlercontext, ByteBuf bytebuf) {
        channelhandlercontext.pipeline().firstContext().writeAndFlush(bytebuf).addListener(ChannelFutureListener.CLOSE);
    }

    private ByteBuf getStringBuffer(String s) {
        ByteBuf bytebuf = Unpooled.buffer();

        bytebuf.writeByte(255);
        char[] achar = s.toCharArray();

        bytebuf.writeShort(achar.length);
        char[] achar1 = achar;
        int i = achar.length;

        for (int j = 0; j < i; ++j) {
            char c0 = achar1[j];

            bytebuf.writeChar(c0);
        }

        return bytebuf;
    }
}
