package net.minecraft.block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class BlockRotatedPillar extends Block {

    public static final PropertyEnum<EnumFacing.Axis> AXIS = PropertyEnum.create("axis", EnumFacing.Axis.class);

    protected BlockRotatedPillar(Material material) {
        super(material, material.getMaterialMapColor());
    }

    protected BlockRotatedPillar(Material material, MapColor materialmapcolor) {
        super(material, materialmapcolor);
    }

    public IBlockState withRotation(IBlockState iblockdata, Rotation enumblockrotation) {
        switch (enumblockrotation) {
        case COUNTERCLOCKWISE_90:
        case CLOCKWISE_90:
            switch ((EnumFacing.Axis) iblockdata.getValue(BlockRotatedPillar.AXIS)) {
            case X:
                return iblockdata.withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.Z);

            case Z:
                return iblockdata.withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.X);

            default:
                return iblockdata;
            }

        default:
            return iblockdata;
        }
    }

    public IBlockState getStateFromMeta(int i) {
        EnumFacing.Axis enumdirection_enumaxis = EnumFacing.Axis.Y;
        int j = i & 12;

        if (j == 4) {
            enumdirection_enumaxis = EnumFacing.Axis.X;
        } else if (j == 8) {
            enumdirection_enumaxis = EnumFacing.Axis.Z;
        }

        return this.getDefaultState().withProperty(BlockRotatedPillar.AXIS, enumdirection_enumaxis);
    }

    public int getMetaFromState(IBlockState iblockdata) {
        int i = 0;
        EnumFacing.Axis enumdirection_enumaxis = (EnumFacing.Axis) iblockdata.getValue(BlockRotatedPillar.AXIS);

        if (enumdirection_enumaxis == EnumFacing.Axis.X) {
            i |= 4;
        } else if (enumdirection_enumaxis == EnumFacing.Axis.Z) {
            i |= 8;
        }

        return i;
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockRotatedPillar.AXIS});
    }

    protected ItemStack getSilkTouchDrop(IBlockState iblockdata) {
        return new ItemStack(Item.getItemFromBlock(this));
    }

    public IBlockState getStateForPlacement(World world, BlockPos blockposition, EnumFacing enumdirection, float f, float f1, float f2, int i, EntityLivingBase entityliving) {
        return super.getStateForPlacement(world, blockposition, enumdirection, f, f1, f2, i, entityliving).withProperty(BlockRotatedPillar.AXIS, enumdirection.getAxis());
    }
}
