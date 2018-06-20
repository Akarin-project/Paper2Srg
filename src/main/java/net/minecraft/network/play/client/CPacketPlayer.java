package net.minecraft.network.play.client;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketPlayer implements Packet<INetHandlerPlayServer> {

    public double field_149479_a;
    public double field_149477_b;
    public double field_149478_c;
    public float field_149476_e;
    public float field_149473_f;
    protected boolean field_149474_g;
    public boolean field_149480_h;
    public boolean field_149481_i;

    public CPacketPlayer() {}

    @Override
    public void func_148833_a(INetHandlerPlayServer packetlistenerplayin) {
        packetlistenerplayin.func_147347_a(this);
    }

    @Override
    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_149474_g = packetdataserializer.readUnsignedByte() != 0;
    }

    @Override
    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeByte(this.field_149474_g ? 1 : 0);
    }

    public double func_186997_a(double d0) {
        return this.field_149480_h ? this.field_149479_a : d0;
    }

    public double func_186996_b(double d0) {
        return this.field_149480_h ? this.field_149477_b : d0;
    }

    public double func_187000_c(double d0) {
        return this.field_149480_h ? this.field_149478_c : d0;
    }

    public float func_186999_a(float f) {
        return this.field_149481_i ? this.field_149476_e : f;
    }

    public float func_186998_b(float f) {
        return this.field_149481_i ? this.field_149473_f : f;
    }

    public boolean func_149465_i() {
        return this.field_149474_g;
    }

    public static class Rotation extends CPacketPlayer {

        public Rotation() {
            this.field_149481_i = true;
        }

        @Override
        public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
            this.field_149476_e = packetdataserializer.readFloat();
            this.field_149473_f = packetdataserializer.readFloat();
            super.func_148837_a(packetdataserializer);
        }

        @Override
        public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
            packetdataserializer.writeFloat(this.field_149476_e);
            packetdataserializer.writeFloat(this.field_149473_f);
            super.func_148840_b(packetdataserializer);
        }
    }

    public static class Position extends CPacketPlayer {

        public Position() {
            this.field_149480_h = true;
        }

        @Override
        public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
            this.field_149479_a = packetdataserializer.readDouble();
            this.field_149477_b = packetdataserializer.readDouble();
            this.field_149478_c = packetdataserializer.readDouble();
            super.func_148837_a(packetdataserializer);
        }

        @Override
        public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
            packetdataserializer.writeDouble(this.field_149479_a);
            packetdataserializer.writeDouble(this.field_149477_b);
            packetdataserializer.writeDouble(this.field_149478_c);
            super.func_148840_b(packetdataserializer);
        }
    }

    public static class PositionRotation extends CPacketPlayer {

        public PositionRotation() {
            this.field_149480_h = true;
            this.field_149481_i = true;
        }

        @Override
        public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
            this.field_149479_a = packetdataserializer.readDouble();
            this.field_149477_b = packetdataserializer.readDouble();
            this.field_149478_c = packetdataserializer.readDouble();
            this.field_149476_e = packetdataserializer.readFloat();
            this.field_149473_f = packetdataserializer.readFloat();
            super.func_148837_a(packetdataserializer);
        }

        @Override
        public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
            packetdataserializer.writeDouble(this.field_149479_a);
            packetdataserializer.writeDouble(this.field_149477_b);
            packetdataserializer.writeDouble(this.field_149478_c);
            packetdataserializer.writeFloat(this.field_149476_e);
            packetdataserializer.writeFloat(this.field_149473_f);
            super.func_148840_b(packetdataserializer);
        }
    }
}
