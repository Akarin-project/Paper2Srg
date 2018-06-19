package net.minecraft.entity.item;

import java.util.Iterator;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.destroystokyo.paper.HopperPusher;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.DamageSource;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.walkers.ItemStackData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

// CraftBukkit start
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
// CraftBukkit end
import org.bukkit.event.player.PlayerAttemptPickupItemEvent; // Paper
import com.destroystokyo.paper.HopperPusher; // Paper

// Paper start - implement HopperPusher
public class EntityItem extends Entity implements HopperPusher {
    @Override
    public boolean acceptItem(TileEntityHopper hopper) {
        return TileEntityHopper.putDropInInventory(null, hopper, this);
    }
// Paper end

    private static final Logger field_145803_d = LogManager.getLogger();
    private static final DataParameter<ItemStack> field_184533_c = EntityDataManager.func_187226_a(EntityItem.class, DataSerializers.field_187196_f);
    private int field_70292_b;
    public int field_145804_b;
    public boolean canMobPickup = true; // Paper
    private int field_70291_e;
    private String field_145801_f;
    private String field_145802_g;
    public float field_70290_d;
    private int lastTick = MinecraftServer.currentTick - 1; // CraftBukkit

    public EntityItem(World world, double d0, double d1, double d2) {
        super(world);
        this.field_70291_e = 5;
        this.field_70290_d = (float) (Math.random() * 3.141592653589793D * 2.0D);
        this.func_70105_a(0.25F, 0.25F);
        this.func_70107_b(d0, d1, d2);
        this.field_70177_z = (float) (Math.random() * 360.0D);
        this.field_70159_w = (double) ((float) (Math.random() * 0.20000000298023224D - 0.10000000149011612D));
        this.field_70181_x = 0.20000000298023224D;
        this.field_70179_y = (double) ((float) (Math.random() * 0.20000000298023224D - 0.10000000149011612D));
    }

    public EntityItem(World world, double d0, double d1, double d2, ItemStack itemstack) {
        this(world, d0, d1, d2);
        this.func_92058_a(itemstack);
    }

    protected boolean func_70041_e_() {
        return false;
    }

    public EntityItem(World world) {
        super(world);
        this.field_70291_e = 5;
        this.field_70290_d = (float) (Math.random() * 3.141592653589793D * 2.0D);
        this.func_70105_a(0.25F, 0.25F);
        this.func_92058_a(ItemStack.field_190927_a);
    }

    protected void func_70088_a() {
        this.func_184212_Q().func_187214_a(EntityItem.field_184533_c, ItemStack.field_190927_a);
    }

