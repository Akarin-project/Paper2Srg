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

    private final ByteBuf buf;

    public PacketBuffer(ByteBuf bytebuf) {
        this.buf = bytebuf;
    }

    public static int countBytes(int i) { return PacketBuffer.getVarIntSize(i); } // Paper - Anti-Xray - OBFHELPER
    public static int getVarIntSize(int i) {
        for (int j = 1; j < 5; ++j) {
            if ((i & -1 << j * 7) == 0) {
                return j;
            }
        }

        return 5;
    }

    public PacketBuffer writeByteArray(byte[] abyte) {
        this.writeVarInt(abyte.length);
        this.writeBytes(abyte);
        return this;
    }

    public byte[] readByteArray() {
        return this.readByteArray(this.readableBytes());
    }

    public byte[] readByteArray(int i) {
        int j = this.readVarInt();

        if (j > i) {
            throw new DecoderException("ByteArray with size " + j + " is bigger than allowed " + i);
        } else {
            byte[] abyte = new byte[j];

            this.readBytes(abyte);
            return abyte;
        }
    }

    public PacketBuffer writeVarIntArray(int[] aint) {
        this.writeVarInt(aint.length);
        int[] aint1 = aint;
        int i = aint.length;

        for (int j = 0; j < i; ++j) {
            int k = aint1[j];

            this.writeVarInt(k);
        }

        return this;
    }

    public int[] readVarIntArray() {
        return this.readVarIntArray(this.readableBytes());
    }

    public int[] readVarIntArray(int i) {
        int j = this.readVarInt();

        if (j > i) {
            throw new DecoderException("VarIntArray with size " + j + " is bigger than allowed " + i);
        } else {
            int[] aint = new int[j];

            for (int k = 0; k < aint.length; ++k) {
                aint[k] = this.readVarInt();
            }

            return aint;
        }
    }

    public PacketBuffer writeLongArray(long[] along) {
        this.writeVarInt(along.length);
        long[] along1 = along;
        int i = along.length;

        for (int j = 0; j < i; ++j) {
            long k = along1[j];

            this.writeLong(k);
        }

        return this;
    }

    public BlockPos readBlockPos() {
        return BlockPos.fromLong(this.readLong());
    }

    public PacketBuffer writeBlockPos(BlockPos blockposition) {
        this.writeLong(blockposition.toLong());
        return this;
    }

    public ITextComponent readTextComponent() {
        return ITextComponent.Serializer.jsonToComponent(this.readString(32767));
    }

    public PacketBuffer writeTextComponent(ITextComponent ichatbasecomponent) {
        return this.writeString(ITextComponent.Serializer.componentToJson(ichatbasecomponent));
    }

    public <T extends Enum<T>> T readEnumValue(Class<T> oclass) {
        return ((T[]) oclass.getEnumConstants())[this.readVarInt()]; // CraftBukkit - fix decompile error
    }

    public PacketBuffer writeEnumValue(Enum<?> oenum) {
        return this.writeVarInt(oenum.ordinal());
    }

    public int readVarInt() {
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

    public long readVarLong() {
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

    public PacketBuffer writeUniqueId(UUID uuid) {
        this.writeLong(uuid.getMostSignificantBits());
        this.writeLong(uuid.getLeastSignificantBits());
        return this;
    }

    public UUID readUniqueId() {
        return new UUID(this.readLong(), this.readLong());
    }

    public PacketBuffer writeVarInt(int i) {
        while ((i & -128) != 0) {
            this.writeByte(i & 127 | 128);
            i >>>= 7;
        }

        this.writeByte(i);
        return this;
    }

    public PacketBuffer writeVarLong(long i) {
        while ((i & -128L) != 0L) {
            this.writeByte((int) (i & 127L) | 128);
            i >>>= 7;
        }

        this.writeByte((int) i);
        return this;
    }

    public PacketBuffer writeCompoundTag(@Nullable NBTTagCompound nbttagcompound) {
        if (nbttagcompound == null) {
            this.writeByte(0);
        } else {
            try {
                CompressedStreamTools.write(nbttagcompound, (DataOutput) (new ByteBufOutputStream(this)));
            } catch (Exception ioexception) { // CraftBukkit - IOException -> Exception
                throw new EncoderException(ioexception);
            }
        }

        return this;
    }

    @Nullable
    public NBTTagCompound readCompoundTag() {
        int i = this.readerIndex();
        byte b0 = this.readByte();

        if (b0 == 0) {
            return null;
        } else {
            this.readerIndex(i);

            try {
                return CompressedStreamTools.read((DataInput) (new ByteBufInputStream(this)), new NBTSizeTracker(2097152L));
            } catch (IOException ioexception) {
                throw new EncoderException(ioexception);
            }
        }
    }

    public PacketBuffer writeItemStack(ItemStack itemstack) {
        if (itemstack.isEmpty() || itemstack.getItem() == null) { // CraftBukkit - NPE fix itemstack.getItem()
            this.writeShort(-1);
        } else {
            this.writeShort(Item.getIdFromItem(itemstack.getItem()));
            this.writeByte(itemstack.getCount());
            this.writeShort(itemstack.getMetadata());
            NBTTagCompound nbttagcompound = null;

            if (itemstack.getItem().isDamageable() || itemstack.getItem().getShareTag()) {
                // Spigot start - filter
                itemstack = itemstack.copy();
                CraftItemStack.setItemMeta(itemstack, CraftItemStack.getItemMeta(itemstack));
                // Spigot end
                nbttagcompound = itemstack.getTagCompound();
            }

            this.writeCompoundTag(nbttagcompound);
        }

        return this;
    }

    public ItemStack readItemStack() {
        short short0 = this.readShort();

        if (short0 < 0) {
            return ItemStack.EMPTY;
        } else {
            byte b0 = this.readByte();
            short short1 = this.readShort();
            ItemStack itemstack = new ItemStack(Item.getItemById(short0), b0, short1);

            itemstack.setTagCompound(this.readCompoundTag());
            // CraftBukkit start
            if (itemstack.getTagCompound() != null) {
                CraftItemStack.setItemMeta(itemstack, CraftItemStack.getItemMeta(itemstack));
            }
            // CraftBukkit end
            return itemstack;
        }
    }

    public String readString(int i) {
        int j = this.readVarInt();

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

    public PacketBuffer writeString(String s) {
        byte[] abyte = s.getBytes(StandardCharsets.UTF_8);

        if (abyte.length > 44767) { // Paper - raise limit a bit more as normal means can trigger this
            throw new EncoderException("String too big (was " + s.length() + " bytes encoded, max " + 44767 + ")"); // Paper
        } else {
            this.writeVarInt(abyte.length);
            this.writeBytes(abyte);
            return this;
        }
    }

    public ResourceLocation readResourceLocation() {
        return new ResourceLocation(this.readString(32767));
    }

    public PacketBuffer writeResourceLocation(ResourceLocation minecraftkey) {
        this.writeString(minecraftkey.toString());
        return this;
    }

    public Date readTime() {
        return new Date(this.readLong());
    }

    public PacketBuffer writeTime(Date date) {
        this.writeLong(date.getTime());
        return this;
    }

    public int capacity() {
        return this.buf.capacity();
    }

    public ByteBuf capacity(int i) {
        return this.buf.capacity(i);
    }

    public int maxCapacity() {
        return this.buf.maxCapacity();
    }

    public ByteBufAllocator alloc() {
        return this.buf.alloc();
    }

    public ByteOrder order() {
        return this.buf.order();
    }

    public ByteBuf order(ByteOrder byteorder) {
        return this.buf.order(byteorder);
    }

    public ByteBuf unwrap() {
        return this.buf.unwrap();
    }

    public boolean isDirect() {
        return this.buf.isDirect();
    }

    public boolean isReadOnly() {
        return this.buf.isReadOnly();
    }

    public ByteBuf asReadOnly() {
        return this.buf.asReadOnly();
    }

    public int readerIndex() {
        return this.buf.readerIndex();
    }

    public ByteBuf readerIndex(int i) {
        return this.buf.readerIndex(i);
    }

    public int writerIndex() {
        return this.buf.writerIndex();
    }

    public ByteBuf writerIndex(int i) {
        return this.buf.writerIndex(i);
    }

    public ByteBuf setIndex(int i, int j) {
        return this.buf.setIndex(i, j);
    }

    public int readableBytes() {
        return this.buf.readableBytes();
    }

    public int writableBytes() {
        return this.buf.writableBytes();
    }

    public int maxWritableBytes() {
        return this.buf.maxWritableBytes();
    }

    public boolean isReadable() {
        return this.buf.isReadable();
    }

    public boolean isReadable(int i) {
        return this.buf.isReadable(i);
    }

    public boolean isWritable() {
        return this.buf.isWritable();
    }

    public boolean isWritable(int i) {
        return this.buf.isWritable(i);
    }

    public ByteBuf clear() {
        return this.buf.clear();
    }

    public ByteBuf markReaderIndex() {
        return this.buf.markReaderIndex();
    }

    public ByteBuf resetReaderIndex() {
        return this.buf.resetReaderIndex();
    }

    public ByteBuf markWriterIndex() {
        return this.buf.markWriterIndex();
    }

    public ByteBuf resetWriterIndex() {
        return this.buf.resetWriterIndex();
    }

    public ByteBuf discardReadBytes() {
        return this.buf.discardReadBytes();
    }

    public ByteBuf discardSomeReadBytes() {
        return this.buf.discardSomeReadBytes();
    }

    public ByteBuf ensureWritable(int i) {
        return this.buf.ensureWritable(i);
    }

    public int ensureWritable(int i, boolean flag) {
        return this.buf.ensureWritable(i, flag);
    }

    public boolean getBoolean(int i) {
        return this.buf.getBoolean(i);
    }

    public byte getByte(int i) {
        return this.buf.getByte(i);
    }

    public short getUnsignedByte(int i) {
        return this.buf.getUnsignedByte(i);
    }

    public short getShort(int i) {
        return this.buf.getShort(i);
    }

    public short getShortLE(int i) {
        return this.buf.getShortLE(i);
    }

    public int getUnsignedShort(int i) {
        return this.buf.getUnsignedShort(i);
    }

    public int getUnsignedShortLE(int i) {
        return this.buf.getUnsignedShortLE(i);
    }

    public int getMedium(int i) {
        return this.buf.getMedium(i);
    }

    public int getMediumLE(int i) {
        return this.buf.getMediumLE(i);
    }

    public int getUnsignedMedium(int i) {
        return this.buf.getUnsignedMedium(i);
    }

    public int getUnsignedMediumLE(int i) {
        return this.buf.getUnsignedMediumLE(i);
    }

    public int getInt(int i) {
        return this.buf.getInt(i);
    }

    public int getIntLE(int i) {
        return this.buf.getIntLE(i);
    }

    public long getUnsignedInt(int i) {
        return this.buf.getUnsignedInt(i);
    }

    public long getUnsignedIntLE(int i) {
        return this.buf.getUnsignedIntLE(i);
    }

    public long getLong(int i) {
        return this.buf.getLong(i);
    }

    public long getLongLE(int i) {
        return this.buf.getLongLE(i);
    }

    public char getChar(int i) {
        return this.buf.getChar(i);
    }

    public float getFloat(int i) {
        return this.buf.getFloat(i);
    }

    public double getDouble(int i) {
        return this.buf.getDouble(i);
    }

    public ByteBuf getBytes(int i, ByteBuf bytebuf) {
        return this.buf.getBytes(i, bytebuf);
    }

    public ByteBuf getBytes(int i, ByteBuf bytebuf, int j) {
        return this.buf.getBytes(i, bytebuf, j);
    }

    public ByteBuf getBytes(int i, ByteBuf bytebuf, int j, int k) {
        return this.buf.getBytes(i, bytebuf, j, k);
    }

    public ByteBuf getBytes(int i, byte[] abyte) {
        return this.buf.getBytes(i, abyte);
    }

    public ByteBuf getBytes(int i, byte[] abyte, int j, int k) {
        return this.buf.getBytes(i, abyte, j, k);
    }

    public ByteBuf getBytes(int i, ByteBuffer bytebuffer) {
        return this.buf.getBytes(i, bytebuffer);
    }

    public ByteBuf getBytes(int i, OutputStream outputstream, int j) throws IOException {
        return this.buf.getBytes(i, outputstream, j);
    }

    public int getBytes(int i, GatheringByteChannel gatheringbytechannel, int j) throws IOException {
        return this.buf.getBytes(i, gatheringbytechannel, j);
    }

    public int getBytes(int i, FileChannel filechannel, long j, int k) throws IOException {
        return this.buf.getBytes(i, filechannel, j, k);
    }

    public CharSequence getCharSequence(int i, int j, Charset charset) {
        return this.buf.getCharSequence(i, j, charset);
    }

    public ByteBuf setBoolean(int i, boolean flag) {
        return this.buf.setBoolean(i, flag);
    }

    public ByteBuf setByte(int i, int j) {
        return this.buf.setByte(i, j);
    }

    public ByteBuf setShort(int i, int j) {
        return this.buf.setShort(i, j);
    }

    public ByteBuf setShortLE(int i, int j) {
        return this.buf.setShortLE(i, j);
    }

    public ByteBuf setMedium(int i, int j) {
        return this.buf.setMedium(i, j);
    }

    public ByteBuf setMediumLE(int i, int j) {
        return this.buf.setMediumLE(i, j);
    }

    public ByteBuf setInt(int i, int j) {
        return this.buf.setInt(i, j);
    }

    public ByteBuf setIntLE(int i, int j) {
        return this.buf.setIntLE(i, j);
    }

    public ByteBuf setLong(int i, long j) {
        return this.buf.setLong(i, j);
    }

    public ByteBuf setLongLE(int i, long j) {
        return this.buf.setLongLE(i, j);
    }

    public ByteBuf setChar(int i, int j) {
        return this.buf.setChar(i, j);
    }

    public ByteBuf setFloat(int i, float f) {
        return this.buf.setFloat(i, f);
    }

    public ByteBuf setDouble(int i, double d0) {
        return this.buf.setDouble(i, d0);
    }

    public ByteBuf setBytes(int i, ByteBuf bytebuf) {
        return this.buf.setBytes(i, bytebuf);
    }

    public ByteBuf setBytes(int i, ByteBuf bytebuf, int j) {
        return this.buf.setBytes(i, bytebuf, j);
    }

    public ByteBuf setBytes(int i, ByteBuf bytebuf, int j, int k) {
        return this.buf.setBytes(i, bytebuf, j, k);
    }

    public ByteBuf setBytes(int i, byte[] abyte) {
        return this.buf.setBytes(i, abyte);
    }

    public ByteBuf setBytes(int i, byte[] abyte, int j, int k) {
        return this.buf.setBytes(i, abyte, j, k);
    }

    public ByteBuf setBytes(int i, ByteBuffer bytebuffer) {
        return this.buf.setBytes(i, bytebuffer);
    }

    public int setBytes(int i, InputStream inputstream, int j) throws IOException {
        return this.buf.setBytes(i, inputstream, j);
    }

    public int setBytes(int i, ScatteringByteChannel scatteringbytechannel, int j) throws IOException {
        return this.buf.setBytes(i, scatteringbytechannel, j);
    }

    public int setBytes(int i, FileChannel filechannel, long j, int k) throws IOException {
        return this.buf.setBytes(i, filechannel, j, k);
    }

    public ByteBuf setZero(int i, int j) {
        return this.buf.setZero(i, j);
    }

    public int setCharSequence(int i, CharSequence charsequence, Charset charset) {
        return this.buf.setCharSequence(i, charsequence, charset);
    }

    public boolean readBoolean() {
        return this.buf.readBoolean();
    }

    public byte readByte() {
        return this.buf.readByte();
    }

    public short readUnsignedByte() {
        return this.buf.readUnsignedByte();
    }

    public short readShort() {
        return this.buf.readShort();
    }

    public short readShortLE() {
        return this.buf.readShortLE();
    }

    public int readUnsignedShort() {
        return this.buf.readUnsignedShort();
    }

    public int readUnsignedShortLE() {
        return this.buf.readUnsignedShortLE();
    }

    public int readMedium() {
        return this.buf.readMedium();
    }

    public int readMediumLE() {
        return this.buf.readMediumLE();
    }

    public int readUnsignedMedium() {
        return this.buf.readUnsignedMedium();
    }

    public int readUnsignedMediumLE() {
        return this.buf.readUnsignedMediumLE();
    }

    public int readInt() {
        return this.buf.readInt();
    }

    public int readIntLE() {
        return this.buf.readIntLE();
    }

    public long readUnsignedInt() {
        return this.buf.readUnsignedInt();
    }

    public long readUnsignedIntLE() {
        return this.buf.readUnsignedIntLE();
    }

    public long readLong() {
        return this.buf.readLong();
    }

    public long readLongLE() {
        return this.buf.readLongLE();
    }

    public char readChar() {
        return this.buf.readChar();
    }

    public float readFloat() {
        return this.buf.readFloat();
    }

    public double readDouble() {
        return this.buf.readDouble();
    }

    public ByteBuf readBytes(int i) {
        return this.buf.readBytes(i);
    }

    public ByteBuf readSlice(int i) {
        return this.buf.readSlice(i);
    }

    public ByteBuf readRetainedSlice(int i) {
        return this.buf.readRetainedSlice(i);
    }

    public ByteBuf readBytes(ByteBuf bytebuf) {
        return this.buf.readBytes(bytebuf);
    }

    public ByteBuf readBytes(ByteBuf bytebuf, int i) {
        return this.buf.readBytes(bytebuf, i);
    }

    public ByteBuf readBytes(ByteBuf bytebuf, int i, int j) {
        return this.buf.readBytes(bytebuf, i, j);
    }

    public ByteBuf readBytes(byte[] abyte) {
        return this.buf.readBytes(abyte);
    }

    public ByteBuf readBytes(byte[] abyte, int i, int j) {
        return this.buf.readBytes(abyte, i, j);
    }

    public ByteBuf readBytes(ByteBuffer bytebuffer) {
        return this.buf.readBytes(bytebuffer);
    }

    public ByteBuf readBytes(OutputStream outputstream, int i) throws IOException {
        return this.buf.readBytes(outputstream, i);
    }

    public int readBytes(GatheringByteChannel gatheringbytechannel, int i) throws IOException {
        return this.buf.readBytes(gatheringbytechannel, i);
    }

    public CharSequence readCharSequence(int i, Charset charset) {
        return this.buf.readCharSequence(i, charset);
    }

    public int readBytes(FileChannel filechannel, long i, int j) throws IOException {
        return this.buf.readBytes(filechannel, i, j);
    }

    public ByteBuf skipBytes(int i) {
        return this.buf.skipBytes(i);
    }

    public ByteBuf writeBoolean(boolean flag) {
        return this.buf.writeBoolean(flag);
    }

    public ByteBuf writeByte(int i) {
        return this.buf.writeByte(i);
    }

    public ByteBuf writeShort(int i) {
        return this.buf.writeShort(i);
    }

    public ByteBuf writeShortLE(int i) {
        return this.buf.writeShortLE(i);
    }

    public ByteBuf writeMedium(int i) {
        return this.buf.writeMedium(i);
    }

    public ByteBuf writeMediumLE(int i) {
        return this.buf.writeMediumLE(i);
    }

    public ByteBuf writeInt(int i) {
        return this.buf.writeInt(i);
    }

    public ByteBuf writeIntLE(int i) {
        return this.buf.writeIntLE(i);
    }

    public ByteBuf writeLong(long i) {
        return this.buf.writeLong(i);
    }

    public ByteBuf writeLongLE(long i) {
        return this.buf.writeLongLE(i);
    }

    public ByteBuf writeChar(int i) {
        return this.buf.writeChar(i);
    }

    public ByteBuf writeFloat(float f) {
        return this.buf.writeFloat(f);
    }

    public ByteBuf writeDouble(double d0) {
        return this.buf.writeDouble(d0);
    }

    public ByteBuf writeBytes(ByteBuf bytebuf) {
        return this.buf.writeBytes(bytebuf);
    }

    public ByteBuf writeBytes(ByteBuf bytebuf, int i) {
        return this.buf.writeBytes(bytebuf, i);
    }

    public ByteBuf writeBytes(ByteBuf bytebuf, int i, int j) {
        return this.buf.writeBytes(bytebuf, i, j);
    }

    public ByteBuf writeBytes(byte[] abyte) {
        return this.buf.writeBytes(abyte);
    }

    public ByteBuf writeBytes(byte[] abyte, int i, int j) {
        return this.buf.writeBytes(abyte, i, j);
    }

    public ByteBuf writeBytes(ByteBuffer bytebuffer) {
        return this.buf.writeBytes(bytebuffer);
    }

    public int writeBytes(InputStream inputstream, int i) throws IOException {
        return this.buf.writeBytes(inputstream, i);
    }

    public int writeBytes(ScatteringByteChannel scatteringbytechannel, int i) throws IOException {
        return this.buf.writeBytes(scatteringbytechannel, i);
    }

    public int writeBytes(FileChannel filechannel, long i, int j) throws IOException {
        return this.buf.writeBytes(filechannel, i, j);
    }

    public ByteBuf writeZero(int i) {
        return this.buf.writeZero(i);
    }

    public int writeCharSequence(CharSequence charsequence, Charset charset) {
        return this.buf.writeCharSequence(charsequence, charset);
    }

    public int indexOf(int i, int j, byte b0) {
        return this.buf.indexOf(i, j, b0);
    }

    public int bytesBefore(byte b0) {
        return this.buf.bytesBefore(b0);
    }

    public int bytesBefore(int i, byte b0) {
        return this.buf.bytesBefore(i, b0);
    }

    public int bytesBefore(int i, int j, byte b0) {
        return this.buf.bytesBefore(i, j, b0);
    }

    public int forEachByte(ByteProcessor byteprocessor) {
        return this.buf.forEachByte(byteprocessor);
    }

    public int forEachByte(int i, int j, ByteProcessor byteprocessor) {
        return this.buf.forEachByte(i, j, byteprocessor);
    }

    public int forEachByteDesc(ByteProcessor byteprocessor) {
        return this.buf.forEachByteDesc(byteprocessor);
    }

    public int forEachByteDesc(int i, int j, ByteProcessor byteprocessor) {
        return this.buf.forEachByteDesc(i, j, byteprocessor);
    }

    public ByteBuf copy() {
        return this.buf.copy();
    }

    public ByteBuf copy(int i, int j) {
        return this.buf.copy(i, j);
    }

    public ByteBuf slice() {
        return this.buf.slice();
    }

    public ByteBuf retainedSlice() {
        return this.buf.retainedSlice();
    }

    public ByteBuf slice(int i, int j) {
        return this.buf.slice(i, j);
    }

    public ByteBuf retainedSlice(int i, int j) {
        return this.buf.retainedSlice(i, j);
    }

    public ByteBuf duplicate() {
        return this.buf.duplicate();
    }

    public ByteBuf retainedDuplicate() {
        return this.buf.retainedDuplicate();
    }

    public int nioBufferCount() {
        return this.buf.nioBufferCount();
    }

    public ByteBuffer nioBuffer() {
        return this.buf.nioBuffer();
    }

    public ByteBuffer nioBuffer(int i, int j) {
        return this.buf.nioBuffer(i, j);
    }

    public ByteBuffer internalNioBuffer(int i, int j) {
        return this.buf.internalNioBuffer(i, j);
    }

    public ByteBuffer[] nioBuffers() {
        return this.buf.nioBuffers();
    }

    public ByteBuffer[] nioBuffers(int i, int j) {
        return this.buf.nioBuffers(i, j);
    }

    public boolean hasArray() {
        return this.buf.hasArray();
    }

    public byte[] array() {
        return this.buf.array();
    }

    public int arrayOffset() {
        return this.buf.arrayOffset();
    }

    public boolean hasMemoryAddress() {
        return this.buf.hasMemoryAddress();
    }

    public long memoryAddress() {
        return this.buf.memoryAddress();
    }

    public String toString(Charset charset) {
        return this.buf.toString(charset);
    }

    public String toString(int i, int j, Charset charset) {
        return this.buf.toString(i, j, charset);
    }

    public int hashCode() {
        return this.buf.hashCode();
    }

    public boolean equals(Object object) {
        return this.buf.equals(object);
    }

    public int compareTo(ByteBuf bytebuf) {
        return this.buf.compareTo(bytebuf);
    }

    public String toString() {
        return this.buf.toString();
    }

    public ByteBuf retain(int i) {
        return this.buf.retain(i);
    }

    public ByteBuf retain() {
        return this.buf.retain();
    }

    public ByteBuf touch() {
        return this.buf.touch();
    }

    public ByteBuf touch(Object object) {
        return this.buf.touch(object);
    }

    public int refCnt() {
        return this.buf.refCnt();
    }

    public boolean release() {
        return this.buf.release();
    }

    public boolean release(int i) {
        return this.buf.release(i);
    }
}
