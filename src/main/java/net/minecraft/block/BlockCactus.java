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

    public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 15);
    protected static final AxisAlignedBB CACTUS_COLLISION_AABB = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.9375D, 0.9375D);
    protected static final AxisAlignedBB CACTUS_AABB = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 1.0D, 0.9375D);

    protected BlockCactus() {
        super(Material.CACTUS);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockCactus.AGE, Integer.valueOf(0)));
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }

    public void updateTick(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        BlockPos blockposition1 = blockposition.up();

        if (world.isAirBlock(blockposition1)) {
            int i;

            for (i = 1; world.getBlockState(blockposition.down(i)).getBlock() == this; ++i) {
                ;
            }

            if (i < world.paperConfig.cactusMaxHeight) { // Paper - Configurable growth height
                int j = ((Integer) iblockdata.getValue(BlockCactus.AGE)).intValue();

                if (j >= (byte) range(3, ((100.0F / world.spigotConfig.cactusModifier) * 15) + 0.5F, 15)) { // Spigot
                    // world.setTypeUpdate(blockposition1, this.getBlockData()); // CraftBukkit
                    IBlockState iblockdata1 = iblockdata.withProperty(BlockCactus.AGE, Integer.valueOf(0));

                    CraftEventFactory.handleBlockGrowEvent(world, blockposition1.getX(), blockposition1.getY(), blockposition1.getZ(), this, 0); // CraftBukkit
                    world.setBlockState(blockposition, iblockdata1, 4);
                    iblockdata1.neighborChanged(world, blockposition1, this, blockposition);
                } else {
                    world.setBlockState(blockposition, iblockdata.withProperty(BlockCactus.AGE, Integer.valueOf(j + 1)), 4);
                }

            }
        }
    }

    public AxisAlignedBB getCollisionBoundingBox(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockCactus.CACTUS_COLLISION_AABB;
    }

    public boolean isFullCube(IBlockState iblockdata) {
        return false;
    }

    public boolean isOpaqueCube(IBlockState iblockdata) {
        return false;
    }

    public boolean canPlaceBlockAt(World world, BlockPos blockposition) {
        return super.canPlaceBlockAt(world, blockposition) ? this.canBlockStay(world, blockposition) : false;
    }

    public void neighborChanged(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        if (!this.canBlockStay(world, blockposition)) {
            world.destroyBlock(blockposition, true);
        }

    }

    public boolean canBlockStay(World world, BlockPos blockposition) {
        Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();

        Material material;

        do {
            if (!iterator.hasNext()) {
                Block block = world.getBlockState(blockposition.down()).getBlock();

                return block == Blocks.CACTUS || block == Blocks.SAND && !world.getBlockState(blockposition.up()).getMaterial().isLiquid();
            }

            EnumFacing enumdirection = (EnumFacing) iterator.next();

            material = world.getBlockState(blockposition.offset(enumdirection)).getMaterial();
        } while (!material.isSolid() && material != Material.LAVA);

        return false;
    }

    public void onEntityCollidedWithBlock(World world, BlockPos blockposition, IBlockState iblockdata, Entity entity) {
        CraftEventFactory.blockDamage = world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ()); // CraftBukkit
        entity.attackEntityFrom(DamageSource.CACTUS, 1.0F);
        CraftEventFactory.blockDamage = null; // CraftBukkit
    }

    public IBlockState getStateFromMeta(int i) {
        return this.getDefaultState().withProperty(BlockCactus.AGE, Integer.valueOf(i));
    }

    public int getMetaFromState(IBlockState iblockdata) {
        return ((Integer) iblockdata.getValue(BlockCactus.AGE)).intValue();
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockCactus.AGE});
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return BlockFaceShape.UNDEFINED;
    }
}
