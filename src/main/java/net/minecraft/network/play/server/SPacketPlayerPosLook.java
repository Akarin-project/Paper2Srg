package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Set;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketPlayerPosLook implements Packet<INetHandlerPlayClient> {

    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private Set<SPacketPlayerPosLook.EnumFlags> flags;
    private int teleportId;

    public SPacketPlayerPosLook() {}

    public SPacketPlayerPosLook(double d0, double d1, double d2, float f, float f1, Set<SPacketPlayerPosLook.EnumFlags> set, int i) {
        this.x = d0;
        this.y = d1;
        this.z = d2;
        this.yaw = f;
        this.pitch = f1;
        this.flags = set;
        this.teleportId = i;
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.x = packetdataserializer.readDouble();
        this.y = packetdataserializer.readDouble();
        this.z = packetdataserializer.readDouble();
        this.yaw = packetdataserializer.readFloat();
        this.pitch = packetdataserializer.readFloat();
        this.flags = SPacketPlayerPosLook.EnumFlags.unpack(packetdataserializer.readUnsignedByte());
        this.teleportId = packetdataserializer.readVarInt();
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeDouble(this.x);
        packetdataserializer.writeDouble(this.y);
        packetdataserializer.writeDouble(this.z);
        packetdataserializer.writeFloat(this.yaw);
        packetdataserializer.writeFloat(this.pitch);
        packetdataserializer.writeByte(SPacketPlayerPosLook.EnumFlags.pack(this.flags));
        packetdataserializer.writeVarInt(this.teleportId);
    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.handlePlayerPosLook(this);
    }

    public static enum EnumFlags {

        X(0), Y(1), Z(2), Y_ROT(3), X_ROT(4);

        private final int bit;

        private EnumFlags(int i) {
            this.bit = i;
        }

        private int getMask() {
            return 1 << this.bit;
        }

        private boolean isSet(int i) {
            return (i & this.getMask()) == this.getMask();
        }

        public static Set<SPacketPlayerPosLook.EnumFlags> unpack(int i) {
            EnumSet enumset = EnumSet.noneOf(SPacketPlayerPosLook.EnumFlags.class);
            SPacketPlayerPosLook.EnumFlags[] apacketplayoutposition_enumplayerteleportflags = values();
            int j = apacketplayoutposition_enumplayerteleportflags.length;

            for (int k = 0; k < j; ++k) {
                SPacketPlayerPosLook.EnumFlags packetplayoutposition_enumplayerteleportflags = apacketplayoutposition_enumplayerteleportflags[k];

                if (packetplayoutposition_enumplayerteleportflags.isSet(i)) {
                    enumset.add(packetplayoutposition_enumplayerteleportflags);
                }
            }

            return enumset;
        }

        public static int pack(Set<SPacketPlayerPosLook.EnumFlags> set) {
            int i = 0;

            SPacketPlayerPosLook.EnumFlags packetplayoutposition_enumplayerteleportflags;

            for (Iterator iterator = set.iterator(); iterator.hasNext(); i |= packetplayoutposition_enumplayerteleportflags.getMask()) {
                packetplayoutposition_enumplayerteleportflags = (SPacketPlayerPosLook.EnumFlags) iterator.next();
            }

            return i;
        }
    }
}
