package net.minecraft.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import io.netty.util.ByteProcessor;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;
import javax.annotation.Nullable;


import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import org.bukkit.craftbukkit.inventory.CraftItemStack;

public class PacketBuffer extends ByteBuf {

    private final ByteBuf field_150794_a;

    public PacketBuffer(ByteBuf bytebuf) {
        this.field_150794_a = bytebuf;
    }

    public static int countBytes(int i) { return PacketBuffer.func_150790_a(i); } // Paper - Anti-Xray - OBFHELPER
    public static int func_150790_a(int i) {
        for (int j = 1; j < 5; ++j) {
            if ((i & -1 << j * 7) == 0) {
                return j;
            }
        }

        return 5;
    }

    public PacketBuffer func_179250_a(byte[] abyte) {
        this.func_150787_b(abyte.length);
        this.writeBytes(abyte);
        return this;
    }

    public byte[] func_179251_a() {
        return this.func_189425_b(this.readableBytes());
    }

    public byte[] func_189425_b(int i) {
        int j = this.func_150792_a();

        if (j > i) {
            throw new DecoderException("ByteArray with size " + j + " is bigger than allowed " + i);
        } else {
            byte[] abyte = new byte[j];

            this.readBytes(abyte);
            return abyte;
        }
    }

    public PacketBuffer func_186875_a(int[] aint) {
        this.func_150787_b(aint.length);
        int[] aint1 = aint;
        int i = aint.length;

        for (int j = 0; j < i; ++j) {
            int k = aint1[j];

            this.func_150787_b(k);
        }

        return this;
    }

    public int[] func_186863_b() {
        return this.func_189424_c(this.readableBytes());
    }

    public int[] func_189424_c(int i) {
        int j = this.func_150792_a();

        if (j > i) {
            throw new DecoderException("VarIntArray with size " + j + " is bigger than allowed " + i);
        } else {
            int[] aint = new int[j];

            for (int k = 0; k < aint.length; ++k) {
                aint[k] = this.func_150792_a();
            }

            return aint;
        }
    }

    public PacketBuffer func_186865_a(long[] along) {
        this.func_150787_b(along.length);
        long[] along1 = along;
        int i = along.length;

        for (int j = 0; j < i; ++j) {
            long k = along1[j];

            this.writeLong(k);
        }

        return this;
    }

    public BlockPos func_179259_c() {
        return BlockPos.func_177969_a(this.readLong());
    }

    public PacketBuffer func_179255_a(BlockPos blockposition) {
        this.writeLong(blockposition.func_177986_g());
        return this;
    }

    public ITextComponent func_179258_d() {
        return ITextComponent.Serializer.func_150699_a(this.func_150789_c(32767));
    }

    public PacketBuffer func_179256_a(ITextComponent ichatbasecomponent) {
        return this.func_180714_a(ITextComponent.Serializer.func_150696_a(ichatbasecomponent));
    }

    public <T extends Enum<T>> T func_179257_a(Class<T> oclass) {
        return ((T[]) oclass.getEnumConstants())[this.func_150792_a()]; // CraftBukkit - fix decompile error
    }

    public PacketBuffer func_179249_a(Enum<?> oenum) {
        return this.func_150787_b(oenum.ordinal());
    }

    public int func_150792_a() {
        int i = 0;
        int j = 0;

        byte b0;

        do {
            b0 = this.readByte();
            i |= (b0 & 127) << j++ * 7;
            if (j > 5) {
                throw new RuntimeException("VarInt too big");
            }
        } while ((b0 & 128) == 128);

        return i;
    }

    public long func_179260_f() {
        long i = 0L;
        int j = 0;

        byte b0;

        do {
            b0 = this.readByte();
            i |= (long) (b0 & 127) << j++ * 7;
            if (j > 10) {
                throw new RuntimeException("VarLong too big");
            }
        } while ((b0 & 128) == 128);

        return i;
    }

    public PacketBuffer func_179252_a(UUID uuid) {
        this.writeLong(uuid.getMostSignificantBits());
        this.writeLong(uuid.getLeastSignificantBits());
        return this;
    }

    public UUID func_179253_g() {
        return new UUID(this.readLong(), this.readLong());
    }

