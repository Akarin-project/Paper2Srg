package net.minecraft.block;

import java.util.Iterator;
import java.util.Random;
import javax.annotation.Nullable;


import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.bukkit.craftbukkit.event.CraftEventFactory;

public class BlockChorusFlower extends Block {

    public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 5);

    protected BlockChorusFlower() {
        super(Material.PLANTS, MapColor.PURPLE);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockChorusFlower.AGE, Integer.valueOf(0)));
        this.setCreativeTab(CreativeTabs.DECORATIONS);
        this.setTickRandomly(true);
    }

    public Item getItemDropped(IBlockState iblockdata, Random random, int i) {
        return Items.AIR;
    }

    public void updateTick(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        if (!this.canSurvive(world, blockposition)) {
            world.destroyBlock(blockposition, true);
        } else {
            BlockPos blockposition1 = blockposition.up();

            if (world.isAirBlock(blockposition1) && blockposition1.getY() < 256) {
                int i = ((Integer) iblockdata.getValue(BlockChorusFlower.AGE)).intValue();

                if (i < 5 && random.nextInt(1) == 0) {
                    boolean flag = false;
                    boolean flag1 = false;
                    IBlockState iblockdata1 = world.getBlockState(blockposition.down());
                    Block block = iblockdata1.getBlock();
                    int j;

                    if (block == Blocks.END_STONE) {
                        flag = true;
                    } else if (block == Blocks.CHORUS_PLANT) {
                        j = 1;

                        int k;

                        for (k = 0; k < 4; ++k) {
                            Block block1 = world.getBlockState(blockposition.down(j + 1)).getBlock();

                            if (block1 != Blocks.CHORUS_PLANT) {
                                if (block1 == Blocks.END_STONE) {
                                    flag1 = true;
                                }
                                break;
                            }

                            ++j;
                        }

                        k = 4;
                        if (flag1) {
                            ++k;
                        }

                        if (j < 2 || random.nextInt(k) >= j) {
                            flag = true;
                        }
                    } else if (iblockdata1.getMaterial() == Material.AIR) {
                        flag = true;
                    }

                    if (flag && areAllNeighborsEmpty(world, blockposition1, (EnumFacing) null) && world.isAirBlock(blockposition.up(2))) {
                        // world.setTypeAndData(blockposition, Blocks.CHORUS_PLANT.getBlockData(), 2);
                        // this.a(world, blockposition1, i);
                        // CraftBukkit start - add event
                        BlockPos target = blockposition1;
                        if (CraftEventFactory.handleBlockSpreadEvent(
                                world.getWorld().getBlockAt(target.getX(), target.getY(), target.getZ()),
                                world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ()),
                                this,
                                getMetaFromState(this.getDefaultState().withProperty(BlockChorusFlower.AGE, Integer.valueOf(i)))
                        )) {
                            world.setBlockState(blockposition, Blocks.CHORUS_PLANT.getDefaultState(), 2);
                            world.playEvent(1033, blockposition, 0);
                        }
                        // CraftBukkit end
                    } else if (i < 4) {
                        j = random.nextInt(4);
                        boolean flag2 = false;

                        if (flag1) {
                            ++j;
                        }

                        for (int l = 0; l < j; ++l) {
                            EnumFacing enumdirection = EnumFacing.Plane.HORIZONTAL.random(random);
                            BlockPos blockposition2 = blockposition.offset(enumdirection);

                            if (world.isAirBlock(blockposition2) && world.isAirBlock(blockposition2.down()) && areAllNeighborsEmpty(world, blockposition2, enumdirection.getOpposite())) {
                                // CraftBukkit start - add event
                                // this.a(world, blockposition2, i + 1);
                                BlockPos target = blockposition2;
                                if (CraftEventFactory.handleBlockSpreadEvent(
                                        world.getWorld().getBlockAt(target.getX(), target.getY(), target.getZ()),
                                        world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ()),
                                        this,
                                        getMetaFromState(this.getDefaultState().withProperty(BlockChorusFlower.AGE, Integer.valueOf(i + 1)))
                                )) {
                                    world.playEvent(1033, blockposition, 0);
                                    flag2 = true;
                                }
                                // CraftBukkit end
                            }
                        }

                        if (flag2) {
                            world.setBlockState(blockposition, Blocks.CHORUS_PLANT.getDefaultState(), 2);
                        } else {
                            // CraftBukkit - add event
                            if (CraftEventFactory.handleBlockGrowEvent(
                                    world,
                                    blockposition.getX(),
                                    blockposition.getY(),
                                    blockposition.getZ(),
                                    this,
                                    getMetaFromState(iblockdata.withProperty(BlockChorusFlower.AGE, Integer.valueOf(5)))
                            )) {
                                world.playEvent(1034, blockposition, 0);
                            }
                            // this.c(world, blockposition);
                            // CraftBukkit end
                        }
                    } else if (i == 4) {
                        // CraftBukkit - add event
                        if (CraftEventFactory.handleBlockGrowEvent(
                                world,
                                blockposition.getX(),
                                blockposition.getY(),
                                blockposition.getZ(),
                                this,
                                getMetaFromState(iblockdata.withProperty(BlockChorusFlower.AGE, Integer.valueOf(5)))
                        )) {
                            world.playEvent(1034, blockposition, 0);
                        }
                        // this.c(world, blockposition);
                        // CraftBukkit end
                    }

                }
            }
        }
    }

    private void placeGrownFlower(World world, BlockPos blockposition, int i) {
        world.setBlockState(blockposition, this.getDefaultState().withProperty(BlockChorusFlower.AGE, Integer.valueOf(i)), 2);
        world.playEvent(1033, blockposition, 0);
    }

    private void placeDeadFlower(World world, BlockPos blockposition) {
        world.setBlockState(blockposition, this.getDefaultState().withProperty(BlockChorusFlower.AGE, Integer.valueOf(5)), 2);
        world.playEvent(1034, blockposition, 0);
    }

    private static boolean areAllNeighborsEmpty(World world, BlockPos blockposition, EnumFacing enumdirection) {
        Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();

        EnumFacing enumdirection1;

        do {
            if (!iterator.hasNext()) {
                return true;
            }

            enumdirection1 = (EnumFacing) iterator.next();
        } while (enumdirection1 == enumdirection || world.isAirBlock(blockposition.offset(enumdirection1)));

        return false;
    }

    public boolean isFullCube(IBlockState iblockdata) {
        return false;
    }

    public boolean isOpaqueCube(IBlockState iblockdata) {
        return false;
    }

    public boolean canPlaceBlockAt(World world, BlockPos blockposition) {
        return super.canPlaceBlockAt(world, blockposition) && this.canSurvive(world, blockposition);
    }

    public void neighborChanged(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        if (!this.canSurvive(world, blockposition)) {
            world.scheduleUpdate(blockposition, (Block) this, 1);
        }

    }

    public boolean canSurvive(World world, BlockPos blockposition) {
        IBlockState iblockdata = world.getBlockState(blockposition.down());
        Block block = iblockdata.getBlock();

        if (block != Blocks.CHORUS_PLANT && block != Blocks.END_STONE) {
            if (iblockdata.getMaterial() == Material.AIR) {
                int i = 0;
                Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();

                while (iterator.hasNext()) {
                    EnumFacing enumdirection = (EnumFacing) iterator.next();
                    IBlockState iblockdata1 = world.getBlockState(blockposition.offset(enumdirection));
                    Block block1 = iblockdata1.getBlock();

                    if (block1 == Blocks.CHORUS_PLANT) {
                        ++i;
                    } else if (iblockdata1.getMaterial() != Material.AIR) {
                        return false;
                    }
                }

                return i == 1;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public void harvestBlock(World world, EntityPlayer entityhuman, BlockPos blockposition, IBlockState iblockdata, @Nullable TileEntity tileentity, ItemStack itemstack) {
        super.harvestBlock(world, entityhuman, blockposition, iblockdata, tileentity, itemstack);
        spawnAsEntity(world, blockposition, new ItemStack(Item.getItemFromBlock(this)));
    }

    protected ItemStack getSilkTouchDrop(IBlockState iblockdata) {
        return ItemStack.EMPTY;
    }

    public IBlockState getStateFromMeta(int i) {
        return this.getDefaultState().withProperty(BlockChorusFlower.AGE, Integer.valueOf(i));
    }

    public int getMetaFromState(IBlockState iblockdata) {
        return ((Integer) iblockdata.getValue(BlockChorusFlower.AGE)).intValue();
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockChorusFlower.AGE});
    }

    public static void generatePlant(World world, BlockPos blockposition, Random random, int i) {
        world.setBlockState(blockposition, Blocks.CHORUS_PLANT.getDefaultState(), 2);
        growTreeRecursive(world, blockposition, random, blockposition, i, 0);
    }

    private static void growTreeRecursive(World world, BlockPos blockposition, Random random, BlockPos blockposition1, int i, int j) {
        int k = random.nextInt(4) + 1;

        if (j == 0) {
            ++k;
        }

        for (int l = 0; l < k; ++l) {
            BlockPos blockposition2 = blockposition.up(l + 1);

            if (!areAllNeighborsEmpty(world, blockposition2, (EnumFacing) null)) {
                return;
            }

            world.setBlockState(blockposition2, Blocks.CHORUS_PLANT.getDefaultState(), 2);
        }

        boolean flag = false;

        if (j < 4) {
            int i1 = random.nextInt(4);

            if (j == 0) {
                ++i1;
            }

            for (int j1 = 0; j1 < i1; ++j1) {
                EnumFacing enumdirection = EnumFacing.Plane.HORIZONTAL.random(random);
                BlockPos blockposition3 = blockposition.up(k).offset(enumdirection);

                if (Math.abs(blockposition3.getX() - blockposition1.getX()) < i && Math.abs(blockposition3.getZ() - blockposition1.getZ()) < i && world.isAirBlock(blockposition3) && world.isAirBlock(blockposition3.down()) && areAllNeighborsEmpty(world, blockposition3, enumdirection.getOpposite())) {
                    flag = true;
                    world.setBlockState(blockposition3, Blocks.CHORUS_PLANT.getDefaultState(), 2);
                    growTreeRecursive(world, blockposition3, random, blockposition1, i, j + 1);
                }
            }
        }

        if (!flag) {
            world.setBlockState(blockposition.up(k), Blocks.CHORUS_FLOWER.getDefaultState().withProperty(BlockChorusFlower.AGE, Integer.valueOf(5)), 2);
        }

    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return BlockFaceShape.UNDEFINED;
    }
}
