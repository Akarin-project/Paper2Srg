package net.minecraft.tileentity;

import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerShulkerBox;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.math.AxisAlignedBB;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;

// CraftBukkit start
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;
// CraftBukkit end

public class TileEntityShulkerBox extends TileEntityLockableLoot implements ITickable, ISidedInventory {

    private static final int[] field_190595_a = new int[27];
    private NonNullList<ItemStack> field_190596_f;
    private boolean field_190597_g;
    private int field_190598_h;
    private TileEntityShulkerBox.AnimationStatus field_190599_i;
    private float field_190600_j;
    private float field_190601_k;
    private EnumDyeColor field_190602_l;
    private boolean field_190594_p;

    // CraftBukkit start - add fields and methods
    public List<HumanEntity> transaction = new java.util.ArrayList<HumanEntity>();
    private int maxStack = MAX_STACK;

    public List<ItemStack> getContents() {
        return this.field_190596_f;
    }

    public void onOpen(CraftHumanEntity who) {
        transaction.add(who);
    }

    public void onClose(CraftHumanEntity who) {
        transaction.remove(who);
    }

    public List<HumanEntity> getViewers() {
        return transaction;
    }

    public void setMaxStackSize(int size) {
        maxStack = size;
    }
    // CraftBukkit end

    public TileEntityShulkerBox() {
        this((EnumDyeColor) null);
    }

    public TileEntityShulkerBox(@Nullable EnumDyeColor enumcolor) {
        this.field_190596_f = NonNullList.func_191197_a(27, ItemStack.field_190927_a);
        this.field_190599_i = TileEntityShulkerBox.AnimationStatus.CLOSED;
        this.field_190602_l = enumcolor;
    }

    public void func_73660_a() {
        this.func_190583_o();
        if (this.field_190599_i == TileEntityShulkerBox.AnimationStatus.OPENING || this.field_190599_i == TileEntityShulkerBox.AnimationStatus.CLOSING) {
            this.func_190589_G();
        }

    }

    protected void func_190583_o() {
        this.field_190601_k = this.field_190600_j;
        switch (this.field_190599_i) {
        case CLOSED:
            this.field_190600_j = 0.0F;
            break;

        case OPENING:
            this.field_190600_j += 0.1F;
            if (this.field_190600_j >= 1.0F) {
                this.func_190589_G();
                this.field_190599_i = TileEntityShulkerBox.AnimationStatus.OPENED;
                this.field_190600_j = 1.0F;
            }
            break;

        case CLOSING:
            this.field_190600_j -= 0.1F;
            if (this.field_190600_j <= 0.0F) {
                this.field_190599_i = TileEntityShulkerBox.AnimationStatus.CLOSED;
                this.field_190600_j = 0.0F;
            }
            break;

        case OPENED:
            this.field_190600_j = 1.0F;
        }

    }

    public TileEntityShulkerBox.AnimationStatus func_190591_p() {
        return this.field_190599_i;
    }

    public AxisAlignedBB func_190584_a(IBlockState iblockdata) {
        return this.func_190587_b((EnumFacing) iblockdata.func_177229_b(BlockShulkerBox.field_190957_a));
    }

    public AxisAlignedBB func_190587_b(EnumFacing enumdirection) {
        return Block.field_185505_j.func_72321_a((double) (0.5F * this.func_190585_a(1.0F) * (float) enumdirection.func_82601_c()), (double) (0.5F * this.func_190585_a(1.0F) * (float) enumdirection.func_96559_d()), (double) (0.5F * this.func_190585_a(1.0F) * (float) enumdirection.func_82599_e()));
    }

    private AxisAlignedBB func_190588_c(EnumFacing enumdirection) {
        EnumFacing enumdirection1 = enumdirection.func_176734_d();

        return this.func_190587_b(enumdirection).func_191195_a((double) enumdirection1.func_82601_c(), (double) enumdirection1.func_96559_d(), (double) enumdirection1.func_82599_e());
    }

