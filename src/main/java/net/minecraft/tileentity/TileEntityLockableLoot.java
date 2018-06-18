package net.minecraft.tileentity;


import java.util.Random;
import javax.annotation.Nullable;

import com.destroystokyo.paper.loottable.CraftLootableInventory;
import com.destroystokyo.paper.loottable.CraftLootableInventoryData;
import com.destroystokyo.paper.loottable.LootableInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MCUtil;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.ILootContainer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;

public abstract class TileEntityLockableLoot extends TileEntityLockable implements ILootContainer, CraftLootableInventory { // Paper

    protected ResourceLocation lootTable;
    protected long lootTableSeed; public long getLootTableSeed() { return lootTableSeed; } // Paper - OBFHELPER
    protected String customName;

    public TileEntityLockableLoot() {}

    protected boolean checkLootAndRead(NBTTagCompound nbttagcompound) {
        lootableData.loadNbt(nbttagcompound); // Paper
        if (nbttagcompound.hasKey("LootTable", 8)) {
            this.lootTable = new ResourceLocation(nbttagcompound.getString("LootTable"));
            this.lootTableSeed = nbttagcompound.getLong("LootTableSeed");
            return false; // Paper - always load the items, table may still remain
        } else {
            return false;
        }
    }

    protected boolean checkLootAndWrite(NBTTagCompound nbttagcompound) {
        lootableData.saveNbt(nbttagcompound); // Paper
        if (this.lootTable != null) {
            nbttagcompound.setString("LootTable", this.lootTable.toString());
            if (this.lootTableSeed != 0L) {
                nbttagcompound.setLong("LootTableSeed", this.lootTableSeed);
            }

            return false; // Paper - always save the items, table may still remain
        } else {
            return false;
        }
    }

    public void fillWithLoot(@Nullable EntityPlayer entityhuman) {
        if (lootableData.shouldReplenish(entityhuman)) { // Paper
            LootTable loottable = this.world.getLootTableManager().getLootTableFromLocation(this.lootTable);

            lootableData.processRefill(entityhuman); // Paper
            Random random;

            if (this.lootTableSeed == 0L) {
                random = new Random();
            } else {
                random = new Random(this.lootTableSeed);
            }

            LootTableInfo.a loottableinfo_a = new LootTableInfo.a((WorldServer) this.world);

            if (entityhuman != null) {
                loottableinfo_a.a(entityhuman.getLuck());
            }

            loottable.fillInventory(this, random, loottableinfo_a.a());
        }

    }

    public ResourceLocation getLootTableKey() { return getLootTable(); } // Paper - OBFHELPER
    public ResourceLocation getLootTable() {
        return this.lootTable;
    }

    public void setLootTable(ResourceLocation key, long seed) { setLootTable(key, seed);} // Paper - OBFHELPER
    public void setLootTable(ResourceLocation minecraftkey, long i) {
        this.lootTable = minecraftkey;
        this.lootTableSeed = i;
    }

    public boolean hasCustomName() {
        return this.customName != null && !this.customName.isEmpty();
    }

    public void setCustomName(String s) {
        this.customName = s;
    }

    public ItemStack getStackInSlot(int i) {
        this.fillWithLoot((EntityPlayer) null);
        return (ItemStack) this.getItems().get(i);
    }

    public ItemStack decrStackSize(int i, int j) {
        this.fillWithLoot((EntityPlayer) null);
        ItemStack itemstack = ItemStackHelper.getAndSplit(this.getItems(), i, j);

        if (!itemstack.isEmpty()) {
            this.markDirty();
        }

        return itemstack;
    }

    public ItemStack removeStackFromSlot(int i) {
        this.fillWithLoot((EntityPlayer) null);
        return ItemStackHelper.getAndRemove(this.getItems(), i);
    }

    public void setInventorySlotContents(int i, @Nullable ItemStack itemstack) {
        this.fillWithLoot((EntityPlayer) null);
        this.getItems().set(i, itemstack);
        if (itemstack.getCount() > this.getInventoryStackLimit()) {
            itemstack.setCount(this.getInventoryStackLimit());
        }

        this.markDirty();
    }

    public boolean isUsableByPlayer(EntityPlayer entityhuman) {
        return this.world.getTileEntity(this.pos) != this ? false : entityhuman.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
    }

    public void openInventory(EntityPlayer entityhuman) {}

    public void closeInventory(EntityPlayer entityhuman) {}

    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return true;
    }

    public int getField(int i) {
        return 0;
    }

    public void setField(int i, int j) {}

    public int getFieldCount() {
        return 0;
    }

    public void clear() {
        this.fillWithLoot((EntityPlayer) null);
        this.getItems().clear();
    }

    protected abstract NonNullList<ItemStack> getItems();

    // Paper start - LootTable API
    private final CraftLootableInventoryData lootableData = new CraftLootableInventoryData(this);

    @Override
    public CraftLootableInventoryData getLootableData() {
        return lootableData;
    }

    @Override
    public LootableInventory getAPILootableInventory() {
        return (LootableInventory) getBukkitWorld().getBlockAt(MCUtil.toLocation(world, getPos())).getState();
    }

    @Override
    public World getNMSWorld() {
        return world;
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
        this.lootTable = (ResourceLocation) null;
    }
    // Paper end

}
