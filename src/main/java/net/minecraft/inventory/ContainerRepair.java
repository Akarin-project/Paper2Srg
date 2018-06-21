package net.minecraft.inventory;

import java.util.Iterator;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.BlockAnvil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;

// CraftBukkit start
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
// CraftBukkit end

public class ContainerRepair extends Container {

    private static final Logger field_148326_f = LogManager.getLogger();
    private final IInventory field_82852_f = new InventoryCraftResult();
    private final IInventory field_82853_g = new InventoryBasic("Repair", true, 2) {
        public void func_70296_d() {
            super.func_70296_d();
            ContainerRepair.this.func_75130_a((IInventory) this);
        }
    };
    private final World field_82860_h;
    private final BlockPos field_178156_j;
    public int field_82854_e;
    public int field_82856_l;
    public String field_82857_m;
    private final EntityPlayer field_82855_n;
    // CraftBukkit start
    private int lastLevelCost;
    private CraftInventoryView bukkitEntity;
    private InventoryPlayer player;
    // CraftBukkit end

    public ContainerRepair(InventoryPlayer playerinventory, final World world, final BlockPos blockposition, EntityPlayer entityhuman) {
        this.player = playerinventory; // CraftBukkit
        this.field_178156_j = blockposition;
        this.field_82860_h = world;
        this.field_82855_n = entityhuman;
        this.func_75146_a(new Slot(this.field_82853_g, 0, 27, 47));
        this.func_75146_a(new Slot(this.field_82853_g, 1, 76, 47));
        this.func_75146_a(new Slot(this.field_82852_f, 2, 134, 47) {
            public boolean func_75214_a(ItemStack itemstack) {
                return false;
            }

            public boolean func_82869_a(EntityPlayer entityhuman) {
                return (entityhuman.field_71075_bZ.field_75098_d || entityhuman.field_71068_ca >= ContainerRepair.this.field_82854_e) && ContainerRepair.this.field_82854_e > 0 && this.func_75216_d();
            }

            public ItemStack func_190901_a(EntityPlayer entityhuman, ItemStack itemstack) {
                if (!entityhuman.field_71075_bZ.field_75098_d) {
                    entityhuman.func_82242_a(-ContainerRepair.this.field_82854_e);
                }

                ContainerRepair.this.field_82853_g.func_70299_a(0, ItemStack.field_190927_a);
                if (ContainerRepair.this.field_82856_l > 0) {
                    ItemStack itemstack1 = ContainerRepair.this.field_82853_g.func_70301_a(1);

                    if (!itemstack1.func_190926_b() && itemstack1.func_190916_E() > ContainerRepair.this.field_82856_l) {
                        itemstack1.func_190918_g(ContainerRepair.this.field_82856_l);
                        ContainerRepair.this.field_82853_g.func_70299_a(1, itemstack1);
                    } else {
                        ContainerRepair.this.field_82853_g.func_70299_a(1, ItemStack.field_190927_a);
                    }
                } else {
                    ContainerRepair.this.field_82853_g.func_70299_a(1, ItemStack.field_190927_a);
                }

                ContainerRepair.this.field_82854_e = 0;
                IBlockState iblockdata = world.func_180495_p(blockposition);

                if (!entityhuman.field_71075_bZ.field_75098_d && !world.field_72995_K && iblockdata.func_177230_c() == Blocks.field_150467_bQ && entityhuman.func_70681_au().nextFloat() < 0.12F) {
                    int i = ((Integer) iblockdata.func_177229_b(BlockAnvil.field_176505_b)).intValue();

                    ++i;
                    if (i > 2) {
                        world.func_175698_g(blockposition);
                        world.func_175718_b(1029, blockposition, 0);
                    } else {
                        world.func_180501_a(blockposition, iblockdata.func_177226_a(BlockAnvil.field_176505_b, Integer.valueOf(i)), 2);
                        world.func_175718_b(1030, blockposition, 0);
                    }
                } else if (!world.field_72995_K) {
                    world.func_175718_b(1030, blockposition, 0);
                }

                return itemstack;
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

    }

    public void func_75130_a(IInventory iinventory) {
        super.func_75130_a(iinventory);
        if (iinventory == this.field_82853_g) {
            this.func_82848_d();
        }

    }

    public void func_82848_d() {
        ItemStack itemstack = this.field_82853_g.func_70301_a(0);

        this.field_82854_e = 1;
        int i = 0;
        byte b0 = 0;
        byte b1 = 0;

        if (itemstack.func_190926_b()) {
            org.bukkit.craftbukkit.event.CraftEventFactory.callPrepareAnvilEvent(getBukkitView(), ItemStack.field_190927_a); // CraftBukkit
            this.field_82854_e = 0;
        } else {
            ItemStack itemstack1 = itemstack.func_77946_l();
            ItemStack itemstack2 = this.field_82853_g.func_70301_a(1);
            Map map = EnchantmentHelper.func_82781_a(itemstack1);
            int j = b0 + itemstack.func_82838_A() + (itemstack2.func_190926_b() ? 0 : itemstack2.func_82838_A());

            this.field_82856_l = 0;
            if (!itemstack2.func_190926_b()) {
                boolean flag = itemstack2.func_77973_b() == Items.field_151134_bR && !ItemEnchantedBook.func_92110_g(itemstack2).func_82582_d();
                int k;
                int l;
                int i1;

                if (itemstack1.func_77984_f() && itemstack1.func_77973_b().func_82789_a(itemstack, itemstack2)) {
                    k = Math.min(itemstack1.func_77952_i(), itemstack1.func_77958_k() / 4);
                    if (k <= 0) {
                        org.bukkit.craftbukkit.event.CraftEventFactory.callPrepareAnvilEvent(getBukkitView(), ItemStack.field_190927_a); // CraftBukkit
                        this.field_82854_e = 0;
                        return;
                    }

                    for (l = 0; k > 0 && l < itemstack2.func_190916_E(); ++l) {
                        i1 = itemstack1.func_77952_i() - k;
                        itemstack1.func_77964_b(i1);
                        ++i;
                        k = Math.min(itemstack1.func_77952_i(), itemstack1.func_77958_k() / 4);
                    }

                    this.field_82856_l = l;
                } else {
                    if (!flag && (itemstack1.func_77973_b() != itemstack2.func_77973_b() || !itemstack1.func_77984_f())) {
                        org.bukkit.craftbukkit.event.CraftEventFactory.callPrepareAnvilEvent(getBukkitView(), ItemStack.field_190927_a); // CraftBukkit
                        this.field_82854_e = 0;
                        return;
                    }

                    if (itemstack1.func_77984_f() && !flag) {
                        k = itemstack.func_77958_k() - itemstack.func_77952_i();
                        l = itemstack2.func_77958_k() - itemstack2.func_77952_i();
                        i1 = l + itemstack1.func_77958_k() * 12 / 100;
                        int j1 = k + i1;
                        int k1 = itemstack1.func_77958_k() - j1;

                        if (k1 < 0) {
                            k1 = 0;
                        }

                        if (k1 < itemstack1.func_77960_j()) {
                            itemstack1.func_77964_b(k1);
                            i += 2;
                        }
                    }

                    Map map1 = EnchantmentHelper.func_82781_a(itemstack2);
                    boolean flag1 = false;
                    boolean flag2 = false;
                    Iterator iterator = map1.keySet().iterator();

                    while (iterator.hasNext()) {
                        Enchantment enchantment = (Enchantment) iterator.next();

                        if (enchantment != null) {
                            int l1 = map.containsKey(enchantment) ? ((Integer) map.get(enchantment)).intValue() : 0;
                            int i2 = ((Integer) map1.get(enchantment)).intValue();

                            i2 = l1 == i2 ? i2 + 1 : Math.max(i2, l1);
                            boolean flag3 = enchantment.func_92089_a(itemstack);

                            if (this.field_82855_n.field_71075_bZ.field_75098_d || itemstack.func_77973_b() == Items.field_151134_bR) {
                                flag3 = true;
                            }

                            Iterator iterator1 = map.keySet().iterator();

                            while (iterator1.hasNext()) {
                                Enchantment enchantment1 = (Enchantment) iterator1.next();

                                if (enchantment1 != enchantment && !enchantment.func_191560_c(enchantment1)) {
                                    flag3 = false;
                                    ++i;
                                }
                            }

                            if (!flag3) {
                                flag2 = true;
                            } else {
                                flag1 = true;
                                if (i2 > enchantment.func_77325_b()) {
                                    i2 = enchantment.func_77325_b();
                                }

                                map.put(enchantment, Integer.valueOf(i2));
                                int j2 = 0;

                                switch (enchantment.func_77324_c()) {
                                case COMMON:
                                    j2 = 1;
                                    break;

                                case UNCOMMON:
                                    j2 = 2;
                                    break;

                                case RARE:
                                    j2 = 4;
                                    break;

                                case VERY_RARE:
                                    j2 = 8;
                                }

                                if (flag) {
                                    j2 = Math.max(1, j2 / 2);
                                }

                                i += j2 * i2;
                                if (itemstack.func_190916_E() > 1) {
                                    i = 40;
                                }
                            }
                        }
                    }

                    if (flag2 && !flag1) {
                        org.bukkit.craftbukkit.event.CraftEventFactory.callPrepareAnvilEvent(getBukkitView(), ItemStack.field_190927_a); // CraftBukkit
                        this.field_82854_e = 0;
                        return;
                    }
                }
            }

            if (StringUtils.isBlank(this.field_82857_m)) {
                if (itemstack.func_82837_s()) {
                    b1 = 1;
                    i += b1;
                    itemstack1.func_135074_t();
                }
            } else if (!this.field_82857_m.equals(itemstack.func_82833_r())) {
                b1 = 1;
                i += b1;
                itemstack1.func_151001_c(this.field_82857_m);
            }

            this.field_82854_e = j + i;
            if (i <= 0) {
                itemstack1 = ItemStack.field_190927_a;
            }

            if (b1 == i && b1 > 0 && this.field_82854_e >= 40) {
                this.field_82854_e = 39;
            }

            if (this.field_82854_e >= 40 && !this.field_82855_n.field_71075_bZ.field_75098_d) {
                itemstack1 = ItemStack.field_190927_a;
            }

            if (!itemstack1.func_190926_b()) {
                int k2 = itemstack1.func_82838_A();

                if (!itemstack2.func_190926_b() && k2 < itemstack2.func_82838_A()) {
                    k2 = itemstack2.func_82838_A();
                }

                if (b1 != i || b1 == 0) {
                    k2 = k2 * 2 + 1;
                }

                itemstack1.func_82841_c(k2);
                EnchantmentHelper.func_82782_a(map, itemstack1);
            }

            org.bukkit.craftbukkit.event.CraftEventFactory.callPrepareAnvilEvent(getBukkitView(), itemstack1); // CraftBukkit
            this.func_75142_b();
        }
    }

    public void func_75132_a(IContainerListener icrafting) {
        super.func_75132_a(icrafting);
        icrafting.func_71112_a(this, 0, this.field_82854_e);
    }

    public void func_75134_a(EntityPlayer entityhuman) {
        super.func_75134_a(entityhuman);
        if (!this.field_82860_h.field_72995_K) {
            this.func_193327_a(entityhuman, this.field_82860_h, this.field_82853_g);
        }
    }

    public boolean func_75145_c(EntityPlayer entityhuman) {
        if (!this.checkReachable) return true; // CraftBukkit
        return this.field_82860_h.func_180495_p(this.field_178156_j).func_177230_c() != Blocks.field_150467_bQ ? false : entityhuman.func_70092_e((double) this.field_178156_j.func_177958_n() + 0.5D, (double) this.field_178156_j.func_177956_o() + 0.5D, (double) this.field_178156_j.func_177952_p() + 0.5D) <= 64.0D;
    }

    public ItemStack func_82846_b(EntityPlayer entityhuman, int i) {
        ItemStack itemstack = ItemStack.field_190927_a;
        Slot slot = (Slot) this.field_75151_b.get(i);

        if (slot != null && slot.func_75216_d()) {
            ItemStack itemstack1 = slot.func_75211_c();

            itemstack = itemstack1.func_77946_l();
            if (i == 2) {
                if (!this.func_75135_a(itemstack1, 3, 39, true)) {
                    return ItemStack.field_190927_a;
                }

                slot.func_75220_a(itemstack1, itemstack);
            } else if (i != 0 && i != 1) {
                if (i >= 3 && i < 39 && !this.func_75135_a(itemstack1, 0, 2, false)) {
                    return ItemStack.field_190927_a;
                }
            } else if (!this.func_75135_a(itemstack1, 3, 39, false)) {
                return ItemStack.field_190927_a;
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

    public void func_82850_a(String s) {
        this.field_82857_m = s;
        if (this.func_75139_a(2).func_75216_d()) {
            ItemStack itemstack = this.func_75139_a(2).func_75211_c();

            if (StringUtils.isBlank(s)) {
                itemstack.func_135074_t();
            } else {
                itemstack.func_151001_c(this.field_82857_m);
            }
        }

        this.func_82848_d();
    }

    // CraftBukkit start
    @Override
    public void func_75142_b() {
        super.func_75142_b();

        for (int i = 0; i < this.field_75149_d.size(); ++i) {
            IContainerListener icrafting = (IContainerListener) this.field_75149_d.get(i);

            //if (this.lastLevelCost != this.levelCost) { // Paper - this was the wrong solution to this, fixing it correctly in CraftPlayer
                icrafting.func_71112_a(this, 0, this.field_82854_e);
            //} // Paper
        }

        this.lastLevelCost = this.field_82854_e;
    }

    @Override
    public CraftInventoryView getBukkitView() {
        if (bukkitEntity != null) {
            return bukkitEntity;
        }

        org.bukkit.craftbukkit.inventory.CraftInventory inventory = new org.bukkit.craftbukkit.inventory.CraftInventoryAnvil(
                new org.bukkit.Location(field_82860_h.getWorld(), field_178156_j.func_177958_n(), field_178156_j.func_177956_o(), field_178156_j.func_177952_p()), this.field_82853_g, this.field_82852_f, this);
        bukkitEntity = new CraftInventoryView(this.player.field_70458_d.getBukkitEntity(), inventory, this);
        return bukkitEntity;
    }
    // CraftBukkit end
}
