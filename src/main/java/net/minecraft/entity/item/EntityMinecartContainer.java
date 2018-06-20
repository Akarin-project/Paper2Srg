package net.minecraft.entity.item;

import java.util.Iterator;
import java.util.Random;
import javax.annotation.Nullable;
import com.destroystokyo.paper.HopperPusher;
import com.destroystokyo.paper.loottable.CraftLootableInventory;
import com.destroystokyo.paper.loottable.CraftLootableInventoryData;
import com.destroystokyo.paper.loottable.LootableInventory;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.LockCode;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.ILootContainer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.InventoryHolder;

// CraftBukkit start
import java.util.List;
import org.bukkit.Location;

import com.destroystokyo.paper.HopperPusher; // Paper
import com.destroystokyo.paper.loottable.CraftLootableInventoryData; // Paper
import com.destroystokyo.paper.loottable.CraftLootableInventory; // Paper
import com.destroystokyo.paper.loottable.LootableInventory; // Paper
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.InventoryHolder;
// CraftBukkit end

// Paper start - push into hoppers
public abstract class EntityMinecartContainer extends EntityMinecart implements ILockableContainer, ILootContainer, CraftLootableInventory, HopperPusher { // Paper - CraftLootableInventory
    @Override
    public boolean acceptItem(TileEntityHopper hopper) {
        return TileEntityHopper.acceptItem(hopper, this);
    }

    @Override
    public void func_70071_h_() {
        super.func_70071_h_();
        tryPutInHopper();
    }

    @Override
    public void inactiveTick() {
        super.inactiveTick();
        tryPutInHopper();
    }
    // Paper end

    private NonNullList<ItemStack> field_94113_a;
    private boolean field_94112_b;
    private ResourceLocation field_184290_c;
    private long field_184291_d;public long getLootTableSeed() { return field_184291_d; } // Paper - OBFHELPER

    // CraftBukkit start
    public List<HumanEntity> transaction = new java.util.ArrayList<HumanEntity>();
    private int maxStack = MAX_STACK;

