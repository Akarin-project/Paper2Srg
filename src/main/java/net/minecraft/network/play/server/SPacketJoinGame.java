package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldType;

public class SPacketJoinGame implements Packet<INetHandlerPlayClient> {

    private int playerId;
    private boolean hardcoreMode;
    private GameType gameType;
    private int dimension;
    private EnumDifficulty difficulty;
    private int maxPlayers;
    private WorldType worldType;
    private boolean reducedDebugInfo;

    public SPacketJoinGame() {}

    public SPacketJoinGame(int i, GameType enumgamemode, boolean flag, int j, EnumDifficulty enumdifficulty, int k, WorldType worldtype, boolean flag1) {
        this.playerId = i;
        this.dimension = j;
        this.difficulty = enumdifficulty;
        this.gameType = enumgamemode;
        this.maxPlayers = k;
        this.hardcoreMode = flag;
        this.worldType = worldtype;
        this.reducedDebugInfo = flag1;
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.playerId = packetdataserializer.readInt();
        short short0 = packetdataserializer.readUnsignedByte();

        this.hardcoreMode = (short0 & 8) == 8;
        int i = short0 & -9;

        this.gameType = GameType.getByID(i);
        this.dimension = packetdataserializer.readInt();
        this.difficulty = EnumDifficulty.getDifficultyEnum(packetdataserializer.readUnsignedByte());
        this.maxPlayers = packetdataserializer.readUnsignedByte();
        this.worldType = WorldType.parseWorldType(packetdataserializer.readString(16));
        if (this.worldType == null) {
            this.worldType = WorldType.DEFAULT;
        }

        this.reducedDebugInfo = packetdataserializer.readBoolean();
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeInt(this.playerId);
        int i = this.gameType.getID();

        if (this.hardcoreMode) {
            i |= 8;
        }

        packetdataserializer.writeByte(i);
        packetdataserializer.writeInt(this.dimension);
        packetdataserializer.writeByte(this.difficulty.getDifficultyId());
        packetdataserializer.writeByte(this.maxPlayers);
        packetdataserializer.writeString(this.worldType.getName());
        packetdataserializer.writeBoolean(this.reducedDebugInfo);
    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.handleJoinGame(this);
    }
}
