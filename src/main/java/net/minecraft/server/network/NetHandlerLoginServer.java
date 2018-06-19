package net.minecraft.server.network;

import com.destroystokyo.paper.profile.CraftPlayerProfile;
import com.destroystokyo.paper.profile.PlayerProfile;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.concurrent.GenericFutureListener;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nullable;
import javax.crypto.SecretKey;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.login.INetHandlerLoginServer;
import net.minecraft.network.login.client.CPacketEncryptionResponse;
import net.minecraft.network.login.client.CPacketLoginStart;
import net.minecraft.network.login.server.SPacketDisconnect;
import net.minecraft.network.login.server.SPacketEnableCompression;
import net.minecraft.network.login.server.SPacketEncryptionRequest;
import net.minecraft.network.login.server.SPacketLoginSuccess;
import net.minecraft.server.LoginListener.LoginHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.CryptManager;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import org.bukkit.craftbukkit.util.Waitable;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;

// CraftBukkit start
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.util.Waitable;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;
// CraftBukkit end

public class NetHandlerLoginServer implements INetHandlerLoginServer, ITickable {

    private static final AtomicInteger field_147331_b = new AtomicInteger(0);
    private static final Logger field_147332_c = LogManager.getLogger();
    private static final Random field_147329_d = new Random();
    private final byte[] field_147330_e = new byte[4];
    private final MinecraftServer field_147327_f;
    public final NetworkManager field_147333_a;
    private NetHandlerLoginServer.LoginState field_147328_g;
    private int field_147336_h;
    private GameProfile field_147337_i; private void setGameProfile(GameProfile profile) { field_147337_i = profile; } private GameProfile getGameProfile() { return field_147337_i; } // Paper - OBFHELPER
    private final String field_147334_j;
    private SecretKey field_147335_k;
    private EntityPlayerMP field_181025_l;
    public String hostname = ""; // CraftBukkit - add field

    public NetHandlerLoginServer(MinecraftServer minecraftserver, NetworkManager networkmanager) {
        this.field_147328_g = NetHandlerLoginServer.LoginState.HELLO;
        this.field_147334_j = "";
        this.field_147327_f = minecraftserver;
        this.field_147333_a = networkmanager;
        NetHandlerLoginServer.field_147329_d.nextBytes(this.field_147330_e);
    }

    public void func_73660_a() {
        // Paper start - Do not allow logins while the server is shutting down
        if (!MinecraftServer.getServer().func_71278_l()) {
            this.func_194026_b(new TextComponentTranslation(org.spigotmc.SpigotConfig.restartMessage));
            return;
        }
        // Paper end
        if (this.field_147328_g == NetHandlerLoginServer.LoginState.READY_TO_ACCEPT) {
            // Paper start - prevent logins to be processed even though disconnect was called
            if (field_147333_a.func_150724_d()) {
                this.func_147326_c();
            }
            // Paper end
        } else if (this.field_147328_g == NetHandlerLoginServer.LoginState.DELAY_ACCEPT) {
            EntityPlayerMP entityplayer = this.field_147327_f.func_184103_al().func_177451_a(this.field_147337_i.getId());

            if (entityplayer == null) {
                this.field_147328_g = NetHandlerLoginServer.LoginState.READY_TO_ACCEPT;
                this.field_147327_f.func_184103_al().func_72355_a(this.field_147333_a, this.field_181025_l);
                this.field_181025_l = null;
            }
        }

        if (this.field_147336_h++ == 600) {
            this.func_194026_b(new TextComponentTranslation("multiplayer.disconnect.slow_login", new Object[0]));
        }

    }

    // CraftBukkit start
    @Deprecated
    public void disconnect(String s) {
        try {
            ITextComponent ichatbasecomponent = new TextComponentString(s);
            NetHandlerLoginServer.field_147332_c.info("Disconnecting {}: {}", this.func_147317_d(), s);
            this.field_147333_a.func_179290_a(new SPacketDisconnect(ichatbasecomponent));
            this.field_147333_a.func_150718_a(ichatbasecomponent);
        } catch (Exception exception) {
            NetHandlerLoginServer.field_147332_c.error("Error whilst disconnecting player", exception);
        }
    }
    // CraftBukkit end

