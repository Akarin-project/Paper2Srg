package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketTimeUpdate implements Packet<INetHandlerPlayClient> {

    // World Age in ticks
    // Not changed by server commands
    // World Age must not be negative
    private long totalWorldTime;
    // Time of Day in ticks
    // If negative the sun will stop moving at the Math.abs of the time
    // Displayed in the debug screen (F3)
    private long worldTime;

    public SPacketTimeUpdate() {}

    public SPacketTimeUpdate(long i, long j, boolean flag) {
        this.totalWorldTime = i;
        this.worldTime = j;
        if (!flag) {
            this.worldTime = -this.worldTime;
            if (this.worldTime == 0L) {
                this.worldTime = -1L;
            }
        }

        // Paper start
        this.totalWorldTime = this.totalWorldTime % 192000;
        // Paper end
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.totalWorldTime = packetdataserializer.readLong();
        this.worldTime = packetdataserializer.readLong();
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeLong(this.totalWorldTime);
        packetdataserializer.writeLong(this.worldTime);
    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.handleTimeUpdate(this);
    }
}
