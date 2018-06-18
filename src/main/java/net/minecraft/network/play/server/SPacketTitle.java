package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.Locale;
import javax.annotation.Nullable;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.text.ITextComponent;

public class SPacketTitle implements Packet<INetHandlerPlayClient> {

    private SPacketTitle.Type type;
    private ITextComponent message;
    private int fadeInTime;
    private int displayTime;
    private int fadeOutTime;

    // Paper start
    public net.md_5.bungee.api.chat.BaseComponent[] components;

    public SPacketTitle(Type action, net.md_5.bungee.api.chat.BaseComponent[] components, int fadeIn, int stay, int fadeOut) {
        this.type = action;
        this.components = components;
        this.fadeInTime = fadeIn;
        this.displayTime = stay;
        this.fadeOutTime = fadeOut;
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
        this.type = packetplayouttitle_enumtitleaction;
        this.message = ichatbasecomponent;
        this.fadeInTime = i;
        this.displayTime = j;
        this.fadeOutTime = k;
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.type = (SPacketTitle.Type) packetdataserializer.readEnumValue(SPacketTitle.Type.class);
        if (this.type == SPacketTitle.Type.TITLE || this.type == SPacketTitle.Type.SUBTITLE || this.type == SPacketTitle.Type.ACTIONBAR) {
            this.message = packetdataserializer.readTextComponent();
        }

        if (this.type == SPacketTitle.Type.TIMES) {
            this.fadeInTime = packetdataserializer.readInt();
            this.displayTime = packetdataserializer.readInt();
            this.fadeOutTime = packetdataserializer.readInt();
        }

    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeEnumValue((Enum) this.type);
        if (this.type == SPacketTitle.Type.TITLE || this.type == SPacketTitle.Type.SUBTITLE || this.type == SPacketTitle.Type.ACTIONBAR) {
            // Paper start
            if (this.components != null) {
                packetdataserializer.writeString(net.md_5.bungee.chat.ComponentSerializer.toString(components));
            } else {
                packetdataserializer.writeTextComponent(this.message);
            }
            // Paper end
        }

        if (this.type == SPacketTitle.Type.TIMES) {
            packetdataserializer.writeInt(this.fadeInTime);
            packetdataserializer.writeInt(this.displayTime);
            packetdataserializer.writeInt(this.fadeOutTime);
        }

    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.handleTitle(this);
    }

    public static enum Type {

        TITLE, SUBTITLE, ACTIONBAR, TIMES, CLEAR, RESET;

        private Type() {}

        public static SPacketTitle.Type byName(String s) {
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

        public static String[] getNames() {
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
