package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.world.border.WorldBorder;

public class SPacketWorldBorder implements Packet<INetHandlerPlayClient> {

    private SPacketWorldBorder.Action action;
    private int size;
    private double centerX;
    private double centerZ;
    private double targetSize;
    private double diameter;
    private long timeUntilTarget;
    private int warningTime;
    private int warningDistance;

    public SPacketWorldBorder() {}

    public SPacketWorldBorder(WorldBorder worldborder, SPacketWorldBorder.Action packetplayoutworldborder_enumworldborderaction) {
        this.action = packetplayoutworldborder_enumworldborderaction;
        // CraftBukkit start - multiply out nether border
        this.centerX = worldborder.getCenterX() * (worldborder.world.provider instanceof WorldProviderHell ? 8 : 1);
        this.centerZ = worldborder.getCenterZ() * (worldborder.world.provider instanceof WorldProviderHell ? 8 : 1);
        // CraftBukkit end
        this.diameter = worldborder.getDiameter();
        this.targetSize = worldborder.getTargetSize();
        this.timeUntilTarget = worldborder.getTimeUntilTarget();
        this.size = worldborder.getSize();
        this.warningDistance = worldborder.getWarningDistance();
        this.warningTime = worldborder.getWarningTime();
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.action = (SPacketWorldBorder.Action) packetdataserializer.readEnumValue(SPacketWorldBorder.Action.class);
        switch (this.action) {
        case SET_SIZE:
            this.targetSize = packetdataserializer.readDouble();
            break;

        case LERP_SIZE:
            this.diameter = packetdataserializer.readDouble();
            this.targetSize = packetdataserializer.readDouble();
            this.timeUntilTarget = packetdataserializer.readVarLong();
            break;

        case SET_CENTER:
            this.centerX = packetdataserializer.readDouble();
            this.centerZ = packetdataserializer.readDouble();
            break;

        case SET_WARNING_BLOCKS:
            this.warningDistance = packetdataserializer.readVarInt();
            break;

        case SET_WARNING_TIME:
            this.warningTime = packetdataserializer.readVarInt();
            break;

        case INITIALIZE:
            this.centerX = packetdataserializer.readDouble();
            this.centerZ = packetdataserializer.readDouble();
            this.diameter = packetdataserializer.readDouble();
            this.targetSize = packetdataserializer.readDouble();
            this.timeUntilTarget = packetdataserializer.readVarLong();
            this.size = packetdataserializer.readVarInt();
            this.warningDistance = packetdataserializer.readVarInt();
            this.warningTime = packetdataserializer.readVarInt();
        }

    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeEnumValue((Enum) this.action);
        switch (this.action) {
        case SET_SIZE:
            packetdataserializer.writeDouble(this.targetSize);
            break;

        case LERP_SIZE:
            packetdataserializer.writeDouble(this.diameter);
            packetdataserializer.writeDouble(this.targetSize);
            packetdataserializer.writeVarLong(this.timeUntilTarget);
            break;

        case SET_CENTER:
            packetdataserializer.writeDouble(this.centerX);
            packetdataserializer.writeDouble(this.centerZ);
            break;

        case SET_WARNING_BLOCKS:
            packetdataserializer.writeVarInt(this.warningDistance);
            break;

        case SET_WARNING_TIME:
            packetdataserializer.writeVarInt(this.warningTime);
            break;

        case INITIALIZE:
            packetdataserializer.writeDouble(this.centerX);
            packetdataserializer.writeDouble(this.centerZ);
            packetdataserializer.writeDouble(this.diameter);
            packetdataserializer.writeDouble(this.targetSize);
            packetdataserializer.writeVarLong(this.timeUntilTarget);
            packetdataserializer.writeVarInt(this.size);
            packetdataserializer.writeVarInt(this.warningDistance);
            packetdataserializer.writeVarInt(this.warningTime);
        }

    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.handleWorldBorder(this);
    }

    public static enum Action {

        SET_SIZE, LERP_SIZE, SET_CENTER, INITIALIZE, SET_WARNING_TIME, SET_WARNING_BLOCKS;

        private Action() {}
    }
}
