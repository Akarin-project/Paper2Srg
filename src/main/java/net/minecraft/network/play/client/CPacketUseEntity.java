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

    private int entityId; public int getEntityId() { return this.entityId; } // Paper - add accessor
    private CPacketUseEntity.Action action;
    private Vec3d hitVec;
    private EnumHand hand;

    public CPacketUseEntity() {}

    public CPacketUseEntity(Entity entity) {
        this.entityId = entity.getEntityId();
        this.action = CPacketUseEntity.Action.ATTACK;
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.entityId = packetdataserializer.readVarInt();
        this.action = (CPacketUseEntity.Action) packetdataserializer.readEnumValue(CPacketUseEntity.Action.class);
        if (this.action == CPacketUseEntity.Action.INTERACT_AT) {
            this.hitVec = new Vec3d((double) packetdataserializer.readFloat(), (double) packetdataserializer.readFloat(), (double) packetdataserializer.readFloat());
        }

        if (this.action == CPacketUseEntity.Action.INTERACT || this.action == CPacketUseEntity.Action.INTERACT_AT) {
            this.hand = (EnumHand) packetdataserializer.readEnumValue(EnumHand.class);
        }

    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeVarInt(this.entityId);
        packetdataserializer.writeEnumValue((Enum) this.action);
        if (this.action == CPacketUseEntity.Action.INTERACT_AT) {
            packetdataserializer.writeFloat((float) this.hitVec.x);
            packetdataserializer.writeFloat((float) this.hitVec.y);
            packetdataserializer.writeFloat((float) this.hitVec.z);
        }

        if (this.action == CPacketUseEntity.Action.INTERACT || this.action == CPacketUseEntity.Action.INTERACT_AT) {
            packetdataserializer.writeEnumValue((Enum) this.hand);
        }

    }

    public void processPacket(INetHandlerPlayServer packetlistenerplayin) {
        packetlistenerplayin.processUseEntity(this);
    }

    @Nullable
    public Entity getEntityFromWorld(World world) {
        return world.getEntityByID(this.entityId);
    }

    public CPacketUseEntity.Action getAction() {
        return this.action;
    }

    public EnumHand getHand() {
        return this.hand;
    }

    public Vec3d getHitVec() {
        return this.hitVec;
    }

    public static enum Action {

        INTERACT, ATTACK, INTERACT_AT;

        private Action() {}
    }
}
