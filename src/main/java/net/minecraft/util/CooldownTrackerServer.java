package net.minecraft.util;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.network.play.server.SPacketCooldown;


public class CooldownTrackerServer extends CooldownTracker {

    private final EntityPlayerMP player;

    public CooldownTrackerServer(EntityPlayerMP entityplayer) {
        this.player = entityplayer;
    }

    protected void notifyOnSet(Item item, int i) {
        super.notifyOnSet(item, i);
        this.player.connection.sendPacket(new SPacketCooldown(item, i));
    }

    protected void notifyOnRemove(Item item) {
        super.notifyOnRemove(item);
        this.player.connection.sendPacket(new SPacketCooldown(item, 0));
    }
}
