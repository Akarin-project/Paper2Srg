package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;

public class SPacketChat implements Packet<INetHandlerPlayClient> {

    private ITextComponent chatComponent;
    public net.md_5.bungee.api.chat.BaseComponent[] components; // Spigot
    private ChatType type;

    public SPacketChat() {}

    public SPacketChat(ITextComponent ichatbasecomponent) {
        this(ichatbasecomponent, ChatType.SYSTEM);
    }

    public SPacketChat(ITextComponent ichatbasecomponent, ChatType chatmessagetype) {
        this.chatComponent = ichatbasecomponent;
        this.type = chatmessagetype;
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.chatComponent = packetdataserializer.readTextComponent();
        this.type = ChatType.byId(packetdataserializer.readByte());
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        // Spigot start
        if (components != null) {
            //packetdataserializer.a(net.md_5.bungee.chat.ComponentSerializer.toString(components)); // Paper - comment, replaced with below
            // Paper start - don't nest if we don't need to so that we can preserve formatting
            if (this.components.length == 1) {
                packetdataserializer.writeString(net.md_5.bungee.chat.ComponentSerializer.toString(this.components[0]));
            } else {
                packetdataserializer.writeString(net.md_5.bungee.chat.ComponentSerializer.toString(this.components));
            }
            // Paper end
        } else {
            packetdataserializer.writeTextComponent(this.chatComponent);
        }
        // Spigot end
        packetdataserializer.writeByte(this.type.getId());
    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.handleChat(this);
    }

    public boolean isSystem() {
        return this.type == ChatType.SYSTEM || this.type == ChatType.GAME_INFO;
    }

    public ChatType getType() {
        return this.type;
    }
}
