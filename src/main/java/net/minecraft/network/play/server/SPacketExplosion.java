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

    private double field_149158_a;
    private double field_149156_b;
    private double field_149157_c;
    private float field_149154_d;
    private List<BlockPos> field_149155_e;
    private float field_149152_f;
    private float field_149153_g;
    private float field_149159_h;

    public SPacketExplosion() {}

    public SPacketExplosion(double d0, double d1, double d2, float f, List<BlockPos> list, Vec3d vec3d) {
        this.field_149158_a = d0;
        this.field_149156_b = d1;
        this.field_149157_c = d2;
        this.field_149154_d = f;
        this.field_149155_e = Lists.newArrayList(list);
        if (vec3d != null) {
            this.field_149152_f = (float) vec3d.field_72450_a;
            this.field_149153_g = (float) vec3d.field_72448_b;
            this.field_149159_h = (float) vec3d.field_72449_c;
        }

    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_149158_a = (double) packetdataserializer.readFloat();
        this.field_149156_b = (double) packetdataserializer.readFloat();
        this.field_149157_c = (double) packetdataserializer.readFloat();
        this.field_149154_d = packetdataserializer.readFloat();
        int i = packetdataserializer.readInt();

        this.field_149155_e = Lists.newArrayListWithCapacity(i);
        int j = (int) this.field_149158_a;
        int k = (int) this.field_149156_b;
        int l = (int) this.field_149157_c;

        for (int i1 = 0; i1 < i; ++i1) {
            int j1 = packetdataserializer.readByte() + j;
            int k1 = packetdataserializer.readByte() + k;
            int l1 = packetdataserializer.readByte() + l;

            this.field_149155_e.add(new BlockPos(j1, k1, l1));
        }

        this.field_149152_f = packetdataserializer.readFloat();
        this.field_149153_g = packetdataserializer.readFloat();
        this.field_149159_h = packetdataserializer.readFloat();
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeFloat((float) this.field_149158_a);
        packetdataserializer.writeFloat((float) this.field_149156_b);
        packetdataserializer.writeFloat((float) this.field_149157_c);
        packetdataserializer.writeFloat(this.field_149154_d);
        packetdataserializer.writeInt(this.field_149155_e.size());
        int i = (int) this.field_149158_a;
        int j = (int) this.field_149156_b;
        int k = (int) this.field_149157_c;
        Iterator iterator = this.field_149155_e.iterator();

        while (iterator.hasNext()) {
            BlockPos blockposition = (BlockPos) iterator.next();
            int l = blockposition.func_177958_n() - i;
            int i1 = blockposition.func_177956_o() - j;
            int j1 = blockposition.func_177952_p() - k;

            packetdataserializer.writeByte(l);
            packetdataserializer.writeByte(i1);
            packetdataserializer.writeByte(j1);
        }

        packetdataserializer.writeFloat(this.field_149152_f);
        packetdataserializer.writeFloat(this.field_149153_g);
        packetdataserializer.writeFloat(this.field_149159_h);
    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_147283_a(this);
    }
}
