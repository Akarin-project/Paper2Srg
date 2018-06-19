package net.minecraft.block;

import java.util.Iterator;
import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockReed extends Block {

    public static final PropertyInteger field_176355_a = PropertyInteger.func_177719_a("age", 0, 15);
    protected static final AxisAlignedBB field_185701_b = new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 1.0D, 0.875D);

    protected BlockReed() {
        super(Material.field_151585_k);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockReed.field_176355_a, Integer.valueOf(0)));
        this.func_149675_a(true);
    }

    public AxisAlignedBB func_185496_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockReed.field_185701_b;
    }

    public void func_180650_b(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        if (world.func_180495_p(blockposition.func_177977_b()).func_177230_c() == Blocks.field_150436_aH || this.func_176353_e(world, blockposition, iblockdata)) {
            if (world.func_175623_d(blockposition.func_177984_a())) {
                int i;

                for (i = 1; world.func_180495_p(blockposition.func_177979_c(i)).func_177230_c() == this; ++i) {
                    ;
                }

                if (i < world.paperConfig.reedMaxHeight) { // Paper - Configurable growth height
                    int j = ((Integer) iblockdata.func_177229_b(BlockReed.field_176355_a)).intValue();

                    if (j >= (byte) range(3, ((100.0F / world.spigotConfig.caneModifier) * 15) + 0.5F, 15)) { // Spigot
                        // CraftBukkit start
                        // world.setTypeUpdate(blockposition.up(), this.getBlockData()); // CraftBukkit
                        BlockPos upPos = blockposition.func_177984_a();
                        org.bukkit.craftbukkit.event.CraftEventFactory.handleBlockGrowEvent(world, upPos.func_177958_n(), upPos.func_177956_o(), upPos.func_177952_p(), this, 0);
                        world.func_180501_a(blockposition, iblockdata.func_177226_a(BlockReed.field_176355_a, Integer.valueOf(0)), 4);
                        // CraftBukkit end
                    } else {
                        world.func_180501_a(blockposition, iblockdata.func_177226_a(BlockReed.field_176355_a, Integer.valueOf(j + 1)), 4);
                    }
                }
            }

        }
    }

    public boolean func_176196_c(World world, BlockPos blockposition) {
        Block block = world.func_180495_p(blockposition.func_177977_b()).func_177230_c();

        if (block == this) {
            return true;
        } else if (block != Blocks.field_150349_c && block != Blocks.field_150346_d && block != Blocks.field_150354_m) {
            return false;
        } else {
            BlockPos blockposition1 = blockposition.func_177977_b();
            Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();

            IBlockState iblockdata;

            do {
                if (!iterator.hasNext()) {
                    return false;
                }

                EnumFacing enumdirection = (EnumFacing) iterator.next();

                iblockdata = world.func_180495_p(blockposition1.func_177972_a(enumdirection));
            } while (iblockdata.func_185904_a() != Material.field_151586_h && iblockdata.func_177230_c() != Blocks.field_185778_de);

            return true;
        }
    }

    public void func_189540_a(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        this.func_176353_e(world, blockposition, iblockdata);
    }

    protected final boolean func_176353_e(World world, BlockPos blockposition, IBlockState iblockdata) {
        if (this.func_176354_d(world, blockposition)) {
            return true;
        } else {
            this.func_176226_b(world, blockposition, iblockdata, 0);
            world.func_175698_g(blockposition);
            return false;
        }
    }

    public boolean func_176354_d(World world, BlockPos blockposition) {
        return this.func_176196_c(world, blockposition);
    }

    @Nullable
    public AxisAlignedBB func_180646_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockReed.field_185506_k;
    }

    public Item func_180660_a(IBlockState iblockdata, Random random, int i) {
        return Items.field_151120_aE;
    }

    public boolean func_149662_c(IBlockState iblockdata) {
        return false;
    }

    public boolean func_149686_d(IBlockState iblockdata) {
        return false;
    }

    public ItemStack func_185473_a(World world, BlockPos blockposition, IBlockState iblockdata) {
        return new ItemStack(Items.field_151120_aE);
    }

    public IBlockState func_176203_a(int i) {
        return this.func_176223_P().func_177226_a(BlockReed.field_176355_a, Integer.valueOf(i));
    }

    public int func_176201_c(IBlockState iblockdata) {
        return ((Integer) iblockdata.func_177229_b(BlockReed.field_176355_a)).intValue();
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockReed.field_176355_a});
    }

    public BlockFaceShape func_193383_a(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return BlockFaceShape.UNDEFINED;
    }
}
