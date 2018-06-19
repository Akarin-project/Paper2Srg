package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketTimeUpdate implements Packet<INetHandlerPlayClient> {

    // World Age in ticks
    // Not changed by server commands
    // World Age must not be negative
    private long field_149369_a;
    // Time of Day in ticks
    // If negative the sun will stop moving at the Math.abs of the time
    // Displayed in the debug screen (F3)
    private long field_149368_b;

    public SPacketTimeUpdate() {}

    public SPacketTimeUpdate(long i, long j, boolean flag) {
        this.field_149369_a = i;
        this.field_149368_b = j;
        if (!flag) {
            this.field_149368_b = -this.field_149368_b;
            if (this.field_149368_b == 0L) {
                this.field_149368_b = -1L;
            }
        }

        // Paper start
        this.field_149369_a = this.field_149369_a % 192000;
        // Paper end
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_149369_a = packetdataserializer.readLong();
        this.field_149368_b = packetdataserializer.readLong();
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeLong(this.field_149369_a);
        packetdataserializer.writeLong(this.field_149368_b);
    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_147285_a(this);
    }
}
