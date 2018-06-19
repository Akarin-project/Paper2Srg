package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
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
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockNetherWart extends BlockBush {

    public static final PropertyInteger field_176486_a = PropertyInteger.func_177719_a("age", 0, 3);
    private static final AxisAlignedBB[] field_185519_c = new AxisAlignedBB[] { new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.3125D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.6875D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.875D, 1.0D)};

    protected BlockNetherWart() {
        super(Material.field_151585_k, MapColor.field_151645_D);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockNetherWart.field_176486_a, Integer.valueOf(0)));
        this.func_149675_a(true);
        this.func_149647_a((CreativeTabs) null);
    }

    public AxisAlignedBB func_185496_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockNetherWart.field_185519_c[((Integer) iblockdata.func_177229_b(BlockNetherWart.field_176486_a)).intValue()];
    }

    protected boolean func_185514_i(IBlockState iblockdata) {
        return iblockdata.func_177230_c() == Blocks.field_150425_aM;
    }

    public boolean func_180671_f(World world, BlockPos blockposition, IBlockState iblockdata) {
        return this.func_185514_i(world.func_180495_p(blockposition.func_177977_b()));
    }

    public void func_180650_b(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        int i = ((Integer) iblockdata.func_177229_b(BlockNetherWart.field_176486_a)).intValue();

        if (i < 3 && random.nextInt(Math.max(1, (int) (100.0F / world.spigotConfig.wartModifier) * 10)) == 0) { // Spigot
            iblockdata = iblockdata.func_177226_a(BlockNetherWart.field_176486_a, Integer.valueOf(i + 1));
            // world.setTypeAndData(blockposition, iblockdata, 2); // CraftBukkit
            org.bukkit.craftbukkit.event.CraftEventFactory.handleBlockGrowEvent(world, blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p(), this, func_176201_c(iblockdata)); // CraftBukkit
        }

        super.func_180650_b(world, blockposition, iblockdata, random);
    }

    public void func_180653_a(World world, BlockPos blockposition, IBlockState iblockdata, float f, int i) {
        if (!world.field_72995_K) {
            int j = 1;

            if (((Integer) iblockdata.func_177229_b(BlockNetherWart.field_176486_a)).intValue() >= 3) {
                j = 2 + world.field_73012_v.nextInt(3);
                if (i > 0) {
                    j += world.field_73012_v.nextInt(i + 1);
                }
            }

            for (int k = 0; k < j; ++k) {
                func_180635_a(world, blockposition, new ItemStack(Items.field_151075_bm));
            }

        }
    }

    public Item func_180660_a(IBlockState iblockdata, Random random, int i) {
        return Items.field_190931_a;
    }

    public int func_149745_a(Random random) {
        return 0;
    }

    public ItemStack func_185473_a(World world, BlockPos blockposition, IBlockState iblockdata) {
        return new ItemStack(Items.field_151075_bm);
    }

    public IBlockState func_176203_a(int i) {
        return this.func_176223_P().func_177226_a(BlockNetherWart.field_176486_a, Integer.valueOf(i));
    }

    public int func_176201_c(IBlockState iblockdata) {
        return ((Integer) iblockdata.func_177229_b(BlockNetherWart.field_176486_a)).intValue();
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockNetherWart.field_176486_a});
    }
}