    public PacketBuffer func_150787_b(int i) {
        while ((i & -128) != 0) {
            this.writeByte(i & 127 | 128);
            i >>>= 7;
        }

        this.writeByte(i);
        return this;
    }

    public PacketBuffer func_179254_b(long i) {
        while ((i & -128L) != 0L) {
            this.writeByte((int) (i & 127L) | 128);
            i >>>= 7;
        }

        this.writeByte((int) i);
        return this;
    }

    public PacketBuffer func_150786_a(@Nullable NBTTagCompound nbttagcompound) {
        if (nbttagcompound == null) {
            this.writeByte(0);
        } else {
            try {
                CompressedStreamTools.func_74800_a(nbttagcompound, (DataOutput) (new ByteBufOutputStream(this)));
            } catch (Exception ioexception) { // CraftBukkit - IOException -> Exception
                throw new EncoderException(ioexception);
            }
        }

        return this;
    }

    @Nullable
    public NBTTagCompound func_150793_b() {
        int i = this.readerIndex();
        byte b0 = this.readByte();

        if (b0 == 0) {
            return null;
        } else {
            this.readerIndex(i);

            try {
                return CompressedStreamTools.func_152456_a((DataInput) (new ByteBufInputStream(this)), new NBTSizeTracker(2097152L));
            } catch (IOException ioexception) {
                throw new EncoderException(ioexception);
            }
        }
    }

    public PacketBuffer func_150788_a(ItemStack itemstack) {
        if (itemstack.func_190926_b() || itemstack.func_77973_b() == null) { // CraftBukkit - NPE fix itemstack.getItem()
            this.writeShort(-1);
        } else {
            this.writeShort(Item.func_150891_b(itemstack.func_77973_b()));
            this.writeByte(itemstack.func_190916_E());
            this.writeShort(itemstack.func_77960_j());
            NBTTagCompound nbttagcompound = null;

            if (itemstack.func_77973_b().func_77645_m() || itemstack.func_77973_b().func_77651_p()) {
                // Spigot start - filter
                itemstack = itemstack.func_77946_l();
                CraftItemStack.setItemMeta(itemstack, CraftItemStack.getItemMeta(itemstack));
                // Spigot end
                nbttagcompound = itemstack.func_77978_p();
            }

            this.func_150786_a(nbttagcompound);
        }

        return this;
    }

    public ItemStack func_150791_c() {
        short short0 = this.readShort();

        if (short0 < 0) {
            return ItemStack.field_190927_a;
        } else {
            byte b0 = this.readByte();
            short short1 = this.readShort();
            ItemStack itemstack = new ItemStack(Item.func_150899_d(short0), b0, short1);

            itemstack.func_77982_d(this.func_150793_b());
            // CraftBukkit start
            if (itemstack.func_77978_p() != null) {
                CraftItemStack.setItemMeta(itemstack, CraftItemStack.getItemMeta(itemstack));
            }
            // CraftBukkit end
            return itemstack;
        }
    }

    public String func_150789_c(int i) {
        int j = this.func_150792_a();

        if (j > i * 4) {
            throw new DecoderException("The received encoded string buffer length is longer than maximum allowed (" + j + " > " + i * 4 + ")");
        } else if (j < 0) {
            throw new DecoderException("The received encoded string buffer length is less than zero! Weird string!");
        } else {
            String s = this.toString(this.readerIndex(), j, StandardCharsets.UTF_8);

            this.readerIndex(this.readerIndex() + j);
            if (s.length() > i) {
                throw new DecoderException("The received string length is longer than maximum allowed (" + j + " > " + i + ")");
            } else {
                return s;
            }
        }
    }

    public PacketBuffer func_180714_a(String s) {
        byte[] abyte = s.getBytes(StandardCharsets.UTF_8);

        if (abyte.length > 44767) { // Paper - raise limit a bit more as normal means can trigger this
            throw new EncoderException("String too big (was " + s.length() + " bytes encoded, max " + 44767 + ")"); // Paper
        } else {
            this.func_150787_b(abyte.length);
            this.writeBytes(abyte);
            return this;
        }
    }

