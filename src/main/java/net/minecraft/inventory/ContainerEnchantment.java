package net.minecraft.inventory;

import java.util.List;
import java.util.Random;

import java.util.Map;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.inventory.CraftInventoryEnchanting;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.entity.Player;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;

// CraftBukkit start
import java.util.Collections;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.craftbukkit.inventory.CraftInventoryEnchanting;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.entity.Player;
// CraftBukkit end

public class ContainerEnchantment extends Container {

    public IInventory field_75168_e = new InventoryBasic("Enchant", true, 2) {
        public int func_70297_j_() {
            return 64;
        }

        public void func_70296_d() {
            super.func_70296_d();
            ContainerEnchantment.this.func_75130_a((IInventory) this);
        }

        // CraftBukkit start
        @Override
        public Location getLocation() {
            return new org.bukkit.Location(field_75172_h.getWorld(), field_178150_j.func_177958_n(), field_178150_j.func_177956_o(), field_178150_j.func_177952_p());
        }
        // CraftBukkit end
    };
    public World field_75172_h;
    private final BlockPos field_178150_j;
    private final Random field_75169_l = new Random();
    public int field_178149_f;
    public int[] field_75167_g = new int[3];
    public int[] field_185001_h = new int[] { -1, -1, -1};
    public int[] field_185002_i = new int[] { -1, -1, -1};
    // CraftBukkit start
    private CraftInventoryView bukkitEntity = null;
    private Player player;
    // CraftBukkit end

