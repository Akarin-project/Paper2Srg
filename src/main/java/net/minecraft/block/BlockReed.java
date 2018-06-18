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

    public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 15);
    protected static final AxisAlignedBB REED_AABB = new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 1.0D, 0.875D);

    protected BlockReed() {
        super(Material.PLANTS);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockReed.AGE, Integer.valueOf(0)));
        this.setTickRandomly(true);
    }

    public AxisAlignedBB getBoundingBox(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockReed.REED_AABB;
    }

    public void updateTick(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        if (world.getBlockState(blockposition.down()).getBlock() == Blocks.REEDS || this.checkForDrop(world, blockposition, iblockdata)) {
            if (world.isAirBlock(blockposition.up())) {
                int i;

                for (i = 1; world.getBlockState(blockposition.down(i)).getBlock() == this; ++i) {
                    ;
                }

                if (i < world.paperConfig.reedMaxHeight) { // Paper - Configurable growth height
                    int j = ((Integer) iblockdata.getValue(BlockReed.AGE)).intValue();

                    if (j >= (byte) range(3, ((100.0F / world.spigotConfig.caneModifier) * 15) + 0.5F, 15)) { // Spigot
                        // CraftBukkit start
                        // world.setTypeUpdate(blockposition.up(), this.getBlockData()); // CraftBukkit
                        BlockPos upPos = blockposition.up();
                        org.bukkit.craftbukkit.event.CraftEventFactory.handleBlockGrowEvent(world, upPos.getX(), upPos.getY(), upPos.getZ(), this, 0);
                        world.setBlockState(blockposition, iblockdata.withProperty(BlockReed.AGE, Integer.valueOf(0)), 4);
                        // CraftBukkit end
                    } else {
                        world.setBlockState(blockposition, iblockdata.withProperty(BlockReed.AGE, Integer.valueOf(j + 1)), 4);
                    }
                }
            }

        }
    }

    public boolean canPlaceBlockAt(World world, BlockPos blockposition) {
        Block block = world.getBlockState(blockposition.down()).getBlock();

        if (block == this) {
            return true;
        } else if (block != Blocks.GRASS && block != Blocks.DIRT && block != Blocks.SAND) {
            return false;
        } else {
            BlockPos blockposition1 = blockposition.down();
            Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();

            IBlockState iblockdata;

            do {
                if (!iterator.hasNext()) {
                    return false;
                }

                EnumFacing enumdirection = (EnumFacing) iterator.next();

                iblockdata = world.getBlockState(blockposition1.offset(enumdirection));
            } while (iblockdata.getMaterial() != Material.WATER && iblockdata.getBlock() != Blocks.FROSTED_ICE);

            return true;
        }
    }

    public void neighborChanged(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        this.checkForDrop(world, blockposition, iblockdata);
    }

    protected final boolean checkForDrop(World world, BlockPos blockposition, IBlockState iblockdata) {
        if (this.canBlockStay(world, blockposition)) {
            return true;
        } else {
            this.dropBlockAsItem(world, blockposition, iblockdata, 0);
            world.setBlockToAir(blockposition);
            return false;
        }
    }

    public boolean canBlockStay(World world, BlockPos blockposition) {
        return this.canPlaceBlockAt(world, blockposition);
    }

    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockReed.NULL_AABB;
    }

    public Item getItemDropped(IBlockState iblockdata, Random random, int i) {
        return Items.REEDS;
    }

    public boolean isOpaqueCube(IBlockState iblockdata) {
        return false;
    }

    public boolean isFullCube(IBlockState iblockdata) {
        return false;
    }

    public ItemStack getItem(World world, BlockPos blockposition, IBlockState iblockdata) {
        return new ItemStack(Items.REEDS);
    }

    public IBlockState getStateFromMeta(int i) {
        return this.getDefaultState().withProperty(BlockReed.AGE, Integer.valueOf(i));
    }

    public int getMetaFromState(IBlockState iblockdata) {
        return ((Integer) iblockdata.getValue(BlockReed.AGE)).intValue();
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockReed.AGE});
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return BlockFaceShape.UNDEFINED;
    }
}
