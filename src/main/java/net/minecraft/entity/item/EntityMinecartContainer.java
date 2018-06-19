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
    public void onUpdate() {
        super.onUpdate();
        tryPutInHopper();
    }

    @Override
    public void inactiveTick() {
        super.inactiveTick();
        tryPutInHopper();
    }
    // Paper end

    private NonNullList<ItemStack> minecartContainerItems;
    private boolean dropContentsWhenDead;
    private ResourceLocation lootTable;
    private long lootTableSeed;@Override
    public long getLootTableSeed() { return lootTableSeed; } // Paper - OBFHELPER

    // CraftBukkit start
    public List<HumanEntity> transaction = new java.util.ArrayList<HumanEntity>();
    private int maxStack = MAX_STACK;

    @Override
    public List<ItemStack> getContents() {
        return this.minecartContainerItems;
    }

    @Override
    public void onOpen(CraftHumanEntity who) {
        transaction.add(who);
    }

    @Override
    public void onClose(CraftHumanEntity who) {
        transaction.remove(who);
    }

    @Override
    public List<HumanEntity> getViewers() {
        return transaction;
    }

    @Override
    public InventoryHolder getOwner() {
        org.bukkit.entity.Entity cart = getBukkitEntity();
        if(cart instanceof InventoryHolder) return (InventoryHolder) cart;
        return null;
    }

    @Override
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
        this.minecartContainerItems = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY); // CraftBukkit - SPIGOT-3513
        this.dropContentsWhenDead = true;
    }

    public EntityMinecartContainer(World world, double d0, double d1, double d2) {
        super(world, d0, d1, d2);
        this.minecartContainerItems = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY); // CraftBukkit - SPIGOT-3513
        this.dropContentsWhenDead = true;
    }

    @Override
    public void killMinecart(DamageSource damagesource) {
        super.killMinecart(damagesource);
        if (this.world.getGameRules().getBoolean("doEntityDrops")) {
            InventoryHelper.dropInventoryItems(this.world, this, this);
        }

    }

    @Override
    public boolean isEmpty() {
        Iterator iterator = this.minecartContainerItems.iterator();

        ItemStack itemstack;

        do {
            if (!iterator.hasNext()) {
                return true;
            }

            itemstack = (ItemStack) iterator.next();
        } while (itemstack.isEmpty());

        return false;
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        this.addLoot((EntityPlayer) null);
        return this.minecartContainerItems.get(i);
    }

    @Override
    public ItemStack decrStackSize(int i, int j) {
        this.addLoot((EntityPlayer) null);
        return ItemStackHelper.getAndSplit(this.minecartContainerItems, i, j);
    }

    @Override
    public ItemStack removeStackFromSlot(int i) {
        this.addLoot((EntityPlayer) null);
        ItemStack itemstack = this.minecartContainerItems.get(i);

        if (itemstack.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            this.minecartContainerItems.set(i, ItemStack.EMPTY);
            return itemstack;
        }
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack) {
        this.addLoot((EntityPlayer) null);
        this.minecartContainerItems.set(i, itemstack);
        if (!itemstack.isEmpty() && itemstack.getCount() > this.getInventoryStackLimit()) {
            itemstack.setCount(this.getInventoryStackLimit());
        }

    }

    @Override
    public void markDirty() {}

    @Override
    public boolean isUsableByPlayer(EntityPlayer entityhuman) {
        return this.isDead ? false : entityhuman.getDistanceSq(this) <= 64.0D;
    }

    @Override
    public void openInventory(EntityPlayer entityhuman) {}

    @Override
    public void closeInventory(EntityPlayer entityhuman) {}

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return true;
    }

    @Override
    public int getInventoryStackLimit() {
        return maxStack; // CraftBukkit
    }

    @Override
    @Nullable
    public Entity changeDimension(int i) {
        this.dropContentsWhenDead = false;
        return super.changeDimension(i);
    }

    @Override
    public void setDead() {
        if (this.dropContentsWhenDead) {
            InventoryHelper.dropInventoryItems(this.world, this, this);
        }

        super.setDead();
    }

    @Override
    public void setDropItemsWhenDead(boolean flag) {
        this.dropContentsWhenDead = flag;
    }

    public static void addDataFixers(DataFixer dataconvertermanager, Class<?> oclass) {
        EntityMinecart.registerFixesMinecart(dataconvertermanager, oclass);
        dataconvertermanager.registerWalker(FixTypes.ENTITY, (new ItemStackDataLists(oclass, new String[] { "Items"})));
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        lootableData.saveNbt(nbttagcompound); // Paper
        if (this.lootTable != null) {
            nbttagcompound.setString("LootTable", this.lootTable.toString());
            if (this.lootTableSeed != 0L) {
                nbttagcompound.setLong("LootTableSeed", this.lootTableSeed);
            }
        } if (true) { // Paper - Always save the items, Table may stick around
            ItemStackHelper.saveAllItems(nbttagcompound, this.minecartContainerItems);
        }

    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        lootableData.loadNbt(nbttagcompound); // Paper
        this.minecartContainerItems = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        if (nbttagcompound.hasKey("LootTable", 8)) {
            this.lootTable = new ResourceLocation(nbttagcompound.getString("LootTable"));
            this.lootTableSeed = nbttagcompound.getLong("LootTableSeed");
        } if (true) { // Paper - always load the items, table may still remain
            ItemStackHelper.loadAllItems(nbttagcompound, this.minecartContainerItems);
        }

    }

    @Override
    public boolean processInitialInteract(EntityPlayer entityhuman, EnumHand enumhand) {
        if (!this.world.isRemote) {
            entityhuman.displayGUIChest(this);
        }

        return true;
    }

    @Override
    protected void applyDrag() {
        float f = 0.98F;

        if (this.lootTable == null) {
            int i = 15 - Container.calcRedstoneFromInventory(this);

            f += i * 0.001F;
        }

        this.motionX *= f;
        this.motionY *= 0.0D;
        this.motionZ *= f;
    }

    @Override
    public int getField(int i) {
        return 0;
    }

    @Override
    public void setField(int i, int j) {}

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public boolean isLocked() {
        return false;
    }

    @Override
    public void setLockCode(LockCode chestlock) {}

    @Override
    public LockCode getLockCode() {
        return LockCode.EMPTY_CODE;
    }

    public void addLoot(@Nullable EntityPlayer entityhuman) {
        if (lootableData.shouldReplenish(entityhuman)) { // Paper
            LootTable loottable = this.world.getLootTableManager().getLootTableFromLocation(this.lootTable);

            lootableData.processRefill(entityhuman); // Paper
            Random random;

            if (this.lootTableSeed == 0L) {
                random = new Random();
            } else {
                random = new Random(this.lootTableSeed);
            }

            LootContext.a loottableinfo_a = new LootContext.a((WorldServer) this.world);

            if (entityhuman != null) {
                loottableinfo_a.a(entityhuman.getLuck());
            }

            loottable.fillInventory(this, random, loottableinfo_a.a());
        }

    }

    @Override
    public void clear() {
        this.addLoot((EntityPlayer) null);
        this.minecartContainerItems.clear();
    }

    public void setLootTable(ResourceLocation minecraftkey, long i) {
        this.lootTable = minecraftkey;
        this.lootTableSeed = i;
    }


    public ResourceLocation getLootTableKey() { return getLootTable(); } // Paper - OBFHELPER
    @Override
    public ResourceLocation getLootTable() {
        return this.lootTable;
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
        return this.world;
    }

    @Override
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
        this.lootTable = null;
    }
    // Paper end
}
