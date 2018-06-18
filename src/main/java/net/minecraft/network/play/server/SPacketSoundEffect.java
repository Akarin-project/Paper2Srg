package net.minecraft.network.play.server;

import java.io.IOException;
import org.apache.commons.lang3.Validate;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

public class SPacketSoundEffect implements Packet<INetHandlerPlayClient> {

    private SoundEvent sound;
    private SoundCategory category;
    private int posX;
    private int posY;
    private int posZ;
    private float soundVolume;
    private float soundPitch;

    public SPacketSoundEffect() {}

    public SPacketSoundEffect(SoundEvent soundeffect, SoundCategory soundcategory, double d0, double d1, double d2, float f, float f1) {
        Validate.notNull(soundeffect, "sound", new Object[0]);
        this.sound = soundeffect;
        this.category = soundcategory;
        this.posX = (int) (d0 * 8.0D);
        this.posY = (int) (d1 * 8.0D);
        this.posZ = (int) (d2 * 8.0D);
        this.soundVolume = f;
        this.soundPitch = f1;
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.sound = (SoundEvent) SoundEvent.REGISTRY.getObjectById(packetdataserializer.readVarInt());
        this.category = (SoundCategory) packetdataserializer.readEnumValue(SoundCategory.class);
        this.posX = packetdataserializer.readInt();
        this.posY = packetdataserializer.readInt();
        this.posZ = packetdataserializer.readInt();
        this.soundVolume = packetdataserializer.readFloat();
        this.soundPitch = packetdataserializer.readFloat();
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeVarInt(SoundEvent.REGISTRY.getIDForObject((Object) this.sound));
        packetdataserializer.writeEnumValue((Enum) this.category);
        packetdataserializer.writeInt(this.posX);
        packetdataserializer.writeInt(this.posY);
        packetdataserializer.writeInt(this.posZ);
        packetdataserializer.writeFloat(this.soundVolume);
        packetdataserializer.writeFloat(this.soundPitch);
    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.handleSoundEffect(this);
    }
}
