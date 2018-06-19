package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;

public class SPacketChat implements Packet<INetHandlerPlayClient> {

    private ITextComponent field_148919_a;
    public net.md_5.bungee.api.chat.BaseComponent[] components; // Spigot
    private ChatType field_179842_b;

    public SPacketChat() {}

    public SPacketChat(ITextComponent ichatbasecomponent) {
        this(ichatbasecomponent, ChatType.SYSTEM);
    }

    public SPacketChat(ITextComponent ichatbasecomponent, ChatType chatmessagetype) {
        this.field_148919_a = ichatbasecomponent;
        this.field_179842_b = chatmessagetype;
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_148919_a = packetdataserializer.func_179258_d();
        this.field_179842_b = ChatType.func_192582_a(packetdataserializer.readByte());
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        // Spigot start
        if (components != null) {
            //packetdataserializer.a(net.md_5.bungee.chat.ComponentSerializer.toString(components)); // Paper - comment, replaced with below
            // Paper start - don't nest if we don't need to so that we can preserve formatting
            if (this.components.length == 1) {
                packetdataserializer.func_180714_a(net.md_5.bungee.chat.ComponentSerializer.toString(this.components[0]));
            } else {
                packetdataserializer.func_180714_a(net.md_5.bungee.chat.ComponentSerializer.toString(this.components));
            }
            // Paper end
        } else {
            packetdataserializer.func_179256_a(this.field_148919_a);
        }
        // Spigot end
        packetdataserializer.writeByte(this.field_179842_b.func_192583_a());
    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_147251_a(this);
    }

    public boolean func_148916_d() {
        return this.field_179842_b == ChatType.SYSTEM || this.field_179842_b == ChatType.GAME_INFO;
    }

    public ChatType func_192590_c() {
        return this.field_179842_b;
    }
}
