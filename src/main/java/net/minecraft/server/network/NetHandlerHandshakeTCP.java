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

    private final MinecraftServer field_147387_a;
    private final NetworkManager field_147386_b;
    private NetworkManager getNetworkManager() { return field_147386_b; } // Paper - OBFHELPER

    public NetHandlerHandshakeTCP(MinecraftServer minecraftserver, NetworkManager networkmanager) {
        this.field_147387_a = minecraftserver;
        this.field_147386_b = networkmanager;
    }

    public void func_147383_a(C00Handshake packethandshakinginsetprotocol) {
        switch (packethandshakinginsetprotocol.func_149594_c()) {
        case LOGIN:
            this.field_147386_b.func_150723_a(EnumConnectionState.LOGIN);
            TextComponentTranslation chatmessage;

            // CraftBukkit start - Connection throttle
            try {
                long currentTime = System.currentTimeMillis();
                long connectionThrottle = MinecraftServer.getServer().server.getConnectionThrottle();
                InetAddress address = ((java.net.InetSocketAddress) this.field_147386_b.func_74430_c()).getAddress();

                synchronized (throttleTracker) {
                    if (throttleTracker.containsKey(address) && !"127.0.0.1".equals(address.getHostAddress()) && currentTime - throttleTracker.get(address) < connectionThrottle) {
                        throttleTracker.put(address, currentTime);
                        chatmessage = new TextComponentTranslation("Connection throttled! Please wait before reconnecting.");
                        this.field_147386_b.func_179290_a(new SPacketDisconnect(chatmessage));
                        this.field_147386_b.func_150718_a(chatmessage);
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

            if (packethandshakinginsetprotocol.func_149595_d() > 340) {
                chatmessage = new TextComponentTranslation( java.text.MessageFormat.format( org.spigotmc.SpigotConfig.outdatedServerMessage.replaceAll("'", "''"), "1.12.2" ) ); // Spigot
                this.field_147386_b.func_179290_a(new SPacketDisconnect(chatmessage));
                this.field_147386_b.func_150718_a(chatmessage);
            } else if (packethandshakinginsetprotocol.func_149595_d() < 340) {
                chatmessage = new TextComponentTranslation( java.text.MessageFormat.format( org.spigotmc.SpigotConfig.outdatedClientMessage.replaceAll("'", "''"), "1.12.2" ) ); // Spigot
                this.field_147386_b.func_179290_a(new SPacketDisconnect(chatmessage));
                this.field_147386_b.func_150718_a(chatmessage);
            } else {
                this.field_147386_b.func_150719_a(new NetHandlerLoginServer(this.field_147387_a, this.field_147386_b));
                // Paper start - handshake event
                boolean proxyLogicEnabled = org.spigotmc.SpigotConfig.bungee;
                boolean handledByEvent = false;
                // Try and handle the handshake through the event
                if (com.destroystokyo.paper.event.player.PlayerHandshakeEvent.getHandlerList().getRegisteredListeners().length != 0) { // Hello? Can you hear me?
                    com.destroystokyo.paper.event.player.PlayerHandshakeEvent event = new com.destroystokyo.paper.event.player.PlayerHandshakeEvent(packethandshakinginsetprotocol.field_149598_b, !proxyLogicEnabled);
                    if (event.callEvent()) {
                        // If we've failed somehow, let the client know so and go no further.
                        if (event.isFailed()) {
                            chatmessage = new TextComponentTranslation(event.getFailMessage());
                            this.field_147386_b.func_179290_a(new SPacketDisconnect(chatmessage));
                            this.field_147386_b.func_150718_a(chatmessage);
                            return;
                        }

                        packethandshakinginsetprotocol.field_149598_b = event.getServerHostname();
                        this.field_147386_b.field_150743_l = new java.net.InetSocketAddress(event.getSocketAddressHostname(), ((java.net.InetSocketAddress) this.field_147386_b.func_74430_c()).getPort());
                        this.field_147386_b.spoofedUUID = event.getUniqueId();
                        this.field_147386_b.spoofedProfile = gson.fromJson(event.getPropertiesJson(), com.mojang.authlib.properties.Property[].class);
                        handledByEvent = true; // Hooray, we did it!
                    }
                }
                // Don't try and handle default logic if it's been handled by the event.
                if (!handledByEvent && proxyLogicEnabled) {
                // Paper end
                // Spigot Start
                //if (org.spigotmc.SpigotConfig.bungee) { // Paper - comment out, we check above!
                    String[] split = packethandshakinginsetprotocol.field_149598_b.split("\00");
                    if ( split.length == 3 || split.length == 4 ) {
                        packethandshakinginsetprotocol.field_149598_b = split[0];
                        field_147386_b.field_150743_l = new java.net.InetSocketAddress(split[1], ((java.net.InetSocketAddress) field_147386_b.func_74430_c()).getPort());
                        field_147386_b.spoofedUUID = com.mojang.util.UUIDTypeAdapter.fromString( split[2] );
                    } else
                    {
                        chatmessage = new TextComponentTranslation("If you wish to use IP forwarding, please enable it in your BungeeCord config as well!");
                        this.field_147386_b.func_179290_a(new SPacketDisconnect(chatmessage));
                        this.field_147386_b.func_150718_a(chatmessage);
                        return;
                    }
                    if ( split.length == 4 )
                    {
                        field_147386_b.spoofedProfile = gson.fromJson(split[3], com.mojang.authlib.properties.Property[].class);
                    }
                }
                // Spigot End
                ((NetHandlerLoginServer) this.field_147386_b.func_150729_e()).hostname = packethandshakinginsetprotocol.field_149598_b + ":" + packethandshakinginsetprotocol.field_149599_c; // CraftBukkit - set hostname
            }
            break;

        case STATUS:
            this.field_147386_b.func_150723_a(EnumConnectionState.STATUS);
            this.field_147386_b.func_150719_a(new NetHandlerStatusServer(this.field_147387_a, this.field_147386_b));
            break;

        default:
            throw new UnsupportedOperationException("Invalid intention " + packethandshakinginsetprotocol.func_149594_c());
        }

        // Paper start - NetworkClient implementation
        this.getNetworkManager().protocolVersion = packethandshakinginsetprotocol.getProtocolVersion();
        this.getNetworkManager().virtualHost = com.destroystokyo.paper.network.PaperNetworkClient.prepareVirtualHost(packethandshakinginsetprotocol.field_149598_b, packethandshakinginsetprotocol.field_149599_c);
        // Paper end
    }

    public void func_147231_a(ITextComponent ichatbasecomponent) {}
}
