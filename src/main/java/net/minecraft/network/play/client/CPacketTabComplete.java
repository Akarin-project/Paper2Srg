package net.minecraft.network.play.client;

import java.io.IOException;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.util.math.BlockPos;

public class CPacketTabComplete implements Packet<INetHandlerPlayServer> {

    private String message;
    private boolean hasTargetBlock;
    @Nullable
    private BlockPos targetBlock;

    public CPacketTabComplete() {}

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.message = packetdataserializer.readString(32767);
        this.hasTargetBlock = packetdataserializer.readBoolean();
        boolean flag = packetdataserializer.readBoolean();

        if (flag) {
            this.targetBlock = packetdataserializer.readBlockPos();
        }

    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeString(StringUtils.substring(this.message, 0, 32767));
        packetdataserializer.writeBoolean(this.hasTargetBlock);
        boolean flag = this.targetBlock != null;

        packetdataserializer.writeBoolean(flag);
        if (flag) {
            packetdataserializer.writeBlockPos(this.targetBlock);
        }

    }

    public void processPacket(INetHandlerPlayServer packetlistenerplayin) {
        packetlistenerplayin.processTabComplete(this);
    }

    public String getMessage() {
        return this.message;
    }

    @Nullable
    public BlockPos getTargetBlock() {
        return this.targetBlock;
    }

    public boolean hasTargetBlock() {
        return this.hasTargetBlock;
    }
}
