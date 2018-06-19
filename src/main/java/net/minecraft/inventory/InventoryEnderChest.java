package net.minecraft.inventory;

import org.bukkit.Location;
import org.bukkit.inventory.InventoryHolder;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntityEnderChest;

public class InventoryEnderChest extends InventoryBasic {

    private TileEntityEnderChest field_70488_a; public TileEntityEnderChest getTileEntity() { return field_70488_a; } // Paper - OBFHELPER
    // CraftBukkit start
    private final EntityPlayer owner;

    public InventoryHolder getBukkitOwner() {
        return owner.getBukkitEntity();
    }

    @Override
    public Location getLocation() {
        if (getTileEntity() == null) return null; // Paper - return null if there is no TE bound (opened by plugin)
        return new Location(this.field_70488_a.func_145831_w().getWorld(), this.field_70488_a.func_174877_v().func_177958_n(), this.field_70488_a.func_174877_v().func_177956_o(), this.field_70488_a.func_174877_v().func_177952_p());
    }

    public InventoryEnderChest(EntityPlayer owner) {
        super("container.enderchest", false, 27);
        this.owner = owner;
        // CraftBukkit end
    }

    public void func_146031_a(TileEntityEnderChest tileentityenderchest) {
        this.field_70488_a = tileentityenderchest;
    }

    public void func_70486_a(NBTTagList nbttaglist) {
        int i;

        for (i = 0; i < this.func_70302_i_(); ++i) {
            this.func_70299_a(i, ItemStack.field_190927_a);
        }

        for (i = 0; i < nbttaglist.func_74745_c(); ++i) {
            NBTTagCompound nbttagcompound = nbttaglist.func_150305_b(i);
            int j = nbttagcompound.func_74771_c("Slot") & 255;

            if (j >= 0 && j < this.func_70302_i_()) {
                this.func_70299_a(j, new ItemStack(nbttagcompound));
            }
        }

    }

    public NBTTagList func_70487_g() {
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.func_70302_i_(); ++i) {
            ItemStack itemstack = this.func_70301_a(i);

            if (!itemstack.func_190926_b()) {
                NBTTagCompound nbttagcompound = new NBTTagCompound();

                nbttagcompound.func_74774_a("Slot", (byte) i);
                itemstack.func_77955_b(nbttagcompound);
                nbttaglist.func_74742_a(nbttagcompound);
            }
        }

        return nbttaglist;
    }

    public boolean func_70300_a(EntityPlayer entityhuman) {
        return this.field_70488_a != null && !this.field_70488_a.func_145971_a(entityhuman) ? false : super.func_70300_a(entityhuman);
    }

    public void func_174889_b(EntityPlayer entityhuman) {
        if (this.field_70488_a != null) {
            this.field_70488_a.func_145969_a();
        }

        super.func_174889_b(entityhuman);
    }

    public void func_174886_c(EntityPlayer entityhuman) {
        if (this.field_70488_a != null) {
            this.field_70488_a.func_145970_b();
        }

        super.func_174886_c(entityhuman);
        this.field_70488_a = null;
    }
}