    public ResourceLocation func_192575_l() {
        return new ResourceLocation(this.func_150789_c(32767));
    }

    public PacketBuffer func_192572_a(ResourceLocation minecraftkey) {
        this.func_180714_a(minecraftkey.toString());
        return this;
    }

    public Date func_192573_m() {
        return new Date(this.readLong());
    }

    public PacketBuffer func_192574_a(Date date) {
        this.writeLong(date.getTime());
        return this;
    }

    public int capacity() {
        return this.field_150794_a.capacity();
    }

    public ByteBuf capacity(int i) {
        return this.field_150794_a.capacity(i);
    }

    public int maxCapacity() {
        return this.field_150794_a.maxCapacity();
    }

    public ByteBufAllocator alloc() {
        return this.field_150794_a.alloc();
    }

    public ByteOrder order() {
        return this.field_150794_a.order();
    }

    public ByteBuf order(ByteOrder byteorder) {
        return this.field_150794_a.order(byteorder);
    }

    public ByteBuf unwrap() {
        return this.field_150794_a.unwrap();
    }

    public boolean isDirect() {
        return this.field_150794_a.isDirect();
    }

    public boolean isReadOnly() {
        return this.field_150794_a.isReadOnly();
    }

    public ByteBuf asReadOnly() {
        return this.field_150794_a.asReadOnly();
    }

    public int readerIndex() {
        return this.field_150794_a.readerIndex();
    }

    public ByteBuf readerIndex(int i) {
        return this.field_150794_a.readerIndex(i);
    }

    public int writerIndex() {
        return this.field_150794_a.writerIndex();
    }

    public ByteBuf writerIndex(int i) {
        return this.field_150794_a.writerIndex(i);
    }

    public ByteBuf setIndex(int i, int j) {
        return this.field_150794_a.setIndex(i, j);
    }

    public int readableBytes() {
        return this.field_150794_a.readableBytes();
    }

    public int writableBytes() {
        return this.field_150794_a.writableBytes();
    }

    public int maxWritableBytes() {
        return this.field_150794_a.maxWritableBytes();
    }

    public boolean isReadable() {
        return this.field_150794_a.isReadable();
    }

    public boolean isReadable(int i) {
        return this.field_150794_a.isReadable(i);
    }

    public boolean isWritable() {
        return this.field_150794_a.isWritable();
    }

    public boolean isWritable(int i) {
        return this.field_150794_a.isWritable(i);
    }

    public ByteBuf clear() {
        return this.field_150794_a.clear();
    }

    public ByteBuf markReaderIndex() {
        return this.field_150794_a.markReaderIndex();
    }

    public ByteBuf resetReaderIndex() {
        return this.field_150794_a.resetReaderIndex();
    }

    public ByteBuf markWriterIndex() {
        return this.field_150794_a.markWriterIndex();
    }

    public ByteBuf resetWriterIndex() {
        return this.field_150794_a.resetWriterIndex();
    }

    public ByteBuf discardReadBytes() {
        return this.field_150794_a.discardReadBytes();
    }

    public ByteBuf discardSomeReadBytes() {
        return this.field_150794_a.discardSomeReadBytes();
    }

    public ByteBuf ensureWritable(int i) {
        return this.field_150794_a.ensureWritable(i);
    }

    public int ensureWritable(int i, boolean flag) {
        return this.field_150794_a.ensureWritable(i, flag);
    }

    public boolean getBoolean(int i) {
        return this.field_150794_a.getBoolean(i);
    }

    public byte getByte(int i) {
        return this.field_150794_a.getByte(i);
    }

    public short getUnsignedByte(int i) {
        return this.field_150794_a.getUnsignedByte(i);
    }

    public short getShort(int i) {
        return this.field_150794_a.getShort(i);
    }

    public short getShortLE(int i) {
        return this.field_150794_a.getShortLE(i);
    }

    public int getUnsignedShort(int i) {
        return this.field_150794_a.getUnsignedShort(i);
    }

    public int getUnsignedShortLE(int i) {
        return this.field_150794_a.getUnsignedShortLE(i);
    }

    public int getMedium(int i) {
        return this.field_150794_a.getMedium(i);
    }

