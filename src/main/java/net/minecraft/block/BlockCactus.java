package net.minecraft.block;

import java.util.Iterator;
import java.util.Random;


import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.bukkit.craftbukkit.event.CraftEventFactory;

public class BlockCactus extends Block {

    public static final PropertyInteger field_176587_a = PropertyInteger.func_177719_a("age", 0, 15);
    protected static final AxisAlignedBB field_185593_b = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.9375D, 0.9375D);
    protected static final AxisAlignedBB field_185594_c = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 1.0D, 0.9375D);

    protected BlockCactus() {
        super(Material.field_151570_A);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockCactus.field_176587_a, Integer.valueOf(0)));
        this.func_149675_a(true);
        this.func_149647_a(CreativeTabs.field_78031_c);
    }

    public void func_180650_b(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        BlockPos blockposition1 = blockposition.func_177984_a();

        if (world.func_175623_d(blockposition1)) {
            int i;

            for (i = 1; world.func_180495_p(blockposition.func_177979_c(i)).func_177230_c() == this; ++i) {
                ;
            }

            if (i < world.paperConfig.cactusMaxHeight) { // Paper - Configurable growth height
                int j = ((Integer) iblockdata.func_177229_b(BlockCactus.field_176587_a)).intValue();

                if (j >= (byte) range(3, ((100.0F / world.spigotConfig.cactusModifier) * 15) + 0.5F, 15)) { // Spigot
                    // world.setTypeUpdate(blockposition1, this.getBlockData()); // CraftBukkit
                    IBlockState iblockdata1 = iblockdata.func_177226_a(BlockCactus.field_176587_a, Integer.valueOf(0));

                    CraftEventFactory.handleBlockGrowEvent(world, blockposition1.func_177958_n(), blockposition1.func_177956_o(), blockposition1.func_177952_p(), this, 0); // CraftBukkit
                    world.func_180501_a(blockposition, iblockdata1, 4);
                    iblockdata1.func_189546_a(world, blockposition1, this, blockposition);
                } else {
                    world.func_180501_a(blockposition, iblockdata.func_177226_a(BlockCactus.field_176587_a, Integer.valueOf(j + 1)), 4);
                }

            }
        }
    }

    public AxisAlignedBB func_180646_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockCactus.field_185593_b;
    }

    public boolean func_149686_d(IBlockState iblockdata) {
        return false;
    }

    public boolean func_149662_c(IBlockState iblockdata) {
        return false;
    }

    public boolean func_176196_c(World world, BlockPos blockposition) {
        return super.func_176196_c(world, blockposition) ? this.func_176586_d(world, blockposition) : false;
    }

    public void func_189540_a(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        if (!this.func_176586_d(world, blockposition)) {
            world.func_175655_b(blockposition, true);
        }

    }

    public boolean func_176586_d(World world, BlockPos blockposition) {
        Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();

        Material material;

        do {
            if (!iterator.hasNext()) {
                Block block = world.func_180495_p(blockposition.func_177977_b()).func_177230_c();

                return block == Blocks.field_150434_aF || block == Blocks.field_150354_m && !world.func_180495_p(blockposition.func_177984_a()).func_185904_a().func_76224_d();
            }

            EnumFacing enumdirection = (EnumFacing) iterator.next();

            material = world.func_180495_p(blockposition.func_177972_a(enumdirection)).func_185904_a();
        } while (!material.func_76220_a() && material != Material.field_151587_i);

        return false;
    }

    public void func_180634_a(World world, BlockPos blockposition, IBlockState iblockdata, Entity entity) {
        CraftEventFactory.blockDamage = world.getWorld().getBlockAt(blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p()); // CraftBukkit
        entity.func_70097_a(DamageSource.field_76367_g, 1.0F);
        CraftEventFactory.blockDamage = null; // CraftBukkit
    }

    public IBlockState func_176203_a(int i) {
        return this.func_176223_P().func_177226_a(BlockCactus.field_176587_a, Integer.valueOf(i));
    }

    public int func_176201_c(IBlockState iblockdata) {
        return ((Integer) iblockdata.func_177229_b(BlockCactus.field_176587_a)).intValue();
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockCactus.field_176587_a});
    }

    public BlockFaceShape func_193383_a(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return BlockFaceShape.UNDEFINED;
    }
}