    public List<ItemStack> getContents() {
        return this.field_94113_a;
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

    public InventoryHolder getOwner() {
        org.bukkit.entity.Entity cart = getBukkitEntity();
        if(cart instanceof InventoryHolder) return (InventoryHolder) cart;
        return null;
    }

    public void setMaxStackSize(int size) {
        maxStack = size;
    }

    @Override
    public Location getLocation() {
        return getBukkitEntity().getLocation();
    }
    // CraftBukkit end

    public EntityMinecartContainer(World world) {
        super(world);
        this.field_94113_a = NonNullList.func_191197_a(this.func_70302_i_(), ItemStack.field_190927_a); // CraftBukkit - SPIGOT-3513
        this.field_94112_b = true;
    }

    public EntityMinecartContainer(World world, double d0, double d1, double d2) {
        super(world, d0, d1, d2);
        this.field_94113_a = NonNullList.func_191197_a(this.func_70302_i_(), ItemStack.field_190927_a); // CraftBukkit - SPIGOT-3513
        this.field_94112_b = true;
    }

    public void func_94095_a(DamageSource damagesource) {
        super.func_94095_a(damagesource);
        if (this.field_70170_p.func_82736_K().func_82766_b("doEntityDrops")) {
            InventoryHelper.func_180176_a(this.field_70170_p, this, this);
        }

    }

    public boolean func_191420_l() {
        Iterator iterator = this.field_94113_a.iterator();

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
        this.func_184288_f((EntityPlayer) null);
        return (ItemStack) this.field_94113_a.get(i);
    }

    public ItemStack func_70298_a(int i, int j) {
        this.func_184288_f((EntityPlayer) null);
        return ItemStackHelper.func_188382_a(this.field_94113_a, i, j);
    }

    public ItemStack func_70304_b(int i) {
        this.func_184288_f((EntityPlayer) null);
        ItemStack itemstack = (ItemStack) this.field_94113_a.get(i);

        if (itemstack.func_190926_b()) {
            return ItemStack.field_190927_a;
        } else {
            this.field_94113_a.set(i, ItemStack.field_190927_a);
            return itemstack;
        }
    }

    public void func_70299_a(int i, ItemStack itemstack) {
        this.func_184288_f((EntityPlayer) null);
        this.field_94113_a.set(i, itemstack);
        if (!itemstack.func_190926_b() && itemstack.func_190916_E() > this.func_70297_j_()) {
            itemstack.func_190920_e(this.func_70297_j_());
        }

    }

    public void func_70296_d() {}

    public boolean func_70300_a(EntityPlayer entityhuman) {
        return this.field_70128_L ? false : entityhuman.func_70068_e(this) <= 64.0D;
    }

    public void func_174889_b(EntityPlayer entityhuman) {}

    public void func_174886_c(EntityPlayer entityhuman) {}

    public boolean func_94041_b(int i, ItemStack itemstack) {
        return true;
    }

    public int func_70297_j_() {
        return maxStack; // CraftBukkit
    }

    @Nullable
    public Entity func_184204_a(int i) {
        this.field_94112_b = false;
        return super.func_184204_a(i);
    }

    public void func_70106_y() {
        if (this.field_94112_b) {
            InventoryHelper.func_180176_a(this.field_70170_p, this, this);
        }

        super.func_70106_y();
    }

    public void func_184174_b(boolean flag) {
        this.field_94112_b = flag;
    }

    public static void func_190574_b(DataFixer dataconvertermanager, Class<?> oclass) {
        EntityMinecart.func_189669_a(dataconvertermanager, oclass);
        dataconvertermanager.func_188258_a(FixTypes.ENTITY, (IDataWalker) (new ItemStackDataLists(oclass, new String[] { "Items"})));
    }

    protected void func_70014_b(NBTTagCompound nbttagcompound) {
        super.func_70014_b(nbttagcompound);
        lootableData.saveNbt(nbttagcompound); // Paper
        if (this.field_184290_c != null) {
            nbttagcompound.func_74778_a("LootTable", this.field_184290_c.toString());
            if (this.field_184291_d != 0L) {
                nbttagcompound.func_74772_a("LootTableSeed", this.field_184291_d);
            }
        } if (true) { // Paper - Always save the items, Table may stick around
            ItemStackHelper.func_191282_a(nbttagcompound, this.field_94113_a);
        }

    }

    protected void func_70037_a(NBTTagCompound nbttagcompound) {
        super.func_70037_a(nbttagcompound);
        lootableData.loadNbt(nbttagcompound); // Paper
        this.field_94113_a = NonNullList.func_191197_a(this.func_70302_i_(), ItemStack.field_190927_a);
        if (nbttagcompound.func_150297_b("LootTable", 8)) {
            this.field_184290_c = new ResourceLocation(nbttagcompound.func_74779_i("LootTable"));
            this.field_184291_d = nbttagcompound.func_74763_f("LootTableSeed");
        } if (true) { // Paper - always load the items, table may still remain
            ItemStackHelper.func_191283_b(nbttagcompound, this.field_94113_a);
        }

    }

    public boolean func_184230_a(EntityPlayer entityhuman, EnumHand enumhand) {
        if (!this.field_70170_p.field_72995_K) {
            entityhuman.func_71007_a(this);
        }

        return true;
    }

    protected void func_94101_h() {
        float f = 0.98F;

        if (this.field_184290_c == null) {
            int i = 15 - Container.func_94526_b((IInventory) this);

            f += (float) i * 0.001F;
        }

        this.field_70159_w *= (double) f;
        this.field_70181_x *= 0.0D;
        this.field_70179_y *= (double) f;
    }

    public int func_174887_a_(int i) {
        return 0;
    }

    public void func_174885_b(int i, int j) {}

    public int func_174890_g() {
        return 0;
    }

    public boolean func_174893_q_() {
        return false;
    }

    public void func_174892_a(LockCode chestlock) {}

    public LockCode func_174891_i() {
        return LockCode.field_180162_a;
    }

    public void func_184288_f(@Nullable EntityPlayer entityhuman) {
        if (lootableData.shouldReplenish(entityhuman)) { // Paper
            LootTable loottable = this.field_70170_p.func_184146_ak().func_186521_a(this.field_184290_c);

            lootableData.processRefill(entityhuman); // Paper
            Random random;

            if (this.field_184291_d == 0L) {
                random = new Random();
            } else {
                random = new Random(this.field_184291_d);
            }

            LootContext.a loottableinfo_a = new LootContext.a((WorldServer) this.field_70170_p);

            if (entityhuman != null) {
                loottableinfo_a.a(entityhuman.func_184817_da());
            }

            loottable.func_186460_a(this, random, loottableinfo_a.a());
        }

    }

    public void func_174888_l() {
        this.func_184288_f((EntityPlayer) null);
        this.field_94113_a.clear();
    }

    public void setLootTable(ResourceLocation key, long seed) { func_184289_a(key, seed);} // Paper - OBFHELPER
    public void func_184289_a(ResourceLocation minecraftkey, long i) {
        this.field_184290_c = minecraftkey;
        this.field_184291_d = i;
    }


    public ResourceLocation getLootTableKey() { return func_184276_b(); } // Paper - OBFHELPER
    public ResourceLocation func_184276_b() {
        return this.field_184290_c;
    }

    // Paper start
    private final CraftLootableInventoryData lootableData = new CraftLootableInventoryData(this);

    @Override
    public CraftLootableInventoryData getLootableData() {
        return lootableData;
    }

    @Override
    public LootableInventory getAPILootableInventory() {
        return (LootableInventory) this.getBukkitEntity();
    }

    @Override
    public World getNMSWorld() {
        return this.field_70170_p;
    }

    public String getLootTableName() {
        final ResourceLocation key = getLootTableKey();
        return key != null ? key.toString() : null;
    }

    @Override
    public String setLootTable(String name, long seed) {
        String prev = getLootTableName();
        setLootTable(new ResourceLocation(name), seed);
        return prev;
    }

    @Override
    public void clearLootTable() {
        //noinspection RedundantCast
        this.field_184290_c = (ResourceLocation) null;
    }
    // Paper end
}