    public int getMediumLE(int i) {
        return this.field_150794_a.getMediumLE(i);
    }

    public int getUnsignedMedium(int i) {
        return this.field_150794_a.getUnsignedMedium(i);
    }

    public int getUnsignedMediumLE(int i) {
        return this.field_150794_a.getUnsignedMediumLE(i);
    }

    public int getInt(int i) {
        return this.field_150794_a.getInt(i);
    }

    public int getIntLE(int i) {
        return this.field_150794_a.getIntLE(i);
    }

    public long getUnsignedInt(int i) {
        return this.field_150794_a.getUnsignedInt(i);
    }

    public long getUnsignedIntLE(int i) {
        return this.field_150794_a.getUnsignedIntLE(i);
    }

    public long getLong(int i) {
        return this.field_150794_a.getLong(i);
    }

    public long getLongLE(int i) {
        return this.field_150794_a.getLongLE(i);
    }

    public char getChar(int i) {
        return this.field_150794_a.getChar(i);
    }

    public float getFloat(int i) {
        return this.field_150794_a.getFloat(i);
    }

    public double getDouble(int i) {
        return this.field_150794_a.getDouble(i);
    }

    public ByteBuf getBytes(int i, ByteBuf bytebuf) {
        return this.field_150794_a.getBytes(i, bytebuf);
    }

    public ByteBuf getBytes(int i, ByteBuf bytebuf, int j) {
        return this.field_150794_a.getBytes(i, bytebuf, j);
    }

    public ByteBuf getBytes(int i, ByteBuf bytebuf, int j, int k) {
        return this.field_150794_a.getBytes(i, bytebuf, j, k);
    }

    public ByteBuf getBytes(int i, byte[] abyte) {
        return this.field_150794_a.getBytes(i, abyte);
    }

    public ByteBuf getBytes(int i, byte[] abyte, int j, int k) {
        return this.field_150794_a.getBytes(i, abyte, j, k);
    }

    public ByteBuf getBytes(int i, ByteBuffer bytebuffer) {
        return this.field_150794_a.getBytes(i, bytebuffer);
    }

    public ByteBuf getBytes(int i, OutputStream outputstream, int j) throws IOException {
        return this.field_150794_a.getBytes(i, outputstream, j);
    }

    public int getBytes(int i, GatheringByteChannel gatheringbytechannel, int j) throws IOException {
        return this.field_150794_a.getBytes(i, gatheringbytechannel, j);
    }

    public int getBytes(int i, FileChannel filechannel, long j, int k) throws IOException {
        return this.field_150794_a.getBytes(i, filechannel, j, k);
    }

    public CharSequence getCharSequence(int i, int j, Charset charset) {
        return this.field_150794_a.getCharSequence(i, j, charset);
    }

    public ByteBuf setBoolean(int i, boolean flag) {
        return this.field_150794_a.setBoolean(i, flag);
    }

    public ByteBuf setByte(int i, int j) {
        return this.field_150794_a.setByte(i, j);
    }

    public ByteBuf setShort(int i, int j) {
        return this.field_150794_a.setShort(i, j);
    }

    public ByteBuf setShortLE(int i, int j) {
        return this.field_150794_a.setShortLE(i, j);
    }

    public ByteBuf setMedium(int i, int j) {
        return this.field_150794_a.setMedium(i, j);
    }

    public ByteBuf setMediumLE(int i, int j) {
        return this.field_150794_a.setMediumLE(i, j);
    }

    public ByteBuf setInt(int i, int j) {
        return this.field_150794_a.setInt(i, j);
    }

    public ByteBuf setIntLE(int i, int j) {
        return this.field_150794_a.setIntLE(i, j);
    }

    public ByteBuf setLong(int i, long j) {
        return this.field_150794_a.setLong(i, j);
    }

    public ByteBuf setLongLE(int i, long j) {
        return this.field_150794_a.setLongLE(i, j);
    }

    public ByteBuf setChar(int i, int j) {
        return this.field_150794_a.setChar(i, j);
    }

    public ByteBuf setFloat(int i, float f) {
        return this.field_150794_a.setFloat(i, f);
    }

