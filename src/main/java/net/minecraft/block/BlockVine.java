package net.minecraft.block;

import java.util.Iterator;
import java.util.Random;
import javax.annotation.Nullable;


import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.bukkit.craftbukkit.event.CraftEventFactory;

public class BlockVine extends Block {

    public static final PropertyBool UP = PropertyBool.create("up");
    public static final PropertyBool NORTH = PropertyBool.create("north");
    public static final PropertyBool EAST = PropertyBool.create("east");
    public static final PropertyBool SOUTH = PropertyBool.create("south");
    public static final PropertyBool WEST = PropertyBool.create("west");
    public static final PropertyBool[] ALL_FACES = new PropertyBool[] { BlockVine.UP, BlockVine.NORTH, BlockVine.SOUTH, BlockVine.WEST, BlockVine.EAST};
    protected static final AxisAlignedBB UP_AABB = new AxisAlignedBB(0.0D, 0.9375D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0625D, 1.0D, 1.0D);
    protected static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0.9375D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.0625D);
    protected static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.9375D, 1.0D, 1.0D, 1.0D);

    public BlockVine() {
        super(Material.VINE);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockVine.UP, Boolean.valueOf(false)).withProperty(BlockVine.NORTH, Boolean.valueOf(false)).withProperty(BlockVine.EAST, Boolean.valueOf(false)).withProperty(BlockVine.SOUTH, Boolean.valueOf(false)).withProperty(BlockVine.WEST, Boolean.valueOf(false)));
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }

    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockVine.NULL_AABB;
    }

    public AxisAlignedBB getBoundingBox(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        iblockdata = iblockdata.getActualState(iblockaccess, blockposition);
        int i = 0;
        AxisAlignedBB axisalignedbb = BlockVine.FULL_BLOCK_AABB;

        if (((Boolean) iblockdata.getValue(BlockVine.UP)).booleanValue()) {
            axisalignedbb = BlockVine.UP_AABB;
            ++i;
        }

        if (((Boolean) iblockdata.getValue(BlockVine.NORTH)).booleanValue()) {
            axisalignedbb = BlockVine.NORTH_AABB;
            ++i;
        }

        if (((Boolean) iblockdata.getValue(BlockVine.EAST)).booleanValue()) {
            axisalignedbb = BlockVine.EAST_AABB;
            ++i;
        }

        if (((Boolean) iblockdata.getValue(BlockVine.SOUTH)).booleanValue()) {
            axisalignedbb = BlockVine.SOUTH_AABB;
            ++i;
        }

        if (((Boolean) iblockdata.getValue(BlockVine.WEST)).booleanValue()) {
            axisalignedbb = BlockVine.WEST_AABB;
            ++i;
        }

        return i == 1 ? axisalignedbb : BlockVine.FULL_BLOCK_AABB;
    }

    public IBlockState getActualState(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        BlockPos blockposition1 = blockposition.up();

        return iblockdata.withProperty(BlockVine.UP, Boolean.valueOf(iblockaccess.getBlockState(blockposition1).getBlockFaceShape(iblockaccess, blockposition1, EnumFacing.DOWN) == BlockFaceShape.SOLID));
    }

    public boolean isOpaqueCube(IBlockState iblockdata) {
        return false;
    }

    public boolean isFullCube(IBlockState iblockdata) {
        return false;
    }

    public boolean isReplaceable(IBlockAccess iblockaccess, BlockPos blockposition) {
        return true;
    }

    public boolean canPlaceBlockOnSide(World world, BlockPos blockposition, EnumFacing enumdirection) {
        return enumdirection != EnumFacing.DOWN && enumdirection != EnumFacing.UP && this.canAttachTo(world, blockposition, enumdirection);
    }

    public boolean canAttachTo(World world, BlockPos blockposition, EnumFacing enumdirection) {
        Block block = world.getBlockState(blockposition.up()).getBlock();

        return this.isAcceptableNeighbor(world, blockposition.offset(enumdirection.getOpposite()), enumdirection) && (block == Blocks.AIR || block == Blocks.VINE || this.isAcceptableNeighbor(world, blockposition.up(), EnumFacing.UP));
    }

    private boolean isAcceptableNeighbor(World world, BlockPos blockposition, EnumFacing enumdirection) {
        IBlockState iblockdata = world.getBlockState(blockposition);

        return iblockdata.getBlockFaceShape(world, blockposition, enumdirection) == BlockFaceShape.SOLID && !isExceptBlockForAttaching(iblockdata.getBlock());
    }

    protected static boolean isExceptBlockForAttaching(Block block) {
        return block instanceof BlockShulkerBox || block == Blocks.BEACON || block == Blocks.CAULDRON || block == Blocks.GLASS || block == Blocks.STAINED_GLASS || block == Blocks.PISTON || block == Blocks.STICKY_PISTON || block == Blocks.PISTON_HEAD || block == Blocks.TRAPDOOR;
    }

    private boolean recheckGrownSides(World world, BlockPos blockposition, IBlockState iblockdata) {
        IBlockState iblockdata1 = iblockdata;
        Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();

        while (iterator.hasNext()) {
            EnumFacing enumdirection = (EnumFacing) iterator.next();
            PropertyBool blockstateboolean = getPropertyFor(enumdirection);

            if (((Boolean) iblockdata.getValue(blockstateboolean)).booleanValue() && !this.canAttachTo(world, blockposition, enumdirection.getOpposite())) {
                IBlockState iblockdata2 = world.getBlockState(blockposition.up());

                if (iblockdata2.getBlock() != this || !((Boolean) iblockdata2.getValue(blockstateboolean)).booleanValue()) {
                    iblockdata = iblockdata.withProperty(blockstateboolean, Boolean.valueOf(false));
                }
            }
        }

        if (getNumGrownFaces(iblockdata) == 0) {
            return false;
        } else {
            if (iblockdata1 != iblockdata) {
                world.setBlockState(blockposition, iblockdata, 2);
            }

            return true;
        }
    }

    public void neighborChanged(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        if (!world.isRemote && !this.recheckGrownSides(world, blockposition, iblockdata)) {
            this.dropBlockAsItem(world, blockposition, iblockdata, 0);
            world.setBlockToAir(blockposition);
        }

    }

    public void updateTick(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        if (!world.isRemote) {
            if (world.rand.nextInt(Math.max(1, (int) (100.0F / world.spigotConfig.vineModifier) * 4)) == 0) { // Spigot
                boolean flag = true;
                int i = 5;
                boolean flag1 = false;

                label178:
                for (int j = -4; j <= 4; ++j) {
                    for (int k = -4; k <= 4; ++k) {
                        for (int l = -1; l <= 1; ++l) {
                            if (world.getBlockState(blockposition.add(j, l, k)).getBlock() == this) {
                                --i;
                                if (i <= 0) {
                                    flag1 = true;
                                    break label178;
                                }
                            }
                        }
                    }
                }

                EnumFacing enumdirection = EnumFacing.random(random);
                BlockPos blockposition1 = blockposition.up();

                if (enumdirection == EnumFacing.UP && blockposition.getY() < 255 && world.isAirBlock(blockposition1)) {
                    IBlockState iblockdata1 = iblockdata;
                    Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();

                    while (iterator.hasNext()) {
                        EnumFacing enumdirection1 = (EnumFacing) iterator.next();

                        if (random.nextBoolean() && this.canAttachTo(world, blockposition1, enumdirection1.getOpposite())) {
                            iblockdata1 = iblockdata1.withProperty(getPropertyFor(enumdirection1), Boolean.valueOf(true));
                        } else {
                            iblockdata1 = iblockdata1.withProperty(getPropertyFor(enumdirection1), Boolean.valueOf(false));
                        }
                    }

                    if (((Boolean) iblockdata1.getValue(BlockVine.NORTH)).booleanValue() || ((Boolean) iblockdata1.getValue(BlockVine.EAST)).booleanValue() || ((Boolean) iblockdata1.getValue(BlockVine.SOUTH)).booleanValue() || ((Boolean) iblockdata1.getValue(BlockVine.WEST)).booleanValue()) {
                        // CraftBukkit start - Call BlockSpreadEvent
                        // world.setTypeAndData(blockposition1, iblockdata1, 2);
                        BlockPos target = blockposition1;
                        org.bukkit.block.Block source = world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());
                        org.bukkit.block.Block block = world.getWorld().getBlockAt(target.getX(), target.getY(), target.getZ());
                        CraftEventFactory.handleBlockSpreadEvent(block, source, this, getMetaFromState(iblockdata1));
                        // CraftBukkit end
                    }

                } else {
                    IBlockState iblockdata2;
                    Block block;
                    BlockPos blockposition2;

                    if (enumdirection.getAxis().isHorizontal() && !((Boolean) iblockdata.getValue(getPropertyFor(enumdirection))).booleanValue()) {
                        if (!flag1) {
                            blockposition2 = blockposition.offset(enumdirection);
                            iblockdata2 = world.getBlockState(blockposition2);
                            block = iblockdata2.getBlock();
                            if (block.blockMaterial == Material.AIR) {
                                EnumFacing enumdirection2 = enumdirection.rotateY();
                                EnumFacing enumdirection3 = enumdirection.rotateYCCW();
                                boolean flag2 = ((Boolean) iblockdata.getValue(getPropertyFor(enumdirection2))).booleanValue();
                                boolean flag3 = ((Boolean) iblockdata.getValue(getPropertyFor(enumdirection3))).booleanValue();
                                BlockPos blockposition3 = blockposition2.offset(enumdirection2);
                                BlockPos blockposition4 = blockposition2.offset(enumdirection3);

                                // CraftBukkit start - Call BlockSpreadEvent
                                org.bukkit.block.Block source = world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());
                                org.bukkit.block.Block bukkitBlock = world.getWorld().getBlockAt(blockposition2.getX(), blockposition2.getY(), blockposition2.getZ());

                                if (flag2 && this.canAttachTo(world, blockposition3.offset(enumdirection2), enumdirection2)) {
                                    // world.setTypeAndData(blockposition2, this.getBlockData().set(getDirection(enumdirection2), Boolean.valueOf(true)), 2);
                                    CraftEventFactory.handleBlockSpreadEvent(bukkitBlock, source, this, getMetaFromState(this.getDefaultState().withProperty(getPropertyFor(enumdirection2), Boolean.valueOf(true))));
                                } else if (flag3 && this.canAttachTo(world, blockposition4.offset(enumdirection3), enumdirection3)) {
                                    // world.setTypeAndData(blockposition2, this.getBlockData().set(getDirection(enumdirection3), Boolean.valueOf(true)), 2);
                                    CraftEventFactory.handleBlockSpreadEvent(bukkitBlock, source, this, getMetaFromState(this.getDefaultState().withProperty(getPropertyFor(enumdirection3), Boolean.valueOf(true))));
                                } else if (flag2 && world.isAirBlock(blockposition3) && this.canAttachTo(world, blockposition3, enumdirection)) {
                                    // world.setTypeAndData(blockposition3, this.getBlockData().set(getDirection(enumdirection.opposite()), Boolean.valueOf(true)), 2);
                                    bukkitBlock = world.getWorld().getBlockAt(blockposition3.getX(), blockposition3.getY(), blockposition3.getZ());
                                    CraftEventFactory.handleBlockSpreadEvent(bukkitBlock, source, this, getMetaFromState(this.getDefaultState().withProperty(getPropertyFor(enumdirection.getOpposite()), Boolean.valueOf(true))));
                                } else if (flag3 && world.isAirBlock(blockposition4) && this.canAttachTo(world, blockposition4, enumdirection)) {
                                    // world.setTypeAndData(blockposition4, this.getBlockData().set(getDirection(enumdirection.opposite()), Boolean.valueOf(true)), 2);
                                    bukkitBlock = world.getWorld().getBlockAt(blockposition4.getX(), blockposition4.getY(), blockposition4.getZ());
                                    CraftEventFactory.handleBlockSpreadEvent(bukkitBlock, source, this, getMetaFromState(this.getDefaultState().withProperty(getPropertyFor(enumdirection.getOpposite()), Boolean.valueOf(true))));
                                }
                                // CraftBukkit end
                            } else if (iblockdata2.getBlockFaceShape(world, blockposition2, enumdirection) == BlockFaceShape.SOLID) {
                                world.setBlockState(blockposition, iblockdata.withProperty(getPropertyFor(enumdirection), Boolean.valueOf(true)), 2);
                            }

                        }
                    } else {
                        if (blockposition.getY() > 1) {
                            blockposition2 = blockposition.down();
                            iblockdata2 = world.getBlockState(blockposition2);
                            block = iblockdata2.getBlock();
                            IBlockState iblockdata3;
                            Iterator iterator1;
                            EnumFacing enumdirection4;

                            if (block.blockMaterial == Material.AIR) {
                                iblockdata3 = iblockdata;
                                iterator1 = EnumFacing.Plane.HORIZONTAL.iterator();

                                while (iterator1.hasNext()) {
                                    enumdirection4 = (EnumFacing) iterator1.next();
                                    if (random.nextBoolean()) {
                                        iblockdata3 = iblockdata3.withProperty(getPropertyFor(enumdirection4), Boolean.valueOf(false));
                                    }
                                }

                                if (((Boolean) iblockdata3.getValue(BlockVine.NORTH)).booleanValue() || ((Boolean) iblockdata3.getValue(BlockVine.EAST)).booleanValue() || ((Boolean) iblockdata3.getValue(BlockVine.SOUTH)).booleanValue() || ((Boolean) iblockdata3.getValue(BlockVine.WEST)).booleanValue()) {
                                    // CraftBukkit start - Call BlockSpreadEvent
                                    // world.setTypeAndData(blockposition2, iblockdata3, 2);
                                    org.bukkit.block.Block source = world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());
                                    org.bukkit.block.Block bukkitBlock = world.getWorld().getBlockAt(blockposition2.getX(), blockposition2.getY(), blockposition2.getZ());
                                    CraftEventFactory.handleBlockSpreadEvent(bukkitBlock, source, this, getMetaFromState(iblockdata3));
                                    // CraftBukkit end
                                }
                            } else if (block == this) {
                                iblockdata3 = iblockdata2;
                                iterator1 = EnumFacing.Plane.HORIZONTAL.iterator();

                                while (iterator1.hasNext()) {
                                    enumdirection4 = (EnumFacing) iterator1.next();
                                    PropertyBool blockstateboolean = getPropertyFor(enumdirection4);

                                    if (random.nextBoolean() && ((Boolean) iblockdata.getValue(blockstateboolean)).booleanValue()) {
                                        iblockdata3 = iblockdata3.withProperty(blockstateboolean, Boolean.valueOf(true));
                                    }
                                }

                                if (((Boolean) iblockdata3.getValue(BlockVine.NORTH)).booleanValue() || ((Boolean) iblockdata3.getValue(BlockVine.EAST)).booleanValue() || ((Boolean) iblockdata3.getValue(BlockVine.SOUTH)).booleanValue() || ((Boolean) iblockdata3.getValue(BlockVine.WEST)).booleanValue()) {
                                    world.setBlockState(blockposition2, iblockdata3, 2);
                                }
                            }
                        }

                    }
                }
            }
        }
    }

    public IBlockState getStateForPlacement(World world, BlockPos blockposition, EnumFacing enumdirection, float f, float f1, float f2, int i, EntityLivingBase entityliving) {
        IBlockState iblockdata = this.getDefaultState().withProperty(BlockVine.UP, Boolean.valueOf(false)).withProperty(BlockVine.NORTH, Boolean.valueOf(false)).withProperty(BlockVine.EAST, Boolean.valueOf(false)).withProperty(BlockVine.SOUTH, Boolean.valueOf(false)).withProperty(BlockVine.WEST, Boolean.valueOf(false));

        return enumdirection.getAxis().isHorizontal() ? iblockdata.withProperty(getPropertyFor(enumdirection.getOpposite()), Boolean.valueOf(true)) : iblockdata;
    }

    public Item getItemDropped(IBlockState iblockdata, Random random, int i) {
        return Items.AIR;
    }

    public int quantityDropped(Random random) {
        return 0;
    }

    public void harvestBlock(World world, EntityPlayer entityhuman, BlockPos blockposition, IBlockState iblockdata, @Nullable TileEntity tileentity, ItemStack itemstack) {
        if (!world.isRemote && itemstack.getItem() == Items.SHEARS) {
            entityhuman.addStat(StatList.getBlockStats((Block) this));
            spawnAsEntity(world, blockposition, new ItemStack(Blocks.VINE, 1, 0));
        } else {
            super.harvestBlock(world, entityhuman, blockposition, iblockdata, tileentity, itemstack);
        }

    }

    public IBlockState getStateFromMeta(int i) {
        return this.getDefaultState().withProperty(BlockVine.SOUTH, Boolean.valueOf((i & 1) > 0)).withProperty(BlockVine.WEST, Boolean.valueOf((i & 2) > 0)).withProperty(BlockVine.NORTH, Boolean.valueOf((i & 4) > 0)).withProperty(BlockVine.EAST, Boolean.valueOf((i & 8) > 0));
    }

    public int getMetaFromState(IBlockState iblockdata) {
        int i = 0;

        if (((Boolean) iblockdata.getValue(BlockVine.SOUTH)).booleanValue()) {
            i |= 1;
        }

        if (((Boolean) iblockdata.getValue(BlockVine.WEST)).booleanValue()) {
            i |= 2;
        }

        if (((Boolean) iblockdata.getValue(BlockVine.NORTH)).booleanValue()) {
            i |= 4;
        }

        if (((Boolean) iblockdata.getValue(BlockVine.EAST)).booleanValue()) {
            i |= 8;
        }

        return i;
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockVine.UP, BlockVine.NORTH, BlockVine.EAST, BlockVine.SOUTH, BlockVine.WEST});
    }

    public IBlockState withRotation(IBlockState iblockdata, Rotation enumblockrotation) {
        switch (enumblockrotation) {
        case CLOCKWISE_180:
            return iblockdata.withProperty(BlockVine.NORTH, iblockdata.getValue(BlockVine.SOUTH)).withProperty(BlockVine.EAST, iblockdata.getValue(BlockVine.WEST)).withProperty(BlockVine.SOUTH, iblockdata.getValue(BlockVine.NORTH)).withProperty(BlockVine.WEST, iblockdata.getValue(BlockVine.EAST));

        case COUNTERCLOCKWISE_90:
            return iblockdata.withProperty(BlockVine.NORTH, iblockdata.getValue(BlockVine.EAST)).withProperty(BlockVine.EAST, iblockdata.getValue(BlockVine.SOUTH)).withProperty(BlockVine.SOUTH, iblockdata.getValue(BlockVine.WEST)).withProperty(BlockVine.WEST, iblockdata.getValue(BlockVine.NORTH));

        case CLOCKWISE_90:
            return iblockdata.withProperty(BlockVine.NORTH, iblockdata.getValue(BlockVine.WEST)).withProperty(BlockVine.EAST, iblockdata.getValue(BlockVine.NORTH)).withProperty(BlockVine.SOUTH, iblockdata.getValue(BlockVine.EAST)).withProperty(BlockVine.WEST, iblockdata.getValue(BlockVine.SOUTH));

        default:
            return iblockdata;
        }
    }

    public IBlockState withMirror(IBlockState iblockdata, Mirror enumblockmirror) {
        switch (enumblockmirror) {
        case LEFT_RIGHT:
            return iblockdata.withProperty(BlockVine.NORTH, iblockdata.getValue(BlockVine.SOUTH)).withProperty(BlockVine.SOUTH, iblockdata.getValue(BlockVine.NORTH));

        case FRONT_BACK:
            return iblockdata.withProperty(BlockVine.EAST, iblockdata.getValue(BlockVine.WEST)).withProperty(BlockVine.WEST, iblockdata.getValue(BlockVine.EAST));

        default:
            return super.withMirror(iblockdata, enumblockmirror);
        }
    }

    public static PropertyBool getPropertyFor(EnumFacing enumdirection) {
        switch (enumdirection) {
        case UP:
            return BlockVine.UP;

        case NORTH:
            return BlockVine.NORTH;

        case SOUTH:
            return BlockVine.SOUTH;

        case WEST:
            return BlockVine.WEST;

        case EAST:
            return BlockVine.EAST;

        default:
            throw new IllegalArgumentException(enumdirection + " is an invalid choice");
        }
    }

    public static int getNumGrownFaces(IBlockState iblockdata) {
        int i = 0;
        PropertyBool[] ablockstateboolean = BlockVine.ALL_FACES;
        int j = ablockstateboolean.length;

        for (int k = 0; k < j; ++k) {
            PropertyBool blockstateboolean = ablockstateboolean[k];

            if (((Boolean) iblockdata.getValue(blockstateboolean)).booleanValue()) {
                ++i;
            }
        }

        return i;
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return BlockFaceShape.UNDEFINED;
    }
}
