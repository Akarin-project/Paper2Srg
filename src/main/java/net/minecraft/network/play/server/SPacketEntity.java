package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketEntity implements Packet<INetHandlerPlayClient> {

    protected int field_149074_a;
    protected int field_149072_b;
    protected int field_149073_c;
    protected int field_149070_d;
    protected byte field_149071_e;
    protected byte field_149068_f;
    protected boolean field_179743_g;
    protected boolean field_149069_g;

    public SPacketEntity() {}

    public SPacketEntity(int i) {
        this.field_149074_a = i;
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_149074_a = packetdataserializer.func_150792_a();
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.func_150787_b(this.field_149074_a);
    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_147259_a(this);
    }

    public String toString() {
        return "Entity_" + super.toString();
    }

    public static class S16PacketEntityLook extends SPacketEntity {

        public S16PacketEntityLook() {
            this.field_149069_g = true;
        }

        public S16PacketEntityLook(int i, byte b0, byte b1, boolean flag) {
            super(i);
            this.field_149071_e = b0;
            this.field_149068_f = b1;
            this.field_149069_g = true;
            this.field_179743_g = flag;
        }

        public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
            super.func_148837_a(packetdataserializer);
            this.field_149071_e = packetdataserializer.readByte();
            this.field_149068_f = packetdataserializer.readByte();
            this.field_179743_g = packetdataserializer.readBoolean();
        }

        public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
            super.func_148840_b(packetdataserializer);
            packetdataserializer.writeByte(this.field_149071_e);
            packetdataserializer.writeByte(this.field_149068_f);
            packetdataserializer.writeBoolean(this.field_179743_g);
        }
    }

    public static class S15PacketEntityRelMove extends SPacketEntity {

        public S15PacketEntityRelMove() {}

        public S15PacketEntityRelMove(int i, long j, long k, long l, boolean flag) {
            super(i);
            this.field_149072_b = (int) j;
            this.field_149073_c = (int) k;
            this.field_149070_d = (int) l;
            this.field_179743_g = flag;
        }

        public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
            super.func_148837_a(packetdataserializer);
            this.field_149072_b = packetdataserializer.readShort();
            this.field_149073_c = packetdataserializer.readShort();
            this.field_149070_d = packetdataserializer.readShort();
            this.field_179743_g = packetdataserializer.readBoolean();
        }

        public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
            super.func_148840_b(packetdataserializer);
            packetdataserializer.writeShort(this.field_149072_b);
            packetdataserializer.writeShort(this.field_149073_c);
            packetdataserializer.writeShort(this.field_149070_d);
            packetdataserializer.writeBoolean(this.field_179743_g);
        }
    }

    public static class S17PacketEntityLookMove extends SPacketEntity {

        public S17PacketEntityLookMove() {
            this.field_149069_g = true;
        }

        public S17PacketEntityLookMove(int i, long j, long k, long l, byte b0, byte b1, boolean flag) {
            super(i);
            this.field_149072_b = (int) j;
            this.field_149073_c = (int) k;
            this.field_149070_d = (int) l;
            this.field_149071_e = b0;
            this.field_149068_f = b1;
            this.field_179743_g = flag;
            this.field_149069_g = true;
        }

        public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
            super.func_148837_a(packetdataserializer);
            this.field_149072_b = packetdataserializer.readShort();
            this.field_149073_c = packetdataserializer.readShort();
            this.field_149070_d = packetdataserializer.readShort();
            this.field_149071_e = packetdataserializer.readByte();
            this.field_149068_f = packetdataserializer.readByte();
            this.field_179743_g = packetdataserializer.readBoolean();
        }

        public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
            super.func_148840_b(packetdataserializer);
            packetdataserializer.writeShort(this.field_149072_b);
            packetdataserializer.writeShort(this.field_149073_c);
            packetdataserializer.writeShort(this.field_149070_d);
            packetdataserializer.writeByte(this.field_149071_e);
            packetdataserializer.writeByte(this.field_149068_f);
            packetdataserializer.writeBoolean(this.field_179743_g);
        }
    }
}
