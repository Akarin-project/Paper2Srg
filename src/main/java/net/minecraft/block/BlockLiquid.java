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
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockLiquid extends Block {

    public static final PropertyInteger LEVEL = PropertyInteger.create("level", 0, 15);

    protected BlockLiquid(Material material) {
        super(material);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockLiquid.LEVEL, Integer.valueOf(0)));
        this.setTickRandomly(true);
    }

    public AxisAlignedBB getBoundingBox(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockLiquid.FULL_BLOCK_AABB;
    }

    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockLiquid.NULL_AABB;
    }

    public boolean isPassable(IBlockAccess iblockaccess, BlockPos blockposition) {
        return this.blockMaterial != Material.LAVA;
    }

    public static float getLiquidHeightPercent(int i) {
        if (i >= 8) {
            i = 0;
        }

        return (float) (i + 1) / 9.0F;
    }

    protected int getDepth(IBlockState iblockdata) {
        return iblockdata.getMaterial() == this.blockMaterial ? ((Integer) iblockdata.getValue(BlockLiquid.LEVEL)).intValue() : -1;
    }

    protected int getRenderedDepth(IBlockState iblockdata) {
        int i = this.getDepth(iblockdata);

        return i >= 8 ? 0 : i;
    }

    public boolean isFullCube(IBlockState iblockdata) {
        return false;
    }

    public boolean isOpaqueCube(IBlockState iblockdata) {
        return false;
    }

    public boolean canCollideCheck(IBlockState iblockdata, boolean flag) {
        return flag && ((Integer) iblockdata.getValue(BlockLiquid.LEVEL)).intValue() == 0;
    }

    private boolean causesDownwardCurrent(IBlockAccess iblockaccess, BlockPos blockposition, EnumFacing enumdirection) {
        IBlockState iblockdata = iblockaccess.getBlockState(blockposition);
        Block block = iblockdata.getBlock();
        Material material = iblockdata.getMaterial();

        if (material == this.blockMaterial) {
            return false;
        } else if (enumdirection == EnumFacing.UP) {
            return true;
        } else if (material == Material.ICE) {
            return false;
        } else {
            boolean flag = isExceptBlockForAttachWithPiston(block) || block instanceof BlockStairs;

            return !flag && iblockdata.getBlockFaceShape(iblockaccess, blockposition, enumdirection) == BlockFaceShape.SOLID;
        }
    }

    public EnumBlockRenderType getRenderType(IBlockState iblockdata) {
        return EnumBlockRenderType.LIQUID;
    }

    public Item getItemDropped(IBlockState iblockdata, Random random, int i) {
        return Items.AIR;
    }

    public int quantityDropped(Random random) {
        return 0;
    }

    protected Vec3d getFlow(IBlockAccess iblockaccess, BlockPos blockposition, IBlockState iblockdata) {
        double d0 = 0.0D;
        double d1 = 0.0D;
        double d2 = 0.0D;
        int i = this.getRenderedDepth(iblockdata);
        BlockPos.PooledMutableBlockPos blockposition_pooledblockposition = BlockPos.PooledMutableBlockPos.retain();
        Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();

        while (iterator.hasNext()) {
            EnumFacing enumdirection = (EnumFacing) iterator.next();

            blockposition_pooledblockposition.setPos(blockposition).move(enumdirection);
            int j = this.getRenderedDepth(iblockaccess.getBlockState(blockposition_pooledblockposition));
            int k;

            if (j < 0) {
                if (!iblockaccess.getBlockState(blockposition_pooledblockposition).getMaterial().blocksMovement()) {
                    j = this.getRenderedDepth(iblockaccess.getBlockState(blockposition_pooledblockposition.down()));
                    if (j >= 0) {
                        k = j - (i - 8);
                        d0 += (double) (enumdirection.getFrontOffsetX() * k);
                        d1 += (double) (enumdirection.getFrontOffsetY() * k);
                        d2 += (double) (enumdirection.getFrontOffsetZ() * k);
                    }
                }
            } else if (j >= 0) {
                k = j - i;
                d0 += (double) (enumdirection.getFrontOffsetX() * k);
                d1 += (double) (enumdirection.getFrontOffsetY() * k);
                d2 += (double) (enumdirection.getFrontOffsetZ() * k);
            }
        }

        Vec3d vec3d = new Vec3d(d0, d1, d2);

        if (((Integer) iblockdata.getValue(BlockLiquid.LEVEL)).intValue() >= 8) {
            Iterator iterator1 = EnumFacing.Plane.HORIZONTAL.iterator();

            while (iterator1.hasNext()) {
                EnumFacing enumdirection1 = (EnumFacing) iterator1.next();

                blockposition_pooledblockposition.setPos(blockposition).move(enumdirection1);
                if (this.causesDownwardCurrent(iblockaccess, (BlockPos) blockposition_pooledblockposition, enumdirection1) || this.causesDownwardCurrent(iblockaccess, blockposition_pooledblockposition.up(), enumdirection1)) {
                    vec3d = vec3d.normalize().addVector(0.0D, -6.0D, 0.0D);
                    break;
                }
            }
        }

        blockposition_pooledblockposition.release();
        return vec3d.normalize();
    }

    public Vec3d modifyAcceleration(World world, BlockPos blockposition, Entity entity, Vec3d vec3d) {
        return vec3d.add(this.getFlow((IBlockAccess) world, blockposition, world.getBlockState(blockposition)));
    }

    public int tickRate(World world) {
        return this.blockMaterial == Material.WATER ? 5 : (this.blockMaterial == Material.LAVA ? (world.provider.isNether() ? 10 : 30) : 0);
    }

    public void onBlockAdded(World world, BlockPos blockposition, IBlockState iblockdata) {
        this.checkForMixing(world, blockposition, iblockdata);
    }

    public void neighborChanged(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        this.checkForMixing(world, blockposition, iblockdata);
    }

    public boolean checkForMixing(World world, BlockPos blockposition, IBlockState iblockdata) {
        if (this.blockMaterial == Material.LAVA) {
            boolean flag = false;
            EnumFacing[] aenumdirection = EnumFacing.values();
            int i = aenumdirection.length;

            for (int j = 0; j < i; ++j) {
                EnumFacing enumdirection = aenumdirection[j];

                if (enumdirection != EnumFacing.DOWN && world.getBlockState(blockposition.offset(enumdirection)).getMaterial() == Material.WATER) {
                    flag = true;
                    break;
                }
            }

            if (flag) {
                Integer integer = (Integer) iblockdata.getValue(BlockLiquid.LEVEL);

                if (integer.intValue() == 0) {
                    // CraftBukkit start
                    if (org.bukkit.craftbukkit.event.CraftEventFactory.handleBlockFormEvent(world, blockposition, Blocks.OBSIDIAN.getDefaultState(), null)) {
                        this.triggerMixEffects(world, blockposition);
                    }
                    // CraftBukkit end
                    return true;
                }

                if (integer.intValue() <= 4) {
                    // CraftBukkit start
                    if (org.bukkit.craftbukkit.event.CraftEventFactory.handleBlockFormEvent(world, blockposition, Blocks.COBBLESTONE.getDefaultState(), null)) {
                        this.triggerMixEffects(world, blockposition);
                    }
                    // CraftBukkit end
                    return true;
                }
            }
        }

        return false;
    }

    protected void triggerMixEffects(World world, BlockPos blockposition) {
        double d0 = (double) blockposition.getX();
        double d1 = (double) blockposition.getY();
        double d2 = (double) blockposition.getZ();

        world.playSound((EntityPlayer) null, blockposition, SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);

        for (int i = 0; i < 8; ++i) {
            world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d0 + Math.random(), d1 + 1.2D, d2 + Math.random(), 0.0D, 0.0D, 0.0D, new int[0]);
        }

    }

    public IBlockState getStateFromMeta(int i) {
        return this.getDefaultState().withProperty(BlockLiquid.LEVEL, Integer.valueOf(i));
    }

    public int getMetaFromState(IBlockState iblockdata) {
        return ((Integer) iblockdata.getValue(BlockLiquid.LEVEL)).intValue();
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockLiquid.LEVEL});
    }

    public static BlockDynamicLiquid getFlowingBlock(Material material) {
        if (material == Material.WATER) {
            return Blocks.FLOWING_WATER;
        } else if (material == Material.LAVA) {
            return Blocks.FLOWING_LAVA;
        } else {
            throw new IllegalArgumentException("Invalid material");
        }
    }

    public static BlockStaticLiquid getStaticBlock(Material material) {
        if (material == Material.WATER) {
            return Blocks.WATER;
        } else if (material == Material.LAVA) {
            return Blocks.LAVA;
        } else {
            throw new IllegalArgumentException("Invalid material");
        }
    }

    public static float getBlockLiquidHeight(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        int i = ((Integer) iblockdata.getValue(BlockLiquid.LEVEL)).intValue();

        return (i & 7) == 0 && iblockaccess.getBlockState(blockposition.up()).getMaterial() == Material.WATER ? 1.0F : 1.0F - getLiquidHeightPercent(i);
    }

    public static float getLiquidHeight(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return (float) blockposition.getY() + getBlockLiquidHeight(iblockdata, iblockaccess, blockposition);
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return BlockFaceShape.UNDEFINED;
    }
}
