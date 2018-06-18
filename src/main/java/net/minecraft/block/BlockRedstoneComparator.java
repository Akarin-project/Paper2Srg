package net.minecraft.block;

import com.google.common.base.Predicate;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityComparator;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRedstoneComparator extends BlockRedstoneDiode implements ITileEntityProvider {

    public static final PropertyBool POWERED = PropertyBool.create("powered");
    public static final PropertyEnum<BlockRedstoneComparator.Mode> MODE = PropertyEnum.create("mode", BlockRedstoneComparator.Mode.class);

    public BlockRedstoneComparator(boolean flag) {
        super(flag);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockRedstoneComparator.FACING, EnumFacing.NORTH).withProperty(BlockRedstoneComparator.POWERED, Boolean.valueOf(false)).withProperty(BlockRedstoneComparator.MODE, BlockRedstoneComparator.Mode.COMPARE));
        this.hasTileEntity = true;
    }

    public String getLocalizedName() {
        return I18n.translateToLocal("item.comparator.name");
    }

    public Item getItemDropped(IBlockState iblockdata, Random random, int i) {
        return Items.COMPARATOR;
    }

    public ItemStack getItem(World world, BlockPos blockposition, IBlockState iblockdata) {
        return new ItemStack(Items.COMPARATOR);
    }

    protected int getDelay(IBlockState iblockdata) {
        return 2;
    }

    protected IBlockState getPoweredState(IBlockState iblockdata) {
        Boolean obool = (Boolean) iblockdata.getValue(BlockRedstoneComparator.POWERED);
        BlockRedstoneComparator.Mode blockredstonecomparator_enumcomparatormode = (BlockRedstoneComparator.Mode) iblockdata.getValue(BlockRedstoneComparator.MODE);
        EnumFacing enumdirection = (EnumFacing) iblockdata.getValue(BlockRedstoneComparator.FACING);

        return Blocks.POWERED_COMPARATOR.getDefaultState().withProperty(BlockRedstoneComparator.FACING, enumdirection).withProperty(BlockRedstoneComparator.POWERED, obool).withProperty(BlockRedstoneComparator.MODE, blockredstonecomparator_enumcomparatormode);
    }

    protected IBlockState getUnpoweredState(IBlockState iblockdata) {
        Boolean obool = (Boolean) iblockdata.getValue(BlockRedstoneComparator.POWERED);
        BlockRedstoneComparator.Mode blockredstonecomparator_enumcomparatormode = (BlockRedstoneComparator.Mode) iblockdata.getValue(BlockRedstoneComparator.MODE);
        EnumFacing enumdirection = (EnumFacing) iblockdata.getValue(BlockRedstoneComparator.FACING);

        return Blocks.UNPOWERED_COMPARATOR.getDefaultState().withProperty(BlockRedstoneComparator.FACING, enumdirection).withProperty(BlockRedstoneComparator.POWERED, obool).withProperty(BlockRedstoneComparator.MODE, blockredstonecomparator_enumcomparatormode);
    }

    protected boolean isPowered(IBlockState iblockdata) {
        return this.isRepeaterPowered || ((Boolean) iblockdata.getValue(BlockRedstoneComparator.POWERED)).booleanValue();
    }

    protected int getActiveSignal(IBlockAccess iblockaccess, BlockPos blockposition, IBlockState iblockdata) {
        TileEntity tileentity = iblockaccess.getTileEntity(blockposition);

        return tileentity instanceof TileEntityComparator ? ((TileEntityComparator) tileentity).getOutputSignal() : 0;
    }

    private int calculateOutput(World world, BlockPos blockposition, IBlockState iblockdata) {
        return iblockdata.getValue(BlockRedstoneComparator.MODE) == BlockRedstoneComparator.Mode.SUBTRACT ? Math.max(this.calculateInputStrength(world, blockposition, iblockdata) - this.getPowerOnSides((IBlockAccess) world, blockposition, iblockdata), 0) : this.calculateInputStrength(world, blockposition, iblockdata);
    }

    protected boolean shouldBePowered(World world, BlockPos blockposition, IBlockState iblockdata) {
        int i = this.calculateInputStrength(world, blockposition, iblockdata);

        if (i >= 15) {
            return true;
        } else if (i == 0) {
            return false;
        } else {
            int j = this.getPowerOnSides((IBlockAccess) world, blockposition, iblockdata);

            return j == 0 ? true : i >= j;
        }
    }

    protected int calculateInputStrength(World world, BlockPos blockposition, IBlockState iblockdata) {
        int i = super.calculateInputStrength(world, blockposition, iblockdata);
        EnumFacing enumdirection = (EnumFacing) iblockdata.getValue(BlockRedstoneComparator.FACING);
        BlockPos blockposition1 = blockposition.offset(enumdirection);
        IBlockState iblockdata1 = world.getBlockState(blockposition1);

        if (iblockdata1.hasComparatorInputOverride()) {
            i = iblockdata1.getComparatorInputOverride(world, blockposition1);
        } else if (i < 15 && iblockdata1.isNormalCube()) {
            blockposition1 = blockposition1.offset(enumdirection);
            iblockdata1 = world.getBlockState(blockposition1);
            if (iblockdata1.hasComparatorInputOverride()) {
                i = iblockdata1.getComparatorInputOverride(world, blockposition1);
            } else if (iblockdata1.getMaterial() == Material.AIR) {
                EntityItemFrame entityitemframe = this.findItemFrame(world, enumdirection, blockposition1);

                if (entityitemframe != null) {
                    i = entityitemframe.getAnalogOutput();
                }
            }
        }

        return i;
    }

    @Nullable
    private EntityItemFrame findItemFrame(World world, final EnumFacing enumdirection, BlockPos blockposition) {
        List list = world.getEntitiesWithinAABB(EntityItemFrame.class, new AxisAlignedBB((double) blockposition.getX(), (double) blockposition.getY(), (double) blockposition.getZ(), (double) (blockposition.getX() + 1), (double) (blockposition.getY() + 1), (double) (blockposition.getZ() + 1)), new Predicate() {
            public boolean a(@Nullable Entity entity) {
                return entity != null && entity.getHorizontalFacing() == enumdirection;
            }

            public boolean apply(@Nullable Object object) {
                return this.a((Entity) object);
            }
        });

        return list.size() == 1 ? (EntityItemFrame) list.get(0) : null;
    }

    public boolean onBlockActivated(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        if (!entityhuman.capabilities.allowEdit) {
            return false;
        } else {
            iblockdata = iblockdata.cycleProperty((IProperty) BlockRedstoneComparator.MODE);
            float f3 = iblockdata.getValue(BlockRedstoneComparator.MODE) == BlockRedstoneComparator.Mode.SUBTRACT ? 0.55F : 0.5F;

            world.playSound(entityhuman, blockposition, SoundEvents.BLOCK_COMPARATOR_CLICK, SoundCategory.BLOCKS, 0.3F, f3);
            world.setBlockState(blockposition, iblockdata, 2);
            this.onStateChange(world, blockposition, iblockdata);
            return true;
        }
    }

    protected void updateState(World world, BlockPos blockposition, IBlockState iblockdata) {
        if (!world.isBlockTickPending(blockposition, (Block) this)) {
            int i = this.calculateOutput(world, blockposition, iblockdata);
            TileEntity tileentity = world.getTileEntity(blockposition);
            int j = tileentity instanceof TileEntityComparator ? ((TileEntityComparator) tileentity).getOutputSignal() : 0;

            if (i != j || this.isPowered(iblockdata) != this.shouldBePowered(world, blockposition, iblockdata)) {
                if (this.isFacingTowardsRepeater(world, blockposition, iblockdata)) {
                    world.updateBlockTick(blockposition, this, 2, -1);
                } else {
                    world.updateBlockTick(blockposition, this, 2, 0);
                }
            }

        }
    }

    private void onStateChange(World world, BlockPos blockposition, IBlockState iblockdata) {
        int i = this.calculateOutput(world, blockposition, iblockdata);
        TileEntity tileentity = world.getTileEntity(blockposition);
        int j = 0;

        if (tileentity instanceof TileEntityComparator) {
            TileEntityComparator tileentitycomparator = (TileEntityComparator) tileentity;

            j = tileentitycomparator.getOutputSignal();
            tileentitycomparator.setOutputSignal(i);
        }

        if (j != i || iblockdata.getValue(BlockRedstoneComparator.MODE) == BlockRedstoneComparator.Mode.COMPARE) {
            boolean flag = this.shouldBePowered(world, blockposition, iblockdata);
            boolean flag1 = this.isPowered(iblockdata);

            if (flag1 && !flag) {
                world.setBlockState(blockposition, iblockdata.withProperty(BlockRedstoneComparator.POWERED, Boolean.valueOf(false)), 2);
            } else if (!flag1 && flag) {
                world.setBlockState(blockposition, iblockdata.withProperty(BlockRedstoneComparator.POWERED, Boolean.valueOf(true)), 2);
            }

            this.notifyNeighbors(world, blockposition, iblockdata);
        }

    }

    public void updateTick(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        if (this.isRepeaterPowered) {
            world.setBlockState(blockposition, this.getUnpoweredState(iblockdata).withProperty(BlockRedstoneComparator.POWERED, Boolean.valueOf(true)), 4);
        }

        this.onStateChange(world, blockposition, iblockdata);
    }

    public void onBlockAdded(World world, BlockPos blockposition, IBlockState iblockdata) {
        super.onBlockAdded(world, blockposition, iblockdata);
        world.setTileEntity(blockposition, this.createNewTileEntity(world, 0));
    }

    public void breakBlock(World world, BlockPos blockposition, IBlockState iblockdata) {
        super.breakBlock(world, blockposition, iblockdata);
        world.removeTileEntity(blockposition);
        this.notifyNeighbors(world, blockposition, iblockdata);
    }

    public boolean eventReceived(IBlockState iblockdata, World world, BlockPos blockposition, int i, int j) {
        super.eventReceived(iblockdata, world, blockposition, i, j);
        TileEntity tileentity = world.getTileEntity(blockposition);

        return tileentity == null ? false : tileentity.receiveClientEvent(i, j);
    }

    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityComparator();
    }

    public IBlockState getStateFromMeta(int i) {
        return this.getDefaultState().withProperty(BlockRedstoneComparator.FACING, EnumFacing.getHorizontal(i)).withProperty(BlockRedstoneComparator.POWERED, Boolean.valueOf((i & 8) > 0)).withProperty(BlockRedstoneComparator.MODE, (i & 4) > 0 ? BlockRedstoneComparator.Mode.SUBTRACT : BlockRedstoneComparator.Mode.COMPARE);
    }

    public int getMetaFromState(IBlockState iblockdata) {
        byte b0 = 0;
        int i = b0 | ((EnumFacing) iblockdata.getValue(BlockRedstoneComparator.FACING)).getHorizontalIndex();

        if (((Boolean) iblockdata.getValue(BlockRedstoneComparator.POWERED)).booleanValue()) {
            i |= 8;
        }

        if (iblockdata.getValue(BlockRedstoneComparator.MODE) == BlockRedstoneComparator.Mode.SUBTRACT) {
            i |= 4;
        }

        return i;
    }

    public IBlockState withRotation(IBlockState iblockdata, Rotation enumblockrotation) {
        return iblockdata.withProperty(BlockRedstoneComparator.FACING, enumblockrotation.rotate((EnumFacing) iblockdata.getValue(BlockRedstoneComparator.FACING)));
    }

    public IBlockState withMirror(IBlockState iblockdata, Mirror enumblockmirror) {
        return iblockdata.withRotation(enumblockmirror.toRotation((EnumFacing) iblockdata.getValue(BlockRedstoneComparator.FACING)));
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockRedstoneComparator.FACING, BlockRedstoneComparator.MODE, BlockRedstoneComparator.POWERED});
    }

    public IBlockState getStateForPlacement(World world, BlockPos blockposition, EnumFacing enumdirection, float f, float f1, float f2, int i, EntityLivingBase entityliving) {
        return this.getDefaultState().withProperty(BlockRedstoneComparator.FACING, entityliving.getHorizontalFacing().getOpposite()).withProperty(BlockRedstoneComparator.POWERED, Boolean.valueOf(false)).withProperty(BlockRedstoneComparator.MODE, BlockRedstoneComparator.Mode.COMPARE);
    }

    public static enum Mode implements IStringSerializable {

        COMPARE("compare"), SUBTRACT("subtract");

        private final String name;

        private Mode(String s) {
            this.name = s;
        }

        public String toString() {
            return this.name;
        }

        public String getName() {
            return this.name;
        }
    }
}