    public ByteBuf setDouble(int i, double d0) {
        return this.field_150794_a.setDouble(i, d0);
    }

    public ByteBuf setBytes(int i, ByteBuf bytebuf) {
        return this.field_150794_a.setBytes(i, bytebuf);
    }

    public ByteBuf setBytes(int i, ByteBuf bytebuf, int j) {
        return this.field_150794_a.setBytes(i, bytebuf, j);
    }

    public ByteBuf setBytes(int i, ByteBuf bytebuf, int j, int k) {
        return this.field_150794_a.setBytes(i, bytebuf, j, k);
    }

    public ByteBuf setBytes(int i, byte[] abyte) {
        return this.field_150794_a.setBytes(i, abyte);
    }

    public ByteBuf setBytes(int i, byte[] abyte, int j, int k) {
        return this.field_150794_a.setBytes(i, abyte, j, k);
    }

    public ByteBuf setBytes(int i, ByteBuffer bytebuffer) {
        return this.field_150794_a.setBytes(i, bytebuffer);
    }

    public int setBytes(int i, InputStream inputstream, int j) throws IOException {
        return this.field_150794_a.setBytes(i, inputstream, j);
    }

    public int setBytes(int i, ScatteringByteChannel scatteringbytechannel, int j) throws IOException {
        return this.field_150794_a.setBytes(i, scatteringbytechannel, j);
    }

    public int setBytes(int i, FileChannel filechannel, long j, int k) throws IOException {
        return this.field_150794_a.setBytes(i, filechannel, j, k);
    }

    public ByteBuf setZero(int i, int j) {
        return this.field_150794_a.setZero(i, j);
    }

    public int setCharSequence(int i, CharSequence charsequence, Charset charset) {
        return this.field_150794_a.setCharSequence(i, charsequence, charset);
    }

    public boolean readBoolean() {
        return this.field_150794_a.readBoolean();
    }

    public byte readByte() {
        return this.field_150794_a.readByte();
    }

    public short readUnsignedByte() {
        return this.field_150794_a.readUnsignedByte();
    }

    public short readShort() {
        return this.field_150794_a.readShort();
    }

    public short readShortLE() {
        return this.field_150794_a.readShortLE();
    }

    public int readUnsignedShort() {
        return this.field_150794_a.readUnsignedShort();
    }

    public int readUnsignedShortLE() {
        return this.field_150794_a.readUnsignedShortLE();
    }

    public int readMedium() {
        return this.field_150794_a.readMedium();
    }

    public int readMediumLE() {
        return this.field_150794_a.readMediumLE();
    }

    public int readUnsignedMedium() {
        return this.field_150794_a.readUnsignedMedium();
    }

    public int readUnsignedMediumLE() {
        return this.field_150794_a.readUnsignedMediumLE();
    }

    public int readInt() {
        return this.field_150794_a.readInt();
    }

    public int readIntLE() {
        return this.field_150794_a.readIntLE();
    }

    public long readUnsignedInt() {
        return this.field_150794_a.readUnsignedInt();
    }

    public long readUnsignedIntLE() {
        return this.field_150794_a.readUnsignedIntLE();
    }

    public long readLong() {
        return this.field_150794_a.readLong();
    }

    public long readLongLE() {
        return this.field_150794_a.readLongLE();
    }

    public char readChar() {
        return this.field_150794_a.readChar();
    }

    public float readFloat() {
        return this.field_150794_a.readFloat();
    }

    public double readDouble() {
        return this.field_150794_a.readDouble();
    }

    public ByteBuf readBytes(int i) {
        return this.field_150794_a.readBytes(i);
    }

    public ByteBuf readSlice(int i) {
        return this.field_150794_a.readSlice(i);
    }

    public ByteBuf readRetainedSlice(int i) {
        return this.field_150794_a.readRetainedSlice(i);
    }

    public ByteBuf readBytes(ByteBuf bytebuf) {
        return this.field_150794_a.readBytes(bytebuf);
    }

    public ByteBuf readBytes(ByteBuf bytebuf, int i) {
        return this.field_150794_a.readBytes(bytebuf, i);
    }

