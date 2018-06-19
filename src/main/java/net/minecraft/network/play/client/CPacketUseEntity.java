package net.minecraft.network.play.client;

import java.io.IOException;
import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class CPacketUseEntity implements Packet<INetHandlerPlayServer> {

    private int field_149567_a; public int getEntityId() { return this.field_149567_a; } // Paper - add accessor
    private CPacketUseEntity.Action field_149566_b;
    private Vec3d field_179713_c;
    private EnumHand field_186995_d;

    public CPacketUseEntity() {}

    public CPacketUseEntity(Entity entity) {
        this.field_149567_a = entity.func_145782_y();
        this.field_149566_b = CPacketUseEntity.Action.ATTACK;
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_149567_a = packetdataserializer.func_150792_a();
        this.field_149566_b = (CPacketUseEntity.Action) packetdataserializer.func_179257_a(CPacketUseEntity.Action.class);
        if (this.field_149566_b == CPacketUseEntity.Action.INTERACT_AT) {
            this.field_179713_c = new Vec3d((double) packetdataserializer.readFloat(), (double) packetdataserializer.readFloat(), (double) packetdataserializer.readFloat());
        }

        if (this.field_149566_b == CPacketUseEntity.Action.INTERACT || this.field_149566_b == CPacketUseEntity.Action.INTERACT_AT) {
            this.field_186995_d = (EnumHand) packetdataserializer.func_179257_a(EnumHand.class);
        }

    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.func_150787_b(this.field_149567_a);
        packetdataserializer.func_179249_a((Enum) this.field_149566_b);
        if (this.field_149566_b == CPacketUseEntity.Action.INTERACT_AT) {
            packetdataserializer.writeFloat((float) this.field_179713_c.field_72450_a);
            packetdataserializer.writeFloat((float) this.field_179713_c.field_72448_b);
            packetdataserializer.writeFloat((float) this.field_179713_c.field_72449_c);
        }

        if (this.field_149566_b == CPacketUseEntity.Action.INTERACT || this.field_149566_b == CPacketUseEntity.Action.INTERACT_AT) {
            packetdataserializer.func_179249_a((Enum) this.field_186995_d);
        }

    }

    public void func_148833_a(INetHandlerPlayServer packetlistenerplayin) {
        packetlistenerplayin.func_147340_a(this);
    }

    @Nullable
    public Entity func_149564_a(World world) {
        return world.func_73045_a(this.field_149567_a);
    }

    public CPacketUseEntity.Action func_149565_c() {
        return this.field_149566_b;
    }

    public EnumHand func_186994_b() {
        return this.field_186995_d;
    }

    public Vec3d func_179712_b() {
        return this.field_179713_c;
    }

    public static enum Action {

        INTERACT, ATTACK, INTERACT_AT;

        private Action() {}
    }
}
