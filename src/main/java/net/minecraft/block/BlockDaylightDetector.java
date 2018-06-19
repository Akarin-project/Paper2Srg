package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityDaylightDetector;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDaylightDetector extends BlockContainer {

    public static final PropertyInteger field_176436_a = PropertyInteger.func_177719_a("power", 0, 15);
    protected static final AxisAlignedBB field_185566_b = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.375D, 1.0D);
    private final boolean field_176435_b;

    public BlockDaylightDetector(boolean flag) {
        super(Material.field_151575_d);
        this.field_176435_b = flag;
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockDaylightDetector.field_176436_a, Integer.valueOf(0)));
        this.func_149647_a(CreativeTabs.field_78028_d);
        this.func_149711_c(0.2F);
        this.func_149672_a(SoundType.field_185848_a);
        this.func_149663_c("daylightDetector");
    }

    public AxisAlignedBB func_185496_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockDaylightDetector.field_185566_b;
    }

    public int func_180656_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition, EnumFacing enumdirection) {
        return ((Integer) iblockdata.func_177229_b(BlockDaylightDetector.field_176436_a)).intValue();
    }

    public void func_180677_d(World world, BlockPos blockposition) {
        if (world.field_73011_w.func_191066_m()) {
            IBlockState iblockdata = world.func_180495_p(blockposition);
            int i = world.func_175642_b(EnumSkyBlock.SKY, blockposition) - world.func_175657_ab();
            float f = world.func_72929_e(1.0F);

            if (this.field_176435_b) {
                i = 15 - i;
            }

            if (i > 0 && !this.field_176435_b) {
                float f1 = f < 3.1415927F ? 0.0F : 6.2831855F;

                f += (f1 - f) * 0.2F;
                i = Math.round((float) i * MathHelper.func_76134_b(f));
            }

            i = MathHelper.func_76125_a(i, 0, 15);
            if (((Integer) iblockdata.func_177229_b(BlockDaylightDetector.field_176436_a)).intValue() != i) {
                i = org.bukkit.craftbukkit.event.CraftEventFactory.callRedstoneChange(world, blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p(), ((Integer) iblockdata.func_177229_b(field_176436_a)), i).getNewCurrent(); // CraftBukkit - Call BlockRedstoneEvent
                world.func_180501_a(blockposition, iblockdata.func_177226_a(BlockDaylightDetector.field_176436_a, Integer.valueOf(i)), 3);
            }

        }
    }

    public boolean func_180639_a(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        if (entityhuman.func_175142_cm()) {
            if (world.field_72995_K) {
                return true;
            } else {
                if (this.field_176435_b) {
                    world.func_180501_a(blockposition, Blocks.field_150453_bW.func_176223_P().func_177226_a(BlockDaylightDetector.field_176436_a, iblockdata.func_177229_b(BlockDaylightDetector.field_176436_a)), 4);
                    Blocks.field_150453_bW.func_180677_d(world, blockposition);
                } else {
                    world.func_180501_a(blockposition, Blocks.field_180402_cm.func_176223_P().func_177226_a(BlockDaylightDetector.field_176436_a, iblockdata.func_177229_b(BlockDaylightDetector.field_176436_a)), 4);
                    Blocks.field_180402_cm.func_180677_d(world, blockposition);
                }

                return true;
            }
        } else {
            return super.func_180639_a(world, blockposition, iblockdata, entityhuman, enumhand, enumdirection, f, f1, f2);
        }
    }

    public Item func_180660_a(IBlockState iblockdata, Random random, int i) {
        return Item.func_150898_a(Blocks.field_150453_bW);
    }

    public ItemStack func_185473_a(World world, BlockPos blockposition, IBlockState iblockdata) {
        return new ItemStack(Blocks.field_150453_bW);
    }

    public boolean func_149686_d(IBlockState iblockdata) {
        return false;
    }

    public boolean func_149662_c(IBlockState iblockdata) {
        return false;
    }

    public EnumBlockRenderType func_149645_b(IBlockState iblockdata) {
        return EnumBlockRenderType.MODEL;
    }

    public boolean func_149744_f(IBlockState iblockdata) {
        return true;
    }

    public TileEntity func_149915_a(World world, int i) {
        return new TileEntityDaylightDetector();
    }

    public IBlockState func_176203_a(int i) {
        return this.func_176223_P().func_177226_a(BlockDaylightDetector.field_176436_a, Integer.valueOf(i));
    }

    public int func_176201_c(IBlockState iblockdata) {
        return ((Integer) iblockdata.func_177229_b(BlockDaylightDetector.field_176436_a)).intValue();
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockDaylightDetector.field_176436_a});
    }

    public void func_149666_a(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        if (!this.field_176435_b) {
            super.func_149666_a(creativemodetab, nonnulllist);
        }

    }

    public BlockFaceShape func_193383_a(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return enumdirection == EnumFacing.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    }
}
