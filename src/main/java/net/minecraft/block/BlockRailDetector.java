package net.minecraft.block;

import com.google.common.base.Predicate;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;


import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartCommandBlock;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.bukkit.event.block.BlockRedstoneEvent;

public class BlockRailDetector extends BlockRailBase {

    public static final PropertyEnum<BlockRailBase.EnumRailDirection> field_176573_b = PropertyEnum.func_177708_a("shape", BlockRailBase.EnumRailDirection.class, new Predicate() {
        public boolean a(@Nullable BlockRailBase.EnumRailDirection blockminecarttrackabstract_enumtrackposition) {
            return blockminecarttrackabstract_enumtrackposition != BlockRailBase.EnumRailDirection.NORTH_EAST && blockminecarttrackabstract_enumtrackposition != BlockRailBase.EnumRailDirection.NORTH_WEST && blockminecarttrackabstract_enumtrackposition != BlockRailBase.EnumRailDirection.SOUTH_EAST && blockminecarttrackabstract_enumtrackposition != BlockRailBase.EnumRailDirection.SOUTH_WEST;
        }

        public boolean apply(@Nullable Object object) {
            return this.a((BlockRailBase.EnumRailDirection) object);
        }
    });
    public static final PropertyBool field_176574_M = PropertyBool.func_177716_a("powered");

