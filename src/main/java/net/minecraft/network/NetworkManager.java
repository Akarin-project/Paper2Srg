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

    private static final Logger LOGGER = LogManager.getLogger();
    public static final Marker NETWORK_MARKER = MarkerManager.getMarker("NETWORK");
    public static final Marker NETWORK_PACKETS_MARKER = MarkerManager.getMarker("NETWORK_PACKETS", NetworkManager.NETWORK_MARKER);
    public static final AttributeKey<EnumConnectionState> PROTOCOL_ATTRIBUTE_KEY = AttributeKey.valueOf("protocol");
    public static final LazyLoadBase<NioEventLoopGroup> CLIENT_NIO_EVENTLOOP = new LazyLoadBase() {
        protected NioEventLoopGroup a() {
            return new NioEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Netty Client IO #%d").setDaemon(true).build());
        }

        protected Object load() {
            return this.a();
        }
    };
    public static final LazyLoadBase<EpollEventLoopGroup> CLIENT_EPOLL_EVENTLOOP = new LazyLoadBase() {
        protected EpollEventLoopGroup a() {
            return new EpollEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Netty Epoll Client IO #%d").setDaemon(true).build());
        }

        protected Object load() {
            return this.a();
        }
    };
    public static final LazyLoadBase<LocalEventLoopGroup> CLIENT_LOCAL_EVENTLOOP = new LazyLoadBase() {
        protected LocalEventLoopGroup a() {
            return new LocalEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Netty Local Client IO #%d").setDaemon(true).build());
        }

        protected Object load() {
            return this.a();
        }
    };
    private final EnumPacketDirection direction;
    private final Queue<NetworkManager.InboundHandlerTuplePacketListener> outboundPacketsQueue = Queues.newConcurrentLinkedQueue(); private final Queue<NetworkManager.InboundHandlerTuplePacketListener> getPacketQueue() { return this.outboundPacketsQueue; } // Paper - Anti-Xray - OBFHELPER
    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    public Channel channel;
    // Spigot Start // PAIL
    public SocketAddress socketAddress;
    public java.util.UUID spoofedUUID;
    public com.mojang.authlib.properties.Property[] spoofedProfile;
    public boolean preparing = true;
    // Spigot End
    private INetHandler packetListener;
    private ITextComponent terminationReason;
    private boolean isEncrypted;
    private boolean disconnected;
    // Paper start - NetworkClient implementation
    public int protocolVersion;
    public java.net.InetSocketAddress virtualHost;
    private static boolean enableExplicitFlush = Boolean.getBoolean("paper.explicit-flush");
    // Paper end

    public NetworkManager(EnumPacketDirection enumprotocoldirection) {
        this.direction = enumprotocoldirection;
    }

    public void channelActive(ChannelHandlerContext channelhandlercontext) throws Exception {
        super.channelActive(channelhandlercontext);
        this.channel = channelhandlercontext.channel();
        this.socketAddress = this.channel.remoteAddress();
        // Spigot Start
        this.preparing = false;
        // Spigot End

        try {
            this.setConnectionState(EnumConnectionState.HANDSHAKING);
        } catch (Throwable throwable) {
            NetworkManager.LOGGER.fatal(throwable);
        }

    }

    public void setConnectionState(EnumConnectionState enumprotocol) {
        this.channel.attr(NetworkManager.PROTOCOL_ATTRIBUTE_KEY).set(enumprotocol);
        this.channel.config().setAutoRead(true);
        NetworkManager.LOGGER.debug("Enabled auto read");
    }

    public void channelInactive(ChannelHandlerContext channelhandlercontext) throws Exception {
        this.closeChannel(new TextComponentTranslation("disconnect.endOfStream", new Object[0]));
    }

    public void exceptionCaught(ChannelHandlerContext channelhandlercontext, Throwable throwable) throws Exception {
        TextComponentTranslation chatmessage;

        if (throwable instanceof TimeoutException) {
            chatmessage = new TextComponentTranslation("disconnect.timeout", new Object[0]);
        } else {
            chatmessage = new TextComponentTranslation("disconnect.genericReason", new Object[] { "Internal Exception: " + throwable});
        }

        NetworkManager.LOGGER.debug(chatmessage.getUnformattedText(), throwable);
        this.closeChannel(chatmessage);
        if (MinecraftServer.getServer().isDebuggingEnabled()) throwable.printStackTrace(); // Spigot
    }

    protected void channelRead0(ChannelHandlerContext channelhandlercontext, Packet<?> packet) throws Exception {
        if (this.channel.isOpen()) {
            try {
                ((Packet) packet).processPacket(this.packetListener); // CraftBukkit - decompile error
            } catch (ThreadQuickExitException cancelledpackethandleexception) {
                ;
            }
        }

    }

    public void setNetHandler(INetHandler packetlistener) {
        Validate.notNull(packetlistener, "packetListener", new Object[0]);
        NetworkManager.LOGGER.debug("Set listener of {} to {}", this, packetlistener);
        this.packetListener = packetlistener;
    }

    public void sendPacket(Packet<?> packet) {
        if (this.isChannelOpen() && this.trySendQueue() && !(packet instanceof SPacketChunkData && !((SPacketChunkData) packet).isReady())) { // Paper - Async-Anti-Xray - Add chunk packets which are not ready or all packets if the queue contains chunk packets which are not ready to the queue and send the packets later in the right order
            //this.m(); // Paper - Async-Anti-Xray - Move to if statement (this.trySendQueue())
            this.dispatchPacket(packet, (GenericFutureListener[]) null);
        } else {
            this.readWriteLock.writeLock().lock();

            try {
                this.outboundPacketsQueue.add(new NetworkManager.InboundHandlerTuplePacketListener(packet, new GenericFutureListener[0]));
            } finally {
                this.readWriteLock.writeLock().unlock();
            }
        }

    }

    public void sendPacket(Packet<?> packet, GenericFutureListener<? extends Future<? super Void>> genericfuturelistener, GenericFutureListener<? extends Future<? super Void>>... agenericfuturelistener) {
        if (this.isChannelOpen() && this.trySendQueue() && !(packet instanceof SPacketChunkData && !((SPacketChunkData) packet).isReady())) { // Paper - Async-Anti-Xray - Add chunk packets which are not ready or all packets if the queue contains chunk packets which are not ready to the queue and send the packets later in the right order
            //this.m(); // Paper - Async-Anti-Xray - Move to if statement (this.trySendQueue())
            this.dispatchPacket(packet, (GenericFutureListener[]) ArrayUtils.add(agenericfuturelistener, 0, genericfuturelistener));
        } else {
            this.readWriteLock.writeLock().lock();

            try {
                this.outboundPacketsQueue.add(new NetworkManager.InboundHandlerTuplePacketListener(packet, (GenericFutureListener[]) ArrayUtils.add(agenericfuturelistener, 0, genericfuturelistener)));
            } finally {
                this.readWriteLock.writeLock().unlock();
            }
        }

    }

    private void dispatchPacket(final Packet<?> packet, @Nullable final GenericFutureListener<? extends Future<? super Void>>[] genericFutureListeners) { this.dispatchPacket(packet, genericFutureListeners); } // Paper - Anti-Xray - OBFHELPER
    private void dispatchPacket(final Packet<?> packet, @Nullable final GenericFutureListener<? extends Future<? super Void>>[] agenericfuturelistener) {
        final EnumConnectionState enumprotocol = EnumConnectionState.getFromPacket(packet);
        final EnumConnectionState enumprotocol1 = (EnumConnectionState) this.channel.attr(NetworkManager.PROTOCOL_ATTRIBUTE_KEY).get();

        if (enumprotocol1 != enumprotocol) {
            NetworkManager.LOGGER.debug("Disabled auto read");
            this.channel.config().setAutoRead(false);
        }

        if (this.channel.eventLoop().inEventLoop()) {
            if (enumprotocol != enumprotocol1) {
                this.setConnectionState(enumprotocol);
            }

            ChannelFuture channelfuture = this.channel.writeAndFlush(packet);

            if (agenericfuturelistener != null) {
                channelfuture.addListeners(agenericfuturelistener);
            }

            channelfuture.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
        } else {
            this.channel.eventLoop().execute(new Runnable() {
                public void run() {
                    if (enumprotocol != enumprotocol1) {
                        NetworkManager.this.setConnectionState(enumprotocol);
                    }

                    ChannelFuture channelfuture = NetworkManager.this.channel.writeAndFlush(packet);

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
        if (this.channel != null && this.channel.isOpen()) {
            if (this.outboundPacketsQueue.isEmpty()) { // return if the packet queue is empty so that the write lock by Anti-Xray doesn't affect the vanilla performance at all
                return true;
            }

            this.readWriteLock.writeLock().lock(); // readLock -> writeLock (because of race condition between peek and poll)

            try {
                while (!this.outboundPacketsQueue.isEmpty()) {
                    NetworkManager.InboundHandlerTuplePacketListener networkmanager_queuedpacket = (NetworkManager.InboundHandlerTuplePacketListener) this.getPacketQueue().peek(); // poll -> peek

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
                this.readWriteLock.writeLock().unlock(); // readLock -> writeLock (because of race condition between peek and poll)
            }

        }

        return true; // Return true if all packets were dispatched
    }
    // Paper end

    public void processReceivedPackets() {
        this.m();
        if (this.packetListener instanceof ITickable) {
            ((ITickable) this.packetListener).update();
        }

        if (this.channel != null) {
            if (enableExplicitFlush) this.channel.eventLoop().execute(() -> this.channel.flush()); // Paper - we don't need to explicit flush here, but allow opt in incase issues are found to a better version
        }

    }

    public SocketAddress getRemoteAddress() {
        return this.socketAddress;
    }

    public void closeChannel(ITextComponent ichatbasecomponent) {
        // Spigot Start
        this.preparing = false;
        // Spigot End
        if (this.channel.isOpen()) {
            this.channel.close(); // We can't wait as this may be called from an event loop.
            this.terminationReason = ichatbasecomponent;
        }

    }

    public boolean isLocalChannel() {
        return this.channel instanceof LocalChannel || this.channel instanceof LocalServerChannel;
    }

    public void enableEncryption(SecretKey secretkey) {
        this.isEncrypted = true;
        this.channel.pipeline().addBefore("splitter", "decrypt", new NettyEncryptingDecoder(CryptManager.createNetCipherInstance(2, secretkey)));
        this.channel.pipeline().addBefore("prepender", "encrypt", new NettyEncryptingEncoder(CryptManager.createNetCipherInstance(1, secretkey)));
    }

    public boolean isChannelOpen() {
        return this.channel != null && this.channel.isOpen();
    }

    public boolean hasNoChannel() {
        return this.channel == null;
    }

    public INetHandler getNetHandler() {
        return this.packetListener;
    }

    public ITextComponent getExitMessage() {
        return this.terminationReason;
    }

    public void disableAutoRead() {
        this.channel.config().setAutoRead(false);
    }

    public void setCompressionThreshold(int i) {
        if (i >= 0) {
            if (this.channel.pipeline().get("decompress") instanceof NettyCompressionDecoder) {
                ((NettyCompressionDecoder) this.channel.pipeline().get("decompress")).setCompressionThreshold(i);
            } else {
                this.channel.pipeline().addBefore("decoder", "decompress", new NettyCompressionDecoder(i));
            }

            if (this.channel.pipeline().get("compress") instanceof NettyCompressionEncoder) {
                ((NettyCompressionEncoder) this.channel.pipeline().get("compress")).setCompressionThreshold(i);
            } else {
                this.channel.pipeline().addBefore("encoder", "compress", new NettyCompressionEncoder(i));
            }
        } else {
            if (this.channel.pipeline().get("decompress") instanceof NettyCompressionDecoder) {
                this.channel.pipeline().remove("decompress");
            }

            if (this.channel.pipeline().get("compress") instanceof NettyCompressionEncoder) {
                this.channel.pipeline().remove("compress");
            }
        }

    }

    public void checkDisconnected() {
        if (this.channel != null && !this.channel.isOpen()) {
            if (this.disconnected) {
                NetworkManager.LOGGER.warn("handleDisconnection() called twice");
            } else {
                this.disconnected = true;
                if (this.getExitMessage() != null) {
                    this.getNetHandler().onDisconnect(this.getExitMessage());
                } else if (this.getNetHandler() != null) {
                    this.getNetHandler().onDisconnect(new TextComponentTranslation("multiplayer.disconnect.generic", new Object[0]));
                }
                this.outboundPacketsQueue.clear(); // Free up packet queue.
            }

        }
    }

    protected void channelRead0(ChannelHandlerContext channelhandlercontext, Packet object) throws Exception { // CraftBukkit - fix decompile error
        this.channelRead0(channelhandlercontext, (Packet) object);
    }

    static class InboundHandlerTuplePacketListener {

        private final Packet<?> packet; private final Packet<?> getPacket() { return this.packet; } // Paper - Anti-Xray - OBFHELPER
        private final GenericFutureListener<? extends Future<? super Void>>[] futureListeners; private final GenericFutureListener<? extends Future<? super Void>>[] getGenericFutureListeners() { return this.futureListeners; } // Paper - Anti-Xray - OBFHELPER

        public InboundHandlerTuplePacketListener(Packet<?> packet, GenericFutureListener<? extends Future<? super Void>>... agenericfuturelistener) {
            this.packet = packet;
            this.futureListeners = agenericfuturelistener;
        }
    }

    // Spigot Start
    public SocketAddress getRawAddress()
    {
        return this.channel.remoteAddress();
    }
    // Spigot End
}
