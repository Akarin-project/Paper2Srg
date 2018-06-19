package com.destroystokyo.paper.network;

import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import org.bukkit.entity.Player;
import org.bukkit.util.CachedServerIcon;

import javax.annotation.Nullable;

class PaperServerListPingEventImpl extends PaperServerListPingEvent {

    private final MinecraftServer server;

    PaperServerListPingEventImpl(MinecraftServer server, StatusClient client, int protocolVersion, @Nullable CachedServerIcon icon) {
        super(client, server.func_71273_Y(), server.getPlayerCount(), server.getMaxPlayers(),
                server.getServerModName() + ' ' + server.func_71249_w(), protocolVersion, icon);
        this.server = server;
    }

    @Override
    protected final Object[] getOnlinePlayers() {
        return this.server.func_184103_al().field_72404_b.toArray();
    }

    @Override
    protected final Player getBukkitPlayer(Object player) {
        return ((EntityPlayerMP) player).getBukkitEntity();
    }

}