    public void func_194026_b(ITextComponent ichatbasecomponent) {
        try {
            NetHandlerLoginServer.field_147332_c.info("Disconnecting {}: {}", this.func_147317_d(), ichatbasecomponent.func_150260_c());
            this.field_147333_a.func_179290_a(new SPacketDisconnect(ichatbasecomponent));
            this.field_147333_a.func_150718_a(ichatbasecomponent);
        } catch (Exception exception) {
            NetHandlerLoginServer.field_147332_c.error("Error whilst disconnecting player", exception);
        }

    }

    // Paper start - Cache authenticator threads
    private static final AtomicInteger threadId = new AtomicInteger(0);
    private static final java.util.concurrent.ExecutorService authenticatorPool = java.util.concurrent.Executors.newCachedThreadPool(
            r -> new Thread(r, "User Authenticator #" + threadId.incrementAndGet())
    );
    // Paper end
    // Spigot start
    public void initUUID()
    {
        UUID uuid;
        if ( field_147333_a.spoofedUUID != null )
        {
            uuid = field_147333_a.spoofedUUID;
        } else
        {
            uuid = UUID.nameUUIDFromBytes( ( "OfflinePlayer:" + this.field_147337_i.getName() ).getBytes( StandardCharsets.UTF_8 ) );
        }

        this.field_147337_i = new GameProfile( uuid, this.field_147337_i.getName() );

        if (field_147333_a.spoofedProfile != null)
        {
            for ( com.mojang.authlib.properties.Property property : field_147333_a.spoofedProfile )
            {
                this.field_147337_i.getProperties().put( property.getName(), property );
            }
        }
    }
    // Spigot end

    public void func_147326_c() {
        // Spigot start - Moved to initUUID
        /*
        if (!this.i.isComplete()) {
            this.i = this.a(this.i);
        }
        */
        // Spigot end

        // CraftBukkit start - fire PlayerLoginEvent
        EntityPlayerMP s = this.field_147327_f.func_184103_al().attemptLogin(this, this.field_147337_i, hostname);

        if (s == null) {
            // this.disconnect(new ChatMessage(s, new Object[0]));
            // CraftBukkit end
        } else {
            this.field_147328_g = NetHandlerLoginServer.LoginState.ACCEPTED;
            if (this.field_147327_f.func_175577_aI() >= 0 && !this.field_147333_a.func_150731_c()) {
                this.field_147333_a.func_179288_a(new SPacketEnableCompression(this.field_147327_f.func_175577_aI()), new ChannelFutureListener() {
                    public void a(ChannelFuture channelfuture) throws Exception {
                        NetHandlerLoginServer.this.field_147333_a.func_179289_a(NetHandlerLoginServer.this.field_147327_f.func_175577_aI());
                    }

                    public void operationComplete(ChannelFuture future) throws Exception { // CraftBukkit - fix decompile error
                        this.a((ChannelFuture) future);
                    }
                }, new GenericFutureListener[0]);
            }

            this.field_147333_a.func_179290_a(new SPacketLoginSuccess(this.field_147337_i));
            EntityPlayerMP entityplayer = this.field_147327_f.func_184103_al().func_177451_a(this.field_147337_i.getId());

            if (entityplayer != null) {
                this.field_147328_g = NetHandlerLoginServer.LoginState.DELAY_ACCEPT;
                this.field_181025_l = this.field_147327_f.func_184103_al().processLogin(this.field_147337_i, s); // CraftBukkit - add player reference
            } else {
                this.field_147327_f.func_184103_al().func_72355_a(this.field_147333_a, this.field_147327_f.func_184103_al().processLogin(this.field_147337_i, s)); // CraftBukkit - add player reference
            }
        }

    }

    public void func_147231_a(ITextComponent ichatbasecomponent) {
        NetHandlerLoginServer.field_147332_c.info("{} lost connection: {}", this.func_147317_d(), ichatbasecomponent.func_150260_c());
    }

    public String func_147317_d() {
        return this.field_147337_i != null ? this.field_147337_i + " (" + this.field_147333_a.func_74430_c() + ")" : String.valueOf(this.field_147333_a.func_74430_c());
    }

