package net.minecraft.network.play.client;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class CPacketPlayerDigging implements Packet<INetHandlerPlayServer> {

    private BlockPos position;
    private EnumFacing facing;
    private CPacketPlayerDigging.Action action;

    public CPacketPlayerDigging() {}

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.action = (CPacketPlayerDigging.Action) packetdataserializer.readEnumValue(CPacketPlayerDigging.Action.class);
        this.position = packetdataserializer.readBlockPos();
        this.facing = EnumFacing.getFront(packetdataserializer.readUnsignedByte());
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeEnumValue((Enum) this.action);
        packetdataserializer.writeBlockPos(this.position);
        packetdataserializer.writeByte(this.facing.getIndex());
    }

    public void processPacket(INetHandlerPlayServer packetlistenerplayin) {
        packetlistenerplayin.processPlayerDigging(this);
    }

    public BlockPos getPosition() {
        return this.position;
    }

    public EnumFacing getFacing() {
        return this.facing;
    }

    public CPacketPlayerDigging.Action getAction() {
        return this.action;
    }

    public static enum Action {

        START_DESTROY_BLOCK, ABORT_DESTROY_BLOCK, STOP_DESTROY_BLOCK, DROP_ALL_ITEMS, DROP_ITEM, RELEASE_USE_ITEM, SWAP_HELD_ITEMS;

        private Action() {}
    }
}
