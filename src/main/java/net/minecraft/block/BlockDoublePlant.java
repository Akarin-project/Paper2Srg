package net.minecraft.block;

import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDoublePlant extends BlockBush implements IGrowable {

    public static final PropertyEnum<BlockDoublePlant.EnumPlantType> field_176493_a = PropertyEnum.func_177709_a("variant", BlockDoublePlant.EnumPlantType.class);
    public static final PropertyEnum<BlockDoublePlant.EnumBlockHalf> field_176492_b = PropertyEnum.func_177709_a("half", BlockDoublePlant.EnumBlockHalf.class);
    public static final PropertyEnum<EnumFacing> field_181084_N = BlockHorizontal.field_185512_D;

    public BlockDoublePlant() {
        super(Material.field_151582_l);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockDoublePlant.field_176493_a, BlockDoublePlant.EnumPlantType.SUNFLOWER).func_177226_a(BlockDoublePlant.field_176492_b, BlockDoublePlant.EnumBlockHalf.LOWER).func_177226_a(BlockDoublePlant.field_181084_N, EnumFacing.NORTH));
        this.func_149711_c(0.0F);
        this.func_149672_a(SoundType.field_185850_c);
        this.func_149663_c("doublePlant");
    }

    public AxisAlignedBB func_185496_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockDoublePlant.field_185505_j;
    }

    private BlockDoublePlant.EnumPlantType func_185517_a(IBlockAccess iblockaccess, BlockPos blockposition, IBlockState iblockdata) {
        if (iblockdata.func_177230_c() == this) {
            iblockdata = iblockdata.func_185899_b(iblockaccess, blockposition);
            return (BlockDoublePlant.EnumPlantType) iblockdata.func_177229_b(BlockDoublePlant.field_176493_a);
        } else {
            return BlockDoublePlant.EnumPlantType.FERN;
        }
    }

    public boolean func_176196_c(World world, BlockPos blockposition) {
        return super.func_176196_c(world, blockposition) && world.func_175623_d(blockposition.func_177984_a());
    }

    public boolean func_176200_f(IBlockAccess iblockaccess, BlockPos blockposition) {
        IBlockState iblockdata = iblockaccess.func_180495_p(blockposition);

        if (iblockdata.func_177230_c() != this) {
            return true;
        } else {
            BlockDoublePlant.EnumPlantType blocktallplant_enumtallflowervariants = (BlockDoublePlant.EnumPlantType) iblockdata.func_185899_b(iblockaccess, blockposition).func_177229_b(BlockDoublePlant.field_176493_a);

            return blocktallplant_enumtallflowervariants == BlockDoublePlant.EnumPlantType.FERN || blocktallplant_enumtallflowervariants == BlockDoublePlant.EnumPlantType.GRASS;
        }
    }

    protected void func_176475_e(World world, BlockPos blockposition, IBlockState iblockdata) {
        if (!this.func_180671_f(world, blockposition, iblockdata)) {
            // CraftBukkit start
            if (org.bukkit.craftbukkit.event.CraftEventFactory.callBlockPhysicsEvent(world, blockposition).isCancelled()) {
                return;
            }
            // CraftBukkit end
            boolean flag = iblockdata.func_177229_b(BlockDoublePlant.field_176492_b) == BlockDoublePlant.EnumBlockHalf.UPPER;
            BlockPos blockposition1 = flag ? blockposition : blockposition.func_177984_a();
            BlockPos blockposition2 = flag ? blockposition.func_177977_b() : blockposition;
            Object object = flag ? this : world.func_180495_p(blockposition1).func_177230_c();
            Object object1 = flag ? world.func_180495_p(blockposition2).func_177230_c() : this;

            if (object == this) {
                world.func_180501_a(blockposition1, Blocks.field_150350_a.func_176223_P(), 2);
            }

            if (object1 == this) {
                world.func_180501_a(blockposition2, Blocks.field_150350_a.func_176223_P(), 3);
                if (!flag) {
                    this.func_176226_b(world, blockposition2, iblockdata, 0);
                }
            }

        }
    }

    public boolean func_180671_f(World world, BlockPos blockposition, IBlockState iblockdata) {
        if (iblockdata.func_177229_b(BlockDoublePlant.field_176492_b) == BlockDoublePlant.EnumBlockHalf.UPPER) {
            return world.func_180495_p(blockposition.func_177977_b()).func_177230_c() == this;
        } else {
            IBlockState iblockdata1 = world.func_180495_p(blockposition.func_177984_a());

            return iblockdata1.func_177230_c() == this && super.func_180671_f(world, blockposition, iblockdata1);
        }
    }

    public Item func_180660_a(IBlockState iblockdata, Random random, int i) {
        if (iblockdata.func_177229_b(BlockDoublePlant.field_176492_b) == BlockDoublePlant.EnumBlockHalf.UPPER) {
            return Items.field_190931_a;
        } else {
            BlockDoublePlant.EnumPlantType blocktallplant_enumtallflowervariants = (BlockDoublePlant.EnumPlantType) iblockdata.func_177229_b(BlockDoublePlant.field_176493_a);

            return blocktallplant_enumtallflowervariants == BlockDoublePlant.EnumPlantType.FERN ? Items.field_190931_a : (blocktallplant_enumtallflowervariants == BlockDoublePlant.EnumPlantType.GRASS ? (random.nextInt(8) == 0 ? Items.field_151014_N : Items.field_190931_a) : super.func_180660_a(iblockdata, random, i));
        }
    }

    public int func_180651_a(IBlockState iblockdata) {
        return iblockdata.func_177229_b(BlockDoublePlant.field_176492_b) != BlockDoublePlant.EnumBlockHalf.UPPER && iblockdata.func_177229_b(BlockDoublePlant.field_176493_a) != BlockDoublePlant.EnumPlantType.GRASS ? ((BlockDoublePlant.EnumPlantType) iblockdata.func_177229_b(BlockDoublePlant.field_176493_a)).func_176936_a() : 0;
    }

    public void func_176491_a(World world, BlockPos blockposition, BlockDoublePlant.EnumPlantType blocktallplant_enumtallflowervariants, int i) {
        world.func_180501_a(blockposition, this.func_176223_P().func_177226_a(BlockDoublePlant.field_176492_b, BlockDoublePlant.EnumBlockHalf.LOWER).func_177226_a(BlockDoublePlant.field_176493_a, blocktallplant_enumtallflowervariants), i);
        world.func_180501_a(blockposition.func_177984_a(), this.func_176223_P().func_177226_a(BlockDoublePlant.field_176492_b, BlockDoublePlant.EnumBlockHalf.UPPER), i);
    }

    public void func_180633_a(World world, BlockPos blockposition, IBlockState iblockdata, EntityLivingBase entityliving, ItemStack itemstack) {
        world.func_180501_a(blockposition.func_177984_a(), this.func_176223_P().func_177226_a(BlockDoublePlant.field_176492_b, BlockDoublePlant.EnumBlockHalf.UPPER), 2);
    }

    public void func_180657_a(World world, EntityPlayer entityhuman, BlockPos blockposition, IBlockState iblockdata, @Nullable TileEntity tileentity, ItemStack itemstack) {
        if (world.field_72995_K || itemstack.func_77973_b() != Items.field_151097_aZ || iblockdata.func_177229_b(BlockDoublePlant.field_176492_b) != BlockDoublePlant.EnumBlockHalf.LOWER || !this.func_176489_b(world, blockposition, iblockdata, entityhuman)) {
            super.func_180657_a(world, entityhuman, blockposition, iblockdata, tileentity, itemstack);
        }
    }

    public void func_176208_a(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman) {
        if (iblockdata.func_177229_b(BlockDoublePlant.field_176492_b) == BlockDoublePlant.EnumBlockHalf.UPPER) {
            if (world.func_180495_p(blockposition.func_177977_b()).func_177230_c() == this) {
                if (entityhuman.field_71075_bZ.field_75098_d) {
                    world.func_175698_g(blockposition.func_177977_b());
                } else {
                    IBlockState iblockdata1 = world.func_180495_p(blockposition.func_177977_b());
                    BlockDoublePlant.EnumPlantType blocktallplant_enumtallflowervariants = (BlockDoublePlant.EnumPlantType) iblockdata1.func_177229_b(BlockDoublePlant.field_176493_a);

                    if (blocktallplant_enumtallflowervariants != BlockDoublePlant.EnumPlantType.FERN && blocktallplant_enumtallflowervariants != BlockDoublePlant.EnumPlantType.GRASS) {
                        world.func_175655_b(blockposition.func_177977_b(), true);
                    } else if (world.field_72995_K) {
                        world.func_175698_g(blockposition.func_177977_b());
                    } else if (!entityhuman.func_184614_ca().func_190926_b() && entityhuman.func_184614_ca().func_77973_b() == Items.field_151097_aZ) {
                        this.func_176489_b(world, blockposition, iblockdata1, entityhuman);
                        world.func_175698_g(blockposition.func_177977_b());
                    } else {
                        world.func_175655_b(blockposition.func_177977_b(), true);
                    }
                }
            }
        } else if (world.func_180495_p(blockposition.func_177984_a()).func_177230_c() == this) {
            world.func_180501_a(blockposition.func_177984_a(), Blocks.field_150350_a.func_176223_P(), 2);
        }

        super.func_176208_a(world, blockposition, iblockdata, entityhuman);
    }

    private boolean func_176489_b(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman) {
        BlockDoublePlant.EnumPlantType blocktallplant_enumtallflowervariants = (BlockDoublePlant.EnumPlantType) iblockdata.func_177229_b(BlockDoublePlant.field_176493_a);

        if (blocktallplant_enumtallflowervariants != BlockDoublePlant.EnumPlantType.FERN && blocktallplant_enumtallflowervariants != BlockDoublePlant.EnumPlantType.GRASS) {
            return false;
        } else {
            entityhuman.func_71029_a(StatList.func_188055_a((Block) this));
            int i = (blocktallplant_enumtallflowervariants == BlockDoublePlant.EnumPlantType.GRASS ? BlockTallGrass.EnumType.GRASS : BlockTallGrass.EnumType.FERN).func_177044_a();

            func_180635_a(world, blockposition, new ItemStack(Blocks.field_150329_H, 2, i));
            return true;
        }
    }

    public void func_149666_a(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        BlockDoublePlant.EnumPlantType[] ablocktallplant_enumtallflowervariants = BlockDoublePlant.EnumPlantType.values();
        int i = ablocktallplant_enumtallflowervariants.length;

        for (int j = 0; j < i; ++j) {
            BlockDoublePlant.EnumPlantType blocktallplant_enumtallflowervariants = ablocktallplant_enumtallflowervariants[j];

            nonnulllist.add(new ItemStack(this, 1, blocktallplant_enumtallflowervariants.func_176936_a()));
        }

    }

    public ItemStack func_185473_a(World world, BlockPos blockposition, IBlockState iblockdata) {
        return new ItemStack(this, 1, this.func_185517_a((IBlockAccess) world, blockposition, iblockdata).func_176936_a());
    }

    public boolean func_176473_a(World world, BlockPos blockposition, IBlockState iblockdata, boolean flag) {
        BlockDoublePlant.EnumPlantType blocktallplant_enumtallflowervariants = this.func_185517_a((IBlockAccess) world, blockposition, iblockdata);

        return blocktallplant_enumtallflowervariants != BlockDoublePlant.EnumPlantType.GRASS && blocktallplant_enumtallflowervariants != BlockDoublePlant.EnumPlantType.FERN;
    }

    public boolean func_180670_a(World world, Random random, BlockPos blockposition, IBlockState iblockdata) {
        return true;
    }

    public void func_176474_b(World world, Random random, BlockPos blockposition, IBlockState iblockdata) {
        func_180635_a(world, blockposition, new ItemStack(this, 1, this.func_185517_a((IBlockAccess) world, blockposition, iblockdata).func_176936_a()));
    }

    public IBlockState func_176203_a(int i) {
        return (i & 8) > 0 ? this.func_176223_P().func_177226_a(BlockDoublePlant.field_176492_b, BlockDoublePlant.EnumBlockHalf.UPPER) : this.func_176223_P().func_177226_a(BlockDoublePlant.field_176492_b, BlockDoublePlant.EnumBlockHalf.LOWER).func_177226_a(BlockDoublePlant.field_176493_a, BlockDoublePlant.EnumPlantType.func_176938_a(i & 7));
    }

    public IBlockState func_176221_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        if (iblockdata.func_177229_b(BlockDoublePlant.field_176492_b) == BlockDoublePlant.EnumBlockHalf.UPPER) {
            IBlockState iblockdata1 = iblockaccess.func_180495_p(blockposition.func_177977_b());

            if (iblockdata1.func_177230_c() == this) {
                iblockdata = iblockdata.func_177226_a(BlockDoublePlant.field_176493_a, iblockdata1.func_177229_b(BlockDoublePlant.field_176493_a));
            }
        }

        return iblockdata;
    }

    public int func_176201_c(IBlockState iblockdata) {
        return iblockdata.func_177229_b(BlockDoublePlant.field_176492_b) == BlockDoublePlant.EnumBlockHalf.UPPER ? 8 | ((EnumFacing) iblockdata.func_177229_b(BlockDoublePlant.field_181084_N)).func_176736_b() : ((BlockDoublePlant.EnumPlantType) iblockdata.func_177229_b(BlockDoublePlant.field_176493_a)).func_176936_a();
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockDoublePlant.field_176492_b, BlockDoublePlant.field_176493_a, BlockDoublePlant.field_181084_N});
    }

    public Block.EnumOffsetType func_176218_Q() {
        return Block.EnumOffsetType.XZ;
    }

    public static enum EnumBlockHalf implements IStringSerializable {

        UPPER, LOWER;

        private EnumBlockHalf() {}

        public String toString() {
            return this.func_176610_l();
        }

        public String func_176610_l() {
            return this == BlockDoublePlant.EnumBlockHalf.UPPER ? "upper" : "lower";
        }
    }

    public static enum EnumPlantType implements IStringSerializable {

        SUNFLOWER(0, "sunflower"), SYRINGA(1, "syringa"), GRASS(2, "double_grass", "grass"), FERN(3, "double_fern", "fern"), ROSE(4, "double_rose", "rose"), PAEONIA(5, "paeonia");

        private static final BlockDoublePlant.EnumPlantType[] field_176941_g = new BlockDoublePlant.EnumPlantType[values().length];
        private final int field_176949_h;
        private final String field_176950_i;
        private final String field_176947_j;

        private EnumPlantType(int i, String s) {
            this(i, s, s);
        }

        private EnumPlantType(int i, String s, String s1) {
            this.field_176949_h = i;
            this.field_176950_i = s;
            this.field_176947_j = s1;
        }

        public int func_176936_a() {
            return this.field_176949_h;
        }

        public String toString() {
            return this.field_176950_i;
        }

        public static BlockDoublePlant.EnumPlantType func_176938_a(int i) {
            if (i < 0 || i >= BlockDoublePlant.EnumPlantType.field_176941_g.length) {
                i = 0;
            }

            return BlockDoublePlant.EnumPlantType.field_176941_g[i];
        }

        public String func_176610_l() {
            return this.field_176950_i;
        }

        public String func_176939_c() {
            return this.field_176947_j;
        }

        static {
            BlockDoublePlant.EnumPlantType[] ablocktallplant_enumtallflowervariants = values();
            int i = ablocktallplant_enumtallflowervariants.length;

            for (int j = 0; j < i; ++j) {
                BlockDoublePlant.EnumPlantType blocktallplant_enumtallflowervariants = ablocktallplant_enumtallflowervariants[j];

                BlockDoublePlant.EnumPlantType.field_176941_g[blocktallplant_enumtallflowervariants.func_176936_a()] = blocktallplant_enumtallflowervariants;
            }

        }
    }
}
