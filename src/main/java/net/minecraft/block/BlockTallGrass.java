package net.minecraft.block;

import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTallGrass extends BlockBush implements IGrowable {

    public static final PropertyEnum<BlockTallGrass.EnumType> field_176497_a = PropertyEnum.func_177709_a("type", BlockTallGrass.EnumType.class);
    protected static final AxisAlignedBB field_185522_c = new AxisAlignedBB(0.09999999403953552D, 0.0D, 0.09999999403953552D, 0.8999999761581421D, 0.800000011920929D, 0.8999999761581421D);

    protected BlockTallGrass() {
        super(Material.field_151582_l);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockTallGrass.field_176497_a, BlockTallGrass.EnumType.DEAD_BUSH));
    }

    public AxisAlignedBB func_185496_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockTallGrass.field_185522_c;
    }

    public boolean func_180671_f(World world, BlockPos blockposition, IBlockState iblockdata) {
        return this.func_185514_i(world.func_180495_p(blockposition.func_177977_b()));
    }

    public boolean func_176200_f(IBlockAccess iblockaccess, BlockPos blockposition) {
        return true;
    }

    public Item func_180660_a(IBlockState iblockdata, Random random, int i) {
        return random.nextInt(8) == 0 ? Items.field_151014_N : Items.field_190931_a;
    }

    public int func_149679_a(int i, Random random) {
        return 1 + random.nextInt(i * 2 + 1);
    }

    public void func_180657_a(World world, EntityPlayer entityhuman, BlockPos blockposition, IBlockState iblockdata, @Nullable TileEntity tileentity, ItemStack itemstack) {
        if (!world.field_72995_K && itemstack.func_77973_b() == Items.field_151097_aZ) {
            entityhuman.func_71029_a(StatList.func_188055_a((Block) this));
            func_180635_a(world, blockposition, new ItemStack(Blocks.field_150329_H, 1, ((BlockTallGrass.EnumType) iblockdata.func_177229_b(BlockTallGrass.field_176497_a)).func_177044_a()));
        } else {
            super.func_180657_a(world, entityhuman, blockposition, iblockdata, tileentity, itemstack);
        }

    }

    public ItemStack func_185473_a(World world, BlockPos blockposition, IBlockState iblockdata) {
        return new ItemStack(this, 1, iblockdata.func_177230_c().func_176201_c(iblockdata));
    }

    public void func_149666_a(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        for (int i = 1; i < 3; ++i) {
            nonnulllist.add(new ItemStack(this, 1, i));
        }

    }

    public boolean func_176473_a(World world, BlockPos blockposition, IBlockState iblockdata, boolean flag) {
        return iblockdata.func_177229_b(BlockTallGrass.field_176497_a) != BlockTallGrass.EnumType.DEAD_BUSH;
    }

    public boolean func_180670_a(World world, Random random, BlockPos blockposition, IBlockState iblockdata) {
        return true;
    }

    public void func_176474_b(World world, Random random, BlockPos blockposition, IBlockState iblockdata) {
        BlockDoublePlant.EnumPlantType blocktallplant_enumtallflowervariants = BlockDoublePlant.EnumPlantType.GRASS;

        if (iblockdata.func_177229_b(BlockTallGrass.field_176497_a) == BlockTallGrass.EnumType.FERN) {
            blocktallplant_enumtallflowervariants = BlockDoublePlant.EnumPlantType.FERN;
        }

        if (Blocks.field_150398_cm.func_176196_c(world, blockposition)) {
            Blocks.field_150398_cm.func_176491_a(world, blockposition, blocktallplant_enumtallflowervariants, 2);
        }

    }

    public IBlockState func_176203_a(int i) {
        return this.func_176223_P().func_177226_a(BlockTallGrass.field_176497_a, BlockTallGrass.EnumType.func_177045_a(i));
    }

    public int func_176201_c(IBlockState iblockdata) {
        return ((BlockTallGrass.EnumType) iblockdata.func_177229_b(BlockTallGrass.field_176497_a)).func_177044_a();
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockTallGrass.field_176497_a});
    }

    public Block.EnumOffsetType func_176218_Q() {
        return Block.EnumOffsetType.XYZ;
    }

    public static enum EnumType implements IStringSerializable {

        DEAD_BUSH(0, "dead_bush"), GRASS(1, "tall_grass"), FERN(2, "fern");

        private static final BlockTallGrass.EnumType[] field_177048_d = new BlockTallGrass.EnumType[values().length];
        private final int field_177049_e;
        private final String field_177046_f;

        private EnumType(int i, String s) {
            this.field_177049_e = i;
            this.field_177046_f = s;
        }

        public int func_177044_a() {
            return this.field_177049_e;
        }

        public String toString() {
            return this.field_177046_f;
        }

        public static BlockTallGrass.EnumType func_177045_a(int i) {
            if (i < 0 || i >= BlockTallGrass.EnumType.field_177048_d.length) {
                i = 0;
            }

            return BlockTallGrass.EnumType.field_177048_d[i];
        }

        public String func_176610_l() {
            return this.field_177046_f;
        }

        static {
            BlockTallGrass.EnumType[] ablocklonggrass_enumtallgrasstype = values();
            int i = ablocklonggrass_enumtallgrasstype.length;

            for (int j = 0; j < i; ++j) {
                BlockTallGrass.EnumType blocklonggrass_enumtallgrasstype = ablocklonggrass_enumtallgrasstype[j];

                BlockTallGrass.EnumType.field_177048_d[blocklonggrass_enumtallgrasstype.func_177044_a()] = blocklonggrass_enumtallgrasstype;
            }

        }
    }
}
