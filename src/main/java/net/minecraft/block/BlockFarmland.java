package net.minecraft.block;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.entity.EntityInteractEvent;

// CraftBukkit start
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.craftbukkit.event.CraftEventFactory;
// CraftBukkit end

public class BlockFarmland extends Block {

    public static final PropertyInteger MOISTURE = PropertyInteger.create("moisture", 0, 7);
    protected static final AxisAlignedBB FARMLAND_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.9375D, 1.0D);
    protected static final AxisAlignedBB field_194405_c = new AxisAlignedBB(0.0D, 0.9375D, 0.0D, 1.0D, 1.0D, 1.0D);

    protected BlockFarmland() {
        super(Material.GROUND);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockFarmland.MOISTURE, Integer.valueOf(0)));
        this.setTickRandomly(true);
        this.setLightOpacity(255);
    }

    public AxisAlignedBB getBoundingBox(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockFarmland.FARMLAND_AABB;
    }

    public boolean isOpaqueCube(IBlockState iblockdata) {
        return false;
    }

    public boolean isFullCube(IBlockState iblockdata) {
        return false;
    }

    public void updateTick(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        int i = ((Integer) iblockdata.getValue(BlockFarmland.MOISTURE)).intValue();

        if (!this.hasWater(world, blockposition) && !world.isRainingAt(blockposition.up())) {
            if (i > 0) {
                world.setBlockState(blockposition, iblockdata.withProperty(BlockFarmland.MOISTURE, Integer.valueOf(i - 1)), 2);
            } else if (!this.hasCrops(world, blockposition)) {
                turnToDirt(world, blockposition);
            }
        } else if (i < 7) {
            world.setBlockState(blockposition, iblockdata.withProperty(BlockFarmland.MOISTURE, Integer.valueOf(7)), 2);
        }

    }

    public void onFallenUpon(World world, BlockPos blockposition, Entity entity, float f) {
        super.onFallenUpon(world, blockposition, entity, f); // CraftBukkit - moved here as game rules / events shouldn't affect fall damage.
        if (!world.isRemote && world.rand.nextFloat() < f - 0.5F && entity instanceof EntityLivingBase && (entity instanceof EntityPlayer || world.getGameRules().getBoolean("mobGriefing")) && entity.width * entity.width * entity.height > 0.512F) {
            // CraftBukkit start - Interact soil
            org.bukkit.event.Cancellable cancellable;
            if (entity instanceof EntityPlayer) {
                cancellable = CraftEventFactory.callPlayerInteractEvent((EntityPlayer) entity, org.bukkit.event.block.Action.PHYSICAL, blockposition, null, null, null);
            } else {
                cancellable = new EntityInteractEvent(entity.getBukkitEntity(), world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ()));
                world.getServer().getPluginManager().callEvent((EntityInteractEvent) cancellable);
            }

            if (cancellable.isCancelled()) {
                return;
            }

            if (CraftEventFactory.callEntityChangeBlockEvent(entity, blockposition, Blocks.DIRT, 0).isCancelled()) {
                return;
            }
            // CraftBukkit end
            turnToDirt(world, blockposition);
        }

        // super.fallOn(world, blockposition, entity, f); // CraftBukkit - moved up
    }

    protected static void turnToDirt(World world, BlockPos blockposition) {
        // CraftBukkit start
        org.bukkit.block.Block block = world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());
        if (CraftEventFactory.callBlockFadeEvent(block, Blocks.DIRT).isCancelled()) {
            return;
        }
        // CraftBukkit end
        world.setBlockState(blockposition, Blocks.DIRT.getDefaultState());
        AxisAlignedBB axisalignedbb = BlockFarmland.field_194405_c.offset(blockposition);
        List list = world.getEntitiesWithinAABBExcludingEntity((Entity) null, axisalignedbb);
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            Entity entity = (Entity) iterator.next();
            double d0 = Math.min(axisalignedbb.maxY - axisalignedbb.minY, axisalignedbb.maxY - entity.getEntityBoundingBox().minY);

            entity.setPositionAndUpdate(entity.posX, entity.posY + d0 + 0.001D, entity.posZ);
        }

    }

    private boolean hasCrops(World world, BlockPos blockposition) {
        Block block = world.getBlockState(blockposition.up()).getBlock();

        return block instanceof BlockCrops || block instanceof BlockStem;
    }

    private boolean hasWater(World world, BlockPos blockposition) {
        Iterator iterator = BlockPos.getAllInBoxMutable(blockposition.add(-4, 0, -4), blockposition.add(4, 1, 4)).iterator();

        BlockPos.MutableBlockPos blockposition_mutableblockposition;

        do {
            if (!iterator.hasNext()) {
                return false;
            }

            blockposition_mutableblockposition = (BlockPos.MutableBlockPos) iterator.next();
        } while (world.getBlockState(blockposition_mutableblockposition).getMaterial() != Material.WATER);

        return true;
    }

    public void neighborChanged(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        super.neighborChanged(iblockdata, world, blockposition, block, blockposition1);
        if (world.getBlockState(blockposition.up()).getMaterial().isSolid()) {
            turnToDirt(world, blockposition);
        }

    }

    public void onBlockAdded(World world, BlockPos blockposition, IBlockState iblockdata) {
        super.onBlockAdded(world, blockposition, iblockdata);
        if (world.getBlockState(blockposition.up()).getMaterial().isSolid()) {
            turnToDirt(world, blockposition);
        }

    }

    public Item getItemDropped(IBlockState iblockdata, Random random, int i) {
        return Blocks.DIRT.getItemDropped(Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT), random, i);
    }

    public IBlockState getStateFromMeta(int i) {
        return this.getDefaultState().withProperty(BlockFarmland.MOISTURE, Integer.valueOf(i & 7));
    }

    public int getMetaFromState(IBlockState iblockdata) {
        return ((Integer) iblockdata.getValue(BlockFarmland.MOISTURE)).intValue();
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockFarmland.MOISTURE});
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return enumdirection == EnumFacing.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    }
}
