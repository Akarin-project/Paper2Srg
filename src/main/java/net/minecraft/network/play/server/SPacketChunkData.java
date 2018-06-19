package net.minecraft.network.play.server;

import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.destroystokyo.paper.antixray.PacketPlayOutMapChunkInfo;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

// Paper start
import com.destroystokyo.paper.antixray.PacketPlayOutMapChunkInfo; // Anti-Xray
// Paper end

public class SPacketChunkData implements Packet<INetHandlerPlayClient> {

    private int field_149284_a;
    private int field_149282_b;
    private int field_186948_c;
    private byte[] field_186949_d;
    private List<NBTTagCompound> field_189557_e;
    private boolean field_149279_g;
    private volatile boolean ready = false; // Paper - Async-Anti-Xray - Ready flag for the network manager

    // Paper start - Async-Anti-Xray - Set the ready flag to true
    public SPacketChunkData() {
        this.ready = true;
    }
    // Paper end

    public SPacketChunkData(Chunk chunk, int i) {
        PacketPlayOutMapChunkInfo packetPlayOutMapChunkInfo = chunk.field_76637_e.chunkPacketBlockController.getPacketPlayOutMapChunkInfo(this, chunk, i); // Paper - Anti-Xray - Add chunk packet info
        this.field_149284_a = chunk.field_76635_g;
        this.field_149282_b = chunk.field_76647_h;
        this.field_149279_g = i == '\uffff';
        boolean flag = chunk.func_177412_p().field_73011_w.func_191066_m();

        this.field_186949_d = new byte[this.func_189556_a(chunk, flag, i)];

        // Paper start - Anti-Xray - Add chunk packet info
        if (packetPlayOutMapChunkInfo != null) {
            packetPlayOutMapChunkInfo.setData(this.field_186949_d);
        }
        // Paper end

        this.field_186948_c = this.writeChunk(new PacketBuffer(this.func_186945_f()), chunk, flag, i, packetPlayOutMapChunkInfo); // Paper - Anti-Xray - Add chunk packet info
        this.field_189557_e = Lists.newArrayList();
        Iterator iterator = chunk.func_177434_r().entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();
            BlockPos blockposition = (BlockPos) entry.getKey();
            TileEntity tileentity = (TileEntity) entry.getValue();
            int j = blockposition.func_177956_o() >> 4;

            if (this.func_149274_i() || (i & 1 << j) != 0) {
                NBTTagCompound nbttagcompound = tileentity.func_189517_E_();

                this.field_189557_e.add(nbttagcompound);
            }
        }

        chunk.field_76637_e.chunkPacketBlockController.modifyBlocks(this, packetPlayOutMapChunkInfo); // Paper - Anti-Xray - Modify blocks
    }

    // Paper start - Async-Anti-Xray - Getter and Setter for the ready flag
    public boolean isReady() {
        return this.ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }
    // Paper end

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_149284_a = packetdataserializer.readInt();
        this.field_149282_b = packetdataserializer.readInt();
        this.field_149279_g = packetdataserializer.readBoolean();
        this.field_186948_c = packetdataserializer.func_150792_a();
        int i = packetdataserializer.func_150792_a();

        if (i > 2097152) {
            throw new RuntimeException("Chunk Packet trying to allocate too much memory on read.");
        } else {
            this.field_186949_d = new byte[i];
            packetdataserializer.readBytes(this.field_186949_d);
            int j = packetdataserializer.func_150792_a();

            this.field_189557_e = Lists.newArrayList();

            for (int k = 0; k < j; ++k) {
                this.field_189557_e.add(packetdataserializer.func_150793_b());
            }

        }
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeInt(this.field_149284_a);
        packetdataserializer.writeInt(this.field_149282_b);
        packetdataserializer.writeBoolean(this.field_149279_g);
        packetdataserializer.func_150787_b(this.field_186948_c);
        packetdataserializer.func_150787_b(this.field_186949_d.length);
        packetdataserializer.writeBytes(this.field_186949_d);
        packetdataserializer.func_150787_b(this.field_189557_e.size());
        Iterator iterator = this.field_189557_e.iterator();

        while (iterator.hasNext()) {
            NBTTagCompound nbttagcompound = (NBTTagCompound) iterator.next();

            packetdataserializer.func_150786_a(nbttagcompound);
        }

    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_147263_a(this);
    }

    private ByteBuf func_186945_f() {
        ByteBuf bytebuf = Unpooled.wrappedBuffer(this.field_186949_d);

        bytebuf.writerIndex(0);
        return bytebuf;
    }

    // Paper start - Anti-Xray - Support default method
    public int writeChunk(PacketBuffer packetDataSerializer, Chunk chunk, boolean writeSkyLightArray, int chunkSectionSelector) { return this.func_189555_a(packetDataSerializer, chunk, writeSkyLightArray, chunkSectionSelector); } // OBFHELPER
    public int func_189555_a(PacketBuffer packetdataserializer, Chunk chunk, boolean flag, int i) {
        return this.a(packetdataserializer, chunk, flag, i, null);
    }
    // Paper end

    public int writeChunk(PacketBuffer packetDataSerializer, Chunk chunk, boolean writeSkyLightArray, int chunkSectionSelector, PacketPlayOutMapChunkInfo packetPlayOutMapChunkInfo) { return this.a(packetDataSerializer, chunk, writeSkyLightArray, chunkSectionSelector, packetPlayOutMapChunkInfo); } // Paper - Anti-Xray - OBFHELPER
    public int a(PacketBuffer packetdataserializer, Chunk chunk, boolean flag, int i, PacketPlayOutMapChunkInfo packetPlayOutMapChunkInfo) { // Paper - Anti-Xray - Add chunk packet info
        int j = 0;
        ExtendedBlockStorage[] achunksection = chunk.func_76587_i();
        int k = 0;

        for (int l = achunksection.length; k < l; ++k) {
            ExtendedBlockStorage chunksection = achunksection[k];

            if (chunksection != Chunk.field_186036_a && (!this.func_149274_i() || !chunksection.func_76663_a()) && (i & 1 << k) != 0) {
                j |= 1 << k;
                chunksection.func_186049_g().writeBlocks(packetdataserializer, packetPlayOutMapChunkInfo, k); // Paper - Anti-Xray - Add chunk packet info
                packetdataserializer.writeBytes(chunksection.func_76661_k().func_177481_a());
                if (flag) {
                    packetdataserializer.writeBytes(chunksection.func_76671_l().func_177481_a());
                }
            }
        }

        if (this.func_149274_i()) {
            packetdataserializer.writeBytes(chunk.func_76605_m());
        }

        return j;
    }

    protected int func_189556_a(Chunk chunk, boolean flag, int i) {
        int j = 0;
        ExtendedBlockStorage[] achunksection = chunk.func_76587_i();
        int k = 0;

        for (int l = achunksection.length; k < l; ++k) {
            ExtendedBlockStorage chunksection = achunksection[k];

            if (chunksection != Chunk.field_186036_a && (!this.func_149274_i() || !chunksection.func_76663_a()) && (i & 1 << k) != 0) {
                j += chunksection.func_186049_g().func_186018_a();
                j += chunksection.func_76661_k().func_177481_a().length;
                if (flag) {
                    j += chunksection.func_76671_l().func_177481_a().length;
                }
            }
        }

        if (this.func_149274_i()) {
            j += chunk.func_76605_m().length;
        }

        return j;
    }

    public boolean func_149274_i() {
        return this.field_149279_g;
    }
}
