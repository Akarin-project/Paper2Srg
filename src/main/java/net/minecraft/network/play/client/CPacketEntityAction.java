package net.minecraft.network.play.client;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketEntityAction implements Packet<INetHandlerPlayServer> {

    private int entityID;
    private CPacketEntityAction.Action action;
    private int auxData;

    public CPacketEntityAction() {}

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.entityID = packetdataserializer.readVarInt();
        this.action = (CPacketEntityAction.Action) packetdataserializer.readEnumValue(CPacketEntityAction.Action.class);
        this.auxData = packetdataserializer.readVarInt();
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeVarInt(this.entityID);
        packetdataserializer.writeEnumValue((Enum) this.action);
        packetdataserializer.writeVarInt(this.auxData);
    }

    public void processPacket(INetHandlerPlayServer packetlistenerplayin) {
        packetlistenerplayin.processEntityAction(this);
    }

    public CPacketEntityAction.Action getAction() {
        return this.action;
    }

    public int getAuxData() {
        return this.auxData;
    }

    public static enum Action {

        START_SNEAKING, STOP_SNEAKING, STOP_SLEEPING, START_SPRINTING, STOP_SPRINTING, START_RIDING_JUMP, STOP_RIDING_JUMP, OPEN_INVENTORY, START_FALL_FLYING;

        private Action() {}
    }
}
