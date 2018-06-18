package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldType;

public class SPacketRespawn implements Packet<INetHandlerPlayClient> {

    private int dimensionID;
    private EnumDifficulty difficulty;
    private GameType gameType;
    private WorldType worldType;

    public SPacketRespawn() {}

    public SPacketRespawn(int i, EnumDifficulty enumdifficulty, WorldType worldtype, GameType enumgamemode) {
        this.dimensionID = i;
        this.difficulty = enumdifficulty;
        this.gameType = enumgamemode;
        this.worldType = worldtype;
    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.handleRespawn(this);
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.dimensionID = packetdataserializer.readInt();
        this.difficulty = EnumDifficulty.getDifficultyEnum(packetdataserializer.readUnsignedByte());
        this.gameType = GameType.getByID(packetdataserializer.readUnsignedByte());
        this.worldType = WorldType.parseWorldType(packetdataserializer.readString(16));
        if (this.worldType == null) {
            this.worldType = WorldType.DEFAULT;
        }

    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeInt(this.dimensionID);
        packetdataserializer.writeByte(this.difficulty.getDifficultyId());
        packetdataserializer.writeByte(this.gameType.getID());
        packetdataserializer.writeString(this.worldType.getName());
    }
}
