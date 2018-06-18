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

    private int chunkX;
    private int chunkZ;
    private int availableSections;
    private byte[] buffer;
    private List<NBTTagCompound> tileEntityTags;
    private boolean fullChunk;
    private volatile boolean ready = false; // Paper - Async-Anti-Xray - Ready flag for the network manager

    // Paper start - Async-Anti-Xray - Set the ready flag to true
    public SPacketChunkData() {
        this.ready = true;
    }
    // Paper end

    public SPacketChunkData(Chunk chunk, int i) {
        PacketPlayOutMapChunkInfo packetPlayOutMapChunkInfo = chunk.world.chunkPacketBlockController.getPacketPlayOutMapChunkInfo(this, chunk, i); // Paper - Anti-Xray - Add chunk packet info
        this.chunkX = chunk.x;
        this.chunkZ = chunk.z;
        this.fullChunk = i == '\uffff';
        boolean flag = chunk.getWorld().provider.hasSkyLight();

        this.buffer = new byte[this.calculateChunkSize(chunk, flag, i)];

        // Paper start - Anti-Xray - Add chunk packet info
        if (packetPlayOutMapChunkInfo != null) {
            packetPlayOutMapChunkInfo.setData(this.buffer);
        }
        // Paper end

        this.availableSections = this.writeChunk(new PacketBuffer(this.getWriteBuffer()), chunk, flag, i, packetPlayOutMapChunkInfo); // Paper - Anti-Xray - Add chunk packet info
        this.tileEntityTags = Lists.newArrayList();
        Iterator iterator = chunk.getTileEntityMap().entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();
            BlockPos blockposition = (BlockPos) entry.getKey();
            TileEntity tileentity = (TileEntity) entry.getValue();
            int j = blockposition.getY() >> 4;

            if (this.isFullChunk() || (i & 1 << j) != 0) {
                NBTTagCompound nbttagcompound = tileentity.getUpdateTag();

                this.tileEntityTags.add(nbttagcompound);
            }
        }

        chunk.world.chunkPacketBlockController.modifyBlocks(this, packetPlayOutMapChunkInfo); // Paper - Anti-Xray - Modify blocks
    }

    // Paper start - Async-Anti-Xray - Getter and Setter for the ready flag
    public boolean isReady() {
        return this.ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }
    // Paper end

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.chunkX = packetdataserializer.readInt();
        this.chunkZ = packetdataserializer.readInt();
        this.fullChunk = packetdataserializer.readBoolean();
        this.availableSections = packetdataserializer.readVarInt();
        int i = packetdataserializer.readVarInt();

        if (i > 2097152) {
            throw new RuntimeException("Chunk Packet trying to allocate too much memory on read.");
        } else {
            this.buffer = new byte[i];
            packetdataserializer.readBytes(this.buffer);
            int j = packetdataserializer.readVarInt();

            this.tileEntityTags = Lists.newArrayList();

            for (int k = 0; k < j; ++k) {
                this.tileEntityTags.add(packetdataserializer.readCompoundTag());
            }

        }
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeInt(this.chunkX);
        packetdataserializer.writeInt(this.chunkZ);
        packetdataserializer.writeBoolean(this.fullChunk);
        packetdataserializer.writeVarInt(this.availableSections);
        packetdataserializer.writeVarInt(this.buffer.length);
        packetdataserializer.writeBytes(this.buffer);
        packetdataserializer.writeVarInt(this.tileEntityTags.size());
        Iterator iterator = this.tileEntityTags.iterator();

        while (iterator.hasNext()) {
            NBTTagCompound nbttagcompound = (NBTTagCompound) iterator.next();

            packetdataserializer.writeCompoundTag(nbttagcompound);
        }

    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.handleChunkData(this);
    }

    private ByteBuf getWriteBuffer() {
        ByteBuf bytebuf = Unpooled.wrappedBuffer(this.buffer);

        bytebuf.writerIndex(0);
        return bytebuf;
    }

    // Paper start - Anti-Xray - Support default method
    public int writeChunk(PacketBuffer packetDataSerializer, Chunk chunk, boolean writeSkyLightArray, int chunkSectionSelector) { return this.extractChunkData(packetDataSerializer, chunk, writeSkyLightArray, chunkSectionSelector); } // OBFHELPER
    public int extractChunkData(PacketBuffer packetdataserializer, Chunk chunk, boolean flag, int i) {
        return this.a(packetdataserializer, chunk, flag, i, null);
    }
    // Paper end

    public int writeChunk(PacketBuffer packetDataSerializer, Chunk chunk, boolean writeSkyLightArray, int chunkSectionSelector, PacketPlayOutMapChunkInfo packetPlayOutMapChunkInfo) { return this.a(packetDataSerializer, chunk, writeSkyLightArray, chunkSectionSelector, packetPlayOutMapChunkInfo); } // Paper - Anti-Xray - OBFHELPER
    public int a(PacketBuffer packetdataserializer, Chunk chunk, boolean flag, int i, PacketPlayOutMapChunkInfo packetPlayOutMapChunkInfo) { // Paper - Anti-Xray - Add chunk packet info
        int j = 0;
        ExtendedBlockStorage[] achunksection = chunk.getBlockStorageArray();
        int k = 0;

        for (int l = achunksection.length; k < l; ++k) {
            ExtendedBlockStorage chunksection = achunksection[k];

            if (chunksection != Chunk.NULL_BLOCK_STORAGE && (!this.isFullChunk() || !chunksection.isEmpty()) && (i & 1 << k) != 0) {
                j |= 1 << k;
                chunksection.getData().writeBlocks(packetdataserializer, packetPlayOutMapChunkInfo, k); // Paper - Anti-Xray - Add chunk packet info
                packetdataserializer.writeBytes(chunksection.getBlockLight().getData());
                if (flag) {
                    packetdataserializer.writeBytes(chunksection.getSkyLight().getData());
                }
            }
        }

        if (this.isFullChunk()) {
            packetdataserializer.writeBytes(chunk.getBiomeArray());
        }

        return j;
    }

    protected int calculateChunkSize(Chunk chunk, boolean flag, int i) {
        int j = 0;
        ExtendedBlockStorage[] achunksection = chunk.getBlockStorageArray();
        int k = 0;

        for (int l = achunksection.length; k < l; ++k) {
            ExtendedBlockStorage chunksection = achunksection[k];

            if (chunksection != Chunk.NULL_BLOCK_STORAGE && (!this.isFullChunk() || !chunksection.isEmpty()) && (i & 1 << k) != 0) {
                j += chunksection.getData().getSerializedSize();
                j += chunksection.getBlockLight().getData().length;
                if (flag) {
                    j += chunksection.getSkyLight().getData().length;
                }
            }
        }

        if (this.isFullChunk()) {
            j += chunk.getBiomeArray().length;
        }

        return j;
    }

    public boolean isFullChunk() {
        return this.fullChunk;
    }
}
