package net.minecraft.block;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;


public class BlockShulkerBox extends BlockContainer {

    public static final PropertyEnum<EnumFacing> FACING = PropertyDirection.create("facing");
    public final EnumDyeColor color;

    public BlockShulkerBox(EnumDyeColor enumcolor) {
        super(Material.ROCK, MapColor.AIR);
        this.color = enumcolor;
        this.setCreativeTab(CreativeTabs.DECORATIONS);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockShulkerBox.FACING, EnumFacing.UP));
    }

    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityShulkerBox(this.color);
    }

    public boolean isOpaqueCube(IBlockState iblockdata) {
        return false;
    }

    public boolean causesSuffocation(IBlockState iblockdata) {
        return true;
    }

    public boolean isFullCube(IBlockState iblockdata) {
        return false;
    }

    public EnumBlockRenderType getRenderType(IBlockState iblockdata) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    public boolean onBlockActivated(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        if (world.isRemote) {
            return true;
        } else if (entityhuman.isSpectator()) {
            return true;
        } else {
            TileEntity tileentity = world.getTileEntity(blockposition);

            if (tileentity instanceof TileEntityShulkerBox) {
                EnumFacing enumdirection1 = (EnumFacing) iblockdata.getValue(BlockShulkerBox.FACING);
                boolean flag;

                if (((TileEntityShulkerBox) tileentity).getAnimationStatus() == TileEntityShulkerBox.AnimationStatus.CLOSED) {
                    AxisAlignedBB axisalignedbb = BlockShulkerBox.FULL_BLOCK_AABB.expand((double) (0.5F * (float) enumdirection1.getFrontOffsetX()), (double) (0.5F * (float) enumdirection1.getFrontOffsetY()), (double) (0.5F * (float) enumdirection1.getFrontOffsetZ())).contract((double) enumdirection1.getFrontOffsetX(), (double) enumdirection1.getFrontOffsetY(), (double) enumdirection1.getFrontOffsetZ());

                    flag = !world.collidesWithAnyBlock(axisalignedbb.offset(blockposition.offset(enumdirection1)));
                } else {
                    flag = true;
                }

                if (flag) {
                    entityhuman.addStat(StatList.OPEN_SHULKER_BOX);
                    entityhuman.displayGUIChest((IInventory) tileentity);
                }

                return true;
            } else {
                return false;
            }
        }
    }

    public IBlockState getStateForPlacement(World world, BlockPos blockposition, EnumFacing enumdirection, float f, float f1, float f2, int i, EntityLivingBase entityliving) {
        return this.getDefaultState().withProperty(BlockShulkerBox.FACING, enumdirection);
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockShulkerBox.FACING});
    }

    public int getMetaFromState(IBlockState iblockdata) {
        return ((EnumFacing) iblockdata.getValue(BlockShulkerBox.FACING)).getIndex();
    }

    public IBlockState getStateFromMeta(int i) {
        EnumFacing enumdirection = EnumFacing.getFront(i);

        return this.getDefaultState().withProperty(BlockShulkerBox.FACING, enumdirection);
    }

    public void onBlockHarvested(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman) {
        if (world.getTileEntity(blockposition) instanceof TileEntityShulkerBox) {
            TileEntityShulkerBox tileentityshulkerbox = (TileEntityShulkerBox) world.getTileEntity(blockposition);

            tileentityshulkerbox.setDestroyedByCreativePlayer(entityhuman.capabilities.isCreativeMode);
            tileentityshulkerbox.fillWithLoot(entityhuman);
        }

    }

    // CraftBukkit start - override to prevent duplication when dropping
    public void dropBlockAsItemWithChance(World world, BlockPos blockposition, IBlockState iblockdata, float f, int i) {
        TileEntity tileentity = world.getTileEntity(blockposition);

        if (tileentity instanceof TileEntityShulkerBox) {
            TileEntityShulkerBox tileentityshulkerbox = (TileEntityShulkerBox) tileentity;

            if (!tileentityshulkerbox.isCleared() && tileentityshulkerbox.shouldDrop()) {
                ItemStack itemstack = new ItemStack(Item.getItemFromBlock(this));
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();

                nbttagcompound.setTag("BlockEntityTag", ((TileEntityShulkerBox) tileentity).saveToNbt(nbttagcompound1));
                itemstack.setTagCompound(nbttagcompound);
                if (tileentityshulkerbox.hasCustomName()) {
                    itemstack.setStackDisplayName(tileentityshulkerbox.getName());
                    tileentityshulkerbox.setCustomName("");
                }

                spawnAsEntity(world, blockposition, itemstack);
                tileentityshulkerbox.clear(); // Paper - This was intended to be called in Vanilla (is checked in the if statement above if has been called) - Fixes dupe issues
            }

            world.updateComparatorOutputLevel(blockposition, iblockdata.getBlock());
        }
    }
    // CraftBukkit end

    public void onBlockPlacedBy(World world, BlockPos blockposition, IBlockState iblockdata, EntityLivingBase entityliving, ItemStack itemstack) {
        if (itemstack.hasDisplayName()) {
            TileEntity tileentity = world.getTileEntity(blockposition);

            if (tileentity instanceof TileEntityShulkerBox) {
                ((TileEntityShulkerBox) tileentity).setCustomName(itemstack.getDisplayName());
            }
        }

    }

    public void breakBlock(World world, BlockPos blockposition, IBlockState iblockdata) {
        TileEntity tileentity = world.getTileEntity(blockposition);

        if (false && tileentity instanceof TileEntityShulkerBox) { // CraftBukkit - moved up
            TileEntityShulkerBox tileentityshulkerbox = (TileEntityShulkerBox) tileentity;

            if (!tileentityshulkerbox.isCleared() && tileentityshulkerbox.shouldDrop()) {
                ItemStack itemstack = new ItemStack(Item.getItemFromBlock(this));
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();

                nbttagcompound.setTag("BlockEntityTag", ((TileEntityShulkerBox) tileentity).saveToNbt(nbttagcompound1));
                itemstack.setTagCompound(nbttagcompound);
                if (tileentityshulkerbox.hasCustomName()) {
                    itemstack.setStackDisplayName(tileentityshulkerbox.getName());
                    tileentityshulkerbox.setCustomName("");
                }

                spawnAsEntity(world, blockposition, itemstack);
            }

        }
        world.updateComparatorOutputLevel(blockposition, iblockdata.getBlock()); // CraftBukkit - moved down

        super.breakBlock(world, blockposition, iblockdata);
    }

    public EnumPushReaction getMobilityFlag(IBlockState iblockdata) {
        return EnumPushReaction.DESTROY;
    }

    public AxisAlignedBB getBoundingBox(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        TileEntity tileentity = iblockaccess.getTileEntity(blockposition);

        return tileentity instanceof TileEntityShulkerBox ? ((TileEntityShulkerBox) tileentity).getBoundingBox(iblockdata) : BlockShulkerBox.FULL_BLOCK_AABB;
    }

    public boolean hasComparatorInputOverride(IBlockState iblockdata) {
        return true;
    }

    public int getComparatorInputOverride(IBlockState iblockdata, World world, BlockPos blockposition) {
        return Container.calcRedstoneFromInventory((IInventory) world.getTileEntity(blockposition));
    }

    public ItemStack getItem(World world, BlockPos blockposition, IBlockState iblockdata) {
        ItemStack itemstack = super.getItem(world, blockposition, iblockdata);
        TileEntityShulkerBox tileentityshulkerbox = (TileEntityShulkerBox) world.getTileEntity(blockposition);
        NBTTagCompound nbttagcompound = tileentityshulkerbox.saveToNbt(new NBTTagCompound());

        if (!nbttagcompound.hasNoTags()) {
            itemstack.setTagInfo("BlockEntityTag", (NBTBase) nbttagcompound);
        }

        return itemstack;
    }

    public static Block getBlockByColor(EnumDyeColor enumcolor) {
        switch (enumcolor) {
        case WHITE:
            return Blocks.WHITE_SHULKER_BOX;

        case ORANGE:
            return Blocks.ORANGE_SHULKER_BOX;

        case MAGENTA:
            return Blocks.MAGENTA_SHULKER_BOX;

        case LIGHT_BLUE:
            return Blocks.LIGHT_BLUE_SHULKER_BOX;

        case YELLOW:
            return Blocks.YELLOW_SHULKER_BOX;

        case LIME:
            return Blocks.LIME_SHULKER_BOX;

        case PINK:
            return Blocks.PINK_SHULKER_BOX;

        case GRAY:
            return Blocks.GRAY_SHULKER_BOX;

        case SILVER:
            return Blocks.SILVER_SHULKER_BOX;

        case CYAN:
            return Blocks.CYAN_SHULKER_BOX;

        case PURPLE:
        default:
            return Blocks.PURPLE_SHULKER_BOX;

        case BLUE:
            return Blocks.BLUE_SHULKER_BOX;

        case BROWN:
            return Blocks.BROWN_SHULKER_BOX;

        case GREEN:
            return Blocks.GREEN_SHULKER_BOX;

        case RED:
            return Blocks.RED_SHULKER_BOX;

        case BLACK:
            return Blocks.BLACK_SHULKER_BOX;
        }
    }

    public static ItemStack getColoredItemStack(EnumDyeColor enumcolor) {
        return new ItemStack(getBlockByColor(enumcolor));
    }

    public IBlockState withRotation(IBlockState iblockdata, Rotation enumblockrotation) {
        return iblockdata.withProperty(BlockShulkerBox.FACING, enumblockrotation.rotate((EnumFacing) iblockdata.getValue(BlockShulkerBox.FACING)));
    }

    public IBlockState withMirror(IBlockState iblockdata, Mirror enumblockmirror) {
        return iblockdata.withRotation(enumblockmirror.toRotation((EnumFacing) iblockdata.getValue(BlockShulkerBox.FACING)));
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        iblockdata = this.getActualState(iblockdata, iblockaccess, blockposition);
        EnumFacing enumdirection1 = (EnumFacing) iblockdata.getValue(BlockShulkerBox.FACING);
        TileEntityShulkerBox.AnimationStatus tileentityshulkerbox_animationphase = ((TileEntityShulkerBox) iblockaccess.getTileEntity(blockposition)).getAnimationStatus();

        return tileentityshulkerbox_animationphase != TileEntityShulkerBox.AnimationStatus.CLOSED && (tileentityshulkerbox_animationphase != TileEntityShulkerBox.AnimationStatus.OPENED || enumdirection1 != enumdirection.getOpposite() && enumdirection1 != enumdirection) ? BlockFaceShape.UNDEFINED : BlockFaceShape.SOLID;
    }
}
