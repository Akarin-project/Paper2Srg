package net.minecraft.tileentity;

import java.util.Arrays;
import java.util.Iterator;

import java.util.List;
import net.minecraft.block.BlockBrewingStand;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerBrewingStand;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionHelper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.math.BlockPos;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.BrewingStandFuelEvent;
import org.bukkit.inventory.InventoryHolder;

// CraftBukkit start
import java.util.List;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.BrewingStandFuelEvent;
import org.bukkit.inventory.InventoryHolder;
// CraftBukkit end

public class TileEntityBrewingStand extends TileEntityLockable implements ITickable, ISidedInventory {

    private static final int[] field_145941_a = new int[] { 3};
    private static final int[] field_184277_f = new int[] { 0, 1, 2, 3};
    private static final int[] field_145947_i = new int[] { 0, 1, 2, 4};
    private NonNullList<ItemStack> field_145945_j;
    private int field_145946_k;
    private boolean[] field_145943_l;
    private Item field_145944_m;
    private String field_145942_n;
    private int field_184278_m;
    // CraftBukkit start - add fields and methods
    private int lastTick = MinecraftServer.currentTick;
    public List<HumanEntity> transaction = new java.util.ArrayList<HumanEntity>();
    private int maxStack = 64;

    public void onOpen(CraftHumanEntity who) {
        transaction.add(who);
    }

    public void onClose(CraftHumanEntity who) {
        transaction.remove(who);
    }

    public List<HumanEntity> getViewers() {
        return transaction;
    }

    public List<ItemStack> getContents() {
        return this.field_145945_j;
    }

    public void setMaxStackSize(int size) {
        maxStack = size;
    }
    // CraftBukkit end

    public TileEntityBrewingStand() {
        this.field_145945_j = NonNullList.func_191197_a(5, ItemStack.field_190927_a);
    }

    public String func_70005_c_() {
        return this.func_145818_k_() ? this.field_145942_n : "container.brewing";
    }

    public boolean func_145818_k_() {
        return this.field_145942_n != null && !this.field_145942_n.isEmpty();
    }

    public void func_145937_a(String s) {
        this.field_145942_n = s;
    }

    public int func_70302_i_() {
        return this.field_145945_j.size();
    }

    public boolean func_191420_l() {
        Iterator iterator = this.field_145945_j.iterator();

        ItemStack itemstack;

        do {
            if (!iterator.hasNext()) {
                return true;
            }

            itemstack = (ItemStack) iterator.next();
        } while (itemstack.func_190926_b());

        return false;
    }

    public void func_73660_a() {
        ItemStack itemstack = (ItemStack) this.field_145945_j.get(4);

        if (this.field_184278_m <= 0 && itemstack.func_77973_b() == Items.field_151065_br) {
            // CraftBukkit start
            BrewingStandFuelEvent event = new BrewingStandFuelEvent(field_145850_b.getWorld().getBlockAt(field_174879_c.func_177958_n(), field_174879_c.func_177956_o(), field_174879_c.func_177952_p()), CraftItemStack.asCraftMirror(itemstack), 20);
            this.field_145850_b.getServer().getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                return;
            }

            this.field_184278_m = event.getFuelPower(); // PAIL fuelLevel
            if (this.field_184278_m > 0 && event.isConsuming()) {
                itemstack.func_190918_g(1);
            }
            // CraftBukkit end
            this.func_70296_d();
        }

        boolean flag = this.func_145934_k();
        boolean flag1 = this.field_145946_k > 0;
        ItemStack itemstack1 = (ItemStack) this.field_145945_j.get(3);

        // CraftBukkit start - Use wall time instead of ticks for brewing
        int elapsedTicks = MinecraftServer.currentTick - this.lastTick;
        this.lastTick = MinecraftServer.currentTick;

