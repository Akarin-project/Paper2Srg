package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.EnumParticleTypes;

public class SPacketParticles implements Packet<INetHandlerPlayClient> {

    private EnumParticleTypes particleType;
    private float xCoord;
    private float yCoord;
    private float zCoord;
    private float xOffset;
    private float yOffset;
    private float zOffset;
    private float particleSpeed;
    private int particleCount;
    private boolean longDistance;
    private int[] particleArguments;

    public SPacketParticles() {}

    public SPacketParticles(EnumParticleTypes enumparticle, boolean flag, float f, float f1, float f2, float f3, float f4, float f5, float f6, int i, int... aint) {
        this.particleType = enumparticle;
        this.longDistance = flag;
        this.xCoord = f;
        this.yCoord = f1;
        this.zCoord = f2;
        this.xOffset = f3;
        this.yOffset = f4;
        this.zOffset = f5;
        this.particleSpeed = f6;
        this.particleCount = i;
        this.particleArguments = aint;
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.particleType = EnumParticleTypes.getParticleFromId(packetdataserializer.readInt());
        if (this.particleType == null) {
            this.particleType = EnumParticleTypes.BARRIER;
        }

        this.longDistance = packetdataserializer.readBoolean();
        this.xCoord = packetdataserializer.readFloat();
        this.yCoord = packetdataserializer.readFloat();
        this.zCoord = packetdataserializer.readFloat();
        this.xOffset = packetdataserializer.readFloat();
        this.yOffset = packetdataserializer.readFloat();
        this.zOffset = packetdataserializer.readFloat();
        this.particleSpeed = packetdataserializer.readFloat();
        this.particleCount = packetdataserializer.readInt();
        int i = this.particleType.getArgumentCount();

        this.particleArguments = new int[i];

        for (int j = 0; j < i; ++j) {
            this.particleArguments[j] = packetdataserializer.readVarInt();
        }

    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeInt(this.particleType.getParticleID());
        packetdataserializer.writeBoolean(this.longDistance);
        packetdataserializer.writeFloat(this.xCoord);
        packetdataserializer.writeFloat(this.yCoord);
        packetdataserializer.writeFloat(this.zCoord);
        packetdataserializer.writeFloat(this.xOffset);
        packetdataserializer.writeFloat(this.yOffset);
        packetdataserializer.writeFloat(this.zOffset);
        packetdataserializer.writeFloat(this.particleSpeed);
        packetdataserializer.writeInt(this.particleCount);
        int i = this.particleType.getArgumentCount();

        for (int j = 0; j < i; ++j) {
            packetdataserializer.writeVarInt(this.particleArguments[j]);
        }

    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.handleParticles(this);
    }
}
