package net.minecraft.server.network;

// CraftBukkit start
import java.net.InetAddress;
import java.util.HashMap;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.handshake.INetHandlerHandshakeServer;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.server.SPacketDisconnect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

// CraftBukkit end

public class NetHandlerHandshakeTCP implements INetHandlerHandshakeServer {

    private static final com.google.gson.Gson gson = new com.google.gson.Gson(); // Spigot
    // CraftBukkit start - add fields
    private static final HashMap<InetAddress, Long> throttleTracker = new HashMap<InetAddress, Long>();
    private static int throttleCounter = 0;
    // CraftBukkit end

    private final MinecraftServer server;
    private final NetworkManager networkManager;
    private NetworkManager getNetworkManager() { return networkManager; } // Paper - OBFHELPER

    public NetHandlerHandshakeTCP(MinecraftServer minecraftserver, NetworkManager networkmanager) {
        this.server = minecraftserver;
        this.networkManager = networkmanager;
    }

    public void processHandshake(C00Handshake packethandshakinginsetprotocol) {
        switch (packethandshakinginsetprotocol.getRequestedState()) {
        case LOGIN:
            this.networkManager.setConnectionState(EnumConnectionState.LOGIN);
            TextComponentTranslation chatmessage;

            // CraftBukkit start - Connection throttle
            try {
                long currentTime = System.currentTimeMillis();
                long connectionThrottle = MinecraftServer.getServer().server.getConnectionThrottle();
                InetAddress address = ((java.net.InetSocketAddress) this.networkManager.getRemoteAddress()).getAddress();

                synchronized (throttleTracker) {
                    if (throttleTracker.containsKey(address) && !"127.0.0.1".equals(address.getHostAddress()) && currentTime - throttleTracker.get(address) < connectionThrottle) {
                        throttleTracker.put(address, currentTime);
                        chatmessage = new TextComponentTranslation("Connection throttled! Please wait before reconnecting.");
                        this.networkManager.sendPacket(new SPacketDisconnect(chatmessage));
                        this.networkManager.closeChannel(chatmessage);
                        return;
                    }

                    throttleTracker.put(address, currentTime);
                    throttleCounter++;
                    if (throttleCounter > 200) {
                        throttleCounter = 0;

                        // Cleanup stale entries
                        java.util.Iterator iter = throttleTracker.entrySet().iterator();
                        while (iter.hasNext()) {
                            java.util.Map.Entry<InetAddress, Long> entry = (java.util.Map.Entry) iter.next();
                            if (entry.getValue() > connectionThrottle) {
                                iter.remove();
                            }
                        }
                    }
                }
            } catch (Throwable t) {
                org.apache.logging.log4j.LogManager.getLogger().debug("Failed to check connection throttle", t);
            }
            // CraftBukkit end

            if (packethandshakinginsetprotocol.getProtocolVersion() > 340) {
                chatmessage = new TextComponentTranslation( java.text.MessageFormat.format( org.spigotmc.SpigotConfig.outdatedServerMessage.replaceAll("'", "''"), "1.12.2" ) ); // Spigot
                this.networkManager.sendPacket(new SPacketDisconnect(chatmessage));
                this.networkManager.closeChannel(chatmessage);
            } else if (packethandshakinginsetprotocol.getProtocolVersion() < 340) {
                chatmessage = new TextComponentTranslation( java.text.MessageFormat.format( org.spigotmc.SpigotConfig.outdatedClientMessage.replaceAll("'", "''"), "1.12.2" ) ); // Spigot
                this.networkManager.sendPacket(new SPacketDisconnect(chatmessage));
                this.networkManager.closeChannel(chatmessage);
            } else {
                this.networkManager.setNetHandler(new NetHandlerLoginServer(this.server, this.networkManager));
                // Paper start - handshake event
                boolean proxyLogicEnabled = org.spigotmc.SpigotConfig.bungee;
                boolean handledByEvent = false;
                // Try and handle the handshake through the event
                if (com.destroystokyo.paper.event.player.PlayerHandshakeEvent.getHandlerList().getRegisteredListeners().length != 0) { // Hello? Can you hear me?
                    com.destroystokyo.paper.event.player.PlayerHandshakeEvent event = new com.destroystokyo.paper.event.player.PlayerHandshakeEvent(packethandshakinginsetprotocol.ip, !proxyLogicEnabled);
                    if (event.callEvent()) {
                        // If we've failed somehow, let the client know so and go no further.
                        if (event.isFailed()) {
                            chatmessage = new TextComponentTranslation(event.getFailMessage());
                            this.networkManager.sendPacket(new SPacketDisconnect(chatmessage));
                            this.networkManager.closeChannel(chatmessage);
                            return;
                        }

                        packethandshakinginsetprotocol.ip = event.getServerHostname();
                        this.networkManager.socketAddress = new java.net.InetSocketAddress(event.getSocketAddressHostname(), ((java.net.InetSocketAddress) this.networkManager.getRemoteAddress()).getPort());
                        this.networkManager.spoofedUUID = event.getUniqueId();
                        this.networkManager.spoofedProfile = gson.fromJson(event.getPropertiesJson(), com.mojang.authlib.properties.Property[].class);
                        handledByEvent = true; // Hooray, we did it!
                    }
                }
                // Don't try and handle default logic if it's been handled by the event.
                if (!handledByEvent && proxyLogicEnabled) {
                // Paper end
                // Spigot Start
                //if (org.spigotmc.SpigotConfig.bungee) { // Paper - comment out, we check above!
                    String[] split = packethandshakinginsetprotocol.ip.split("\00");
                    if ( split.length == 3 || split.length == 4 ) {
                        packethandshakinginsetprotocol.ip = split[0];
                        networkManager.socketAddress = new java.net.InetSocketAddress(split[1], ((java.net.InetSocketAddress) networkManager.getRemoteAddress()).getPort());
                        networkManager.spoofedUUID = com.mojang.util.UUIDTypeAdapter.fromString( split[2] );
                    } else
                    {
                        chatmessage = new TextComponentTranslation("If you wish to use IP forwarding, please enable it in your BungeeCord config as well!");
                        this.networkManager.sendPacket(new SPacketDisconnect(chatmessage));
                        this.networkManager.closeChannel(chatmessage);
                        return;
                    }
                    if ( split.length == 4 )
                    {
                        networkManager.spoofedProfile = gson.fromJson(split[3], com.mojang.authlib.properties.Property[].class);
                    }
                }
                // Spigot End
                ((NetHandlerLoginServer) this.networkManager.getNetHandler()).hostname = packethandshakinginsetprotocol.ip + ":" + packethandshakinginsetprotocol.port; // CraftBukkit - set hostname
            }
            break;

        case STATUS:
            this.networkManager.setConnectionState(EnumConnectionState.STATUS);
            this.networkManager.setNetHandler(new NetHandlerStatusServer(this.server, this.networkManager));
            break;

        default:
            throw new UnsupportedOperationException("Invalid intention " + packethandshakinginsetprotocol.getRequestedState());
        }

        // Paper start - NetworkClient implementation
        this.getNetworkManager().protocolVersion = packethandshakinginsetprotocol.getProtocolVersion();
        this.getNetworkManager().virtualHost = com.destroystokyo.paper.network.PaperNetworkClient.prepareVirtualHost(packethandshakinginsetprotocol.ip, packethandshakinginsetprotocol.port);
        // Paper end
    }

    public void onDisconnect(ITextComponent ichatbasecomponent) {}
}
