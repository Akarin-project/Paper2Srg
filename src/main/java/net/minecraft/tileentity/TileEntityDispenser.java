package net.minecraft.tileentity;

import java.util.Iterator;
import java.util.Random;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerDispenser;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;

// CraftBukkit start
import java.util.List;

import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;
// CraftBukkit end

public class TileEntityDispenser extends TileEntityLockableLoot {

    private static final Random field_174913_f = new Random();
    private NonNullList<ItemStack> field_146022_i;

    // CraftBukkit start - add fields and methods
    public List<HumanEntity> transaction = new java.util.ArrayList<HumanEntity>();
    private int maxStack = MAX_STACK;

    public List<ItemStack> getContents() {
        return this.field_146022_i;
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

    public TileEntityDispenser() {
        this.field_146022_i = NonNullList.func_191197_a(9, ItemStack.field_190927_a);
    }

    public int func_70302_i_() {
        return 9;
    }

    public boolean func_191420_l() {
        Iterator iterator = this.field_146022_i.iterator();

        ItemStack itemstack;

        do {
            if (!iterator.hasNext()) {
                return true;
            }

            itemstack = (ItemStack) iterator.next();
        } while (itemstack.func_190926_b());

        return false;
    }

    public int func_146017_i() {
        this.func_184281_d((EntityPlayer) null);
        int i = -1;
        int j = 1;

        for (int k = 0; k < this.field_146022_i.size(); ++k) {
            if (!((ItemStack) this.field_146022_i.get(k)).func_190926_b() && TileEntityDispenser.field_174913_f.nextInt(j++) == 0) {
                i = k;
            }
        }

        return i;
    }

    public int func_146019_a(ItemStack itemstack) {
        for (int i = 0; i < this.field_146022_i.size(); ++i) {
            if (((ItemStack) this.field_146022_i.get(i)).func_190926_b()) {
                this.func_70299_a(i, itemstack);
                return i;
            }
        }

        return -1;
    }

    public String func_70005_c_() {
        return this.func_145818_k_() ? this.field_190577_o : "container.dispenser";
    }

    public static void func_189678_a(DataFixer dataconvertermanager) {
        dataconvertermanager.func_188258_a(FixTypes.BLOCK_ENTITY, (IDataWalker) (new ItemStackDataLists(TileEntityDispenser.class, new String[] { "Items"})));
    }

    public void func_145839_a(NBTTagCompound nbttagcompound) {
        super.func_145839_a(nbttagcompound);
        this.field_146022_i = NonNullList.func_191197_a(this.func_70302_i_(), ItemStack.field_190927_a);
        if (!this.func_184283_b(nbttagcompound)) {
            ItemStackHelper.func_191283_b(nbttagcompound, this.field_146022_i);
        }

        if (nbttagcompound.func_150297_b("CustomName", 8)) {
            this.field_190577_o = nbttagcompound.func_74779_i("CustomName");
        }

    }

    public NBTTagCompound func_189515_b(NBTTagCompound nbttagcompound) {
        super.func_189515_b(nbttagcompound);
        if (!this.func_184282_c(nbttagcompound)) {
            ItemStackHelper.func_191282_a(nbttagcompound, this.field_146022_i);
        }

        if (this.func_145818_k_()) {
            nbttagcompound.func_74778_a("CustomName", this.field_190577_o);
        }

        return nbttagcompound;
    }

    public int func_70297_j_() {
        return maxStack; // CraftBukkit
    }

    public String func_174875_k() {
        return "minecraft:dispenser";
    }

    public Container func_174876_a(InventoryPlayer playerinventory, EntityPlayer entityhuman) {
        this.func_184281_d(entityhuman);
        return new ContainerDispenser(playerinventory, this);
    }

    protected NonNullList<ItemStack> func_190576_q() {
        return this.field_146022_i;
    }
}