    public ByteBuf readBytes(ByteBuf bytebuf, int i, int j) {
        return this.field_150794_a.readBytes(bytebuf, i, j);
    }

    public ByteBuf readBytes(byte[] abyte) {
        return this.field_150794_a.readBytes(abyte);
    }

    public ByteBuf readBytes(byte[] abyte, int i, int j) {
        return this.field_150794_a.readBytes(abyte, i, j);
    }

    public ByteBuf readBytes(ByteBuffer bytebuffer) {
        return this.field_150794_a.readBytes(bytebuffer);
    }

    public ByteBuf readBytes(OutputStream outputstream, int i) throws IOException {
        return this.field_150794_a.readBytes(outputstream, i);
    }

    public int readBytes(GatheringByteChannel gatheringbytechannel, int i) throws IOException {
        return this.field_150794_a.readBytes(gatheringbytechannel, i);
    }

    public CharSequence readCharSequence(int i, Charset charset) {
        return this.field_150794_a.readCharSequence(i, charset);
    }

    public int readBytes(FileChannel filechannel, long i, int j) throws IOException {
        return this.field_150794_a.readBytes(filechannel, i, j);
    }

    public ByteBuf skipBytes(int i) {
        return this.field_150794_a.skipBytes(i);
    }

    public ByteBuf writeBoolean(boolean flag) {
        return this.field_150794_a.writeBoolean(flag);
    }

    public ByteBuf writeByte(int i) {
        return this.field_150794_a.writeByte(i);
    }

    public ByteBuf writeShort(int i) {
        return this.field_150794_a.writeShort(i);
    }

    public ByteBuf writeShortLE(int i) {
        return this.field_150794_a.writeShortLE(i);
    }

    public ByteBuf writeMedium(int i) {
        return this.field_150794_a.writeMedium(i);
    }

    public ByteBuf writeMediumLE(int i) {
        return this.field_150794_a.writeMediumLE(i);
    }

    public ByteBuf writeInt(int i) {
        return this.field_150794_a.writeInt(i);
    }

    public ByteBuf writeIntLE(int i) {
        return this.field_150794_a.writeIntLE(i);
    }

    public ByteBuf writeLong(long i) {
        return this.field_150794_a.writeLong(i);
    }

    public ByteBuf writeLongLE(long i) {
        return this.field_150794_a.writeLongLE(i);
    }

    public ByteBuf writeChar(int i) {
        return this.field_150794_a.writeChar(i);
    }

    public ByteBuf writeFloat(float f) {
        return this.field_150794_a.writeFloat(f);
    }

    public ByteBuf writeDouble(double d0) {
        return this.field_150794_a.writeDouble(d0);
    }

    public ByteBuf writeBytes(ByteBuf bytebuf) {
        return this.field_150794_a.writeBytes(bytebuf);
    }

    public ByteBuf writeBytes(ByteBuf bytebuf, int i) {
        return this.field_150794_a.writeBytes(bytebuf, i);
    }

    public ByteBuf writeBytes(ByteBuf bytebuf, int i, int j) {
        return this.field_150794_a.writeBytes(bytebuf, i, j);
    }

    public ByteBuf writeBytes(byte[] abyte) {
        return this.field_150794_a.writeBytes(abyte);
    }

    public ByteBuf writeBytes(byte[] abyte, int i, int j) {
        return this.field_150794_a.writeBytes(abyte, i, j);
    }

    public ByteBuf writeBytes(ByteBuffer bytebuffer) {
        return this.field_150794_a.writeBytes(bytebuffer);
    }

    public int writeBytes(InputStream inputstream, int i) throws IOException {
        return this.field_150794_a.writeBytes(inputstream, i);
    }

    public int writeBytes(ScatteringByteChannel scatteringbytechannel, int i) throws IOException {
        return this.field_150794_a.writeBytes(scatteringbytechannel, i);
    }

    public int writeBytes(FileChannel filechannel, long i, int j) throws IOException {
        return this.field_150794_a.writeBytes(filechannel, i, j);
    }

    public ByteBuf writeZero(int i) {
        return this.field_150794_a.writeZero(i);
    }

    public int writeCharSequence(CharSequence charsequence, Charset charset) {
        return this.field_150794_a.writeCharSequence(charsequence, charset);
    }

