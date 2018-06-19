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

    private ChunkPos field_148925_b;
    private SPacketMultiBlockChange.BlockUpdateData[] field_179845_b;

    public SPacketMultiBlockChange() {}

    public SPacketMultiBlockChange(int i, short[] ashort, Chunk chunk) {
        this.field_148925_b = new ChunkPos(chunk.field_76635_g, chunk.field_76647_h);
        this.field_179845_b = new SPacketMultiBlockChange.BlockUpdateData[i];

        for (int j = 0; j < this.field_179845_b.length; ++j) {
            this.field_179845_b[j] = new SPacketMultiBlockChange.BlockUpdateData(ashort[j], chunk);
        }

    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_148925_b = new ChunkPos(packetdataserializer.readInt(), packetdataserializer.readInt());
        this.field_179845_b = new SPacketMultiBlockChange.BlockUpdateData[packetdataserializer.func_150792_a()];

        for (int i = 0; i < this.field_179845_b.length; ++i) {
            this.field_179845_b[i] = new SPacketMultiBlockChange.BlockUpdateData(packetdataserializer.readShort(), (IBlockState) Block.field_176229_d.func_148745_a(packetdataserializer.func_150792_a()));
        }

    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeInt(this.field_148925_b.field_77276_a);
        packetdataserializer.writeInt(this.field_148925_b.field_77275_b);
        packetdataserializer.func_150787_b(this.field_179845_b.length);
        SPacketMultiBlockChange.BlockUpdateData[] apacketplayoutmultiblockchange_multiblockchangeinfo = this.field_179845_b;
        int i = apacketplayoutmultiblockchange_multiblockchangeinfo.length;

        for (int j = 0; j < i; ++j) {
            SPacketMultiBlockChange.BlockUpdateData packetplayoutmultiblockchange_multiblockchangeinfo = apacketplayoutmultiblockchange_multiblockchangeinfo[j];

            packetdataserializer.writeShort(packetplayoutmultiblockchange_multiblockchangeinfo.func_180089_b());
            packetdataserializer.func_150787_b(Block.field_176229_d.func_148747_b(packetplayoutmultiblockchange_multiblockchangeinfo.func_180088_c()));
        }

    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_147287_a(this);
    }

    public class BlockUpdateData {

        private final short field_180091_b;
        private final IBlockState field_180092_c;

        public BlockUpdateData(short short0, IBlockState iblockdata) {
            this.field_180091_b = short0;
            this.field_180092_c = iblockdata;
        }

        public BlockUpdateData(short short0, Chunk chunk) {
            this.field_180091_b = short0;
            this.field_180092_c = chunk.func_177435_g(this.func_180090_a());
        }

        public BlockPos func_180090_a() {
            return new BlockPos(SPacketMultiBlockChange.this.field_148925_b.func_180331_a(this.field_180091_b >> 12 & 15, this.field_180091_b & 255, this.field_180091_b >> 8 & 15));
        }

        public short func_180089_b() {
            return this.field_180091_b;
        }

        public IBlockState func_180088_c() {
            return this.field_180092_c;
        }
    }
}
