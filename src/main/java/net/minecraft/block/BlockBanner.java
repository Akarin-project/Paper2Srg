package net.minecraft.block;

import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockBanner extends BlockContainer {

    public static final PropertyDirection field_176449_a = BlockHorizontal.field_185512_D;
    public static final PropertyInteger field_176448_b = PropertyInteger.func_177719_a("rotation", 0, 15);
    protected static final AxisAlignedBB field_185550_c = new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 1.0D, 0.75D);

    protected BlockBanner() {
        super(Material.field_151575_d);
    }

    public String func_149732_F() {
        return I18n.func_74838_a("item.banner.white.name");
    }

    @Nullable
    public AxisAlignedBB func_180646_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockBanner.field_185506_k;
    }

    public boolean func_149686_d(IBlockState iblockdata) {
        return false;
    }

    public boolean func_176205_b(IBlockAccess iblockaccess, BlockPos blockposition) {
        return true;
    }

    public boolean func_149662_c(IBlockState iblockdata) {
        return false;
    }

    public boolean func_181623_g() {
        return true;
    }

    public TileEntity func_149915_a(World world, int i) {
        return new TileEntityBanner();
    }

    public Item func_180660_a(IBlockState iblockdata, Random random, int i) {
        return Items.field_179564_cE;
    }

    private ItemStack func_185549_e(World world, BlockPos blockposition) {
        TileEntity tileentity = world.func_175625_s(blockposition);

        return tileentity instanceof TileEntityBanner ? ((TileEntityBanner) tileentity).func_190615_l() : ItemStack.field_190927_a;
    }

    public ItemStack func_185473_a(World world, BlockPos blockposition, IBlockState iblockdata) {
        ItemStack itemstack = this.func_185549_e(world, blockposition);

        return itemstack.func_190926_b() ? new ItemStack(Items.field_179564_cE) : itemstack;
    }

    public void func_180653_a(World world, BlockPos blockposition, IBlockState iblockdata, float f, int i) {
        ItemStack itemstack = this.func_185549_e(world, blockposition);

        if (itemstack.func_190926_b()) {
            super.func_180653_a(world, blockposition, iblockdata, f, i);
        } else {
            func_180635_a(world, blockposition, itemstack);
        }

    }

    public boolean func_176196_c(World world, BlockPos blockposition) {
        return !this.func_181087_e(world, blockposition) && super.func_176196_c(world, blockposition);
    }

    public void func_180657_a(World world, EntityPlayer entityhuman, BlockPos blockposition, IBlockState iblockdata, @Nullable TileEntity tileentity, ItemStack itemstack) {
        if (tileentity instanceof TileEntityBanner) {
            TileEntityBanner tileentitybanner = (TileEntityBanner) tileentity;
            ItemStack itemstack1 = tileentitybanner.func_190615_l();

            func_180635_a(world, blockposition, itemstack1);
        } else {
            super.func_180657_a(world, entityhuman, blockposition, iblockdata, (TileEntity) null, itemstack);
        }

    }

    public BlockFaceShape func_193383_a(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return BlockFaceShape.UNDEFINED;
    }

    public static class BlockBannerStanding extends BlockBanner {

        public BlockBannerStanding() {
            this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockBanner.BlockBannerStanding.field_176448_b, Integer.valueOf(0)));
        }

        public AxisAlignedBB func_185496_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
            return BlockBanner.BlockBannerStanding.field_185550_c;
        }

        public IBlockState func_185499_a(IBlockState iblockdata, Rotation enumblockrotation) {
            return iblockdata.func_177226_a(BlockBanner.BlockBannerStanding.field_176448_b, Integer.valueOf(enumblockrotation.func_185833_a(((Integer) iblockdata.func_177229_b(BlockBanner.BlockBannerStanding.field_176448_b)).intValue(), 16)));
        }

        public IBlockState func_185471_a(IBlockState iblockdata, Mirror enumblockmirror) {
            return iblockdata.func_177226_a(BlockBanner.BlockBannerStanding.field_176448_b, Integer.valueOf(enumblockmirror.func_185802_a(((Integer) iblockdata.func_177229_b(BlockBanner.BlockBannerStanding.field_176448_b)).intValue(), 16)));
        }

        public void func_189540_a(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
            if (!world.func_180495_p(blockposition.func_177977_b()).func_185904_a().func_76220_a()) {
                this.func_176226_b(world, blockposition, iblockdata, 0);
                world.func_175698_g(blockposition);
            }

            super.func_189540_a(iblockdata, world, blockposition, block, blockposition1);
        }

        public IBlockState func_176203_a(int i) {
            return this.func_176223_P().func_177226_a(BlockBanner.BlockBannerStanding.field_176448_b, Integer.valueOf(i));
        }

        public int func_176201_c(IBlockState iblockdata) {
            return ((Integer) iblockdata.func_177229_b(BlockBanner.BlockBannerStanding.field_176448_b)).intValue();
        }

        protected BlockStateContainer func_180661_e() {
            return new BlockStateContainer(this, new IProperty[] { BlockBanner.BlockBannerStanding.field_176448_b});
        }
    }

    public static class BlockBannerHanging extends BlockBanner {

        protected static final AxisAlignedBB field_185551_d = new AxisAlignedBB(0.0D, 0.0D, 0.875D, 1.0D, 0.78125D, 1.0D);
        protected static final AxisAlignedBB field_185552_e = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.78125D, 0.125D);
        protected static final AxisAlignedBB field_185553_f = new AxisAlignedBB(0.875D, 0.0D, 0.0D, 1.0D, 0.78125D, 1.0D);
        protected static final AxisAlignedBB field_185554_g = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.125D, 0.78125D, 1.0D);

        public BlockBannerHanging() {
            this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockBanner.BlockBannerHanging.field_176449_a, EnumFacing.NORTH));
        }

        public IBlockState func_185499_a(IBlockState iblockdata, Rotation enumblockrotation) {
            return iblockdata.func_177226_a(BlockBanner.BlockBannerHanging.field_176449_a, enumblockrotation.func_185831_a((EnumFacing) iblockdata.func_177229_b(BlockBanner.BlockBannerHanging.field_176449_a)));
        }

        public IBlockState func_185471_a(IBlockState iblockdata, Mirror enumblockmirror) {
            return iblockdata.func_185907_a(enumblockmirror.func_185800_a((EnumFacing) iblockdata.func_177229_b(BlockBanner.BlockBannerHanging.field_176449_a)));
        }

        public AxisAlignedBB func_185496_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
            switch ((EnumFacing) iblockdata.func_177229_b(BlockBanner.BlockBannerHanging.field_176449_a)) {
            case NORTH:
            default:
                return BlockBanner.BlockBannerHanging.field_185551_d;

            case SOUTH:
                return BlockBanner.BlockBannerHanging.field_185552_e;

            case WEST:
                return BlockBanner.BlockBannerHanging.field_185553_f;

            case EAST:
                return BlockBanner.BlockBannerHanging.field_185554_g;
            }
        }

        public void func_189540_a(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
            EnumFacing enumdirection = (EnumFacing) iblockdata.func_177229_b(BlockBanner.BlockBannerHanging.field_176449_a);

            if (!world.func_180495_p(blockposition.func_177972_a(enumdirection.func_176734_d())).func_185904_a().func_76220_a()) {
                this.func_176226_b(world, blockposition, iblockdata, 0);
                world.func_175698_g(blockposition);
            }

            super.func_189540_a(iblockdata, world, blockposition, block, blockposition1);
        }

        public IBlockState func_176203_a(int i) {
            EnumFacing enumdirection = EnumFacing.func_82600_a(i);

            if (enumdirection.func_176740_k() == EnumFacing.Axis.Y) {
                enumdirection = EnumFacing.NORTH;
            }

            return this.func_176223_P().func_177226_a(BlockBanner.BlockBannerHanging.field_176449_a, enumdirection);
        }

        public int func_176201_c(IBlockState iblockdata) {
            return ((EnumFacing) iblockdata.func_177229_b(BlockBanner.BlockBannerHanging.field_176449_a)).func_176745_a();
        }

        protected BlockStateContainer func_180661_e() {
            return new BlockStateContainer(this, new IProperty[] { BlockBanner.BlockBannerHanging.field_176449_a});
        }
    }
}
