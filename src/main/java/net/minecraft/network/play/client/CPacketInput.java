package net.minecraft.network.play.client;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketInput implements Packet<INetHandlerPlayServer> {

    private float strafeSpeed;
    private float forwardSpeed;
    private boolean jumping;
    private boolean sneaking;

    public CPacketInput() {}

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.strafeSpeed = packetdataserializer.readFloat();
        this.forwardSpeed = packetdataserializer.readFloat();
        byte b0 = packetdataserializer.readByte();

        this.jumping = (b0 & 1) > 0;
        this.sneaking = (b0 & 2) > 0;
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeFloat(this.strafeSpeed);
        packetdataserializer.writeFloat(this.forwardSpeed);
        byte b0 = 0;

        if (this.jumping) {
            b0 = (byte) (b0 | 1);
        }

        if (this.sneaking) {
            b0 = (byte) (b0 | 2);
        }

        packetdataserializer.writeByte(b0);
    }

    public void processPacket(INetHandlerPlayServer packetlistenerplayin) {
        packetlistenerplayin.processInput(this);
    }

    public float getStrafeSpeed() {
        return this.strafeSpeed;
    }

    public float getForwardSpeed() {
        return this.forwardSpeed;
    }

    public boolean isJumping() {
        return this.jumping;
    }

    public boolean isSneaking() {
        return this.sneaking;
    }
}
