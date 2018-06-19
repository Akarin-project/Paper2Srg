package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockObserver extends BlockDirectional {

    public static final PropertyBool field_190963_a = PropertyBool.func_177716_a("powered");

    public BlockObserver() {
        super(Material.field_151576_e);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockObserver.field_176387_N, EnumFacing.SOUTH).func_177226_a(BlockObserver.field_190963_a, Boolean.valueOf(false)));
        this.func_149647_a(CreativeTabs.field_78028_d);
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockObserver.field_176387_N, BlockObserver.field_190963_a});
    }

    public IBlockState func_185499_a(IBlockState iblockdata, Rotation enumblockrotation) {
        return iblockdata.func_177226_a(BlockObserver.field_176387_N, enumblockrotation.func_185831_a((EnumFacing) iblockdata.func_177229_b(BlockObserver.field_176387_N)));
    }

    public IBlockState func_185471_a(IBlockState iblockdata, Mirror enumblockmirror) {
        return iblockdata.func_185907_a(enumblockmirror.func_185800_a((EnumFacing) iblockdata.func_177229_b(BlockObserver.field_176387_N)));
    }

    public void func_180650_b(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        if (((Boolean) iblockdata.func_177229_b(BlockObserver.field_190963_a)).booleanValue()) {
            world.func_180501_a(blockposition, iblockdata.func_177226_a(BlockObserver.field_190963_a, Boolean.valueOf(false)), 2);
        } else {
            world.func_180501_a(blockposition, iblockdata.func_177226_a(BlockObserver.field_190963_a, Boolean.valueOf(true)), 2);
            world.func_175684_a(blockposition, (Block) this, 2);
        }

        this.func_190961_e(world, blockposition, iblockdata);
    }

    public void func_189540_a(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {}

    public void func_190962_b(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        if (!world.field_72995_K && blockposition.func_177972_a((EnumFacing) iblockdata.func_177229_b(BlockObserver.field_176387_N)).equals(blockposition1)) {
            this.func_190960_d(iblockdata, world, blockposition);
        }

    }

    private void func_190960_d(IBlockState iblockdata, World world, BlockPos blockposition) {
        if (!((Boolean) iblockdata.func_177229_b(BlockObserver.field_190963_a)).booleanValue()) {
            if (!world.func_184145_b(blockposition, (Block) this)) {
                world.func_175684_a(blockposition, (Block) this, 2);
            }

        }
    }

    protected void func_190961_e(World world, BlockPos blockposition, IBlockState iblockdata) {
        EnumFacing enumdirection = (EnumFacing) iblockdata.func_177229_b(BlockObserver.field_176387_N);
        BlockPos blockposition1 = blockposition.func_177972_a(enumdirection.func_176734_d());

        world.func_190524_a(blockposition1, (Block) this, blockposition);
        world.func_175695_a(blockposition1, (Block) this, enumdirection);
    }

    public boolean func_149744_f(IBlockState iblockdata) {
        return true;
    }

    public int func_176211_b(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition, EnumFacing enumdirection) {
        return iblockdata.func_185911_a(iblockaccess, blockposition, enumdirection);
    }

    public int func_180656_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition, EnumFacing enumdirection) {
        return ((Boolean) iblockdata.func_177229_b(BlockObserver.field_190963_a)).booleanValue() && iblockdata.func_177229_b(BlockObserver.field_176387_N) == enumdirection ? 15 : 0;
    }

    public void func_176213_c(World world, BlockPos blockposition, IBlockState iblockdata) {
        if (!world.field_72995_K) {
            if (((Boolean) iblockdata.func_177229_b(BlockObserver.field_190963_a)).booleanValue()) {
                this.func_180650_b(world, blockposition, iblockdata, world.field_73012_v);
            }

            this.func_190960_d(iblockdata, world, blockposition);
        }

    }

    public void func_180663_b(World world, BlockPos blockposition, IBlockState iblockdata) {
        if (((Boolean) iblockdata.func_177229_b(BlockObserver.field_190963_a)).booleanValue() && world.func_184145_b(blockposition, (Block) this)) {
            this.func_190961_e(world, blockposition, iblockdata.func_177226_a(BlockObserver.field_190963_a, Boolean.valueOf(false)));
        }

    }

    public IBlockState func_180642_a(World world, BlockPos blockposition, EnumFacing enumdirection, float f, float f1, float f2, int i, EntityLivingBase entityliving) {
        return this.func_176223_P().func_177226_a(BlockObserver.field_176387_N, EnumFacing.func_190914_a(blockposition, entityliving).func_176734_d());
    }

    public int func_176201_c(IBlockState iblockdata) {
        byte b0 = 0;
        int i = b0 | ((EnumFacing) iblockdata.func_177229_b(BlockObserver.field_176387_N)).func_176745_a();

        if (((Boolean) iblockdata.func_177229_b(BlockObserver.field_190963_a)).booleanValue()) {
            i |= 8;
        }

        return i;
    }

    public IBlockState func_176203_a(int i) {
        return this.func_176223_P().func_177226_a(BlockObserver.field_176387_N, EnumFacing.func_82600_a(i & 7));
    }
}
