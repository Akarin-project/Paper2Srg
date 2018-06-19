package net.minecraft.block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;


public class BlockBeacon extends BlockContainer {

    public BlockBeacon() {
        super(Material.field_151592_s, MapColor.field_151648_G);
        this.func_149711_c(3.0F);
        this.func_149647_a(CreativeTabs.field_78026_f);
    }

    public TileEntity func_149915_a(World world, int i) {
        return new TileEntityBeacon();
    }

    public boolean func_180639_a(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        if (world.field_72995_K) {
            return true;
        } else {
            TileEntity tileentity = world.func_175625_s(blockposition);

            if (tileentity instanceof TileEntityBeacon) {
                entityhuman.func_71007_a((TileEntityBeacon) tileentity);
                entityhuman.func_71029_a(StatList.field_188082_P);
            }

            return true;
        }
    }

    public boolean func_149662_c(IBlockState iblockdata) {
        return false;
    }

    public boolean func_149686_d(IBlockState iblockdata) {
        return false;
    }

    public EnumBlockRenderType func_149645_b(IBlockState iblockdata) {
        return EnumBlockRenderType.MODEL;
    }

    public void func_180633_a(World world, BlockPos blockposition, IBlockState iblockdata, EntityLivingBase entityliving, ItemStack itemstack) {
        super.func_180633_a(world, blockposition, iblockdata, entityliving, itemstack);
        if (itemstack.func_82837_s()) {
            TileEntity tileentity = world.func_175625_s(blockposition);

            if (tileentity instanceof TileEntityBeacon) {
                ((TileEntityBeacon) tileentity).func_145999_a(itemstack.func_82833_r());
            }
        }

    }

    public void func_189540_a(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        TileEntity tileentity = world.func_175625_s(blockposition);

        if (tileentity instanceof TileEntityBeacon) {
            ((TileEntityBeacon) tileentity).func_174908_m();
            world.func_175641_c(blockposition, this, 1, 0);
        }

    }

    public static void func_176450_d(final World world, final BlockPos blockposition) {
        /*HttpUtilities.a.submit(new Runnable() {
            public void run() {*/ // Paper
                Chunk chunk = world.func_175726_f(blockposition);

                for (int i = blockposition.func_177956_o() - 1; i >= 0; --i) {
                    final BlockPos blockposition1 = new BlockPos(blockposition.func_177958_n(), i, blockposition.func_177952_p());

                    if (!chunk.func_177444_d(blockposition1)) {
                        break;
                    }

                    IBlockState iblockdata = world.func_180495_p(blockposition1);

                    if (iblockdata.func_177230_c() == Blocks.field_150461_bJ) {
                        /*((WorldServer) world).postToMainThread(new Runnable() {
                            public void run() {*/ // Paper
                                TileEntity tileentity = world.func_175625_s(blockposition);

                                if (tileentity instanceof TileEntityBeacon) {
                                    ((TileEntityBeacon) tileentity).func_174908_m();
                                    world.func_175641_c(blockposition, Blocks.field_150461_bJ, 1, 0);
                                }

                            /*}
                        });*/ // Paper
                    }
                }

            /*}
        });*/ // Paper
    }
}
