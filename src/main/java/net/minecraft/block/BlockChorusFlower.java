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

    public static final PropertyInteger field_185607_a = PropertyInteger.func_177719_a("age", 0, 5);

    protected BlockChorusFlower() {
        super(Material.field_151585_k, MapColor.field_151678_z);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockChorusFlower.field_185607_a, Integer.valueOf(0)));
        this.func_149647_a(CreativeTabs.field_78031_c);
        this.func_149675_a(true);
    }

    public Item func_180660_a(IBlockState iblockdata, Random random, int i) {
        return Items.field_190931_a;
    }

    public void func_180650_b(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        if (!this.func_185606_b(world, blockposition)) {
            world.func_175655_b(blockposition, true);
        } else {
            BlockPos blockposition1 = blockposition.func_177984_a();

            if (world.func_175623_d(blockposition1) && blockposition1.func_177956_o() < 256) {
                int i = ((Integer) iblockdata.func_177229_b(BlockChorusFlower.field_185607_a)).intValue();

                if (i < 5 && random.nextInt(1) == 0) {
                    boolean flag = false;
                    boolean flag1 = false;
                    IBlockState iblockdata1 = world.func_180495_p(blockposition.func_177977_b());
                    Block block = iblockdata1.func_177230_c();
                    int j;

                    if (block == Blocks.field_150377_bs) {
                        flag = true;
                    } else if (block == Blocks.field_185765_cR) {
                        j = 1;

                        int k;

                        for (k = 0; k < 4; ++k) {
                            Block block1 = world.func_180495_p(blockposition.func_177979_c(j + 1)).func_177230_c();

                            if (block1 != Blocks.field_185765_cR) {
                                if (block1 == Blocks.field_150377_bs) {
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
                    } else if (iblockdata1.func_185904_a() == Material.field_151579_a) {
                        flag = true;
                    }

                    if (flag && func_185604_a(world, blockposition1, (EnumFacing) null) && world.func_175623_d(blockposition.func_177981_b(2))) {
                        // world.setTypeAndData(blockposition, Blocks.CHORUS_PLANT.getBlockData(), 2);
                        // this.a(world, blockposition1, i);
                        // CraftBukkit start - add event
                        BlockPos target = blockposition1;
                        if (CraftEventFactory.handleBlockSpreadEvent(
                                world.getWorld().getBlockAt(target.func_177958_n(), target.func_177956_o(), target.func_177952_p()),
                                world.getWorld().getBlockAt(blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p()),
                                this,
                                func_176201_c(this.func_176223_P().func_177226_a(BlockChorusFlower.field_185607_a, Integer.valueOf(i)))
                        )) {
                            world.func_180501_a(blockposition, Blocks.field_185765_cR.func_176223_P(), 2);
                            world.func_175718_b(1033, blockposition, 0);
                        }
                        // CraftBukkit end
                    } else if (i < 4) {
                        j = random.nextInt(4);
                        boolean flag2 = false;

                        if (flag1) {
                            ++j;
                        }

                        for (int l = 0; l < j; ++l) {
                            EnumFacing enumdirection = EnumFacing.Plane.HORIZONTAL.func_179518_a(random);
                            BlockPos blockposition2 = blockposition.func_177972_a(enumdirection);

                            if (world.func_175623_d(blockposition2) && world.func_175623_d(blockposition2.func_177977_b()) && func_185604_a(world, blockposition2, enumdirection.func_176734_d())) {
                                // CraftBukkit start - add event
                                // this.a(world, blockposition2, i + 1);
                                BlockPos target = blockposition2;
                                if (CraftEventFactory.handleBlockSpreadEvent(
                                        world.getWorld().getBlockAt(target.func_177958_n(), target.func_177956_o(), target.func_177952_p()),
                                        world.getWorld().getBlockAt(blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p()),
                                        this,
                                        func_176201_c(this.func_176223_P().func_177226_a(BlockChorusFlower.field_185607_a, Integer.valueOf(i + 1)))
                                )) {
                                    world.func_175718_b(1033, blockposition, 0);
                                    flag2 = true;
                                }
                                // CraftBukkit end
                            }
                        }

                        if (flag2) {
                            world.func_180501_a(blockposition, Blocks.field_185765_cR.func_176223_P(), 2);
                        } else {
                            // CraftBukkit - add event
                            if (CraftEventFactory.handleBlockGrowEvent(
                                    world,
                                    blockposition.func_177958_n(),
                                    blockposition.func_177956_o(),
                                    blockposition.func_177952_p(),
                                    this,
                                    func_176201_c(iblockdata.func_177226_a(BlockChorusFlower.field_185607_a, Integer.valueOf(5)))
                            )) {
                                world.func_175718_b(1034, blockposition, 0);
                            }
                            // this.c(world, blockposition);
                            // CraftBukkit end
                        }
                    } else if (i == 4) {
                        // CraftBukkit - add event
                        if (CraftEventFactory.handleBlockGrowEvent(
                                world,
                                blockposition.func_177958_n(),
                                blockposition.func_177956_o(),
                                blockposition.func_177952_p(),
                                this,
                                func_176201_c(iblockdata.func_177226_a(BlockChorusFlower.field_185607_a, Integer.valueOf(5)))
                        )) {
                            world.func_175718_b(1034, blockposition, 0);
                        }
                        // this.c(world, blockposition);
                        // CraftBukkit end
                    }

                }
            }
        }
    }

    private void func_185602_a(World world, BlockPos blockposition, int i) {
        world.func_180501_a(blockposition, this.func_176223_P().func_177226_a(BlockChorusFlower.field_185607_a, Integer.valueOf(i)), 2);
        world.func_175718_b(1033, blockposition, 0);
    }

    private void func_185605_c(World world, BlockPos blockposition) {
        world.func_180501_a(blockposition, this.func_176223_P().func_177226_a(BlockChorusFlower.field_185607_a, Integer.valueOf(5)), 2);
        world.func_175718_b(1034, blockposition, 0);
    }

    private static boolean func_185604_a(World world, BlockPos blockposition, EnumFacing enumdirection) {
        Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();

        EnumFacing enumdirection1;

        do {
            if (!iterator.hasNext()) {
                return true;
            }

            enumdirection1 = (EnumFacing) iterator.next();
        } while (enumdirection1 == enumdirection || world.func_175623_d(blockposition.func_177972_a(enumdirection1)));

        return false;
    }

    public boolean func_149686_d(IBlockState iblockdata) {
        return false;
    }

    public boolean func_149662_c(IBlockState iblockdata) {
        return false;
    }

    public boolean func_176196_c(World world, BlockPos blockposition) {
        return super.func_176196_c(world, blockposition) && this.func_185606_b(world, blockposition);
    }

    public void func_189540_a(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        if (!this.func_185606_b(world, blockposition)) {
            world.func_175684_a(blockposition, (Block) this, 1);
        }

    }

    public boolean func_185606_b(World world, BlockPos blockposition) {
        IBlockState iblockdata = world.func_180495_p(blockposition.func_177977_b());
        Block block = iblockdata.func_177230_c();

        if (block != Blocks.field_185765_cR && block != Blocks.field_150377_bs) {
            if (iblockdata.func_185904_a() == Material.field_151579_a) {
                int i = 0;
                Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();

                while (iterator.hasNext()) {
                    EnumFacing enumdirection = (EnumFacing) iterator.next();
                    IBlockState iblockdata1 = world.func_180495_p(blockposition.func_177972_a(enumdirection));
                    Block block1 = iblockdata1.func_177230_c();

                    if (block1 == Blocks.field_185765_cR) {
                        ++i;
                    } else if (iblockdata1.func_185904_a() != Material.field_151579_a) {
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

    public void func_180657_a(World world, EntityPlayer entityhuman, BlockPos blockposition, IBlockState iblockdata, @Nullable TileEntity tileentity, ItemStack itemstack) {
        super.func_180657_a(world, entityhuman, blockposition, iblockdata, tileentity, itemstack);
        func_180635_a(world, blockposition, new ItemStack(Item.func_150898_a(this)));
    }

    protected ItemStack func_180643_i(IBlockState iblockdata) {
        return ItemStack.field_190927_a;
    }

    public IBlockState func_176203_a(int i) {
        return this.func_176223_P().func_177226_a(BlockChorusFlower.field_185607_a, Integer.valueOf(i));
    }

    public int func_176201_c(IBlockState iblockdata) {
        return ((Integer) iblockdata.func_177229_b(BlockChorusFlower.field_185607_a)).intValue();
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockChorusFlower.field_185607_a});
    }

    public static void func_185603_a(World world, BlockPos blockposition, Random random, int i) {
        world.func_180501_a(blockposition, Blocks.field_185765_cR.func_176223_P(), 2);
        func_185601_a(world, blockposition, random, blockposition, i, 0);
    }

    private static void func_185601_a(World world, BlockPos blockposition, Random random, BlockPos blockposition1, int i, int j) {
        int k = random.nextInt(4) + 1;

        if (j == 0) {
            ++k;
        }

        for (int l = 0; l < k; ++l) {
            BlockPos blockposition2 = blockposition.func_177981_b(l + 1);

            if (!func_185604_a(world, blockposition2, (EnumFacing) null)) {
                return;
            }

            world.func_180501_a(blockposition2, Blocks.field_185765_cR.func_176223_P(), 2);
        }

        boolean flag = false;

        if (j < 4) {
            int i1 = random.nextInt(4);

            if (j == 0) {
                ++i1;
            }

            for (int j1 = 0; j1 < i1; ++j1) {
                EnumFacing enumdirection = EnumFacing.Plane.HORIZONTAL.func_179518_a(random);
                BlockPos blockposition3 = blockposition.func_177981_b(k).func_177972_a(enumdirection);

                if (Math.abs(blockposition3.func_177958_n() - blockposition1.func_177958_n()) < i && Math.abs(blockposition3.func_177952_p() - blockposition1.func_177952_p()) < i && world.func_175623_d(blockposition3) && world.func_175623_d(blockposition3.func_177977_b()) && func_185604_a(world, blockposition3, enumdirection.func_176734_d())) {
                    flag = true;
                    world.func_180501_a(blockposition3, Blocks.field_185765_cR.func_176223_P(), 2);
                    func_185601_a(world, blockposition3, random, blockposition1, i, j + 1);
                }
            }
        }

        if (!flag) {
            world.func_180501_a(blockposition.func_177981_b(k), Blocks.field_185766_cS.func_176223_P().func_177226_a(BlockChorusFlower.field_185607_a, Integer.valueOf(5)), 2);
        }

    }

    public BlockFaceShape func_193383_a(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return BlockFaceShape.UNDEFINED;
    }
}
