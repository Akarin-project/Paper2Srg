package net.minecraft.util;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.network.play.server.SPacketCooldown;


public class CooldownTrackerServer extends CooldownTracker {

    private final EntityPlayerMP field_185149_a;

    public CooldownTrackerServer(EntityPlayerMP entityplayer) {
        this.field_185149_a = entityplayer;
    }

    protected void func_185140_b(Item item, int i) {
        super.func_185140_b(item, i);
        this.field_185149_a.field_71135_a.func_147359_a(new SPacketCooldown(item, i));
    }

    protected void func_185146_c(Item item) {
        super.func_185146_c(item);
        this.field_185149_a.field_71135_a.func_147359_a(new SPacketCooldown(item, 0));
    }
}
