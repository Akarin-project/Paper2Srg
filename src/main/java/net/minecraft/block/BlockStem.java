package net.minecraft.block;

import java.util.Iterator;
import java.util.Random;
import javax.annotation.Nullable;


import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.bukkit.craftbukkit.event.CraftEventFactory;

public class BlockStem extends BlockBush implements IGrowable {

    public static final PropertyInteger field_176484_a = PropertyInteger.func_177719_a("age", 0, 7);
    public static final PropertyDirection field_176483_b = BlockTorch.field_176596_a;
    private final Block field_149877_a;
    protected static final AxisAlignedBB[] field_185521_d = new AxisAlignedBB[] { new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 0.125D, 0.625D), new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 0.25D, 0.625D), new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 0.375D, 0.625D), new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 0.5D, 0.625D), new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 0.625D, 0.625D), new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 0.75D, 0.625D), new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 0.875D, 0.625D), new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 1.0D, 0.625D)};

    protected BlockStem(Block block) {
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockStem.field_176484_a, Integer.valueOf(0)).func_177226_a(BlockStem.field_176483_b, EnumFacing.UP));
        this.field_149877_a = block;
        this.func_149675_a(true);
        this.func_149647_a((CreativeTabs) null);
    }

    public AxisAlignedBB func_185496_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockStem.field_185521_d[((Integer) iblockdata.func_177229_b(BlockStem.field_176484_a)).intValue()];
    }

    public IBlockState func_176221_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        int i = ((Integer) iblockdata.func_177229_b(BlockStem.field_176484_a)).intValue();

        iblockdata = iblockdata.func_177226_a(BlockStem.field_176483_b, EnumFacing.UP);
        Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();

        while (iterator.hasNext()) {
            EnumFacing enumdirection = (EnumFacing) iterator.next();

            if (iblockaccess.func_180495_p(blockposition.func_177972_a(enumdirection)).func_177230_c() == this.field_149877_a && i == 7) {
                iblockdata = iblockdata.func_177226_a(BlockStem.field_176483_b, enumdirection);
                break;
            }
        }

        return iblockdata;
    }

    protected boolean func_185514_i(IBlockState iblockdata) {
        return iblockdata.func_177230_c() == Blocks.field_150458_ak;
    }

    public void func_180650_b(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        super.func_180650_b(world, blockposition, iblockdata, random);
        if (world.isLightLevel(blockposition.func_177984_a(), 9)) { // Paper
            float f = BlockCrops.func_180672_a((Block) this, world, blockposition);

            if (random.nextInt((int) ((100.0F / (this == Blocks.field_150393_bb ? world.spigotConfig.pumpkinModifier : world.spigotConfig.melonModifier)) * (25.0F / f)) + 1) == 0) { // Spigot
                int i = ((Integer) iblockdata.func_177229_b(BlockStem.field_176484_a)).intValue();

                if (i < 7) {
                    iblockdata = iblockdata.func_177226_a(BlockStem.field_176484_a, Integer.valueOf(i + 1));
                    // world.setTypeAndData(blockposition, iblockdata, 2); // CraftBukkit
                    CraftEventFactory.handleBlockGrowEvent(world, blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p(), this, func_176201_c(iblockdata)); // CraftBukkit
                } else {
                    Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();

                    while (iterator.hasNext()) {
                        EnumFacing enumdirection = (EnumFacing) iterator.next();

                        if (world.func_180495_p(blockposition.func_177972_a(enumdirection)).func_177230_c() == this.field_149877_a) {
                            return;
                        }
                    }

                    blockposition = blockposition.func_177972_a(EnumFacing.Plane.HORIZONTAL.func_179518_a(random));
                    Block block = world.func_180495_p(blockposition.func_177977_b()).func_177230_c();

                    if (world.func_180495_p(blockposition).func_177230_c().field_149764_J == Material.field_151579_a && (block == Blocks.field_150458_ak || block == Blocks.field_150346_d || block == Blocks.field_150349_c)) {
                        // world.setTypeUpdate(blockposition, this.blockFruit.getBlockData()); // CraftBukkit
                        CraftEventFactory.handleBlockGrowEvent(world, blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p(), this.field_149877_a, 0); // CraftBukkit
                    }
                }
            }

        }
    }

    public void func_176482_g(World world, BlockPos blockposition, IBlockState iblockdata) {
        int i = ((Integer) iblockdata.func_177229_b(BlockStem.field_176484_a)).intValue() + MathHelper.func_76136_a(world.field_73012_v, 2, 5);

        // world.setTypeAndData(blockposition, iblockdata.set(BlockStem.AGE, Integer.valueOf(Math.min(7, i))), 2);
        CraftEventFactory.handleBlockGrowEvent(world, blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p(), this, Math.min(7, i)); // CraftBukkit
    }

    public void func_180653_a(World world, BlockPos blockposition, IBlockState iblockdata, float f, int i) {
        super.func_180653_a(world, blockposition, iblockdata, f, i);
        if (!world.field_72995_K) {
            Item item = this.func_176481_j();

            if (item != null) {
                int j = ((Integer) iblockdata.func_177229_b(BlockStem.field_176484_a)).intValue();

                for (int k = 0; k < 3; ++k) {
                    if (world.field_73012_v.nextInt(15) <= j) {
                        func_180635_a(world, blockposition, new ItemStack(item));
                    }
                }

            }
        }
    }

    @Nullable
    protected Item func_176481_j() {
        return this.field_149877_a == Blocks.field_150423_aK ? Items.field_151080_bb : (this.field_149877_a == Blocks.field_150440_ba ? Items.field_151081_bc : null);
    }

    public Item func_180660_a(IBlockState iblockdata, Random random, int i) {
        return Items.field_190931_a;
    }

    public ItemStack func_185473_a(World world, BlockPos blockposition, IBlockState iblockdata) {
        Item item = this.func_176481_j();

        return item == null ? ItemStack.field_190927_a : new ItemStack(item);
    }

    public boolean func_176473_a(World world, BlockPos blockposition, IBlockState iblockdata, boolean flag) {
        return ((Integer) iblockdata.func_177229_b(BlockStem.field_176484_a)).intValue() != 7;
    }

    public boolean func_180670_a(World world, Random random, BlockPos blockposition, IBlockState iblockdata) {
        return true;
    }

    public void func_176474_b(World world, Random random, BlockPos blockposition, IBlockState iblockdata) {
        this.func_176482_g(world, blockposition, iblockdata);
    }

    public IBlockState func_176203_a(int i) {
        return this.func_176223_P().func_177226_a(BlockStem.field_176484_a, Integer.valueOf(i));
    }

    public int func_176201_c(IBlockState iblockdata) {
        return ((Integer) iblockdata.func_177229_b(BlockStem.field_176484_a)).intValue();
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockStem.field_176484_a, BlockStem.field_176483_b});
    }
}