    private void func_190589_G() {
        IBlockState iblockdata = this.field_145850_b.func_180495_p(this.func_174877_v());

        if (iblockdata.func_177230_c() instanceof BlockShulkerBox) {
            EnumFacing enumdirection = (EnumFacing) iblockdata.func_177229_b(BlockShulkerBox.field_190957_a);
            AxisAlignedBB axisalignedbb = this.func_190588_c(enumdirection).func_186670_a(this.field_174879_c);
            List list = this.field_145850_b.func_72839_b((Entity) null, axisalignedbb);

            if (!list.isEmpty()) {
                for (int i = 0; i < list.size(); ++i) {
                    Entity entity = (Entity) list.get(i);

                    if (entity.func_184192_z() != EnumPushReaction.IGNORE) {
                        double d0 = 0.0D;
                        double d1 = 0.0D;
                        double d2 = 0.0D;
                        AxisAlignedBB axisalignedbb1 = entity.func_174813_aQ();

                        switch (enumdirection.func_176740_k()) {
                        case X:
                            if (enumdirection.func_176743_c() == EnumFacing.AxisDirection.POSITIVE) {
                                d0 = axisalignedbb.field_72336_d - axisalignedbb1.field_72340_a;
                            } else {
                                d0 = axisalignedbb1.field_72336_d - axisalignedbb.field_72340_a;
                            }

                            d0 += 0.01D;
                            break;

                        case Y:
                            if (enumdirection.func_176743_c() == EnumFacing.AxisDirection.POSITIVE) {
                                d1 = axisalignedbb.field_72337_e - axisalignedbb1.field_72338_b;
                            } else {
                                d1 = axisalignedbb1.field_72337_e - axisalignedbb.field_72338_b;
                            }

                            d1 += 0.01D;
                            break;

                        case Z:
                            if (enumdirection.func_176743_c() == EnumFacing.AxisDirection.POSITIVE) {
                                d2 = axisalignedbb.field_72334_f - axisalignedbb1.field_72339_c;
                            } else {
                                d2 = axisalignedbb1.field_72334_f - axisalignedbb.field_72339_c;
                            }

                            d2 += 0.01D;
                        }

                        entity.func_70091_d(MoverType.SHULKER_BOX, d0 * (double) enumdirection.func_82601_c(), d1 * (double) enumdirection.func_96559_d(), d2 * (double) enumdirection.func_82599_e());
                    }
                }

            }
        }
    }

    public int func_70302_i_() {
        return this.field_190596_f.size();
    }

    public int func_70297_j_() {
        return maxStack; // CraftBukkit
    }

    public boolean func_145842_c(int i, int j) {
        if (i == 1) {
            this.field_190598_h = j;
            if (j == 0) {
                this.field_190599_i = TileEntityShulkerBox.AnimationStatus.CLOSING;
            }

            if (j == 1) {
                this.field_190599_i = TileEntityShulkerBox.AnimationStatus.OPENING;
            }

            return true;
        } else {
            return super.func_145842_c(i, j);
        }
    }

    public void func_174889_b(EntityPlayer entityhuman) {
        if (!entityhuman.func_175149_v()) {
            if (this.field_190598_h < 0) {
                this.field_190598_h = 0;
            }

            ++this.field_190598_h;
            this.field_145850_b.func_175641_c(this.field_174879_c, this.func_145838_q(), 1, this.field_190598_h);
            if (this.field_190598_h == 1) {
                this.field_145850_b.func_184133_a((EntityPlayer) null, this.field_174879_c, SoundEvents.field_191262_fB, SoundCategory.BLOCKS, 0.5F, this.field_145850_b.field_73012_v.nextFloat() * 0.1F + 0.9F);
            }
        }

    }

    public void func_174886_c(EntityPlayer entityhuman) {
        if (!entityhuman.func_175149_v()) {
            --this.field_190598_h;
            this.field_145850_b.func_175641_c(this.field_174879_c, this.func_145838_q(), 1, this.field_190598_h);
            if (this.field_190598_h <= 0) {
                this.field_145850_b.func_184133_a((EntityPlayer) null, this.field_174879_c, SoundEvents.field_191261_fA, SoundCategory.BLOCKS, 0.5F, this.field_145850_b.field_73012_v.nextFloat() * 0.1F + 0.9F);
            }
        }

    }