    public void func_70071_h_() {
        if (this.func_92059_d().func_190926_b()) {
            this.func_70106_y();
        } else {
            super.func_70071_h_();
            if (tryPutInHopper()) return; // Paper
            // CraftBukkit start - Use wall time for pickup and despawn timers
            int elapsedTicks = MinecraftServer.currentTick - this.lastTick;
            if (this.field_145804_b != 32767) this.field_145804_b -= elapsedTicks;
            if (this.field_70292_b != -32768) this.field_70292_b += elapsedTicks;
            this.lastTick = MinecraftServer.currentTick;
            // CraftBukkit end

            this.field_70169_q = this.field_70165_t;
            this.field_70167_r = this.field_70163_u;
            this.field_70166_s = this.field_70161_v;
            double d0 = this.field_70159_w;
            double d1 = this.field_70181_x;
            double d2 = this.field_70179_y;

            if (!this.func_189652_ae()) {
                this.field_70181_x -= 0.03999999910593033D;
            }

            if (this.field_70170_p.field_72995_K) {
                this.field_70145_X = false;
            } else {
                this.field_70145_X = this.func_145771_j(this.field_70165_t, (this.func_174813_aQ().field_72338_b + this.func_174813_aQ().field_72337_e) / 2.0D, this.field_70161_v);
            }

            this.func_70091_d(MoverType.SELF, this.field_70159_w, this.field_70181_x, this.field_70179_y);
            boolean flag = (int) this.field_70169_q != (int) this.field_70165_t || (int) this.field_70167_r != (int) this.field_70163_u || (int) this.field_70166_s != (int) this.field_70161_v;

            if (flag || this.field_70173_aa % 25 == 0) {
                if (this.field_70170_p.func_180495_p(new BlockPos(this)).func_185904_a() == Material.field_151587_i) {
                    this.field_70181_x = 0.20000000298023224D;
                    this.field_70159_w = (double) ((this.field_70146_Z.nextFloat() - this.field_70146_Z.nextFloat()) * 0.2F);
                    this.field_70179_y = (double) ((this.field_70146_Z.nextFloat() - this.field_70146_Z.nextFloat()) * 0.2F);
                    this.func_184185_a(SoundEvents.field_187658_bx, 0.4F, 2.0F + this.field_70146_Z.nextFloat() * 0.4F);
                }

                if (!this.field_70170_p.field_72995_K) {
                    this.func_85054_d();
                }
            }

            float f = 0.98F;

            if (this.field_70122_E) {
                f = this.field_70170_p.func_180495_p(new BlockPos(MathHelper.func_76128_c(this.field_70165_t), MathHelper.func_76128_c(this.func_174813_aQ().field_72338_b) - 1, MathHelper.func_76128_c(this.field_70161_v))).func_177230_c().field_149765_K * 0.98F;
            }

            this.field_70159_w *= (double) f;
            this.field_70181_x *= 0.9800000190734863D;
            this.field_70179_y *= (double) f;
            if (this.field_70122_E) {
                this.field_70181_x *= -0.5D;
            }

            /* Craftbukkit start - moved up
            if (this.age != -32768) {
                ++this.age;
            }
            // Craftbukkit end */

            this.func_70072_I();
            if (!this.field_70170_p.field_72995_K) {
                double d3 = this.field_70159_w - d0;
                double d4 = this.field_70181_x - d1;
                double d5 = this.field_70179_y - d2;
                double d6 = d3 * d3 + d4 * d4 + d5 * d5;

                if (d6 > 0.01D) {
                    this.field_70160_al = true;
                }
            }

            if (!this.field_70170_p.field_72995_K && this.field_70292_b >= field_70170_p.spigotConfig.itemDespawnRate) { // Spigot
                // CraftBukkit start - fire ItemDespawnEvent
                if (org.bukkit.craftbukkit.event.CraftEventFactory.callItemDespawnEvent(this).isCancelled()) {
                    this.field_70292_b = 0;
                    return;
                }
                // CraftBukkit end
                this.func_70106_y();
            }

        }
    }

    // Spigot start - copied from above
    @Override
    public void inactiveTick() {
        if (tryPutInHopper()) return; // Paper
        // CraftBukkit start - Use wall time for pickup and despawn timers
        int elapsedTicks = MinecraftServer.currentTick - this.lastTick;
        if (this.field_145804_b != 32767) this.field_145804_b -= elapsedTicks;
        if (this.field_70292_b != -32768) this.field_70292_b += elapsedTicks;
        this.lastTick = MinecraftServer.currentTick;
        // CraftBukkit end

        if (!this.field_70170_p.field_72995_K && this.field_70292_b >= field_70170_p.spigotConfig.itemDespawnRate) { // Spigot
            // CraftBukkit start - fire ItemDespawnEvent
            if (org.bukkit.craftbukkit.event.CraftEventFactory.callItemDespawnEvent(this).isCancelled()) {
                this.field_70292_b = 0;
                return;
            }
            // CraftBukkit end
            this.func_70106_y();
        }
    }
    // Spigot end

    private void func_85054_d() {
        // Spigot start
        double radius = field_70170_p.spigotConfig.itemMerge;
        Iterator iterator = this.field_70170_p.func_72872_a(EntityItem.class, this.func_174813_aQ().func_72314_b(radius, radius, radius)).iterator();
        // Spigot end

        while (iterator.hasNext()) {
            EntityItem entityitem = (EntityItem) iterator.next();

            this.func_70289_a(entityitem);
        }

    }

