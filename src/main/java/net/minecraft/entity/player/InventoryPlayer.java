package net.minecraft.entity.player;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

import java.util.ArrayList;
import net;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ReportedException;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;

// CraftBukkit start
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;

import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;
// CraftBukkit end

public class InventoryPlayer implements IInventory {

    public final NonNullList<ItemStack> field_70462_a;
    public final NonNullList<ItemStack> field_70460_b;
    public final NonNullList<ItemStack> field_184439_c;
    private final List<NonNullList<ItemStack>> field_184440_g;
    public int field_70461_c;
    public EntityPlayer field_70458_d;
    private ItemStack field_70457_g;
    private int field_194017_h;

    // CraftBukkit start - add fields and methods
    public List<HumanEntity> transaction = new java.util.ArrayList<HumanEntity>();
    private int maxStack = MAX_STACK;

    public List<ItemStack> getContents() {
        List<ItemStack> combined = new ArrayList<ItemStack>(field_70462_a.size() + field_70460_b.size() + field_184439_c.size());
        for (List<net.minecraft.item.ItemStack> sub : this.field_184440_g) {
            combined.addAll(sub);
        }

        return combined;
    }

    public List<ItemStack> getArmorContents() {
        return this.field_70460_b;
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

    public org.bukkit.inventory.InventoryHolder getOwner() {
        return this.field_70458_d.getBukkitEntity();
    }

    public void setMaxStackSize(int size) {
        maxStack = size;
    }

    @Override
    public Location getLocation() {
        return field_70458_d.getBukkitEntity().getLocation();
    }
    // CraftBukkit end

    public InventoryPlayer(EntityPlayer entityhuman) {
        this.field_70462_a = NonNullList.func_191197_a(36, ItemStack.field_190927_a);
        this.field_70460_b = NonNullList.func_191197_a(4, ItemStack.field_190927_a);
        this.field_184439_c = NonNullList.func_191197_a(1, ItemStack.field_190927_a);
        this.field_184440_g = Arrays.asList(new NonNullList[] { this.field_70462_a, this.field_70460_b, this.field_184439_c});
        this.field_70457_g = ItemStack.field_190927_a;
        this.field_70458_d = entityhuman;
    }

    public ItemStack func_70448_g() {
        return func_184435_e(this.field_70461_c) ? (ItemStack) this.field_70462_a.get(this.field_70461_c) : ItemStack.field_190927_a;
    }

    public static int func_70451_h() {
        return 9;
    }

    private boolean func_184436_a(ItemStack itemstack, ItemStack itemstack1) {
        return !itemstack.func_190926_b() && this.func_184431_b(itemstack, itemstack1) && itemstack.func_77985_e() && itemstack.func_190916_E() < itemstack.func_77976_d() && itemstack.func_190916_E() < this.func_70297_j_();
    }

    private boolean func_184431_b(ItemStack itemstack, ItemStack itemstack1) {
        return itemstack.func_77973_b() == itemstack1.func_77973_b() && (!itemstack.func_77981_g() || itemstack.func_77960_j() == itemstack1.func_77960_j()) && ItemStack.func_77970_a(itemstack, itemstack1);
    }

    // CraftBukkit start - Watch method above! :D
    public int canHold(ItemStack itemstack) {
        int remains = itemstack.func_190916_E();
        for (int i = 0; i < this.field_70462_a.size(); ++i) {
            ItemStack itemstack1 = this.func_70301_a(i);
            if (itemstack1.func_190926_b()) return itemstack.func_190916_E();

            // Taken from firstPartial(ItemStack)
            if (!itemstack1.func_190926_b() && itemstack1.func_77973_b() == itemstack.func_77973_b() && itemstack1.func_77985_e() && itemstack1.func_190916_E() < itemstack1.func_77976_d() && itemstack1.func_190916_E() < this.func_70297_j_() && (!itemstack1.func_77981_g() || itemstack1.func_77960_j() == itemstack.func_77960_j()) && ItemStack.func_77970_a(itemstack1, itemstack)) {
                remains -= (itemstack1.func_77976_d() < this.func_70297_j_() ? itemstack1.func_77976_d() : this.func_70297_j_()) - itemstack1.func_190916_E();
            }
            if (remains <= 0) return itemstack.func_190916_E();
        }
        return itemstack.func_190916_E() - remains;
    }
    // CraftBukkit end

    public int func_70447_i() {
        for (int i = 0; i < this.field_70462_a.size(); ++i) {
            if (((ItemStack) this.field_70462_a.get(i)).func_190926_b()) {
                return i;
            }
        }

        return -1;
    }

    public void func_184430_d(int i) {
        this.field_70461_c = this.func_184433_k();
        ItemStack itemstack = (ItemStack) this.field_70462_a.get(this.field_70461_c);

        this.field_70462_a.set(this.field_70461_c, this.field_70462_a.get(i));
        this.field_70462_a.set(i, itemstack);
    }

    public static boolean func_184435_e(int i) {
        return i >= 0 && i < 9;
    }

    public int func_194014_c(ItemStack itemstack) {
        for (int i = 0; i < this.field_70462_a.size(); ++i) {
            ItemStack itemstack1 = (ItemStack) this.field_70462_a.get(i);

            if (!((ItemStack) this.field_70462_a.get(i)).func_190926_b() && this.func_184431_b(itemstack, (ItemStack) this.field_70462_a.get(i)) && !((ItemStack) this.field_70462_a.get(i)).func_77951_h() && !itemstack1.func_77948_v() && !itemstack1.func_82837_s()) {
                return i;
            }
        }

        return -1;
    }

    public int func_184433_k() {
        int i;
        int j;

        for (i = 0; i < 9; ++i) {
            j = (this.field_70461_c + i) % 9;
            if (((ItemStack) this.field_70462_a.get(j)).func_190926_b()) {
                return j;
            }
        }

        for (i = 0; i < 9; ++i) {
            j = (this.field_70461_c + i) % 9;
            if (!((ItemStack) this.field_70462_a.get(j)).func_77948_v()) {
                return j;
            }
        }

        return this.field_70461_c;
    }

    public int func_174925_a(@Nullable Item item, int i, int j, @Nullable NBTTagCompound nbttagcompound) {
        int k = 0;

        int l;

        for (l = 0; l < this.func_70302_i_(); ++l) {
            ItemStack itemstack = this.func_70301_a(l);

            if (!itemstack.func_190926_b() && (item == null || itemstack.func_77973_b() == item) && (i <= -1 || itemstack.func_77960_j() == i) && (nbttagcompound == null || NBTUtil.func_181123_a(nbttagcompound, itemstack.func_77978_p(), true))) {
                int i1 = j <= 0 ? itemstack.func_190916_E() : Math.min(j - k, itemstack.func_190916_E());

                k += i1;
                if (j != 0) {
                    itemstack.func_190918_g(i1);
                    if (itemstack.func_190926_b()) {
                        this.func_70299_a(l, ItemStack.field_190927_a);
                    }

                    if (j > 0 && k >= j) {
                        return k;
                    }
                }
            }
        }

        if (!this.field_70457_g.func_190926_b()) {
            if (item != null && this.field_70457_g.func_77973_b() != item) {
                return k;
            }

            if (i > -1 && this.field_70457_g.func_77960_j() != i) {
                return k;
            }

            if (nbttagcompound != null && !NBTUtil.func_181123_a(nbttagcompound, this.field_70457_g.func_77978_p(), true)) {
                return k;
            }

            l = j <= 0 ? this.field_70457_g.func_190916_E() : Math.min(j - k, this.field_70457_g.func_190916_E());
            k += l;
            if (j != 0) {
                this.field_70457_g.func_190918_g(l);
                if (this.field_70457_g.func_190926_b()) {
                    this.field_70457_g = ItemStack.field_190927_a;
                }

                if (j > 0 && k >= j) {
                    return k;
                }
            }
        }

        return k;
    }

    private int func_70452_e(ItemStack itemstack) {
        int i = this.func_70432_d(itemstack);

        if (i == -1) {
            i = this.func_70447_i();
        }

        return i == -1 ? itemstack.func_190916_E() : this.func_191973_d(i, itemstack);
    }

    private int func_191973_d(int i, ItemStack itemstack) {
        Item item = itemstack.func_77973_b();
        int j = itemstack.func_190916_E();
        ItemStack itemstack1 = this.func_70301_a(i);

        if (itemstack1.func_190926_b()) {
            itemstack1 = new ItemStack(item, 0, itemstack.func_77960_j());
            if (itemstack.func_77942_o()) {
                itemstack1.func_77982_d(itemstack.func_77978_p().func_74737_b());
            }

            this.func_70299_a(i, itemstack1);
        }

        int k = j;

        if (j > itemstack1.func_77976_d() - itemstack1.func_190916_E()) {
            k = itemstack1.func_77976_d() - itemstack1.func_190916_E();
        }

        if (k > this.func_70297_j_() - itemstack1.func_190916_E()) {
            k = this.func_70297_j_() - itemstack1.func_190916_E();
        }

        if (k == 0) {
            return j;
        } else {
            j -= k;
            itemstack1.func_190917_f(k);
            itemstack1.func_190915_d(5);
            return j;
        }
    }

    public int func_70432_d(ItemStack itemstack) {
        if (this.func_184436_a(this.func_70301_a(this.field_70461_c), itemstack)) {
            return this.field_70461_c;
        } else if (this.func_184436_a(this.func_70301_a(40), itemstack)) {
            return 40;
        } else {
            for (int i = 0; i < this.field_70462_a.size(); ++i) {
                if (this.func_184436_a((ItemStack) this.field_70462_a.get(i), itemstack)) {
                    return i;
                }
            }

            return -1;
        }
    }

    public void func_70429_k() {
        Iterator iterator = this.field_184440_g.iterator();

        while (iterator.hasNext()) {
            NonNullList nonnulllist = (NonNullList) iterator.next();

            for (int i = 0; i < nonnulllist.size(); ++i) {
                if (!((ItemStack) nonnulllist.get(i)).func_190926_b()) {
                    ((ItemStack) nonnulllist.get(i)).func_77945_a(this.field_70458_d.field_70170_p, this.field_70458_d, i, this.field_70461_c == i);
                }
            }
        }

    }

    public boolean func_70441_a(ItemStack itemstack) {
        return this.func_191971_c(-1, itemstack);
    }

    public boolean func_191971_c(int i, final ItemStack itemstack) {
        if (itemstack.func_190926_b()) {
            return false;
        } else {
            try {
                if (itemstack.func_77951_h()) {
                    if (i == -1) {
                        i = this.func_70447_i();
                    }

                    if (i >= 0) {
                        this.field_70462_a.set(i, itemstack.func_77946_l());
                        ((ItemStack) this.field_70462_a.get(i)).func_190915_d(5);
                        itemstack.func_190920_e(0);
                        return true;
                    } else if (this.field_70458_d.field_71075_bZ.field_75098_d) {
                        itemstack.func_190920_e(0);
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    int j;

                    do {
                        j = itemstack.func_190916_E();
                        if (i == -1) {
                            itemstack.func_190920_e(this.func_70452_e(itemstack));
                        } else {
                            itemstack.func_190920_e(this.func_191973_d(i, itemstack));
                        }
                    } while (!itemstack.func_190926_b() && itemstack.func_190916_E() < j);

                    if (itemstack.func_190916_E() == j && this.field_70458_d.field_71075_bZ.field_75098_d) {
                        itemstack.func_190920_e(0);
                        return true;
                    } else {
                        return itemstack.func_190916_E() < j;
                    }
                }
            } catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.func_85055_a(throwable, "Adding item to inventory");
                CrashReportCategory crashreportsystemdetails = crashreport.func_85058_a("Item being added");

                crashreportsystemdetails.func_71507_a("Item ID", (Object) Integer.valueOf(Item.func_150891_b(itemstack.func_77973_b())));
                crashreportsystemdetails.func_71507_a("Item data", (Object) Integer.valueOf(itemstack.func_77960_j()));
                crashreportsystemdetails.func_189529_a("Item name", new ICrashReportDetail() {
                    public String a() throws Exception {
                        return itemstack.func_82833_r();
                    }

                    public Object call() throws Exception {
                        return this.a();
                    }
                });
                throw new ReportedException(crashreport);
            }
        }
    }

    public void func_191975_a(World world, ItemStack itemstack) {
        if (!world.field_72995_K) {
            while (!itemstack.func_190926_b()) {
                int i = this.func_70432_d(itemstack);

                if (i == -1) {
                    i = this.func_70447_i();
                }

                if (i == -1) {
                    this.field_70458_d.func_71019_a(itemstack, false);
                    break;
                }

                int j = itemstack.func_77976_d() - this.func_70301_a(i).func_190916_E();

                if (this.func_191971_c(i, itemstack.func_77979_a(j))) {
                    ((EntityPlayerMP) this.field_70458_d).field_71135_a.func_147359_a(new SPacketSetSlot(-2, i, this.func_70301_a(i)));
                }
            }

        }
    }

    public ItemStack func_70298_a(int i, int j) {
        NonNullList nonnulllist = null;

        NonNullList nonnulllist1;

        for (Iterator iterator = this.field_184440_g.iterator(); iterator.hasNext(); i -= nonnulllist1.size()) {
            nonnulllist1 = (NonNullList) iterator.next();
            if (i < nonnulllist1.size()) {
                nonnulllist = nonnulllist1;
                break;
            }
        }

        return nonnulllist != null && !((ItemStack) nonnulllist.get(i)).func_190926_b() ? ItemStackHelper.func_188382_a(nonnulllist, i, j) : ItemStack.field_190927_a;
    }

    public void func_184437_d(ItemStack itemstack) {
        Iterator iterator = this.field_184440_g.iterator();

        while (iterator.hasNext()) {
            NonNullList nonnulllist = (NonNullList) iterator.next();

            for (int i = 0; i < nonnulllist.size(); ++i) {
                if (nonnulllist.get(i) == itemstack) {
                    nonnulllist.set(i, ItemStack.field_190927_a);
                    break;
                }
            }
        }

    }

    public ItemStack func_70304_b(int i) {
        NonNullList nonnulllist = null;

        NonNullList nonnulllist1;

        for (Iterator iterator = this.field_184440_g.iterator(); iterator.hasNext(); i -= nonnulllist1.size()) {
            nonnulllist1 = (NonNullList) iterator.next();
            if (i < nonnulllist1.size()) {
                nonnulllist = nonnulllist1;
                break;
            }
        }

        if (nonnulllist != null && !((ItemStack) nonnulllist.get(i)).func_190926_b()) {
            ItemStack itemstack = (ItemStack) nonnulllist.get(i);

            nonnulllist.set(i, ItemStack.field_190927_a);
            return itemstack;
        } else {
            return ItemStack.field_190927_a;
        }
    }

    public void func_70299_a(int i, ItemStack itemstack) {
        NonNullList nonnulllist = null;

        NonNullList nonnulllist1;

        for (Iterator iterator = this.field_184440_g.iterator(); iterator.hasNext(); i -= nonnulllist1.size()) {
            nonnulllist1 = (NonNullList) iterator.next();
            if (i < nonnulllist1.size()) {
                nonnulllist = nonnulllist1;
                break;
            }
        }

        if (nonnulllist != null) {
            nonnulllist.set(i, itemstack);
        }

    }

    public float func_184438_a(IBlockState iblockdata) {
        float f = 1.0F;

        if (!((ItemStack) this.field_70462_a.get(this.field_70461_c)).func_190926_b()) {
            f *= ((ItemStack) this.field_70462_a.get(this.field_70461_c)).func_150997_a(iblockdata);
        }

        return f;
    }

    public NBTTagList func_70442_a(NBTTagList nbttaglist) {
        int i;
        NBTTagCompound nbttagcompound;

        for (i = 0; i < this.field_70462_a.size(); ++i) {
            if (!((ItemStack) this.field_70462_a.get(i)).func_190926_b()) {
                nbttagcompound = new NBTTagCompound();
                nbttagcompound.func_74774_a("Slot", (byte) i);
                ((ItemStack) this.field_70462_a.get(i)).func_77955_b(nbttagcompound);
                nbttaglist.func_74742_a(nbttagcompound);
            }
        }

        for (i = 0; i < this.field_70460_b.size(); ++i) {
            if (!((ItemStack) this.field_70460_b.get(i)).func_190926_b()) {
                nbttagcompound = new NBTTagCompound();
                nbttagcompound.func_74774_a("Slot", (byte) (i + 100));
                ((ItemStack) this.field_70460_b.get(i)).func_77955_b(nbttagcompound);
                nbttaglist.func_74742_a(nbttagcompound);
            }
        }

        for (i = 0; i < this.field_184439_c.size(); ++i) {
            if (!((ItemStack) this.field_184439_c.get(i)).func_190926_b()) {
                nbttagcompound = new NBTTagCompound();
                nbttagcompound.func_74774_a("Slot", (byte) (i + 150));
                ((ItemStack) this.field_184439_c.get(i)).func_77955_b(nbttagcompound);
                nbttaglist.func_74742_a(nbttagcompound);
            }
        }

        return nbttaglist;
    }

    public void func_70443_b(NBTTagList nbttaglist) {
        this.field_70462_a.clear();
        this.field_70460_b.clear();
        this.field_184439_c.clear();

        for (int i = 0; i < nbttaglist.func_74745_c(); ++i) {
            NBTTagCompound nbttagcompound = nbttaglist.func_150305_b(i);
            int j = nbttagcompound.func_74771_c("Slot") & 255;
            ItemStack itemstack = new ItemStack(nbttagcompound);

            if (!itemstack.func_190926_b()) {
                if (j >= 0 && j < this.field_70462_a.size()) {
                    this.field_70462_a.set(j, itemstack);
                } else if (j >= 100 && j < this.field_70460_b.size() + 100) {
                    this.field_70460_b.set(j - 100, itemstack);
                } else if (j >= 150 && j < this.field_184439_c.size() + 150) {
                    this.field_184439_c.set(j - 150, itemstack);
                }
            }
        }

    }

    public int func_70302_i_() {
        return this.field_70462_a.size() + this.field_70460_b.size() + this.field_184439_c.size();
    }

    public boolean func_191420_l() {
        Iterator iterator = this.field_70462_a.iterator();

        ItemStack itemstack;

        do {
            if (!iterator.hasNext()) {
                iterator = this.field_70460_b.iterator();

                do {
                    if (!iterator.hasNext()) {
                        iterator = this.field_184439_c.iterator();

                        do {
                            if (!iterator.hasNext()) {
                                return true;
                            }

                            itemstack = (ItemStack) iterator.next();
                        } while (itemstack.func_190926_b());

                        return false;
                    }

                    itemstack = (ItemStack) iterator.next();
                } while (itemstack.func_190926_b());

                return false;
            }

            itemstack = (ItemStack) iterator.next();
        } while (itemstack.func_190926_b());

        return false;
    }

    public ItemStack func_70301_a(int i) {
        NonNullList nonnulllist = null;

        NonNullList nonnulllist1;

        for (Iterator iterator = this.field_184440_g.iterator(); iterator.hasNext(); i -= nonnulllist1.size()) {
            nonnulllist1 = (NonNullList) iterator.next();
            if (i < nonnulllist1.size()) {
                nonnulllist = nonnulllist1;
                break;
            }
        }

        return nonnulllist == null ? ItemStack.field_190927_a : (ItemStack) nonnulllist.get(i);
    }

    public String func_70005_c_() {
        return "container.inventory";
    }

    public boolean func_145818_k_() {
        return false;
    }

    public ITextComponent func_145748_c_() {
        return (ITextComponent) (this.func_145818_k_() ? new TextComponentString(this.func_70005_c_()) : new TextComponentTranslation(this.func_70005_c_(), new Object[0]));
    }

    public int func_70297_j_() {
        return maxStack; // CraftBukkit
    }

    public boolean func_184432_b(IBlockState iblockdata) {
        if (iblockdata.func_185904_a().func_76229_l()) {
            return true;
        } else {
            ItemStack itemstack = this.func_70301_a(this.field_70461_c);

            return !itemstack.func_190926_b() ? itemstack.func_150998_b(iblockdata) : false;
        }
    }

    public void func_70449_g(float f) {
        f /= 4.0F;
        if (f < 1.0F) {
            f = 1.0F;
        }

        for (int i = 0; i < this.field_70460_b.size(); ++i) {
            ItemStack itemstack = (ItemStack) this.field_70460_b.get(i);

            if (itemstack.func_77973_b() instanceof ItemArmor) {
                itemstack.func_77972_a((int) f, this.field_70458_d);
            }
        }

    }

    public void func_70436_m() {
        Iterator iterator = this.field_184440_g.iterator();

        while (iterator.hasNext()) {
            List list = (List) iterator.next();

            for (int i = 0; i < list.size(); ++i) {
                ItemStack itemstack = (ItemStack) list.get(i);

                if (!itemstack.func_190926_b()) {
                    this.field_70458_d.func_146097_a(itemstack, true, false);
                    list.set(i, ItemStack.field_190927_a);
                }
            }
        }

    }

    public void func_70296_d() {
        ++this.field_194017_h;
    }

    public void func_70437_b(ItemStack itemstack) {
        this.field_70457_g = itemstack;
    }

    public ItemStack func_70445_o() {
        // CraftBukkit start
        if (this.field_70457_g.func_190926_b()) {
            this.func_70437_b(ItemStack.field_190927_a);
        }
        // CraftBukkit end
        return this.field_70457_g;
    }

    public boolean func_70300_a(EntityPlayer entityhuman) {
        return this.field_70458_d.field_70128_L ? false : entityhuman.func_70068_e(this.field_70458_d) <= 64.0D;
    }

    public boolean func_70431_c(ItemStack itemstack) {
        Iterator iterator = this.field_184440_g.iterator();

        while (iterator.hasNext()) {
            List list = (List) iterator.next();
            Iterator iterator1 = list.iterator();

            while (iterator1.hasNext()) {
                ItemStack itemstack1 = (ItemStack) iterator1.next();

                if (!itemstack1.func_190926_b() && itemstack1.func_77969_a(itemstack)) {
                    return true;
                }
            }
        }

        return false;
    }

    public void func_174889_b(EntityPlayer entityhuman) {}

    public void func_174886_c(EntityPlayer entityhuman) {}

    public boolean func_94041_b(int i, ItemStack itemstack) {
        return true;
    }

    public void func_70455_b(InventoryPlayer playerinventory) {
        for (int i = 0; i < this.func_70302_i_(); ++i) {
            this.func_70299_a(i, playerinventory.func_70301_a(i));
        }

        this.field_70461_c = playerinventory.field_70461_c;
    }

    public int func_174887_a_(int i) {
        return 0;
    }

    public void func_174885_b(int i, int j) {}

    public int func_174890_g() {
        return 0;
    }

    public void func_174888_l() {
        Iterator iterator = this.field_184440_g.iterator();

        while (iterator.hasNext()) {
            List list = (List) iterator.next();

            list.clear();
        }

    }

    public void func_194016_a(RecipeItemHelper autorecipestackmanager, boolean flag) {
        Iterator iterator = this.field_70462_a.iterator();

        while (iterator.hasNext()) {
            ItemStack itemstack = (ItemStack) iterator.next();

            autorecipestackmanager.func_194112_a(itemstack);
        }

        if (flag) {
            autorecipestackmanager.func_194112_a((ItemStack) this.field_184439_c.get(0));
        }

    }
}