    public ContainerEnchantment(InventoryPlayer playerinventory, World world, BlockPos blockposition) {
        this.field_75172_h = world;
        this.field_178150_j = blockposition;
        this.field_178149_f = playerinventory.field_70458_d.func_175138_ci();
        this.func_75146_a(new Slot(this.field_75168_e, 0, 15, 47) {
            public boolean func_75214_a(ItemStack itemstack) {
                return true;
            }

            public int func_75219_a() {
                return 1;
            }
        });
        this.func_75146_a(new Slot(this.field_75168_e, 1, 35, 47) {
            public boolean func_75214_a(ItemStack itemstack) {
                return itemstack.func_77973_b() == Items.field_151100_aR && EnumDyeColor.func_176766_a(itemstack.func_77960_j()) == EnumDyeColor.BLUE;
            }
        });

        int i;

        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.func_75146_a(new Slot(playerinventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i) {
            this.func_75146_a(new Slot(playerinventory, i, 8 + i * 18, 142));
        }

        // CraftBukkit start
        player = (Player) playerinventory.field_70458_d.getBukkitEntity();
        // CraftBukkit end
    }

    protected void func_185000_c(IContainerListener icrafting) {
        icrafting.func_71112_a(this, 0, this.field_75167_g[0]);
        icrafting.func_71112_a(this, 1, this.field_75167_g[1]);
        icrafting.func_71112_a(this, 2, this.field_75167_g[2]);
        icrafting.func_71112_a(this, 3, this.field_178149_f & -16);
        icrafting.func_71112_a(this, 4, this.field_185001_h[0]);
        icrafting.func_71112_a(this, 5, this.field_185001_h[1]);
        icrafting.func_71112_a(this, 6, this.field_185001_h[2]);
        icrafting.func_71112_a(this, 7, this.field_185002_i[0]);
        icrafting.func_71112_a(this, 8, this.field_185002_i[1]);
        icrafting.func_71112_a(this, 9, this.field_185002_i[2]);
    }

    public void func_75132_a(IContainerListener icrafting) {
        super.func_75132_a(icrafting);
        this.func_185000_c(icrafting);
    }

    public void func_75142_b() {
        super.func_75142_b();

        for (int i = 0; i < this.field_75149_d.size(); ++i) {
            IContainerListener icrafting = (IContainerListener) this.field_75149_d.get(i);

            this.func_185000_c(icrafting);
        }

    }

    public void func_75130_a(IInventory iinventory) {
        if (iinventory == this.field_75168_e) {
            ItemStack itemstack = iinventory.func_70301_a(0);
            int i;

            if (!itemstack.func_190926_b()) { // CraftBukkit - relax condition
                if (!this.field_75172_h.field_72995_K) {
                    i = 0;

                    int j;

                    for (j = -1; j <= 1; ++j) {
                        for (int k = -1; k <= 1; ++k) {
                            if ((j != 0 || k != 0) && this.field_75172_h.func_175623_d(this.field_178150_j.func_177982_a(k, 0, j)) && this.field_75172_h.func_175623_d(this.field_178150_j.func_177982_a(k, 1, j))) {
                                if (this.field_75172_h.func_180495_p(this.field_178150_j.func_177982_a(k * 2, 0, j * 2)).func_177230_c() == Blocks.field_150342_X) {
                                    ++i;
                                }

                                if (this.field_75172_h.func_180495_p(this.field_178150_j.func_177982_a(k * 2, 1, j * 2)).func_177230_c() == Blocks.field_150342_X) {
                                    ++i;
                                }

                                if (k != 0 && j != 0) {
                                    if (this.field_75172_h.func_180495_p(this.field_178150_j.func_177982_a(k * 2, 0, j)).func_177230_c() == Blocks.field_150342_X) {
                                        ++i;
                                    }

                                    if (this.field_75172_h.func_180495_p(this.field_178150_j.func_177982_a(k * 2, 1, j)).func_177230_c() == Blocks.field_150342_X) {
                                        ++i;
                                    }

                                    if (this.field_75172_h.func_180495_p(this.field_178150_j.func_177982_a(k, 0, j * 2)).func_177230_c() == Blocks.field_150342_X) {
                                        ++i;
                                    }

                                    if (this.field_75172_h.func_180495_p(this.field_178150_j.func_177982_a(k, 1, j * 2)).func_177230_c() == Blocks.field_150342_X) {
                                        ++i;
                                    }
                                }
                            }
                        }
                    }

                    this.field_75169_l.setSeed((long) this.field_178149_f);

                    for (j = 0; j < 3; ++j) {
                        this.field_75167_g[j] = EnchantmentHelper.func_77514_a(this.field_75169_l, j, i, itemstack);
                        this.field_185001_h[j] = -1;
                        this.field_185002_i[j] = -1;
                        if (this.field_75167_g[j] < j + 1) {
                            this.field_75167_g[j] = 0;
                        }
                    }

                    for (j = 0; j < 3; ++j) {
                        if (this.field_75167_g[j] > 0) {
                            List list = this.func_178148_a(itemstack, j, this.field_75167_g[j]);

                            if (list != null && !list.isEmpty()) {
                                EnchantmentData weightedrandomenchant = (EnchantmentData) list.get(this.field_75169_l.nextInt(list.size()));

                                this.field_185001_h[j] = Enchantment.func_185258_b(weightedrandomenchant.field_76302_b);
                                this.field_185002_i[j] = weightedrandomenchant.field_76303_c;
                            }
                        }
                    }

                    // CraftBukkit start
                    CraftItemStack item = CraftItemStack.asCraftMirror(itemstack);
                    org.bukkit.enchantments.EnchantmentOffer[] offers = new EnchantmentOffer[3];
                    for (j = 0; j < 3; ++j) {
                        org.bukkit.enchantments.Enchantment enchantment = (this.field_185001_h[j] >= 0) ? org.bukkit.enchantments.Enchantment.getById(this.field_185001_h[j]) : null;
                        offers[j] = (enchantment != null) ? new EnchantmentOffer(enchantment, this.field_185002_i[j], this.field_75167_g[j]) : null;
                    }

                    PrepareItemEnchantEvent event = new PrepareItemEnchantEvent(player, this.getBukkitView(), this.field_75172_h.getWorld().getBlockAt(field_178150_j.func_177958_n(), field_178150_j.func_177956_o(), field_178150_j.func_177952_p()), item, offers, i);
                    event.setCancelled(!itemstack.func_77956_u());
                    this.field_75172_h.getServer().getPluginManager().callEvent(event);

                    if (event.isCancelled()) {
                        for (j = 0; j < 3; ++j) {
                            this.field_75167_g[j] = 0;
                            this.field_185001_h[j] = -1;
                            this.field_185002_i[j] = -1;
                        }
                        return;
                    }

                    for (j = 0; j < 3; j++) {
                        EnchantmentOffer offer = event.getOffers()[j];
                        if (offer != null) {
                            this.field_75167_g[j] = offer.getCost();
                            this.field_185001_h[j] = offer.getEnchantment().getId();
                            this.field_185002_i[j] = offer.getEnchantmentLevel();
                        } else {
                            this.field_75167_g[j] = 0;
                            this.field_185001_h[j] = -1;
                            this.field_185002_i[j] = -1;
                        }
                    }
                    // CraftBukkit end

                    this.func_75142_b();
                }
            } else {
                for (i = 0; i < 3; ++i) {
                    this.field_75167_g[i] = 0;
                    this.field_185001_h[i] = -1;
                    this.field_185002_i[i] = -1;
                }
            }
        }

    }

    public boolean func_75140_a(EntityPlayer entityhuman, int i) {
        ItemStack itemstack = this.field_75168_e.func_70301_a(0);
        ItemStack itemstack1 = this.field_75168_e.func_70301_a(1);
        int j = i + 1;

        if ((itemstack1.func_190926_b() || itemstack1.func_190916_E() < j) && !entityhuman.field_71075_bZ.field_75098_d) {
            return false;
        } else if (this.field_75167_g[i] > 0 && !itemstack.func_190926_b() && (entityhuman.field_71068_ca >= j && entityhuman.field_71068_ca >= this.field_75167_g[i] || entityhuman.field_71075_bZ.field_75098_d)) {
            if (!this.field_75172_h.field_72995_K) {
                List list = this.func_178148_a(itemstack, i, this.field_75167_g[i]);

                // CraftBukkit start
                if (true || !list.isEmpty()) {
                    // entityhuman.enchantDone(itemstack, j); // Moved down
                    boolean flag = itemstack.func_77973_b() == Items.field_151122_aG;
                    Map<org.bukkit.enchantments.Enchantment, Integer> enchants = new java.util.HashMap<org.bukkit.enchantments.Enchantment, Integer>();
                    for (Object obj : list) {
                        EnchantmentData instance = (EnchantmentData) obj;
                        enchants.put(org.bukkit.enchantments.Enchantment.getById(Enchantment.func_185258_b(instance.field_76302_b)), instance.field_76303_c);
                    }
                    CraftItemStack item = CraftItemStack.asCraftMirror(itemstack);

                    EnchantItemEvent event = new EnchantItemEvent((Player) entityhuman.getBukkitEntity(), this.getBukkitView(), this.field_75172_h.getWorld().getBlockAt(field_178150_j.func_177958_n(), field_178150_j.func_177956_o(), field_178150_j.func_177952_p()), item, this.field_75167_g[i], enchants, i);
                    this.field_75172_h.getServer().getPluginManager().callEvent(event);

                    int level = event.getExpLevelCost();
                    if (event.isCancelled() || (level > entityhuman.field_71068_ca && !entityhuman.field_71075_bZ.field_75098_d) || event.getEnchantsToAdd().isEmpty()) {
                        return false;
                    }

                    if (flag) {
                        itemstack = new ItemStack(Items.field_151134_bR);
                        this.field_75168_e.func_70299_a(0, itemstack);
                    }

                    for (Map.Entry<org.bukkit.enchantments.Enchantment, Integer> entry : event.getEnchantsToAdd().entrySet()) {
                        try {
                            if (flag) {
                                int enchantId = entry.getKey().getId();
                                if (Enchantment.func_185262_c(enchantId) == null) {
                                    continue;
                                }

                                EnchantmentData weightedrandomenchant = new EnchantmentData(Enchantment.func_185262_c(enchantId), entry.getValue());
                                ItemEnchantedBook.func_92115_a(itemstack, weightedrandomenchant);
                            } else {
                                item.addUnsafeEnchantment(entry.getKey(), entry.getValue());
                            }
                        } catch (IllegalArgumentException e) {
                            /* Just swallow invalid enchantments */
                        }
                    }

                    entityhuman.func_192024_a(itemstack, j);
                    // CraftBukkit end

                    // CraftBukkit - TODO: let plugins change this
                    if (!entityhuman.field_71075_bZ.field_75098_d) {
                        itemstack1.func_190918_g(j);
                        if (itemstack1.func_190926_b()) {
                            this.field_75168_e.func_70299_a(1, ItemStack.field_190927_a);
                        }
                    }

                    entityhuman.func_71029_a(StatList.field_188091_Y);
                    if (entityhuman instanceof EntityPlayerMP) {
                        CriteriaTriggers.field_192129_i.func_192190_a((EntityPlayerMP) entityhuman, itemstack, j);
                    }

                    this.field_75168_e.func_70296_d();
                    this.field_178149_f = entityhuman.func_175138_ci();
                    this.func_75130_a(this.field_75168_e);
                    this.field_75172_h.func_184133_a((EntityPlayer) null, this.field_178150_j, SoundEvents.field_190021_aL, SoundCategory.BLOCKS, 1.0F, this.field_75172_h.field_73012_v.nextFloat() * 0.1F + 0.9F);
                }
            }

            return true;
        } else {
            return false;
        }
    }

    private List<EnchantmentData> func_178148_a(ItemStack itemstack, int i, int j) {
        this.field_75169_l.setSeed((long) (this.field_178149_f + i));
        List list = EnchantmentHelper.func_77513_b(this.field_75169_l, itemstack, j, false);

        if (itemstack.func_77973_b() == Items.field_151122_aG && list.size() > 1) {
            list.remove(this.field_75169_l.nextInt(list.size()));
        }

        return list;
    }

    public void func_75134_a(EntityPlayer entityhuman) {
        super.func_75134_a(entityhuman);
        // CraftBukkit Start - If an enchantable was opened from a null location, set the world to the player's world, preventing a crash
        if (this.field_75172_h == null) {
            this.field_75172_h = entityhuman.func_130014_f_();
        }
        // CraftBukkit end
        if (!this.field_75172_h.field_72995_K) {
            this.func_193327_a(entityhuman, entityhuman.field_70170_p, this.field_75168_e);
        }
    }

    public boolean func_75145_c(EntityPlayer entityhuman) {
        if (!this.checkReachable) return true; // CraftBukkit
        return this.field_75172_h.func_180495_p(this.field_178150_j).func_177230_c() != Blocks.field_150381_bn ? false : entityhuman.func_70092_e((double) this.field_178150_j.func_177958_n() + 0.5D, (double) this.field_178150_j.func_177956_o() + 0.5D, (double) this.field_178150_j.func_177952_p() + 0.5D) <= 64.0D;
    }

    public ItemStack func_82846_b(EntityPlayer entityhuman, int i) {
        ItemStack itemstack = ItemStack.field_190927_a;
        Slot slot = (Slot) this.field_75151_b.get(i);

        if (slot != null && slot.func_75216_d()) {
            ItemStack itemstack1 = slot.func_75211_c();

            itemstack = itemstack1.func_77946_l();
            if (i == 0) {
                if (!this.func_75135_a(itemstack1, 2, 38, true)) {
                    return ItemStack.field_190927_a;
                }
            } else if (i == 1) {
                if (!this.func_75135_a(itemstack1, 2, 38, true)) {
                    return ItemStack.field_190927_a;
                }
            } else if (itemstack1.func_77973_b() == Items.field_151100_aR && EnumDyeColor.func_176766_a(itemstack1.func_77960_j()) == EnumDyeColor.BLUE) {
                if (!this.func_75135_a(itemstack1, 1, 2, true)) {
                    return ItemStack.field_190927_a;
                }
            } else {
                if (((Slot) this.field_75151_b.get(0)).func_75216_d() || !((Slot) this.field_75151_b.get(0)).func_75214_a(itemstack1)) {
                    return ItemStack.field_190927_a;
                }

                if (itemstack1.func_77942_o() && itemstack1.func_190916_E() == 1) {
                    ((Slot) this.field_75151_b.get(0)).func_75215_d(itemstack1.func_77946_l());
                    itemstack1.func_190920_e(0);
                } else if (!itemstack1.func_190926_b()) {
                    // Spigot start
                    ItemStack clone = itemstack1.func_77946_l();
                    clone.func_190920_e(1);
                    ((Slot) this.field_75151_b.get(0)).func_75215_d(clone);
                    // Spigot end
                    itemstack1.func_190918_g(1);
                }
            }

            if (itemstack1.func_190926_b()) {
                slot.func_75215_d(ItemStack.field_190927_a);
            } else {
                slot.func_75218_e();
            }

            if (itemstack1.func_190916_E() == itemstack.func_190916_E()) {
                return ItemStack.field_190927_a;
            }

            slot.func_190901_a(entityhuman, itemstack1);
        }

        return itemstack;
    }

    // CraftBukkit start
    @Override
    public CraftInventoryView getBukkitView() {
        if (bukkitEntity != null) {
            return bukkitEntity;
        }

        CraftInventoryEnchanting inventory = new CraftInventoryEnchanting(this.field_75168_e);
        bukkitEntity = new CraftInventoryView(this.player, inventory, this);
        return bukkitEntity;
    }
    // CraftBukkit end
}
