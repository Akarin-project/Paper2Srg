package net.minecraft.tileentity;

import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.SlotFurnaceFuel;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBoat;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.math.MathHelper;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;

// CraftBukkit start
import java.util.List;

import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
// CraftBukkit end

public class TileEntityFurnace extends TileEntityLockable implements ITickable, ISidedInventory {

    private static final int[] field_145962_k = new int[] { 0};
    private static final int[] field_145959_l = new int[] { 2, 1};
    private static final int[] field_145960_m = new int[] { 1};
    private NonNullList<ItemStack> field_145957_n;
    private int field_145956_a;
    private int field_145963_i;
    private int field_174906_k;
    private int field_174905_l;
    private String field_145958_o;

    // CraftBukkit start - add fields and methods
    private int lastTick = MinecraftServer.currentTick;
    private int maxStack = MAX_STACK;
    public List<HumanEntity> transaction = new java.util.ArrayList<HumanEntity>();

    public List<ItemStack> getContents() {
        return this.field_145957_n;
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

    public TileEntityFurnace() {
        this.field_145957_n = NonNullList.func_191197_a(3, ItemStack.field_190927_a);
    }

    public int func_70302_i_() {
        return this.field_145957_n.size();
    }

    public boolean func_191420_l() {
        Iterator iterator = this.field_145957_n.iterator();

        ItemStack itemstack;

        do {
            if (!iterator.hasNext()) {
                return true;
            }

            itemstack = (ItemStack) iterator.next();
        } while (itemstack.func_190926_b());

        return false;
    }

    public ItemStack func_70301_a(int i) {
        return (ItemStack) this.field_145957_n.get(i);
    }

    public ItemStack func_70298_a(int i, int j) {
        return ItemStackHelper.func_188382_a(this.field_145957_n, i, j);
    }

    public ItemStack func_70304_b(int i) {
        return ItemStackHelper.func_188383_a(this.field_145957_n, i);
    }

    public void func_70299_a(int i, ItemStack itemstack) {
        ItemStack itemstack1 = (ItemStack) this.field_145957_n.get(i);
        boolean flag = !itemstack.func_190926_b() && itemstack.func_77969_a(itemstack1) && ItemStack.func_77970_a(itemstack, itemstack1);

        this.field_145957_n.set(i, itemstack);
        if (itemstack.func_190916_E() > this.func_70297_j_()) {
            itemstack.func_190920_e(this.func_70297_j_());
        }

        if (i == 0 && !flag) {
            this.field_174905_l = this.func_174904_a(itemstack);
            this.field_174906_k = 0;
            this.func_70296_d();
        }

    }

    public String func_70005_c_() {
        return this.func_145818_k_() ? this.field_145958_o : "container.furnace";
    }

    public boolean func_145818_k_() {
        return this.field_145958_o != null && !this.field_145958_o.isEmpty();
    }

    public void func_145951_a(String s) {
        this.field_145958_o = s;
    }

    public static void func_189676_a(DataFixer dataconvertermanager) {
        dataconvertermanager.func_188258_a(FixTypes.BLOCK_ENTITY, (IDataWalker) (new ItemStackDataLists(TileEntityFurnace.class, new String[] { "Items"})));
    }

    public void func_145839_a(NBTTagCompound nbttagcompound) {
        super.func_145839_a(nbttagcompound);
        this.field_145957_n = NonNullList.func_191197_a(this.func_70302_i_(), ItemStack.field_190927_a);
        ItemStackHelper.func_191283_b(nbttagcompound, this.field_145957_n);
        this.field_145956_a = nbttagcompound.func_74765_d("BurnTime");
        this.field_174906_k = nbttagcompound.func_74765_d("CookTime");
        this.field_174905_l = nbttagcompound.func_74765_d("CookTimeTotal");
        this.field_145963_i = func_145952_a((ItemStack) this.field_145957_n.get(1));
        if (nbttagcompound.func_150297_b("CustomName", 8)) {
            this.field_145958_o = nbttagcompound.func_74779_i("CustomName");
        }

    }

    public NBTTagCompound func_189515_b(NBTTagCompound nbttagcompound) {
        super.func_189515_b(nbttagcompound);
        nbttagcompound.func_74777_a("BurnTime", (short) this.field_145956_a);
        nbttagcompound.func_74777_a("CookTime", (short) this.field_174906_k);
        nbttagcompound.func_74777_a("CookTimeTotal", (short) this.field_174905_l);
        ItemStackHelper.func_191282_a(nbttagcompound, this.field_145957_n);
        if (this.func_145818_k_()) {
            nbttagcompound.func_74778_a("CustomName", this.field_145958_o);
        }

        return nbttagcompound;
    }

    public int func_70297_j_() {
        return 64;
    }

    public boolean func_145950_i() {
        return this.field_145956_a > 0;
    }

    public void func_73660_a() {
        boolean flag = (this.func_145838_q() == Blocks.field_150470_am); // CraftBukkit - SPIGOT-844 - Check if furnace block is lit using the block instead of burn time
        boolean flag1 = false;

        // CraftBukkit start - Use wall time instead of ticks for cooking
        int elapsedTicks = MinecraftServer.currentTick - this.lastTick;
        this.lastTick = MinecraftServer.currentTick;

        // CraftBukkit - moved from below - edited for wall time
        if (this.func_145950_i() && this.func_145948_k()) {
            this.field_174906_k += elapsedTicks;
            if (this.field_174906_k >= this.field_174905_l) {
                this.field_174906_k -= this.field_174905_l; // Paper
                this.field_174905_l = this.func_174904_a((ItemStack) this.field_145957_n.get(0));
                this.func_145949_j();
                flag1 = true;
            }
        } else {
            this.field_174906_k = 0;
        }
        // CraftBukkit end

        if (this.func_145950_i()) {
            this.field_145956_a -= elapsedTicks; // CraftBukkit - use elapsedTicks in place of constant
        }

        if (!this.field_145850_b.field_72995_K) {
            ItemStack itemstack = (ItemStack) this.field_145957_n.get(1);

            if (!this.func_145950_i() && (itemstack.func_190926_b() || ((ItemStack) this.field_145957_n.get(0)).func_190926_b())) {
                if (!this.func_145950_i() && this.field_174906_k > 0) {
                    this.field_174906_k = MathHelper.func_76125_a(this.field_174906_k - 2, 0, this.field_174905_l);
                }
            } else {
                // CraftBukkit start - Handle multiple elapsed ticks
                if (this.field_145956_a <= 0 && this.func_145948_k()) { // CraftBukkit - == to <=
                    CraftItemStack fuel = CraftItemStack.asCraftMirror(itemstack);

                    FurnaceBurnEvent furnaceBurnEvent = new FurnaceBurnEvent(this.field_145850_b.getWorld().getBlockAt(field_174879_c.func_177958_n(), field_174879_c.func_177956_o(), field_174879_c.func_177952_p()), fuel, func_145952_a(itemstack));
                    this.field_145850_b.getServer().getPluginManager().callEvent(furnaceBurnEvent);

                    if (furnaceBurnEvent.isCancelled()) {
                        return;
                    }

                    this.field_145963_i = furnaceBurnEvent.getBurnTime();
                    this.field_145956_a += this.field_145963_i;
                    if (this.field_145956_a > 0 && furnaceBurnEvent.isBurning()) {
                        // CraftBukkit end
                        flag1 = true;
                        if (!itemstack.func_190926_b()) {
                            Item item = itemstack.func_77973_b();

                            itemstack.func_190918_g(1);
                            if (itemstack.func_190926_b()) {
                                Item item1 = item.func_77668_q();

                                this.field_145957_n.set(1, item1 == null ? ItemStack.field_190927_a : new ItemStack(item1));
                            }
                        }
                    }
                }

                /* CraftBukkit start - Moved up
                if (this.isBurning() && this.canBurn()) {
                    ++this.cookTime;
                    if (this.cookTime == this.cookTimeTotal) {
                        this.cookTime = 0;
                        this.cookTimeTotal = this.a((ItemStack) this.items.get(0));
                        this.burn();
                        flag1 = true;
                    }
                } else {
                    this.cookTime = 0;
                }
                */
            }

            if (flag != this.func_145950_i()) {
                flag1 = true;
                BlockFurnace.func_176446_a(this.func_145950_i(), this.field_145850_b, this.field_174879_c);
                this.func_145836_u(); // CraftBukkit - Invalidate tile entity's cached block type 
            }
        }

        if (flag1) {
            this.func_70296_d();
        }

    }

    public int func_174904_a(ItemStack itemstack) {
        return 200;
    }

    private boolean func_145948_k() {
        if (((ItemStack) this.field_145957_n.get(0)).func_190926_b()) {
            return false;
        } else {
            ItemStack itemstack = FurnaceRecipes.func_77602_a().func_151395_a((ItemStack) this.field_145957_n.get(0));

            if (itemstack.func_190926_b()) {
                return false;
            } else {
                ItemStack itemstack1 = (ItemStack) this.field_145957_n.get(2);

                // CraftBukkit - consider resultant count instead of current count
                return itemstack1.func_190926_b() ? true : (!itemstack1.func_77969_a(itemstack) ? false : (itemstack1.func_190916_E() + itemstack.func_190916_E() <= this.func_70297_j_() && itemstack1.func_190916_E() + itemstack.func_190916_E() < itemstack1.func_77976_d() ? true : itemstack1.func_190916_E() + itemstack.func_190916_E() <= itemstack.func_77976_d()));
            }
        }
    }

    public void func_145949_j() {
        if (this.func_145948_k()) {
            ItemStack itemstack = (ItemStack) this.field_145957_n.get(0);
            ItemStack itemstack1 = FurnaceRecipes.func_77602_a().func_151395_a(itemstack);
            ItemStack itemstack2 = (ItemStack) this.field_145957_n.get(2);

            // CraftBukkit start - fire FurnaceSmeltEvent
            CraftItemStack source = CraftItemStack.asCraftMirror(itemstack);
            org.bukkit.inventory.ItemStack result = CraftItemStack.asBukkitCopy(itemstack1);

            FurnaceSmeltEvent furnaceSmeltEvent = new FurnaceSmeltEvent(this.field_145850_b.getWorld().getBlockAt(field_174879_c.func_177958_n(), field_174879_c.func_177956_o(), field_174879_c.func_177952_p()), source, result);
            this.field_145850_b.getServer().getPluginManager().callEvent(furnaceSmeltEvent);

            if (furnaceSmeltEvent.isCancelled()) {
                return;
            }

            result = furnaceSmeltEvent.getResult();
            itemstack1 = CraftItemStack.asNMSCopy(result);

            if (!itemstack1.func_190926_b()) {
                if (itemstack2.func_190926_b()) {
                    this.field_145957_n.set(2, itemstack1.func_77946_l());
                } else if (CraftItemStack.asCraftMirror(itemstack2).isSimilar(result)) {
                    itemstack2.func_190917_f(itemstack1.func_190916_E());
                } else {
                    return;
                }
            }

            /*
            if (itemstack2.isEmpty()) {
                this.items.set(2, itemstack1.cloneItemStack());
            } else if (itemstack2.getItem() == itemstack1.getItem()) {
                itemstack2.add(1);
            }
            */
            // CraftBukkit end

            if (itemstack.func_77973_b() == Item.func_150898_a(Blocks.field_150360_v) && itemstack.func_77960_j() == 1 && !((ItemStack) this.field_145957_n.get(1)).func_190926_b() && ((ItemStack) this.field_145957_n.get(1)).func_77973_b() == Items.field_151133_ar) {
                this.field_145957_n.set(1, new ItemStack(Items.field_151131_as));
            }

            itemstack.func_190918_g(1);
        }
    }

    public static int func_145952_a(ItemStack itemstack) {
        if (itemstack.func_190926_b()) {
            return 0;
        } else {
            Item item = itemstack.func_77973_b();

            return item == Item.func_150898_a(Blocks.field_150376_bx) ? 150 : (item == Item.func_150898_a(Blocks.field_150325_L) ? 100 : (item == Item.func_150898_a(Blocks.field_150404_cg) ? 67 : (item == Item.func_150898_a(Blocks.field_150468_ap) ? 300 : (item == Item.func_150898_a(Blocks.field_150471_bO) ? 100 : (Block.func_149634_a(item).func_176223_P().func_185904_a() == Material.field_151575_d ? 300 : (item == Item.func_150898_a(Blocks.field_150402_ci) ? 16000 : (item instanceof ItemTool && "WOOD".equals(((ItemTool) item).func_77861_e()) ? 200 : (item instanceof ItemSword && "WOOD".equals(((ItemSword) item).func_150932_j()) ? 200 : (item instanceof ItemHoe && "WOOD".equals(((ItemHoe) item).func_77842_f()) ? 200 : (item == Items.field_151055_y ? 100 : (item != Items.field_151031_f && item != Items.field_151112_aM ? (item == Items.field_151155_ap ? 200 : (item == Items.field_151044_h ? 1600 : (item == Items.field_151129_at ? 20000 : (item != Item.func_150898_a(Blocks.field_150345_g) && item != Items.field_151054_z ? (item == Items.field_151072_bj ? 2400 : (item instanceof ItemDoor && item != Items.field_151139_aw ? 200 : (item instanceof ItemBoat ? 400 : 0))) : 100)))) : 300)))))))))));
        }
    }

    public static boolean func_145954_b(ItemStack itemstack) {
        return func_145952_a(itemstack) > 0;
    }

    public boolean func_70300_a(EntityPlayer entityhuman) {
        return this.field_145850_b.func_175625_s(this.field_174879_c) != this ? false : entityhuman.func_70092_e((double) this.field_174879_c.func_177958_n() + 0.5D, (double) this.field_174879_c.func_177956_o() + 0.5D, (double) this.field_174879_c.func_177952_p() + 0.5D) <= 64.0D;
    }

    public void func_174889_b(EntityPlayer entityhuman) {}

    public void func_174886_c(EntityPlayer entityhuman) {}

    public boolean func_94041_b(int i, ItemStack itemstack) {
        if (i == 2) {
            return false;
        } else if (i != 1) {
            return true;
        } else {
            ItemStack itemstack1 = (ItemStack) this.field_145957_n.get(1);

            return func_145954_b(itemstack) || SlotFurnaceFuel.func_178173_c_(itemstack) && itemstack1.func_77973_b() != Items.field_151133_ar;
        }
    }

    public int[] func_180463_a(EnumFacing enumdirection) {
        return enumdirection == EnumFacing.DOWN ? TileEntityFurnace.field_145959_l : (enumdirection == EnumFacing.UP ? TileEntityFurnace.field_145962_k : TileEntityFurnace.field_145960_m);
    }

    public boolean func_180462_a(int i, ItemStack itemstack, EnumFacing enumdirection) {
        return this.func_94041_b(i, itemstack);
    }

    public boolean func_180461_b(int i, ItemStack itemstack, EnumFacing enumdirection) {
        if (enumdirection == EnumFacing.DOWN && i == 1) {
            Item item = itemstack.func_77973_b();

            if (item != Items.field_151131_as && item != Items.field_151133_ar) {
                return false;
            }
        }

        return true;
    }

    public String func_174875_k() {
        return "minecraft:furnace";
    }

    public Container func_174876_a(InventoryPlayer playerinventory, EntityPlayer entityhuman) {
        return new ContainerFurnace(playerinventory, this);
    }

    public int func_174887_a_(int i) {
        switch (i) {
        case 0:
            return this.field_145956_a;

        case 1:
            return this.field_145963_i;

        case 2:
            return this.field_174906_k;

        case 3:
            return this.field_174905_l;

        default:
            return 0;
        }
    }

    public void func_174885_b(int i, int j) {
        switch (i) {
        case 0:
            this.field_145956_a = j;
            break;

        case 1:
            this.field_145963_i = j;
            break;

        case 2:
            this.field_174906_k = j;
            break;

        case 3:
            this.field_174905_l = j;
        }

    }

    public int func_174890_g() {
        return 4;
    }

    public void func_174888_l() {
        this.field_145957_n.clear();
    }
}
