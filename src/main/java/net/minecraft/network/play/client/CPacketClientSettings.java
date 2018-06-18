package net.minecraft.network.play.client;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.util.EnumHandSide;

public class CPacketClientSettings implements Packet<INetHandlerPlayServer> {

    private String lang;
    private int view;
    private EntityPlayer.EnumChatVisibility chatVisibility;
    private boolean enableColors;
    private int modelPartFlags;
    private EnumHandSide mainHand;

    public CPacketClientSettings() {}

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.lang = packetdataserializer.readString(16);
        this.view = packetdataserializer.readByte();
        this.chatVisibility = (EntityPlayer.EnumChatVisibility) packetdataserializer.readEnumValue(EntityPlayer.EnumChatVisibility.class);
        this.enableColors = packetdataserializer.readBoolean();
        this.modelPartFlags = packetdataserializer.readUnsignedByte();
        this.mainHand = (EnumHandSide) packetdataserializer.readEnumValue(EnumHandSide.class);
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeString(this.lang);
        packetdataserializer.writeByte(this.view);
        packetdataserializer.writeEnumValue((Enum) this.chatVisibility);
        packetdataserializer.writeBoolean(this.enableColors);
        packetdataserializer.writeByte(this.modelPartFlags);
        packetdataserializer.writeEnumValue((Enum) this.mainHand);
    }

    public void processPacket(INetHandlerPlayServer packetlistenerplayin) {
        packetlistenerplayin.processClientSettings(this);
    }

    public String getLang() {
        return this.lang;
    }

    public EntityPlayer.EnumChatVisibility getChatVisibility() {
        return this.chatVisibility;
    }

    public boolean isColorsEnabled() {
        return this.enableColors;
    }

    public int getModelPartFlags() {
        return this.modelPartFlags;
    }

    public EnumHandSide getMainHand() {
        return this.mainHand;
    }
}
