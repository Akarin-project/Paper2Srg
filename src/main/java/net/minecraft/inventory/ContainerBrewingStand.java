package net.minecraft.inventory;

// CraftBukkit start
import org.bukkit.craftbukkit.inventory.CraftInventoryBrewer;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionHelper;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;

// CraftBukkit end

public class ContainerBrewingStand extends Container {

    private final IInventory field_75188_e;
    private final Slot field_75186_f;
    private int field_184998_g;
    private int field_184999_h;

    // CraftBukkit start
    private CraftInventoryView bukkitEntity = null;
    private InventoryPlayer player;
    // CraftBukkit end

    public ContainerBrewingStand(InventoryPlayer playerinventory, IInventory iinventory) {
        player = playerinventory; // CraftBukkit
        this.field_75188_e = iinventory;
        this.func_75146_a((new ContainerBrewingStand.Potion(iinventory, 0, 56, 51)));
        this.func_75146_a((new ContainerBrewingStand.Potion(iinventory, 1, 79, 58)));
        this.func_75146_a((new ContainerBrewingStand.Potion(iinventory, 2, 102, 51)));
        this.field_75186_f = this.func_75146_a((new ContainerBrewingStand.Ingredient(iinventory, 3, 79, 17)));
        this.func_75146_a((new ContainerBrewingStand.a(iinventory, 4, 17, 17)));

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

    @Override
    public void func_75132_a(IContainerListener icrafting) {
        super.func_75132_a(icrafting);
        icrafting.func_175173_a(this, this.field_75188_e);
    }

    @Override
    public void func_75142_b() {
        super.func_75142_b();

        for (int i = 0; i < this.field_75149_d.size(); ++i) {
            IContainerListener icrafting = this.field_75149_d.get(i);

            if (this.field_184998_g != this.field_75188_e.func_174887_a_(0)) {
                icrafting.func_71112_a(this, 0, this.field_75188_e.func_174887_a_(0));
            }

            if (this.field_184999_h != this.field_75188_e.func_174887_a_(1)) {
                icrafting.func_71112_a(this, 1, this.field_75188_e.func_174887_a_(1));
            }
        }

        this.field_184998_g = this.field_75188_e.func_174887_a_(0);
        this.field_184999_h = this.field_75188_e.func_174887_a_(1);
    }

    @Override
    public boolean func_75145_c(EntityPlayer entityhuman) {
        if (!this.checkReachable) return true; // CraftBukkit
        return this.field_75188_e.func_70300_a(entityhuman);
    }

    @Override
    public ItemStack func_82846_b(EntityPlayer entityhuman, int i) {
        ItemStack itemstack = ItemStack.field_190927_a;
        Slot slot = this.field_75151_b.get(i);

        if (slot != null && slot.func_75216_d()) {
            ItemStack itemstack1 = slot.func_75211_c();

            itemstack = itemstack1.func_77946_l();
            if ((i < 0 || i > 2) && i != 3 && i != 4) {
                if (this.field_75186_f.func_75214_a(itemstack1)) {
                    if (!this.func_75135_a(itemstack1, 3, 4, false)) {
                        return ItemStack.field_190927_a;
                    }
                } else if (ContainerBrewingStand.Potion.func_75243_a_(itemstack) && itemstack.func_190916_E() == 1) {
                    if (!this.func_75135_a(itemstack1, 0, 3, false)) {
                        return ItemStack.field_190927_a;
                    }
                } else if (ContainerBrewingStand.a.b_(itemstack)) {
                    if (!this.func_75135_a(itemstack1, 4, 5, false)) {
                        return ItemStack.field_190927_a;
                    }
                } else if (i >= 5 && i < 32) {
                    if (!this.func_75135_a(itemstack1, 32, 41, false)) {
                        return ItemStack.field_190927_a;
                    }
                } else if (i >= 32 && i < 41) {
                    if (!this.func_75135_a(itemstack1, 5, 32, false)) {
                        return ItemStack.field_190927_a;
                    }
                } else if (!this.func_75135_a(itemstack1, 5, 41, false)) {
                    return ItemStack.field_190927_a;
                }
            } else {
                if (!this.func_75135_a(itemstack1, 5, 41, true)) {
                    return ItemStack.field_190927_a;
                }

                slot.func_75220_a(itemstack1, itemstack);
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

    static class a extends Slot {

        public a(IInventory iinventory, int i, int j, int k) {
            super(iinventory, i, j, k);
        }

        @Override
        public boolean func_75214_a(ItemStack itemstack) {
            return b_(itemstack);
        }

        public static boolean b_(ItemStack itemstack) {
            return itemstack.func_77973_b() == Items.field_151065_br;
        }

        @Override
        public int func_75219_a() {
            return 64;
        }
    }

    static class Ingredient extends Slot {

        public Ingredient(IInventory iinventory, int i, int j, int k) {
            super(iinventory, i, j, k);
        }

        @Override
        public boolean func_75214_a(ItemStack itemstack) {
            return PotionHelper.func_185205_a(itemstack);
        }

        @Override
        public int func_75219_a() {
            return 64;
        }
    }

    static class Potion extends Slot {

        public Potion(IInventory iinventory, int i, int j, int k) {
            super(iinventory, i, j, k);
        }

        @Override
        public boolean func_75214_a(ItemStack itemstack) {
            return func_75243_a_(itemstack);
        }

        @Override
        public int func_75219_a() {
            return 1;
        }

        @Override
        public ItemStack func_190901_a(EntityPlayer entityhuman, ItemStack itemstack) {
            PotionType potionregistry = PotionUtils.func_185191_c(itemstack);

            if (entityhuman instanceof EntityPlayerMP) {
                CriteriaTriggers.field_192130_j.func_192173_a((EntityPlayerMP) entityhuman, potionregistry);
            }

            super.func_190901_a(entityhuman, itemstack);
            return itemstack;
        }

        public static boolean func_75243_a_(ItemStack itemstack) {
            Item item = itemstack.func_77973_b();

            return item == Items.field_151068_bn || item == Items.field_185155_bH || item == Items.field_185156_bI || item == Items.field_151069_bo;
        }
    }

    // CraftBukkit start
    @Override
    public CraftInventoryView getBukkitView() {
        if (bukkitEntity != null) {
            return bukkitEntity;
        }

        CraftInventoryBrewer inventory = new CraftInventoryBrewer(this.field_75188_e);
        bukkitEntity = new CraftInventoryView(this.player.field_70458_d.getBukkitEntity(), inventory, this);
        return bukkitEntity;
    }
    // CraftBukkit end
}
