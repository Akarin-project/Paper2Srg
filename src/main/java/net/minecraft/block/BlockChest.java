package net.minecraft.block;

import java.util.Iterator;
import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.World;

public class BlockChest extends BlockContainer {

    public static final PropertyDirection field_176459_a = BlockHorizontal.field_185512_D;
    protected static final AxisAlignedBB field_185557_b = new AxisAlignedBB(0.0625D, 0.0D, 0.0D, 0.9375D, 0.875D, 0.9375D);
    protected static final AxisAlignedBB field_185558_c = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.875D, 1.0D);
    protected static final AxisAlignedBB field_185559_d = new AxisAlignedBB(0.0D, 0.0D, 0.0625D, 0.9375D, 0.875D, 0.9375D);
    protected static final AxisAlignedBB field_185560_e = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 1.0D, 0.875D, 0.9375D);
    protected static final AxisAlignedBB field_185561_f = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.875D, 0.9375D);
    public final BlockChest.Type field_149956_a;

    protected BlockChest(BlockChest.Type blockchest_type) {
        super(Material.field_151575_d);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockChest.field_176459_a, EnumFacing.NORTH));
        this.field_149956_a = blockchest_type;
        this.func_149647_a(blockchest_type == BlockChest.Type.TRAP ? CreativeTabs.field_78028_d : CreativeTabs.field_78031_c);
    }

    public boolean func_149662_c(IBlockState iblockdata) {
        return false;
    }

    public boolean func_149686_d(IBlockState iblockdata) {
        return false;
    }

    public EnumBlockRenderType func_149645_b(IBlockState iblockdata) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    public AxisAlignedBB func_185496_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return iblockaccess.func_180495_p(blockposition.func_177978_c()).func_177230_c() == this ? BlockChest.field_185557_b : (iblockaccess.func_180495_p(blockposition.func_177968_d()).func_177230_c() == this ? BlockChest.field_185558_c : (iblockaccess.func_180495_p(blockposition.func_177976_e()).func_177230_c() == this ? BlockChest.field_185559_d : (iblockaccess.func_180495_p(blockposition.func_177974_f()).func_177230_c() == this ? BlockChest.field_185560_e : BlockChest.field_185561_f)));
    }

    public void func_176213_c(World world, BlockPos blockposition, IBlockState iblockdata) {
        this.func_176455_e(world, blockposition, iblockdata);
        Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();

        while (iterator.hasNext()) {
            EnumFacing enumdirection = (EnumFacing) iterator.next();
            BlockPos blockposition1 = blockposition.func_177972_a(enumdirection);
            IBlockState iblockdata1 = world.func_180495_p(blockposition1);

            if (iblockdata1.func_177230_c() == this) {
                this.func_176455_e(world, blockposition1, iblockdata1);
            }
        }

    }

    public IBlockState func_180642_a(World world, BlockPos blockposition, EnumFacing enumdirection, float f, float f1, float f2, int i, EntityLivingBase entityliving) {
        return this.func_176223_P().func_177226_a(BlockChest.field_176459_a, entityliving.func_174811_aO());
    }

    public void func_180633_a(World world, BlockPos blockposition, IBlockState iblockdata, EntityLivingBase entityliving, ItemStack itemstack) {
        EnumFacing enumdirection = EnumFacing.func_176731_b(MathHelper.func_76128_c((double) (entityliving.field_70177_z * 4.0F / 360.0F) + 0.5D) & 3).func_176734_d();

        iblockdata = iblockdata.func_177226_a(BlockChest.field_176459_a, enumdirection);
        BlockPos blockposition1 = blockposition.func_177978_c();
        BlockPos blockposition2 = blockposition.func_177968_d();
        BlockPos blockposition3 = blockposition.func_177976_e();
        BlockPos blockposition4 = blockposition.func_177974_f();
        boolean flag = this == world.func_180495_p(blockposition1).func_177230_c();
        boolean flag1 = this == world.func_180495_p(blockposition2).func_177230_c();
        boolean flag2 = this == world.func_180495_p(blockposition3).func_177230_c();
        boolean flag3 = this == world.func_180495_p(blockposition4).func_177230_c();

        if (!flag && !flag1 && !flag2 && !flag3) {
            world.func_180501_a(blockposition, iblockdata, 3);
        } else if (enumdirection.func_176740_k() == EnumFacing.Axis.X && (flag || flag1)) {
            if (flag) {
                world.func_180501_a(blockposition1, iblockdata, 3);
            } else {
                world.func_180501_a(blockposition2, iblockdata, 3);
            }

            world.func_180501_a(blockposition, iblockdata, 3);
        } else if (enumdirection.func_176740_k() == EnumFacing.Axis.Z && (flag2 || flag3)) {
            if (flag2) {
                world.func_180501_a(blockposition3, iblockdata, 3);
            } else {
                world.func_180501_a(blockposition4, iblockdata, 3);
            }

            world.func_180501_a(blockposition, iblockdata, 3);
        }

        if (itemstack.func_82837_s()) {
            TileEntity tileentity = world.func_175625_s(blockposition);

            if (tileentity instanceof TileEntityChest) {
                ((TileEntityChest) tileentity).func_190575_a(itemstack.func_82833_r());
            }
        }

    }

    public IBlockState func_176455_e(World world, BlockPos blockposition, IBlockState iblockdata) {
        if (world.field_72995_K) {
            return iblockdata;
        } else {
            IBlockState iblockdata1 = world.func_180495_p(blockposition.func_177978_c());
            IBlockState iblockdata2 = world.func_180495_p(blockposition.func_177968_d());
            IBlockState iblockdata3 = world.func_180495_p(blockposition.func_177976_e());
            IBlockState iblockdata4 = world.func_180495_p(blockposition.func_177974_f());
            EnumFacing enumdirection = (EnumFacing) iblockdata.func_177229_b(BlockChest.field_176459_a);

            if (iblockdata1.func_177230_c() != this && iblockdata2.func_177230_c() != this) {
                boolean flag = iblockdata1.func_185913_b();
                boolean flag1 = iblockdata2.func_185913_b();

                if (iblockdata3.func_177230_c() == this || iblockdata4.func_177230_c() == this) {
                    BlockPos blockposition1 = iblockdata3.func_177230_c() == this ? blockposition.func_177976_e() : blockposition.func_177974_f();
                    IBlockState iblockdata5 = world.func_180495_p(blockposition1.func_177978_c());
                    IBlockState iblockdata6 = world.func_180495_p(blockposition1.func_177968_d());

                    enumdirection = EnumFacing.SOUTH;
                    EnumFacing enumdirection1;

                    if (iblockdata3.func_177230_c() == this) {
                        enumdirection1 = (EnumFacing) iblockdata3.func_177229_b(BlockChest.field_176459_a);
                    } else {
                        enumdirection1 = (EnumFacing) iblockdata4.func_177229_b(BlockChest.field_176459_a);
                    }

                    if (enumdirection1 == EnumFacing.NORTH) {
                        enumdirection = EnumFacing.NORTH;
                    }

                    if ((flag || iblockdata5.func_185913_b()) && !flag1 && !iblockdata6.func_185913_b()) {
                        enumdirection = EnumFacing.SOUTH;
                    }

                    if ((flag1 || iblockdata6.func_185913_b()) && !flag && !iblockdata5.func_185913_b()) {
                        enumdirection = EnumFacing.NORTH;
                    }
                }
            } else {
                BlockPos blockposition2 = iblockdata1.func_177230_c() == this ? blockposition.func_177978_c() : blockposition.func_177968_d();
                IBlockState iblockdata7 = world.func_180495_p(blockposition2.func_177976_e());
                IBlockState iblockdata8 = world.func_180495_p(blockposition2.func_177974_f());

                enumdirection = EnumFacing.EAST;
                EnumFacing enumdirection2;

                if (iblockdata1.func_177230_c() == this) {
                    enumdirection2 = (EnumFacing) iblockdata1.func_177229_b(BlockChest.field_176459_a);
                } else {
                    enumdirection2 = (EnumFacing) iblockdata2.func_177229_b(BlockChest.field_176459_a);
                }

                if (enumdirection2 == EnumFacing.WEST) {
                    enumdirection = EnumFacing.WEST;
                }

                if ((iblockdata3.func_185913_b() || iblockdata7.func_185913_b()) && !iblockdata4.func_185913_b() && !iblockdata8.func_185913_b()) {
                    enumdirection = EnumFacing.EAST;
                }

                if ((iblockdata4.func_185913_b() || iblockdata8.func_185913_b()) && !iblockdata3.func_185913_b() && !iblockdata7.func_185913_b()) {
                    enumdirection = EnumFacing.WEST;
                }
            }

            iblockdata = iblockdata.func_177226_a(BlockChest.field_176459_a, enumdirection);
            world.func_180501_a(blockposition, iblockdata, 3);
            return iblockdata;
        }
    }

    public IBlockState func_176458_f(World world, BlockPos blockposition, IBlockState iblockdata) {
        EnumFacing enumdirection = null;
        Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();

        while (iterator.hasNext()) {
            EnumFacing enumdirection1 = (EnumFacing) iterator.next();
            IBlockState iblockdata1 = world.func_180495_p(blockposition.func_177972_a(enumdirection1));

            if (iblockdata1.func_177230_c() == this) {
                return iblockdata;
            }

            if (iblockdata1.func_185913_b()) {
                if (enumdirection != null) {
                    enumdirection = null;
                    break;
                }

                enumdirection = enumdirection1;
            }
        }

        if (enumdirection != null) {
            return iblockdata.func_177226_a(BlockChest.field_176459_a, enumdirection.func_176734_d());
        } else {
            EnumFacing enumdirection2 = (EnumFacing) iblockdata.func_177229_b(BlockChest.field_176459_a);

            if (world.func_180495_p(blockposition.func_177972_a(enumdirection2)).func_185913_b()) {
                enumdirection2 = enumdirection2.func_176734_d();
            }

            if (world.func_180495_p(blockposition.func_177972_a(enumdirection2)).func_185913_b()) {
                enumdirection2 = enumdirection2.func_176746_e();
            }

            if (world.func_180495_p(blockposition.func_177972_a(enumdirection2)).func_185913_b()) {
                enumdirection2 = enumdirection2.func_176734_d();
            }

            return iblockdata.func_177226_a(BlockChest.field_176459_a, enumdirection2);
        }
    }

    public boolean func_176196_c(World world, BlockPos blockposition) {
        int i = 0;
        BlockPos blockposition1 = blockposition.func_177976_e();
        BlockPos blockposition2 = blockposition.func_177974_f();
        BlockPos blockposition3 = blockposition.func_177978_c();
        BlockPos blockposition4 = blockposition.func_177968_d();

        if (world.func_180495_p(blockposition1).func_177230_c() == this) {
            if (this.func_176454_e(world, blockposition1)) {
                return false;
            }

            ++i;
        }

        if (world.func_180495_p(blockposition2).func_177230_c() == this) {
            if (this.func_176454_e(world, blockposition2)) {
                return false;
            }

            ++i;
        }

        if (world.func_180495_p(blockposition3).func_177230_c() == this) {
            if (this.func_176454_e(world, blockposition3)) {
                return false;
            }

            ++i;
        }

        if (world.func_180495_p(blockposition4).func_177230_c() == this) {
            if (this.func_176454_e(world, blockposition4)) {
                return false;
            }

            ++i;
        }

        return i <= 1;
    }

    private boolean func_176454_e(World world, BlockPos blockposition) {
        if (world.func_180495_p(blockposition).func_177230_c() != this) {
            return false;
        } else {
            Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();

            EnumFacing enumdirection;

            do {
                if (!iterator.hasNext()) {
                    return false;
                }

                enumdirection = (EnumFacing) iterator.next();
            } while (world.func_180495_p(blockposition.func_177972_a(enumdirection)).func_177230_c() != this);

            return true;
        }
    }

    public void func_189540_a(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        super.func_189540_a(iblockdata, world, blockposition, block, blockposition1);
        TileEntity tileentity = world.func_175625_s(blockposition);

        if (tileentity instanceof TileEntityChest) {
            tileentity.func_145836_u();
        }

    }

    public void func_180663_b(World world, BlockPos blockposition, IBlockState iblockdata) {
        TileEntity tileentity = world.func_175625_s(blockposition);

        if (tileentity instanceof IInventory) {
            InventoryHelper.func_180175_a(world, blockposition, (IInventory) tileentity);
            world.func_175666_e(blockposition, this);
        }

        super.func_180663_b(world, blockposition, iblockdata);
    }

    public boolean func_180639_a(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        if (world.field_72995_K) {
            return true;
        } else {
            ILockableContainer itileinventory = this.func_180676_d(world, blockposition);

            if (itileinventory != null) {
                entityhuman.func_71007_a(itileinventory);
                if (this.field_149956_a == BlockChest.Type.BASIC) {
                    entityhuman.func_71029_a(StatList.field_188063_ac);
                } else if (this.field_149956_a == BlockChest.Type.TRAP) {
                    entityhuman.func_71029_a(StatList.field_188089_W);
                }
            }

            return true;
        }
    }

    @Nullable
    public ILockableContainer func_180676_d(World world, BlockPos blockposition) {
        return this.func_189418_a(world, blockposition, false);
    }

    @Nullable
    public ILockableContainer func_189418_a(World world, BlockPos blockposition, boolean flag) {
        TileEntity tileentity = world.func_175625_s(blockposition);

        if (!(tileentity instanceof TileEntityChest)) {
            return null;
        } else {
            Object object = (TileEntityChest) tileentity;

            if (!flag && this.func_176457_m(world, blockposition)) {
                return null;
            } else {
                Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();

                while (iterator.hasNext()) {
                    EnumFacing enumdirection = (EnumFacing) iterator.next();
                    BlockPos blockposition1 = blockposition.func_177972_a(enumdirection);
                    // Paper start - don't load chunks if the other side of the chest is in unloaded chunk
                    final IBlockState type = world.getTypeIfLoaded(blockposition1); // Paper
                    if (type ==  null) {
                        continue;
                    }
                    Block block = type.func_177230_c();
                    // Paper end

                    if (block == this) {
                        if (!flag && this.func_176457_m(world, blockposition1)) { // Paper - check for allowBlocked flag - MC-99321
                            return null;
                        }

                        TileEntity tileentity1 = world.func_175625_s(blockposition1);

                        if (tileentity1 instanceof TileEntityChest) {
                            if (enumdirection != EnumFacing.WEST && enumdirection != EnumFacing.NORTH) {
                                object = new InventoryLargeChest("container.chestDouble", (ILockableContainer) object, (TileEntityChest) tileentity1);
                            } else {
                                object = new InventoryLargeChest("container.chestDouble", (TileEntityChest) tileentity1, (ILockableContainer) object);
                            }
                        }
                    }
                }

                return (ILockableContainer) object;
            }
        }
    }

    public TileEntity func_149915_a(World world, int i) {
        return new TileEntityChest();
    }

    public boolean func_149744_f(IBlockState iblockdata) {
        return this.field_149956_a == BlockChest.Type.TRAP;
    }

    public int func_180656_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition, EnumFacing enumdirection) {
        if (!iblockdata.func_185897_m()) {
            return 0;
        } else {
            int i = 0;
            TileEntity tileentity = iblockaccess.func_175625_s(blockposition);

            if (tileentity instanceof TileEntityChest) {
                i = ((TileEntityChest) tileentity).field_145987_o;
            }

            return MathHelper.func_76125_a(i, 0, 15);
        }
    }

    public int func_176211_b(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition, EnumFacing enumdirection) {
        return enumdirection == EnumFacing.UP ? iblockdata.func_185911_a(iblockaccess, blockposition, enumdirection) : 0;
    }

    private boolean func_176457_m(World world, BlockPos blockposition) {
        return this.func_176456_n(world, blockposition) || this.func_176453_o(world, blockposition);
    }

    private boolean func_176456_n(World world, BlockPos blockposition) {
        return world.func_180495_p(blockposition.func_177984_a()).func_185915_l();
    }

    private boolean func_176453_o(World world, BlockPos blockposition) {
        // Paper start - Option ti dsiable chest cat detection
        if (world.paperConfig.disableChestCatDetection) {
            return false;
        }
        // Paper end
        Iterator iterator = world.func_72872_a(EntityOcelot.class, new AxisAlignedBB((double) blockposition.func_177958_n(), (double) (blockposition.func_177956_o() + 1), (double) blockposition.func_177952_p(), (double) (blockposition.func_177958_n() + 1), (double) (blockposition.func_177956_o() + 2), (double) (blockposition.func_177952_p() + 1))).iterator();

        EntityOcelot entityocelot;

        do {
            if (!iterator.hasNext()) {
                return false;
            }

            Entity entity = (Entity) iterator.next();

            entityocelot = (EntityOcelot) entity;
        } while (!entityocelot.func_70906_o());

        return true;
    }

    public boolean func_149740_M(IBlockState iblockdata) {
        return true;
    }

    public int func_180641_l(IBlockState iblockdata, World world, BlockPos blockposition) {
        return Container.func_94526_b((IInventory) this.func_180676_d(world, blockposition));
    }

    public IBlockState func_176203_a(int i) {
        EnumFacing enumdirection = EnumFacing.func_82600_a(i);

        if (enumdirection.func_176740_k() == EnumFacing.Axis.Y) {
            enumdirection = EnumFacing.NORTH;
        }

        return this.func_176223_P().func_177226_a(BlockChest.field_176459_a, enumdirection);
    }

    public int func_176201_c(IBlockState iblockdata) {
        return ((EnumFacing) iblockdata.func_177229_b(BlockChest.field_176459_a)).func_176745_a();
    }

    public IBlockState func_185499_a(IBlockState iblockdata, Rotation enumblockrotation) {
        return iblockdata.func_177226_a(BlockChest.field_176459_a, enumblockrotation.func_185831_a((EnumFacing) iblockdata.func_177229_b(BlockChest.field_176459_a)));
    }

    public IBlockState func_185471_a(IBlockState iblockdata, Mirror enumblockmirror) {
        return iblockdata.func_185907_a(enumblockmirror.func_185800_a((EnumFacing) iblockdata.func_177229_b(BlockChest.field_176459_a)));
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockChest.field_176459_a});
    }

    public BlockFaceShape func_193383_a(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return BlockFaceShape.UNDEFINED;
    }

    public static enum Type {

        BASIC, TRAP;

        private Type() {}
    }
}
