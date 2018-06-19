package net.minecraft.network.datasync;

import com.google.common.base.Optional;
import java.util.UUID;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IntIdentityHashBiMap;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Rotations;
import net.minecraft.util.text.ITextComponent;

public class DataSerializers {

    private static final IntIdentityHashBiMap<DataSerializer<?>> REGISTRY = new IntIdentityHashBiMap(16);
    public static final DataSerializer<Byte> BYTE = new DataSerializer<Byte>() {
        @Override
        public void write(PacketBuffer packetdataserializer, Byte obyte) {
            packetdataserializer.writeByte(obyte.byteValue());
        }

        @Override
        public Byte read(PacketBuffer packetdataserializer) {
            return Byte.valueOf(packetdataserializer.readByte());
        }

        @Override
        public DataParameter<Byte> createKey(int i) {
            return new DataParameter(i, this);
        }

        @Override
        public Byte copyValue(Byte obyte) {
            return obyte;
        }
    };
    public static final DataSerializer<Integer> VARINT = new DataSerializer<Integer>() {
        @Override
        public void write(PacketBuffer packetdataserializer, Integer integer) {
            packetdataserializer.writeVarInt(integer.intValue());
        }

        @Override
        public Integer read(PacketBuffer packetdataserializer) {
            return Integer.valueOf(packetdataserializer.readVarInt());
        }

        @Override
        public DataParameter<Integer> createKey(int i) {
            return new DataParameter(i, this);
        }

        @Override
        public Integer copyValue(Integer integer) {
            return integer;
        }
    };
    public static final DataSerializer<Float> FLOAT = new DataSerializer<Float>() {
        @Override
        public void write(PacketBuffer packetdataserializer, Float ofloat) {
            packetdataserializer.writeFloat(ofloat.floatValue());
        }

        @Override
        public Float read(PacketBuffer packetdataserializer) {
            return Float.valueOf(packetdataserializer.readFloat());
        }

        @Override
        public DataParameter<Float> createKey(int i) {
            return new DataParameter(i, this);
        }

        @Override
        public Float copyValue(Float ofloat) {
            return ofloat;
        }
    };
    public static final DataSerializer<String> STRING = new DataSerializer<String>() {
        @Override
        public void write(PacketBuffer packetdataserializer, String s) {
            packetdataserializer.writeString(s);
        }

        @Override
        public String read(PacketBuffer packetdataserializer) {
            return packetdataserializer.readString(32767);
        }

        @Override
        public DataParameter<String> createKey(int i) {
            return new DataParameter(i, this);
        }

        @Override
        public String copyValue(String s) {
            return s;
        }
    };
    public static final DataSerializer<ITextComponent> TEXT_COMPONENT = new DataSerializer<ITextComponent>() {
        @Override
        public void write(PacketBuffer packetdataserializer, ITextComponent ichatbasecomponent) {
            packetdataserializer.writeTextComponent(ichatbasecomponent);
        }

        @Override
        public ITextComponent read(PacketBuffer packetdataserializer) {
            return packetdataserializer.readTextComponent();
        }

        @Override
        public DataParameter<ITextComponent> createKey(int i) {
            return new DataParameter(i, this);
        }

        @Override
        public ITextComponent copyValue(ITextComponent ichatbasecomponent) {
            return ichatbasecomponent.createCopy();
        }
    };
    public static final DataSerializer<ItemStack> ITEM_STACK = new DataSerializer<ItemStack>() {
        @Override
        public void write(PacketBuffer packetdataserializer, ItemStack itemstack) {
            packetdataserializer.writeItemStack(itemstack);
        }

        @Override
        public ItemStack read(PacketBuffer packetdataserializer) {
            return packetdataserializer.readItemStack();
        }

        @Override
        public DataParameter<ItemStack> createKey(int i) {
            return new DataParameter(i, this);
        }

        @Override
        public ItemStack copyValue(ItemStack itemstack) {
            return itemstack.copy();
        }
    };
    public static final DataSerializer<Optional<IBlockState>> OPTIONAL_BLOCK_STATE = new DataSerializer<Optional<IBlockState>>() {
        @Override
        public void write(PacketBuffer packetdataserializer, Optional<IBlockState> optional) {
            if (optional.isPresent()) {
                packetdataserializer.writeVarInt(Block.getStateId(optional.get()));
            } else {
                packetdataserializer.writeVarInt(0);
            }

        }

        @Override
        public Optional<IBlockState> read(PacketBuffer packetdataserializer) {
            int i = packetdataserializer.readVarInt();

            return i == 0 ? Optional.absent() : Optional.of(Block.getStateById(i));
        }

        @Override
        public DataParameter<Optional<IBlockState>> createKey(int i) {
            return new DataParameter(i, this);
        }

        @Override
        public Optional<IBlockState> copyValue(Optional<IBlockState> optional) {
            return optional;
        }
    };
    public static final DataSerializer<Boolean> BOOLEAN = new DataSerializer<Boolean>() {
        @Override
        public void write(PacketBuffer packetdataserializer, Boolean obool) {
            packetdataserializer.writeBoolean(obool.booleanValue());
        }

        @Override
        public Boolean read(PacketBuffer packetdataserializer) {
            return Boolean.valueOf(packetdataserializer.readBoolean());
        }

        @Override
        public DataParameter<Boolean> createKey(int i) {
            return new DataParameter(i, this);
        }

        @Override
        public Boolean copyValue(Boolean obool) {
            return obool;
        }
    };
    public static final DataSerializer<Rotations> ROTATIONS = new DataSerializer<Rotations>() {
        @Override
        public void write(PacketBuffer packetdataserializer, Rotations vector3f) {
            packetdataserializer.writeFloat(vector3f.getX());
            packetdataserializer.writeFloat(vector3f.getY());
            packetdataserializer.writeFloat(vector3f.getZ());
        }

        @Override
        public Rotations read(PacketBuffer packetdataserializer) {
            return new Rotations(packetdataserializer.readFloat(), packetdataserializer.readFloat(), packetdataserializer.readFloat());
        }

        @Override
        public DataParameter<Rotations> createKey(int i) {
            return new DataParameter(i, this);
        }

        @Override
        public Rotations copyValue(Rotations vector3f) {
            return vector3f;
        }
    };
    public static final DataSerializer<BlockPos> BLOCK_POS = new DataSerializer<BlockPos>() {
        @Override
        public void write(PacketBuffer packetdataserializer, BlockPos blockposition) {
            packetdataserializer.writeBlockPos(blockposition);
        }

        @Override
        public BlockPos read(PacketBuffer packetdataserializer) {
            return packetdataserializer.readBlockPos();
        }

        @Override
        public DataParameter<BlockPos> createKey(int i) {
            return new DataParameter(i, this);
        }

        @Override
        public BlockPos copyValue(BlockPos blockposition) {
            return blockposition;
        }
    };
    public static final DataSerializer<Optional<BlockPos>> OPTIONAL_BLOCK_POS = new DataSerializer<Optional<BlockPos>>() {
        @Override
        public void write(PacketBuffer packetdataserializer, Optional<BlockPos> optional) {
            packetdataserializer.writeBoolean(optional.isPresent());
            if (optional.isPresent()) {
                packetdataserializer.writeBlockPos(optional.get());
            }

        }

        @Override
        public Optional<BlockPos> read(PacketBuffer packetdataserializer) {
            return !packetdataserializer.readBoolean() ? Optional.absent() : Optional.of(packetdataserializer.readBlockPos());
        }

        @Override
        public DataParameter<Optional<BlockPos>> createKey(int i) {
            return new DataParameter(i, this);
        }

        @Override
        public Optional<BlockPos> copyValue(Optional<BlockPos> optional) {
            return optional;
        }
    };
    public static final DataSerializer<EnumFacing> FACING = new DataSerializer<EnumFacing>() {
        @Override
        public void write(PacketBuffer packetdataserializer, EnumFacing enumdirection) {
            packetdataserializer.writeEnumValue(enumdirection);
        }

        @Override
        public EnumFacing read(PacketBuffer packetdataserializer) {
            return packetdataserializer.readEnumValue(EnumFacing.class);
        }

        @Override
        public DataParameter<EnumFacing> createKey(int i) {
            return new DataParameter(i, this);
        }

        @Override
        public EnumFacing copyValue(EnumFacing enumdirection) {
            return enumdirection;
        }
    };
    public static final DataSerializer<Optional<UUID>> OPTIONAL_UNIQUE_ID = new DataSerializer<Optional<UUID>>() {
        @Override
        public void write(PacketBuffer packetdataserializer, Optional<UUID> optional) {
            packetdataserializer.writeBoolean(optional.isPresent());
            if (optional.isPresent()) {
                packetdataserializer.writeUniqueId(optional.get());
            }

        }

        @Override
        public Optional<UUID> read(PacketBuffer packetdataserializer) {
            return !packetdataserializer.readBoolean() ? Optional.absent() : Optional.of(packetdataserializer.readUniqueId());
        }

        @Override
        public DataParameter<Optional<UUID>> createKey(int i) {
            return new DataParameter(i, this);
        }

        @Override
        public Optional<UUID> copyValue(Optional<UUID> optional) {
            return optional;
        }
    };
    public static final DataSerializer<NBTTagCompound> COMPOUND_TAG = new DataSerializer<NBTTagCompound>() {
        @Override
        public void write(PacketBuffer packetdataserializer, NBTTagCompound nbttagcompound) {
            packetdataserializer.writeCompoundTag(nbttagcompound);
        }

        @Override
        public NBTTagCompound read(PacketBuffer packetdataserializer) {
            return packetdataserializer.readCompoundTag();
        }

        @Override
        public DataParameter<NBTTagCompound> createKey(int i) {
            return new DataParameter(i, this);
        }

        @Override
        public NBTTagCompound copyValue(NBTTagCompound nbttagcompound) {
            return nbttagcompound.copy();
        }
    };

