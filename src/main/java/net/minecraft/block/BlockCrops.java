package net.minecraft.block;

import java.util.Random;


import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.bukkit.craftbukkit.event.CraftEventFactory;

public class BlockCrops extends BlockBush implements IGrowable {

    public static final PropertyInteger field_176488_a = PropertyInteger.func_177719_a("age", 0, 7);
    private static final AxisAlignedBB[] field_185530_a = new AxisAlignedBB[] { new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.25D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.375D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.625D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.75D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.875D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D)};

    protected BlockCrops() {
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(this.func_185524_e(), Integer.valueOf(0)));
        this.func_149675_a(true);
        this.func_149647_a((CreativeTabs) null);
        this.func_149711_c(0.0F);
        this.func_149672_a(SoundType.field_185850_c);
        this.func_149649_H();
    }

    public AxisAlignedBB func_185496_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockCrops.field_185530_a[((Integer) iblockdata.func_177229_b(this.func_185524_e())).intValue()];
    }

    protected boolean func_185514_i(IBlockState iblockdata) {
        return iblockdata.func_177230_c() == Blocks.field_150458_ak;
    }

    protected PropertyInteger func_185524_e() {
        return BlockCrops.field_176488_a;
    }

    public int func_185526_g() {
        return 7;
    }

    protected int func_185527_x(IBlockState iblockdata) {
        return ((Integer) iblockdata.func_177229_b(this.func_185524_e())).intValue();
    }

    public IBlockState func_185528_e(int i) {
        return this.func_176223_P().func_177226_a(this.func_185524_e(), Integer.valueOf(i));
    }

    public boolean func_185525_y(IBlockState iblockdata) {
        return ((Integer) iblockdata.func_177229_b(this.func_185524_e())).intValue() >= this.func_185526_g();
    }

    public void func_180650_b(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        super.func_180650_b(world, blockposition, iblockdata, random);
        if (world.isLightLevel(blockposition.func_177984_a(), 9)) { // Paper
            int i = this.func_185527_x(iblockdata);

            if (i < this.func_185526_g()) {
                float f = func_180672_a((Block) this, world, blockposition);

                if (random.nextInt((int) ((100.0F / world.spigotConfig.wheatModifier) * (25.0F / f)) + 1) == 0) { // Spigot
                    // CraftBukkit start
                    IBlockState data = this.func_185528_e(i + 1);
                    CraftEventFactory.handleBlockGrowEvent(world, blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p(), this, func_176201_c(data));
                    // CraftBukkit end
                }
            }
        }

    }

    public void func_176487_g(World world, BlockPos blockposition, IBlockState iblockdata) {
        int i = this.func_185527_x(iblockdata) + this.func_185529_b(world);
        int j = this.func_185526_g();

        if (i > j) {
            i = j;
        }

        // CraftBukkit start
        IBlockState data = this.func_185528_e(i);
        CraftEventFactory.handleBlockGrowEvent(world, blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p(), this, func_176201_c(data));
        // CraftBukkit end
    }

    protected int func_185529_b(World world) {
        return MathHelper.func_76136_a(world.field_73012_v, 2, 5);
    }

    protected static float func_180672_a(Block block, World world, BlockPos blockposition) {
        float f = 1.0F;
        BlockPos blockposition1 = blockposition.func_177977_b();

        for (int i = -1; i <= 1; ++i) {
            for (int j = -1; j <= 1; ++j) {
                float f1 = 0.0F;
                IBlockState iblockdata = world.func_180495_p(blockposition1.func_177982_a(i, 0, j));

                if (iblockdata.func_177230_c() == Blocks.field_150458_ak) {
                    f1 = 1.0F;
                    if (((Integer) iblockdata.func_177229_b(BlockFarmland.field_176531_a)).intValue() > 0) {
                        f1 = 3.0F;
                    }
                }

                if (i != 0 || j != 0) {
                    f1 /= 4.0F;
                }

                f += f1;
            }
        }

        BlockPos blockposition2 = blockposition.func_177978_c();
        BlockPos blockposition3 = blockposition.func_177968_d();
        BlockPos blockposition4 = blockposition.func_177976_e();
        BlockPos blockposition5 = blockposition.func_177974_f();
        boolean flag = block == world.func_180495_p(blockposition4).func_177230_c() || block == world.func_180495_p(blockposition5).func_177230_c();
        boolean flag1 = block == world.func_180495_p(blockposition2).func_177230_c() || block == world.func_180495_p(blockposition3).func_177230_c();

        if (flag && flag1) {
            f /= 2.0F;
        } else {
            boolean flag2 = block == world.func_180495_p(blockposition4.func_177978_c()).func_177230_c() || block == world.func_180495_p(blockposition5.func_177978_c()).func_177230_c() || block == world.func_180495_p(blockposition5.func_177968_d()).func_177230_c() || block == world.func_180495_p(blockposition4.func_177968_d()).func_177230_c();

            if (flag2) {
                f /= 2.0F;
            }
        }

        return f;
    }

    public boolean func_180671_f(World world, BlockPos blockposition, IBlockState iblockdata) {
        return (world.func_175699_k(blockposition) >= 8 || world.func_175678_i(blockposition)) && this.func_185514_i(world.func_180495_p(blockposition.func_177977_b()));
    }

    protected Item func_149866_i() {
        return Items.field_151014_N;
    }

    protected Item func_149865_P() {
        return Items.field_151015_O;
    }

    public void func_180653_a(World world, BlockPos blockposition, IBlockState iblockdata, float f, int i) {
        super.func_180653_a(world, blockposition, iblockdata, f, 0);
        if (!world.field_72995_K) {
            int j = this.func_185527_x(iblockdata);

            if (j >= this.func_185526_g()) {
                int k = 3 + i;

                for (int l = 0; l < k; ++l) {
                    if (world.field_73012_v.nextInt(2 * this.func_185526_g()) <= j) {
                        func_180635_a(world, blockposition, new ItemStack(this.func_149866_i()));
                    }
                }
            }

        }
    }

    public Item func_180660_a(IBlockState iblockdata, Random random, int i) {
        return this.func_185525_y(iblockdata) ? this.func_149865_P() : this.func_149866_i();
    }

    public ItemStack func_185473_a(World world, BlockPos blockposition, IBlockState iblockdata) {
        return new ItemStack(this.func_149866_i());
    }

    public boolean func_176473_a(World world, BlockPos blockposition, IBlockState iblockdata, boolean flag) {
        return !this.func_185525_y(iblockdata);
    }

    public boolean func_180670_a(World world, Random random, BlockPos blockposition, IBlockState iblockdata) {
        return true;
    }

    public void func_176474_b(World world, Random random, BlockPos blockposition, IBlockState iblockdata) {
        this.func_176487_g(world, blockposition, iblockdata);
    }

    public IBlockState func_176203_a(int i) {
        return this.func_185528_e(i);
    }

    public int func_176201_c(IBlockState iblockdata) {
        return this.func_185527_x(iblockdata);
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockCrops.field_176488_a});
    }
}