    public Container func_174876_a(InventoryPlayer playerinventory, EntityPlayer entityhuman) {
        return new ContainerShulkerBox(playerinventory, this, entityhuman);
    }

    public String func_174875_k() {
        return "minecraft:shulker_box";
    }

    public String func_70005_c_() {
        return this.func_145818_k_() ? this.field_190577_o : "container.shulkerBox";
    }

    public static void func_190593_a(DataFixer dataconvertermanager) {
        dataconvertermanager.func_188258_a(FixTypes.BLOCK_ENTITY, (IDataWalker) (new ItemStackDataLists(TileEntityShulkerBox.class, new String[] { "Items"})));
    }

    public void func_145839_a(NBTTagCompound nbttagcompound) {
        super.func_145839_a(nbttagcompound);
        this.func_190586_e(nbttagcompound);
    }

    public NBTTagCompound func_189515_b(NBTTagCompound nbttagcompound) {
        super.func_189515_b(nbttagcompound);
        return this.func_190580_f(nbttagcompound);
    }

    public void func_190586_e(NBTTagCompound nbttagcompound) {
        this.field_190596_f = NonNullList.func_191197_a(this.func_70302_i_(), ItemStack.field_190927_a);
        if (!this.func_184283_b(nbttagcompound) && nbttagcompound.func_150297_b("Items", 9)) {
            ItemStackHelper.func_191283_b(nbttagcompound, this.field_190596_f);
        }

        if (nbttagcompound.func_150297_b("CustomName", 8)) {
            this.field_190577_o = nbttagcompound.func_74779_i("CustomName");
        }

    }

    public NBTTagCompound func_190580_f(NBTTagCompound nbttagcompound) {
        if (!this.func_184282_c(nbttagcompound)) {
            ItemStackHelper.func_191281_a(nbttagcompound, this.field_190596_f, false);
        }

        if (this.func_145818_k_()) {
            nbttagcompound.func_74778_a("CustomName", this.field_190577_o);
        }

        if (!nbttagcompound.func_74764_b("Lock") && this.func_174893_q_()) {
            this.func_174891_i().func_180157_a(nbttagcompound);
        }

        return nbttagcompound;
    }

    protected NonNullList<ItemStack> func_190576_q() {
        return this.field_190596_f;
    }

    public boolean func_191420_l() {
        Iterator iterator = this.field_190596_f.iterator();

        ItemStack itemstack;

        do {
            if (!iterator.hasNext()) {
                return true;
            }

            itemstack = (ItemStack) iterator.next();
        } while (itemstack.func_190926_b());

        return false;
    }

    public int[] func_180463_a(EnumFacing enumdirection) {
        return TileEntityShulkerBox.field_190595_a;
    }

    public boolean func_180462_a(int i, ItemStack itemstack, EnumFacing enumdirection) {
        return !(Block.func_149634_a(itemstack.func_77973_b()) instanceof BlockShulkerBox);
    }

    public boolean func_180461_b(int i, ItemStack itemstack, EnumFacing enumdirection) {
        return true;
    }

    public void func_174888_l() {
        this.field_190597_g = true;
        super.func_174888_l();
    }

    public boolean func_190590_r() {
        return this.field_190597_g;
    }

    public float func_190585_a(float f) {
        return this.field_190601_k + (this.field_190600_j - this.field_190601_k) * f;
    }

    @Nullable
    public SPacketUpdateTileEntity func_189518_D_() {
        return new SPacketUpdateTileEntity(this.field_174879_c, 10, this.func_189517_E_());
    }

    public boolean func_190581_E() {
        return this.field_190594_p;
    }

    public void func_190579_a(boolean flag) {
        this.field_190594_p = flag;
    }

    public boolean func_190582_F() {
        return !this.func_190581_E() || !this.func_191420_l() || this.func_145818_k_() || this.field_184284_m != null;
    }

    static {
        for (int i = 0; i < TileEntityShulkerBox.field_190595_a.length; TileEntityShulkerBox.field_190595_a[i] = i++) {
            ;
        }

    }

    public static enum AnimationStatus {

        CLOSED, OPENING, OPENED, CLOSING;

        private AnimationStatus() {}
    }
}
