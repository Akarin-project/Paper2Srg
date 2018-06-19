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

    public static final PropertyInteger field_176413_a = PropertyInteger.func_177719_a("rotation", 0, 15);

    public BlockStandingSign() {
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockStandingSign.field_176413_a, Integer.valueOf(0)));
    }

    public void func_189540_a(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        if (!world.func_180495_p(blockposition.func_177977_b()).func_185904_a().func_76220_a()) {
            this.func_176226_b(world, blockposition, iblockdata, 0);
            world.func_175698_g(blockposition);
        }

        super.func_189540_a(iblockdata, world, blockposition, block, blockposition1);
    }

    public IBlockState func_176203_a(int i) {
        return this.func_176223_P().func_177226_a(BlockStandingSign.field_176413_a, Integer.valueOf(i));
    }

    public int func_176201_c(IBlockState iblockdata) {
        return ((Integer) iblockdata.func_177229_b(BlockStandingSign.field_176413_a)).intValue();
    }

    public IBlockState func_185499_a(IBlockState iblockdata, Rotation enumblockrotation) {
        return iblockdata.func_177226_a(BlockStandingSign.field_176413_a, Integer.valueOf(enumblockrotation.func_185833_a(((Integer) iblockdata.func_177229_b(BlockStandingSign.field_176413_a)).intValue(), 16)));
    }

    public IBlockState func_185471_a(IBlockState iblockdata, Mirror enumblockmirror) {
        return iblockdata.func_177226_a(BlockStandingSign.field_176413_a, Integer.valueOf(enumblockmirror.func_185802_a(((Integer) iblockdata.func_177229_b(BlockStandingSign.field_176413_a)).intValue(), 16)));
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockStandingSign.field_176413_a});
    }
}
