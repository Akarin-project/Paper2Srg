package net.minecraft.block;

import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.bukkit.event.block.BlockRedstoneEvent;

public class BlockTrapDoor extends Block {

    public static final PropertyDirection field_176284_a = BlockHorizontal.field_185512_D;
    public static final PropertyBool field_176283_b = PropertyBool.func_177716_a("open");
    public static final PropertyEnum<BlockTrapDoor.DoorHalf> field_176285_M = PropertyEnum.func_177709_a("half", BlockTrapDoor.DoorHalf.class);
    protected static final AxisAlignedBB field_185734_d = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.1875D, 1.0D, 1.0D);
    protected static final AxisAlignedBB field_185735_e = new AxisAlignedBB(0.8125D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB field_185736_f = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.1875D);
    protected static final AxisAlignedBB field_185737_g = new AxisAlignedBB(0.0D, 0.0D, 0.8125D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB field_185732_B = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.1875D, 1.0D);
    protected static final AxisAlignedBB field_185733_C = new AxisAlignedBB(0.0D, 0.8125D, 0.0D, 1.0D, 1.0D, 1.0D);

    protected BlockTrapDoor(Material material) {
        super(material);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockTrapDoor.field_176284_a, EnumFacing.NORTH).func_177226_a(BlockTrapDoor.field_176283_b, Boolean.valueOf(false)).func_177226_a(BlockTrapDoor.field_176285_M, BlockTrapDoor.DoorHalf.BOTTOM));
        this.func_149647_a(CreativeTabs.field_78028_d);
    }

    public AxisAlignedBB func_185496_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        AxisAlignedBB axisalignedbb;

        if (((Boolean) iblockdata.func_177229_b(BlockTrapDoor.field_176283_b)).booleanValue()) {
            switch ((EnumFacing) iblockdata.func_177229_b(BlockTrapDoor.field_176284_a)) {
            case NORTH:
            default:
                axisalignedbb = BlockTrapDoor.field_185737_g;
                break;

            case SOUTH:
                axisalignedbb = BlockTrapDoor.field_185736_f;
                break;

            case WEST:
                axisalignedbb = BlockTrapDoor.field_185735_e;
                break;

            case EAST:
                axisalignedbb = BlockTrapDoor.field_185734_d;
            }
        } else if (iblockdata.func_177229_b(BlockTrapDoor.field_176285_M) == BlockTrapDoor.DoorHalf.TOP) {
            axisalignedbb = BlockTrapDoor.field_185733_C;
        } else {
            axisalignedbb = BlockTrapDoor.field_185732_B;
        }

        return axisalignedbb;
    }

    public boolean func_149662_c(IBlockState iblockdata) {
        return false;
    }

    public boolean func_149686_d(IBlockState iblockdata) {
        return false;
    }

    public boolean func_176205_b(IBlockAccess iblockaccess, BlockPos blockposition) {
        return !((Boolean) iblockaccess.func_180495_p(blockposition).func_177229_b(BlockTrapDoor.field_176283_b)).booleanValue();
    }

    public boolean func_180639_a(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        if (this.field_149764_J == Material.field_151573_f) {
            return false;
        } else {
            iblockdata = iblockdata.func_177231_a((IProperty) BlockTrapDoor.field_176283_b);
            world.func_180501_a(blockposition, iblockdata, 2);
            this.func_185731_a(entityhuman, world, blockposition, ((Boolean) iblockdata.func_177229_b(BlockTrapDoor.field_176283_b)).booleanValue());
            return true;
        }
    }

    protected void func_185731_a(@Nullable EntityPlayer entityhuman, World world, BlockPos blockposition, boolean flag) {
        int i;

        if (flag) {
            i = this.field_149764_J == Material.field_151573_f ? 1037 : 1007;
            world.func_180498_a(entityhuman, i, blockposition, 0);
        } else {
            i = this.field_149764_J == Material.field_151573_f ? 1036 : 1013;
            world.func_180498_a(entityhuman, i, blockposition, 0);
        }

    }

    public void func_189540_a(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        if (!world.field_72995_K) {
            boolean flag = world.func_175640_z(blockposition);

            if (flag || block.func_176223_P().func_185897_m()) {
                // CraftBukkit start
                org.bukkit.World bworld = world.getWorld();
                org.bukkit.block.Block bblock = bworld.getBlockAt(blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p());

                int power = bblock.getBlockPower();
                int oldPower = (Boolean) iblockdata.func_177229_b(field_176283_b) ? 15 : 0;

                if (oldPower == 0 ^ power == 0 || block.func_176223_P().func_185912_n()) {
                    BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(bblock, oldPower, power);
                    world.getServer().getPluginManager().callEvent(eventRedstone);
                    flag = eventRedstone.getNewCurrent() > 0;
                }
                // CraftBukkit end
                boolean flag1 = ((Boolean) iblockdata.func_177229_b(BlockTrapDoor.field_176283_b)).booleanValue();

                if (flag1 != flag) {
                    world.func_180501_a(blockposition, iblockdata.func_177226_a(BlockTrapDoor.field_176283_b, Boolean.valueOf(flag)), 2);
                    this.func_185731_a((EntityPlayer) null, world, blockposition, flag);
                }
            }

        }
    }

    public IBlockState func_180642_a(World world, BlockPos blockposition, EnumFacing enumdirection, float f, float f1, float f2, int i, EntityLivingBase entityliving) {
        IBlockState iblockdata = this.func_176223_P();

        if (enumdirection.func_176740_k().func_176722_c()) {
            iblockdata = iblockdata.func_177226_a(BlockTrapDoor.field_176284_a, enumdirection).func_177226_a(BlockTrapDoor.field_176283_b, Boolean.valueOf(false));
            iblockdata = iblockdata.func_177226_a(BlockTrapDoor.field_176285_M, f1 > 0.5F ? BlockTrapDoor.DoorHalf.TOP : BlockTrapDoor.DoorHalf.BOTTOM);
        } else {
            iblockdata = iblockdata.func_177226_a(BlockTrapDoor.field_176284_a, entityliving.func_174811_aO().func_176734_d()).func_177226_a(BlockTrapDoor.field_176283_b, Boolean.valueOf(false));
            iblockdata = iblockdata.func_177226_a(BlockTrapDoor.field_176285_M, enumdirection == EnumFacing.UP ? BlockTrapDoor.DoorHalf.BOTTOM : BlockTrapDoor.DoorHalf.TOP);
        }

        if (world.func_175640_z(blockposition)) {
            iblockdata = iblockdata.func_177226_a(BlockTrapDoor.field_176283_b, Boolean.valueOf(true));
        }

        return iblockdata;
    }

    public boolean func_176198_a(World world, BlockPos blockposition, EnumFacing enumdirection) {
        return true;
    }

    protected static EnumFacing func_176281_b(int i) {
        switch (i & 3) {
        case 0:
            return EnumFacing.NORTH;

        case 1:
            return EnumFacing.SOUTH;

        case 2:
            return EnumFacing.WEST;

        case 3:
        default:
            return EnumFacing.EAST;
        }
    }

    protected static int func_176282_a(EnumFacing enumdirection) {
        switch (enumdirection) {
        case NORTH:
            return 0;

        case SOUTH:
            return 1;

        case WEST:
            return 2;

        case EAST:
        default:
            return 3;
        }
    }

    public IBlockState func_176203_a(int i) {
        return this.func_176223_P().func_177226_a(BlockTrapDoor.field_176284_a, func_176281_b(i)).func_177226_a(BlockTrapDoor.field_176283_b, Boolean.valueOf((i & 4) != 0)).func_177226_a(BlockTrapDoor.field_176285_M, (i & 8) == 0 ? BlockTrapDoor.DoorHalf.BOTTOM : BlockTrapDoor.DoorHalf.TOP);
    }

    public int func_176201_c(IBlockState iblockdata) {
        byte b0 = 0;
        int i = b0 | func_176282_a((EnumFacing) iblockdata.func_177229_b(BlockTrapDoor.field_176284_a));

        if (((Boolean) iblockdata.func_177229_b(BlockTrapDoor.field_176283_b)).booleanValue()) {
            i |= 4;
        }

        if (iblockdata.func_177229_b(BlockTrapDoor.field_176285_M) == BlockTrapDoor.DoorHalf.TOP) {
            i |= 8;
        }

        return i;
    }

    public IBlockState func_185499_a(IBlockState iblockdata, Rotation enumblockrotation) {
        return iblockdata.func_177226_a(BlockTrapDoor.field_176284_a, enumblockrotation.func_185831_a((EnumFacing) iblockdata.func_177229_b(BlockTrapDoor.field_176284_a)));
    }

    public IBlockState func_185471_a(IBlockState iblockdata, Mirror enumblockmirror) {
        return iblockdata.func_185907_a(enumblockmirror.func_185800_a((EnumFacing) iblockdata.func_177229_b(BlockTrapDoor.field_176284_a)));
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockTrapDoor.field_176284_a, BlockTrapDoor.field_176283_b, BlockTrapDoor.field_176285_M});
    }

    public BlockFaceShape func_193383_a(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return (enumdirection == EnumFacing.UP && iblockdata.func_177229_b(BlockTrapDoor.field_176285_M) == BlockTrapDoor.DoorHalf.TOP || enumdirection == EnumFacing.DOWN && iblockdata.func_177229_b(BlockTrapDoor.field_176285_M) == BlockTrapDoor.DoorHalf.BOTTOM) && !((Boolean) iblockdata.func_177229_b(BlockTrapDoor.field_176283_b)).booleanValue() ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    }

    public static enum DoorHalf implements IStringSerializable {

        TOP("top"), BOTTOM("bottom");

        private final String field_176671_c;

        private DoorHalf(String s) {
            this.field_176671_c = s;
        }

        public String toString() {
            return this.field_176671_c;
        }

        public String func_176610_l() {
            return this.field_176671_c;
        }
    }
}
