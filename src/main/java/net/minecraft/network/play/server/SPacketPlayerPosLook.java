package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Set;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketPlayerPosLook implements Packet<INetHandlerPlayClient> {

    private double field_148940_a;
    private double field_148938_b;
    private double field_148939_c;
    private float field_148936_d;
    private float field_148937_e;
    private Set<SPacketPlayerPosLook.EnumFlags> field_179835_f;
    private int field_186966_g;

    public SPacketPlayerPosLook() {}

    public SPacketPlayerPosLook(double d0, double d1, double d2, float f, float f1, Set<SPacketPlayerPosLook.EnumFlags> set, int i) {
        this.field_148940_a = d0;
        this.field_148938_b = d1;
        this.field_148939_c = d2;
        this.field_148936_d = f;
        this.field_148937_e = f1;
        this.field_179835_f = set;
        this.field_186966_g = i;
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_148940_a = packetdataserializer.readDouble();
        this.field_148938_b = packetdataserializer.readDouble();
        this.field_148939_c = packetdataserializer.readDouble();
        this.field_148936_d = packetdataserializer.readFloat();
        this.field_148937_e = packetdataserializer.readFloat();
        this.field_179835_f = SPacketPlayerPosLook.EnumFlags.func_187044_a(packetdataserializer.readUnsignedByte());
        this.field_186966_g = packetdataserializer.func_150792_a();
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeDouble(this.field_148940_a);
        packetdataserializer.writeDouble(this.field_148938_b);
        packetdataserializer.writeDouble(this.field_148939_c);
        packetdataserializer.writeFloat(this.field_148936_d);
        packetdataserializer.writeFloat(this.field_148937_e);
        packetdataserializer.writeByte(SPacketPlayerPosLook.EnumFlags.func_187040_a(this.field_179835_f));
        packetdataserializer.func_150787_b(this.field_186966_g);
    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_184330_a(this);
    }

    public static enum EnumFlags {

        X(0), Y(1), Z(2), Y_ROT(3), X_ROT(4);

        private final int field_187050_f;

        private EnumFlags(int i) {
            this.field_187050_f = i;
        }

        private int func_187042_a() {
            return 1 << this.field_187050_f;
        }

        private boolean func_187043_b(int i) {
            return (i & this.func_187042_a()) == this.func_187042_a();
        }

        public static Set<SPacketPlayerPosLook.EnumFlags> func_187044_a(int i) {
            EnumSet enumset = EnumSet.noneOf(SPacketPlayerPosLook.EnumFlags.class);
            SPacketPlayerPosLook.EnumFlags[] apacketplayoutposition_enumplayerteleportflags = values();
            int j = apacketplayoutposition_enumplayerteleportflags.length;

            for (int k = 0; k < j; ++k) {
                SPacketPlayerPosLook.EnumFlags packetplayoutposition_enumplayerteleportflags = apacketplayoutposition_enumplayerteleportflags[k];

                if (packetplayoutposition_enumplayerteleportflags.func_187043_b(i)) {
                    enumset.add(packetplayoutposition_enumplayerteleportflags);
                }
            }

            return enumset;
        }

        public static int func_187040_a(Set<SPacketPlayerPosLook.EnumFlags> set) {
            int i = 0;

            SPacketPlayerPosLook.EnumFlags packetplayoutposition_enumplayerteleportflags;

            for (Iterator iterator = set.iterator(); iterator.hasNext(); i |= packetplayoutposition_enumplayerteleportflags.func_187042_a()) {
                packetplayoutposition_enumplayerteleportflags = (SPacketPlayerPosLook.EnumFlags) iterator.next();
            }

            return i;
        }
    }
}
