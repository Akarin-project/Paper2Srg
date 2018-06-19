package net.minecraft.block;

import java.util.Random;


import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.bukkit.event.block.BlockRedstoneEvent;

public class BlockDoor extends Block {

    public static final PropertyDirection field_176520_a = BlockHorizontal.field_185512_D;
    public static final PropertyBool field_176519_b = PropertyBool.func_177716_a("open");
    public static final PropertyEnum<BlockDoor.EnumHingePosition> field_176521_M = PropertyEnum.func_177709_a("hinge", BlockDoor.EnumHingePosition.class);
    public static final PropertyBool field_176522_N = PropertyBool.func_177716_a("powered");
    public static final PropertyEnum<BlockDoor.EnumDoorHalf> field_176523_O = PropertyEnum.func_177709_a("half", BlockDoor.EnumDoorHalf.class);
    protected static final AxisAlignedBB field_185658_f = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.1875D);
    protected static final AxisAlignedBB field_185659_g = new AxisAlignedBB(0.0D, 0.0D, 0.8125D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB field_185656_B = new AxisAlignedBB(0.8125D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB field_185657_C = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.1875D, 1.0D, 1.0D);

    protected BlockDoor(Material material) {
        super(material);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockDoor.field_176520_a, EnumFacing.NORTH).func_177226_a(BlockDoor.field_176519_b, Boolean.valueOf(false)).func_177226_a(BlockDoor.field_176521_M, BlockDoor.EnumHingePosition.LEFT).func_177226_a(BlockDoor.field_176522_N, Boolean.valueOf(false)).func_177226_a(BlockDoor.field_176523_O, BlockDoor.EnumDoorHalf.LOWER));
    }

    public AxisAlignedBB func_185496_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        iblockdata = iblockdata.func_185899_b(iblockaccess, blockposition);
        EnumFacing enumdirection = (EnumFacing) iblockdata.func_177229_b(BlockDoor.field_176520_a);
        boolean flag = !((Boolean) iblockdata.func_177229_b(BlockDoor.field_176519_b)).booleanValue();
        boolean flag1 = iblockdata.func_177229_b(BlockDoor.field_176521_M) == BlockDoor.EnumHingePosition.RIGHT;

        switch (enumdirection) {
        case EAST:
        default:
            return flag ? BlockDoor.field_185657_C : (flag1 ? BlockDoor.field_185659_g : BlockDoor.field_185658_f);

        case SOUTH:
            return flag ? BlockDoor.field_185658_f : (flag1 ? BlockDoor.field_185657_C : BlockDoor.field_185656_B);

        case WEST:
            return flag ? BlockDoor.field_185656_B : (flag1 ? BlockDoor.field_185658_f : BlockDoor.field_185659_g);

        case NORTH:
            return flag ? BlockDoor.field_185659_g : (flag1 ? BlockDoor.field_185656_B : BlockDoor.field_185657_C);
        }
    }

    public String func_149732_F() {
        return I18n.func_74838_a((this.func_149739_a() + ".name").replaceAll("tile", "item"));
    }

    public boolean func_149662_c(IBlockState iblockdata) {
        return false;
    }

    public boolean func_176205_b(IBlockAccess iblockaccess, BlockPos blockposition) {
        return func_176516_g(func_176515_e(iblockaccess, blockposition));
    }

    public boolean func_149686_d(IBlockState iblockdata) {
        return false;
    }

    private int func_185654_e() {
        return this.field_149764_J == Material.field_151573_f ? 1011 : 1012;
    }

    private int func_185655_g() {
        return this.field_149764_J == Material.field_151573_f ? 1005 : 1006;
    }

    public MapColor func_180659_g(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return iblockdata.func_177230_c() == Blocks.field_150454_av ? MapColor.field_151668_h : (iblockdata.func_177230_c() == Blocks.field_180413_ao ? BlockPlanks.EnumType.OAK.func_181070_c() : (iblockdata.func_177230_c() == Blocks.field_180414_ap ? BlockPlanks.EnumType.SPRUCE.func_181070_c() : (iblockdata.func_177230_c() == Blocks.field_180412_aq ? BlockPlanks.EnumType.BIRCH.func_181070_c() : (iblockdata.func_177230_c() == Blocks.field_180411_ar ? BlockPlanks.EnumType.JUNGLE.func_181070_c() : (iblockdata.func_177230_c() == Blocks.field_180410_as ? BlockPlanks.EnumType.ACACIA.func_181070_c() : (iblockdata.func_177230_c() == Blocks.field_180409_at ? BlockPlanks.EnumType.DARK_OAK.func_181070_c() : super.func_180659_g(iblockdata, iblockaccess, blockposition)))))));
    }

    public boolean func_180639_a(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        if (this.field_149764_J == Material.field_151573_f) {
            return false;
        } else {
            BlockPos blockposition1 = iblockdata.func_177229_b(BlockDoor.field_176523_O) == BlockDoor.EnumDoorHalf.LOWER ? blockposition : blockposition.func_177977_b();
            IBlockState iblockdata1 = blockposition.equals(blockposition1) ? iblockdata : world.func_180495_p(blockposition1);

            if (iblockdata1.func_177230_c() != this) {
                return false;
            } else {
                iblockdata = iblockdata1.func_177231_a((IProperty) BlockDoor.field_176519_b);
                world.func_180501_a(blockposition1, iblockdata, 10);
                world.func_175704_b(blockposition1, blockposition);
                world.func_180498_a(entityhuman, ((Boolean) iblockdata.func_177229_b(BlockDoor.field_176519_b)).booleanValue() ? this.func_185655_g() : this.func_185654_e(), blockposition, 0);
                return true;
            }
        }
    }

    public void func_176512_a(World world, BlockPos blockposition, boolean flag) {
        IBlockState iblockdata = world.func_180495_p(blockposition);

        if (iblockdata.func_177230_c() == this) {
            BlockPos blockposition1 = iblockdata.func_177229_b(BlockDoor.field_176523_O) == BlockDoor.EnumDoorHalf.LOWER ? blockposition : blockposition.func_177977_b();
            IBlockState iblockdata1 = blockposition == blockposition1 ? iblockdata : world.func_180495_p(blockposition1);

            if (iblockdata1.func_177230_c() == this && ((Boolean) iblockdata1.func_177229_b(BlockDoor.field_176519_b)).booleanValue() != flag) {
                world.func_180501_a(blockposition1, iblockdata1.func_177226_a(BlockDoor.field_176519_b, Boolean.valueOf(flag)), 10);
                world.func_175704_b(blockposition1, blockposition);
                world.func_180498_a((EntityPlayer) null, flag ? this.func_185655_g() : this.func_185654_e(), blockposition, 0);
            }

        }
    }

    public void func_189540_a(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        if (iblockdata.func_177229_b(BlockDoor.field_176523_O) == BlockDoor.EnumDoorHalf.UPPER) {
            BlockPos blockposition2 = blockposition.func_177977_b();
            IBlockState iblockdata1 = world.func_180495_p(blockposition2);

            if (iblockdata1.func_177230_c() != this) {
                world.func_175698_g(blockposition);
            } else if (block != this) {
                iblockdata1.func_189546_a(world, blockposition2, block, blockposition1);
            }
        } else {
            boolean flag = false;
            BlockPos blockposition3 = blockposition.func_177984_a();
            IBlockState iblockdata2 = world.func_180495_p(blockposition3);

            if (iblockdata2.func_177230_c() != this) {
                world.func_175698_g(blockposition);
                flag = true;
            }

            if (!world.func_180495_p(blockposition.func_177977_b()).func_185896_q()) {
                world.func_175698_g(blockposition);
                flag = true;
                if (iblockdata2.func_177230_c() == this) {
                    world.func_175698_g(blockposition3);
                }
            }

            if (flag) {
                if (!world.field_72995_K) {
                    this.func_176226_b(world, blockposition, iblockdata, 0);
                }
            } else {

                // CraftBukkit start
                org.bukkit.World bworld = world.getWorld();
                org.bukkit.block.Block bukkitBlock = bworld.getBlockAt(blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p());
                org.bukkit.block.Block blockTop = bworld.getBlockAt(blockposition3.func_177958_n(), blockposition3.func_177956_o(), blockposition3.func_177952_p());

                int power = bukkitBlock.getBlockPower();
                int powerTop = blockTop.getBlockPower();
                if (powerTop > power) power = powerTop;
                int oldPower = (Boolean) iblockdata2.func_177229_b(BlockDoor.field_176522_N) ? 15 : 0;

                if (oldPower == 0 ^ power == 0) {
                    BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(bukkitBlock, oldPower, power);
                    world.getServer().getPluginManager().callEvent(eventRedstone);

                    boolean flag1 = eventRedstone.getNewCurrent() > 0;
                    // CraftBukkit end
                    world.func_180501_a(blockposition3, iblockdata2.func_177226_a(BlockDoor.field_176522_N, Boolean.valueOf(flag1)), 2);
                    if (flag1 != ((Boolean) iblockdata.func_177229_b(BlockDoor.field_176519_b)).booleanValue()) {
                        world.func_180501_a(blockposition, iblockdata.func_177226_a(BlockDoor.field_176519_b, Boolean.valueOf(flag1)), 2);
                        world.func_175704_b(blockposition, blockposition);
                        world.func_180498_a((EntityPlayer) null, flag1 ? this.func_185655_g() : this.func_185654_e(), blockposition, 0);
                    }
                }
            }
        }

    }

    public Item func_180660_a(IBlockState iblockdata, Random random, int i) {
        return iblockdata.func_177229_b(BlockDoor.field_176523_O) == BlockDoor.EnumDoorHalf.UPPER ? Items.field_190931_a : this.func_176509_j();
    }

    public boolean func_176196_c(World world, BlockPos blockposition) {
        return blockposition.func_177956_o() >= 255 ? false : world.func_180495_p(blockposition.func_177977_b()).func_185896_q() && super.func_176196_c(world, blockposition) && super.func_176196_c(world, blockposition.func_177984_a());
    }

    public EnumPushReaction func_149656_h(IBlockState iblockdata) {
        return EnumPushReaction.DESTROY;
    }

    public static int func_176515_e(IBlockAccess iblockaccess, BlockPos blockposition) {
        IBlockState iblockdata = iblockaccess.func_180495_p(blockposition);
        int i = iblockdata.func_177230_c().func_176201_c(iblockdata);
        boolean flag = func_176518_i(i);
        IBlockState iblockdata1 = iblockaccess.func_180495_p(blockposition.func_177977_b());
        int j = iblockdata1.func_177230_c().func_176201_c(iblockdata1);
        int k = flag ? j : i;
        IBlockState iblockdata2 = iblockaccess.func_180495_p(blockposition.func_177984_a());
        int l = iblockdata2.func_177230_c().func_176201_c(iblockdata2);
        int i1 = flag ? i : l;
        boolean flag1 = (i1 & 1) != 0;
        boolean flag2 = (i1 & 2) != 0;

        return func_176510_b(k) | (flag ? 8 : 0) | (flag1 ? 16 : 0) | (flag2 ? 32 : 0);
    }

    public ItemStack func_185473_a(World world, BlockPos blockposition, IBlockState iblockdata) {
        return new ItemStack(this.func_176509_j());
    }

    private Item func_176509_j() {
        return this == Blocks.field_150454_av ? Items.field_151139_aw : (this == Blocks.field_180414_ap ? Items.field_179569_ar : (this == Blocks.field_180412_aq ? Items.field_179568_as : (this == Blocks.field_180411_ar ? Items.field_179567_at : (this == Blocks.field_180410_as ? Items.field_179572_au : (this == Blocks.field_180409_at ? Items.field_179571_av : Items.field_179570_aq)))));
    }

    public void func_176208_a(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman) {
        BlockPos blockposition1 = blockposition.func_177977_b();
        BlockPos blockposition2 = blockposition.func_177984_a();

        if (entityhuman.field_71075_bZ.field_75098_d && iblockdata.func_177229_b(BlockDoor.field_176523_O) == BlockDoor.EnumDoorHalf.UPPER && world.func_180495_p(blockposition1).func_177230_c() == this) {
            world.func_175698_g(blockposition1);
        }

        if (iblockdata.func_177229_b(BlockDoor.field_176523_O) == BlockDoor.EnumDoorHalf.LOWER && world.func_180495_p(blockposition2).func_177230_c() == this) {
            if (entityhuman.field_71075_bZ.field_75098_d) {
                world.func_175698_g(blockposition);
            }

            world.func_175698_g(blockposition2);
        }

    }

    public IBlockState func_176221_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        IBlockState iblockdata1;

        if (iblockdata.func_177229_b(BlockDoor.field_176523_O) == BlockDoor.EnumDoorHalf.LOWER) {
            iblockdata1 = iblockaccess.func_180495_p(blockposition.func_177984_a());
            if (iblockdata1.func_177230_c() == this) {
                iblockdata = iblockdata.func_177226_a(BlockDoor.field_176521_M, iblockdata1.func_177229_b(BlockDoor.field_176521_M)).func_177226_a(BlockDoor.field_176522_N, iblockdata1.func_177229_b(BlockDoor.field_176522_N));
            }
        } else {
            iblockdata1 = iblockaccess.func_180495_p(blockposition.func_177977_b());
            if (iblockdata1.func_177230_c() == this) {
                iblockdata = iblockdata.func_177226_a(BlockDoor.field_176520_a, iblockdata1.func_177229_b(BlockDoor.field_176520_a)).func_177226_a(BlockDoor.field_176519_b, iblockdata1.func_177229_b(BlockDoor.field_176519_b));
            }
        }

        return iblockdata;
    }

    public IBlockState func_185499_a(IBlockState iblockdata, Rotation enumblockrotation) {
        return iblockdata.func_177229_b(BlockDoor.field_176523_O) != BlockDoor.EnumDoorHalf.LOWER ? iblockdata : iblockdata.func_177226_a(BlockDoor.field_176520_a, enumblockrotation.func_185831_a((EnumFacing) iblockdata.func_177229_b(BlockDoor.field_176520_a)));
    }

    public IBlockState func_185471_a(IBlockState iblockdata, Mirror enumblockmirror) {
        return enumblockmirror == Mirror.NONE ? iblockdata : iblockdata.func_185907_a(enumblockmirror.func_185800_a((EnumFacing) iblockdata.func_177229_b(BlockDoor.field_176520_a))).func_177231_a((IProperty) BlockDoor.field_176521_M);
    }

    public IBlockState func_176203_a(int i) {
        return (i & 8) > 0 ? this.func_176223_P().func_177226_a(BlockDoor.field_176523_O, BlockDoor.EnumDoorHalf.UPPER).func_177226_a(BlockDoor.field_176521_M, (i & 1) > 0 ? BlockDoor.EnumHingePosition.RIGHT : BlockDoor.EnumHingePosition.LEFT).func_177226_a(BlockDoor.field_176522_N, Boolean.valueOf((i & 2) > 0)) : this.func_176223_P().func_177226_a(BlockDoor.field_176523_O, BlockDoor.EnumDoorHalf.LOWER).func_177226_a(BlockDoor.field_176520_a, EnumFacing.func_176731_b(i & 3).func_176735_f()).func_177226_a(BlockDoor.field_176519_b, Boolean.valueOf((i & 4) > 0));
    }

    public int func_176201_c(IBlockState iblockdata) {
        byte b0 = 0;
        int i;

        if (iblockdata.func_177229_b(BlockDoor.field_176523_O) == BlockDoor.EnumDoorHalf.UPPER) {
            i = b0 | 8;
            if (iblockdata.func_177229_b(BlockDoor.field_176521_M) == BlockDoor.EnumHingePosition.RIGHT) {
                i |= 1;
            }

            if (((Boolean) iblockdata.func_177229_b(BlockDoor.field_176522_N)).booleanValue()) {
                i |= 2;
            }
        } else {
            i = b0 | ((EnumFacing) iblockdata.func_177229_b(BlockDoor.field_176520_a)).func_176746_e().func_176736_b();
            if (((Boolean) iblockdata.func_177229_b(BlockDoor.field_176519_b)).booleanValue()) {
                i |= 4;
            }
        }

        return i;
    }

    protected static int func_176510_b(int i) {
        return i & 7;
    }

    public static boolean func_176514_f(IBlockAccess iblockaccess, BlockPos blockposition) {
        return func_176516_g(func_176515_e(iblockaccess, blockposition));
    }

    public static EnumFacing func_176517_h(IBlockAccess iblockaccess, BlockPos blockposition) {
        return func_176511_f(func_176515_e(iblockaccess, blockposition));
    }

    public static EnumFacing func_176511_f(int i) {
        return EnumFacing.func_176731_b(i & 3).func_176735_f();
    }

    protected static boolean func_176516_g(int i) {
        return (i & 4) != 0;
    }

    protected static boolean func_176518_i(int i) {
        return (i & 8) != 0;
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockDoor.field_176523_O, BlockDoor.field_176520_a, BlockDoor.field_176519_b, BlockDoor.field_176521_M, BlockDoor.field_176522_N});
    }

    public BlockFaceShape func_193383_a(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return BlockFaceShape.UNDEFINED;
    }

    public static enum EnumHingePosition implements IStringSerializable {

        LEFT, RIGHT;

        private EnumHingePosition() {}

        public String toString() {
            return this.func_176610_l();
        }

        public String func_176610_l() {
            return this == BlockDoor.EnumHingePosition.LEFT ? "left" : "right";
        }
    }

    public static enum EnumDoorHalf implements IStringSerializable {

        UPPER, LOWER;

        private EnumDoorHalf() {}

        public String toString() {
            return this.func_176610_l();
        }

        public String func_176610_l() {
            return this == BlockDoor.EnumDoorHalf.UPPER ? "upper" : "lower";
        }
    }
}
