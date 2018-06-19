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

    private static final IntIdentityHashBiMap<DataSerializer<?>> field_187204_n = new IntIdentityHashBiMap(16);
    public static final DataSerializer<Byte> field_187191_a = new DataSerializer() {
        public void a(PacketBuffer packetdataserializer, Byte obyte) {
            packetdataserializer.writeByte(obyte.byteValue());
        }

        public Byte b(PacketBuffer packetdataserializer) {
            return Byte.valueOf(packetdataserializer.readByte());
        }

        public DataParameter<Byte> func_187161_a(int i) {
            return new DataParameter(i, this);
        }

        public Byte a(Byte obyte) {
            return obyte;
        }

        public Object func_187159_a(PacketBuffer packetdataserializer) {
            return this.b(packetdataserializer);
        }
    };
    public static final DataSerializer<Integer> field_187192_b = new DataSerializer() {
        public void a(PacketBuffer packetdataserializer, Integer integer) {
            packetdataserializer.func_150787_b(integer.intValue());
        }

        public Integer b(PacketBuffer packetdataserializer) {
            return Integer.valueOf(packetdataserializer.func_150792_a());
        }

        public DataParameter<Integer> func_187161_a(int i) {
            return new DataParameter(i, this);
        }

        public Integer a(Integer integer) {
            return integer;
        }

        public Object func_187159_a(PacketBuffer packetdataserializer) {
            return this.b(packetdataserializer);
        }
    };
    public static final DataSerializer<Float> field_187193_c = new DataSerializer() {
        public void a(PacketBuffer packetdataserializer, Float ofloat) {
            packetdataserializer.writeFloat(ofloat.floatValue());
        }

        public Float b(PacketBuffer packetdataserializer) {
            return Float.valueOf(packetdataserializer.readFloat());
        }

        public DataParameter<Float> func_187161_a(int i) {
            return new DataParameter(i, this);
        }

        public Float a(Float ofloat) {
            return ofloat;
        }

        public Object func_187159_a(PacketBuffer packetdataserializer) {
            return this.b(packetdataserializer);
        }
    };
    public static final DataSerializer<String> field_187194_d = new DataSerializer() {
        public void a(PacketBuffer packetdataserializer, String s) {
            packetdataserializer.func_180714_a(s);
        }

        public String b(PacketBuffer packetdataserializer) {
            return packetdataserializer.func_150789_c(32767);
        }

        public DataParameter<String> func_187161_a(int i) {
            return new DataParameter(i, this);
        }

        public String a(String s) {
            return s;
        }

        public Object func_187159_a(PacketBuffer packetdataserializer) {
            return this.b(packetdataserializer);
        }
    };
    public static final DataSerializer<ITextComponent> field_187195_e = new DataSerializer() {
        public void a(PacketBuffer packetdataserializer, ITextComponent ichatbasecomponent) {
            packetdataserializer.func_179256_a(ichatbasecomponent);
        }

        public ITextComponent b(PacketBuffer packetdataserializer) {
            return packetdataserializer.func_179258_d();
        }

        public DataParameter<ITextComponent> func_187161_a(int i) {
            return new DataParameter(i, this);
        }

        public ITextComponent a(ITextComponent ichatbasecomponent) {
            return ichatbasecomponent.func_150259_f();
        }

        public Object func_187159_a(PacketBuffer packetdataserializer) {
            return this.b(packetdataserializer);
        }
    };
    public static final DataSerializer<ItemStack> field_187196_f = new DataSerializer() {
        public void a(PacketBuffer packetdataserializer, ItemStack itemstack) {
            packetdataserializer.func_150788_a(itemstack);
        }

        public ItemStack b(PacketBuffer packetdataserializer) {
            return packetdataserializer.func_150791_c();
        }

        public DataParameter<ItemStack> func_187161_a(int i) {
            return new DataParameter(i, this);
        }

        public ItemStack a(ItemStack itemstack) {
            return itemstack.func_77946_l();
        }

        public Object func_187159_a(PacketBuffer packetdataserializer) {
            return this.b(packetdataserializer);
        }
    };
    public static final DataSerializer<Optional<IBlockState>> field_187197_g = new DataSerializer() {
        public void a(PacketBuffer packetdataserializer, Optional<IBlockState> optional) {
            if (optional.isPresent()) {
                packetdataserializer.func_150787_b(Block.func_176210_f((IBlockState) optional.get()));
            } else {
                packetdataserializer.func_150787_b(0);
            }

        }

        public Optional<IBlockState> b(PacketBuffer packetdataserializer) {
            int i = packetdataserializer.func_150792_a();

            return i == 0 ? Optional.absent() : Optional.of(Block.func_176220_d(i));
        }

        public DataParameter<Optional<IBlockState>> func_187161_a(int i) {
            return new DataParameter(i, this);
        }

        public Optional<IBlockState> a(Optional<IBlockState> optional) {
            return optional;
        }

        public Object func_187159_a(PacketBuffer packetdataserializer) {
            return this.b(packetdataserializer);
        }
    };
    public static final DataSerializer<Boolean> field_187198_h = new DataSerializer() {
        public void a(PacketBuffer packetdataserializer, Boolean obool) {
            packetdataserializer.writeBoolean(obool.booleanValue());
        }

        public Boolean b(PacketBuffer packetdataserializer) {
            return Boolean.valueOf(packetdataserializer.readBoolean());
        }

        public DataParameter<Boolean> func_187161_a(int i) {
            return new DataParameter(i, this);
        }

        public Boolean a(Boolean obool) {
            return obool;
        }

        public Object func_187159_a(PacketBuffer packetdataserializer) {
            return this.b(packetdataserializer);
        }
    };
    public static final DataSerializer<Rotations> field_187199_i = new DataSerializer() {
        public void a(PacketBuffer packetdataserializer, Rotations vector3f) {
            packetdataserializer.writeFloat(vector3f.func_179415_b());
            packetdataserializer.writeFloat(vector3f.func_179416_c());
            packetdataserializer.writeFloat(vector3f.func_179413_d());
        }

        public Rotations b(PacketBuffer packetdataserializer) {
            return new Rotations(packetdataserializer.readFloat(), packetdataserializer.readFloat(), packetdataserializer.readFloat());
        }

        public DataParameter<Rotations> func_187161_a(int i) {
            return new DataParameter(i, this);
        }

        public Rotations a(Rotations vector3f) {
            return vector3f;
        }

        public Object func_187159_a(PacketBuffer packetdataserializer) {
            return this.b(packetdataserializer);
        }
    };
    public static final DataSerializer<BlockPos> field_187200_j = new DataSerializer() {
        public void a(PacketBuffer packetdataserializer, BlockPos blockposition) {
            packetdataserializer.func_179255_a(blockposition);
        }

        public BlockPos b(PacketBuffer packetdataserializer) {
            return packetdataserializer.func_179259_c();
        }

        public DataParameter<BlockPos> func_187161_a(int i) {
            return new DataParameter(i, this);
        }

        public BlockPos a(BlockPos blockposition) {
            return blockposition;
        }

        public Object func_187159_a(PacketBuffer packetdataserializer) {
            return this.b(packetdataserializer);
        }
    };
    public static final DataSerializer<Optional<BlockPos>> field_187201_k = new DataSerializer() {
        public void a(PacketBuffer packetdataserializer, Optional<BlockPos> optional) {
            packetdataserializer.writeBoolean(optional.isPresent());
            if (optional.isPresent()) {
                packetdataserializer.func_179255_a((BlockPos) optional.get());
            }

        }

        public Optional<BlockPos> b(PacketBuffer packetdataserializer) {
            return !packetdataserializer.readBoolean() ? Optional.absent() : Optional.of(packetdataserializer.func_179259_c());
        }

        public DataParameter<Optional<BlockPos>> func_187161_a(int i) {
            return new DataParameter(i, this);
        }

        public Optional<BlockPos> a(Optional<BlockPos> optional) {
            return optional;
        }

        public Object func_187159_a(PacketBuffer packetdataserializer) {
            return this.b(packetdataserializer);
        }
    };
    public static final DataSerializer<EnumFacing> field_187202_l = new DataSerializer() {
        public void a(PacketBuffer packetdataserializer, EnumFacing enumdirection) {
            packetdataserializer.func_179249_a((Enum) enumdirection);
        }

        public EnumFacing b(PacketBuffer packetdataserializer) {
            return (EnumFacing) packetdataserializer.func_179257_a(EnumFacing.class);
        }

        public DataParameter<EnumFacing> func_187161_a(int i) {
            return new DataParameter(i, this);
        }

        public EnumFacing a(EnumFacing enumdirection) {
            return enumdirection;
        }

        public Object func_187159_a(PacketBuffer packetdataserializer) {
            return this.b(packetdataserializer);
        }
    };
    public static final DataSerializer<Optional<UUID>> field_187203_m = new DataSerializer() {
        public void a(PacketBuffer packetdataserializer, Optional<UUID> optional) {
            packetdataserializer.writeBoolean(optional.isPresent());
            if (optional.isPresent()) {
                packetdataserializer.func_179252_a((UUID) optional.get());
            }

        }

        public Optional<UUID> b(PacketBuffer packetdataserializer) {
            return !packetdataserializer.readBoolean() ? Optional.absent() : Optional.of(packetdataserializer.func_179253_g());
        }

        public DataParameter<Optional<UUID>> func_187161_a(int i) {
            return new DataParameter(i, this);
        }

        public Optional<UUID> a(Optional<UUID> optional) {
            return optional;
        }

        public Object func_187159_a(PacketBuffer packetdataserializer) {
            return this.b(packetdataserializer);
        }
    };
    public static final DataSerializer<NBTTagCompound> field_192734_n = new DataSerializer() {
        public void a(PacketBuffer packetdataserializer, NBTTagCompound nbttagcompound) {
            packetdataserializer.func_150786_a(nbttagcompound);
        }

        public NBTTagCompound b(PacketBuffer packetdataserializer) {
            return packetdataserializer.func_150793_b();
        }

        public DataParameter<NBTTagCompound> func_187161_a(int i) {
            return new DataParameter(i, this);
        }

        public NBTTagCompound a(NBTTagCompound nbttagcompound) {
            return nbttagcompound.func_74737_b();
        }

        public Object func_187159_a(PacketBuffer packetdataserializer) {
            return this.b(packetdataserializer);
        }
    };

    public static void func_187189_a(DataSerializer<?> datawatcherserializer) {
        DataSerializers.field_187204_n.func_186808_c(datawatcherserializer);
    }

    @Nullable
    public static DataSerializer<?> func_187190_a(int i) {
        return (DataSerializer) DataSerializers.field_187204_n.func_186813_a(i);
    }

    public static int func_187188_b(DataSerializer<?> datawatcherserializer) {
        return DataSerializers.field_187204_n.func_186815_a(datawatcherserializer);
    }

    static {
        func_187189_a(DataSerializers.field_187191_a);
        func_187189_a(DataSerializers.field_187192_b);
        func_187189_a(DataSerializers.field_187193_c);
        func_187189_a(DataSerializers.field_187194_d);
        func_187189_a(DataSerializers.field_187195_e);
        func_187189_a(DataSerializers.field_187196_f);
        func_187189_a(DataSerializers.field_187198_h);
        func_187189_a(DataSerializers.field_187199_i);
        func_187189_a(DataSerializers.field_187200_j);
        func_187189_a(DataSerializers.field_187201_k);
        func_187189_a(DataSerializers.field_187202_l);
        func_187189_a(DataSerializers.field_187203_m);
        func_187189_a(DataSerializers.field_187197_g);
        func_187189_a(DataSerializers.field_192734_n);
    }
}
