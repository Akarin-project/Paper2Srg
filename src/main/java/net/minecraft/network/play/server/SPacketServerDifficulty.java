package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.EnumDifficulty;

public class SPacketServerDifficulty implements Packet<INetHandlerPlayClient> {

    private EnumDifficulty difficulty;
    private boolean difficultyLocked;

    public SPacketServerDifficulty() {}

    public SPacketServerDifficulty(EnumDifficulty enumdifficulty, boolean flag) {
        this.difficulty = enumdifficulty;
        this.difficultyLocked = flag;
    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.handleServerDifficulty(this);
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.difficulty = EnumDifficulty.getDifficultyEnum(packetdataserializer.readUnsignedByte());
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeByte(this.difficulty.getDifficultyId());
    }
}
