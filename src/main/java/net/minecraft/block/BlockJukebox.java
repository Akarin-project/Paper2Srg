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

    public static final PropertyBool field_176432_a = PropertyBool.func_177716_a("has_record");

    public static void func_189873_a(DataFixer dataconvertermanager) {
        dataconvertermanager.func_188258_a(FixTypes.BLOCK_ENTITY, (IDataWalker) (new ItemStackData(BlockJukebox.TileEntityJukebox.class, new String[] { "RecordItem"})));
    }

    protected BlockJukebox() {
        super(Material.field_151575_d, MapColor.field_151664_l);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockJukebox.field_176432_a, Boolean.valueOf(false)));
        this.func_149647_a(CreativeTabs.field_78031_c);
    }

    public boolean func_180639_a(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        if (((Boolean) iblockdata.func_177229_b(BlockJukebox.field_176432_a)).booleanValue()) {
            this.func_180678_e(world, blockposition, iblockdata);
            iblockdata = iblockdata.func_177226_a(BlockJukebox.field_176432_a, Boolean.valueOf(false));
            world.func_180501_a(blockposition, iblockdata, 2);
            return true;
        } else {
            return false;
        }
    }

    public void func_176431_a(World world, BlockPos blockposition, IBlockState iblockdata, ItemStack itemstack) {
        TileEntity tileentity = world.func_175625_s(blockposition);

        if (tileentity instanceof BlockJukebox.TileEntityJukebox) {
            ((BlockJukebox.TileEntityJukebox) tileentity).func_145857_a(itemstack.func_77946_l());
            world.func_180501_a(blockposition, iblockdata.func_177226_a(BlockJukebox.field_176432_a, Boolean.valueOf(true)), 2);
        }
    }

    public void func_180678_e(World world, BlockPos blockposition, IBlockState iblockdata) {
        if (!world.field_72995_K) {
            TileEntity tileentity = world.func_175625_s(blockposition);

            if (tileentity instanceof BlockJukebox.TileEntityJukebox) {
                BlockJukebox.TileEntityJukebox blockjukebox_tileentityrecordplayer = (BlockJukebox.TileEntityJukebox) tileentity;
                ItemStack itemstack = blockjukebox_tileentityrecordplayer.func_145856_a();

                if (!itemstack.func_190926_b()) {
                    world.func_175718_b(1010, blockposition, 0);
                    world.func_184149_a(blockposition, (SoundEvent) null);
                    blockjukebox_tileentityrecordplayer.func_145857_a(ItemStack.field_190927_a);
                    float f = 0.7F;
                    double d0 = (double) (world.field_73012_v.nextFloat() * 0.7F) + 0.15000000596046448D;
                    double d1 = (double) (world.field_73012_v.nextFloat() * 0.7F) + 0.06000000238418579D + 0.6D;
                    double d2 = (double) (world.field_73012_v.nextFloat() * 0.7F) + 0.15000000596046448D;
                    ItemStack itemstack1 = itemstack.func_77946_l();
                    EntityItem entityitem = new EntityItem(world, (double) blockposition.func_177958_n() + d0, (double) blockposition.func_177956_o() + d1, (double) blockposition.func_177952_p() + d2, itemstack1);

                    entityitem.func_174869_p();
                    world.func_72838_d(entityitem);
                }
            }
        }
    }

    public void func_180663_b(World world, BlockPos blockposition, IBlockState iblockdata) {
        this.func_180678_e(world, blockposition, iblockdata);
        super.func_180663_b(world, blockposition, iblockdata);
    }

    public void func_180653_a(World world, BlockPos blockposition, IBlockState iblockdata, float f, int i) {
        if (!world.field_72995_K) {
            super.func_180653_a(world, blockposition, iblockdata, f, 0);
        }
    }

    public TileEntity func_149915_a(World world, int i) {
        return new BlockJukebox.TileEntityJukebox();
    }

    public boolean func_149740_M(IBlockState iblockdata) {
        return true;
    }

    public int func_180641_l(IBlockState iblockdata, World world, BlockPos blockposition) {
        TileEntity tileentity = world.func_175625_s(blockposition);

        if (tileentity instanceof BlockJukebox.TileEntityJukebox) {
            ItemStack itemstack = ((BlockJukebox.TileEntityJukebox) tileentity).func_145856_a();

            if (!itemstack.func_190926_b()) {
                return Item.func_150891_b(itemstack.func_77973_b()) + 1 - Item.func_150891_b(Items.field_151096_cd);
            }
        }

        return 0;
    }

    public EnumBlockRenderType func_149645_b(IBlockState iblockdata) {
        return EnumBlockRenderType.MODEL;
    }

    public IBlockState func_176203_a(int i) {
        return this.func_176223_P().func_177226_a(BlockJukebox.field_176432_a, Boolean.valueOf(i > 0));
    }

    public int func_176201_c(IBlockState iblockdata) {
        return ((Boolean) iblockdata.func_177229_b(BlockJukebox.field_176432_a)).booleanValue() ? 1 : 0;
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockJukebox.field_176432_a});
    }

    public static class TileEntityJukebox extends TileEntity {

        private ItemStack field_145858_a;

        public TileEntityJukebox() {
            this.field_145858_a = ItemStack.field_190927_a;
        }

        public void func_145839_a(NBTTagCompound nbttagcompound) {
            super.func_145839_a(nbttagcompound);
            if (nbttagcompound.func_150297_b("RecordItem", 10)) {
                this.func_145857_a(new ItemStack(nbttagcompound.func_74775_l("RecordItem")));
            } else if (nbttagcompound.func_74762_e("Record") > 0) {
                this.func_145857_a(new ItemStack(Item.func_150899_d(nbttagcompound.func_74762_e("Record"))));
            }

        }

        public NBTTagCompound func_189515_b(NBTTagCompound nbttagcompound) {
            super.func_189515_b(nbttagcompound);
            if (!this.func_145856_a().func_190926_b()) {
                nbttagcompound.func_74782_a("RecordItem", this.func_145856_a().func_77955_b(new NBTTagCompound()));
            }

            return nbttagcompound;
        }

        public ItemStack func_145856_a() {
            return this.field_145858_a;
        }

        public void func_145857_a(ItemStack itemstack) {
            // CraftBukkit start - There can only be one
            if (!itemstack.func_190926_b()) {
                itemstack.func_190920_e(1);
            }
            // CraftBukkit end
            this.field_145858_a = itemstack;
            this.func_70296_d();
        }
    }
}
