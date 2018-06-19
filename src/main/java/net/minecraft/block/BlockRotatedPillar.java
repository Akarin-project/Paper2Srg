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

    public static final PropertyEnum<EnumFacing.Axis> field_176298_M = PropertyEnum.func_177709_a("axis", EnumFacing.Axis.class);

    protected BlockRotatedPillar(Material material) {
        super(material, material.func_151565_r());
    }

    protected BlockRotatedPillar(Material material, MapColor materialmapcolor) {
        super(material, materialmapcolor);
    }

    public IBlockState func_185499_a(IBlockState iblockdata, Rotation enumblockrotation) {
        switch (enumblockrotation) {
        case COUNTERCLOCKWISE_90:
        case CLOCKWISE_90:
            switch ((EnumFacing.Axis) iblockdata.func_177229_b(BlockRotatedPillar.field_176298_M)) {
            case X:
                return iblockdata.func_177226_a(BlockRotatedPillar.field_176298_M, EnumFacing.Axis.Z);

            case Z:
                return iblockdata.func_177226_a(BlockRotatedPillar.field_176298_M, EnumFacing.Axis.X);

            default:
                return iblockdata;
            }

        default:
            return iblockdata;
        }
    }

    public IBlockState func_176203_a(int i) {
        EnumFacing.Axis enumdirection_enumaxis = EnumFacing.Axis.Y;
        int j = i & 12;

        if (j == 4) {
            enumdirection_enumaxis = EnumFacing.Axis.X;
        } else if (j == 8) {
            enumdirection_enumaxis = EnumFacing.Axis.Z;
        }

        return this.func_176223_P().func_177226_a(BlockRotatedPillar.field_176298_M, enumdirection_enumaxis);
    }

    public int func_176201_c(IBlockState iblockdata) {
        int i = 0;
        EnumFacing.Axis enumdirection_enumaxis = (EnumFacing.Axis) iblockdata.func_177229_b(BlockRotatedPillar.field_176298_M);

        if (enumdirection_enumaxis == EnumFacing.Axis.X) {
            i |= 4;
        } else if (enumdirection_enumaxis == EnumFacing.Axis.Z) {
            i |= 8;
        }

        return i;
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockRotatedPillar.field_176298_M});
    }

    protected ItemStack func_180643_i(IBlockState iblockdata) {
        return new ItemStack(Item.func_150898_a(this));
    }

    public IBlockState func_180642_a(World world, BlockPos blockposition, EnumFacing enumdirection, float f, float f1, float f2, int i, EntityLivingBase entityliving) {
        return super.func_180642_a(world, blockposition, enumdirection, f, f1, f2, i, entityliving).func_177226_a(BlockRotatedPillar.field_176298_M, enumdirection.func_176740_k());
    }
}
