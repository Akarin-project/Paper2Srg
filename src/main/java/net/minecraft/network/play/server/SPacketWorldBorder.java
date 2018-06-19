package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.world.border.WorldBorder;

public class SPacketWorldBorder implements Packet<INetHandlerPlayClient> {

    private SPacketWorldBorder.Action field_179795_a;
    private int field_179793_b;
    private double field_179794_c;
    private double field_179791_d;
    private double field_179792_e;
    private double field_179789_f;
    private long field_179790_g;
    private int field_179796_h;
    private int field_179797_i;

    public SPacketWorldBorder() {}

    public SPacketWorldBorder(WorldBorder worldborder, SPacketWorldBorder.Action packetplayoutworldborder_enumworldborderaction) {
        this.field_179795_a = packetplayoutworldborder_enumworldborderaction;
        // CraftBukkit start - multiply out nether border
        this.field_179794_c = worldborder.func_177731_f() * (worldborder.world.field_73011_w instanceof WorldProviderHell ? 8 : 1);
        this.field_179791_d = worldborder.func_177721_g() * (worldborder.world.field_73011_w instanceof WorldProviderHell ? 8 : 1);
        // CraftBukkit end
        this.field_179789_f = worldborder.func_177741_h();
        this.field_179792_e = worldborder.func_177751_j();
        this.field_179790_g = worldborder.func_177732_i();
        this.field_179793_b = worldborder.func_177722_l();
        this.field_179797_i = worldborder.func_177748_q();
        this.field_179796_h = worldborder.func_177740_p();
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_179795_a = (SPacketWorldBorder.Action) packetdataserializer.func_179257_a(SPacketWorldBorder.Action.class);
        switch (this.field_179795_a) {
        case SET_SIZE:
            this.field_179792_e = packetdataserializer.readDouble();
            break;

        case LERP_SIZE:
            this.field_179789_f = packetdataserializer.readDouble();
            this.field_179792_e = packetdataserializer.readDouble();
            this.field_179790_g = packetdataserializer.func_179260_f();
            break;

        case SET_CENTER:
            this.field_179794_c = packetdataserializer.readDouble();
            this.field_179791_d = packetdataserializer.readDouble();
            break;

        case SET_WARNING_BLOCKS:
            this.field_179797_i = packetdataserializer.func_150792_a();
            break;

        case SET_WARNING_TIME:
            this.field_179796_h = packetdataserializer.func_150792_a();
            break;

        case INITIALIZE:
            this.field_179794_c = packetdataserializer.readDouble();
            this.field_179791_d = packetdataserializer.readDouble();
            this.field_179789_f = packetdataserializer.readDouble();
            this.field_179792_e = packetdataserializer.readDouble();
            this.field_179790_g = packetdataserializer.func_179260_f();
            this.field_179793_b = packetdataserializer.func_150792_a();
            this.field_179797_i = packetdataserializer.func_150792_a();
            this.field_179796_h = packetdataserializer.func_150792_a();
        }

    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.func_179249_a((Enum) this.field_179795_a);
        switch (this.field_179795_a) {
        case SET_SIZE:
            packetdataserializer.writeDouble(this.field_179792_e);
            break;

        case LERP_SIZE:
            packetdataserializer.writeDouble(this.field_179789_f);
            packetdataserializer.writeDouble(this.field_179792_e);
            packetdataserializer.func_179254_b(this.field_179790_g);
            break;

        case SET_CENTER:
            packetdataserializer.writeDouble(this.field_179794_c);
            packetdataserializer.writeDouble(this.field_179791_d);
            break;

        case SET_WARNING_BLOCKS:
            packetdataserializer.func_150787_b(this.field_179797_i);
            break;

        case SET_WARNING_TIME:
            packetdataserializer.func_150787_b(this.field_179796_h);
            break;

        case INITIALIZE:
            packetdataserializer.writeDouble(this.field_179794_c);
            packetdataserializer.writeDouble(this.field_179791_d);
            packetdataserializer.writeDouble(this.field_179789_f);
            packetdataserializer.writeDouble(this.field_179792_e);
            packetdataserializer.func_179254_b(this.field_179790_g);
            packetdataserializer.func_150787_b(this.field_179793_b);
            packetdataserializer.func_150787_b(this.field_179797_i);
            packetdataserializer.func_150787_b(this.field_179796_h);
        }

    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_175093_a(this);
    }

    public static enum Action {

        SET_SIZE, LERP_SIZE, SET_CENTER, INITIALIZE, SET_WARNING_TIME, SET_WARNING_BLOCKS;

        private Action() {}
    }
}
