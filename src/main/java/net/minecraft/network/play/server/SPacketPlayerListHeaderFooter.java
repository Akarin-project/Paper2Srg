package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.text.ITextComponent;

public class SPacketPlayerListHeaderFooter implements Packet<INetHandlerPlayClient> {

    public net.md_5.bungee.api.chat.BaseComponent[] header, footer; // Paper

    private ITextComponent field_179703_a;
    private ITextComponent field_179702_b;

    public SPacketPlayerListHeaderFooter() {}

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_179703_a = packetdataserializer.func_179258_d();
        this.field_179702_b = packetdataserializer.func_179258_d();
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        // Paper start
        if (this.header != null) {
            packetdataserializer.func_180714_a(net.md_5.bungee.chat.ComponentSerializer.toString(this.header));
        } else {
            packetdataserializer.func_179256_a(this.field_179703_a);
        }

        if (this.footer != null) {
            packetdataserializer.func_180714_a(net.md_5.bungee.chat.ComponentSerializer.toString(this.footer));
        } else {
            packetdataserializer.func_179256_a(this.field_179702_b);
        }
        // Paper end
    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_175096_a(this);
    }
}
