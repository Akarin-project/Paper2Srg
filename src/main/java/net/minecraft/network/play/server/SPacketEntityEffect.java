package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class SPacketEntityEffect implements Packet<INetHandlerPlayClient> {

    private int entityId;
    private byte effectId;
    private byte amplifier;
    private int duration;
    private byte flags;

    public SPacketEntityEffect() {}

    public SPacketEntityEffect(int i, PotionEffect mobeffect) {
        this.entityId = i;
        this.effectId = (byte) (Potion.getIdFromPotion(mobeffect.getPotion()) & 255);
        this.amplifier = (byte) (mobeffect.getAmplifier() & 255);
        if (mobeffect.getDuration() > 32767) {
            this.duration = 32767;
        } else {
            this.duration = mobeffect.getDuration();
        }

        this.flags = 0;
        if (mobeffect.getIsAmbient()) {
            this.flags = (byte) (this.flags | 1);
        }

        if (mobeffect.doesShowParticles()) {
            this.flags = (byte) (this.flags | 2);
        }

    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.entityId = packetdataserializer.readVarInt();
        this.effectId = packetdataserializer.readByte();
        this.amplifier = packetdataserializer.readByte();
        this.duration = packetdataserializer.readVarInt();
        this.flags = packetdataserializer.readByte();
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeVarInt(this.entityId);
        packetdataserializer.writeByte(this.effectId);
        packetdataserializer.writeByte(this.amplifier);
        packetdataserializer.writeVarInt(this.duration);
        packetdataserializer.writeByte(this.flags);
    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.handleEntityEffect(this);
    }
}