    public void func_147316_a(CPacketLoginStart packetlogininstart) {
        Validate.validState(this.field_147328_g == NetHandlerLoginServer.LoginState.HELLO, "Unexpected hello packet", new Object[0]);
        this.field_147337_i = packetlogininstart.func_149304_c();
        if (this.field_147327_f.func_71266_T() && !this.field_147333_a.func_150731_c()) {
            this.field_147328_g = NetHandlerLoginServer.LoginState.KEY;
            this.field_147333_a.func_179290_a(new SPacketEncryptionRequest("", this.field_147327_f.func_71250_E().getPublic(), this.field_147330_e));
        } else {
            // Spigot start
            // Paper start - Cache authenticator threads
            authenticatorPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        initUUID();
                        new LoginHandler().fireEvents();
                        NetHandlerLoginServer.this.field_147328_g = NetHandlerLoginServer.LoginState.READY_TO_ACCEPT;
                    } catch (Exception ex) {
                        disconnect("Failed to verify username!");
                        field_147327_f.server.getLogger().log(java.util.logging.Level.WARNING, "Exception verifying " + field_147337_i.getName(), ex);
                    }
                }
            });
            // Paper end
            // Spigot end
        }

    }

    public void func_147315_a(CPacketEncryptionResponse packetlogininencryptionbegin) {
        Validate.validState(this.field_147328_g == NetHandlerLoginServer.LoginState.KEY, "Unexpected key packet", new Object[0]);
        PrivateKey privatekey = this.field_147327_f.func_71250_E().getPrivate();

        if (!Arrays.equals(this.field_147330_e, packetlogininencryptionbegin.func_149299_b(privatekey))) {
            throw new IllegalStateException("Invalid nonce!");
        } else {
            this.field_147335_k = packetlogininencryptionbegin.func_149300_a(privatekey);
            this.field_147328_g = NetHandlerLoginServer.LoginState.AUTHENTICATING;
            this.field_147333_a.func_150727_a(this.field_147335_k);
            // Paper start - Cache authenticator threads
            authenticatorPool.execute(new Runnable() {
                public void run() {
                    GameProfile gameprofile = NetHandlerLoginServer.this.field_147337_i;

                    try {
                        String s = (new BigInteger(CryptManager.func_75895_a("", NetHandlerLoginServer.this.field_147327_f.func_71250_E().getPublic(), NetHandlerLoginServer.this.field_147335_k))).toString(16);

                        NetHandlerLoginServer.this.field_147337_i = NetHandlerLoginServer.this.field_147327_f.func_147130_as().hasJoinedServer(new GameProfile((UUID) null, gameprofile.getName()), s, this.a());
                        if (NetHandlerLoginServer.this.field_147337_i != null) {
                            // CraftBukkit start - fire PlayerPreLoginEvent
                            if (!field_147333_a.func_150724_d()) {
                                return;
                            }

                            new LoginHandler().fireEvents();
                        } else if (NetHandlerLoginServer.this.field_147327_f.func_71264_H()) {
                            NetHandlerLoginServer.field_147332_c.warn("Failed to verify username but will let them in anyway!");
                            NetHandlerLoginServer.this.field_147337_i = NetHandlerLoginServer.this.func_152506_a(gameprofile);
                            NetHandlerLoginServer.this.field_147328_g = NetHandlerLoginServer.LoginState.READY_TO_ACCEPT;
                        } else {
                            NetHandlerLoginServer.this.func_194026_b(new TextComponentTranslation("multiplayer.disconnect.unverified_username", new Object[0]));
                            NetHandlerLoginServer.field_147332_c.error("Username \'{}\' tried to join with an invalid session", gameprofile.getName());
                        }
                    } catch (AuthenticationUnavailableException authenticationunavailableexception) {
                        if (NetHandlerLoginServer.this.field_147327_f.func_71264_H()) {
                            NetHandlerLoginServer.field_147332_c.warn("Authentication servers are down but will let them in anyway!");
                            NetHandlerLoginServer.this.field_147337_i = NetHandlerLoginServer.this.func_152506_a(gameprofile);
                            NetHandlerLoginServer.this.field_147328_g = NetHandlerLoginServer.LoginState.READY_TO_ACCEPT;
                        } else {
                            // Paper start
                            if (com.destroystokyo.paper.PaperConfig.authenticationServersDownKickMessage != null) {
                                NetHandlerLoginServer.this.func_194026_b(new TextComponentString(com.destroystokyo.paper.PaperConfig.authenticationServersDownKickMessage));
                            } else // Paper end
                            NetHandlerLoginServer.this.func_194026_b(new TextComponentTranslation("multiplayer.disconnect.authservers_down", new Object[0]));
                            NetHandlerLoginServer.field_147332_c.error("Couldn\'t verify username because servers are unavailable");
                        }
                        // CraftBukkit start - catch all exceptions
                    } catch (Exception exception) {
                        disconnect("Failed to verify username!");
                        field_147327_f.server.getLogger().log(java.util.logging.Level.WARNING, "Exception verifying " + gameprofile.getName(), exception);
                        // CraftBukkit end
                    }

                }

                @Nullable
                private InetAddress a() {
                    SocketAddress socketaddress = NetHandlerLoginServer.this.field_147333_a.func_74430_c();

                    return NetHandlerLoginServer.this.field_147327_f.func_190518_ac() && socketaddress instanceof InetSocketAddress ? ((InetSocketAddress) socketaddress).getAddress() : null;
                }
            });
            // Paper end
        }
    }

    // Spigot start
    public class LoginHandler {

        public void fireEvents() throws Exception {
                            String playerName = field_147337_i.getName();
                            java.net.InetAddress address = ((java.net.InetSocketAddress) field_147333_a.func_74430_c()).getAddress();
                            java.util.UUID uniqueId = field_147337_i.getId();
                            final org.bukkit.craftbukkit.CraftServer server = NetHandlerLoginServer.this.field_147327_f.server;

                            // Paper start
                            PlayerProfile profile = CraftPlayerProfile.asBukkitMirror(getGameProfile());
                            AsyncPlayerPreLoginEvent asyncEvent = new AsyncPlayerPreLoginEvent(playerName, address, uniqueId, profile);
                            server.getPluginManager().callEvent(asyncEvent);
                            profile = asyncEvent.getPlayerProfile();
                            profile.complete(true);
                            setGameProfile(CraftPlayerProfile.asAuthlib(profile));
                            playerName = field_147337_i.getName();
                            uniqueId = field_147337_i.getId();
                            // Paper end

                            if (PlayerPreLoginEvent.getHandlerList().getRegisteredListeners().length != 0) {
                                final PlayerPreLoginEvent event = new PlayerPreLoginEvent(playerName, address, uniqueId);
                                if (asyncEvent.getResult() != PlayerPreLoginEvent.Result.ALLOWED) {
                                    event.disallow(asyncEvent.getResult(), asyncEvent.getKickMessage());
                                }
                                Waitable<PlayerPreLoginEvent.Result> waitable = new Waitable<PlayerPreLoginEvent.Result>() {
                                    @Override
                                    protected PlayerPreLoginEvent.Result evaluate() {
                                        server.getPluginManager().callEvent(event);
                                        return event.getResult();
                                    }};

                                NetHandlerLoginServer.this.field_147327_f.processQueue.add(waitable);
                                if (waitable.get() != PlayerPreLoginEvent.Result.ALLOWED) {
                                    disconnect(event.getKickMessage());
                                    return;
                                }
                            } else {
                                if (asyncEvent.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) {
                                    disconnect(asyncEvent.getKickMessage());
                                    return;
                                }
                            }
                            // CraftBukkit end
                            NetHandlerLoginServer.field_147332_c.info("UUID of player {} is {}", NetHandlerLoginServer.this.field_147337_i.getName(), NetHandlerLoginServer.this.field_147337_i.getId());
                            NetHandlerLoginServer.this.field_147328_g = NetHandlerLoginServer.LoginState.READY_TO_ACCEPT;
                }
        }
    // Spigot end

    protected GameProfile func_152506_a(GameProfile gameprofile) {
        UUID uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + gameprofile.getName()).getBytes(StandardCharsets.UTF_8));

        return new GameProfile(uuid, gameprofile.getName());
    }

    static enum LoginState {

        HELLO, KEY, AUTHENTICATING, READY_TO_ACCEPT, DELAY_ACCEPT, ACCEPTED;

        private LoginState() {}
    }
}
