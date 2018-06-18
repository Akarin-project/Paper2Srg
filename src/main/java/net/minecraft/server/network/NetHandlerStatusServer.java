package net.minecraft.server.network;

// CraftBukkit start


import net.minecraft.network.NetworkManager;
import net.minecraft.network.status.INetHandlerStatusServer;
import net.minecraft.network.status.client.CPacketPing;
import net.minecraft.network.status.client.CPacketServerQuery;
import net.minecraft.network.status.server.SPacketPong;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

// CraftBukkit end

public class NetHandlerStatusServer implements INetHandlerStatusServer {

    private static final ITextComponent EXIT_MESSAGE = new TextComponentString("Status request has been handled.");
    private final MinecraftServer server;
    private final NetworkManager networkManager;
    private boolean handled;

    public NetHandlerStatusServer(MinecraftServer minecraftserver, NetworkManager networkmanager) {
        this.server = minecraftserver;
        this.networkManager = networkmanager;
    }

    public void onDisconnect(ITextComponent ichatbasecomponent) {}

    public void processServerQuery(CPacketServerQuery packetstatusinstart) {
        if (this.handled) {
            this.networkManager.closeChannel(NetHandlerStatusServer.EXIT_MESSAGE);
        } else {
            this.handled = true;
            // Paper start - Replace everything
            /*
            // CraftBukkit start
            // this.networkManager.sendPacket(new PacketStatusOutServerInfo(this.minecraftServer.getServerPing()));
            final Object[] players = minecraftServer.getPlayerList().players.toArray();
            class ServerListPingEvent extends org.bukkit.event.server.ServerListPingEvent {

                CraftIconCache icon = minecraftServer.server.getServerIcon();

                ServerListPingEvent() {
                    super(((InetSocketAddress) networkManager.getSocketAddress()).getAddress(), minecraftServer.getMotd(), minecraftServer.getPlayerList().getMaxPlayers());
                }

                @Override
                public void setServerIcon(org.bukkit.util.CachedServerIcon icon) {
                    if (!(icon instanceof CraftIconCache)) {
                        throw new IllegalArgumentException(icon + " was not created by " + org.bukkit.craftbukkit.CraftServer.class);
                    }
                    this.icon = (CraftIconCache) icon;
                }

                @Override
                public Iterator<Player> iterator() throws UnsupportedOperationException {
                    return new Iterator<Player>() {
                        int i;
                        int ret = Integer.MIN_VALUE;
                        EntityPlayer player;

                        @Override
                        public boolean hasNext() {
                            if (player != null) {
                                return true;
                            }
                            final Object[] currentPlayers = players;
                            for (int length = currentPlayers.length, i = this.i; i < length; i++) {
                                final EntityPlayer player = (EntityPlayer) currentPlayers[i];
                                if (player != null) {
                                    this.i = i + 1;
                                    this.player = player;
                                    return true;
                                }
                            }
                            return false;
                        }

                        @Override
                        public Player next() {
                            if (!hasNext()) {
                                throw new java.util.NoSuchElementException();
                            }
                            final EntityPlayer player = this.player;
                            this.player = null;
                            this.ret = this.i - 1;
                            return player.getBukkitEntity();
                        }

                        @Override
                        public void remove() {
                            final Object[] currentPlayers = players;
                            final int i = this.ret;
                            if (i < 0 || currentPlayers[i] == null) {
                                throw new IllegalStateException();
                            }
                            currentPlayers[i] = null;
                        }
                    };
                }
            }

            ServerListPingEvent event = new ServerListPingEvent();
            this.minecraftServer.server.getPluginManager().callEvent(event);

            java.util.List<GameProfile> profiles = new java.util.ArrayList<GameProfile>(players.length);
            for (Object player : players) {
                if (player != null) {
                    profiles.add(((EntityPlayer) player).getProfile());
                }
            }

            ServerPing.ServerPingPlayerSample playerSample = new ServerPing.ServerPingPlayerSample(event.getMaxPlayers(), profiles.size());
            // Spigot Start
            if ( !profiles.isEmpty() )
            {
                java.util.Collections.shuffle( profiles ); // This sucks, its inefficient but we have no simple way of doing it differently
                profiles = profiles.subList( 0, Math.min( profiles.size(), org.spigotmc.SpigotConfig.playerSample ) ); // Cap the sample to n (or less) displayed players, ie: Vanilla behaviour
            }
            // Spigot End
            playerSample.a(profiles.toArray(new GameProfile[profiles.size()]));

            ServerPing ping = new ServerPing();
            ping.setFavicon(event.icon.value);
            ping.setMOTD(new ChatComponentText(event.getMotd()));
            ping.setPlayerSample(playerSample);
            int version = minecraftServer.getServerPing().getServerData().getProtocolVersion();
            ping.setServerInfo(new ServerPing.ServerData(minecraftServer.getServerModName() + " " + minecraftServer.getVersion(), version));

            this.networkManager.sendPacket(new PacketStatusOutServerInfo(ping));
            */
            com.destroystokyo.paper.network.StandardPaperServerListPingEventImpl.processRequest(this.server, this.networkManager);
            // Paper end
        }
        // CraftBukkit end
    }

    public void processPing(CPacketPing packetstatusinping) {
        this.networkManager.sendPacket(new SPacketPong(packetstatusinping.getClientTime()));
        this.networkManager.closeChannel(NetHandlerStatusServer.EXIT_MESSAGE);
    }
}