    public BlockRailDetector() {
        super(true);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockRailDetector.field_176574_M, Boolean.valueOf(false)).func_177226_a(BlockRailDetector.field_176573_b, BlockRailBase.EnumRailDirection.NORTH_SOUTH));
        this.func_149675_a(true);
    }

    public int func_149738_a(World world) {
        return 20;
    }

    public boolean func_149744_f(IBlockState iblockdata) {
        return true;
    }

    public void func_180634_a(World world, BlockPos blockposition, IBlockState iblockdata, Entity entity) {
        if (!world.field_72995_K) {
            if (!((Boolean) iblockdata.func_177229_b(BlockRailDetector.field_176574_M)).booleanValue()) {
                this.func_176570_e(world, blockposition, iblockdata);
            }
        }
    }

    public void func_180645_a(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {}

    public void func_180650_b(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        if (!world.field_72995_K && ((Boolean) iblockdata.func_177229_b(BlockRailDetector.field_176574_M)).booleanValue()) {
            this.func_176570_e(world, blockposition, iblockdata);
        }
    }

    public int func_180656_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition, EnumFacing enumdirection) {
        return ((Boolean) iblockdata.func_177229_b(BlockRailDetector.field_176574_M)).booleanValue() ? 15 : 0;
    }

    public int func_176211_b(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition, EnumFacing enumdirection) {
        return !((Boolean) iblockdata.func_177229_b(BlockRailDetector.field_176574_M)).booleanValue() ? 0 : (enumdirection == EnumFacing.UP ? 15 : 0);
    }

    private void func_176570_e(World world, BlockPos blockposition, IBlockState iblockdata) {
        boolean flag = ((Boolean) iblockdata.func_177229_b(BlockRailDetector.field_176574_M)).booleanValue();
        boolean flag1 = false;
        List list = this.func_176571_a(world, blockposition, EntityMinecart.class, new Predicate[0]);

        if (!list.isEmpty()) {
            flag1 = true;
        }

        // CraftBukkit start
        if (flag != flag1) {
            org.bukkit.block.Block block = world.getWorld().getBlockAt(blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p());

            BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(block, flag ? 15 : 0, flag1 ? 15 : 0);
            world.getServer().getPluginManager().callEvent(eventRedstone);

            flag1 = eventRedstone.getNewCurrent() > 0;
        }
        // CraftBukkit end

        if (flag1 && !flag) {
            world.func_180501_a(blockposition, iblockdata.func_177226_a(BlockRailDetector.field_176574_M, Boolean.valueOf(true)), 3);
            this.func_185592_b(world, blockposition, iblockdata, true);
            world.func_175685_c(blockposition, this, false);
            world.func_175685_c(blockposition.func_177977_b(), this, false);
            world.func_175704_b(blockposition, blockposition);
        }

        if (!flag1 && flag) {
            world.func_180501_a(blockposition, iblockdata.func_177226_a(BlockRailDetector.field_176574_M, Boolean.valueOf(false)), 3);
            this.func_185592_b(world, blockposition, iblockdata, false);
            world.func_175685_c(blockposition, this, false);
            world.func_175685_c(blockposition.func_177977_b(), this, false);
            world.func_175704_b(blockposition, blockposition);
        }

        if (flag1) {
            world.func_175684_a(new BlockPos(blockposition), (Block) this, this.func_149738_a(world));
        }

        world.func_175666_e(blockposition, this);
    }

    protected void func_185592_b(World world, BlockPos blockposition, IBlockState iblockdata, boolean flag) {
        BlockRailBase.Rail blockminecarttrackabstract_minecarttracklogic = new BlockRailBase.Rail(world, blockposition, iblockdata);
        List list = blockminecarttrackabstract_minecarttracklogic.func_185763_a();
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            BlockPos blockposition1 = (BlockPos) iterator.next();
            IBlockState iblockdata1 = world.func_180495_p(blockposition1);

            if (iblockdata1 != null) {
                iblockdata1.func_189546_a(world, blockposition1, iblockdata1.func_177230_c(), blockposition);
            }
        }

    }

    public void func_176213_c(World world, BlockPos blockposition, IBlockState iblockdata) {
        super.func_176213_c(world, blockposition, iblockdata);
        this.func_176570_e(world, blockposition, iblockdata);
    }

    public IProperty<BlockRailBase.EnumRailDirection> func_176560_l() {
        return BlockRailDetector.field_176573_b;
    }

    public boolean func_149740_M(IBlockState iblockdata) {
        return true;
    }

    public int func_180641_l(IBlockState iblockdata, World world, BlockPos blockposition) {
        if (((Boolean) iblockdata.func_177229_b(BlockRailDetector.field_176574_M)).booleanValue()) {
            List list = this.func_176571_a(world, blockposition, EntityMinecartCommandBlock.class, new Predicate[0]);

            if (!list.isEmpty()) {
                return ((EntityMinecartCommandBlock) list.get(0)).func_145822_e().func_145760_g();
            }

            List list1 = this.func_176571_a(world, blockposition, EntityMinecart.class, new Predicate[] { EntitySelectors.field_96566_b});

            if (!list1.isEmpty()) {
                return Container.func_94526_b((IInventory) list1.get(0));
            }
        }

        return 0;
    }

    protected <T extends EntityMinecart> List<T> func_176571_a(World world, BlockPos blockposition, Class<T> oclass, Predicate<Entity>... apredicate) {
        AxisAlignedBB axisalignedbb = this.func_176572_a(blockposition);

        return apredicate.length != 1 ? world.func_72872_a(oclass, axisalignedbb) : world.func_175647_a(oclass, axisalignedbb, apredicate[0]);
    }

    private AxisAlignedBB func_176572_a(BlockPos blockposition) {
        float f = 0.2F;

        return new AxisAlignedBB((double) ((float) blockposition.func_177958_n() + 0.2F), (double) blockposition.func_177956_o(), (double) ((float) blockposition.func_177952_p() + 0.2F), (double) ((float) (blockposition.func_177958_n() + 1) - 0.2F), (double) ((float) (blockposition.func_177956_o() + 1) - 0.2F), (double) ((float) (blockposition.func_177952_p() + 1) - 0.2F));
    }

    public IBlockState func_176203_a(int i) {
        return this.func_176223_P().func_177226_a(BlockRailDetector.field_176573_b, BlockRailBase.EnumRailDirection.func_177016_a(i & 7)).func_177226_a(BlockRailDetector.field_176574_M, Boolean.valueOf((i & 8) > 0));
    }

    public int func_176201_c(IBlockState iblockdata) {
        byte b0 = 0;
        int i = b0 | ((BlockRailBase.EnumRailDirection) iblockdata.func_177229_b(BlockRailDetector.field_176573_b)).func_177015_a();

        if (((Boolean) iblockdata.func_177229_b(BlockRailDetector.field_176574_M)).booleanValue()) {
            i |= 8;
        }

        return i;
    }

    public IBlockState func_185499_a(IBlockState iblockdata, Rotation enumblockrotation) {
        switch (enumblockrotation) {
        case CLOCKWISE_180:
            switch ((BlockRailBase.EnumRailDirection) iblockdata.func_177229_b(BlockRailDetector.field_176573_b)) {
            case ASCENDING_EAST:
                return iblockdata.func_177226_a(BlockRailDetector.field_176573_b, BlockRailBase.EnumRailDirection.ASCENDING_WEST);

            case ASCENDING_WEST:
                return iblockdata.func_177226_a(BlockRailDetector.field_176573_b, BlockRailBase.EnumRailDirection.ASCENDING_EAST);

            case ASCENDING_NORTH:
                return iblockdata.func_177226_a(BlockRailDetector.field_176573_b, BlockRailBase.EnumRailDirection.ASCENDING_SOUTH);

            case ASCENDING_SOUTH:
                return iblockdata.func_177226_a(BlockRailDetector.field_176573_b, BlockRailBase.EnumRailDirection.ASCENDING_NORTH);

            case SOUTH_EAST:
                return iblockdata.func_177226_a(BlockRailDetector.field_176573_b, BlockRailBase.EnumRailDirection.NORTH_WEST);

            case SOUTH_WEST:
                return iblockdata.func_177226_a(BlockRailDetector.field_176573_b, BlockRailBase.EnumRailDirection.NORTH_EAST);

            case NORTH_WEST:
                return iblockdata.func_177226_a(BlockRailDetector.field_176573_b, BlockRailBase.EnumRailDirection.SOUTH_EAST);

            case NORTH_EAST:
                return iblockdata.func_177226_a(BlockRailDetector.field_176573_b, BlockRailBase.EnumRailDirection.SOUTH_WEST);
            }

        case COUNTERCLOCKWISE_90:
            switch ((BlockRailBase.EnumRailDirection) iblockdata.func_177229_b(BlockRailDetector.field_176573_b)) {
            case ASCENDING_EAST:
                return iblockdata.func_177226_a(BlockRailDetector.field_176573_b, BlockRailBase.EnumRailDirection.ASCENDING_NORTH);

            case ASCENDING_WEST:
                return iblockdata.func_177226_a(BlockRailDetector.field_176573_b, BlockRailBase.EnumRailDirection.ASCENDING_SOUTH);

            case ASCENDING_NORTH:
                return iblockdata.func_177226_a(BlockRailDetector.field_176573_b, BlockRailBase.EnumRailDirection.ASCENDING_WEST);

            case ASCENDING_SOUTH:
                return iblockdata.func_177226_a(BlockRailDetector.field_176573_b, BlockRailBase.EnumRailDirection.ASCENDING_EAST);

            case SOUTH_EAST:
                return iblockdata.func_177226_a(BlockRailDetector.field_176573_b, BlockRailBase.EnumRailDirection.NORTH_EAST);

            case SOUTH_WEST:
                return iblockdata.func_177226_a(BlockRailDetector.field_176573_b, BlockRailBase.EnumRailDirection.SOUTH_EAST);

            case NORTH_WEST:
                return iblockdata.func_177226_a(BlockRailDetector.field_176573_b, BlockRailBase.EnumRailDirection.SOUTH_WEST);

            case NORTH_EAST:
                return iblockdata.func_177226_a(BlockRailDetector.field_176573_b, BlockRailBase.EnumRailDirection.NORTH_WEST);

            case NORTH_SOUTH:
                return iblockdata.func_177226_a(BlockRailDetector.field_176573_b, BlockRailBase.EnumRailDirection.EAST_WEST);

            case EAST_WEST:
                return iblockdata.func_177226_a(BlockRailDetector.field_176573_b, BlockRailBase.EnumRailDirection.NORTH_SOUTH);
            }

        case CLOCKWISE_90:
            switch ((BlockRailBase.EnumRailDirection) iblockdata.func_177229_b(BlockRailDetector.field_176573_b)) {
            case ASCENDING_EAST:
                return iblockdata.func_177226_a(BlockRailDetector.field_176573_b, BlockRailBase.EnumRailDirection.ASCENDING_SOUTH);

            case ASCENDING_WEST:
                return iblockdata.func_177226_a(BlockRailDetector.field_176573_b, BlockRailBase.EnumRailDirection.ASCENDING_NORTH);

            case ASCENDING_NORTH:
                return iblockdata.func_177226_a(BlockRailDetector.field_176573_b, BlockRailBase.EnumRailDirection.ASCENDING_EAST);

            case ASCENDING_SOUTH:
                return iblockdata.func_177226_a(BlockRailDetector.field_176573_b, BlockRailBase.EnumRailDirection.ASCENDING_WEST);

            case SOUTH_EAST:
                return iblockdata.func_177226_a(BlockRailDetector.field_176573_b, BlockRailBase.EnumRailDirection.SOUTH_WEST);

            case SOUTH_WEST:
                return iblockdata.func_177226_a(BlockRailDetector.field_176573_b, BlockRailBase.EnumRailDirection.NORTH_WEST);

            case NORTH_WEST:
                return iblockdata.func_177226_a(BlockRailDetector.field_176573_b, BlockRailBase.EnumRailDirection.NORTH_EAST);

            case NORTH_EAST:
                return iblockdata.func_177226_a(BlockRailDetector.field_176573_b, BlockRailBase.EnumRailDirection.SOUTH_EAST);

            case NORTH_SOUTH:
                return iblockdata.func_177226_a(BlockRailDetector.field_176573_b, BlockRailBase.EnumRailDirection.EAST_WEST);

            case EAST_WEST:
                return iblockdata.func_177226_a(BlockRailDetector.field_176573_b, BlockRailBase.EnumRailDirection.NORTH_SOUTH);
            }

        default:
            return iblockdata;
        }
    }

    public IBlockState func_185471_a(IBlockState iblockdata, Mirror enumblockmirror) {
        BlockRailBase.EnumRailDirection blockminecarttrackabstract_enumtrackposition = (BlockRailBase.EnumRailDirection) iblockdata.func_177229_b(BlockRailDetector.field_176573_b);

        switch (enumblockmirror) {
        case LEFT_RIGHT:
            switch (blockminecarttrackabstract_enumtrackposition) {
            case ASCENDING_NORTH:
                return iblockdata.func_177226_a(BlockRailDetector.field_176573_b, BlockRailBase.EnumRailDirection.ASCENDING_SOUTH);

            case ASCENDING_SOUTH:
                return iblockdata.func_177226_a(BlockRailDetector.field_176573_b, BlockRailBase.EnumRailDirection.ASCENDING_NORTH);

            case SOUTH_EAST:
                return iblockdata.func_177226_a(BlockRailDetector.field_176573_b, BlockRailBase.EnumRailDirection.NORTH_EAST);

            case SOUTH_WEST:
                return iblockdata.func_177226_a(BlockRailDetector.field_176573_b, BlockRailBase.EnumRailDirection.NORTH_WEST);

            case NORTH_WEST:
                return iblockdata.func_177226_a(BlockRailDetector.field_176573_b, BlockRailBase.EnumRailDirection.SOUTH_WEST);

            case NORTH_EAST:
                return iblockdata.func_177226_a(BlockRailDetector.field_176573_b, BlockRailBase.EnumRailDirection.SOUTH_EAST);

            default:
                return super.func_185471_a(iblockdata, enumblockmirror);
            }

        case FRONT_BACK:
            switch (blockminecarttrackabstract_enumtrackposition) {
            case ASCENDING_EAST:
                return iblockdata.func_177226_a(BlockRailDetector.field_176573_b, BlockRailBase.EnumRailDirection.ASCENDING_WEST);

            case ASCENDING_WEST:
                return iblockdata.func_177226_a(BlockRailDetector.field_176573_b, BlockRailBase.EnumRailDirection.ASCENDING_EAST);

            case ASCENDING_NORTH:
            case ASCENDING_SOUTH:
            default:
                break;

            case SOUTH_EAST:
                return iblockdata.func_177226_a(BlockRailDetector.field_176573_b, BlockRailBase.EnumRailDirection.SOUTH_WEST);

            case SOUTH_WEST:
                return iblockdata.func_177226_a(BlockRailDetector.field_176573_b, BlockRailBase.EnumRailDirection.SOUTH_EAST);

            case NORTH_WEST:
                return iblockdata.func_177226_a(BlockRailDetector.field_176573_b, BlockRailBase.EnumRailDirection.NORTH_EAST);

            case NORTH_EAST:
                return iblockdata.func_177226_a(BlockRailDetector.field_176573_b, BlockRailBase.EnumRailDirection.NORTH_WEST);
            }
        }

        return super.func_185471_a(iblockdata, enumblockmirror);
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockRailDetector.field_176573_b, BlockRailDetector.field_176574_M});
    }
}
