package net.minecraft.block;

import java.util.Iterator;
import javax.annotation.Nullable;


import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.bukkit.event.block.BlockRedstoneEvent;

public class BlockLever extends Block {

    public static final PropertyEnum<BlockLever.EnumOrientation> field_176360_a = PropertyEnum.func_177709_a("facing", BlockLever.EnumOrientation.class);
    public static final PropertyBool field_176359_b = PropertyBool.func_177716_a("powered");
    protected static final AxisAlignedBB field_185692_c = new AxisAlignedBB(0.3125D, 0.20000000298023224D, 0.625D, 0.6875D, 0.800000011920929D, 1.0D);
    protected static final AxisAlignedBB field_185693_d = new AxisAlignedBB(0.3125D, 0.20000000298023224D, 0.0D, 0.6875D, 0.800000011920929D, 0.375D);
    protected static final AxisAlignedBB field_185694_e = new AxisAlignedBB(0.625D, 0.20000000298023224D, 0.3125D, 1.0D, 0.800000011920929D, 0.6875D);
    protected static final AxisAlignedBB field_185695_f = new AxisAlignedBB(0.0D, 0.20000000298023224D, 0.3125D, 0.375D, 0.800000011920929D, 0.6875D);
    protected static final AxisAlignedBB field_185696_g = new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 0.6000000238418579D, 0.75D);
    protected static final AxisAlignedBB field_185691_B = new AxisAlignedBB(0.25D, 0.4000000059604645D, 0.25D, 0.75D, 1.0D, 0.75D);

    protected BlockLever() {
        super(Material.field_151594_q);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockLever.field_176360_a, BlockLever.EnumOrientation.NORTH).func_177226_a(BlockLever.field_176359_b, Boolean.valueOf(false)));
        this.func_149647_a(CreativeTabs.field_78028_d);
    }

    @Nullable
    public AxisAlignedBB func_180646_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockLever.field_185506_k;
    }

    public boolean func_149662_c(IBlockState iblockdata) {
        return false;
    }

    public boolean func_149686_d(IBlockState iblockdata) {
        return false;
    }

    public boolean func_176198_a(World world, BlockPos blockposition, EnumFacing enumdirection) {
        return func_181090_a(world, blockposition, enumdirection);
    }

    public boolean func_176196_c(World world, BlockPos blockposition) {
        EnumFacing[] aenumdirection = EnumFacing.values();
        int i = aenumdirection.length;

        for (int j = 0; j < i; ++j) {
            EnumFacing enumdirection = aenumdirection[j];

            if (func_181090_a(world, blockposition, enumdirection)) {
                return true;
            }
        }

        return false;
    }

    protected static boolean func_181090_a(World world, BlockPos blockposition, EnumFacing enumdirection) {
        return BlockButton.func_181088_a(world, blockposition, enumdirection);
    }

    public IBlockState func_180642_a(World world, BlockPos blockposition, EnumFacing enumdirection, float f, float f1, float f2, int i, EntityLivingBase entityliving) {
        IBlockState iblockdata = this.func_176223_P().func_177226_a(BlockLever.field_176359_b, Boolean.valueOf(false));

        if (func_181090_a(world, blockposition, enumdirection)) {
            return iblockdata.func_177226_a(BlockLever.field_176360_a, BlockLever.EnumOrientation.func_176856_a(enumdirection, entityliving.func_174811_aO()));
        } else {
            Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();

            EnumFacing enumdirection1;

            do {
                if (!iterator.hasNext()) {
                    if (world.func_180495_p(blockposition.func_177977_b()).func_185896_q()) {
                        return iblockdata.func_177226_a(BlockLever.field_176360_a, BlockLever.EnumOrientation.func_176856_a(EnumFacing.UP, entityliving.func_174811_aO()));
                    }

                    return iblockdata;
                }

                enumdirection1 = (EnumFacing) iterator.next();
            } while (enumdirection1 == enumdirection || !func_181090_a(world, blockposition, enumdirection1));

            return iblockdata.func_177226_a(BlockLever.field_176360_a, BlockLever.EnumOrientation.func_176856_a(enumdirection1, entityliving.func_174811_aO()));
        }
    }

    public void func_189540_a(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        if (this.func_181091_e(world, blockposition, iblockdata) && !func_181090_a(world, blockposition, ((BlockLever.EnumOrientation) iblockdata.func_177229_b(BlockLever.field_176360_a)).func_176852_c())) {
            this.func_176226_b(world, blockposition, iblockdata, 0);
            world.func_175698_g(blockposition);
        }

    }

    private boolean func_181091_e(World world, BlockPos blockposition, IBlockState iblockdata) {
        if (this.func_176196_c(world, blockposition)) {
            return true;
        } else {
            this.func_176226_b(world, blockposition, iblockdata, 0);
            world.func_175698_g(blockposition);
            return false;
        }
    }

    public AxisAlignedBB func_185496_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        switch ((BlockLever.EnumOrientation) iblockdata.func_177229_b(BlockLever.field_176360_a)) {
        case EAST:
        default:
            return BlockLever.field_185695_f;

        case WEST:
            return BlockLever.field_185694_e;

        case SOUTH:
            return BlockLever.field_185693_d;

        case NORTH:
            return BlockLever.field_185692_c;

        case UP_Z:
        case UP_X:
            return BlockLever.field_185696_g;

        case DOWN_X:
        case DOWN_Z:
            return BlockLever.field_185691_B;
        }
    }

    public boolean func_180639_a(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        if (world.field_72995_K) {
            return true;
        } else {
            // CraftBukkit start - Interact Lever
            boolean powered = iblockdata.func_177229_b(field_176359_b);
            org.bukkit.block.Block block = world.getWorld().getBlockAt(blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p());
            int old = (powered) ? 15 : 0;
            int current = (!powered) ? 15 : 0;

            BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(block, old, current);
            world.getServer().getPluginManager().callEvent(eventRedstone);

            if ((eventRedstone.getNewCurrent() > 0) != (!powered)) {
                return true;
            }
            // CraftBukkit end

            iblockdata = iblockdata.func_177231_a((IProperty) BlockLever.field_176359_b);
            world.func_180501_a(blockposition, iblockdata, 3);
            float f3 = ((Boolean) iblockdata.func_177229_b(BlockLever.field_176359_b)).booleanValue() ? 0.6F : 0.5F;

            world.func_184133_a((EntityPlayer) null, blockposition, SoundEvents.field_187750_dc, SoundCategory.BLOCKS, 0.3F, f3);
            world.func_175685_c(blockposition, this, false);
            EnumFacing enumdirection1 = ((BlockLever.EnumOrientation) iblockdata.func_177229_b(BlockLever.field_176360_a)).func_176852_c();

            world.func_175685_c(blockposition.func_177972_a(enumdirection1.func_176734_d()), this, false);
            return true;
        }
    }

    public void func_180663_b(World world, BlockPos blockposition, IBlockState iblockdata) {
        if (((Boolean) iblockdata.func_177229_b(BlockLever.field_176359_b)).booleanValue()) {
            world.func_175685_c(blockposition, this, false);
            EnumFacing enumdirection = ((BlockLever.EnumOrientation) iblockdata.func_177229_b(BlockLever.field_176360_a)).func_176852_c();

            world.func_175685_c(blockposition.func_177972_a(enumdirection.func_176734_d()), this, false);
        }

        super.func_180663_b(world, blockposition, iblockdata);
    }

    public int func_180656_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition, EnumFacing enumdirection) {
        return ((Boolean) iblockdata.func_177229_b(BlockLever.field_176359_b)).booleanValue() ? 15 : 0;
    }

    public int func_176211_b(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition, EnumFacing enumdirection) {
        return !((Boolean) iblockdata.func_177229_b(BlockLever.field_176359_b)).booleanValue() ? 0 : (((BlockLever.EnumOrientation) iblockdata.func_177229_b(BlockLever.field_176360_a)).func_176852_c() == enumdirection ? 15 : 0);
    }

    public boolean func_149744_f(IBlockState iblockdata) {
        return true;
    }

    public IBlockState func_176203_a(int i) {
        return this.func_176223_P().func_177226_a(BlockLever.field_176360_a, BlockLever.EnumOrientation.func_176853_a(i & 7)).func_177226_a(BlockLever.field_176359_b, Boolean.valueOf((i & 8) > 0));
    }

    public int func_176201_c(IBlockState iblockdata) {
        byte b0 = 0;
        int i = b0 | ((BlockLever.EnumOrientation) iblockdata.func_177229_b(BlockLever.field_176360_a)).func_176855_a();

        if (((Boolean) iblockdata.func_177229_b(BlockLever.field_176359_b)).booleanValue()) {
            i |= 8;
        }

        return i;
    }

    public IBlockState func_185499_a(IBlockState iblockdata, Rotation enumblockrotation) {
        switch (enumblockrotation) {
        case CLOCKWISE_180:
            switch ((BlockLever.EnumOrientation) iblockdata.func_177229_b(BlockLever.field_176360_a)) {
            case EAST:
                return iblockdata.func_177226_a(BlockLever.field_176360_a, BlockLever.EnumOrientation.WEST);

            case WEST:
                return iblockdata.func_177226_a(BlockLever.field_176360_a, BlockLever.EnumOrientation.EAST);

            case SOUTH:
                return iblockdata.func_177226_a(BlockLever.field_176360_a, BlockLever.EnumOrientation.NORTH);

            case NORTH:
                return iblockdata.func_177226_a(BlockLever.field_176360_a, BlockLever.EnumOrientation.SOUTH);

            default:
                return iblockdata;
            }

        case COUNTERCLOCKWISE_90:
            switch ((BlockLever.EnumOrientation) iblockdata.func_177229_b(BlockLever.field_176360_a)) {
            case EAST:
                return iblockdata.func_177226_a(BlockLever.field_176360_a, BlockLever.EnumOrientation.NORTH);

            case WEST:
                return iblockdata.func_177226_a(BlockLever.field_176360_a, BlockLever.EnumOrientation.SOUTH);

            case SOUTH:
                return iblockdata.func_177226_a(BlockLever.field_176360_a, BlockLever.EnumOrientation.EAST);

            case NORTH:
                return iblockdata.func_177226_a(BlockLever.field_176360_a, BlockLever.EnumOrientation.WEST);

            case UP_Z:
                return iblockdata.func_177226_a(BlockLever.field_176360_a, BlockLever.EnumOrientation.UP_X);

            case UP_X:
                return iblockdata.func_177226_a(BlockLever.field_176360_a, BlockLever.EnumOrientation.UP_Z);

            case DOWN_X:
                return iblockdata.func_177226_a(BlockLever.field_176360_a, BlockLever.EnumOrientation.DOWN_Z);

            case DOWN_Z:
                return iblockdata.func_177226_a(BlockLever.field_176360_a, BlockLever.EnumOrientation.DOWN_X);
            }

        case CLOCKWISE_90:
            switch ((BlockLever.EnumOrientation) iblockdata.func_177229_b(BlockLever.field_176360_a)) {
            case EAST:
                return iblockdata.func_177226_a(BlockLever.field_176360_a, BlockLever.EnumOrientation.SOUTH);

            case WEST:
                return iblockdata.func_177226_a(BlockLever.field_176360_a, BlockLever.EnumOrientation.NORTH);

            case SOUTH:
                return iblockdata.func_177226_a(BlockLever.field_176360_a, BlockLever.EnumOrientation.WEST);

            case NORTH:
                return iblockdata.func_177226_a(BlockLever.field_176360_a, BlockLever.EnumOrientation.EAST);

            case UP_Z:
                return iblockdata.func_177226_a(BlockLever.field_176360_a, BlockLever.EnumOrientation.UP_X);

            case UP_X:
                return iblockdata.func_177226_a(BlockLever.field_176360_a, BlockLever.EnumOrientation.UP_Z);

            case DOWN_X:
                return iblockdata.func_177226_a(BlockLever.field_176360_a, BlockLever.EnumOrientation.DOWN_Z);

            case DOWN_Z:
                return iblockdata.func_177226_a(BlockLever.field_176360_a, BlockLever.EnumOrientation.DOWN_X);
            }

        default:
            return iblockdata;
        }
    }

    public IBlockState func_185471_a(IBlockState iblockdata, Mirror enumblockmirror) {
        return iblockdata.func_185907_a(enumblockmirror.func_185800_a(((BlockLever.EnumOrientation) iblockdata.func_177229_b(BlockLever.field_176360_a)).func_176852_c()));
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockLever.field_176360_a, BlockLever.field_176359_b});
    }

    public BlockFaceShape func_193383_a(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return BlockFaceShape.UNDEFINED;
    }

    public static enum EnumOrientation implements IStringSerializable {

        DOWN_X(0, "down_x", EnumFacing.DOWN), EAST(1, "east", EnumFacing.EAST), WEST(2, "west", EnumFacing.WEST), SOUTH(3, "south", EnumFacing.SOUTH), NORTH(4, "north", EnumFacing.NORTH), UP_Z(5, "up_z", EnumFacing.UP), UP_X(6, "up_x", EnumFacing.UP), DOWN_Z(7, "down_z", EnumFacing.DOWN);

        private static final BlockLever.EnumOrientation[] field_176869_i = new BlockLever.EnumOrientation[values().length];
        private final int field_176866_j;
        private final String field_176867_k;
        private final EnumFacing field_176864_l;

        private EnumOrientation(int i, String s, EnumFacing enumdirection) {
            this.field_176866_j = i;
            this.field_176867_k = s;
            this.field_176864_l = enumdirection;
        }

        public int func_176855_a() {
            return this.field_176866_j;
        }

        public EnumFacing func_176852_c() {
            return this.field_176864_l;
        }

        public String toString() {
            return this.field_176867_k;
        }

        public static BlockLever.EnumOrientation func_176853_a(int i) {
            if (i < 0 || i >= BlockLever.EnumOrientation.field_176869_i.length) {
                i = 0;
            }

            return BlockLever.EnumOrientation.field_176869_i[i];
        }

        public static BlockLever.EnumOrientation func_176856_a(EnumFacing enumdirection, EnumFacing enumdirection1) {
            switch (enumdirection) {
            case DOWN:
                switch (enumdirection1.func_176740_k()) {
                case X:
                    return BlockLever.EnumOrientation.DOWN_X;

                case Z:
                    return BlockLever.EnumOrientation.DOWN_Z;

                default:
                    throw new IllegalArgumentException("Invalid entityFacing " + enumdirection1 + " for facing " + enumdirection);
                }

            case UP:
                switch (enumdirection1.func_176740_k()) {
                case X:
                    return BlockLever.EnumOrientation.UP_X;

                case Z:
                    return BlockLever.EnumOrientation.UP_Z;

                default:
                    throw new IllegalArgumentException("Invalid entityFacing " + enumdirection1 + " for facing " + enumdirection);
                }

            case NORTH:
                return BlockLever.EnumOrientation.NORTH;

            case SOUTH:
                return BlockLever.EnumOrientation.SOUTH;

            case WEST:
                return BlockLever.EnumOrientation.WEST;

            case EAST:
                return BlockLever.EnumOrientation.EAST;

            default:
                throw new IllegalArgumentException("Invalid facing: " + enumdirection);
            }
        }

        public String func_176610_l() {
            return this.field_176867_k;
        }

        static {
            BlockLever.EnumOrientation[] ablocklever_enumleverposition = values();
            int i = ablocklever_enumleverposition.length;

            for (int j = 0; j < i; ++j) {
                BlockLever.EnumOrientation blocklever_enumleverposition = ablocklever_enumleverposition[j];

                BlockLever.EnumOrientation.field_176869_i[blocklever_enumleverposition.func_176855_a()] = blocklever_enumleverposition;
            }

        }
    }
}
