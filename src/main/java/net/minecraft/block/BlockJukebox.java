package net.minecraft.block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.walkers.ItemStackData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class BlockJukebox extends BlockContainer {

    public static final PropertyBool HAS_RECORD = PropertyBool.create("has_record");

    public static void registerFixesJukebox(DataFixer dataconvertermanager) {
        dataconvertermanager.registerWalker(FixTypes.BLOCK_ENTITY, (IDataWalker) (new ItemStackData(BlockJukebox.TileEntityJukebox.class, new String[] { "RecordItem"})));
    }

    protected BlockJukebox() {
        super(Material.WOOD, MapColor.DIRT);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockJukebox.HAS_RECORD, Boolean.valueOf(false)));
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }

    public boolean onBlockActivated(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        if (((Boolean) iblockdata.getValue(BlockJukebox.HAS_RECORD)).booleanValue()) {
            this.dropRecord(world, blockposition, iblockdata);
            iblockdata = iblockdata.withProperty(BlockJukebox.HAS_RECORD, Boolean.valueOf(false));
            world.setBlockState(blockposition, iblockdata, 2);
            return true;
        } else {
            return false;
        }
    }

    public void insertRecord(World world, BlockPos blockposition, IBlockState iblockdata, ItemStack itemstack) {
        TileEntity tileentity = world.getTileEntity(blockposition);

        if (tileentity instanceof BlockJukebox.TileEntityJukebox) {
            ((BlockJukebox.TileEntityJukebox) tileentity).setRecord(itemstack.copy());
            world.setBlockState(blockposition, iblockdata.withProperty(BlockJukebox.HAS_RECORD, Boolean.valueOf(true)), 2);
        }
    }

    public void dropRecord(World world, BlockPos blockposition, IBlockState iblockdata) {
        if (!world.isRemote) {
            TileEntity tileentity = world.getTileEntity(blockposition);

            if (tileentity instanceof BlockJukebox.TileEntityJukebox) {
                BlockJukebox.TileEntityJukebox blockjukebox_tileentityrecordplayer = (BlockJukebox.TileEntityJukebox) tileentity;
                ItemStack itemstack = blockjukebox_tileentityrecordplayer.getRecord();

                if (!itemstack.isEmpty()) {
                    world.playEvent(1010, blockposition, 0);
                    world.playRecord(blockposition, (SoundEvent) null);
                    blockjukebox_tileentityrecordplayer.setRecord(ItemStack.EMPTY);
                    float f = 0.7F;
                    double d0 = (double) (world.rand.nextFloat() * 0.7F) + 0.15000000596046448D;
                    double d1 = (double) (world.rand.nextFloat() * 0.7F) + 0.06000000238418579D + 0.6D;
                    double d2 = (double) (world.rand.nextFloat() * 0.7F) + 0.15000000596046448D;
                    ItemStack itemstack1 = itemstack.copy();
                    EntityItem entityitem = new EntityItem(world, (double) blockposition.getX() + d0, (double) blockposition.getY() + d1, (double) blockposition.getZ() + d2, itemstack1);

                    entityitem.setDefaultPickupDelay();
                    world.spawnEntity(entityitem);
                }
            }
        }
    }

    public void breakBlock(World world, BlockPos blockposition, IBlockState iblockdata) {
        this.dropRecord(world, blockposition, iblockdata);
        super.breakBlock(world, blockposition, iblockdata);
    }

    public void dropBlockAsItemWithChance(World world, BlockPos blockposition, IBlockState iblockdata, float f, int i) {
        if (!world.isRemote) {
            super.dropBlockAsItemWithChance(world, blockposition, iblockdata, f, 0);
        }
    }

    public TileEntity createNewTileEntity(World world, int i) {
        return new BlockJukebox.TileEntityJukebox();
    }

    public boolean hasComparatorInputOverride(IBlockState iblockdata) {
        return true;
    }

    public int getComparatorInputOverride(IBlockState iblockdata, World world, BlockPos blockposition) {
        TileEntity tileentity = world.getTileEntity(blockposition);

        if (tileentity instanceof BlockJukebox.TileEntityJukebox) {
            ItemStack itemstack = ((BlockJukebox.TileEntityJukebox) tileentity).getRecord();

            if (!itemstack.isEmpty()) {
                return Item.getIdFromItem(itemstack.getItem()) + 1 - Item.getIdFromItem(Items.RECORD_13);
            }
        }

        return 0;
    }

    public EnumBlockRenderType getRenderType(IBlockState iblockdata) {
        return EnumBlockRenderType.MODEL;
    }

    public IBlockState getStateFromMeta(int i) {
        return this.getDefaultState().withProperty(BlockJukebox.HAS_RECORD, Boolean.valueOf(i > 0));
    }

    public int getMetaFromState(IBlockState iblockdata) {
        return ((Boolean) iblockdata.getValue(BlockJukebox.HAS_RECORD)).booleanValue() ? 1 : 0;
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockJukebox.HAS_RECORD});
    }

    public static class TileEntityJukebox extends TileEntity {

        private ItemStack record;

        public TileEntityJukebox() {
            this.record = ItemStack.EMPTY;
        }

        public void readFromNBT(NBTTagCompound nbttagcompound) {
            super.readFromNBT(nbttagcompound);
            if (nbttagcompound.hasKey("RecordItem", 10)) {
                this.setRecord(new ItemStack(nbttagcompound.getCompoundTag("RecordItem")));
            } else if (nbttagcompound.getInteger("Record") > 0) {
                this.setRecord(new ItemStack(Item.getItemById(nbttagcompound.getInteger("Record"))));
            }

        }

        public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
            super.writeToNBT(nbttagcompound);
            if (!this.getRecord().isEmpty()) {
                nbttagcompound.setTag("RecordItem", this.getRecord().writeToNBT(new NBTTagCompound()));
            }

            return nbttagcompound;
        }

        public ItemStack getRecord() {
            return this.record;
        }

        public void setRecord(ItemStack itemstack) {
            // CraftBukkit start - There can only be one
            if (!itemstack.isEmpty()) {
                itemstack.setCount(1);
            }
            // CraftBukkit end
            this.record = itemstack;
            this.markDirty();
        }
    }
}
