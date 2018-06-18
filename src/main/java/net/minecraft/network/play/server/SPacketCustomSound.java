package net.minecraft.network.play.server;

import java.io.IOException;
import org.apache.commons.lang3.Validate;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.SoundCategory;

public class SPacketCustomSound implements Packet<INetHandlerPlayClient> {

    private String soundName;
    private SoundCategory category;
    private int x;
    private int y = Integer.MAX_VALUE;
    private int z;
    private float volume;
    private float pitch;

    public SPacketCustomSound() {}

    public SPacketCustomSound(String s, SoundCategory soundcategory, double d0, double d1, double d2, float f, float f1) {
        Validate.notNull(s, "name", new Object[0]);
        this.soundName = s;
        this.category = soundcategory;
        this.x = (int) (d0 * 8.0D);
        this.y = (int) (d1 * 8.0D);
        this.z = (int) (d2 * 8.0D);
        this.volume = f;
        this.pitch = f1;
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.soundName = packetdataserializer.readString(256);
        this.category = (SoundCategory) packetdataserializer.readEnumValue(SoundCategory.class);
        this.x = packetdataserializer.readInt();
        this.y = packetdataserializer.readInt();
        this.z = packetdataserializer.readInt();
        this.volume = packetdataserializer.readFloat();
        this.pitch = packetdataserializer.readFloat();
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeString(this.soundName);
        packetdataserializer.writeEnumValue((Enum) this.category);
        packetdataserializer.writeInt(this.x);
        packetdataserializer.writeInt(this.y);
        packetdataserializer.writeInt(this.z);
        packetdataserializer.writeFloat(this.volume);
        packetdataserializer.writeFloat(this.pitch);
    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.handleCustomSound(this);
    }
}