    private boolean func_70289_a(EntityItem entityitem) {
        if (entityitem == this) {
            return false;
        } else if (entityitem.func_70089_S() && this.func_70089_S()) {
            ItemStack itemstack = this.func_92059_d();
            ItemStack itemstack1 = entityitem.func_92059_d();

            if (this.field_145804_b != 32767 && entityitem.field_145804_b != 32767) {
                if (this.field_70292_b != -32768 && entityitem.field_70292_b != -32768) {
                    if (itemstack1.func_77973_b() != itemstack.func_77973_b()) {
                        return false;
                    } else if (itemstack1.func_77942_o() ^ itemstack.func_77942_o()) {
                        return false;
                    } else if (itemstack1.func_77942_o() && !itemstack1.func_77978_p().equals(itemstack.func_77978_p())) {
                        return false;
                    } else if (itemstack1.func_77973_b() == null) {
                        return false;
                    } else if (itemstack1.func_77973_b().func_77614_k() && itemstack1.func_77960_j() != itemstack.func_77960_j()) {
                        return false;
                    } else if (itemstack1.func_190916_E() < itemstack.func_190916_E()) {
                        return entityitem.func_70289_a(this);
                    } else if (itemstack1.func_190916_E() + itemstack.func_190916_E() > itemstack1.func_77976_d()) {
                        return false;
                    } else {
                        // Spigot start
                        if (org.bukkit.craftbukkit.event.CraftEventFactory.callItemMergeEvent(entityitem, this).isCancelled()) return false; // CraftBukkit
                        itemstack.func_190917_f(itemstack1.func_190916_E());
                        this.field_145804_b = Math.max(entityitem.field_145804_b, this.field_145804_b);
                        this.field_70292_b = Math.min(entityitem.field_70292_b, this.field_70292_b);
                        this.func_92058_a(itemstack);
                        entityitem.func_70106_y();
                        // Spigot end
                        return true;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void func_70288_d() {
        this.field_70292_b = 4800;
    }

    public boolean func_70072_I() {
        if (this.field_70170_p.func_72918_a(this.func_174813_aQ(), Material.field_151586_h, (Entity) this)) {
            if (!this.field_70171_ac && !this.field_70148_d) {
                this.func_71061_d_();
            }

            this.field_70171_ac = true;
        } else {
            this.field_70171_ac = false;
        }

        return this.field_70171_ac;
    }

    protected void burn(int i) {
        this.func_70097_a(DamageSource.field_76372_a, (float) i);
    }

    public boolean func_70097_a(DamageSource damagesource, float f) {
        if (this.func_180431_b(damagesource)) {
            return false;
        } else if (!this.func_92059_d().func_190926_b() && this.func_92059_d().func_77973_b() == Items.field_151156_bN && damagesource.func_94541_c()) {
            return false;
        } else {
            // CraftBukkit start
            if (org.bukkit.craftbukkit.event.CraftEventFactory.handleNonLivingEntityDamageEvent(this, damagesource, f)) {
                return false;
            }
            // CraftBukkit end
            this.func_70018_K();
            this.field_70291_e = (int) ((float) this.field_70291_e - f);
            if (this.field_70291_e <= 0) {
                this.func_70106_y();
            }

            return false;
        }
    }

    public static void func_189742_a(DataFixer dataconvertermanager) {
        dataconvertermanager.func_188258_a(FixTypes.ENTITY, (IDataWalker) (new ItemStackData(EntityItem.class, new String[] { "Item"})));
    }

    public void func_70014_b(NBTTagCompound nbttagcompound) {
        nbttagcompound.func_74777_a("Health", (short) this.field_70291_e);
        nbttagcompound.func_74777_a("Age", (short) this.field_70292_b);
        nbttagcompound.func_74777_a("PickupDelay", (short) this.field_145804_b);
        if (this.func_145800_j() != null) {
            nbttagcompound.func_74778_a("Thrower", this.field_145801_f);
        }

        if (this.func_145798_i() != null) {
            nbttagcompound.func_74778_a("Owner", this.field_145802_g);
        }

        if (!this.func_92059_d().func_190926_b()) {
            nbttagcompound.func_74782_a("Item", this.func_92059_d().func_77955_b(new NBTTagCompound()));
        }

    }

    public void func_70037_a(NBTTagCompound nbttagcompound) {
        this.field_70291_e = nbttagcompound.func_74765_d("Health");
        this.field_70292_b = nbttagcompound.func_74765_d("Age");
        if (nbttagcompound.func_74764_b("PickupDelay")) {
            this.field_145804_b = nbttagcompound.func_74765_d("PickupDelay");
        }

        if (nbttagcompound.func_74764_b("Owner")) {
            this.field_145802_g = nbttagcompound.func_74779_i("Owner");
        }

        if (nbttagcompound.func_74764_b("Thrower")) {
            this.field_145801_f = nbttagcompound.func_74779_i("Thrower");
        }

        NBTTagCompound nbttagcompound1 = nbttagcompound.func_74775_l("Item");

        this.func_92058_a(new ItemStack(nbttagcompound1));
        if (this.func_92059_d().func_190926_b()) {
            this.func_70106_y();
        }

    }

    public void func_70100_b_(EntityPlayer entityhuman) {
        if (!this.field_70170_p.field_72995_K) {
            ItemStack itemstack = this.func_92059_d();
            Item item = itemstack.func_77973_b();
            int i = itemstack.func_190916_E();

            // CraftBukkit start - fire PlayerPickupItemEvent
            int canHold = entityhuman.field_71071_by.canHold(itemstack);
            int remaining = i - canHold;
            boolean flyAtPlayer = false; // Paper

            // Paper start
            if (this.field_145804_b <= 0) {
                PlayerAttemptPickupItemEvent attemptEvent = new PlayerAttemptPickupItemEvent((org.bukkit.entity.Player) entityhuman.getBukkitEntity(), (org.bukkit.entity.Item) this.getBukkitEntity(), remaining);
                this.field_70170_p.getServer().getPluginManager().callEvent(attemptEvent);

                flyAtPlayer = attemptEvent.getFlyAtPlayer();
                if (attemptEvent.isCancelled()) {
                    if (flyAtPlayer) {
                        entityhuman.func_71001_a(this, i);
                    }

                    return;
                }
            }
            // Paper end

            if (this.field_145804_b <= 0 && canHold > 0) {
                itemstack.func_190920_e(canHold);
                // Call legacy event
                PlayerPickupItemEvent playerEvent = new PlayerPickupItemEvent((org.bukkit.entity.Player) entityhuman.getBukkitEntity(), (org.bukkit.entity.Item) this.getBukkitEntity(), remaining);
                playerEvent.setCancelled(!entityhuman.canPickUpLoot);
                this.field_70170_p.getServer().getPluginManager().callEvent(playerEvent);
                flyAtPlayer = playerEvent.getFlyAtPlayer(); // Paper
                if (playerEvent.isCancelled()) {
                    // Paper Start
                    if (flyAtPlayer) {
                        entityhuman.func_71001_a(this, i);
                    }
                    // Paper End
                    return;
                }

                // Call newer event afterwards
                EntityPickupItemEvent entityEvent = new EntityPickupItemEvent((org.bukkit.entity.Player) entityhuman.getBukkitEntity(), (org.bukkit.entity.Item) this.getBukkitEntity(), remaining);
                entityEvent.setCancelled(!entityhuman.canPickUpLoot);
                this.field_70170_p.getServer().getPluginManager().callEvent(entityEvent);
                if (entityEvent.isCancelled()) {
                    return;
                }

                itemstack.func_190920_e(canHold + remaining);

                // Possibly < 0; fix here so we do not have to modify code below
                this.field_145804_b = 0;
            }
            // CraftBukkit end

            if (this.field_145804_b == 0 && (this.field_145802_g == null || 6000 - this.field_70292_b <= 200 || this.field_145802_g.equals(entityhuman.func_70005_c_())) && entityhuman.field_71071_by.func_70441_a(itemstack)) {
                // Paper Start
                if (flyAtPlayer) {
                    entityhuman.func_71001_a(this, i);
                }
                // Paper End
                if (itemstack.func_190926_b()) {
                    this.func_70106_y();
                    itemstack.func_190920_e(i);
                }

                entityhuman.func_71064_a(StatList.func_188056_d(item), i);
            }

        }
    }

    public String func_70005_c_() {
        return this.func_145818_k_() ? this.func_95999_t() : I18n.func_74838_a("item." + this.func_92059_d().func_77977_a());
    }

    public boolean func_70075_an() {
        return false;
    }

    @Nullable
    public Entity func_184204_a(int i) {
        Entity entity = super.func_184204_a(i);

        if (!this.field_70170_p.field_72995_K && entity instanceof EntityItem) {
            ((EntityItem) entity).func_85054_d();
        }

        return entity;
    }

    public ItemStack func_92059_d() {
        return (ItemStack) this.func_184212_Q().func_187225_a(EntityItem.field_184533_c);
    }

    public void func_92058_a(ItemStack itemstack) {
        this.func_184212_Q().func_187227_b(EntityItem.field_184533_c, itemstack);
        this.func_184212_Q().func_187217_b(EntityItem.field_184533_c);
    }

    public String func_145798_i() {
        return this.field_145802_g;
    }

    public void func_145797_a(String s) {
        this.field_145802_g = s;
    }

    public String func_145800_j() {
        return this.field_145801_f;
    }

    public void func_145799_b(String s) {
        this.field_145801_f = s;
    }

    public void func_174869_p() {
        this.field_145804_b = 10;
    }

    public void func_174868_q() {
        this.field_145804_b = 0;
    }

    public void func_174871_r() {
        this.field_145804_b = 32767;
    }

    public void func_174867_a(int i) {
        this.field_145804_b = i;
    }

    public boolean func_174874_s() {
        return this.field_145804_b > 0;
    }

    public void func_174873_u() {
        this.field_70292_b = -6000;
    }

    public void func_174870_v() {
        this.func_174871_r();
        this.field_70292_b = field_70170_p.spigotConfig.itemDespawnRate - 1; // Spigot
    }
}
