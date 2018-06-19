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

    public static final PropertyEnum<EnumFacing> field_190957_a = PropertyDirection.func_177714_a("facing");
    public final EnumDyeColor field_190958_b;

    public BlockShulkerBox(EnumDyeColor enumcolor) {
        super(Material.field_151576_e, MapColor.field_151660_b);
        this.field_190958_b = enumcolor;
        this.func_149647_a(CreativeTabs.field_78031_c);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockShulkerBox.field_190957_a, EnumFacing.UP));
    }

    public TileEntity func_149915_a(World world, int i) {
        return new TileEntityShulkerBox(this.field_190958_b);
    }

    public boolean func_149662_c(IBlockState iblockdata) {
        return false;
    }

    public boolean func_176214_u(IBlockState iblockdata) {
        return true;
    }

    public boolean func_149686_d(IBlockState iblockdata) {
        return false;
    }

    public EnumBlockRenderType func_149645_b(IBlockState iblockdata) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    public boolean func_180639_a(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        if (world.field_72995_K) {
            return true;
        } else if (entityhuman.func_175149_v()) {
            return true;
        } else {
            TileEntity tileentity = world.func_175625_s(blockposition);

            if (tileentity instanceof TileEntityShulkerBox) {
                EnumFacing enumdirection1 = (EnumFacing) iblockdata.func_177229_b(BlockShulkerBox.field_190957_a);
                boolean flag;

                if (((TileEntityShulkerBox) tileentity).func_190591_p() == TileEntityShulkerBox.AnimationStatus.CLOSED) {
                    AxisAlignedBB axisalignedbb = BlockShulkerBox.field_185505_j.func_72321_a((double) (0.5F * (float) enumdirection1.func_82601_c()), (double) (0.5F * (float) enumdirection1.func_96559_d()), (double) (0.5F * (float) enumdirection1.func_82599_e())).func_191195_a((double) enumdirection1.func_82601_c(), (double) enumdirection1.func_96559_d(), (double) enumdirection1.func_82599_e());

                    flag = !world.func_184143_b(axisalignedbb.func_186670_a(blockposition.func_177972_a(enumdirection1)));
                } else {
                    flag = true;
                }

                if (flag) {
                    entityhuman.func_71029_a(StatList.field_191272_ae);
                    entityhuman.func_71007_a((IInventory) tileentity);
                }

                return true;
            } else {
                return false;
            }
        }
    }

    public IBlockState func_180642_a(World world, BlockPos blockposition, EnumFacing enumdirection, float f, float f1, float f2, int i, EntityLivingBase entityliving) {
        return this.func_176223_P().func_177226_a(BlockShulkerBox.field_190957_a, enumdirection);
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockShulkerBox.field_190957_a});
    }

    public int func_176201_c(IBlockState iblockdata) {
        return ((EnumFacing) iblockdata.func_177229_b(BlockShulkerBox.field_190957_a)).func_176745_a();
    }

    public IBlockState func_176203_a(int i) {
        EnumFacing enumdirection = EnumFacing.func_82600_a(i);

        return this.func_176223_P().func_177226_a(BlockShulkerBox.field_190957_a, enumdirection);
    }

    public void func_176208_a(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman) {
        if (world.func_175625_s(blockposition) instanceof TileEntityShulkerBox) {
            TileEntityShulkerBox tileentityshulkerbox = (TileEntityShulkerBox) world.func_175625_s(blockposition);

            tileentityshulkerbox.func_190579_a(entityhuman.field_71075_bZ.field_75098_d);
            tileentityshulkerbox.func_184281_d(entityhuman);
        }

    }

    // CraftBukkit start - override to prevent duplication when dropping
    public void func_180653_a(World world, BlockPos blockposition, IBlockState iblockdata, float f, int i) {
        TileEntity tileentity = world.func_175625_s(blockposition);

        if (tileentity instanceof TileEntityShulkerBox) {
            TileEntityShulkerBox tileentityshulkerbox = (TileEntityShulkerBox) tileentity;

            if (!tileentityshulkerbox.func_190590_r() && tileentityshulkerbox.func_190582_F()) {
                ItemStack itemstack = new ItemStack(Item.func_150898_a(this));
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();

                nbttagcompound.func_74782_a("BlockEntityTag", ((TileEntityShulkerBox) tileentity).func_190580_f(nbttagcompound1));
                itemstack.func_77982_d(nbttagcompound);
                if (tileentityshulkerbox.func_145818_k_()) {
                    itemstack.func_151001_c(tileentityshulkerbox.func_70005_c_());
                    tileentityshulkerbox.func_190575_a("");
                }

                func_180635_a(world, blockposition, itemstack);
                tileentityshulkerbox.func_174888_l(); // Paper - This was intended to be called in Vanilla (is checked in the if statement above if has been called) - Fixes dupe issues
            }

            world.func_175666_e(blockposition, iblockdata.func_177230_c());
        }
    }
    // CraftBukkit end

    public void func_180633_a(World world, BlockPos blockposition, IBlockState iblockdata, EntityLivingBase entityliving, ItemStack itemstack) {
        if (itemstack.func_82837_s()) {
            TileEntity tileentity = world.func_175625_s(blockposition);

            if (tileentity instanceof TileEntityShulkerBox) {
                ((TileEntityShulkerBox) tileentity).func_190575_a(itemstack.func_82833_r());
            }
        }

    }

    public void func_180663_b(World world, BlockPos blockposition, IBlockState iblockdata) {
        TileEntity tileentity = world.func_175625_s(blockposition);

        if (false && tileentity instanceof TileEntityShulkerBox) { // CraftBukkit - moved up
            TileEntityShulkerBox tileentityshulkerbox = (TileEntityShulkerBox) tileentity;

            if (!tileentityshulkerbox.func_190590_r() && tileentityshulkerbox.func_190582_F()) {
                ItemStack itemstack = new ItemStack(Item.func_150898_a(this));
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();

                nbttagcompound.func_74782_a("BlockEntityTag", ((TileEntityShulkerBox) tileentity).func_190580_f(nbttagcompound1));
                itemstack.func_77982_d(nbttagcompound);
                if (tileentityshulkerbox.func_145818_k_()) {
                    itemstack.func_151001_c(tileentityshulkerbox.func_70005_c_());
                    tileentityshulkerbox.func_190575_a("");
                }

                func_180635_a(world, blockposition, itemstack);
            }

        }
        world.func_175666_e(blockposition, iblockdata.func_177230_c()); // CraftBukkit - moved down

        super.func_180663_b(world, blockposition, iblockdata);
    }

    public EnumPushReaction func_149656_h(IBlockState iblockdata) {
        return EnumPushReaction.DESTROY;
    }

    public AxisAlignedBB func_185496_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        TileEntity tileentity = iblockaccess.func_175625_s(blockposition);

        return tileentity instanceof TileEntityShulkerBox ? ((TileEntityShulkerBox) tileentity).func_190584_a(iblockdata) : BlockShulkerBox.field_185505_j;
    }

    public boolean func_149740_M(IBlockState iblockdata) {
        return true;
    }

    public int func_180641_l(IBlockState iblockdata, World world, BlockPos blockposition) {
        return Container.func_94526_b((IInventory) world.func_175625_s(blockposition));
    }

    public ItemStack func_185473_a(World world, BlockPos blockposition, IBlockState iblockdata) {
        ItemStack itemstack = super.func_185473_a(world, blockposition, iblockdata);
        TileEntityShulkerBox tileentityshulkerbox = (TileEntityShulkerBox) world.func_175625_s(blockposition);
        NBTTagCompound nbttagcompound = tileentityshulkerbox.func_190580_f(new NBTTagCompound());

        if (!nbttagcompound.func_82582_d()) {
            itemstack.func_77983_a("BlockEntityTag", (NBTBase) nbttagcompound);
        }

        return itemstack;
    }

    public static Block func_190952_a(EnumDyeColor enumcolor) {
        switch (enumcolor) {
        case WHITE:
            return Blocks.field_190977_dl;

        case ORANGE:
            return Blocks.field_190978_dm;

        case MAGENTA:
            return Blocks.field_190979_dn;

        case LIGHT_BLUE:
            return Blocks.field_190980_do;

        case YELLOW:
            return Blocks.field_190981_dp;

        case LIME:
            return Blocks.field_190982_dq;

        case PINK:
            return Blocks.field_190983_dr;

        case GRAY:
            return Blocks.field_190984_ds;

        case SILVER:
            return Blocks.field_190985_dt;

        case CYAN:
            return Blocks.field_190986_du;

        case PURPLE:
        default:
            return Blocks.field_190987_dv;

        case BLUE:
            return Blocks.field_190988_dw;

        case BROWN:
            return Blocks.field_190989_dx;

        case GREEN:
            return Blocks.field_190990_dy;

        case RED:
            return Blocks.field_190991_dz;

        case BLACK:
            return Blocks.field_190975_dA;
        }
    }

    public static ItemStack func_190953_b(EnumDyeColor enumcolor) {
        return new ItemStack(func_190952_a(enumcolor));
    }

    public IBlockState func_185499_a(IBlockState iblockdata, Rotation enumblockrotation) {
        return iblockdata.func_177226_a(BlockShulkerBox.field_190957_a, enumblockrotation.func_185831_a((EnumFacing) iblockdata.func_177229_b(BlockShulkerBox.field_190957_a)));
    }

    public IBlockState func_185471_a(IBlockState iblockdata, Mirror enumblockmirror) {
        return iblockdata.func_185907_a(enumblockmirror.func_185800_a((EnumFacing) iblockdata.func_177229_b(BlockShulkerBox.field_190957_a)));
    }

    public BlockFaceShape func_193383_a(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        iblockdata = this.func_176221_a(iblockdata, iblockaccess, blockposition);
        EnumFacing enumdirection1 = (EnumFacing) iblockdata.func_177229_b(BlockShulkerBox.field_190957_a);
        TileEntityShulkerBox.AnimationStatus tileentityshulkerbox_animationphase = ((TileEntityShulkerBox) iblockaccess.func_175625_s(blockposition)).func_190591_p();

        return tileentityshulkerbox_animationphase != TileEntityShulkerBox.AnimationStatus.CLOSED && (tileentityshulkerbox_animationphase != TileEntityShulkerBox.AnimationStatus.OPENED || enumdirection1 != enumdirection.func_176734_d() && enumdirection1 != enumdirection) ? BlockFaceShape.UNDEFINED : BlockFaceShape.SOLID;
    }
}
