package com.destroystokyo.paper.loottable;

import com.destroystokyo.paper.PaperWorldConfig;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class CraftLootableInventoryData {

    private static final Random RANDOM = new Random();

    private long lastFill = -1;
    private long nextRefill = -1;
    private int numRefills = 0;
    private Map<UUID, Long> lootedPlayers;
    private final CraftLootableInventory lootable;

    public CraftLootableInventoryData(CraftLootableInventory lootable) {
        this.lootable = lootable;
    }

    long getLastFill() {
        return this.lastFill;
    }

    long getNextRefill() {
        return this.nextRefill;
    }

    long setNextRefill(long nextRefill) {
        long prev = this.nextRefill;
        this.nextRefill = nextRefill;
        return prev;
    }

    CraftLootableInventory getLootable() {
        return lootable;
    }

    public boolean shouldReplenish(@Nullable EntityPlayer player) {
        String tableName = this.lootable.getLootTableName();

        // No Loot Table associated
        if (tableName == null) {
            return false;
        }

        // ALWAYS process the first fill
        if (this.lastFill == -1) {
            return true;
        }

        // Only process refills when a player is set
        if (player == null) {
            return false;
        }

        // Chest is not scheduled for refill
        if (this.nextRefill == -1) {
            return false;
        }

        final PaperWorldConfig paperConfig = this.lootable.getNMSWorld().paperConfig;

        // Check if max refills has been hit
        if (paperConfig.maxLootableRefills != -1 && this.numRefills >= paperConfig.maxLootableRefills) {
            return false;
        }

        // Refill has not been reached
        if (this.nextRefill > System.currentTimeMillis()) {
            return false;
        }


        final Player bukkitPlayer = (Player) player.getBukkitEntity();
        LootableInventoryReplenishEvent event = new LootableInventoryReplenishEvent(bukkitPlayer, lootable.getAPILootableInventory());
        if (paperConfig.restrictPlayerReloot && hasPlayerLooted(player.func_110124_au())) {
            event.setCancelled(true);
        }
        return event.callEvent();
    }
    public void processRefill(@Nullable EntityPlayer player) {
        this.lastFill = System.currentTimeMillis();
        final PaperWorldConfig paperConfig = this.lootable.getNMSWorld().paperConfig;
        if (paperConfig.autoReplenishLootables) {
            int min = paperConfig.lootableRegenMin;
            int max = paperConfig.lootableRegenMax;
            this.nextRefill = this.lastFill + (min + RANDOM.nextInt(max - min + 1)) * 1000L;
            this.numRefills++;
            if (paperConfig.changeLootTableSeedOnFill) {
                this.lootable.setLootTableSeed(0);
            }
            if (player != null) { // This means that numRefills can be incremented without a player being in the lootedPlayers list - Seems to be EntityMinecartChest specific
                this.setPlayerLootedState(player.func_110124_au(), true);
            }
        } else {
            this.lootable.clearLootTable();
        }
    }


    public void loadNbt(NBTTagCompound base) {
        if (!base.func_150297_b("Paper.LootableData", 10)) { // 10 = compound
            return;
        }
        NBTTagCompound comp = base.func_74775_l("Paper.LootableData");
        if (comp.func_74764_b("lastFill")) {
            this.lastFill = comp.func_74763_f("lastFill");
        }
        if (comp.func_74764_b("nextRefill")) {
            this.nextRefill = comp.func_74763_f("nextRefill");
        }

        if (comp.func_74764_b("numRefills")) {
            this.numRefills = comp.func_74762_e("numRefills");
        }
        if (comp.func_150297_b("lootedPlayers", 9)) { // 9 = list
            NBTTagList list = comp.func_150295_c("lootedPlayers", 10); // 10 = compound
            final int size = list.func_74745_c();
            if (size > 0) {
                this.lootedPlayers = new HashMap<>(list.func_74745_c());
            }
            for (int i = 0; i < size; i++) {
                final NBTTagCompound cmp = list.func_150305_b(i);
                lootedPlayers.put(cmp.getUUID("UUID"), cmp.func_74763_f("Time"));
            }
        }
    }
    public void saveNbt(NBTTagCompound base) {
        NBTTagCompound comp = new NBTTagCompound();
        if (this.nextRefill != -1) {
            comp.func_74772_a("nextRefill", this.nextRefill);
        }
        if (this.lastFill != -1) {
            comp.func_74772_a("lastFill", this.lastFill);
        }
        if (this.numRefills != 0) {
            comp.func_74768_a("numRefills", this.numRefills);
        }
        if (this.lootedPlayers != null && !this.lootedPlayers.isEmpty()) {
            NBTTagList list = new NBTTagList();
            for (Map.Entry<UUID, Long> entry : this.lootedPlayers.entrySet()) {
                NBTTagCompound cmp = new NBTTagCompound();
                cmp.setUUID("UUID", entry.getKey());
                cmp.func_74772_a("Time", entry.getValue());
                list.func_74742_a(cmp);
            }
            comp.func_74782_a("lootedPlayers", list);
        }

        if (!comp.func_82582_d()) {
            base.func_74782_a("Paper.LootableData", comp);
        }
    }

    void setPlayerLootedState(UUID player, boolean looted) {
        if (looted && this.lootedPlayers == null) {
            this.lootedPlayers = new HashMap<>();
        }
        if (looted) {
            if (!this.lootedPlayers.containsKey(player)) {
                this.lootedPlayers.put(player, System.currentTimeMillis());
            }
        } else if (this.lootedPlayers != null) {
            this.lootedPlayers.remove(player);
        }
    }

    boolean hasPlayerLooted(UUID player) {
        return this.lootedPlayers != null && this.lootedPlayers.containsKey(player);
    }

    Long getLastLooted(UUID player) {
        return lootedPlayers != null ? lootedPlayers.get(player) : null;
    }
}