    public int indexOf(int i, int j, byte b0) {
        return this.field_150794_a.indexOf(i, j, b0);
    }

    public int bytesBefore(byte b0) {
        return this.field_150794_a.bytesBefore(b0);
    }

    public int bytesBefore(int i, byte b0) {
        return this.field_150794_a.bytesBefore(i, b0);
    }

    public int bytesBefore(int i, int j, byte b0) {
        return this.field_150794_a.bytesBefore(i, j, b0);
    }

    public int forEachByte(ByteProcessor byteprocessor) {
        return this.field_150794_a.forEachByte(byteprocessor);
    }

    public int forEachByte(int i, int j, ByteProcessor byteprocessor) {
        return this.field_150794_a.forEachByte(i, j, byteprocessor);
    }

    public int forEachByteDesc(ByteProcessor byteprocessor) {
        return this.field_150794_a.forEachByteDesc(byteprocessor);
    }

    public int forEachByteDesc(int i, int j, ByteProcessor byteprocessor) {
        return this.field_150794_a.forEachByteDesc(i, j, byteprocessor);
    }

    public ByteBuf copy() {
        return this.field_150794_a.copy();
    }

    public ByteBuf copy(int i, int j) {
        return this.field_150794_a.copy(i, j);
    }

    public ByteBuf slice() {
        return this.field_150794_a.slice();
    }

    public ByteBuf retainedSlice() {
        return this.field_150794_a.retainedSlice();
    }

    public ByteBuf slice(int i, int j) {
        return this.field_150794_a.slice(i, j);
    }

    public ByteBuf retainedSlice(int i, int j) {
        return this.field_150794_a.retainedSlice(i, j);
    }

    public ByteBuf duplicate() {
        return this.field_150794_a.duplicate();
    }

    public ByteBuf retainedDuplicate() {
        return this.field_150794_a.retainedDuplicate();
    }

    public int nioBufferCount() {
        return this.field_150794_a.nioBufferCount();
    }

    public ByteBuffer nioBuffer() {
        return this.field_150794_a.nioBuffer();
    }

    public ByteBuffer nioBuffer(int i, int j) {
        return this.field_150794_a.nioBuffer(i, j);
    }

    public ByteBuffer internalNioBuffer(int i, int j) {
        return this.field_150794_a.internalNioBuffer(i, j);
    }

    public ByteBuffer[] nioBuffers() {
        return this.field_150794_a.nioBuffers();
    }

    public ByteBuffer[] nioBuffers(int i, int j) {
        return this.field_150794_a.nioBuffers(i, j);
    }

    public boolean hasArray() {
        return this.field_150794_a.hasArray();
    }

    public byte[] array() {
        return this.field_150794_a.array();
    }

    public int arrayOffset() {
        return this.field_150794_a.arrayOffset();
    }

    public boolean hasMemoryAddress() {
        return this.field_150794_a.hasMemoryAddress();
    }

    public long memoryAddress() {
        return this.field_150794_a.memoryAddress();
    }

    public String toString(Charset charset) {
        return this.field_150794_a.toString(charset);
    }

    public String toString(int i, int j, Charset charset) {
        return this.field_150794_a.toString(i, j, charset);
    }

    public int hashCode() {
        return this.field_150794_a.hashCode();
    }

    public boolean equals(Object object) {
        return this.field_150794_a.equals(object);
    }

    public int compareTo(ByteBuf bytebuf) {
        return this.field_150794_a.compareTo(bytebuf);
    }

    public String toString() {
        return this.field_150794_a.toString();
    }

    public ByteBuf retain(int i) {
        return this.field_150794_a.retain(i);
    }

    public ByteBuf retain() {
        return this.field_150794_a.retain();
    }

    public ByteBuf touch() {
        return this.field_150794_a.touch();
    }

    public ByteBuf touch(Object object) {
        return this.field_150794_a.touch(object);
    }

    public int refCnt() {
        return this.field_150794_a.refCnt();
    }

    public boolean release() {
        return this.field_150794_a.release();
    }

    public boolean release(int i) {
        return this.field_150794_a.release(i);
    }
}
