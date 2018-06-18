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

    private static final AtomicInteger AUTHENTICATOR_THREAD_ID = new AtomicInteger(0);
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Random RANDOM = new Random();
    private final byte[] verifyToken = new byte[4];
    private final MinecraftServer server;
    public final NetworkManager networkManager;
    private NetHandlerLoginServer.LoginState currentLoginState;
    private int connectionTimer;
    private GameProfile loginGameProfile; private void setGameProfile(GameProfile profile) { loginGameProfile = profile; } private GameProfile getGameProfile() { return loginGameProfile; } // Paper - OBFHELPER
    private final String serverId;
    private SecretKey secretKey;
    private EntityPlayerMP player;
    public String hostname = ""; // CraftBukkit - add field

    public NetHandlerLoginServer(MinecraftServer minecraftserver, NetworkManager networkmanager) {
        this.currentLoginState = NetHandlerLoginServer.LoginState.HELLO;
        this.serverId = "";
        this.server = minecraftserver;
        this.networkManager = networkmanager;
        NetHandlerLoginServer.RANDOM.nextBytes(this.verifyToken);
    }

    public void update() {
        // Paper start - Do not allow logins while the server is shutting down
        if (!MinecraftServer.getServer().isServerRunning()) {
            this.disconnect(new TextComponentTranslation(org.spigotmc.SpigotConfig.restartMessage));
            return;
        }
        // Paper end
        if (this.currentLoginState == NetHandlerLoginServer.LoginState.READY_TO_ACCEPT) {
            // Paper start - prevent logins to be processed even though disconnect was called
            if (networkManager.isChannelOpen()) {
                this.tryAcceptPlayer();
            }
            // Paper end
        } else if (this.currentLoginState == NetHandlerLoginServer.LoginState.DELAY_ACCEPT) {
            EntityPlayerMP entityplayer = this.server.getPlayerList().getPlayerByUUID(this.loginGameProfile.getId());

            if (entityplayer == null) {
                this.currentLoginState = NetHandlerLoginServer.LoginState.READY_TO_ACCEPT;
                this.server.getPlayerList().initializeConnectionToPlayer(this.networkManager, this.player);
                this.player = null;
            }
        }

        if (this.connectionTimer++ == 600) {
            this.disconnect(new TextComponentTranslation("multiplayer.disconnect.slow_login", new Object[0]));
        }

    }

    // CraftBukkit start
    @Deprecated
    public void disconnect(String s) {
        try {
            ITextComponent ichatbasecomponent = new TextComponentString(s);
            NetHandlerLoginServer.LOGGER.info("Disconnecting {}: {}", this.getConnectionInfo(), s);
            this.networkManager.sendPacket(new SPacketDisconnect(ichatbasecomponent));
            this.networkManager.closeChannel(ichatbasecomponent);
        } catch (Exception exception) {
            NetHandlerLoginServer.LOGGER.error("Error whilst disconnecting player", exception);
        }
    }
    // CraftBukkit end

    public void disconnect(ITextComponent ichatbasecomponent) {
        try {
            NetHandlerLoginServer.LOGGER.info("Disconnecting {}: {}", this.getConnectionInfo(), ichatbasecomponent.getUnformattedText());
            this.networkManager.sendPacket(new SPacketDisconnect(ichatbasecomponent));
            this.networkManager.closeChannel(ichatbasecomponent);
        } catch (Exception exception) {
            NetHandlerLoginServer.LOGGER.error("Error whilst disconnecting player", exception);
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
        if ( networkManager.spoofedUUID != null )
        {
            uuid = networkManager.spoofedUUID;
        } else
        {
            uuid = UUID.nameUUIDFromBytes( ( "OfflinePlayer:" + this.loginGameProfile.getName() ).getBytes( StandardCharsets.UTF_8 ) );
        }

        this.loginGameProfile = new GameProfile( uuid, this.loginGameProfile.getName() );

        if (networkManager.spoofedProfile != null)
        {
            for ( com.mojang.authlib.properties.Property property : networkManager.spoofedProfile )
            {
                this.loginGameProfile.getProperties().put( property.getName(), property );
            }
        }
    }
    // Spigot end

    public void tryAcceptPlayer() {
        // Spigot start - Moved to initUUID
        /*
        if (!this.i.isComplete()) {
            this.i = this.a(this.i);
        }
        */
        // Spigot end

        // CraftBukkit start - fire PlayerLoginEvent
        EntityPlayerMP s = this.server.getPlayerList().attemptLogin(this, this.loginGameProfile, hostname);

        if (s == null) {
            // this.disconnect(new ChatMessage(s, new Object[0]));
            // CraftBukkit end
        } else {
            this.currentLoginState = NetHandlerLoginServer.LoginState.ACCEPTED;
            if (this.server.getNetworkCompressionThreshold() >= 0 && !this.networkManager.isLocalChannel()) {
                this.networkManager.sendPacket(new SPacketEnableCompression(this.server.getNetworkCompressionThreshold()), new ChannelFutureListener() {
                    public void a(ChannelFuture channelfuture) throws Exception {
                        NetHandlerLoginServer.this.networkManager.setCompressionThreshold(NetHandlerLoginServer.this.server.getNetworkCompressionThreshold());
                    }

                    public void operationComplete(ChannelFuture future) throws Exception { // CraftBukkit - fix decompile error
                        this.a((ChannelFuture) future);
                    }
                }, new GenericFutureListener[0]);
            }

            this.networkManager.sendPacket(new SPacketLoginSuccess(this.loginGameProfile));
            EntityPlayerMP entityplayer = this.server.getPlayerList().getPlayerByUUID(this.loginGameProfile.getId());

            if (entityplayer != null) {
                this.currentLoginState = NetHandlerLoginServer.LoginState.DELAY_ACCEPT;
                this.player = this.server.getPlayerList().processLogin(this.loginGameProfile, s); // CraftBukkit - add player reference
            } else {
                this.server.getPlayerList().initializeConnectionToPlayer(this.networkManager, this.server.getPlayerList().processLogin(this.loginGameProfile, s)); // CraftBukkit - add player reference
            }
        }

    }

    public void onDisconnect(ITextComponent ichatbasecomponent) {
        NetHandlerLoginServer.LOGGER.info("{} lost connection: {}", this.getConnectionInfo(), ichatbasecomponent.getUnformattedText());
    }

    public String getConnectionInfo() {
        return this.loginGameProfile != null ? this.loginGameProfile + " (" + this.networkManager.getRemoteAddress() + ")" : String.valueOf(this.networkManager.getRemoteAddress());
    }

    public void processLoginStart(CPacketLoginStart packetlogininstart) {
        Validate.validState(this.currentLoginState == NetHandlerLoginServer.LoginState.HELLO, "Unexpected hello packet", new Object[0]);
        this.loginGameProfile = packetlogininstart.getProfile();
        if (this.server.isServerInOnlineMode() && !this.networkManager.isLocalChannel()) {
            this.currentLoginState = NetHandlerLoginServer.LoginState.KEY;
            this.networkManager.sendPacket(new SPacketEncryptionRequest("", this.server.getKeyPair().getPublic(), this.verifyToken));
        } else {
            // Spigot start
            // Paper start - Cache authenticator threads
            authenticatorPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        initUUID();
                        new LoginHandler().fireEvents();
                        NetHandlerLoginServer.this.currentLoginState = NetHandlerLoginServer.LoginState.READY_TO_ACCEPT;
                    } catch (Exception ex) {
                        disconnect("Failed to verify username!");
                        server.server.getLogger().log(java.util.logging.Level.WARNING, "Exception verifying " + loginGameProfile.getName(), ex);
                    }
                }
            });
            // Paper end
            // Spigot end
        }

    }

    public void processEncryptionResponse(CPacketEncryptionResponse packetlogininencryptionbegin) {
        Validate.validState(this.currentLoginState == NetHandlerLoginServer.LoginState.KEY, "Unexpected key packet", new Object[0]);
        PrivateKey privatekey = this.server.getKeyPair().getPrivate();

        if (!Arrays.equals(this.verifyToken, packetlogininencryptionbegin.getVerifyToken(privatekey))) {
            throw new IllegalStateException("Invalid nonce!");
        } else {
            this.secretKey = packetlogininencryptionbegin.getSecretKey(privatekey);
            this.currentLoginState = NetHandlerLoginServer.LoginState.AUTHENTICATING;
            this.networkManager.enableEncryption(this.secretKey);
            // Paper start - Cache authenticator threads
            authenticatorPool.execute(new Runnable() {
                public void run() {
                    GameProfile gameprofile = NetHandlerLoginServer.this.loginGameProfile;

                    try {
                        String s = (new BigInteger(CryptManager.getServerIdHash("", NetHandlerLoginServer.this.server.getKeyPair().getPublic(), NetHandlerLoginServer.this.secretKey))).toString(16);

                        NetHandlerLoginServer.this.loginGameProfile = NetHandlerLoginServer.this.server.getMinecraftSessionService().hasJoinedServer(new GameProfile((UUID) null, gameprofile.getName()), s, this.a());
                        if (NetHandlerLoginServer.this.loginGameProfile != null) {
                            // CraftBukkit start - fire PlayerPreLoginEvent
                            if (!networkManager.isChannelOpen()) {
                                return;
                            }

                            new LoginHandler().fireEvents();
                        } else if (NetHandlerLoginServer.this.server.isSinglePlayer()) {
                            NetHandlerLoginServer.LOGGER.warn("Failed to verify username but will let them in anyway!");
                            NetHandlerLoginServer.this.loginGameProfile = NetHandlerLoginServer.this.getOfflineProfile(gameprofile);
                            NetHandlerLoginServer.this.currentLoginState = NetHandlerLoginServer.LoginState.READY_TO_ACCEPT;
                        } else {
                            NetHandlerLoginServer.this.disconnect(new TextComponentTranslation("multiplayer.disconnect.unverified_username", new Object[0]));
                            NetHandlerLoginServer.LOGGER.error("Username \'{}\' tried to join with an invalid session", gameprofile.getName());
                        }
                    } catch (AuthenticationUnavailableException authenticationunavailableexception) {
                        if (NetHandlerLoginServer.this.server.isSinglePlayer()) {
                            NetHandlerLoginServer.LOGGER.warn("Authentication servers are down but will let them in anyway!");
                            NetHandlerLoginServer.this.loginGameProfile = NetHandlerLoginServer.this.getOfflineProfile(gameprofile);
                            NetHandlerLoginServer.this.currentLoginState = NetHandlerLoginServer.LoginState.READY_TO_ACCEPT;
                        } else {
                            // Paper start
                            if (com.destroystokyo.paper.PaperConfig.authenticationServersDownKickMessage != null) {
                                NetHandlerLoginServer.this.disconnect(new TextComponentString(com.destroystokyo.paper.PaperConfig.authenticationServersDownKickMessage));
                            } else // Paper end
                            NetHandlerLoginServer.this.disconnect(new TextComponentTranslation("multiplayer.disconnect.authservers_down", new Object[0]));
                            NetHandlerLoginServer.LOGGER.error("Couldn\'t verify username because servers are unavailable");
                        }
                        // CraftBukkit start - catch all exceptions
                    } catch (Exception exception) {
                        disconnect("Failed to verify username!");
                        server.server.getLogger().log(java.util.logging.Level.WARNING, "Exception verifying " + gameprofile.getName(), exception);
                        // CraftBukkit end
                    }

                }

                @Nullable
                private InetAddress a() {
                    SocketAddress socketaddress = NetHandlerLoginServer.this.networkManager.getRemoteAddress();

                    return NetHandlerLoginServer.this.server.getPreventProxyConnections() && socketaddress instanceof InetSocketAddress ? ((InetSocketAddress) socketaddress).getAddress() : null;
                }
            });
            // Paper end
        }
    }

    // Spigot start
    public class LoginHandler {

        public void fireEvents() throws Exception {
                            String playerName = loginGameProfile.getName();
                            java.net.InetAddress address = ((java.net.InetSocketAddress) networkManager.getRemoteAddress()).getAddress();
                            java.util.UUID uniqueId = loginGameProfile.getId();
                            final org.bukkit.craftbukkit.CraftServer server = NetHandlerLoginServer.this.server.server;

                            // Paper start
                            PlayerProfile profile = CraftPlayerProfile.asBukkitMirror(getGameProfile());
                            AsyncPlayerPreLoginEvent asyncEvent = new AsyncPlayerPreLoginEvent(playerName, address, uniqueId, profile);
                            server.getPluginManager().callEvent(asyncEvent);
                            profile = asyncEvent.getPlayerProfile();
                            profile.complete(true);
                            setGameProfile(CraftPlayerProfile.asAuthlib(profile));
                            playerName = loginGameProfile.getName();
                            uniqueId = loginGameProfile.getId();
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

                                NetHandlerLoginServer.this.server.processQueue.add(waitable);
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
                            NetHandlerLoginServer.LOGGER.info("UUID of player {} is {}", NetHandlerLoginServer.this.loginGameProfile.getName(), NetHandlerLoginServer.this.loginGameProfile.getId());
                            NetHandlerLoginServer.this.currentLoginState = NetHandlerLoginServer.LoginState.READY_TO_ACCEPT;
                }
        }
    // Spigot end

    protected GameProfile getOfflineProfile(GameProfile gameprofile) {
        UUID uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + gameprofile.getName()).getBytes(StandardCharsets.UTF_8));

        return new GameProfile(uuid, gameprofile.getName());
    }

    static enum LoginState {

        HELLO, KEY, AUTHENTICATING, READY_TO_ACCEPT, DELAY_ACCEPT, ACCEPTED;

        private LoginState() {}
    }
}
