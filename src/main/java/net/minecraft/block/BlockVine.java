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

    public static final PropertyBool field_176277_a = PropertyBool.func_177716_a("up");
    public static final PropertyBool field_176273_b = PropertyBool.func_177716_a("north");
    public static final PropertyBool field_176278_M = PropertyBool.func_177716_a("east");
    public static final PropertyBool field_176279_N = PropertyBool.func_177716_a("south");
    public static final PropertyBool field_176280_O = PropertyBool.func_177716_a("west");
    public static final PropertyBool[] field_176274_P = new PropertyBool[] { BlockVine.field_176277_a, BlockVine.field_176273_b, BlockVine.field_176279_N, BlockVine.field_176280_O, BlockVine.field_176278_M};
    protected static final AxisAlignedBB field_185757_g = new AxisAlignedBB(0.0D, 0.9375D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB field_185753_B = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0625D, 1.0D, 1.0D);
    protected static final AxisAlignedBB field_185754_C = new AxisAlignedBB(0.9375D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB field_185755_D = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.0625D);
    protected static final AxisAlignedBB field_185756_E = new AxisAlignedBB(0.0D, 0.0D, 0.9375D, 1.0D, 1.0D, 1.0D);

    public BlockVine() {
        super(Material.field_151582_l);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockVine.field_176277_a, Boolean.valueOf(false)).func_177226_a(BlockVine.field_176273_b, Boolean.valueOf(false)).func_177226_a(BlockVine.field_176278_M, Boolean.valueOf(false)).func_177226_a(BlockVine.field_176279_N, Boolean.valueOf(false)).func_177226_a(BlockVine.field_176280_O, Boolean.valueOf(false)));
        this.func_149675_a(true);
        this.func_149647_a(CreativeTabs.field_78031_c);
    }

    @Nullable
    public AxisAlignedBB func_180646_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockVine.field_185506_k;
    }

    public AxisAlignedBB func_185496_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        iblockdata = iblockdata.func_185899_b(iblockaccess, blockposition);
        int i = 0;
        AxisAlignedBB axisalignedbb = BlockVine.field_185505_j;

        if (((Boolean) iblockdata.func_177229_b(BlockVine.field_176277_a)).booleanValue()) {
            axisalignedbb = BlockVine.field_185757_g;
            ++i;
        }

        if (((Boolean) iblockdata.func_177229_b(BlockVine.field_176273_b)).booleanValue()) {
            axisalignedbb = BlockVine.field_185755_D;
            ++i;
        }

        if (((Boolean) iblockdata.func_177229_b(BlockVine.field_176278_M)).booleanValue()) {
            axisalignedbb = BlockVine.field_185754_C;
            ++i;
        }

        if (((Boolean) iblockdata.func_177229_b(BlockVine.field_176279_N)).booleanValue()) {
            axisalignedbb = BlockVine.field_185756_E;
            ++i;
        }

        if (((Boolean) iblockdata.func_177229_b(BlockVine.field_176280_O)).booleanValue()) {
            axisalignedbb = BlockVine.field_185753_B;
            ++i;
        }

        return i == 1 ? axisalignedbb : BlockVine.field_185505_j;
    }

    public IBlockState func_176221_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        BlockPos blockposition1 = blockposition.func_177984_a();

        return iblockdata.func_177226_a(BlockVine.field_176277_a, Boolean.valueOf(iblockaccess.func_180495_p(blockposition1).func_193401_d(iblockaccess, blockposition1, EnumFacing.DOWN) == BlockFaceShape.SOLID));
    }

    public boolean func_149662_c(IBlockState iblockdata) {
        return false;
    }

    public boolean func_149686_d(IBlockState iblockdata) {
        return false;
    }

    public boolean func_176200_f(IBlockAccess iblockaccess, BlockPos blockposition) {
        return true;
    }

    public boolean func_176198_a(World world, BlockPos blockposition, EnumFacing enumdirection) {
        return enumdirection != EnumFacing.DOWN && enumdirection != EnumFacing.UP && this.func_193395_a(world, blockposition, enumdirection);
    }

    public boolean func_193395_a(World world, BlockPos blockposition, EnumFacing enumdirection) {
        Block block = world.func_180495_p(blockposition.func_177984_a()).func_177230_c();

        return this.func_193396_c(world, blockposition.func_177972_a(enumdirection.func_176734_d()), enumdirection) && (block == Blocks.field_150350_a || block == Blocks.field_150395_bd || this.func_193396_c(world, blockposition.func_177984_a(), EnumFacing.UP));
    }

    private boolean func_193396_c(World world, BlockPos blockposition, EnumFacing enumdirection) {
        IBlockState iblockdata = world.func_180495_p(blockposition);

        return iblockdata.func_193401_d(world, blockposition, enumdirection) == BlockFaceShape.SOLID && !func_193397_e(iblockdata.func_177230_c());
    }

    protected static boolean func_193397_e(Block block) {
        return block instanceof BlockShulkerBox || block == Blocks.field_150461_bJ || block == Blocks.field_150383_bp || block == Blocks.field_150359_w || block == Blocks.field_150399_cn || block == Blocks.field_150331_J || block == Blocks.field_150320_F || block == Blocks.field_150332_K || block == Blocks.field_150415_aT;
    }

    private boolean func_176269_e(World world, BlockPos blockposition, IBlockState iblockdata) {
        IBlockState iblockdata1 = iblockdata;
        Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();

        while (iterator.hasNext()) {
            EnumFacing enumdirection = (EnumFacing) iterator.next();
            PropertyBool blockstateboolean = func_176267_a(enumdirection);

            if (((Boolean) iblockdata.func_177229_b(blockstateboolean)).booleanValue() && !this.func_193395_a(world, blockposition, enumdirection.func_176734_d())) {
                IBlockState iblockdata2 = world.func_180495_p(blockposition.func_177984_a());

                if (iblockdata2.func_177230_c() != this || !((Boolean) iblockdata2.func_177229_b(blockstateboolean)).booleanValue()) {
                    iblockdata = iblockdata.func_177226_a(blockstateboolean, Boolean.valueOf(false));
                }
            }
        }

        if (func_176268_d(iblockdata) == 0) {
            return false;
        } else {
            if (iblockdata1 != iblockdata) {
                world.func_180501_a(blockposition, iblockdata, 2);
            }

            return true;
        }
    }

    public void func_189540_a(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        if (!world.field_72995_K && !this.func_176269_e(world, blockposition, iblockdata)) {
            this.func_176226_b(world, blockposition, iblockdata, 0);
            world.func_175698_g(blockposition);
        }

    }

    public void func_180650_b(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        if (!world.field_72995_K) {
            if (world.field_73012_v.nextInt(Math.max(1, (int) (100.0F / world.spigotConfig.vineModifier) * 4)) == 0) { // Spigot
                boolean flag = true;
                int i = 5;
                boolean flag1 = false;

                label178:
                for (int j = -4; j <= 4; ++j) {
                    for (int k = -4; k <= 4; ++k) {
                        for (int l = -1; l <= 1; ++l) {
                            if (world.func_180495_p(blockposition.func_177982_a(j, l, k)).func_177230_c() == this) {
                                --i;
                                if (i <= 0) {
                                    flag1 = true;
                                    break label178;
                                }
                            }
                        }
                    }
                }

                EnumFacing enumdirection = EnumFacing.func_176741_a(random);
                BlockPos blockposition1 = blockposition.func_177984_a();

                if (enumdirection == EnumFacing.UP && blockposition.func_177956_o() < 255 && world.func_175623_d(blockposition1)) {
                    IBlockState iblockdata1 = iblockdata;
                    Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();

                    while (iterator.hasNext()) {
                        EnumFacing enumdirection1 = (EnumFacing) iterator.next();

                        if (random.nextBoolean() && this.func_193395_a(world, blockposition1, enumdirection1.func_176734_d())) {
                            iblockdata1 = iblockdata1.func_177226_a(func_176267_a(enumdirection1), Boolean.valueOf(true));
                        } else {
                            iblockdata1 = iblockdata1.func_177226_a(func_176267_a(enumdirection1), Boolean.valueOf(false));
                        }
                    }

                    if (((Boolean) iblockdata1.func_177229_b(BlockVine.field_176273_b)).booleanValue() || ((Boolean) iblockdata1.func_177229_b(BlockVine.field_176278_M)).booleanValue() || ((Boolean) iblockdata1.func_177229_b(BlockVine.field_176279_N)).booleanValue() || ((Boolean) iblockdata1.func_177229_b(BlockVine.field_176280_O)).booleanValue()) {
                        // CraftBukkit start - Call BlockSpreadEvent
                        // world.setTypeAndData(blockposition1, iblockdata1, 2);
                        BlockPos target = blockposition1;
                        org.bukkit.block.Block source = world.getWorld().getBlockAt(blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p());
                        org.bukkit.block.Block block = world.getWorld().getBlockAt(target.func_177958_n(), target.func_177956_o(), target.func_177952_p());
                        CraftEventFactory.handleBlockSpreadEvent(block, source, this, func_176201_c(iblockdata1));
                        // CraftBukkit end
                    }

                } else {
                    IBlockState iblockdata2;
                    Block block;
                    BlockPos blockposition2;

                    if (enumdirection.func_176740_k().func_176722_c() && !((Boolean) iblockdata.func_177229_b(func_176267_a(enumdirection))).booleanValue()) {
                        if (!flag1) {
                            blockposition2 = blockposition.func_177972_a(enumdirection);
                            iblockdata2 = world.func_180495_p(blockposition2);
                            block = iblockdata2.func_177230_c();
                            if (block.field_149764_J == Material.field_151579_a) {
                                EnumFacing enumdirection2 = enumdirection.func_176746_e();
                                EnumFacing enumdirection3 = enumdirection.func_176735_f();
                                boolean flag2 = ((Boolean) iblockdata.func_177229_b(func_176267_a(enumdirection2))).booleanValue();
                                boolean flag3 = ((Boolean) iblockdata.func_177229_b(func_176267_a(enumdirection3))).booleanValue();
                                BlockPos blockposition3 = blockposition2.func_177972_a(enumdirection2);
                                BlockPos blockposition4 = blockposition2.func_177972_a(enumdirection3);

                                // CraftBukkit start - Call BlockSpreadEvent
                                org.bukkit.block.Block source = world.getWorld().getBlockAt(blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p());
                                org.bukkit.block.Block bukkitBlock = world.getWorld().getBlockAt(blockposition2.func_177958_n(), blockposition2.func_177956_o(), blockposition2.func_177952_p());

                                if (flag2 && this.func_193395_a(world, blockposition3.func_177972_a(enumdirection2), enumdirection2)) {
                                    // world.setTypeAndData(blockposition2, this.getBlockData().set(getDirection(enumdirection2), Boolean.valueOf(true)), 2);
                                    CraftEventFactory.handleBlockSpreadEvent(bukkitBlock, source, this, func_176201_c(this.func_176223_P().func_177226_a(func_176267_a(enumdirection2), Boolean.valueOf(true))));
                                } else if (flag3 && this.func_193395_a(world, blockposition4.func_177972_a(enumdirection3), enumdirection3)) {
                                    // world.setTypeAndData(blockposition2, this.getBlockData().set(getDirection(enumdirection3), Boolean.valueOf(true)), 2);
                                    CraftEventFactory.handleBlockSpreadEvent(bukkitBlock, source, this, func_176201_c(this.func_176223_P().func_177226_a(func_176267_a(enumdirection3), Boolean.valueOf(true))));
                                } else if (flag2 && world.func_175623_d(blockposition3) && this.func_193395_a(world, blockposition3, enumdirection)) {
                                    // world.setTypeAndData(blockposition3, this.getBlockData().set(getDirection(enumdirection.opposite()), Boolean.valueOf(true)), 2);
                                    bukkitBlock = world.getWorld().getBlockAt(blockposition3.func_177958_n(), blockposition3.func_177956_o(), blockposition3.func_177952_p());
                                    CraftEventFactory.handleBlockSpreadEvent(bukkitBlock, source, this, func_176201_c(this.func_176223_P().func_177226_a(func_176267_a(enumdirection.func_176734_d()), Boolean.valueOf(true))));
                                } else if (flag3 && world.func_175623_d(blockposition4) && this.func_193395_a(world, blockposition4, enumdirection)) {
                                    // world.setTypeAndData(blockposition4, this.getBlockData().set(getDirection(enumdirection.opposite()), Boolean.valueOf(true)), 2);
                                    bukkitBlock = world.getWorld().getBlockAt(blockposition4.func_177958_n(), blockposition4.func_177956_o(), blockposition4.func_177952_p());
                                    CraftEventFactory.handleBlockSpreadEvent(bukkitBlock, source, this, func_176201_c(this.func_176223_P().func_177226_a(func_176267_a(enumdirection.func_176734_d()), Boolean.valueOf(true))));
                                }
                                // CraftBukkit end
                            } else if (iblockdata2.func_193401_d(world, blockposition2, enumdirection) == BlockFaceShape.SOLID) {
                                world.func_180501_a(blockposition, iblockdata.func_177226_a(func_176267_a(enumdirection), Boolean.valueOf(true)), 2);
                            }

                        }
                    } else {
                        if (blockposition.func_177956_o() > 1) {
                            blockposition2 = blockposition.func_177977_b();
                            iblockdata2 = world.func_180495_p(blockposition2);
                            block = iblockdata2.func_177230_c();
                            IBlockState iblockdata3;
                            Iterator iterator1;
                            EnumFacing enumdirection4;

                            if (block.field_149764_J == Material.field_151579_a) {
                                iblockdata3 = iblockdata;
                                iterator1 = EnumFacing.Plane.HORIZONTAL.iterator();

                                while (iterator1.hasNext()) {
                                    enumdirection4 = (EnumFacing) iterator1.next();
                                    if (random.nextBoolean()) {
                                        iblockdata3 = iblockdata3.func_177226_a(func_176267_a(enumdirection4), Boolean.valueOf(false));
                                    }
                                }

                                if (((Boolean) iblockdata3.func_177229_b(BlockVine.field_176273_b)).booleanValue() || ((Boolean) iblockdata3.func_177229_b(BlockVine.field_176278_M)).booleanValue() || ((Boolean) iblockdata3.func_177229_b(BlockVine.field_176279_N)).booleanValue() || ((Boolean) iblockdata3.func_177229_b(BlockVine.field_176280_O)).booleanValue()) {
                                    // CraftBukkit start - Call BlockSpreadEvent
                                    // world.setTypeAndData(blockposition2, iblockdata3, 2);
                                    org.bukkit.block.Block source = world.getWorld().getBlockAt(blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p());
                                    org.bukkit.block.Block bukkitBlock = world.getWorld().getBlockAt(blockposition2.func_177958_n(), blockposition2.func_177956_o(), blockposition2.func_177952_p());
                                    CraftEventFactory.handleBlockSpreadEvent(bukkitBlock, source, this, func_176201_c(iblockdata3));
                                    // CraftBukkit end
                                }
                            } else if (block == this) {
                                iblockdata3 = iblockdata2;
                                iterator1 = EnumFacing.Plane.HORIZONTAL.iterator();

                                while (iterator1.hasNext()) {
                                    enumdirection4 = (EnumFacing) iterator1.next();
                                    PropertyBool blockstateboolean = func_176267_a(enumdirection4);

                                    if (random.nextBoolean() && ((Boolean) iblockdata.func_177229_b(blockstateboolean)).booleanValue()) {
                                        iblockdata3 = iblockdata3.func_177226_a(blockstateboolean, Boolean.valueOf(true));
                                    }
                                }

                                if (((Boolean) iblockdata3.func_177229_b(BlockVine.field_176273_b)).booleanValue() || ((Boolean) iblockdata3.func_177229_b(BlockVine.field_176278_M)).booleanValue() || ((Boolean) iblockdata3.func_177229_b(BlockVine.field_176279_N)).booleanValue() || ((Boolean) iblockdata3.func_177229_b(BlockVine.field_176280_O)).booleanValue()) {
                                    world.func_180501_a(blockposition2, iblockdata3, 2);
                                }
                            }
                        }

                    }
                }
            }
        }
    }

    public IBlockState func_180642_a(World world, BlockPos blockposition, EnumFacing enumdirection, float f, float f1, float f2, int i, EntityLivingBase entityliving) {
        IBlockState iblockdata = this.func_176223_P().func_177226_a(BlockVine.field_176277_a, Boolean.valueOf(false)).func_177226_a(BlockVine.field_176273_b, Boolean.valueOf(false)).func_177226_a(BlockVine.field_176278_M, Boolean.valueOf(false)).func_177226_a(BlockVine.field_176279_N, Boolean.valueOf(false)).func_177226_a(BlockVine.field_176280_O, Boolean.valueOf(false));

        return enumdirection.func_176740_k().func_176722_c() ? iblockdata.func_177226_a(func_176267_a(enumdirection.func_176734_d()), Boolean.valueOf(true)) : iblockdata;
    }

    public Item func_180660_a(IBlockState iblockdata, Random random, int i) {
        return Items.field_190931_a;
    }

    public int func_149745_a(Random random) {
        return 0;
    }

    public void func_180657_a(World world, EntityPlayer entityhuman, BlockPos blockposition, IBlockState iblockdata, @Nullable TileEntity tileentity, ItemStack itemstack) {
        if (!world.field_72995_K && itemstack.func_77973_b() == Items.field_151097_aZ) {
            entityhuman.func_71029_a(StatList.func_188055_a((Block) this));
            func_180635_a(world, blockposition, new ItemStack(Blocks.field_150395_bd, 1, 0));
        } else {
            super.func_180657_a(world, entityhuman, blockposition, iblockdata, tileentity, itemstack);
        }

    }

    public IBlockState func_176203_a(int i) {
        return this.func_176223_P().func_177226_a(BlockVine.field_176279_N, Boolean.valueOf((i & 1) > 0)).func_177226_a(BlockVine.field_176280_O, Boolean.valueOf((i & 2) > 0)).func_177226_a(BlockVine.field_176273_b, Boolean.valueOf((i & 4) > 0)).func_177226_a(BlockVine.field_176278_M, Boolean.valueOf((i & 8) > 0));
    }

    public int func_176201_c(IBlockState iblockdata) {
        int i = 0;

        if (((Boolean) iblockdata.func_177229_b(BlockVine.field_176279_N)).booleanValue()) {
            i |= 1;
        }

        if (((Boolean) iblockdata.func_177229_b(BlockVine.field_176280_O)).booleanValue()) {
            i |= 2;
        }

        if (((Boolean) iblockdata.func_177229_b(BlockVine.field_176273_b)).booleanValue()) {
            i |= 4;
        }

        if (((Boolean) iblockdata.func_177229_b(BlockVine.field_176278_M)).booleanValue()) {
            i |= 8;
        }

        return i;
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockVine.field_176277_a, BlockVine.field_176273_b, BlockVine.field_176278_M, BlockVine.field_176279_N, BlockVine.field_176280_O});
    }

    public IBlockState func_185499_a(IBlockState iblockdata, Rotation enumblockrotation) {
        switch (enumblockrotation) {
        case CLOCKWISE_180:
            return iblockdata.func_177226_a(BlockVine.field_176273_b, iblockdata.func_177229_b(BlockVine.field_176279_N)).func_177226_a(BlockVine.field_176278_M, iblockdata.func_177229_b(BlockVine.field_176280_O)).func_177226_a(BlockVine.field_176279_N, iblockdata.func_177229_b(BlockVine.field_176273_b)).func_177226_a(BlockVine.field_176280_O, iblockdata.func_177229_b(BlockVine.field_176278_M));

        case COUNTERCLOCKWISE_90:
            return iblockdata.func_177226_a(BlockVine.field_176273_b, iblockdata.func_177229_b(BlockVine.field_176278_M)).func_177226_a(BlockVine.field_176278_M, iblockdata.func_177229_b(BlockVine.field_176279_N)).func_177226_a(BlockVine.field_176279_N, iblockdata.func_177229_b(BlockVine.field_176280_O)).func_177226_a(BlockVine.field_176280_O, iblockdata.func_177229_b(BlockVine.field_176273_b));

        case CLOCKWISE_90:
            return iblockdata.func_177226_a(BlockVine.field_176273_b, iblockdata.func_177229_b(BlockVine.field_176280_O)).func_177226_a(BlockVine.field_176278_M, iblockdata.func_177229_b(BlockVine.field_176273_b)).func_177226_a(BlockVine.field_176279_N, iblockdata.func_177229_b(BlockVine.field_176278_M)).func_177226_a(BlockVine.field_176280_O, iblockdata.func_177229_b(BlockVine.field_176279_N));

        default:
            return iblockdata;
        }
    }

    public IBlockState func_185471_a(IBlockState iblockdata, Mirror enumblockmirror) {
        switch (enumblockmirror) {
        case LEFT_RIGHT:
            return iblockdata.func_177226_a(BlockVine.field_176273_b, iblockdata.func_177229_b(BlockVine.field_176279_N)).func_177226_a(BlockVine.field_176279_N, iblockdata.func_177229_b(BlockVine.field_176273_b));

        case FRONT_BACK:
            return iblockdata.func_177226_a(BlockVine.field_176278_M, iblockdata.func_177229_b(BlockVine.field_176280_O)).func_177226_a(BlockVine.field_176280_O, iblockdata.func_177229_b(BlockVine.field_176278_M));

        default:
            return super.func_185471_a(iblockdata, enumblockmirror);
        }
    }

    public static PropertyBool func_176267_a(EnumFacing enumdirection) {
        switch (enumdirection) {
        case UP:
            return BlockVine.field_176277_a;

        case NORTH:
            return BlockVine.field_176273_b;

        case SOUTH:
            return BlockVine.field_176279_N;

        case WEST:
            return BlockVine.field_176280_O;

        case EAST:
            return BlockVine.field_176278_M;

        default:
            throw new IllegalArgumentException(enumdirection + " is an invalid choice");
        }
    }

    public static int func_176268_d(IBlockState iblockdata) {
        int i = 0;
        PropertyBool[] ablockstateboolean = BlockVine.field_176274_P;
        int j = ablockstateboolean.length;

        for (int k = 0; k < j; ++k) {
            PropertyBool blockstateboolean = ablockstateboolean[k];

            if (((Boolean) iblockdata.func_177229_b(blockstateboolean)).booleanValue()) {
                ++i;
            }
        }

        return i;
    }

    public BlockFaceShape func_193383_a(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return BlockFaceShape.UNDEFINED;
    }
}
