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
    public static final DataSerializer<Byte> BYTE = new DataSerializer() {
        public void a(PacketBuffer packetdataserializer, Byte obyte) {
            packetdataserializer.writeByte(obyte.byteValue());
        }

        public Byte b(PacketBuffer packetdataserializer) {
            return Byte.valueOf(packetdataserializer.readByte());
        }

        public DataParameter<Byte> createKey(int i) {
            return new DataParameter(i, this);
        }

        public Byte a(Byte obyte) {
            return obyte;
        }

        public Object read(PacketBuffer packetdataserializer) {
            return this.b(packetdataserializer);
        }
    };
    public static final DataSerializer<Integer> VARINT = new DataSerializer() {
        public void a(PacketBuffer packetdataserializer, Integer integer) {
            packetdataserializer.writeVarInt(integer.intValue());
        }

        public Integer b(PacketBuffer packetdataserializer) {
            return Integer.valueOf(packetdataserializer.readVarInt());
        }

        public DataParameter<Integer> createKey(int i) {
            return new DataParameter(i, this);
        }

        public Integer a(Integer integer) {
            return integer;
        }

        public Object read(PacketBuffer packetdataserializer) {
            return this.b(packetdataserializer);
        }
    };
    public static final DataSerializer<Float> FLOAT = new DataSerializer() {
        public void a(PacketBuffer packetdataserializer, Float ofloat) {
            packetdataserializer.writeFloat(ofloat.floatValue());
        }

        public Float b(PacketBuffer packetdataserializer) {
            return Float.valueOf(packetdataserializer.readFloat());
        }

        public DataParameter<Float> createKey(int i) {
            return new DataParameter(i, this);
        }

        public Float a(Float ofloat) {
            return ofloat;
        }

        public Object read(PacketBuffer packetdataserializer) {
            return this.b(packetdataserializer);
        }
    };
    public static final DataSerializer<String> STRING = new DataSerializer() {
        public void a(PacketBuffer packetdataserializer, String s) {
            packetdataserializer.writeString(s);
        }

        public String b(PacketBuffer packetdataserializer) {
            return packetdataserializer.readString(32767);
        }

        public DataParameter<String> createKey(int i) {
            return new DataParameter(i, this);
        }

        public String a(String s) {
            return s;
        }

        public Object read(PacketBuffer packetdataserializer) {
            return this.b(packetdataserializer);
        }
    };
    public static final DataSerializer<ITextComponent> TEXT_COMPONENT = new DataSerializer() {
        public void a(PacketBuffer packetdataserializer, ITextComponent ichatbasecomponent) {
            packetdataserializer.writeTextComponent(ichatbasecomponent);
        }

        public ITextComponent b(PacketBuffer packetdataserializer) {
            return packetdataserializer.readTextComponent();
        }

        public DataParameter<ITextComponent> createKey(int i) {
            return new DataParameter(i, this);
        }

        public ITextComponent a(ITextComponent ichatbasecomponent) {
            return ichatbasecomponent.createCopy();
        }

        public Object read(PacketBuffer packetdataserializer) {
            return this.b(packetdataserializer);
        }
    };
    public static final DataSerializer<ItemStack> ITEM_STACK = new DataSerializer() {
        public void a(PacketBuffer packetdataserializer, ItemStack itemstack) {
            packetdataserializer.writeItemStack(itemstack);
        }

        public ItemStack b(PacketBuffer packetdataserializer) {
            return packetdataserializer.readItemStack();
        }

        public DataParameter<ItemStack> createKey(int i) {
            return new DataParameter(i, this);
        }

        public ItemStack a(ItemStack itemstack) {
            return itemstack.copy();
        }

        public Object read(PacketBuffer packetdataserializer) {
            return this.b(packetdataserializer);
        }
    };
    public static final DataSerializer<Optional<IBlockState>> OPTIONAL_BLOCK_STATE = new DataSerializer() {
        public void a(PacketBuffer packetdataserializer, Optional<IBlockState> optional) {
            if (optional.isPresent()) {
                packetdataserializer.writeVarInt(Block.getStateId((IBlockState) optional.get()));
            } else {
                packetdataserializer.writeVarInt(0);
            }

        }

        public Optional<IBlockState> b(PacketBuffer packetdataserializer) {
            int i = packetdataserializer.readVarInt();

            return i == 0 ? Optional.absent() : Optional.of(Block.getStateById(i));
        }

        public DataParameter<Optional<IBlockState>> createKey(int i) {
            return new DataParameter(i, this);
        }

        public Optional<IBlockState> a(Optional<IBlockState> optional) {
            return optional;
        }

        public Object read(PacketBuffer packetdataserializer) {
            return this.b(packetdataserializer);
        }
    };
    public static final DataSerializer<Boolean> BOOLEAN = new DataSerializer() {
        public void a(PacketBuffer packetdataserializer, Boolean obool) {
            packetdataserializer.writeBoolean(obool.booleanValue());
        }

        public Boolean b(PacketBuffer packetdataserializer) {
            return Boolean.valueOf(packetdataserializer.readBoolean());
        }

        public DataParameter<Boolean> createKey(int i) {
            return new DataParameter(i, this);
        }

        public Boolean a(Boolean obool) {
            return obool;
        }

        public Object read(PacketBuffer packetdataserializer) {
            return this.b(packetdataserializer);
        }
    };
    public static final DataSerializer<Rotations> ROTATIONS = new DataSerializer() {
        public void a(PacketBuffer packetdataserializer, Rotations vector3f) {
            packetdataserializer.writeFloat(vector3f.getX());
            packetdataserializer.writeFloat(vector3f.getY());
            packetdataserializer.writeFloat(vector3f.getZ());
        }

        public Rotations b(PacketBuffer packetdataserializer) {
            return new Rotations(packetdataserializer.readFloat(), packetdataserializer.readFloat(), packetdataserializer.readFloat());
        }

        public DataParameter<Rotations> createKey(int i) {
            return new DataParameter(i, this);
        }

        public Rotations a(Rotations vector3f) {
            return vector3f;
        }

        public Object read(PacketBuffer packetdataserializer) {
            return this.b(packetdataserializer);
        }
    };
    public static final DataSerializer<BlockPos> BLOCK_POS = new DataSerializer() {
        public void a(PacketBuffer packetdataserializer, BlockPos blockposition) {
            packetdataserializer.writeBlockPos(blockposition);
        }

        public BlockPos b(PacketBuffer packetdataserializer) {
            return packetdataserializer.readBlockPos();
        }

        public DataParameter<BlockPos> createKey(int i) {
            return new DataParameter(i, this);
        }

        public BlockPos a(BlockPos blockposition) {
            return blockposition;
        }

        public Object read(PacketBuffer packetdataserializer) {
            return this.b(packetdataserializer);
        }
    };
    public static final DataSerializer<Optional<BlockPos>> OPTIONAL_BLOCK_POS = new DataSerializer() {
        public void a(PacketBuffer packetdataserializer, Optional<BlockPos> optional) {
            packetdataserializer.writeBoolean(optional.isPresent());
            if (optional.isPresent()) {
                packetdataserializer.writeBlockPos((BlockPos) optional.get());
            }

        }

        public Optional<BlockPos> b(PacketBuffer packetdataserializer) {
            return !packetdataserializer.readBoolean() ? Optional.absent() : Optional.of(packetdataserializer.readBlockPos());
        }

        public DataParameter<Optional<BlockPos>> createKey(int i) {
            return new DataParameter(i, this);
        }

        public Optional<BlockPos> a(Optional<BlockPos> optional) {
            return optional;
        }

        public Object read(PacketBuffer packetdataserializer) {
            return this.b(packetdataserializer);
        }
    };
    public static final DataSerializer<EnumFacing> FACING = new DataSerializer() {
        public void a(PacketBuffer packetdataserializer, EnumFacing enumdirection) {
            packetdataserializer.writeEnumValue((Enum) enumdirection);
        }

        public EnumFacing b(PacketBuffer packetdataserializer) {
            return (EnumFacing) packetdataserializer.readEnumValue(EnumFacing.class);
        }

        public DataParameter<EnumFacing> createKey(int i) {
            return new DataParameter(i, this);
        }

        public EnumFacing a(EnumFacing enumdirection) {
            return enumdirection;
        }

        public Object read(PacketBuffer packetdataserializer) {
            return this.b(packetdataserializer);
        }
    };
    public static final DataSerializer<Optional<UUID>> OPTIONAL_UNIQUE_ID = new DataSerializer() {
        public void a(PacketBuffer packetdataserializer, Optional<UUID> optional) {
            packetdataserializer.writeBoolean(optional.isPresent());
            if (optional.isPresent()) {
                packetdataserializer.writeUniqueId((UUID) optional.get());
            }

        }

        public Optional<UUID> b(PacketBuffer packetdataserializer) {
            return !packetdataserializer.readBoolean() ? Optional.absent() : Optional.of(packetdataserializer.readUniqueId());
        }

        public DataParameter<Optional<UUID>> createKey(int i) {
            return new DataParameter(i, this);
        }

        public Optional<UUID> a(Optional<UUID> optional) {
            return optional;
        }

        public Object read(PacketBuffer packetdataserializer) {
            return this.b(packetdataserializer);
        }
    };
    public static final DataSerializer<NBTTagCompound> COMPOUND_TAG = new DataSerializer() {
        public void a(PacketBuffer packetdataserializer, NBTTagCompound nbttagcompound) {
            packetdataserializer.writeCompoundTag(nbttagcompound);
        }

        public NBTTagCompound b(PacketBuffer packetdataserializer) {
            return packetdataserializer.readCompoundTag();
        }

        public DataParameter<NBTTagCompound> createKey(int i) {
            return new DataParameter(i, this);
        }

        public NBTTagCompound a(NBTTagCompound nbttagcompound) {
            return nbttagcompound.copy();
        }

        public Object read(PacketBuffer packetdataserializer) {
            return this.b(packetdataserializer);
        }
    };

    public static void registerSerializer(DataSerializer<?> datawatcherserializer) {
        DataSerializers.REGISTRY.add(datawatcherserializer);
    }

    @Nullable
    public static DataSerializer<?> getSerializer(int i) {
        return (DataSerializer) DataSerializers.REGISTRY.get(i);
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
