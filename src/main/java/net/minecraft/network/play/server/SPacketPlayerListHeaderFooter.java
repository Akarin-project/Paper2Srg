package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.text.ITextComponent;

public class SPacketPlayerListHeaderFooter implements Packet<INetHandlerPlayClient> {

    public net.md_5.bungee.api.chat.BaseComponent[] header, footer; // Paper

    private ITextComponent header;
    private ITextComponent footer;

    public SPacketPlayerListHeaderFooter() {}

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.header = packetdataserializer.readTextComponent();
        this.footer = packetdataserializer.readTextComponent();
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        // Paper start
        if (this.header != null) {
            packetdataserializer.writeString(net.md_5.bungee.chat.ComponentSerializer.toString(this.header));
        } else {
            packetdataserializer.writeTextComponent(this.header);
        }

        if (this.footer != null) {
            packetdataserializer.writeString(net.md_5.bungee.chat.ComponentSerializer.toString(this.footer));
        } else {
            packetdataserializer.writeTextComponent(this.footer);
        }
        // Paper end
    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.handlePlayerListHeaderFooter(this);
    }
}
