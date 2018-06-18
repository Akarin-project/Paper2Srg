package net.minecraft.block;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class BlockGlazedTerracotta extends BlockHorizontal {

    public BlockGlazedTerracotta(EnumDyeColor enumcolor) {
        super(Material.ROCK, MapColor.getBlockColor(enumcolor));
        this.setHardness(1.4F);
        this.setSoundType(SoundType.STONE);
        String s = enumcolor.getUnlocalizedName();

        if (s.length() > 1) {
            String s1 = s.substring(0, 1).toUpperCase() + s.substring(1, s.length());

            this.setUnlocalizedName("glazedTerracotta" + s1);
        }

        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockGlazedTerracotta.FACING});
    }

    public IBlockState withRotation(IBlockState iblockdata, Rotation enumblockrotation) {
        return iblockdata.withProperty(BlockGlazedTerracotta.FACING, enumblockrotation.rotate((EnumFacing) iblockdata.getValue(BlockGlazedTerracotta.FACING)));
    }

    public IBlockState withMirror(IBlockState iblockdata, Mirror enumblockmirror) {
        return iblockdata.withRotation(enumblockmirror.toRotation((EnumFacing) iblockdata.getValue(BlockGlazedTerracotta.FACING)));
    }

    public IBlockState getStateForPlacement(World world, BlockPos blockposition, EnumFacing enumdirection, float f, float f1, float f2, int i, EntityLivingBase entityliving) {
        return this.getDefaultState().withProperty(BlockGlazedTerracotta.FACING, entityliving.getHorizontalFacing().getOpposite());
    }

    public int getMetaFromState(IBlockState iblockdata) {
        byte b0 = 0;
        int i = b0 | ((EnumFacing) iblockdata.getValue(BlockGlazedTerracotta.FACING)).getHorizontalIndex();

        return i;
    }

    public IBlockState getStateFromMeta(int i) {
        return this.getDefaultState().withProperty(BlockGlazedTerracotta.FACING, EnumFacing.getHorizontal(i));
    }

    public EnumPushReaction getMobilityFlag(IBlockState iblockdata) {
        return EnumPushReaction.PUSH_ONLY;
    }
}
