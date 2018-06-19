package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.Locale;
import javax.annotation.Nullable;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.text.ITextComponent;

public class SPacketTitle implements Packet<INetHandlerPlayClient> {

    private SPacketTitle.Type field_179812_a;
    private ITextComponent field_179810_b;
    private int field_179811_c;
    private int field_179808_d;
    private int field_179809_e;

    // Paper start
    public net.md_5.bungee.api.chat.BaseComponent[] components;

    public SPacketTitle(Type action, net.md_5.bungee.api.chat.BaseComponent[] components, int fadeIn, int stay, int fadeOut) {
        this.field_179812_a = action;
        this.components = components;
        this.field_179811_c = fadeIn;
        this.field_179808_d = stay;
        this.field_179809_e = fadeOut;
    }
    // Paper end

    public SPacketTitle() {}

    public SPacketTitle(SPacketTitle.Type packetplayouttitle_enumtitleaction, ITextComponent ichatbasecomponent) {
        this(packetplayouttitle_enumtitleaction, ichatbasecomponent, -1, -1, -1);
    }

    public SPacketTitle(int i, int j, int k) {
        this(SPacketTitle.Type.TIMES, (ITextComponent) null, i, j, k);
    }

    public SPacketTitle(SPacketTitle.Type packetplayouttitle_enumtitleaction, @Nullable ITextComponent ichatbasecomponent, int i, int j, int k) {
        this.field_179812_a = packetplayouttitle_enumtitleaction;
        this.field_179810_b = ichatbasecomponent;
        this.field_179811_c = i;
        this.field_179808_d = j;
        this.field_179809_e = k;
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_179812_a = (SPacketTitle.Type) packetdataserializer.func_179257_a(SPacketTitle.Type.class);
        if (this.field_179812_a == SPacketTitle.Type.TITLE || this.field_179812_a == SPacketTitle.Type.SUBTITLE || this.field_179812_a == SPacketTitle.Type.ACTIONBAR) {
            this.field_179810_b = packetdataserializer.func_179258_d();
        }

        if (this.field_179812_a == SPacketTitle.Type.TIMES) {
            this.field_179811_c = packetdataserializer.readInt();
            this.field_179808_d = packetdataserializer.readInt();
            this.field_179809_e = packetdataserializer.readInt();
        }

    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.func_179249_a((Enum) this.field_179812_a);
        if (this.field_179812_a == SPacketTitle.Type.TITLE || this.field_179812_a == SPacketTitle.Type.SUBTITLE || this.field_179812_a == SPacketTitle.Type.ACTIONBAR) {
            // Paper start
            if (this.components != null) {
                packetdataserializer.func_180714_a(net.md_5.bungee.chat.ComponentSerializer.toString(components));
            } else {
                packetdataserializer.func_179256_a(this.field_179810_b);
            }
            // Paper end
        }

        if (this.field_179812_a == SPacketTitle.Type.TIMES) {
            packetdataserializer.writeInt(this.field_179811_c);
            packetdataserializer.writeInt(this.field_179808_d);
            packetdataserializer.writeInt(this.field_179809_e);
        }

    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_175099_a(this);
    }

    public static enum Type {

        TITLE, SUBTITLE, ACTIONBAR, TIMES, CLEAR, RESET;

        private Type() {}

        public static SPacketTitle.Type func_179969_a(String s) {
            SPacketTitle.Type[] apacketplayouttitle_enumtitleaction = values();
            int i = apacketplayouttitle_enumtitleaction.length;

            for (int j = 0; j < i; ++j) {
                SPacketTitle.Type packetplayouttitle_enumtitleaction = apacketplayouttitle_enumtitleaction[j];

                if (packetplayouttitle_enumtitleaction.name().equalsIgnoreCase(s)) {
                    return packetplayouttitle_enumtitleaction;
                }
            }

            return SPacketTitle.Type.TITLE;
        }

        public static String[] func_179971_a() {
            String[] astring = new String[values().length];
            int i = 0;
            SPacketTitle.Type[] apacketplayouttitle_enumtitleaction = values();
            int j = apacketplayouttitle_enumtitleaction.length;

            for (int k = 0; k < j; ++k) {
                SPacketTitle.Type packetplayouttitle_enumtitleaction = apacketplayouttitle_enumtitleaction[k];

                astring[i++] = packetplayouttitle_enumtitleaction.name().toLowerCase(Locale.ROOT);
            }

            return astring;
        }
    }
}
