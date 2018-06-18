package net.minecraft.block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class BlockStandingSign extends BlockSign {

    public static final PropertyInteger ROTATION = PropertyInteger.create("rotation", 0, 15);

    public BlockStandingSign() {
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockStandingSign.ROTATION, Integer.valueOf(0)));
    }

    public void neighborChanged(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        if (!world.getBlockState(blockposition.down()).getMaterial().isSolid()) {
            this.dropBlockAsItem(world, blockposition, iblockdata, 0);
            world.setBlockToAir(blockposition);
        }

        super.neighborChanged(iblockdata, world, blockposition, block, blockposition1);
    }

    public IBlockState getStateFromMeta(int i) {
        return this.getDefaultState().withProperty(BlockStandingSign.ROTATION, Integer.valueOf(i));
    }

    public int getMetaFromState(IBlockState iblockdata) {
        return ((Integer) iblockdata.getValue(BlockStandingSign.ROTATION)).intValue();
    }

    public IBlockState withRotation(IBlockState iblockdata, Rotation enumblockrotation) {
        return iblockdata.withProperty(BlockStandingSign.ROTATION, Integer.valueOf(enumblockrotation.rotate(((Integer) iblockdata.getValue(BlockStandingSign.ROTATION)).intValue(), 16)));
    }

    public IBlockState withMirror(IBlockState iblockdata, Mirror enumblockmirror) {
        return iblockdata.withProperty(BlockStandingSign.ROTATION, Integer.valueOf(enumblockmirror.mirrorRotation(((Integer) iblockdata.getValue(BlockStandingSign.ROTATION)).intValue(), 16)));
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockStandingSign.ROTATION});
    }
}
