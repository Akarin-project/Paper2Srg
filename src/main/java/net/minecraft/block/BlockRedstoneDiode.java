package net.minecraft.block;

import java.util.Random;


import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.bukkit.craftbukkit.event.CraftEventFactory;

public abstract class BlockRedstoneDiode extends BlockHorizontal {

    protected static final AxisAlignedBB field_185548_c = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D);
    protected final boolean field_149914_a;

    protected BlockRedstoneDiode(boolean flag) {
        super(Material.field_151594_q);
        this.field_149914_a = flag;
    }

    public AxisAlignedBB func_185496_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockRedstoneDiode.field_185548_c;
    }

    public boolean func_149686_d(IBlockState iblockdata) {
        return false;
    }

    public boolean func_176196_c(World world, BlockPos blockposition) {
        return world.func_180495_p(blockposition.func_177977_b()).func_185896_q() ? super.func_176196_c(world, blockposition) : false;
    }

    public boolean func_176409_d(World world, BlockPos blockposition) {
        return world.func_180495_p(blockposition.func_177977_b()).func_185896_q();
    }

    public void func_180645_a(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {}

    public void func_180650_b(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        if (!this.func_176405_b((IBlockAccess) world, blockposition, iblockdata)) {
            boolean flag = this.func_176404_e(world, blockposition, iblockdata);

            if (this.field_149914_a && !flag) {
                // CraftBukkit start
                if (CraftEventFactory.callRedstoneChange(world, blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p(), 15, 0).getNewCurrent() != 0) {
                    return;
                }
                // CraftBukkit end
                world.func_180501_a(blockposition, this.func_180675_k(iblockdata), 2);
            } else if (!this.field_149914_a) {
                // CraftBukkit start
                if (CraftEventFactory.callRedstoneChange(world, blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p(), 0, 15).getNewCurrent() != 15) {
                    return;
                }
                // CraftBukkit end
                world.func_180501_a(blockposition, this.func_180674_e(iblockdata), 2);
                if (!flag) {
                    world.func_175654_a(blockposition, this.func_180674_e(iblockdata).func_177230_c(), this.func_176399_m(iblockdata), -1);
                }
            }

        }
    }

    protected boolean func_176406_l(IBlockState iblockdata) {
        return this.field_149914_a;
    }

    public int func_176211_b(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition, EnumFacing enumdirection) {
        return iblockdata.func_185911_a(iblockaccess, blockposition, enumdirection);
    }

    public int func_180656_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition, EnumFacing enumdirection) {
        return !this.func_176406_l(iblockdata) ? 0 : (iblockdata.func_177229_b(BlockRedstoneDiode.field_185512_D) == enumdirection ? this.func_176408_a(iblockaccess, blockposition, iblockdata) : 0);
    }

    public void func_189540_a(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        if (this.func_176409_d(world, blockposition)) {
            this.func_176398_g(world, blockposition, iblockdata);
        } else {
            this.func_176226_b(world, blockposition, iblockdata, 0);
            world.func_175698_g(blockposition);
            EnumFacing[] aenumdirection = EnumFacing.values();
            int i = aenumdirection.length;

            for (int j = 0; j < i; ++j) {
                EnumFacing enumdirection = aenumdirection[j];

                world.func_175685_c(blockposition.func_177972_a(enumdirection), this, false);
            }

        }
    }

    protected void func_176398_g(World world, BlockPos blockposition, IBlockState iblockdata) {
        if (!this.func_176405_b((IBlockAccess) world, blockposition, iblockdata)) {
            boolean flag = this.func_176404_e(world, blockposition, iblockdata);

            if (this.field_149914_a != flag && !world.func_175691_a(blockposition, (Block) this)) {
                byte b0 = -1;

                if (this.func_176402_i(world, blockposition, iblockdata)) {
                    b0 = -3;
                } else if (this.field_149914_a) {
                    b0 = -2;
                }

                world.func_175654_a(blockposition, this, this.func_176403_d(iblockdata), b0);
            }

        }
    }

    public boolean func_176405_b(IBlockAccess iblockaccess, BlockPos blockposition, IBlockState iblockdata) {
        return false;
    }

    protected boolean func_176404_e(World world, BlockPos blockposition, IBlockState iblockdata) {
        return this.func_176397_f(world, blockposition, iblockdata) > 0;
    }

    protected int func_176397_f(World world, BlockPos blockposition, IBlockState iblockdata) {
        EnumFacing enumdirection = (EnumFacing) iblockdata.func_177229_b(BlockRedstoneDiode.field_185512_D);
        BlockPos blockposition1 = blockposition.func_177972_a(enumdirection);
        int i = world.func_175651_c(blockposition1, enumdirection);

        if (i >= 15) {
            return i;
        } else {
            IBlockState iblockdata1 = world.func_180495_p(blockposition1);

            return Math.max(i, iblockdata1.func_177230_c() == Blocks.field_150488_af ? ((Integer) iblockdata1.func_177229_b(BlockRedstoneWire.field_176351_O)).intValue() : 0);
        }
    }

    protected int func_176407_c(IBlockAccess iblockaccess, BlockPos blockposition, IBlockState iblockdata) {
        EnumFacing enumdirection = (EnumFacing) iblockdata.func_177229_b(BlockRedstoneDiode.field_185512_D);
        EnumFacing enumdirection1 = enumdirection.func_176746_e();
        EnumFacing enumdirection2 = enumdirection.func_176735_f();

        return Math.max(this.func_176401_c(iblockaccess, blockposition.func_177972_a(enumdirection1), enumdirection1), this.func_176401_c(iblockaccess, blockposition.func_177972_a(enumdirection2), enumdirection2));
    }

    protected int func_176401_c(IBlockAccess iblockaccess, BlockPos blockposition, EnumFacing enumdirection) {
        IBlockState iblockdata = iblockaccess.func_180495_p(blockposition);
        Block block = iblockdata.func_177230_c();

        return this.func_185545_A(iblockdata) ? (block == Blocks.field_150451_bX ? 15 : (block == Blocks.field_150488_af ? ((Integer) iblockdata.func_177229_b(BlockRedstoneWire.field_176351_O)).intValue() : iblockaccess.func_175627_a(blockposition, enumdirection))) : 0;
    }

    public boolean func_149744_f(IBlockState iblockdata) {
        return true;
    }

    public IBlockState func_180642_a(World world, BlockPos blockposition, EnumFacing enumdirection, float f, float f1, float f2, int i, EntityLivingBase entityliving) {
        return this.func_176223_P().func_177226_a(BlockRedstoneDiode.field_185512_D, entityliving.func_174811_aO().func_176734_d());
    }

    public void func_180633_a(World world, BlockPos blockposition, IBlockState iblockdata, EntityLivingBase entityliving, ItemStack itemstack) {
        if (this.func_176404_e(world, blockposition, iblockdata)) {
            world.func_175684_a(blockposition, (Block) this, 1);
        }

    }

    public void func_176213_c(World world, BlockPos blockposition, IBlockState iblockdata) {
        this.func_176400_h(world, blockposition, iblockdata);
    }

    protected void func_176400_h(World world, BlockPos blockposition, IBlockState iblockdata) {
        EnumFacing enumdirection = (EnumFacing) iblockdata.func_177229_b(BlockRedstoneDiode.field_185512_D);
        BlockPos blockposition1 = blockposition.func_177972_a(enumdirection.func_176734_d());

        world.func_190524_a(blockposition1, (Block) this, blockposition);
        world.func_175695_a(blockposition1, (Block) this, enumdirection);
    }

    public void func_176206_d(World world, BlockPos blockposition, IBlockState iblockdata) {
        if (this.field_149914_a) {
            EnumFacing[] aenumdirection = EnumFacing.values();
            int i = aenumdirection.length;

            for (int j = 0; j < i; ++j) {
                EnumFacing enumdirection = aenumdirection[j];

                world.func_175685_c(blockposition.func_177972_a(enumdirection), this, false);
            }
        }

        super.func_176206_d(world, blockposition, iblockdata);
    }

    public boolean func_149662_c(IBlockState iblockdata) {
        return false;
    }

    protected boolean func_185545_A(IBlockState iblockdata) {
        return iblockdata.func_185897_m();
    }

    protected int func_176408_a(IBlockAccess iblockaccess, BlockPos blockposition, IBlockState iblockdata) {
        return 15;
    }

    public static boolean func_185546_B(IBlockState iblockdata) {
        return Blocks.field_150413_aR.func_185547_C(iblockdata) || Blocks.field_150441_bU.func_185547_C(iblockdata);
    }

    public boolean func_185547_C(IBlockState iblockdata) {
        Block block = iblockdata.func_177230_c();

        return block == this.func_180674_e(this.func_176223_P()).func_177230_c() || block == this.func_180675_k(this.func_176223_P()).func_177230_c();
    }

    public boolean func_176402_i(World world, BlockPos blockposition, IBlockState iblockdata) {
        EnumFacing enumdirection = ((EnumFacing) iblockdata.func_177229_b(BlockRedstoneDiode.field_185512_D)).func_176734_d();
        BlockPos blockposition1 = blockposition.func_177972_a(enumdirection);

        return func_185546_B(world.func_180495_p(blockposition1)) ? world.func_180495_p(blockposition1).func_177229_b(BlockRedstoneDiode.field_185512_D) != enumdirection : false;
    }

    protected int func_176399_m(IBlockState iblockdata) {
        return this.func_176403_d(iblockdata);
    }

    protected abstract int func_176403_d(IBlockState iblockdata);

    protected abstract IBlockState func_180674_e(IBlockState iblockdata);

    protected abstract IBlockState func_180675_k(IBlockState iblockdata);

    public boolean func_149667_c(Block block) {
        return this.func_185547_C(block.func_176223_P());
    }

    public BlockFaceShape func_193383_a(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return enumdirection == EnumFacing.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    }
}
