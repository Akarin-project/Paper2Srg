package net.minecraft.network;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.local.LocalEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.network.play.server.SPacketDisconnect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.NetHandlerHandshakeTCP;
import net.minecraft.util.LazyLoadBase;
import net.minecraft.util.ReportedException;
import net.minecraft.util.text.TextComponentString;

public class NetworkSystem {

    private static final Logger LOGGER = LogManager.getLogger();
    public static final LazyLoadBase<NioEventLoopGroup> SERVER_NIO_EVENTLOOP = new LazyLoadBase() {
        protected NioEventLoopGroup a() {
            return new NioEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Netty Server IO #%d").setDaemon(true).build());
        }

        protected Object load() {
            return this.a();
        }
    };
    public static final LazyLoadBase<EpollEventLoopGroup> SERVER_EPOLL_EVENTLOOP = new LazyLoadBase() {
        protected EpollEventLoopGroup a() {
            return new EpollEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Netty Epoll Server IO #%d").setDaemon(true).build());
        }

        protected Object load() {
            return this.a();
        }
    };
    public static final LazyLoadBase<LocalEventLoopGroup> SERVER_LOCAL_EVENTLOOP = new LazyLoadBase() {
        protected LocalEventLoopGroup a() {
            return new LocalEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Netty Local Server IO #%d").setDaemon(true).build());
        }

        protected Object load() {
            return this.a();
        }
    };
    private final MinecraftServer mcServer;
    public volatile boolean isAlive;
    private final List<ChannelFuture> endpoints = Collections.synchronizedList(Lists.newArrayList());
    private final List<NetworkManager> networkManagers = Collections.synchronizedList(Lists.newArrayList());
    // Paper start - prevent blocking on adding a new network manager while the server is ticking
    private final List<NetworkManager> pending = Collections.synchronizedList(Lists.<NetworkManager>newArrayList());
    private void addPending() {
        synchronized (pending) {
            this.networkManagers.addAll(pending); // Paper - OBFHELPER - List of network managers
            pending.clear();
        }
    }
    // Paper end

    public NetworkSystem(MinecraftServer minecraftserver) {
        this.mcServer = minecraftserver;
        this.isAlive = true;
    }

    public void addLanEndpoint(InetAddress inetaddress, int i) throws IOException {
        List list = this.endpoints;

        synchronized (this.endpoints) {
            Class oclass;
            LazyLoadBase lazyinitvar;

            if (Epoll.isAvailable() && this.mcServer.shouldUseNativeTransport()) {
                oclass = EpollServerSocketChannel.class;
                lazyinitvar = NetworkSystem.SERVER_EPOLL_EVENTLOOP;
                NetworkSystem.LOGGER.info("Using epoll channel type");
            } else {
                oclass = NioServerSocketChannel.class;
                lazyinitvar = NetworkSystem.SERVER_NIO_EVENTLOOP;
                NetworkSystem.LOGGER.info("Using default channel type");
            }

            this.endpoints.add(((ServerBootstrap) ((ServerBootstrap) (new ServerBootstrap()).channel(oclass)).childHandler(new ChannelInitializer() {
                protected void initChannel(Channel channel) throws Exception {
                    try {
                        channel.config().setOption(ChannelOption.TCP_NODELAY, Boolean.valueOf(true));
                    } catch (ChannelException channelexception) {
                        ;
                    }

                    channel.pipeline().addLast("timeout", new ReadTimeoutHandler(30)).addLast("legacy_query", new LegacyPingHandler(NetworkSystem.this)).addLast("splitter", new NettyVarint21FrameDecoder()).addLast("decoder", new NettyPacketDecoder(EnumPacketDirection.SERVERBOUND)).addLast("prepender", new NettyVarint21FrameEncoder()).addLast("encoder", new NettyPacketEncoder(EnumPacketDirection.CLIENTBOUND));
                    NetworkManager networkmanager = new NetworkManager(EnumPacketDirection.SERVERBOUND);

                    pending.add(networkmanager); // Paper
                    channel.pipeline().addLast("packet_handler", networkmanager);
                    networkmanager.setNetHandler(new NetHandlerHandshakeTCP(NetworkSystem.this.mcServer, networkmanager));
                }
            }).group((EventLoopGroup) lazyinitvar.getValue()).localAddress(inetaddress, i)).bind().syncUninterruptibly());
        }
    }

    public void terminateEndpoints() {
        this.isAlive = false;
        Iterator iterator = this.endpoints.iterator();

        while (iterator.hasNext()) {
            ChannelFuture channelfuture = (ChannelFuture) iterator.next();

            try {
                channelfuture.channel().close().sync();
            } catch (InterruptedException interruptedexception) {
                NetworkSystem.LOGGER.error("Interrupted whilst closing channel");
            }
        }

    }

    public void networkTick() {
        List list = this.networkManagers;

        synchronized (this.networkManagers) {
            // Spigot Start
            addPending(); // Paper
            // This prevents players from 'gaming' the server, and strategically relogging to increase their position in the tick order
            if ( org.spigotmc.SpigotConfig.playerShuffle > 0 && MinecraftServer.currentTick % org.spigotmc.SpigotConfig.playerShuffle == 0 )
            {
                Collections.shuffle( this.networkManagers );
            }
            // Spigot End
            Iterator iterator = this.networkManagers.iterator();

            while (iterator.hasNext()) {
                final NetworkManager networkmanager = (NetworkManager) iterator.next();

                if (!networkmanager.hasNoChannel()) {
                    if (networkmanager.isChannelOpen()) {
                        try {
                            networkmanager.processReceivedPackets();
                        } catch (Exception exception) {
                            if (networkmanager.isLocalChannel()) {
                                CrashReport crashreport = CrashReport.makeCrashReport(exception, "Ticking memory connection");
                                CrashReportCategory crashreportsystemdetails = crashreport.makeCategory("Ticking connection");

                                crashreportsystemdetails.addDetail("Connection", new ICrashReportDetail() {
                                    public String a() throws Exception {
                                        return networkmanager.toString();
                                    }

                                    public Object call() throws Exception {
                                        return this.a();
                                    }
                                });
                                throw new ReportedException(crashreport);
                            }

                            NetworkSystem.LOGGER.warn("Failed to handle packet for {}", networkmanager.getRemoteAddress(), exception);
                            final TextComponentString chatcomponenttext = new TextComponentString("Internal server error");

                            networkmanager.sendPacket(new SPacketDisconnect(chatcomponenttext), new GenericFutureListener() {
                                public void operationComplete(Future future) throws Exception {
                                    networkmanager.closeChannel(chatcomponenttext);
                                }
                            }, new GenericFutureListener[0]);
                            networkmanager.disableAutoRead();
                        }
                    } else {
                        // Spigot Start
                        // Fix a race condition where a NetworkManager could be unregistered just before connection.
                        if (networkmanager.preparing) continue;
                        // Spigot End
                        iterator.remove();
                        networkmanager.checkDisconnected();
                    }
                }
            }

        }
    }

    public MinecraftServer getServer() {
        return this.mcServer;
    }
}