        if (flag1) {
            this.field_145946_k -= elapsedTicks;
            boolean flag2 = this.field_145946_k <= 0; // == -> <=
            // CraftBukkit end

            if (flag2 && flag) {
                this.func_145940_l();
                this.func_70296_d();
            } else if (!flag) {
                this.field_145946_k = 0;
                this.func_70296_d();
            } else if (this.field_145944_m != itemstack1.func_77973_b()) {
                this.field_145946_k = 0;
                this.func_70296_d();
            }
        } else if (flag && this.field_184278_m > 0) {
            --this.field_184278_m;
            this.field_145946_k = 400;
            this.field_145944_m = itemstack1.func_77973_b();
            this.func_70296_d();
        }

        if (!this.field_145850_b.field_72995_K) {
            boolean[] aboolean = this.func_174902_m();

            if (!Arrays.equals(aboolean, this.field_145943_l)) {
                this.field_145943_l = aboolean;
                IBlockState iblockdata = this.field_145850_b.func_180495_p(this.func_174877_v());

                if (!(iblockdata.func_177230_c() instanceof BlockBrewingStand)) {
                    return;
                }

                for (int i = 0; i < BlockBrewingStand.field_176451_a.length; ++i) {
                    iblockdata = iblockdata.func_177226_a(BlockBrewingStand.field_176451_a[i], Boolean.valueOf(aboolean[i]));
                }

                this.field_145850_b.func_180501_a(this.field_174879_c, iblockdata, 2);
            }
        }

    }

    public boolean[] func_174902_m() {
        boolean[] aboolean = new boolean[3];

        for (int i = 0; i < 3; ++i) {
            if (!((ItemStack) this.field_145945_j.get(i)).func_190926_b()) {
                aboolean[i] = true;
            }
        }

        return aboolean;
    }

    private boolean func_145934_k() {
        ItemStack itemstack = (ItemStack) this.field_145945_j.get(3);

        if (itemstack.func_190926_b()) {
            return false;
        } else if (!PotionHelper.func_185205_a(itemstack)) {
            return false;
        } else {
            for (int i = 0; i < 3; ++i) {
                ItemStack itemstack1 = (ItemStack) this.field_145945_j.get(i);

                if (!itemstack1.func_190926_b() && PotionHelper.func_185208_a(itemstack1, itemstack)) {
                    return true;
                }
            }

            return false;
        }
    }

    private void func_145940_l() {
        ItemStack itemstack = (ItemStack) this.field_145945_j.get(3);
        // CraftBukkit start
        InventoryHolder owner = this.getOwner();
        if (owner != null) {
            BrewEvent event = new BrewEvent(field_145850_b.getWorld().getBlockAt(field_174879_c.func_177958_n(), field_174879_c.func_177956_o(), field_174879_c.func_177952_p()), (org.bukkit.inventory.BrewerInventory) owner.getInventory(), this.field_184278_m);
            org.bukkit.Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                return;
            }
        }
        // CraftBukkit end

        for (int i = 0; i < 3; ++i) {
            this.field_145945_j.set(i, PotionHelper.func_185212_d(itemstack, (ItemStack) this.field_145945_j.get(i)));
        }

        itemstack.func_190918_g(1);
        BlockPos blockposition = this.func_174877_v();

        if (itemstack.func_77973_b().func_77634_r()) {
            ItemStack itemstack1 = new ItemStack(itemstack.func_77973_b().func_77668_q());

            if (itemstack.func_190926_b()) {
                itemstack = itemstack1;
            } else {
                InventoryHelper.func_180173_a(this.field_145850_b, (double) blockposition.func_177958_n(), (double) blockposition.func_177956_o(), (double) blockposition.func_177952_p(), itemstack1);
            }
        }

        this.field_145945_j.set(3, itemstack);
        this.field_145850_b.func_175718_b(1035, blockposition, 0);
    }

    public static void func_189675_a(DataFixer dataconvertermanager) {
        dataconvertermanager.func_188258_a(FixTypes.BLOCK_ENTITY, (IDataWalker) (new ItemStackDataLists(TileEntityBrewingStand.class, new String[] { "Items"})));
    }

    public void func_145839_a(NBTTagCompound nbttagcompound) {
        super.func_145839_a(nbttagcompound);
        this.field_145945_j = NonNullList.func_191197_a(this.func_70302_i_(), ItemStack.field_190927_a);
        ItemStackHelper.func_191283_b(nbttagcompound, this.field_145945_j);
        this.field_145946_k = nbttagcompound.func_74765_d("BrewTime");
        if (nbttagcompound.func_150297_b("CustomName", 8)) {
            this.field_145942_n = nbttagcompound.func_74779_i("CustomName");
        }

        this.field_184278_m = nbttagcompound.func_74771_c("Fuel");
    }

    public NBTTagCompound func_189515_b(NBTTagCompound nbttagcompound) {
        super.func_189515_b(nbttagcompound);
        nbttagcompound.func_74777_a("BrewTime", (short) this.field_145946_k);
        ItemStackHelper.func_191282_a(nbttagcompound, this.field_145945_j);
        if (this.func_145818_k_()) {
            nbttagcompound.func_74778_a("CustomName", this.field_145942_n);
        }

        nbttagcompound.func_74774_a("Fuel", (byte) this.field_184278_m);
        return nbttagcompound;
    }

    public ItemStack func_70301_a(int i) {
        return i >= 0 && i < this.field_145945_j.size() ? (ItemStack) this.field_145945_j.get(i) : ItemStack.field_190927_a;
    }

    public ItemStack func_70298_a(int i, int j) {
        return ItemStackHelper.func_188382_a(this.field_145945_j, i, j);
    }

    public ItemStack func_70304_b(int i) {
        return ItemStackHelper.func_188383_a(this.field_145945_j, i);
    }

    public void func_70299_a(int i, ItemStack itemstack) {
        if (i >= 0 && i < this.field_145945_j.size()) {
            this.field_145945_j.set(i, itemstack);
        }

    }

    public int func_70297_j_() {
        return this.maxStack; // CraftBukkit
    }

    public boolean func_70300_a(EntityPlayer entityhuman) {
        return this.field_145850_b.func_175625_s(this.field_174879_c) != this ? false : entityhuman.func_70092_e((double) this.field_174879_c.func_177958_n() + 0.5D, (double) this.field_174879_c.func_177956_o() + 0.5D, (double) this.field_174879_c.func_177952_p() + 0.5D) <= 64.0D;
    }

    public void func_174889_b(EntityPlayer entityhuman) {}

    public void func_174886_c(EntityPlayer entityhuman) {}

    public boolean func_94041_b(int i, ItemStack itemstack) {
        if (i == 3) {
            return PotionHelper.func_185205_a(itemstack);
        } else {
            Item item = itemstack.func_77973_b();

            return i == 4 ? item == Items.field_151065_br : (item == Items.field_151068_bn || item == Items.field_185155_bH || item == Items.field_185156_bI || item == Items.field_151069_bo) && this.func_70301_a(i).func_190926_b();
        }
    }

    public int[] func_180463_a(EnumFacing enumdirection) {
        return enumdirection == EnumFacing.UP ? TileEntityBrewingStand.field_145941_a : (enumdirection == EnumFacing.DOWN ? TileEntityBrewingStand.field_184277_f : TileEntityBrewingStand.field_145947_i);
    }

    public boolean func_180462_a(int i, ItemStack itemstack, EnumFacing enumdirection) {
        return this.func_94041_b(i, itemstack);
    }

    public boolean func_180461_b(int i, ItemStack itemstack, EnumFacing enumdirection) {
        return i == 3 ? itemstack.func_77973_b() == Items.field_151069_bo : true;
    }

    public String func_174875_k() {
        return "minecraft:brewing_stand";
    }

    public Container func_174876_a(InventoryPlayer playerinventory, EntityPlayer entityhuman) {
        return new ContainerBrewingStand(playerinventory, this);
    }

    public int func_174887_a_(int i) {
        switch (i) {
        case 0:
            return this.field_145946_k;

        case 1:
            return this.field_184278_m;

        default:
            return 0;
        }
    }

    public void func_174885_b(int i, int j) {
        switch (i) {
        case 0:
            this.field_145946_k = j;
            break;

        case 1:
            this.field_184278_m = j;
        }

    }

    public int func_174890_g() {
        return 2;
    }

    public void func_174888_l() {
        this.field_145945_j.clear();
    }
}