    public static void registerSerializer(DataSerializer<?> datawatcherserializer) {
        DataSerializers.REGISTRY.add(datawatcherserializer);
    }

    @Nullable
    public static DataSerializer<?> getSerializer(int i) {
        return DataSerializers.REGISTRY.get(i);
    }

    public static int getSerializerId(DataSerializer<?> datawatcherserializer) {
        return DataSerializers.REGISTRY.getId(datawatcherserializer);
    }

    static {
        registerSerializer(DataSerializers.BYTE);
        registerSerializer(DataSerializers.VARINT);
        registerSerializer(DataSerializers.FLOAT);
        registerSerializer(DataSerializers.STRING);
        registerSerializer(DataSerializers.TEXT_COMPONENT);
        registerSerializer(DataSerializers.ITEM_STACK);
        registerSerializer(DataSerializers.BOOLEAN);
        registerSerializer(DataSerializers.ROTATIONS);
        registerSerializer(DataSerializers.BLOCK_POS);
        registerSerializer(DataSerializers.OPTIONAL_BLOCK_POS);
        registerSerializer(DataSerializers.FACING);
        registerSerializer(DataSerializers.OPTIONAL_UNIQUE_ID);
        registerSerializer(DataSerializers.OPTIONAL_BLOCK_STATE);
        registerSerializer(DataSerializers.COMPOUND_TAG);
    }
}
