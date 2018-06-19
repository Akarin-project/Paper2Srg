package net.minecraft.block;

import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockAir extends Block {

    protected BlockAir() {
        super(Material.field_151579_a);
    }

    public EnumBlockRenderType func_149645_b(IBlockState iblockdata) {
        return EnumBlockRenderType.INVISIBLE;
    }

    @Nullable
    public AxisAlignedBB func_180646_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockAir.field_185506_k;
    }

    public boolean func_149662_c(IBlockState iblockdata) {
        return false;
    }

    public boolean func_176209_a(IBlockState iblockdata, boolean flag) {
        return false;
    }

    public void func_180653_a(World world, BlockPos blockposition, IBlockState iblockdata, float f, int i) {}

    public boolean func_176200_f(IBlockAccess iblockaccess, BlockPos blockposition) {
        return true;
    }

    public boolean func_149686_d(IBlockState iblockdata) {
        return false;
    }

    public BlockFaceShape func_193383_a(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return BlockFaceShape.UNDEFINED;
    }
}
