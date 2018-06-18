package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;

public class SPacketMultiBlockChange implements Packet<INetHandlerPlayClient> {

    private ChunkPos chunkPos;
    private SPacketMultiBlockChange.BlockUpdateData[] changedBlocks;

    public SPacketMultiBlockChange() {}

    public SPacketMultiBlockChange(int i, short[] ashort, Chunk chunk) {
        this.chunkPos = new ChunkPos(chunk.x, chunk.z);
        this.changedBlocks = new SPacketMultiBlockChange.BlockUpdateData[i];

        for (int j = 0; j < this.changedBlocks.length; ++j) {
            this.changedBlocks[j] = new SPacketMultiBlockChange.BlockUpdateData(ashort[j], chunk);
        }

    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.chunkPos = new ChunkPos(packetdataserializer.readInt(), packetdataserializer.readInt());
        this.changedBlocks = new SPacketMultiBlockChange.BlockUpdateData[packetdataserializer.readVarInt()];

        for (int i = 0; i < this.changedBlocks.length; ++i) {
            this.changedBlocks[i] = new SPacketMultiBlockChange.BlockUpdateData(packetdataserializer.readShort(), (IBlockState) Block.BLOCK_STATE_IDS.getByValue(packetdataserializer.readVarInt()));
        }

    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeInt(this.chunkPos.x);
        packetdataserializer.writeInt(this.chunkPos.z);
        packetdataserializer.writeVarInt(this.changedBlocks.length);
        SPacketMultiBlockChange.BlockUpdateData[] apacketplayoutmultiblockchange_multiblockchangeinfo = this.changedBlocks;
        int i = apacketplayoutmultiblockchange_multiblockchangeinfo.length;

        for (int j = 0; j < i; ++j) {
            SPacketMultiBlockChange.BlockUpdateData packetplayoutmultiblockchange_multiblockchangeinfo = apacketplayoutmultiblockchange_multiblockchangeinfo[j];

            packetdataserializer.writeShort(packetplayoutmultiblockchange_multiblockchangeinfo.getOffset());
            packetdataserializer.writeVarInt(Block.BLOCK_STATE_IDS.get(packetplayoutmultiblockchange_multiblockchangeinfo.getBlockState()));
        }

    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.handleMultiBlockChange(this);
    }

    public class BlockUpdateData {

        private final short offset;
        private final IBlockState blockState;

        public BlockUpdateData(short short0, IBlockState iblockdata) {
            this.offset = short0;
            this.blockState = iblockdata;
        }

        public BlockUpdateData(short short0, Chunk chunk) {
            this.offset = short0;
            this.blockState = chunk.getBlockState(this.getPos());
        }

        public BlockPos getPos() {
            return new BlockPos(SPacketMultiBlockChange.this.chunkPos.getBlock(this.offset >> 12 & 15, this.offset & 255, this.offset >> 8 & 15));
        }

        public short getOffset() {
            return this.offset;
        }

        public IBlockState getBlockState() {
            return this.blockState;
        }
    }
}
