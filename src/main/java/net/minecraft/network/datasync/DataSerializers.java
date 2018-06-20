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
    public static final DataSerializer<Byte> field_187191_a = new DataSerializer<Byte>() {
        @Override
        public void func_187160_a(PacketBuffer packetdataserializer, Byte obyte) {
            packetdataserializer.writeByte(obyte.byteValue());
        }

        @Override
        public Byte func_187159_a(PacketBuffer packetdataserializer) {
            return Byte.valueOf(packetdataserializer.readByte());
        }

        @Override
        public DataParameter<Byte> func_187161_a(int i) {
            return new DataParameter(i, this);
        }

        @Override
        public Byte func_192717_a(Byte obyte) {
            return obyte;
        }
    };
    public static final DataSerializer<Integer> field_187192_b = new DataSerializer<Integer>() {
        @Override
        public void func_187160_a(PacketBuffer packetdataserializer, Integer integer) {
            packetdataserializer.func_150787_b(integer.intValue());
        }

        @Override
        public Integer func_187159_a(PacketBuffer packetdataserializer) {
            return Integer.valueOf(packetdataserializer.func_150792_a());
        }

        @Override
        public DataParameter<Integer> func_187161_a(int i) {
            return new DataParameter(i, this);
        }

        @Override
        public Integer func_192717_a(Integer integer) {
            return integer;
        }
    };
    public static final DataSerializer<Float> field_187193_c = new DataSerializer<Float>() {
        @Override
        public void func_187160_a(PacketBuffer packetdataserializer, Float ofloat) {
            packetdataserializer.writeFloat(ofloat.floatValue());
        }

        @Override
        public Float func_187159_a(PacketBuffer packetdataserializer) {
            return Float.valueOf(packetdataserializer.readFloat());
        }

        @Override
        public DataParameter<Float> func_187161_a(int i) {
            return new DataParameter(i, this);
        }

        @Override
        public Float func_192717_a(Float ofloat) {
            return ofloat;
        }
    };
    public static final DataSerializer<String> field_187194_d = new DataSerializer<String>() {
        @Override
        public void func_187160_a(PacketBuffer packetdataserializer, String s) {
            packetdataserializer.func_180714_a(s);
        }

        @Override
        public String func_187159_a(PacketBuffer packetdataserializer) {
            return packetdataserializer.func_150789_c(32767);
        }

        @Override
        public DataParameter<String> func_187161_a(int i) {
            return new DataParameter(i, this);
        }

        @Override
        public String func_192717_a(String s) {
            return s;
        }
    };
    public static final DataSerializer<ITextComponent> field_187195_e = new DataSerializer<ITextComponent>() {
        @Override
        public void func_187160_a(PacketBuffer packetdataserializer, ITextComponent ichatbasecomponent) {
            packetdataserializer.func_179256_a(ichatbasecomponent);
        }

        @Override
        public ITextComponent func_187159_a(PacketBuffer packetdataserializer) {
            return packetdataserializer.func_179258_d();
        }

        @Override
        public DataParameter<ITextComponent> func_187161_a(int i) {
            return new DataParameter(i, this);
        }

        @Override
        public ITextComponent func_192717_a(ITextComponent ichatbasecomponent) {
            return ichatbasecomponent.func_150259_f();
        }
    };
    public static final DataSerializer<ItemStack> field_187196_f = new DataSerializer<ItemStack>() {
        @Override
        public void func_187160_a(PacketBuffer packetdataserializer, ItemStack itemstack) {
            packetdataserializer.func_150788_a(itemstack);
        }

        @Override
        public ItemStack func_187159_a(PacketBuffer packetdataserializer) {
            return packetdataserializer.func_150791_c();
        }

        @Override
        public DataParameter<ItemStack> func_187161_a(int i) {
            return new DataParameter(i, this);
        }

        @Override
        public ItemStack func_192717_a(ItemStack itemstack) {
            return itemstack.func_77946_l();
        }
    };
    public static final DataSerializer<Optional<IBlockState>> field_187197_g = new DataSerializer<Optional<IBlockState>>() {
        @Override
        public void func_187160_a(PacketBuffer packetdataserializer, Optional<IBlockState> optional) {
            if (optional.isPresent()) {
                packetdataserializer.func_150787_b(Block.func_176210_f(optional.get()));
            } else {
                packetdataserializer.func_150787_b(0);
            }

        }

        @Override
        public Optional<IBlockState> func_187159_a(PacketBuffer packetdataserializer) {
            int i = packetdataserializer.func_150792_a();

            return i == 0 ? Optional.absent() : Optional.of(Block.func_176220_d(i));
        }

        @Override
        public DataParameter<Optional<IBlockState>> func_187161_a(int i) {
            return new DataParameter(i, this);
        }

        @Override
        public Optional<IBlockState> func_192717_a(Optional<IBlockState> optional) {
            return optional;
        }
    };
    public static final DataSerializer<Boolean> field_187198_h = new DataSerializer<Boolean>() {
        @Override
        public void func_187160_a(PacketBuffer packetdataserializer, Boolean obool) {
            packetdataserializer.writeBoolean(obool.booleanValue());
        }

        @Override
        public Boolean func_187159_a(PacketBuffer packetdataserializer) {
            return Boolean.valueOf(packetdataserializer.readBoolean());
        }

        @Override
        public DataParameter<Boolean> func_187161_a(int i) {
            return new DataParameter(i, this);
        }

        @Override
        public Boolean func_192717_a(Boolean obool) {
            return obool;
        }
    };
    public static final DataSerializer<Rotations> field_187199_i = new DataSerializer<Rotations>() {
        @Override
        public void func_187160_a(PacketBuffer packetdataserializer, Rotations vector3f) {
            packetdataserializer.writeFloat(vector3f.func_179415_b());
            packetdataserializer.writeFloat(vector3f.func_179416_c());
            packetdataserializer.writeFloat(vector3f.func_179413_d());
        }

        @Override
        public Rotations func_187159_a(PacketBuffer packetdataserializer) {
            return new Rotations(packetdataserializer.readFloat(), packetdataserializer.readFloat(), packetdataserializer.readFloat());
        }

        @Override
        public DataParameter<Rotations> func_187161_a(int i) {
            return new DataParameter(i, this);
        }

        @Override
        public Rotations func_192717_a(Rotations vector3f) {
            return vector3f;
        }
    };
    public static final DataSerializer<BlockPos> field_187200_j = new DataSerializer<BlockPos>() {
        @Override
        public void func_187160_a(PacketBuffer packetdataserializer, BlockPos blockposition) {
            packetdataserializer.func_179255_a(blockposition);
        }

        @Override
        public BlockPos func_187159_a(PacketBuffer packetdataserializer) {
            return packetdataserializer.func_179259_c();
        }

        @Override
        public DataParameter<BlockPos> func_187161_a(int i) {
            return new DataParameter(i, this);
        }

        @Override
        public BlockPos func_192717_a(BlockPos blockposition) {
            return blockposition;
        }
    };
    public static final DataSerializer<Optional<BlockPos>> field_187201_k = new DataSerializer<Optional<BlockPos>>() {
        @Override
        public void func_187160_a(PacketBuffer packetdataserializer, Optional<BlockPos> optional) {
            packetdataserializer.writeBoolean(optional.isPresent());
            if (optional.isPresent()) {
                packetdataserializer.func_179255_a(optional.get());
            }

        }

        @Override
        public Optional<BlockPos> func_187159_a(PacketBuffer packetdataserializer) {
            return !packetdataserializer.readBoolean() ? Optional.absent() : Optional.of(packetdataserializer.func_179259_c());
        }

        @Override
        public DataParameter<Optional<BlockPos>> func_187161_a(int i) {
            return new DataParameter(i, this);
        }

        @Override
        public Optional<BlockPos> func_192717_a(Optional<BlockPos> optional) {
            return optional;
        }
    };
    public static final DataSerializer<EnumFacing> field_187202_l = new DataSerializer<EnumFacing>() {
        @Override
        public void func_187160_a(PacketBuffer packetdataserializer, EnumFacing enumdirection) {
            packetdataserializer.func_179249_a(enumdirection);
        }

        @Override
        public EnumFacing func_187159_a(PacketBuffer packetdataserializer) {
            return packetdataserializer.func_179257_a(EnumFacing.class);
        }

        @Override
        public DataParameter<EnumFacing> func_187161_a(int i) {
            return new DataParameter(i, this);
        }

        @Override
        public EnumFacing func_192717_a(EnumFacing enumdirection) {
            return enumdirection;
        }
    };
    public static final DataSerializer<Optional<UUID>> field_187203_m = new DataSerializer<Optional<UUID>>() {
        @Override
        public void func_187160_a(PacketBuffer packetdataserializer, Optional<UUID> optional) {
            packetdataserializer.writeBoolean(optional.isPresent());
            if (optional.isPresent()) {
                packetdataserializer.func_179252_a(optional.get());
            }

        }

        @Override
        public Optional<UUID> func_187159_a(PacketBuffer packetdataserializer) {
            return !packetdataserializer.readBoolean() ? Optional.absent() : Optional.of(packetdataserializer.func_179253_g());
        }

        @Override
        public DataParameter<Optional<UUID>> func_187161_a(int i) {
            return new DataParameter(i, this);
        }

        @Override
        public Optional<UUID> func_192717_a(Optional<UUID> optional) {
            return optional;
        }
    };
    public static final DataSerializer<NBTTagCompound> field_192734_n = new DataSerializer<NBTTagCompound>() {
        @Override
        public void func_187160_a(PacketBuffer packetdataserializer, NBTTagCompound nbttagcompound) {
            packetdataserializer.func_150786_a(nbttagcompound);
        }

        @Override
        public NBTTagCompound func_187159_a(PacketBuffer packetdataserializer) {
            return packetdataserializer.func_150793_b();
        }

        @Override
        public DataParameter<NBTTagCompound> func_187161_a(int i) {
            return new DataParameter(i, this);
        }

        @Override
        public NBTTagCompound func_192717_a(NBTTagCompound nbttagcompound) {
            return nbttagcompound.func_74737_b();
        }
    };

    public static void func_187189_a(DataSerializer<?> datawatcherserializer) {
        DataSerializers.field_187204_n.func_186808_c(datawatcherserializer);
    }

    @Nullable
    public static DataSerializer<?> func_187190_a(int i) {
        return DataSerializers.field_187204_n.func_186813_a(i);
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
