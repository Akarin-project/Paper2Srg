package net.minecraft.block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;


public class BlockPotato extends BlockCrops {

    private static final AxisAlignedBB[] field_185534_a = new AxisAlignedBB[] { new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.1875D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.25D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.3125D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.375D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.4375D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5625D, 1.0D)};

    public BlockPotato() {}

    protected Item func_149866_i() {
        return Items.field_151174_bG;
    }

    protected Item func_149865_P() {
        return Items.field_151174_bG;
    }

    public void func_180653_a(World world, BlockPos blockposition, IBlockState iblockdata, float f, int i) {
        super.func_180653_a(world, blockposition, iblockdata, f, i);
        if (!world.field_72995_K) {
            if (this.func_185525_y(iblockdata) && world.field_73012_v.nextInt(50) == 0) {
                func_180635_a(world, blockposition, new ItemStack(Items.field_151170_bI));
            }

        }
    }

    public AxisAlignedBB func_185496_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockPotato.field_185534_a[((Integer) iblockdata.func_177229_b(this.func_185524_e())).intValue()];
    }
}
