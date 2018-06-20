package net.minecraft.network;

import com.google.common.collect.Queues;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.local.LocalChannel;
import io.netty.channel.local.LocalEventLoopGroup;
import io.netty.channel.local.LocalServerChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.handler.timeout.TimeoutException;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import java.net.SocketAddress;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.annotation.Nullable;
import javax.crypto.SecretKey;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import net.minecraft.network.play.server.SPacketChunkData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.CryptManager;
import net.minecraft.util.ITickable;
import net.minecraft.util.LazyLoadBase;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class NetworkManager extends SimpleChannelInboundHandler<Packet<?>> {

    private static final Logger field_150735_g = LogManager.getLogger();
    public static final Marker field_150740_a = MarkerManager.getMarker("NETWORK");
    public static final Marker field_150738_b = MarkerManager.getMarker("NETWORK_PACKETS", NetworkManager.field_150740_a);
    public static final AttributeKey<EnumConnectionState> field_150739_c = AttributeKey.valueOf("protocol");
    public static final LazyLoadBase<NioEventLoopGroup> field_179295_d = new LazyLoadBase() {
        protected NioEventLoopGroup a() {
            return new NioEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Netty Client IO #%d").setDaemon(true).build());
        }

        @Override
        protected Object func_179280_b() {
            return this.a();
        }
    };
    public static final LazyLoadBase<EpollEventLoopGroup> field_181125_e = new LazyLoadBase() {
        protected EpollEventLoopGroup a() {
            return new EpollEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Netty Epoll Client IO #%d").setDaemon(true).build());
        }

        @Override
        protected Object func_179280_b() {
            return this.a();
        }
    };
    public static final LazyLoadBase<LocalEventLoopGroup> field_179296_e = new LazyLoadBase() {
        protected LocalEventLoopGroup a() {
            return new LocalEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Netty Local Client IO #%d").setDaemon(true).build());
        }

        @Override
        protected Object func_179280_b() {
            return this.a();
        }
    };
    private final EnumPacketDirection field_179294_g;
    private final Queue<NetworkManager.InboundHandlerTuplePacketListener> field_150745_j = Queues.newConcurrentLinkedQueue(); private final Queue<NetworkManager.InboundHandlerTuplePacketListener> getPacketQueue() { return this.field_150745_j; } // Paper - Anti-Xray - OBFHELPER
    private final ReentrantReadWriteLock field_181680_j = new ReentrantReadWriteLock();
    public Channel field_150746_k;
    // Spigot Start // PAIL
    public SocketAddress field_150743_l;
    public java.util.UUID spoofedUUID;
    public com.mojang.authlib.properties.Property[] spoofedProfile;
    public boolean preparing = true;
    // Spigot End
    private INetHandler field_150744_m;
    private ITextComponent field_150742_o;
    private boolean field_152463_r;
    private boolean field_179297_n;
    // Paper start - NetworkClient implementation
    public int protocolVersion;
    public java.net.InetSocketAddress virtualHost;
    private static boolean enableExplicitFlush = Boolean.getBoolean("paper.explicit-flush");
    // Paper end

    public NetworkManager(EnumPacketDirection enumprotocoldirection) {
        this.field_179294_g = enumprotocoldirection;
    }

    @Override
    public void channelActive(ChannelHandlerContext channelhandlercontext) throws Exception {
        super.channelActive(channelhandlercontext);
        this.field_150746_k = channelhandlercontext.channel();
        this.field_150743_l = this.field_150746_k.remoteAddress();
        // Spigot Start
        this.preparing = false;
        // Spigot End

        try {
            this.func_150723_a(EnumConnectionState.HANDSHAKING);
        } catch (Throwable throwable) {
            NetworkManager.field_150735_g.fatal(throwable);
        }

    }

    public void func_150723_a(EnumConnectionState enumprotocol) {
        this.field_150746_k.attr(NetworkManager.field_150739_c).set(enumprotocol);
        this.field_150746_k.config().setAutoRead(true);
        NetworkManager.field_150735_g.debug("Enabled auto read");
    }

    @Override
    public void channelInactive(ChannelHandlerContext channelhandlercontext) throws Exception {
        this.func_150718_a(new TextComponentTranslation("disconnect.endOfStream", new Object[0]));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelhandlercontext, Throwable throwable) throws Exception {
        TextComponentTranslation chatmessage;

        if (throwable instanceof TimeoutException) {
            chatmessage = new TextComponentTranslation("disconnect.timeout", new Object[0]);
        } else {
            chatmessage = new TextComponentTranslation("disconnect.genericReason", new Object[] { "Internal Exception: " + throwable});
        }

        NetworkManager.field_150735_g.debug(chatmessage.func_150260_c(), throwable);
        this.func_150718_a(chatmessage);
        if (MinecraftServer.getServer().func_71239_B()) throwable.printStackTrace(); // Spigot
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelhandlercontext, Packet<?> packet) throws Exception {
        if (this.field_150746_k.isOpen()) {
            try {
                ((Packet) packet).func_148833_a(this.field_150744_m); // CraftBukkit - decompile error
            } catch (ThreadQuickExitException cancelledpackethandleexception) {
                ;
            }
        }

    }

    public void func_150719_a(INetHandler packetlistener) {
        Validate.notNull(packetlistener, "packetListener", new Object[0]);
        NetworkManager.field_150735_g.debug("Set listener of {} to {}", this, packetlistener);
        this.field_150744_m = packetlistener;
    }

    public void func_179290_a(Packet<?> packet) {
        if (this.func_150724_d() && this.trySendQueue() && !(packet instanceof SPacketChunkData && !((SPacketChunkData) packet).isReady())) { // Paper - Async-Anti-Xray - Add chunk packets which are not ready or all packets if the queue contains chunk packets which are not ready to the queue and send the packets later in the right order
            //this.m(); // Paper - Async-Anti-Xray - Move to if statement (this.trySendQueue())
            this.func_150732_b(packet, (GenericFutureListener[]) null);
        } else {
            this.field_181680_j.writeLock().lock();

            try {
                this.field_150745_j.add(new NetworkManager.InboundHandlerTuplePacketListener(packet, new GenericFutureListener[0]));
            } finally {
                this.field_181680_j.writeLock().unlock();
            }
        }

    }

    public void func_179288_a(Packet<?> packet, GenericFutureListener<? extends Future<? super Void>> genericfuturelistener, GenericFutureListener<? extends Future<? super Void>>... agenericfuturelistener) {
        if (this.func_150724_d() && this.trySendQueue() && !(packet instanceof SPacketChunkData && !((SPacketChunkData) packet).isReady())) { // Paper - Async-Anti-Xray - Add chunk packets which are not ready or all packets if the queue contains chunk packets which are not ready to the queue and send the packets later in the right order
            //this.m(); // Paper - Async-Anti-Xray - Move to if statement (this.trySendQueue())
            this.func_150732_b(packet, ArrayUtils.add(agenericfuturelistener, 0, genericfuturelistener));
        } else {
            this.field_181680_j.writeLock().lock();

            try {
                this.field_150745_j.add(new NetworkManager.InboundHandlerTuplePacketListener(packet, ArrayUtils.add(agenericfuturelistener, 0, genericfuturelistener)));
            } finally {
                this.field_181680_j.writeLock().unlock();
            }
        }

    }

    private void dispatchPacket(final Packet<?> packet, @Nullable final GenericFutureListener<? extends Future<? super Void>>[] genericFutureListeners) { this.func_150732_b(packet, genericFutureListeners); } // Paper - Anti-Xray - OBFHELPER
    private void func_150732_b(final Packet<?> packet, @Nullable final GenericFutureListener<? extends Future<? super Void>>[] agenericfuturelistener) {
        final EnumConnectionState enumprotocol = EnumConnectionState.func_150752_a(packet);
        final EnumConnectionState enumprotocol1 = this.field_150746_k.attr(NetworkManager.field_150739_c).get();

        if (enumprotocol1 != enumprotocol) {
            NetworkManager.field_150735_g.debug("Disabled auto read");
            this.field_150746_k.config().setAutoRead(false);
        }

        if (this.field_150746_k.eventLoop().inEventLoop()) {
            if (enumprotocol != enumprotocol1) {
                this.func_150723_a(enumprotocol);
            }

            ChannelFuture channelfuture = this.field_150746_k.writeAndFlush(packet);

            if (agenericfuturelistener != null) {
                channelfuture.addListeners(agenericfuturelistener);
            }

            channelfuture.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
        } else {
            this.field_150746_k.eventLoop().execute(new Runnable() {
                @Override
                public void run() {
                    if (enumprotocol != enumprotocol1) {
                        NetworkManager.this.func_150723_a(enumprotocol);
                    }

                    ChannelFuture channelfuture = NetworkManager.this.field_150746_k.writeAndFlush(packet);

                    if (agenericfuturelistener != null) {
                        channelfuture.addListeners(agenericfuturelistener);
                    }

                    channelfuture.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
                }
            });
        }

    }

    // Paper start - Async-Anti-Xray - Stop dispatching further packets and return false if the peeked packet is a chunk packet which is not ready
    private boolean trySendQueue() { return this.m(); } // OBFHELPER
    private boolean m() { // void -> boolean
        if (this.field_150746_k != null && this.field_150746_k.isOpen()) {
            if (this.field_150745_j.isEmpty()) { // return if the packet queue is empty so that the write lock by Anti-Xray doesn't affect the vanilla performance at all
                return true;
            }

            this.field_181680_j.writeLock().lock(); // readLock -> writeLock (because of race condition between peek and poll)

            try {
                while (!this.field_150745_j.isEmpty()) {
                    NetworkManager.InboundHandlerTuplePacketListener networkmanager_queuedpacket = this.getPacketQueue().peek(); // poll -> peek

                    if (networkmanager_queuedpacket != null) { // Fix NPE (Spigot bug caused by handleDisconnection())
                        if (networkmanager_queuedpacket.getPacket() instanceof SPacketChunkData && !((SPacketChunkData) networkmanager_queuedpacket.getPacket()).isReady()) { // Check if the peeked packet is a chunk packet which is not ready
                            return false; // Return false if the peeked packet is a chunk packet which is not ready
                        } else {
                            this.getPacketQueue().poll(); // poll here
                            this.dispatchPacket(networkmanager_queuedpacket.getPacket(), networkmanager_queuedpacket.getGenericFutureListeners()); // dispatch the packet
                        }
                    }
                }
            } finally {
                this.field_181680_j.writeLock().unlock(); // readLock -> writeLock (because of race condition between peek and poll)
            }

        }

        return true; // Return true if all packets were dispatched
    }
    // Paper end

    public void func_74428_b() {
        this.m();
        if (this.field_150744_m instanceof ITickable) {
            ((ITickable) this.field_150744_m).func_73660_a();
        }

        if (this.field_150746_k != null) {
            if (enableExplicitFlush) this.field_150746_k.eventLoop().execute(() -> this.field_150746_k.flush()); // Paper - we don't need to explicit flush here, but allow opt in incase issues are found to a better version
        }

    }

    public SocketAddress func_74430_c() {
        return this.field_150743_l;
    }

    public void func_150718_a(ITextComponent ichatbasecomponent) {
        // Spigot Start
        this.preparing = false;
        // Spigot End
        if (this.field_150746_k.isOpen()) {
            this.field_150746_k.close(); // We can't wait as this may be called from an event loop.
            this.field_150742_o = ichatbasecomponent;
        }

    }

    public boolean func_150731_c() {
        return this.field_150746_k instanceof LocalChannel || this.field_150746_k instanceof LocalServerChannel;
    }

    public void func_150727_a(SecretKey secretkey) {
        this.field_152463_r = true;
        this.field_150746_k.pipeline().addBefore("splitter", "decrypt", new NettyEncryptingDecoder(CryptManager.func_151229_a(2, secretkey)));
        this.field_150746_k.pipeline().addBefore("prepender", "encrypt", new NettyEncryptingEncoder(CryptManager.func_151229_a(1, secretkey)));
    }

    public boolean func_150724_d() {
        return this.field_150746_k != null && this.field_150746_k.isOpen();
    }

    public boolean func_179291_h() {
        return this.field_150746_k == null;
    }

    public INetHandler func_150729_e() {
        return this.field_150744_m;
    }

    public ITextComponent func_150730_f() {
        return this.field_150742_o;
    }

    public void func_150721_g() {
        this.field_150746_k.config().setAutoRead(false);
    }

    public void func_179289_a(int i) {
        if (i >= 0) {
            if (this.field_150746_k.pipeline().get("decompress") instanceof NettyCompressionDecoder) {
                ((NettyCompressionDecoder) this.field_150746_k.pipeline().get("decompress")).func_179303_a(i);
            } else {
                this.field_150746_k.pipeline().addBefore("decoder", "decompress", new NettyCompressionDecoder(i));
            }

            if (this.field_150746_k.pipeline().get("compress") instanceof NettyCompressionEncoder) {
                ((NettyCompressionEncoder) this.field_150746_k.pipeline().get("compress")).func_179299_a(i);
            } else {
                this.field_150746_k.pipeline().addBefore("encoder", "compress", new NettyCompressionEncoder(i));
            }
        } else {
            if (this.field_150746_k.pipeline().get("decompress") instanceof NettyCompressionDecoder) {
                this.field_150746_k.pipeline().remove("decompress");
            }

            if (this.field_150746_k.pipeline().get("compress") instanceof NettyCompressionEncoder) {
                this.field_150746_k.pipeline().remove("compress");
            }
        }

    }

    public void func_179293_l() {
        if (this.field_150746_k != null && !this.field_150746_k.isOpen()) {
            if (this.field_179297_n) {
                NetworkManager.field_150735_g.warn("handleDisconnection() called twice");
            } else {
                this.field_179297_n = true;
                if (this.func_150730_f() != null) {
                    this.func_150729_e().func_147231_a(this.func_150730_f());
                } else if (this.func_150729_e() != null) {
                    this.func_150729_e().func_147231_a(new TextComponentTranslation("multiplayer.disconnect.generic", new Object[0]));
                }
                this.field_150745_j.clear(); // Free up packet queue.
            }

        }
    }

    static class InboundHandlerTuplePacketListener {

        private final Packet<?> field_150774_a; private final Packet<?> getPacket() { return this.field_150774_a; } // Paper - Anti-Xray - OBFHELPER
        private final GenericFutureListener<? extends Future<? super Void>>[] field_150773_b; private final GenericFutureListener<? extends Future<? super Void>>[] getGenericFutureListeners() { return this.field_150773_b; } // Paper - Anti-Xray - OBFHELPER

        public InboundHandlerTuplePacketListener(Packet<?> packet, GenericFutureListener<? extends Future<? super Void>>... agenericfuturelistener) {
            this.field_150774_a = packet;
            this.field_150773_b = agenericfuturelistener;
        }
    }

    // Spigot Start
    public SocketAddress getRawAddress()
    {
        return this.field_150746_k.remoteAddress();
    }
    // Spigot End
}
