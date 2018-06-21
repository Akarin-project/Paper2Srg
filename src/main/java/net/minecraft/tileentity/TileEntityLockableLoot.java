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

    protected ResourceLocation field_184284_m;
    protected long field_184285_n; public long getLootTableSeed() { return field_184285_n; } // Paper - OBFHELPER
    protected String field_190577_o;

    public TileEntityLockableLoot() {}

    protected boolean func_184283_b(NBTTagCompound nbttagcompound) {
        lootableData.loadNbt(nbttagcompound); // Paper
        if (nbttagcompound.func_150297_b("LootTable", 8)) {
            this.field_184284_m = new ResourceLocation(nbttagcompound.func_74779_i("LootTable"));
            this.field_184285_n = nbttagcompound.func_74763_f("LootTableSeed");
            return false; // Paper - always load the items, table may still remain
        } else {
            return false;
        }
    }

    protected boolean func_184282_c(NBTTagCompound nbttagcompound) {
        lootableData.saveNbt(nbttagcompound); // Paper
        if (this.field_184284_m != null) {
            nbttagcompound.func_74778_a("LootTable", this.field_184284_m.toString());
            if (this.field_184285_n != 0L) {
                nbttagcompound.func_74772_a("LootTableSeed", this.field_184285_n);
            }

            return false; // Paper - always save the items, table may still remain
        } else {
            return false;
        }
    }

    public void func_184281_d(@Nullable EntityPlayer entityhuman) {
        if (lootableData.shouldReplenish(entityhuman)) { // Paper
            LootTable loottable = this.field_145850_b.func_184146_ak().func_186521_a(this.field_184284_m);

            lootableData.processRefill(entityhuman); // Paper
            Random random;

            if (this.field_184285_n == 0L) {
                random = new Random();
            } else {
                random = new Random(this.field_184285_n);
            }

            LootContext.Builder loottableinfo_a = new LootContext.Builder((WorldServer) this.field_145850_b);

            if (entityhuman != null) {
                loottableinfo_a.func_186469_a(entityhuman.func_184817_da());
            }

            loottable.func_186460_a(this, random, loottableinfo_a.func_186471_a());
        }

    }

    public ResourceLocation getLootTableKey() { return func_184276_b(); } // Paper - OBFHELPER
    public ResourceLocation func_184276_b() {
        return this.field_184284_m;
    }

    public void setLootTable(ResourceLocation key, long seed) { func_189404_a(key, seed);} // Paper - OBFHELPER
    public void func_189404_a(ResourceLocation minecraftkey, long i) {
        this.field_184284_m = minecraftkey;
        this.field_184285_n = i;
    }

    public boolean func_145818_k_() {
        return this.field_190577_o != null && !this.field_190577_o.isEmpty();
    }

    public void func_190575_a(String s) {
        this.field_190577_o = s;
    }

    public ItemStack func_70301_a(int i) {
        this.func_184281_d((EntityPlayer) null);
        return (ItemStack) this.func_190576_q().get(i);
    }

    public ItemStack func_70298_a(int i, int j) {
        this.func_184281_d((EntityPlayer) null);
        ItemStack itemstack = ItemStackHelper.func_188382_a(this.func_190576_q(), i, j);

        if (!itemstack.func_190926_b()) {
            this.func_70296_d();
        }

        return itemstack;
    }

    public ItemStack func_70304_b(int i) {
        this.func_184281_d((EntityPlayer) null);
        return ItemStackHelper.func_188383_a(this.func_190576_q(), i);
    }

    public void func_70299_a(int i, @Nullable ItemStack itemstack) {
        this.func_184281_d((EntityPlayer) null);
        this.func_190576_q().set(i, itemstack);
        if (itemstack.func_190916_E() > this.func_70297_j_()) {
            itemstack.func_190920_e(this.func_70297_j_());
        }

        this.func_70296_d();
    }

    public boolean func_70300_a(EntityPlayer entityhuman) {
        return this.field_145850_b.func_175625_s(this.field_174879_c) != this ? false : entityhuman.func_70092_e((double) this.field_174879_c.func_177958_n() + 0.5D, (double) this.field_174879_c.func_177956_o() + 0.5D, (double) this.field_174879_c.func_177952_p() + 0.5D) <= 64.0D;
    }

    public void func_174889_b(EntityPlayer entityhuman) {}

    public void func_174886_c(EntityPlayer entityhuman) {}

    public boolean func_94041_b(int i, ItemStack itemstack) {
        return true;
    }

    public int func_174887_a_(int i) {
        return 0;
    }

    public void func_174885_b(int i, int j) {}

    public int func_174890_g() {
        return 0;
    }

    public void func_174888_l() {
        this.func_184281_d((EntityPlayer) null);
        this.func_190576_q().clear();
    }

    protected abstract NonNullList<ItemStack> func_190576_q();

    // Paper start - LootTable API
    private final CraftLootableInventoryData lootableData = new CraftLootableInventoryData(this);

    @Override
    public CraftLootableInventoryData getLootableData() {
        return lootableData;
    }

    @Override
    public LootableInventory getAPILootableInventory() {
        return (LootableInventory) getBukkitWorld().getBlockAt(MCUtil.toLocation(field_145850_b, func_174877_v())).getState();
    }

    @Override
    public World getNMSWorld() {
        return field_145850_b;
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
        this.field_184284_m = (ResourceLocation) null;
    }
    // Paper end

}
