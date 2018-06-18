package net.minecraft.network.play.server;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class SPacketExplosion implements Packet<INetHandlerPlayClient> {

    private double posX;
    private double posY;
    private double posZ;
    private float strength;
    private List<BlockPos> affectedBlockPositions;
    private float motionX;
    private float motionY;
    private float motionZ;

    public SPacketExplosion() {}

    public SPacketExplosion(double d0, double d1, double d2, float f, List<BlockPos> list, Vec3d vec3d) {
        this.posX = d0;
        this.posY = d1;
        this.posZ = d2;
        this.strength = f;
        this.affectedBlockPositions = Lists.newArrayList(list);
        if (vec3d != null) {
            this.motionX = (float) vec3d.x;
            this.motionY = (float) vec3d.y;
            this.motionZ = (float) vec3d.z;
        }

    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.posX = (double) packetdataserializer.readFloat();
        this.posY = (double) packetdataserializer.readFloat();
        this.posZ = (double) packetdataserializer.readFloat();
        this.strength = packetdataserializer.readFloat();
        int i = packetdataserializer.readInt();

        this.affectedBlockPositions = Lists.newArrayListWithCapacity(i);
        int j = (int) this.posX;
        int k = (int) this.posY;
        int l = (int) this.posZ;

        for (int i1 = 0; i1 < i; ++i1) {
            int j1 = packetdataserializer.readByte() + j;
            int k1 = packetdataserializer.readByte() + k;
            int l1 = packetdataserializer.readByte() + l;

            this.affectedBlockPositions.add(new BlockPos(j1, k1, l1));
        }

        this.motionX = packetdataserializer.readFloat();
        this.motionY = packetdataserializer.readFloat();
        this.motionZ = packetdataserializer.readFloat();
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeFloat((float) this.posX);
        packetdataserializer.writeFloat((float) this.posY);
        packetdataserializer.writeFloat((float) this.posZ);
        packetdataserializer.writeFloat(this.strength);
        packetdataserializer.writeInt(this.affectedBlockPositions.size());
        int i = (int) this.posX;
        int j = (int) this.posY;
        int k = (int) this.posZ;
        Iterator iterator = this.affectedBlockPositions.iterator();

        while (iterator.hasNext()) {
            BlockPos blockposition = (BlockPos) iterator.next();
            int l = blockposition.getX() - i;
            int i1 = blockposition.getY() - j;
            int j1 = blockposition.getZ() - k;

            packetdataserializer.writeByte(l);
            packetdataserializer.writeByte(i1);
            packetdataserializer.writeByte(j1);
        }

        packetdataserializer.writeFloat(this.motionX);
        packetdataserializer.writeFloat(this.motionY);
        packetdataserializer.writeFloat(this.motionZ);
    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.handleExplosion(this);
    }
}
