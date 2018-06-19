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

    private static final Logger field_151275_b = LogManager.getLogger();
    public static final LazyLoadBase<NioEventLoopGroup> field_151276_c = new LazyLoadBase() {
        protected NioEventLoopGroup a() {
            return new NioEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Netty Server IO #%d").setDaemon(true).build());
        }

        protected Object func_179280_b() {
            return this.a();
        }
    };
    public static final LazyLoadBase<EpollEventLoopGroup> field_181141_b = new LazyLoadBase() {
        protected EpollEventLoopGroup a() {
            return new EpollEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Netty Epoll Server IO #%d").setDaemon(true).build());
        }

        protected Object func_179280_b() {
            return this.a();
        }
    };
    public static final LazyLoadBase<LocalEventLoopGroup> field_180232_b = new LazyLoadBase() {
        protected LocalEventLoopGroup a() {
            return new LocalEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Netty Local Server IO #%d").setDaemon(true).build());
        }

        protected Object func_179280_b() {
            return this.a();
        }
    };
    private final MinecraftServer field_151273_d;
    public volatile boolean field_151277_a;
    private final List<ChannelFuture> field_151274_e = Collections.synchronizedList(Lists.newArrayList());
    private final List<NetworkManager> field_151272_f = Collections.synchronizedList(Lists.newArrayList());
    // Paper start - prevent blocking on adding a new network manager while the server is ticking
    private final List<NetworkManager> pending = Collections.synchronizedList(Lists.<NetworkManager>newArrayList());
    private void addPending() {
        synchronized (pending) {
            this.field_151272_f.addAll(pending); // Paper - OBFHELPER - List of network managers
            pending.clear();
        }
    }
    // Paper end

    public NetworkSystem(MinecraftServer minecraftserver) {
        this.field_151273_d = minecraftserver;
        this.field_151277_a = true;
    }

    public void func_151265_a(InetAddress inetaddress, int i) throws IOException {
        List list = this.field_151274_e;

        synchronized (this.field_151274_e) {
            Class oclass;
            LazyLoadBase lazyinitvar;

            if (Epoll.isAvailable() && this.field_151273_d.func_181035_ah()) {
                oclass = EpollServerSocketChannel.class;
                lazyinitvar = NetworkSystem.field_181141_b;
                NetworkSystem.field_151275_b.info("Using epoll channel type");
            } else {
                oclass = NioServerSocketChannel.class;
                lazyinitvar = NetworkSystem.field_151276_c;
                NetworkSystem.field_151275_b.info("Using default channel type");
            }

            this.field_151274_e.add(((ServerBootstrap) ((ServerBootstrap) (new ServerBootstrap()).channel(oclass)).childHandler(new ChannelInitializer() {
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
                    networkmanager.func_150719_a(new NetHandlerHandshakeTCP(NetworkSystem.this.field_151273_d, networkmanager));
                }
            }).group((EventLoopGroup) lazyinitvar.func_179281_c()).localAddress(inetaddress, i)).bind().syncUninterruptibly());
        }
    }

    public void func_151268_b() {
        this.field_151277_a = false;
        Iterator iterator = this.field_151274_e.iterator();

        while (iterator.hasNext()) {
            ChannelFuture channelfuture = (ChannelFuture) iterator.next();

            try {
                channelfuture.channel().close().sync();
            } catch (InterruptedException interruptedexception) {
                NetworkSystem.field_151275_b.error("Interrupted whilst closing channel");
            }
        }

    }

    public void func_151269_c() {
        List list = this.field_151272_f;

        synchronized (this.field_151272_f) {
            // Spigot Start
            addPending(); // Paper
            // This prevents players from 'gaming' the server, and strategically relogging to increase their position in the tick order
            if ( org.spigotmc.SpigotConfig.playerShuffle > 0 && MinecraftServer.currentTick % org.spigotmc.SpigotConfig.playerShuffle == 0 )
            {
                Collections.shuffle( this.field_151272_f );
            }
            // Spigot End
            Iterator iterator = this.field_151272_f.iterator();

            while (iterator.hasNext()) {
                final NetworkManager networkmanager = (NetworkManager) iterator.next();

                if (!networkmanager.func_179291_h()) {
                    if (networkmanager.func_150724_d()) {
                        try {
                            networkmanager.func_74428_b();
                        } catch (Exception exception) {
                            if (networkmanager.func_150731_c()) {
                                CrashReport crashreport = CrashReport.func_85055_a(exception, "Ticking memory connection");
                                CrashReportCategory crashreportsystemdetails = crashreport.func_85058_a("Ticking connection");

                                crashreportsystemdetails.func_189529_a("Connection", new ICrashReportDetail() {
                                    public String a() throws Exception {
                                        return networkmanager.toString();
                                    }

                                    public Object call() throws Exception {
                                        return this.a();
                                    }
                                });
                                throw new ReportedException(crashreport);
                            }

                            NetworkSystem.field_151275_b.warn("Failed to handle packet for {}", networkmanager.func_74430_c(), exception);
                            final TextComponentString chatcomponenttext = new TextComponentString("Internal server error");

                            networkmanager.func_179288_a(new SPacketDisconnect(chatcomponenttext), new GenericFutureListener() {
                                public void operationComplete(Future future) throws Exception {
                                    networkmanager.func_150718_a(chatcomponenttext);
                                }
                            }, new GenericFutureListener[0]);
                            networkmanager.func_150721_g();
                        }
                    } else {
                        // Spigot Start
                        // Fix a race condition where a NetworkManager could be unregistered just before connection.
                        if (networkmanager.preparing) continue;
                        // Spigot End
                        iterator.remove();
                        networkmanager.func_179293_l();
                    }
                }
            }

        }
    }

    public MinecraftServer func_151267_d() {
        return this.field_151273_d;
    }
}
