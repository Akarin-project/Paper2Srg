package net.minecraft.block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;


public class BlockCompressedPowered extends Block {

    public BlockCompressedPowered(Material material, MapColor materialmapcolor) {
        super(material, materialmapcolor);
    }

    public boolean canProvidePower(IBlockState iblockdata) {
        return true;
    }

    public int getWeakPower(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition, EnumFacing enumdirection) {
        return 15;
    }
}
